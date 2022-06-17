package net.gbicc.app.irr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.jpa.service.support.protocol.criteria.FetchMode;
import org.wsp.framework.jpa.service.support.protocol.criteria.TextMatchStyle;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import net.gbicc.app.irr.jpa.entity.IrrIndexOptionEntity;
import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.entity.IrrSplitRelationEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrSplitIndexRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.JavaJsEvalUtil;
import net.gbicc.app.irr.service.IrrIndexOptionService;
import net.gbicc.app.irr.service.IrrSplitIndexService;
import net.gbicc.app.irr.service.IrrSplitRelationService;
import net.sf.json.JSONObject;

/**
* 拆分指标信息
*/
@Service
public class IrrSplitIndexServiceImpl extends DaoServiceImpl<IrrSplitIndexEntity, String, IrrSplitIndexRepository> 
	implements IrrSplitIndexService {

	private static final Logger LOG = LogManager.getLogger(IrrSplitIndexServiceImpl.class);
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private IrrSplitRelationService irrSplitRelationService;
	@Autowired private IrrIndexOptionService irrIndexOptionService;
	@Autowired private UserService userService;
	
	/**
	 * 根据用户角色查询用户的MapJsonString
	 * @param userRole 用户角色
	 */
	@Override
	public JSONObject getUserMapJsonString(String userRole,String orgId,String roleAble) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.FD_ID,U.FD_USERNAME||'('||O.FD_NAME||')' USERNAME FROM FR_AA_USER U INNER JOIN FR_AA_USER_ROLE UR ON U.FD_ID=UR.FD_USER_ID ");
		sql.append("INNER JOIN FR_AA_ROLE R ON UR.FD_ROLE_ID=R.FD_ID INNER JOIN FR_AA_USER_ORG UO ON U.FD_ID=UO.FD_USER_ID ");
		sql.append("INNER JOIN FR_AA_ORG O ON UO.FD_ORG_ID=O.FD_ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(userRole)) {
			sql.append("AND R.FD_CODE='"+userRole+"' ");
		}
		if(StringUtils.isNotBlank(orgId)) {
			sql.append("AND O.FD_ID='"+orgId+"' ");
		}
		if(StringUtils.isNotBlank(roleAble)) {
			sql.append("AND R.FD_ENABLE='"+roleAble+"' ");
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
		if(list == null || list.size() <= 0) {
			return null;
		}
		JSONObject json = new JSONObject();
		for(Map<String, Object> map : list) {
			json.put(map.get("FD_ID"), map.get("USERNAME"));
		}
		return json;
	}

	@Override
	public List<IrrSplitIndexEntity> findSplitIndexByCollUser(String collUserId) throws Exception  {
		return findSplitIndexByCollUserAndUse(collUserId,IrrEnum.YESNO_Y.getValue());
	}

	@Override
	public List<IrrSplitIndexEntity> findSplitIndexByCollUserAndUse(String collUserId, String isUse) throws Exception {
		IrrSplitIndexEntity exampleEntity = new IrrSplitIndexEntity();
		exampleEntity.setCollUserId(collUserId);
		exampleEntity.setIsUse(isUse);
		QueryParameter qp = new QueryParameter();
		qp.setFetchMode(FetchMode.EMPTY_CRITERIA_EMPTY);
		qp.setTextMatchStyle(TextMatchStyle.substring);
		return fetch(exampleEntity, qp);
	}
	
	@Override
	public Map<String, IrrSplitIndexEntity> getSplitIndexMap(List<IrrSplitIndexEntity> list){
		Map<String, IrrSplitIndexEntity> map = new HashMap<String, IrrSplitIndexEntity>();
		if(CollectionUtils.isEmpty(list)) {
			return map;
		}
		for(IrrSplitIndexEntity split : list) {
			map.put(split.getSplitCode(), split);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findSplitCollUser(String isUse, String collWay) {
		String sql = "SELECT DISTINCT COLL_USER_LOGINNAME FROM T_IRR_SPLIT_INDEX WHERE IS_USE='"+isUse+"' AND COLL_WAY='"+collWay+"'";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<String> findSplitExamUserByCollUserLoginName(String collUserLoginName, String isUse) {
		String sql = "SELECT DISTINCT EXAM_USER_LOGINNAME FROM T_IRR_SPLIT_INDEX WHERE IS_USE='"+isUse+"' AND COLL_USER_LOGINNAME='"+collUserLoginName+"'";
		return jdbcTemplate.queryForList(sql,String.class);
	}

	@Override
	public List<String> findSplitReviewUserByCollUserLoginName(String collUserLoginName, String isUse) {
		String sql = "SELECT DISTINCT REVIEW_USER_LOGINNAME FROM T_IRR_SPLIT_INDEX WHERE IS_USE='"+isUse+"' AND COLL_USER_LOGINNAME='"+collUserLoginName+"'";
		return jdbcTemplate.queryForList(sql,String.class);
	}

	@Override
	public List<String> findSubProcessByCollUserLoginName(String collUserLoginName,String isUse) {
		String sql = "SELECT DISTINCT SUB_PROCESS_CODE FROM T_IRR_SPLIT_INDEX WHERE IS_USE='"+isUse+"' AND COLL_USER_LOGINNAME='"+collUserLoginName+"'";
		return jdbcTemplate.queryForList(sql,String.class);
	}

	@Override
	public List<IndexValueDTO> summSplitIndex(List<IndexValueDTO> baseIndex) throws Exception {
		if(CollectionUtils.isEmpty(baseIndex)) {
			LOG.error("无法汇总全部拆分指标：传入的参数为空!");
			return null;
		}
		/*查询所有的系统计算的拆分指标*/
		IrrSplitIndexEntity exampleSplit = new IrrSplitIndexEntity();
		exampleSplit.setCollWay(IrrEnum.COLL_WAY_CALC.getValue());
		List<IrrSplitIndexEntity> sysCalcSplit = fetch(exampleSplit, IrrQueryParameter.getIrrDefaultQP());
		/*计算非得分拆分指标：不适用*/
		List<IrrSplitIndexEntity> disableSplit = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_DISABLE.getValue(), IrrEnum.YESNO_N.getValue(), IrrEnum.YESNO_N.getValue());
		List<IndexValueDTO> disableSplitResult = summSplitIndexDisable(disableSplit);
		baseIndex.addAll(disableSplitResult);
		/*计算非得分拆分指标：简答*/
		List<IrrSplitIndexEntity> noneSplit = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_NONE.getValue(), IrrEnum.YESNO_N.getValue(), IrrEnum.YESNO_Y.getValue());
		List<IndexValueDTO> noneSplitResult = summSplitIndexNone(noneSplit, baseIndex);
		baseIndex.addAll(noneSplitResult);
		/*计算非得分拆分指标：数值*/
		List<IrrSplitIndexEntity> numSplit = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue(), IrrEnum.YESNO_N.getValue(), IrrEnum.YESNO_Y.getValue());
		List<IndexValueDTO> numSplitResult = summSplitIndexNum(numSplit, baseIndex);
		baseIndex.addAll(numSplitResult);
		/*计算非得分拆分指标：选项*/
		List<IrrSplitIndexEntity> optionSplit = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_OPTION.getValue(), IrrEnum.YESNO_N.getValue(), IrrEnum.YESNO_Y.getValue());
		List<IndexValueDTO> optionSplitResult = summSplitIndexOption(optionSplit,baseIndex);
		baseIndex.addAll(optionSplitResult);
		/*计算得分拆分指标：不适用*/
		List<IrrSplitIndexEntity> disableSplitScore = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_DISABLE.getValue(), IrrEnum.YESNO_Y.getValue(), IrrEnum.YESNO_N.getValue());
		List<IndexValueDTO> disableSplitScoreResult = summSplitIndexDisable(disableSplitScore);
		baseIndex.addAll(disableSplitScoreResult);
		/*计算得分拆分指标：简答*/
		List<IrrSplitIndexEntity> noneSplitScore = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_NONE.getValue(), IrrEnum.YESNO_Y.getValue(), IrrEnum.YESNO_Y.getValue());
		List<IndexValueDTO> noneSplitScoreResult = summSplitIndexNone(noneSplitScore, baseIndex);
		baseIndex.addAll(noneSplitScoreResult);
		/*计算得分拆分指标：数值*/
		List<IrrSplitIndexEntity> numSplitScore = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue(), IrrEnum.YESNO_Y.getValue(), IrrEnum.YESNO_Y.getValue());
		List<IndexValueDTO> numSplitScoreResult = summSplitIndexNum(numSplitScore, baseIndex);
		baseIndex.addAll(numSplitScoreResult);
		/*计算得分拆分指标：选项*/
		List<IrrSplitIndexEntity> optionSplitScore = findSplitByCondition(IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_STATUS_ENABLE.getValue(), 
				IrrEnum.INDEX_RESULT_TYPE_OPTION.getValue(), IrrEnum.YESNO_Y.getValue(), IrrEnum.YESNO_Y.getValue());
		List<IndexValueDTO> optionSplitScoreResult = summSplitIndexOption(optionSplitScore, baseIndex);
		baseIndex.addAll(optionSplitScoreResult);
		return baseIndex;
	}

	@Override
	public List<IndexValueDTO> summSplitIndexNum(List<IrrSplitIndexEntity> calcIndex, List<IndexValueDTO> baseIndex)
			throws Exception {
		if(CollectionUtils.isEmpty(calcIndex) || CollectionUtils.isEmpty(baseIndex)) {
			LOG.error("无法汇总数值拆分指标：传入的参数为空！");
			return null;
		}
		Map<String,String> baseMap = new HashMap<String,String>();
		for(IndexValueDTO dto : baseIndex) {
			baseMap.put(dto.getCode(), dto.getValue());
		}
		List<IndexValueDTO> returnList = new ArrayList<IndexValueDTO>();
		for(IrrSplitIndexEntity index : calcIndex) {//拆分指标计算
			if(index == null) {
				continue;
			}
			IndexValueDTO value = new IndexValueDTO();
			value.setIndexId(index.getId());
			value.setCode(index.getSplitCode());
			value.setName(index.getSplitName());
			String splitEvalForm = index.getSplitEvalForm();
			if(StringUtils.isBlank(splitEvalForm)) {
				returnList.add(value);
				continue;
			}
			IrrSplitRelationEntity exampleSplitRela = new IrrSplitRelationEntity();
			exampleSplitRela.setSplitId(index.getId());
			List<IrrSplitRelationEntity> relaList = irrSplitRelationService.fetch(exampleSplitRela, IrrQueryParameter.getIrrDefaultQP());
			if(CollectionUtils.isEmpty(relaList)) {
				returnList.add(value);
				continue;
			}
			for(IrrSplitRelationEntity rela : relaList) {//替换拆分编码为拆分值
				if(rela == null) {
					continue;
				}
				String relaCode = rela.getRelaCode();
				if(StringUtils.isBlank(relaCode)) {
					continue;
				}
				String relaValue = baseMap.get(relaCode);
				splitEvalForm = splitEvalForm.replace(relaCode, relaValue);
			}
			Object evalValue = JavaJsEvalUtil.evalStr(splitEvalForm);
			if(evalValue != null && !"null".equals(evalValue)) {
				value.setValue(evalValue.toString());
			}
			returnList.add(value);
		}
		return returnList;
	}

	@Override
	public List<IndexValueDTO> summSplitIndexOption(List<IrrSplitIndexEntity> calcIndex, List<IndexValueDTO> baseIndex)
			throws Exception {
		if(CollectionUtils.isEmpty(calcIndex) || CollectionUtils.isEmpty(baseIndex)) {
			LOG.error("无法汇总选项拆分指标：传入的参数为空！");
			return null;
		}
		List<IrrIndexOptionEntity> options = irrIndexOptionService.findAllMaxPoints();
		if(CollectionUtils.isEmpty(options)) {
			LOG.error("无法汇总选项拆分指标：无指标选项信息！");
			return null;
		}
		Map<String, String> baseMap = new HashMap<String,String>();
		for(IrrIndexOptionEntity option : options) {
			baseMap.put(option.getIndexId(), option.getOptionName());
		}
		List<IndexValueDTO> returnList = new ArrayList<IndexValueDTO>();
		for(IrrSplitIndexEntity index : calcIndex) {
			if(index == null) {
				continue;
			}
			IndexValueDTO value = new IndexValueDTO();
			value.setIndexId(index.getId());
			value.setCode(index.getSplitCode());
			value.setName(index.getSplitName());
			value.setValue(baseMap.get(index.getSourceIndexId()));
			returnList.add(value);
		}
		return returnList;
	}

	@Override
	public List<IndexValueDTO> summSplitIndexNone(List<IrrSplitIndexEntity> calcIndex, List<IndexValueDTO> baseIndex)
			throws Exception {
		/*简答的为封面的，在采集过程中已经采集了，无需计算*/
		return baseIndex;
	}

	@Override
	public List<IndexValueDTO> summSplitIndexDisable(List<IrrSplitIndexEntity> calcIndex)
			throws Exception {
		if(CollectionUtils.isEmpty(calcIndex)) {
			LOG.error("无法汇总不适用拆分指标：传入的参数为空！");
			return null;
		}
		List<IndexValueDTO> returnList = new ArrayList<IndexValueDTO>();
		for(IrrSplitIndexEntity split : calcIndex) {
			IndexValueDTO indexValue = new IndexValueDTO();
			indexValue.setIndexId(split.getId());
			indexValue.setCode(split.getSplitCode());
			indexValue.setName(split.getSplitName());
			indexValue.setValue(IrrEnum.INDEX_NOT_APPLICABIE_VALUE.getValue());
			returnList.add(indexValue);
		}
		return returnList;
	}

	@Override
	public List<IrrSplitIndexEntity> findSplitByCondition(String isUse, String indexStatus, String resultType,
			String isScore, String isApplicabie) {
		StringBuffer sb = new StringBuffer("SELECT SI.* FROM T_IRR_SPLIT_INDEX SI INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(isUse)) {
			sb.append("AND SI.IS_USE='"+isUse+"' ");
		}
		if(StringUtils.isNotBlank(indexStatus)) {
			sb.append("AND II.INDEX_STATUS='"+indexStatus+"' ");
		}
		if(StringUtils.isNotBlank(resultType)) {
			sb.append("AND SI.RESULT_TYPE='"+resultType+"' ");
		}
		if(StringUtils.isNotBlank(isScore)) {
			sb.append("AND II.IS_SCORE_INDEX='"+isScore+"' ");
		}
		if(StringUtils.isNotBlank(isApplicabie)) {
			sb.append("AND II.IS_APPLICABIE='"+isApplicabie+"' ");
		}
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IrrSplitIndexEntity>(IrrSplitIndexEntity.class));
	}
	
	@Override
	public List<String> findExamUserByCondition(String isUse, String indexStatus, String indexLevel, String orgId) {
		StringBuffer sb = new StringBuffer("SELECT DISTINCT SI.EXAM_USER_LOGINNAME FROM T_IRR_INDEX_INFO II INNER JOIN T_IRR_SPLIT_INDEX SI ON II.ID=SI.SOURCE_INDEX_ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(isUse)) {
			sb.append("AND SI.IS_USE='"+isUse+"' ");
		}
		if(StringUtils.isNotBlank(indexStatus)) {
			sb.append("AND II.INDEX_STATUS='"+indexStatus+"' ");	
		}
		if(StringUtils.isNotBlank(indexLevel)) {
			sb.append("AND II.INDEX_LEVEL='"+indexLevel+"' ");
		}
		if(StringUtils.isNotBlank(orgId)) {
			sb.append("AND SI.ORG_ID='"+orgId+"' ");
		}
		return jdbcTemplate.queryForList(sb.toString(), String.class);
	}

	@Override
	public List<IrrSplitIndexEntity> findSplitIndexByCondition(String isUse, String collWay, String indexStatus,
			String indexLevel,String orgId) {
		StringBuffer sb = new StringBuffer("SELECT SI.* FROM T_IRR_SPLIT_INDEX SI INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(isUse)) {
			sb.append("AND SI.IS_USE='"+isUse+"' ");
		}
		if(StringUtils.isNotBlank(collWay)) {
			sb.append("AND SI.COLL_WAY='"+collWay+"' ");
		}
		if(StringUtils.isNotBlank(indexStatus)) {
			sb.append("AND II.INDEX_STATUS='"+indexStatus+"' ");
		}
		if(StringUtils.isNotBlank(indexLevel)) {
			sb.append("AND II.INDEX_LEVEL='"+indexLevel+"' ");
		}
		if(StringUtils.isNotBlank(orgId)) {
			sb.append("AND SI.ORG_ID='"+orgId+"' ");
		}
		sb.append("ORDER BY SI.SPLIT_CODE");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IrrSplitIndexEntity>(IrrSplitIndexEntity.class));
	}

	@Override
	public List<IrrSplitIndexEntity> findSplitIndexByUserIdAndCollWay(String userId, String collWay) throws Exception{
		User user = userService.findById(SecurityUtil.getUserId());
		List<Org> orgs = user.getOrgs();
		if(CollectionUtils.isEmpty(orgs)) {
			throw new IrrProcessException("当前人没有机构");
		}
		Org org = orgs.get(0);
		List<IrrSplitIndexEntity> list = null;
		if(org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {//分公司
			list = findSplitIndexByCondition(IrrEnum.YESNO_Y.getValue(), collWay,
					IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_BRANCH.getValue(),null);
		}else {//总公司
			list = findSplitIndexByCondition(IrrEnum.YESNO_Y.getValue(), collWay,
					IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.INDEX_LEVEL_HEAD.getValue(),org.getId());
		}
		return list;
	}

	@Override
	public List<IrrSplitIndexEntity> findSplitExamIsNullByCondition(String isUse) throws Exception {
		String sql = "SELECT * FROM T_IRR_SPLIT_INDEX WHERE EXAM_USER_LOGINNAME IS NULL AND IS_USE='"+isUse+"'";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrSplitIndexEntity>(IrrSplitIndexEntity.class));
	}

	@Override
	public Map<String, Object> updateSplitIndexUse(String ids) throws Exception{
		if(StringUtils.isBlank(ids)) {
			throw new RuntimeException("无法变更，拆分指标ID为空！");
		}
		String[] idArr = ids.split(",");
		for(String id : idArr) {
			IrrSplitIndexEntity splitIndex = findById(id);
			String isUse = splitIndex.getIsUse();
			if(StringUtils.isBlank(isUse)) {
				splitIndex.setIsUse(IrrEnum.YESNO_Y.getValue());
			}else if(IrrEnum.YESNO_Y.getValue().equals(isUse)) {
				splitIndex.setIsUse(IrrEnum.YESNO_N.getValue());
			}else {
				splitIndex.setIsUse(IrrEnum.YESNO_Y.getValue());
			}
			update(splitIndex.getId(),splitIndex);
		}
		return IrrUtil.getMap(true, "变更成功！");
	}

}
