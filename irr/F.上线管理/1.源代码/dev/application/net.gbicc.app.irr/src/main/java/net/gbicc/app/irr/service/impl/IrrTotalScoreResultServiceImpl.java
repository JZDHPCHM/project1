package net.gbicc.app.irr.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.OrgService;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.IrrTotalScoreResultEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrTotalScoreResultRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProjTypeEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.JavaJsEvalUtil;
import net.gbicc.app.irr.service.IrrOrgResultService;
import net.gbicc.app.irr.service.IrrProjTypeService;
import net.gbicc.app.irr.service.IrrTotalScoreResultService;

/**
* 总分多维度结果
*/
@Service
@Transactional
public class IrrTotalScoreResultServiceImpl extends DaoServiceImpl<IrrTotalScoreResultEntity, String, IrrTotalScoreResultRepository> 
	implements IrrTotalScoreResultService {
	
	@Autowired private SystemDictionaryService systemDictionaryService;
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private OrgService orgService;
	@Autowired private IrrOrgResultService irrOrgResultService;
	@Autowired private IrrProjTypeService irrProjTypeService;

	@Override
	public List<IrrTotalScoreResultEntity> totalScoreSumm(IrrTaskEntity task) throws Exception {
		List<IrrTotalScoreResultEntity> returnList = new ArrayList<IrrTotalScoreResultEntity>();
		if(task == null) {
			return returnList;
		}
		//在此系统上报的评估项目
		Map<String, String> xbrlProjMap = systemDictionaryService.getDictionaryMap(IrrEnum.XBRL_PROJ.getValue(), Locale.CHINA);
		if(MapUtils.isEmpty(xbrlProjMap)) {
			throw new IrrProcessException("数据字典中没有配置在此系统上报的评估项目！");
		}
		//删除同期次数据
		jdbcTemplate.execute("DELETE FROM T_IRR_TOTALSCORE_RESULT WHERE TASK_ID='"+task.getId()+"'");
		List<Org> orgList = orgService.getRepository().findAll();
		if(CollectionUtils.isEmpty(orgList)) {
			throw new IrrProcessException("组织机构不存在！");
		}
		Org rootOrg = orgService.getRepository().findByCode(IrrEnum.ORG_ROOT_CODE.getValue()); 
		if(rootOrg == null) {
			throw new IrrProcessException("不存在机构编码为"+IrrEnum.ORG_ROOT_CODE.getValue()+"的机构！");
		}
		//查询不上报的分公司
		Map<String, String> branchXBRLOrgExclude = systemDictionaryService.getDictionaryMap(IrrEnum.BRANCH_XBRL_ORG_EXCLUDE.getValue(), Locale.CHINA);
		for(String key : xbrlProjMap.keySet()) {
			if(StringUtils.isBlank(key) || key.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
				continue;
			}
			IrrProjTypeEntity projExam = new IrrProjTypeEntity();
			projExam.setTypeCode(key);
			List<IrrProjTypeEntity> projList = irrProjTypeService.fetch(projExam, IrrQueryParameter.getIrrDefaultQP());
			if(CollectionUtils.isEmpty(projList) || projList.get(0) == null) {
				throw new IrrProcessException("不存在编码为"+key+"的评估项目！");
			}
			IrrProjTypeEntity proj = projList.get(0);
			String projName = xbrlProjMap.get(key);
			if(StringUtils.isNotBlank(projName) && IrrTemplateEnum.SUMMARY_FILE_BRANCH_PROJ_FIRST.getValue().equals(projName.substring(0, 1))) {
				//分公司评估项目多维度汇总
				for(Org org : orgList) {
					if(org == null || StringUtils.isBlank(org.getName()) 
							|| !org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())
							|| !IrrUtil.strObjectIsEmpty(branchXBRLOrgExclude.get(org.getCode()))) {
						//不是分公司或者不是需上报的分公司，则跳过
						continue;
					}
					List<IrrTotalScoreResultEntity> branchTotalScoreList = summOrgTotalScore(task,proj,org);
					if(CollectionUtils.isNotEmpty(branchTotalScoreList)) {
						returnList.addAll(branchTotalScoreList);
					}
				}
			}else {
				//总公司评估项目多维度汇总
				List<IrrTotalScoreResultEntity> headTotalScoreList = summOrgTotalScore(task,proj,rootOrg);
				if(CollectionUtils.isNotEmpty(headTotalScoreList)) {
					returnList.addAll(headTotalScoreList);
				}
			}
		}
		//不在此系统上报的评估项目(5大类),可直接去相应的上传数据表中根据编码取到
		/*List<IrrTotalScoreResultEntity> otherTotalScoreResult = summOtherProjTotalScore(task, rootOrg);
		if(CollectionUtils.isNotEmpty(otherTotalScoreResult)) {
			returnList.addAll(otherTotalScoreResult);
		}*/
		return returnList;
	}

	@Override
	public List<IrrTotalScoreResultEntity> summOrgTotalScore(IrrTaskEntity task,IrrProjTypeEntity proj,Org org) throws Exception {
		if(task == null || proj == null || org == null) {
			return null;
		}
		List<IrrTotalScoreResultEntity> returnList = new ArrayList<IrrTotalScoreResultEntity>();
		//总分:指标得分相加
		IrrTotalScoreResultEntity totalScoreResult = createTotalScoreResult(task,proj,org);
		totalScoreResult.setResultFlag(IrrEnum.TOTAL_SCORE_FLAG.getValue());
		totalScoreResult.setProjResult(irrOrgResultService.sumOrgIndexTotalScoreResult(task, proj, org).toString());
		returnList.add(add(totalScoreResult));
		//行业水平确定可得:分三种计算方法
		IrrTotalScoreResultEntity induSureResult = createTotalScoreResult(task,proj,org);
		induSureResult.setResultFlag(IrrEnum.INDU_LEVEL_FLAG.getValue());
		if(IrrEnum.OR_08.getValue().equals(proj.getTypeCode())) {//OR08需要特殊处理(行业水平确定可得计算方式为特定的指标)
			//IF(最近4个季度公司自查发现理赔管理操作风险事件的次数+最近4个季度公司发生业内欺诈案件的次数=0,10,0)
			IrrOrgResultEntity orgExam = new IrrOrgResultEntity();
			orgExam.setTaskId(task.getId());
			orgExam.setOrgId(org.getId());
			orgExam.setIndexCode(IrrEnum.OR08029.getValue());
			List<IrrOrgResultEntity> OR08029ResultList = irrOrgResultService.fetch(orgExam, IrrQueryParameter.getIrrDefaultQP());
			orgExam.setIndexCode(IrrEnum.OR08031.getValue());
			List<IrrOrgResultEntity> OR08031ResultList = irrOrgResultService.fetch(orgExam, IrrQueryParameter.getIrrDefaultQP());
			if(CollectionUtils.isNotEmpty(OR08029ResultList) && CollectionUtils.isNotEmpty(OR08031ResultList) 
					&& OR08029ResultList.get(0) != null && OR08031ResultList.get(0) != null
					&& StringUtils.isNotBlank(OR08029ResultList.get(0).getIndexResultQ2())
					&& StringUtils.isNotBlank(OR08031ResultList.get(0).getIndexResultQ2())) {
				Double temp = Double.valueOf(OR08029ResultList.get(0).getIndexResultQ2()) + Double.valueOf(OR08031ResultList.get(0).getIndexResultQ2());
				if(Double.valueOf(IrrEnum.DEFAULT_NUM.getValue()).equals(temp)) {
					induSureResult.setProjResult(IrrEnum.OR08_MAX_INDULEVEL_NUM.getValue());
				}else {
					induSureResult.setProjResult(IrrEnum.DEFAULT_NUM.getValue());
				}
			}else {
				induSureResult.setProjResult(IrrEnum.DEFAULT_NUM.getValue());
			}
		}else if(IrrEnum.OR_04.getValue().equals(proj.getTypeCode()) || IrrEnum.OR_12.getValue().equals(proj.getTypeCode())
				|| IrrEnum.OR_13.getValue().equals(proj.getTypeCode())){//最大值最小值
			//IF(行业水平指标得分=满分,满分,0)
			induSureResult.setProjResult(irrOrgResultService.calcOrgScoreInduLevelResult(task, proj, org).toString());
		}else {//直接获取得分
			induSureResult.setProjResult(irrOrgResultService.sumInduOrgScore(task, proj, org).toString());
		}
		returnList.add(add(induSureResult));
		//行业无法确定:行业的指标的标准分之和-行业水平确定可得
		IrrTotalScoreResultEntity induCanNotResult = createTotalScoreResult(task,proj,org);
		induCanNotResult.setResultFlag(IrrEnum.INDU_CAN_NOT_FLAG.getValue());
		Double induStanValue = irrOrgResultService.sumInduIndexStanValue(proj);
		induCanNotResult.setProjResult(String.valueOf(induStanValue - Double.valueOf(induSureResult.getProjResult())));
		returnList.add(add(induCanNotResult));
		//直接确定可得:总分-行业水平确定可得
		IrrTotalScoreResultEntity direDeteResult = createTotalScoreResult(task,proj,org);
		direDeteResult.setResultFlag(IrrEnum.DIRE_DETE_FLAG.getValue());
		direDeteResult.setProjResult(JavaJsEvalUtil.evalStr(totalScoreResult.getProjResult()+"-"+induSureResult.getProjResult()).toString());
		returnList.add(add(direDeteResult));
		//监管评分:100-评估项目所有得分指标标准分之和
		IrrTotalScoreResultEntity reguGradResult = createTotalScoreResult(task,proj,org);
		reguGradResult.setResultFlag(IrrEnum.REGU_GRAD_FLAG.getValue());
		Double projScoreIndexStanSum = irrOrgResultService.sumProjScoreIndexStanValue(proj);
		reguGradResult.setProjResult(String.valueOf(Double.valueOf(IrrEnum.DEFAULT_STAN_MAX.getValue()) - projScoreIndexStanSum));
		returnList.add(add(reguGradResult));
		//直接扣分:100-直接确定可得-行业水平确定可得-行业无法确定-监管评分
		IrrTotalScoreResultEntity directPointsResult = createTotalScoreResult(task,proj,org);
		directPointsResult.setResultFlag(IrrEnum.DIRECT_POINTS_FLAG.getValue());
		String pointEvalFormula = IrrEnum.DEFAULT_STAN_MAX.getValue() + "-" 
									+ Math.abs(Double.valueOf(direDeteResult.getProjResult())) + "-" 
									+ Math.abs(Double.valueOf(induSureResult.getProjResult())) + "-" 
									+ Math.abs(Double.valueOf(induCanNotResult.getProjResult())) + "-"
									+ Math.abs(Double.valueOf(reguGradResult.getProjResult()));
		directPointsResult.setProjResult(JavaJsEvalUtil.evalStr(pointEvalFormula).toString());
		returnList.add(add(directPointsResult));
		return returnList;
	}
	
	/**
	 * 生成多维度对象
	 * @param task 评估计划
	 * @param proj 评估项目
	 * @param org  机构
	 * @return
	 */
	private IrrTotalScoreResultEntity createTotalScoreResult(IrrTaskEntity task,IrrProjTypeEntity proj,Org org) {
		IrrTotalScoreResultEntity entity = new IrrTotalScoreResultEntity();
		entity.setProjId(proj.getId());
		entity.setProjCode(proj.getTypeCode());
		entity.setProjName(proj.getTypeName());
		entity.setTaskId(task.getId());
		entity.setTaskBatch(task.getTaskBatch());
		entity.setOrgId(org.getId());
		return entity;
	}

	@Override
	public List<Map<String, Object>> findDistinctTotalScoreProjByTask(IrrTaskEntity task) throws Exception {
		if(task == null) {
			return null;
		}
		String sql = "SELECT DISTINCT IOR.PROJ_ID,PT2.ID,PT2.TYPE_CODE,PT2.TYPE_NAME FROM T_IRR_TOTALSCORE_RESULT IOR INNER JOIN T_IRR_PROJ_TYPE PT\r\n" + 
				"ON IOR.PROJ_ID=PT.ID INNER JOIN T_IRR_PROJ_TYPE PT2 ON PT.PID=PT2.ID WHERE IOR.TASK_ID='"+task.getId()+"'";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public BigDecimal calcWeight(IrrTaskEntity task, IrrProjTypeEntity proj, String dim)
			throws Exception {
		if(task == null || proj == null || StringUtils.isBlank(dim)) {
			throw new IrrProcessException("无法计算权重:计算传入的参数存在空!");
		}
		String sql = "SELECT TR.* FROM T_IRR_TOTALSCORE_RESULT TR INNER JOIN FR_AA_ORG ORG ON TR.ORG_ID=ORG.FD_ID\r\n" + 
				"WHERE TR.PROJ_CODE='"+proj.getTypeCode()+"' AND TR.TASK_ID='"+task.getId()+"' AND TR.RESULT_FLAG='"+dim+"' ";
		//判断是分公司、总公司
		if(StringUtils.isNotBlank(proj.getTypeName())
				&& IrrTemplateEnum.SUMMARY_FILE_BRANCH_PROJ_FIRST.getValue().equals(proj.getTypeName().substring(0, 1))) {
			//分公司
			sql += " AND ORG.FD_CODE<>'"+IrrEnum.ORG_ROOT_CODE.getValue()+"'";
		}else {
			//总公司
			sql += " AND ORG.FD_CODE='"+IrrEnum.ORG_ROOT_CODE.getValue()+"'";
		}
		List<IrrTotalScoreResultEntity> resultList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<IrrTotalScoreResultEntity>(IrrTotalScoreResultEntity.class));
		if(CollectionUtils.isEmpty(resultList)) {
			return new BigDecimal("0");
		}
		//取平均值
		Double result = 0d;
		for(IrrTotalScoreResultEntity totalScore : resultList) {
			String score = totalScore.getProjResult();
			if(IrrUtil.isIntOrDouble(score)) {
				result += Double.valueOf(score);
			}
		}
		return new BigDecimal(result/resultList.size());
	}

	@Override
	public BigDecimal calcFiveProj(IrrTaskEntity task, String typeNameTable, String dim, String typeCode) throws Exception {
		if(task == null || StringUtils.isBlank(typeNameTable) || StringUtils.isBlank(dim)) {
			throw new IrrProcessException("无法计算权重:计算传入的参数存在空!");
		}
		String[] infoArr = typeNameTable.split(IrrEnum.SEPARATOR.getValue());
		if(infoArr == null || infoArr.length < 3) {
			throw new IrrProcessException("无法计算五大类权重:数据字典配置的五大类信息不规范!");
		}
		//结果表编码
		String tableCode = infoArr[1];
		//采集频率
		String frequency = infoArr[2];
		String taskBatch = task.getTaskBatch();
		if(IrrEnum.FIVE_PRPJ_COLL_FREQ_YEAR.getValue().equals(frequency)) {
			taskBatch = task.getTaskBatch().substring(0, 4);
		}
        //流动性风险-总分：取百分制后
        if (IrrEnum.TOTAL_SCORE_FLAG.getValue().equals(dim) && IrrEnum.LR01_TYPE_CODE.getValue().equals(typeCode)) {
		   dim = IrrEnum.LR01_CENT_AFTER_CODE.getValue(); 
		}
		String sql = "SELECT INDEX_SCORE FROM "+tableCode+" WHERE TASK_BATCH='"+taskBatch+"' AND INDEX_CODE='"+dim+"'";
		List<String> indexScore = jdbcTemplate.queryForList(sql,String.class);
		if(CollectionUtils.isEmpty(indexScore) || IrrUtil.strObjectIsEmpty(indexScore.get(0))) {
			return new BigDecimal("0");
		}
		return new BigDecimal(indexScore.get(0).toString());
	}

	@Override
	public BigDecimal calcDirectPointSpecialHand(IrrTaskEntity task, IrrProjTypeEntity proj) throws Exception {
		if(task == null || proj == null) {
			throw new IrrProcessException("无法计算权重:计算传入的参数存在空!");
		}
		String indexValueSql = "SELECT SUM(II.INDEX_VALUE) FROM T_IRR_INDEX_INFO II WHERE II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.IS_SCORE_INDEX='"+IrrEnum.YESNO_Y.getValue()+"' "
				+ "AND II.SCOR_TYPE='"+IrrEnum.SCORE_TYPE_DIRECT.getValue()+"' AND II.PROJ_TYPE_ID='"+proj.getId()+"'";
		String calcIndexValueSql = "SELECT TR.PROJ_RESULT FROM T_IRR_TOTALSCORE_RESULT TR WHERE TR.PROJ_ID='"+proj.getId()+"' AND TR.TASK_ID='"+task.getId()+"' AND TR.RESULT_FLAG='"+IrrEnum.DIRE_DETE_FLAG.getValue()+"'";
		BigDecimal indexValue = jdbcTemplate.queryForObject(indexValueSql, BigDecimal.class);
		BigDecimal calcIndexValue = jdbcTemplate.queryForObject(calcIndexValueSql, BigDecimal.class);
		if(indexValue == null) {
			indexValue = new BigDecimal("0");
		}
		if(calcIndexValue == null) {
			calcIndexValue = new BigDecimal("0");
		}
		return indexValue.subtract(calcIndexValue);
	}

	@Override
	public String findProjResultByCondition(String taskId, String dim, String projCode, String orgId) throws Exception {
		String sql = "SELECT ITR.PROJ_RESULT FROM T_IRR_TOTALSCORE_RESULT ITR WHERE 1=1 ";
		if(StringUtils.isNotBlank(taskId)) {
			sql += "AND ITR.TASK_ID='"+taskId+"' ";
		}
		if(StringUtils.isNotBlank(dim)) {
			sql += "AND ITR.RESULT_FLAG='"+dim+"' ";
		}
		if(StringUtils.isNotBlank(projCode)) {
			sql += "AND ITR.PROJ_CODE='"+projCode+"' ";
		}
		if(StringUtils.isNotBlank(orgId)) {
			sql += "AND ITR.ORG_ID='"+orgId+"' ";
		}
		List<String> list = jdbcTemplate.queryForList(sql, String.class);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}


}
