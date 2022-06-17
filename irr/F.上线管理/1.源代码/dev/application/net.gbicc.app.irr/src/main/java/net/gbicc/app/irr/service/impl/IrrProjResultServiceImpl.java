package net.gbicc.app.irr.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.org.entity.QOrg;
import org.wsp.framework.jpa.model.org.repository.OrgRepository;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.querydsl.core.BooleanBuilder;

import net.gbicc.app.irr.jpa.entity.IrrProjResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrProjResultRepository;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.dto.WeightInfoDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.enums.WeightEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrProjResultService;
import net.gbicc.app.irr.service.IrrProjTypeService;
import net.gbicc.app.irr.service.IrrTotalScoreResultService;

/**
* 评估项目得分
*/
@Service
@Transactional
public class IrrProjResultServiceImpl extends DaoServiceImpl<IrrProjResultEntity, String, IrrProjResultRepository> 
	implements IrrProjResultService {
	
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private IrrTotalScoreResultService irrTotalScoreResultService;
	@Autowired private SystemDictionaryService systemDictionaryService;
	@Autowired private IrrProjTypeService irrProjTypeService;
	@Autowired private OrgRepository orgRepository;

	@Override
	public List<IrrProjResultEntity> calcProjRate(IrrTaskEntity task) throws Exception {
		if(task == null) {
			return null;
		}
		//删除同期次数据
		jdbcTemplate.execute("DELETE FROM T_IRR_PROJ_RESULT WHERE TASK_ID='"+task.getId()+"'");
		//权重维度
		Map<String, String> projTypeDimMap = systemDictionaryService.getDictionaryMap(IrrEnum.PROJ_TYPE_DIM.getValue(), Locale.CHINA);
		//权重内容
		Map<String, String> weightMap = systemDictionaryService.getDictionaryMap(IrrEnum.WEIGHT_FLAG.getValue(), Locale.CHINA);
		//五大类
		Map<String, String> otherProjMap = systemDictionaryService.getDictionaryMap(IrrEnum.OTHER_PROJ.getValue(), Locale.CHINA);
        //声誉风险权重
        Map<String, String> rrWeightMap = systemDictionaryService.getDictionaryMap(IrrEnum.RR_WEIGHT.getValue(),
                Locale.CHINA);
        //合规风险权重
        Map<String, String> oraWeightMap = systemDictionaryService.getDictionaryMap(IrrEnum.ORA_WEIGHT.getValue(),
                Locale.CHINA);
        if (MapUtils.isEmpty(projTypeDimMap) || MapUtils.isEmpty(weightMap) || MapUtils.isEmpty(otherProjMap)
                || MapUtils.isEmpty(rrWeightMap) || MapUtils.isEmpty(oraWeightMap)) {
			throw new IrrProcessException("无法计算权重:数据字典配置不完全!");
		}
		//直接扣分特殊处理评估项目
		Map<String,String> directPointSpecialProjMap = systemDictionaryService.getDictionaryMap(IrrEnum.DIRECT_POINT_SPECIAL_PROJ.getValue(), Locale.CHINA);
		//组装权重中评估项目的子类
		Map<String, List<IrrProjTypeEntity>> weightProjChildMap = new HashMap<String, List<IrrProjTypeEntity>>();
		for(String key : weightMap.keySet()) {
			weightProjChildMap.put(key, irrProjTypeService.findProjChildByParentCode(key));
		}
		//查询评估项目基础信息
		List<IrrProjTypeEntity> projList = irrProjTypeService.getRepository().findAll();
		if(CollectionUtils.isEmpty(projList)) {
			throw new IrrProcessException("无法计算权重:不存在评估项目!");
		}
		Map<String, IrrProjTypeEntity> projMap = new HashMap<String, IrrProjTypeEntity>();
		for(IrrProjTypeEntity proj : projList) {
			projMap.put(proj.getTypeCode(), proj);
		}
		List<IrrProjResultEntity> returnList = new ArrayList<IrrProjResultEntity>();
		for(String key : projTypeDimMap.keySet()) {
			int index = 0;
			Map<String, IrrProjResultEntity> dimProjResultMap = new HashMap<String, IrrProjResultEntity>();
			for(String projTypeCode : weightMap.keySet()) {
				IrrProjResultEntity projResult = new IrrProjResultEntity();
				projResult.setResultFlag(key);
				projResult.setTaskId(task.getId());
				projResult.setTaskBatch(task.getTaskBatch());
				projResult.setTypeCode(projTypeCode);
				projResult.setTypeName(weightMap.get(projTypeCode));
				projResult.setSortNum(index++);
				IrrProjTypeEntity proj = projMap.get(projTypeCode);
                if (IrrEnum.OR_A.getValue().equals(projTypeCode)) {//合规风险
                    projResult.setTotalScore(new BigDecimal(oraWeightMap.get(key)));
				}else if(IrrEnum.WEIGHT_OR_CALC.getValue().equals(projTypeCode)) {//小计
					projResult.setTotalScore(calcOR(dimProjResultMap));
                } else if (IrrEnum.RR.getValue().equals(projTypeCode)) {//声誉风险
                    projResult.setTotalScore(new BigDecimal(rrWeightMap.get(key)));
				}else if(IrrEnum.WEIGHT_DR.getValue().equals(projTypeCode)) {//难以量化风险合计
					projResult.setTotalScore(calcDR(dimProjResultMap, projMap));
				}else if(IrrEnum.WEIGHT_QR.getValue().equals(projTypeCode)) {//量化风险
					projResult.setTotalScore(calcQR(key));
				}else if(IrrEnum.WEIGHT_CR.getValue().equals(projTypeCode)) {//风险综合评级
					projResult.setTotalScore(calcCR(dimProjResultMap, projMap));
				}else {
					List<IrrProjTypeEntity> childProjList = weightProjChildMap.get(projTypeCode);
					if(CollectionUtils.isNotEmpty(childProjList)) {
						//计算
						for(IrrProjTypeEntity temp : childProjList) {
							if(temp == null) {
								continue;
							}
							BigDecimal result = new BigDecimal("0");
							if(IrrEnum.DIRECT_POINTS_FLAG.getValue().equals(key) && MapUtils.isNotEmpty(directPointSpecialProjMap)
									&& IrrUtil.strObjectIsNotEmpty(directPointSpecialProjMap.get(key))) {
								//特殊处理(只有直接扣分时，OR02、OR06、OR12、OR15。取的是直接评分扣分项)
								result = irrTotalScoreResultService.calcDirectPointSpecialHand(task, temp);
							}else if(IrrUtil.strObjectIsNotEmpty(otherProjMap.get(temp.getTypeCode()))) {
								//五大类
                                result = irrTotalScoreResultService.calcFiveProj(task,
                                        otherProjMap.get(temp.getTypeCode()), key, temp.getTypeCode());
							}else {
								//正常取数
								result = irrTotalScoreResultService.calcWeight(task, temp, key);
							}
							//判断是分公司、总公司
							if(StringUtils.isNotBlank(temp.getTypeName())
									&& IrrTemplateEnum.SUMMARY_FILE_BRANCH_PROJ_FIRST.getValue().equals(temp.getTypeName().substring(0, 1))) {
								//分公司
								projResult.setBranchScore(result);
							}else {
								//总公司
								projResult.setHeadScore(result);
							}
						}
					}
					//计算得分
					Double projTotalScore = 0d;
					if(IrrUtil.isIntOrDouble(proj.getCircRate()) && projResult.getHeadScore() != null) {
						projTotalScore  = projTotalScore + projResult.getHeadScore().doubleValue() * IrrUtil.perceStrTransDouble(proj.getCircRate());
					}
					if(IrrUtil.isIntOrDouble(proj.getBureauRate()) && projResult.getBranchScore() != null) {
						projTotalScore = projTotalScore + projResult.getBranchScore().doubleValue() * IrrUtil.perceStrTransDouble(proj.getBureauRate());
					}
					projResult.setTotalScore(new BigDecimal(projTotalScore));
				}
                //合规风险：由于为扣分，总分和直接确定要负数。改成配置数据字典
                /*if(IrrEnum.OR_A.getValue().equals(projTypeCode) && (IrrEnum.TOTAL_SCORE_FLAG.getValue().equals(key)
                		|| IrrEnum.DIRE_DETE_FLAG.getValue().equals(key))) {
                	Double stanScore = proj.getStanScore().doubleValue();
                	projResult.setHeadScore(new BigDecimal(projResult.getHeadScore().doubleValue()+stanScore));					
                	projResult.setTotalScore(new BigDecimal(projResult.getTotalScore().doubleValue()+stanScore));
                }*/
				projResult.setTotalScore(projResult.getTotalScore()==null?new BigDecimal("0.0000"):projResult.getTotalScore().setScale(4,BigDecimal.ROUND_HALF_UP));
				projResult.setHeadScore(projResult.getHeadScore()==null?new BigDecimal("0.0000"):projResult.getHeadScore().setScale(4,BigDecimal.ROUND_HALF_UP));
				projResult.setBranchScore(projResult.getBranchScore()==null?new BigDecimal("0.0000"):projResult.getBranchScore().setScale(4,BigDecimal.ROUND_HALF_UP));
				dimProjResultMap.put(projTypeCode, projResult);
				returnList.add(projResult);
			}
		}
		return add(returnList);
	}

	@Override
	public BigDecimal calcOR(Map<String, IrrProjResultEntity> dimProjResultMap) throws Exception {
		//公式：(OR1+OR2+OR6+OR4+ORA+OR5+OR7*2+OR8+OR9)/9
		//改为科目得分乘以权重20200721
		double result = 0d;
		if(MapUtils.isEmpty(dimProjResultMap)) {
			return new BigDecimal(result);
		}
		List<IrrProjTypeEntity> list=new ArrayList<IrrProjTypeEntity>();
		String theRiskRate;//占本类操作风险比重
		IrrProjResultEntity OR1 = dimProjResultMap.get(IrrEnum.OR_1.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_1.getValue());
		IrrProjTypeEntity entityOR1=list.size()>0?list.get(0):null;
		if(OR1 != null && OR1.getTotalScore() != null&&entityOR1!=null&&entityOR1.getTheRiskRate()!=null) {
			theRiskRate=entityOR1.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR1.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += OR1.getTotalScore().doubleValue() * Double.valueOf(entityOR1.getTheRiskRate());
			}
		}
		IrrProjResultEntity OR2 = dimProjResultMap.get(IrrEnum.OR_2.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_2.getValue());
		IrrProjTypeEntity entityOR2=list.size()>0?list.get(0):null;
		if(OR2 != null && OR2.getTotalScore() != null&&entityOR2!=null&&entityOR2.getTheRiskRate()!=null) {
			theRiskRate=entityOR2.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR2.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += OR2.getTotalScore().doubleValue() * Double.valueOf(entityOR2.getTheRiskRate());
			}
		}
		IrrProjResultEntity OR6 = dimProjResultMap.get(IrrEnum.OR_6.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_6.getValue());
		IrrProjTypeEntity entityOR6=list.size()>0?list.get(0):null;
		if(OR6 != null && OR6.getTotalScore() != null&&entityOR6!=null&&entityOR6.getTheRiskRate()!=null) {
			theRiskRate=entityOR6.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR6.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += OR6.getTotalScore().doubleValue() * Double.valueOf(entityOR6.getTheRiskRate());
			}
		}
		IrrProjResultEntity OR4 = dimProjResultMap.get(IrrEnum.OR_4.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_4.getValue());
		IrrProjTypeEntity entityOR4=list.size()>0?list.get(0):null;
		if(OR4 != null && OR4.getTotalScore() != null&&entityOR4!=null&&entityOR4.getTheRiskRate()!=null) {
			theRiskRate=entityOR4.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR4.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += OR4.getTotalScore().doubleValue() * Double.valueOf(entityOR4.getTheRiskRate());
			}
		}
		IrrProjResultEntity ORA = dimProjResultMap.get(IrrEnum.OR_A.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_A.getValue());
		IrrProjTypeEntity entityORA=list.size()>0?list.get(0):null;
		if(ORA != null && ORA.getTotalScore() != null&&entityORA!=null&&entityORA.getTheRiskRate()!=null) {
			theRiskRate=entityORA.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += ORA.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += ORA.getTotalScore().doubleValue() * Double.valueOf(entityORA.getTheRiskRate());
			}
		}
		IrrProjResultEntity OR5 = dimProjResultMap.get(IrrEnum.OR_5.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_5.getValue());
		IrrProjTypeEntity entityOR5=list.size()>0?list.get(0):null;
		if(OR5 != null && OR5.getTotalScore() != null&&entityOR5!=null&&entityOR5.getTheRiskRate()!=null) {
			theRiskRate=entityOR5.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR5.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += OR5.getTotalScore().doubleValue() * Double.valueOf(entityOR5.getTheRiskRate());
			}
		}
		IrrProjResultEntity OR7 = dimProjResultMap.get(IrrEnum.OR_7.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_7.getValue());
		IrrProjTypeEntity entityOR7=list.size()>0?list.get(0):null;
		if(OR7 != null && OR7.getTotalScore() != null&&entityOR7!=null&&entityOR7.getTheRiskRate()!=null) {
			theRiskRate=entityOR7.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR7.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result = result + OR7.getTotalScore().doubleValue() * Double.valueOf(entityOR7.getTheRiskRate());
			}
		}
		IrrProjResultEntity OR8 = dimProjResultMap.get(IrrEnum.OR_8.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_8.getValue());
		IrrProjTypeEntity entityOR8=list.size()>0?list.get(0):null;
		if(OR8 != null && OR8.getTotalScore() != null&&entityOR8!=null&&entityOR8.getTheRiskRate()!=null) {
			theRiskRate=entityOR8.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR8.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += OR8.getTotalScore().doubleValue() * Double.valueOf(entityOR8.getTheRiskRate());
			}
		}
		IrrProjResultEntity OR9 = dimProjResultMap.get(IrrEnum.OR_9.getValue());
		list=irrProjTypeService.getRepository().findByTypeCode(IrrEnum.OR_9.getValue());
		IrrProjTypeEntity entityOR9=list.size()>0?list.get(0):null;
		if(OR9 != null && OR9.getTotalScore() != null&&entityOR9!=null&&entityOR9.getTheRiskRate()!=null) {
			theRiskRate=entityOR9.getTheRiskRate();
			if(theRiskRate.contains("/")) {
				String[] theRiskRateArr=theRiskRate.split("/");
				result += OR9.getTotalScore().doubleValue() *Double.valueOf(theRiskRateArr[0])/Double.valueOf(theRiskRateArr[1]);
			}else {
				result += OR9.getTotalScore().doubleValue() * Double.valueOf(entityOR9.getTheRiskRate());
			}
		}
		return new BigDecimal(result);
	}

	@Override
	public BigDecimal calcDR(Map<String, IrrProjResultEntity> dimProjResultMap, Map<String, IrrProjTypeEntity> projMap)
			throws Exception {
		//公式:小计*操作风险的占难以量化风险的比重+战略风险*战略风险的占难以量化风险的比重+声誉风险*声誉风险的占难以量化风险的比重+流动性风险*流动性风险的占难以量化风险的比重
		double result = 0d;
		if(MapUtils.isEmpty(dimProjResultMap) || MapUtils.isEmpty(projMap)) {
			return new BigDecimal(result);
		}
		//操作风险
		IrrProjResultEntity ORCALC = dimProjResultMap.get(IrrEnum.WEIGHT_OR_CALC.getValue());
		IrrProjTypeEntity or = projMap.get(IrrEnum.OR.getValue());
		if(ORCALC != null && ORCALC.getTotalScore() != null && or != null && IrrUtil.isIntOrDouble(or.getPdqRiskRate())) {
			result = result + ORCALC.getTotalScore().doubleValue() * IrrUtil.perceStrTransDouble(or.getPdqRiskRate());
		}
		//战略风险
		IrrProjResultEntity sr = dimProjResultMap.get(IrrEnum.SR.getValue());
		IrrProjTypeEntity srProj = projMap.get(IrrEnum.SR.getValue());
		if(sr != null && sr.getTotalScore() != null && srProj != null && IrrUtil.isIntOrDouble(srProj.getPdqRiskRate())) {
			result = result + sr.getTotalScore().doubleValue() * IrrUtil.perceStrTransDouble(srProj.getPdqRiskRate());
		}
		//声誉风险
		IrrProjResultEntity rr = dimProjResultMap.get(IrrEnum.RR.getValue());
		IrrProjTypeEntity rrProj = projMap.get(IrrEnum.RR.getValue());
		if(rr != null && rr.getTotalScore() != null && rrProj != null && IrrUtil.isIntOrDouble(rrProj.getPdqRiskRate())) {
			result = result + rr.getTotalScore().doubleValue() * IrrUtil.perceStrTransDouble(rrProj.getPdqRiskRate());
		}
		//流动性风险
		IrrProjResultEntity lr = dimProjResultMap.get(IrrEnum.LR.getValue());
		IrrProjTypeEntity lrProj = projMap.get(IrrEnum.LR.getValue());
		if(lr != null && lr.getTotalScore() != null && lrProj != null && IrrUtil.isIntOrDouble(lrProj.getPdqRiskRate())) {
			result = result + lr.getTotalScore().doubleValue() * IrrUtil.perceStrTransDouble(lrProj.getPdqRiskRate());
		}
		return new BigDecimal(result);
	}

	@Override
	public BigDecimal calcQR(String key) throws Exception {
		//总分和直接确定可得为100，其他为0
		if(StringUtils.isNotBlank(key) && (IrrEnum.TOTAL_SCORE_FLAG.getValue().equals(key) || IrrEnum.DIRE_DETE_FLAG.getValue().equals(key))) {
			return new BigDecimal(IrrEnum.DEFAULT_STAN_MAX.getValue());
		}
		return new BigDecimal(IrrEnum.DEFAULT_NUM.getValue());
	}

	@Override
	public BigDecimal calcCR(Map<String, IrrProjResultEntity> dimProjResultMap, Map<String, IrrProjTypeEntity> projMap)
			throws Exception {
		if(MapUtils.isEmpty(dimProjResultMap) || MapUtils.isEmpty(projMap)) {
			return new BigDecimal(IrrEnum.DEFAULT_NUM.getValue());
		}
		double result = 0d;
		//难以量化风险合计*难以量化风险合计占总风险的比重+量化风险*量化风险占总风险的比重
		IrrProjResultEntity dr = dimProjResultMap.get(IrrEnum.WEIGHT_DR.getValue());
		IrrProjTypeEntity drProj = projMap.get(IrrEnum.WEIGHT_DR.getValue());
		if(dr != null && dr.getTotalScore() != null && drProj != null && IrrUtil.isIntOrDouble(drProj.getTotalRiskRate())) {
			result = result + dr.getTotalScore().doubleValue() * IrrUtil.perceStrTransDouble(drProj.getTotalRiskRate());
		}
		IrrProjResultEntity qr = dimProjResultMap.get(IrrEnum.WEIGHT_QR.getValue());
		IrrProjTypeEntity qrProj = projMap.get(IrrEnum.WEIGHT_QR.getValue());
		if(qr != null && qr.getTotalScore() != null && qrProj != null && IrrUtil.isIntOrDouble(qrProj.getTotalRiskRate())) {
			result = result + qr.getTotalScore().doubleValue() * IrrUtil.perceStrTransDouble(qrProj.getTotalRiskRate());
		}
		return new BigDecimal(result);
	}

	@Override
	public Map<String, Object> findProjResultByDim(String taskId,String projTypeDim) throws Exception {
        List<WeightInfoDTO> weightInfoList = findAllProjResultByDim(taskId, projTypeDim);
		ResponsePage<WeightInfoDTO> response = new ResponsePage<WeightInfoDTO>();
		Map<String, Object> map = new HashMap<String,Object>();
		response.setAllData(weightInfoList);
		map.put("response", response.getPageData());
		return map;
	}

	@Override
	public Map<String, Object> editProjResult(String id) throws Exception {
		return null;
	}

    @Override
    public Workbook exportProjResult(IrrTaskEntity task, Workbook wb) throws Exception {
        if (task == null || wb == null) {
            return wb;
        }
        Map<String, String> projDimMap = systemDictionaryService.getDictionaryMap(IrrEnum.PROJ_TYPE_DIM.getValue(),
                Locale.CHINA);
        Map<String, String> nameCodeMap = new LinkedHashMap<String, String>();
        for (String key : projDimMap.keySet()) {
            nameCodeMap.put(projDimMap.get(key), key);
        }
        CellStyle contentCellStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
        for (String key : nameCodeMap.keySet()) {
            if (StringUtils.isBlank(key) || IrrUtil.strObjectIsEmpty(nameCodeMap.get(key))) {
                continue;
            }
            String projTypeDimCode = nameCodeMap.get(key);
            List<WeightInfoDTO> list = findAllProjResultByDim(task.getId(), projTypeDimCode);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            Sheet sheet = wb.getSheet(key);
            int rownum = 1;
            for (WeightInfoDTO temp : list) {
                Row row = sheet.createRow(rownum++);
                int column = 0;
                Object obj = null;
                Cell cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue((obj = temp.getProjResult().getTypeName()) == null ? null : obj.toString());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue((obj = temp.getProjResult().getTotalScore()) == null ? null : obj.toString());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue((obj = temp.getProjResult().getHeadScore()) == null ? null : obj.toString());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue((obj = temp.getProjResult().getBranchScore()) == null ? null : obj.toString());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getBeijing());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getTianjin());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getLiaoning());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getDalian());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getJiangsu());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getShandong());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getQingdao());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getHenan());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getGuangdong());

                cell = row.createCell(column++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(temp.getSichuan());
            }
        }
        return wb;
    }

    @Override
    public List<WeightInfoDTO> findAllProjResultByDim(String taskId, String projTypeDim) throws Exception {
        if (StringUtils.isBlank(taskId) || StringUtils.isBlank(projTypeDim)) {
            return null;
        }
        QOrg qOrg = QOrg.org;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qOrg.name.like("%" + IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString()));
        List<Org> branchOrg = (List<Org>) orgRepository.findAll(builder.getValue());
        if (CollectionUtils.isEmpty(branchOrg)) {
            throw new RuntimeException("无法查询:系统不存在分公司机构!");
        }
        //机构Map
        Map<String, Org> orgMap = new HashMap<String, Org>();
        for (Org org : branchOrg) {
            orgMap.put(org.getCode(), org);
        }
        String sql = "SELECT IOR.* FROM T_IRR_PROJ_RESULT IOR WHERE IOR.TASK_ID='" + taskId + "' AND IOR.RESULT_FLAG='"
                + projTypeDim + "' ORDER BY SORT_NUM";
        List<IrrProjResultEntity> projResultList = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<IrrProjResultEntity>(IrrProjResultEntity.class));
        if (CollectionUtils.isEmpty(projResultList)) {
            return null;
        }
        List<WeightInfoDTO> weightInfoList = new ArrayList<WeightInfoDTO>();
        for (IrrProjResultEntity projResult : projResultList) {
            WeightInfoDTO weight = new WeightInfoDTO();
            weight.setProjResult(projResult);
            List<IrrProjTypeEntity> childProjList = irrProjTypeService
                    .findProjChildByParentCode(projResult.getTypeCode());
            if (CollectionUtils.isNotEmpty(childProjList)) {
                for (IrrProjTypeEntity projType : childProjList) {
                    if (projType != null && StringUtils.isNotBlank(projType.getTypeName())
                            && IrrTemplateEnum.SUMMARY_FILE_BRANCH_PROJ_FIRST.getValue()
                                    .equals(projType.getTypeName().substring(0, 1))) {
                        //具有分公司
                        Org org = orgMap.get(WeightEnum.ORG_BRANCH_BEIJING_CODE.getValue());
                        weight.setBeijing(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_TIANJIN_CODE.getValue());
                        weight.setTianjin(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_LIAONING_CODE.getValue());
                        weight.setLiaoning(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_DALIAN_CODE.getValue());
                        weight.setDalian(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_JIANGSU_CODE.getValue());
                        weight.setJiangsu(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_SHANDONG_CODE.getValue());
                        weight.setShandong(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_QINGDAO_CODE.getValue());
                        weight.setQingdao(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_HENAN_CODE.getValue());
                        weight.setHenan(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_GUANGDONG_CODE.getValue());
                        weight.setGuangdong(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                        org = orgMap.get(WeightEnum.ORG_BRANCH_SICHUAN_CODE.getValue());
                        weight.setSichuan(irrTotalScoreResultService.findProjResultByCondition(taskId, projTypeDim,
                                projType.getTypeCode(), org.getId()));
                    }
                }
            }
            weightInfoList.add(weight);
        }
        return weightInfoList;
    }

}
