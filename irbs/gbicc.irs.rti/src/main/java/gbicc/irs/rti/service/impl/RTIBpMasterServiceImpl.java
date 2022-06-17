package gbicc.irs.rti.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.customer.repository.NsBpMasterRepository;
import gbicc.irs.debtRating.debt.service.impl.DebtRatingStepServiceImpl;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.service.impl.BpMasterServiceImpl;
import gbicc.irs.main.rating.service.impl.MainRatingServiceImpl;
import gbicc.irs.main.rating.utils.Constats;
import gbicc.irs.main.rating.utils.Tools;
import gbicc.irs.rti.service.RTIAssetsRatingService;
import gbicc.irs.rti.service.RTINsBpMasterService;
/**
 * 评级总控
 * @author hzw
 *
 */
@Service("rTIService")
public class RTIBpMasterServiceImpl extends DaoServiceImpl<NsBpMaster, String, NsBpMasterRepository>
		implements RTINsBpMasterService {
	private static final Logger log =LoggerFactory.getLogger(RTIBpMasterServiceImpl.class);
	
	@Autowired
	private JdbcTemplate template;
	@Autowired
	MainRatingServiceImpl mainRatingServiceImpl;
	@Autowired
	private RTIAssetsRatingService rTIAssetsRatingService;
	@Autowired
	private RTICreditRatingServiceImpl rTICreditRatingService;
	@Autowired
	private BpMasterServiceImpl clockDictionary;
	@Autowired
	private DebtRatingStepServiceImpl debtRatingStepServiceImpl;
	

	/**
	 * 获取数据库连接
	 * @return
	 * @throws SQLException
	 */
	public Connection achieveConnection() throws SQLException{
		Connection conn =  template.getDataSource().getConnection();
		return conn;
	}


	/**
	 * 资产评级
	 * @param fromMap
	 * @return 处理结果
	 */
	public Map<String, String> parsingAssetsInfo(Map<String, Object> fromMap){
		return rTIAssetsRatingService.parsingData(fromMap);
	}
	
	/**
	 * 债项评级3.0
	 * @param fromMap
	 * @return 处理结果
	 */
	public Map<String, String> parsingDebtInfo(Map<String, Object> fromMap){
		Map<String, String> result = new HashMap<String, String>();
		try {
			result.put("CREDIT_LEVEL", null);
			result.put("DEBT_LEVEL", null);
			result.put("REPORTURL", null);
			result.put("RET_STATUS", Constats.FAIL_STATUS);
			result.put("RET_CODE", Constats.FAIL_CODE);
			result.put("MSG", "评级失败");
			Map<String,Object> projectInfo = JSONObject.parseObject(fromMap.get("projectInfo").toString());
			result.put("BP_CODE", projectInfo.get("PROJECT_NUMBER")==null?null:projectInfo.get("PROJECT_NUMBER").toString().trim());
			result.put("BP_NAME",projectInfo.get("PROJECT_NAME")==null?null:projectInfo.get("PROJECT_NAME").toString().trim());
			return rTICreditRatingService.parsingData(fromMap);
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" : 评级失败！",e);
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * 主体评级2.0
	 * @param fromMap
	 * @return	处理结果
	 */
	public Map<String, String> parsingMainInfo2(Map<String, Object> fromMap){
		return clockDictionary.parsingData(fromMap);
	}
	
	/**
	 * 债项评级2.0
	 * @param fromMap
	 * @return 处理结果
	 */
	public Map<String, String> assemblyDataInter(Map<String, Object> fromMap){
		Map<String, String> result =new HashMap<String, String>();
		try {
			Map<String,Object> temp= debtRatingStepServiceImpl.interPa(fromMap.toString());
			result = debtRatingStepServiceImpl.assemblyDataInter(temp);
			if(result.get("flag").toString().equals("false")) {
      			result.put("flag","020");
				result.put("msg", "评级失败!");
      		}else {
      			result.put("flag","010");
      		}
		}catch(Exception e) {
			result.put("flag","020");
			result.put("msg", "评级失败!");
			log.info(this.getClass().getName()+" : "+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 主体评级3.0
	 * @param map json报文
	 * @return	处理接口
	 */
	public Map<String, String> parsingMainInfo(Map<String, Object> map){
		Map<String, String> res = new HashMap<String, String>();
		res.put("MAIN_LEVEL", null);
		res.put("REPORTURL", null);
		res.put("BP_CODE", null);
		res.put("BP_NAME", null);
		res.put("RET_STATUS", Constats.FAIL_STATUS);
		res.put("RET_CODE", Constats.FAIL_CODE);
		res.put("MSG", "评级失败");
		List<String> sqls = new ArrayList<String>();
		Connection conn	= null;
		Statement stmt 	= null;
		Map<String, String> initDataforModelCalculate = null;
		try {
			Map bpInfo = JSONObject.parseObject(map.get("bpInfo").toString());
			Map mainRatingInfo = JSONObject.parseObject(map.get("mainRatingInfo").toString());
			res.put("BP_CODE", bpInfo.get("BP_CODE")==null?null:bpInfo.get("BP_CODE").toString().trim());
			res.put("BP_NAME", bpInfo.get("BP_NAME")==null?null:bpInfo.get("BP_NAME").toString().trim());
			if(bpInfo.get("BP_ID")==null||"".equals(bpInfo.get("BP_ID").toString().trim())) {
				res.put("MSG", "客户ID为空，评级失败!");
				return res;
			}
			conn = achieveConnection(); 
			NsBpMaster master = setNsBpMaster(bpInfo);//解析客户信息报文
			sqls.add("DELETE NS_BP_MASTER WHERE FD_ID = '"+master.getFdid()+"'");//清除旧客户信息
			sqls.add("INSERT INTO NS_BP_MASTER (FD_ID,FD_BP_NAME,FD_BP_TYPE,FD_BP_CATEGORY,FD_BP_CODE,FD_REGISTERED_CAPITAL,FD_FOUNDED_DATE,"
					+ "FD_LEGAL_PERSON,FD_ENTERPRISE_SCALE,FD_REG_NUMBER_TYPE,FD_REG_NUMBER,FD_SEGMENT_INDUSTRY,FD_HIGH_PRECISION,FD_ECONOMIC_TYPE,"
					+ "FD_ENTERPRISE_HONOR,FD_ORG_TYPE,FD_ORG_SUB_TYPE,FD_PARK_ADDRESS,FD_MAIN_PRODUCTS,FD_EMPLOYEE_ID,FD_LEASE_ORGANIZATION,"
					+ "FD_ACTUAL_LESSEE,FD_SCORE_TEMPLATE_ID,MARKET_SIZE,FINANCING_SCALE) VALUES "
					+ "('"+master.getFdid()+"','"+master.getBpName()+"','"+master.getBpType()+"','"+master.getBpCategory()+"','"+master.getBpCode()+"'"
					+ ",'"+master.getRegisteredCapital()+"','"+master.getFoundedDate()+"','"+master.getLegalPerson()+"','"+master.getEnterpriseScale()+"'"
					+ ",'"+master.getRegNumberType()+"','"+master.getRegNumber()+"','"+master.getSegmentIndustry()+"',"+ "'"+master.getHighPrecision()+"',"
					+ "'"+master.getEconomicType()+"','"+master.getEnterpriseHonor()+"','"+master.getOrgType()+"',"
					+ "'"+master.getOrgSubType()+"','"+master.getParkAddress()+"','"+master.getMainProducts()+"','"+master.getEmployeeId()+"',"
					+ "'"+master.getLeaseOrganization()+"','"+master.getActualLessee()+"','"+master.getScoreTemplateId()+"',"
					+ "'"+master.getMarketSize()+"','"+master.getFinancingScale()+"')");
			MainRating mainEntity = new MainRating();
			String currentDate = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date());
			/*复评:
			 *评级步骤数据状态：0无效  	1当前所在的评级		2初评（历史）
			1：已处在初评阶段：更新当前初评步骤状态为 NS_RATING_STEP	rateStatus 	= "010" ，valid 		= "2";
			2：已处在复评阶段：更新当前初评步骤状态为 NS_RATING_STEP	rateStatus 	= "020" ，valid 		= "0";
			*/
			if ("OVER_STAGE".equals(mainRatingInfo.get("FD_RATING_TYPE")==null?null:mainRatingInfo.get("FD_RATING_TYPE").toString().trim())) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：主体复评开始："+master.getBpCode());
				String sqlCount 	= "SELECT COUNT(*) FROM  NS_MAIN_RATING WHERE FD_RATING_STATUS='010' AND FD_VERSION='3.0' AND FD_RATING_VAILD='1' AND FD_CUST_CODE=? ";
				Integer count 		= template.queryForObject(sqlCount, Integer.class, master.getBpCode());
				String rateStatus	= "";
				String valid 		= "";
				if (count > 0) {
					rateStatus 	= "010";
					valid 		= "2";
				} else {
					sqlCount 		= "SELECT COUNT(*) FROM  NS_MAIN_RATING WHERE FD_RATING_STATUS='020' AND FD_VERSION='3.0' AND FD_RATING_VAILD='1' AND  FD_CUST_CODE=? ";
					count = template.queryForObject(sqlCount, Integer.class, master.getBpCode());
					if (count > 0) {
						rateStatus	= "020";
						valid 		= "0";
					} else {
						res.put("MSG", "无初评记录，无法复评!");
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+master.getBpCode()+"无主体初评记录，无法复评!");
						return res;
					}
				}
				String sqlFdId = "SELECT FD_ID FROM  NS_MAIN_RATING WHERE FD_RATING_STATUS=? AND FD_VERSION='3.0' AND FD_RATING_VAILD='1' AND FD_CUST_CODE=? ";
				String idMainRate = template.queryForObject(sqlFdId, String.class,rateStatus,master.getBpCode());
				sqls.add("UPDATE NS_RATING_STEP SET FD_VAILD='"+valid+"' WHERE FD_VAILD='1' AND FD_RATEID=(SELECT FD_ID FROM  NS_MAIN_RATING WHERE FD_RATING_STATUS='"+rateStatus+"' AND FD_VERSION='3.0' AND FD_RATING_VAILD='1' AND FD_CUST_CODE='"+master.getBpCode()+"'  )");
				mainEntity = mainRatingServiceImpl.getRepository().findOne(idMainRate);
				String _internDate 	= Tools.formatDate(mainEntity.getInternDate(),"yyyy-MM-dd hh:mm:ss");
				String _finalDate 	= Tools.formatDate(null,"yyyy-MM-dd hh:mm:ss");
				mainEntity.setActualRateSubjectId(mainRatingInfo.get("ACTUAL_RATE_SUBJECT_ID")==null?"":mainRatingInfo.get("ACTUAL_RATE_SUBJECT_ID").toString().trim());//评级主体名称
				mainEntity.setCustCode(master.getBpCode());
				mainEntity.setCustName(master.getBpName());
				mainEntity.setRatingVaild("1");
				mainEntity.setRatingType(mainRatingInfo.get("FD_RATING_TYPE")==null?null:mainRatingInfo.get("FD_RATING_TYPE").toString().trim());
				mainEntity.setFdVersion(mainRatingInfo.get("FD_VERSION")==null?null:mainRatingInfo.get("FD_VERSION").toString().trim());
				mainEntity.setFinalName(mainRatingInfo.get("FD_FINAL_NAME")==null?null:mainRatingInfo.get("FD_FINAL_NAME").toString().trim());
				mainEntity.setInternCode(mainRatingInfo.get("FD_INTERN_CODE")==null?null:mainRatingInfo.get("FD_INTERN_CODE").toString().trim());
				mainEntity.setTrackType(mainRatingInfo.get("TRACK_TYPE")==null?null:mainRatingInfo.get("TRACK_TYPE").toString().trim());//评主体评级类型/赛道类型（市值型，增速型，利润型）
				mainEntity.setType(choseType(mainEntity.getTrackType(),master.getScoreTemplateId()));//评级类型--模型类型,	生产 or 服务
				mainEntity.setInternName(mainRatingInfo.get("FD_INTERN_NAME")==null?null:mainRatingInfo.get("FD_INTERN_NAME").toString().trim());
				mainEntity.setVaild(true);	
				mainEntity.setRatingStatus("020");
				mainEntity.setFinalDate(new Date());
				mainEntity.setfDate(Tools.formatDate(Tools.addYear(new Date(), 1),"yyyy-MM-dd hh:mm:ss"));//评级到期日
	//			mainEntity.setTreatN(map.get("treat_n").toString());//未知是神马
				mainEntity.setLastModifier(mainRatingInfo.get("FD_FINAL_NAME")==null?null:mainRatingInfo.get("FD_FINAL_NAME").toString().trim());
				mainEntity.setFinalCode(mainRatingInfo.get("FD_FINAL_CODE")==null?null:mainRatingInfo.get("FD_FINAL_CODE").toString().trim());
				mainEntity.setFinalAdvice(mainRatingInfo.get("REVIEWEROPINION")==null?null:mainRatingInfo.get("REVIEWEROPINION").toString().trim());//暂取评审经理意见值
				mainEntity.setReviewerOpinion(mainRatingInfo.get("REVIEWEROPINION")==null?null:mainRatingInfo.get("REVIEWEROPINION").toString().trim());
				if(!StringUtils.isEmpty(master.getActualLessee())&&Constats.STR_NO.equalsIgnoreCase(master.getActualLessee())) {
					mainEntity.setSco("0");
					mainEntity.setFinalLevel("I");
					sqls.add("UPDATE NS_MAIN_RATING SET FD_RATING_STATUS='"+mainEntity.getRatingStatus()+"'"
							+ ",ACTUAL_RATE_SUBJECT_ID='"+mainEntity.getActualRateSubjectId()+"',FD_FIR_REP=decode('"+mainEntity.getFirRep()+"','null',null,'"+mainEntity.getFirRep()+"')"
							+ ",FD_SEC_REP=decode('"+mainEntity.getSecRep()+"','null',null,'"+mainEntity.getSecRep()+"')"
							+ ",FD_THI_REP=decode('"+mainEntity.getThiRep()+"','null',null,'"+mainEntity.getThiRep()+"')"
							+ ",FD_CUST_CODE='"+mainEntity.getCustCode()+"'"
							+ ",FD_CUST_NAME='"+mainEntity.getCustName()+"'"
							+ ",FD_RATING_VAILD='1'"
							+ ",FD_RATING_TYPE='"+mainEntity.getRatingType()+"'"
							+ ",FD_VERSION='"+mainEntity.getFdVersion()+"'"
							+ ",FD_FINAL_NAME='"+mainEntity.getFinalName()+"'"
							+ ",FD_INTERN_CODE='"+mainEntity.getInternCode()+"'"
							+ ",TRACK_TYPE='"+mainEntity.getTrackType()+"'"
							+ ",FD_TYPE='"+mainEntity.getType()+"'"
							+ ",FD_INTERN_NAME='"+mainEntity.getInternName()+"'"
							+ ",FD_VAILD='1'"
							+ ",FD_INTERN_DATE=to_date('"+_internDate+"','yyyy-MM-dd hh:mi:ss')"
							+ ",FD_FINAL_DATE=to_date('"+_finalDate+"','yyyy-MM-dd hh:mi:ss')"
							+ ",F_DATE='"+mainEntity.getfDate()+"'"
							+ ",FD_FINAL_CODE='"+mainEntity.getFinalCode()+"'"
							+ ",FD_FINAL_ADVICE='"+mainEntity.getFinalAdvice()+"'"
							+ ",REVIEWEROPINION=DECODE('"+mainEntity.getReviewerOpinion()+"','null',null,'"+mainEntity.getReviewerOpinion()+"')"
							+ ",FD_SCO='0',FD_FINAL_LEVEL='I'"
							+ ",FD_LAST_MODIFYDATE=TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss') "
							+ ",FD_LAST_MODIFIER='"+mainEntity.getLastModifier()+"' "
							+ " WHERE FD_ID='"+mainEntity.getId()+"'");
				}else {
					sqls.add("UPDATE NS_MAIN_RATING SET FD_RATING_STATUS='"+mainEntity.getRatingStatus()+"'"
							+ ",ACTUAL_RATE_SUBJECT_ID='"+mainEntity.getActualRateSubjectId()+"'"
							+ ",FD_FIR_REP=decode('"+mainEntity.getFirRep()+"','null',null,'"+mainEntity.getFirRep()+"')"
							+ ",FD_SEC_REP=decode('"+mainEntity.getSecRep()+"','null',null,'"+mainEntity.getSecRep()+"')"
							+ ",FD_THI_REP=decode('"+mainEntity.getThiRep()+"','null',null,'"+mainEntity.getThiRep()+"')"
							+ ",FD_CUST_CODE='"+mainEntity.getCustCode()+"'"
							+ ",FD_CUST_NAME='"+mainEntity.getCustName()+"'"
							+ ",FD_RATING_VAILD='1'"
							+ ",FD_RATING_TYPE='"+mainEntity.getRatingType()+"'"
							+ ",FD_VERSION='"+mainEntity.getFdVersion()+"'"
							+ ",FD_FINAL_NAME='"+mainEntity.getFinalName()+"'"
							+ ",FD_INTERN_CODE='"+mainEntity.getInternCode()+"'"
							+ ",TRACK_TYPE='"+mainEntity.getTrackType()+"'"
							+ ",FD_TYPE='"+mainEntity.getType()+"'"
							+ ",FD_INTERN_NAME='"+mainEntity.getInternName()+"'"
							+ ",FD_VAILD='1'"
							+ ",FD_INTERN_DATE=to_date('"+_internDate+"','yyyy-MM-dd hh:mi:ss')"
							+ ",FD_FINAL_DATE=to_date('"+_finalDate+"','yyyy-MM-dd hh:mi:ss')"
							+ ",F_DATE='"+mainEntity.getfDate()+"'"
							+ ",FD_FINAL_CODE='"+mainEntity.getFinalCode()+"'"
							+ ",FD_FINAL_ADVICE='"+mainEntity.getFinalAdvice()+"'"
							+ ",FD_LAST_MODIFYDATE=TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss') "
							+ ",FD_LAST_MODIFIER='"+mainEntity.getLastModifier()+"' "
							+ ",REVIEWEROPINION=DECODE('"+mainEntity.getReviewerOpinion()+"','null',null,'"+mainEntity.getReviewerOpinion()+"')  WHERE FD_ID='"+mainEntity.getId()+"'");
				}
			}else if ("START_STAGE".equals(mainRatingInfo.get("FD_RATING_TYPE")==null?null:mainRatingInfo.get("FD_RATING_TYPE").toString().trim())) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：主体初评开始："+master.getBpCode());
				/*初评:
				 * 	已处在初评阶段：更新所有初评步骤状态为无效 NS_RATING_STEP	valid = "0";
				 * 	已处在复评阶段：更新所有初评状态为无效 NS_MAIN_RATING	FD_VAILD='0',FD_RATING_VAILD='0' ;
				 */
				String ratingEffective = "SELECT COUNT(FD_RATING_VAILD) FROM  NS_MAIN_RATING WHERE FD_RATING_STATUS='010' AND FD_VERSION='3.0' AND FD_RATING_VAILD='1'  AND  FD_CUST_CODE=?  ";
				Integer ratingEffectiveCount = template.queryForObject(ratingEffective, Integer.class, master.getBpCode());
				if (ratingEffectiveCount > 0) {
					String sql	= "SELECT FD_ID FROM  NS_MAIN_RATING WHERE  FD_RATING_STATUS='010' AND FD_VERSION='3.0' AND FD_RATING_VAILD='1' AND  FD_CUST_CODE=?";
					String id	= template.queryForObject(sql, String.class, master.getBpCode());
					//所有评级步骤信息置为无效
					sqls.add("UPDATE NS_RATING_STEP SET FD_VAILD='0' WHERE FD_RATEID='"+id+"'");
//					sqls.add("UPDATE NS_MAIN_RATING SET FD_VAILD='0' , FD_RATING_VAILD='0' WHERE FD_ID='"+id+"'");
				}
				//复评
//				ratingEffective = "SELECT COUNT(FD_RATING_VAILD) FROM  NS_MAIN_RATING WHERE FD_RATING_STATUS='020' AND FD_VERSION='3.0' AND FD_RATING_VAILD='1'  AND  FD_CUST_CODE=? ";
//				ratingEffectiveCount = template.queryForObject(ratingEffective, Integer.class, master.getBpCode());
//				if (ratingEffectiveCount > 0) {
//					String sql = "SELECT FD_ID FROM  NS_MAIN_RATING WHERE FD_RATING_STATUS='020' AND FD_VERSION='3.0' AND FD_RATING_VAILD='1'  AND  FD_CUST_CODE=? ";
//					String id = template.queryForObject(sql, String.class, master.getBpCode());
//					sqls.add("UPDATE NS_MAIN_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE FD_ID='"+id+"'");
//				}
				sqls.add("UPDATE NS_MAIN_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE  FD_VERSION='3.0' AND FD_RATING_VAILD='1' and FD_VAILD='1'  AND  FD_CUST_CODE='"+master.getBpCode()+"' ");
				mainEntity.setActualRateSubjectId(mainRatingInfo.get("ACTUAL_RATE_SUBJECT_ID")==null?"":mainRatingInfo.get("ACTUAL_RATE_SUBJECT_ID").toString().trim());//评级主体名称
				mainEntity.setCustCode(master.getBpCode());
				mainEntity.setCustName(master.getBpName());
				mainEntity.setRatingVaild("1");
				mainEntity.setRatingType(mainRatingInfo.get("FD_RATING_TYPE")==null?null:mainRatingInfo.get("FD_RATING_TYPE").toString().trim());
				mainEntity.setFdVersion(mainRatingInfo.get("FD_VERSION")==null?null:mainRatingInfo.get("FD_VERSION").toString().trim());
				mainEntity.setInternCode(mainRatingInfo.get("FD_INTERN_CODE")==null?null:mainRatingInfo.get("FD_INTERN_CODE").toString().trim());
				mainEntity.setTrackType(mainRatingInfo.get("TRACK_TYPE")==null?null:mainRatingInfo.get("TRACK_TYPE").toString().trim());//评主体评级类型/赛道类型（市值型，增速型，利润型）
				mainEntity.setType(choseType(mainEntity.getTrackType(),master.getScoreTemplateId()));//评级类型--模型类型,	生产 or 服务
				mainEntity.setInternName(mainRatingInfo.get("FD_INTERN_NAME")==null?null:mainRatingInfo.get("FD_INTERN_NAME").toString().trim());
				mainEntity.setRatingStatus("010");
				mainEntity.setInternDate(new Date());
				mainEntity.setVaild(true);
				mainEntity.setId(UUID.randomUUID().toString());
				mainEntity.setCreator(mainEntity.getInternName());
				if(!StringUtils.isEmpty(master.getActualLessee())&&Constats.STR_NO.equalsIgnoreCase(master.getActualLessee())) {
					mainEntity.setInitSco("0");
					mainEntity.setInternLevel("I");
					sqls.add("INSERT INTO NS_MAIN_RATING "
							+ "(FD_ID,FD_CUST_CODE,FD_CUST_NAME,FD_TYPE,FD_INTERN_DATE"
							+ ",FD_RATING_VAILD,FD_VAILD,FD_INTERN_CODE,FD_INTERN_NAME"
							+ ",REVIEWEROPINION,FD_RATING_STATUS,FD_RATING_TYPE,TRACK_TYPE,FD_VERSION"
							+ ",FD_INITSCO,FD_INTERN_LEVEL,FD_CREATOR,FD_CREATE_DATE,ACTUAL_RATE_SUBJECT_ID)"
							+ "values("
							+ "'"+mainEntity.getId()+"','"+mainEntity.getCustCode()+"','"+mainEntity.getCustName()+"','"+mainEntity.getType()+"'"
							+ ",TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss')"
							+ ",'"+mainEntity.getRatingVaild()+"','1'"
							+ ",'"+mainEntity.getInternCode()+"','"+mainEntity.getInternName()+"'"
							+ ",decode('"+mainEntity.getReviewerOpinion()+"','null',null,'"+mainEntity.getReviewerOpinion()+"'),'"+mainEntity.getRatingStatus()+"','"+mainEntity.getRatingType()+"','"+mainEntity.getTrackType()+"'"
							+ ",'"+mainEntity.getFdVersion()+"','"+mainEntity.getInitSco()+"'"
							+ ",'"+mainEntity.getInternLevel()+"','"+mainEntity.getCreator()+"',TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss'),'"+mainEntity.getActualRateSubjectId()+"')");
				
				}else {
					sqls.add("INSERT INTO NS_MAIN_RATING "
							+ "(FD_ID,FD_CUST_CODE,FD_CUST_NAME,FD_TYPE,FD_INTERN_DATE"
							+ ",FD_RATING_VAILD,FD_VAILD,FD_INTERN_CODE,FD_INTERN_NAME"
							+ ",REVIEWEROPINION,FD_RATING_STATUS,FD_RATING_TYPE,TRACK_TYPE,FD_VERSION"
							+ ",FD_CREATOR,FD_CREATE_DATE,ACTUAL_RATE_SUBJECT_ID)"
							+ "values("
							+ "'"+mainEntity.getId()+"','"+mainEntity.getCustCode()+"','"+mainEntity.getCustName()+"','"+mainEntity.getType()+"'"
							+ ",TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss')"
							+ ",'"+mainEntity.getRatingVaild()+"','1'"
							+ ",'"+mainEntity.getInternCode()+"','"+mainEntity.getInternName()+"'"
							+ ",decode('"+mainEntity.getReviewerOpinion()+"','null',null,'"+mainEntity.getReviewerOpinion()+"'),'"+mainEntity.getRatingStatus()+"','"+mainEntity.getRatingType()+"','"+mainEntity.getTrackType()+"'"
							+ ",'"+mainEntity.getFdVersion()+"'"
							+ ",'"+mainEntity.getCreator()+"',TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss'),'"+mainEntity.getActualRateSubjectId()+"')");
				}
			}else {
				res.put("MSG", "评级失败，解析报文时初复评标识数据异常！");
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级失败，解析报文时初复评标识数据异常！");
				return res;
			}
			
			//不评级，空报文，不解析
			if(!StringUtils.isEmpty(master.getActualLessee())&&Constats.STR_YES.equalsIgnoreCase(master.getActualLessee())) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）是评级主体");
				initDataforModelCalculate = initDataForModelCalculate();
				//解析财报报文
				List<Map<String, Object>> lsitReport = (List<Map<String, Object>>) map.get("financialReportDatas");
				for (int i = 0; i < lsitReport.size(); i++) {
					if(StringUtils.isEmpty(lsitReport.get(i).get("fiscalTimes"))||StringUtils.isEmpty(lsitReport.get(i).get("fiscalTimes").toString().trim())
							||StringUtils.isEmpty(lsitReport.get(i).get("finStatementId"))||StringUtils.isEmpty(lsitReport.get(i).get("finStatementId").toString().trim())
							||StringUtils.isEmpty(lsitReport.get(i).get("REPORTDETAILTYPE"))||StringUtils.isEmpty(lsitReport.get(i).get("REPORTDETAILTYPE").toString().trim())
							||StringUtils.isEmpty(lsitReport.get(i).get("REPORTDETAILTYPEDESC"))||StringUtils.isEmpty(lsitReport.get(i).get("REPORTDETAILTYPEDESC").toString().trim())
							||StringUtils.isEmpty(lsitReport.get(i).get("BPCODE"))||StringUtils.isEmpty(lsitReport.get(i).get("BPCODE").toString().trim())
							||StringUtils.isEmpty(lsitReport.get(i).get("CURRENCYCODE"))||StringUtils.isEmpty(lsitReport.get(i).get("CURRENCYCODE").toString().trim())
							||StringUtils.isEmpty(lsitReport.get(i).get("CURRENCYCODEDESC"))||StringUtils.isEmpty(lsitReport.get(i).get("CURRENCYCODEDESC").toString().trim())) {
						res.put("MSG", "财报基本信息数据异常");
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级失败，财报基本信息数据异常");
						return res;
					}
					String reportDate = lsitReport.get(i).get("fiscalTimes").toString().trim();
					if (i == 0) {
						mainEntity.setFirRep(reportDate);
						sqls.add("UPDATE NS_MAIN_RATING SET FD_FIR_REP='"+reportDate+"' WHERE FD_ID='"+mainEntity.getId()+"'");
					}else if(i == 1) {
						mainEntity.setSecRep(reportDate);
						sqls.add("UPDATE NS_MAIN_RATING SET FD_SEC_REP='"+reportDate+"' WHERE FD_ID='"+mainEntity.getId()+"'");
					}else {
						mainEntity.setThiRep(reportDate);
						sqls.add("UPDATE NS_MAIN_RATING SET FD_THI_REP='"+reportDate+"' WHERE FD_ID='"+mainEntity.getId()+"'");
					}
					String _queryCount 	= "SELECT COUNT(*) FROM  NS_FIN_STAT WHERE  FD_CUST_NO =? AND FD_REPORT_BUSS_DATE =?";
					Integer countNsFinStat 		= template.queryForObject(_queryCount, Integer.class, master.getBpCode(),reportDate);
					String mainFinStatementId = lsitReport.get(i).get("finStatementId").toString().trim();//FD_ID
					String _bpCode = lsitReport.get(i).get("BPCODE").toString().trim();
					String _fiscalTimes = lsitReport.get(i).get("fiscalTimes").toString().trim();
					String _reportDetailType = lsitReport.get(i).get("REPORTDETAILTYPE").toString().trim();
					String _reportDetailTypeDesc = lsitReport.get(i).get("REPORTDETAILTYPEDESC").toString().trim();
					String _currencyCode = lsitReport.get(i).get("CURRENCYCODE").toString().trim();
					String _currencyCodeDesc = lsitReport.get(i).get("CURRENCYCODEDESC").toString().trim();
					if(countNsFinStat < 1) {
						sqls.add("INSERT INTO NS_FIN_STAT (FD_ID,FD_CUST_NO,FD_REPORT_BUSS_DATE,FD_REPORT_SPECIFICATIONS,FD_REPORT_SPECIFICATIONS_DESC,FD_REPORT_CURRENCY,FD_REPORT_CURRENCY_DESC,FD_VAILD) VALUES"
							+ "('"+mainFinStatementId+"','"+_bpCode+"','"+_fiscalTimes+"','"+_reportDetailType+"','"+_reportDetailTypeDesc+"','"+_currencyCode+"','"+_currencyCodeDesc+"',1)");
					}else {
						sqls.add("UPDATE NS_FIN_STAT SET FD_VAILD='0' WHERE FD_CUST_NO='"+_bpCode+"' AND FD_REPORT_BUSS_DATE='"+_fiscalTimes+"'");//避免存在多条数据情况
						sqls.add("UPDATE NS_FIN_STAT SET FD_CUST_NO='"+_bpCode+"',FD_REPORT_BUSS_DATE='"+_fiscalTimes+"',FD_REPORT_SPECIFICATIONS='"+_reportDetailType+"',FD_REPORT_SPECIFICATIONS_DESC='"+_reportDetailTypeDesc+"',FD_REPORT_CURRENCY='"+_currencyCode+"',FD_REPORT_CURRENCY_DESC='"+_currencyCodeDesc+"' ,FD_VAILD=1 where FD_ID='"+mainFinStatementId+"' ");
					}
					//财报子表	7
					List<Map<String, Object>> listData = (List<Map<String, Object>>) lsitReport.get(i).get("financialSubjectDatas");
					if(listData==null || listData.size()<1) {
						res.put("MSG", "财报科目数据异常");
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级失败，财报科目数据异常");
						return res;
					}
					sqls.add("DELETE FROM NS_FIN_ACCOUNT_DATA WHERE  FD_REPORT_ID ='"+mainFinStatementId+"'");
					//验证是否包含必要的模型计算指标
					HashMap<String,String> codes = checkLegalityOfFinance(listData,i);
					if(codes!=null&&!codes.isEmpty()) {
						res.put("MSG", reportDate+"财报的详细科目中缺少评级必须的财报科目"+codes.values().toString());
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级失败，"+reportDate+"财报的详细科目中缺少评级必须的财报科目"+codes.values().toString());
						return res;
					}
					for (int j = 0; j < listData.size(); j++) {
						if(StringUtils.isEmpty(listData.get(j).get("FINSTATEMENTITEM"))||StringUtils.isEmpty(listData.get(j).get("FINSTATEMENTITEM").toString().trim())
								||StringUtils.isEmpty(listData.get(j).get("AMOUNT"))||StringUtils.isEmpty(listData.get(j).get("AMOUNT").toString().trim())
								||StringUtils.isEmpty(listData.get(j).get("FINSTATEMENTID"))||StringUtils.isEmpty(listData.get(j).get("FINSTATEMENTID").toString().trim())
								||StringUtils.isEmpty(listData.get(j).get("ITEMDESC"))||StringUtils.isEmpty(listData.get(j).get("ITEMDESC").toString().trim())
								||StringUtils.isEmpty(listData.get(j).get("FINSTATEMENTTYPE"))||StringUtils.isEmpty(listData.get(j).get("FINSTATEMENTTYPE").toString().trim())) {
							res.put("MSG", reportDate+"财报中第"+(j+1)+"个财报科目数据异常");
							log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级失败，"+reportDate+"财报中第"+(j+1)+"个财报科目数据异常");
							return res;
						}
						String _uuid = UUID.randomUUID().toString();
						String _finstatementItem= listData.get(j).get("FINSTATEMENTITEM").toString().trim();
						String _finstatementId = listData.get(j).get("FINSTATEMENTID").toString().trim();
						String _itemDesc = listData.get(j).get("ITEMDESC").toString().trim();
						double _amount = Double.parseDouble(listData.get(j).get("AMOUNT").toString().trim());
						String _finstatementType = listData.get(j).get("FINSTATEMENTTYPE").toString().trim();
						sqls.add("INSERT INTO NS_FIN_ACCOUNT_DATA (FD_ID,FD_REPORT_ID,FD_DATAITEM_CODE,FD_DATAITEM_NAME,FD_END_VALUE,FD_ACCOUNT_CATEGORY )VALUES"
								+ "('"+_uuid+"','"+_finstatementId+"','"+_finstatementItem+"','"+_itemDesc+"',"+_amount+",'"+_finstatementType+"')");
						//组装模型计算所需财报指标数据
						getCalculateFinData(initDataforModelCalculate,listData.get(j),i);
						initDataforModelCalculate.put(Constats.STR_F307, master.getMarketSize());
						initDataforModelCalculate.put(Constats.STR_F308,master.getFinancingScale());
					}
				}
				sqls.add("UPDATE NS_NON_FIN SET FD_VAILD='0' WHERE  FD_MAIN_ID='"+mainEntity.getId()+"'");
				List<Map<String, Object>> lsitNonFinancial = (List<Map<String, Object>>) map.get("scoreTargetDatas");
				for (int i = 0; i < lsitNonFinancial.size(); i++) {
					if(StringUtils.isEmpty(lsitNonFinancial.get(i).get("SCORETARGETCODE"))||StringUtils.isEmpty(lsitNonFinancial.get(i).get("SCORETARGETCODE").toString().trim())||StringUtils.isEmpty(lsitNonFinancial.get(i).get("VALUE"))||StringUtils.isEmpty(lsitNonFinancial.get(i).get("VALUE").toString().trim())) {
						res.put("MSG", "非财务选项中第"+(i+1)+"个选项数据异常");
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级失败，非财务选项中第"+(i+1)+"个选项数据异常");
						return res;
					}
					String _uuidNsNonFin = UUID.randomUUID().toString();
					String _scoreTargetCode = lsitNonFinancial.get(i).get("SCORETARGETCODE").toString().trim();
					String _value = lsitNonFinancial.get(i).get("VALUE").toString().trim();
					sqls.add("INSERT INTO NS_NON_FIN VALUES('"+_uuidNsNonFin+"','"+mainEntity.getId()+"','"+_scoreTargetCode+"','"+_value+"','1')");
					initDataforModelCalculate.put(_scoreTargetCode, _value);
				}
			}else {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）非评级主体");
			}
			//解析并持久化重大风险提示信息
			List<Map<String, Object>> majorRiskWarnings = (List<Map<String, Object>>) map.get("majorRiskWarningDatas");
			sqls.add("DELETE FROM NS_MAJOR_RECORD WHERE  SERIAL_NUMBER='"+mainEntity.getId()+"' and RATE_TYPE='"+mainEntity.getRatingStatus()+"'");
			if(majorRiskWarnings!=null&&majorRiskWarnings.size()>0) {
				for (int i = 0; i < majorRiskWarnings.size(); i++) {
					if(StringUtils.isEmpty(majorRiskWarnings.get(i).get("RISKWARNING_CODE"))||StringUtils.isEmpty(majorRiskWarnings.get(i).get("RISKWARNING_CODE").toString().trim())
							||StringUtils.isEmpty(majorRiskWarnings.get(i).get("ISSELECTED"))||StringUtils.isEmpty(majorRiskWarnings.get(i).get("ISSELECTED").toString().trim())) {
						res.put("MSG", "重大风险提示信息中第"+(i+1)+"个选项数据异常");
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级失败，重大风险提示信息中第"+(i+1)+"个选项数据异常");
						return res;
					}
					String idNsMajorRecord = UUID.randomUUID().toString();
					String RISKWARNING_CODE = majorRiskWarnings.get(i).get("RISKWARNING_CODE").toString().trim();
					String ISSELECTED = majorRiskWarnings.get(i).get("ISSELECTED").toString().trim();
					String SELECT_EXPLAIN = majorRiskWarnings.get(i).get("SELECT_EXPLAIN")==null?null:majorRiskWarnings.get(i).get("SELECT_EXPLAIN").toString().trim();
					sqls.add("INSERT INTO NS_MAJOR_RECORD VALUES('"+idNsMajorRecord+"','"+mainEntity.getId()+"',decode('"+RISKWARNING_CODE+"','null',null,'"+RISKWARNING_CODE+"'),decode('"+ISSELECTED+"','null',null,'"+ISSELECTED+"'),decode('"+SELECT_EXPLAIN+"','null',null,'"+SELECT_EXPLAIN+"'),'"+mainEntity.getRatingStatus()+"')");
					if(!StringUtils.isEmpty(master.getActualLessee())&&Constats.STR_YES.equalsIgnoreCase(master.getActualLessee())) {
						initDataforModelCalculate.put(RISKWARNING_CODE, ISSELECTED);
					}
				}
			}else {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）无重大风险提示信息");
			}
			long insertStart = System.currentTimeMillis();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）主体评级实时接口处理sqls : ");
			for(String sqlStr : sqls){
				log.info(sqlStr);
				stmt.addBatch(sqlStr);
			}
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）start execute sqlBatch ---------------------------");
			stmt.executeBatch();
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）end execute sqlBatch ---------------------------");
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）execute sqls : " + sqls.size()+" execute time (ms) "+(System.currentTimeMillis() - insertStart)/1000);
			if(!StringUtils.isEmpty(master.getActualLessee())&&Constats.STR_YES.equalsIgnoreCase(master.getActualLessee())) {
				res = rating(mainEntity,mainEntity.getRatingStatus(),master,initDataforModelCalculate,res);
				if("F".equals(res.get("RET_STATUS"))) {
					conn.rollback();
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级处理失败，批处理事务回滚！");
				}else {
					if(!"".equals(mainEntity.getId())&&null!=mainEntity.getId()) {
						String updateMainRatingSQL = "UPDATE NS_MAIN_RATING SET FD_CURRENT_STEPID=decode('"+mainEntity.getCurrentStepId()+"','null',null,'"+mainEntity.getCurrentStepId()+"'),"
								+ "FD_RATING_STATUS='"+mainEntity.getRatingStatus()+"',FD_VAILD='1',FD_SCO=decode('"+mainEntity.getSco()+"','null',null,'"+mainEntity.getSco()+"'),"
								+ "FD_PD=decode('"+mainEntity.getPd()+"','null',null,'"+mainEntity.getPd()+"'),FD_INTERN_LEVEL=decode('"+mainEntity.getInternLevel()+"','null',null,'"+mainEntity.getInternLevel()+"'),"
								+ "FD_INITSCO=decode('"+mainEntity.getInitSco()+"','null',null,'"+mainEntity.getInitSco()+"'),FD_FINAL_LEVEL=decode('"+mainEntity.getFinalLevel()+"','null',null,'"+mainEntity.getFinalLevel()+"') "
										+ "where fd_ID='"+mainEntity.getId()+"'";
						stmt.execute(updateMainRatingSQL);
						conn.commit();
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）batch transaction commit ---------------------------");
					}else {
						conn.rollback();
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）评级处理失败，批处理事务回滚！");
						res.put("RET_STATUS", Constats.FAIL_STATUS);
						res.put("RET_CODE", Constats.FAIL_CODE);
						res.put("MSG", "评级失败");
					}
				}
			}else {
				res.put("MAIN_LEVEL", "I");
				res.put("RET_STATUS", Constats.SUCCESS_STATUS);
				res.put("RET_CODE", Constats.SUCCESS_CODE);
				res.put("MSG", "成功");
				conn.commit();
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+master.getBpCode()+"）batch transaction commit ---------------------------");
			}
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：主体评级结束！"+master.getBpCode());
			return res;
		} catch (Exception e) {
			res.put("RET_STATUS", Constats.FAIL_STATUS);
			res.put("RET_CODE", Constats.FAIL_CODE);
			res.put("MSG", "评级失败");
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：评级失败！",e);
			e.printStackTrace();
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e2) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：",e);
					e2.printStackTrace();
					return res;
				}
			}
			if(conn != null) {
				try {
					conn.rollback();
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：batch transaction rollback ---------------------------");
				} catch (SQLException e1) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：",e1);
					e1.printStackTrace();
					return res;
				}
			}
			return res;
		}finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：",e);
					e.printStackTrace();
					return res;
				}
			}
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：",e);
					e.printStackTrace();
					return res;
				}
			}
			
		}
		
	}
	
	/**
	 * @评级分流
	 */	
	@Override
//	@Transactional
	public Map<String, String> parsingData(Map<String, Object> map) {
		Map<String, String> res = new HashMap<String, String>();
		try {
			if(map.get("type").equals("020")) {
				res = parsingMainInfo2(map);//2.0主体
			}else if(map.get("type").equals("040")) {
				res = assemblyDataInter(map);//2.0债项
			}else if(map.get("type").equals("060")) {
				res = parsingAssetsInfo(map);
			}else if(map.get("type").equals("320")) {
				res = parsingMainInfo(map);
			}else if(map.get("type").equals("340")) {
				res = parsingDebtInfo(map);
			}
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" : 评级失败！",e);
			e.printStackTrace();
			return res;
		}
		return res;
	}
	
	/**
	 * @param trackType	市值型，增速型，利润型
	 * @param scoreTemplateId	生产 or 服务
	 * @return
	 * @throws Exception 
	 */
	public String choseType(String trackType,String scoreTemplateId) throws Exception {
		String type = "";
		switch (trackType.concat(scoreTemplateId)) {
		case Constats.STR_L_F:
			type = Constats.TYPE_L_F;
			break;
		case Constats.STR_L_S:
			type = Constats.TYPE_L_S;
			break;
		case Constats.STR_S_F:
			type = Constats.TYPE_S_F;
			break;
		case Constats.STR_S_S:
			type = Constats.TYPE_S_S;
			break;
		case Constats.STR_Z_F:
			type = Constats.TYPE_Z_F;
			break;
		case Constats.STR_Z_S:
			type = Constats.TYPE_Z_S;
			break;
		default:
			throw new Exception("实时接口评级信息中TRACK_TYPE或SCORE_TEMPLATE_ID数据异常，无法匹配评级类型");
		}
		return type;
	}
	
	
	/**
	 * 	主体信息数据解析并赋值
	 * @param json
	 * @return
	 * 
	 */
	public NsBpMaster setNsBpMaster(Map<String,Object> bpInfoMap) {
		NsBpMaster nsBpMaster = null;
		try {
			nsBpMaster = new NsBpMaster();
			nsBpMaster.setFdid(bpInfoMap.get("BP_ID")==null?"":bpInfoMap.get("BP_ID").toString().trim());
			nsBpMaster.setBpName(bpInfoMap.get("BP_NAME")==null?"":bpInfoMap.get("BP_NAME").toString().trim());
			nsBpMaster.setBpType(bpInfoMap.get("BP_TYPE")==null?"":bpInfoMap.get("BP_TYPE").toString().trim());
			nsBpMaster.setBpCategory(bpInfoMap.get("BP_CATEGORY")==null?"":bpInfoMap.get("BP_CATEGORY").toString().trim());
			nsBpMaster.setBpCode(bpInfoMap.get("BP_CODE")==null?"":bpInfoMap.get("BP_CODE").toString().trim());
			nsBpMaster.setRegisteredCapital(bpInfoMap.get("REGISTERED_CAPITAL")==null?"":bpInfoMap.get("REGISTERED_CAPITAL").toString().trim());
			nsBpMaster.setFoundedDate(bpInfoMap.get("FOUNDED_DATE")==null?"":bpInfoMap.get("FOUNDED_DATE").toString().trim());
			nsBpMaster.setLegalPerson(bpInfoMap.get("LEGAL_PERSON")==null?"":bpInfoMap.get("LEGAL_PERSON").toString().trim());
			nsBpMaster.setEnterpriseScale(bpInfoMap.get("ENTERPRISE_SCALE")==null?"":bpInfoMap.get("ENTERPRISE_SCALE").toString().trim());
			nsBpMaster.setRegNumberType(bpInfoMap.get("REG_NUMBER_TYPE")==null?"":bpInfoMap.get("REG_NUMBER_TYPE").toString().trim());
			nsBpMaster.setRegNumber(bpInfoMap.get("REG_NUMBER")==null?"":bpInfoMap.get("REG_NUMBER").toString().trim());
			nsBpMaster.setSegmentIndustry(bpInfoMap.get("SEGMENT_INDUSTRY")==null?"":bpInfoMap.get("SEGMENT_INDUSTRY").toString().trim());
			nsBpMaster.setHighPrecision(bpInfoMap.get("HIGH_PRECISION")==null?"":bpInfoMap.get("HIGH_PRECISION").toString().trim());
			nsBpMaster.setEconomicType(bpInfoMap.get("ECONOMIC_TYPE")==null?"":bpInfoMap.get("ECONOMIC_TYPE").toString().trim());
			nsBpMaster.setEnterpriseHonor(bpInfoMap.get("ENTERPRISE_HONOR")==null?"":bpInfoMap.get("ENTERPRISE_HONOR").toString().trim());
			nsBpMaster.setOrgType(bpInfoMap.get("ORG_TYPE")==null?"":bpInfoMap.get("ORG_TYPE").toString().trim());
			nsBpMaster.setOrgSubType(bpInfoMap.get("ORG_SUB_TYPE")==null?"":bpInfoMap.get("ORG_SUB_TYPE").toString().trim());
			nsBpMaster.setParkAddress(bpInfoMap.get("PARK_ADDRESS")==null?"":bpInfoMap.get("PARK_ADDRESS").toString().trim());
			nsBpMaster.setMainProducts(bpInfoMap.get("MAIN_PRODUCTS")==null?"":bpInfoMap.get("MAIN_PRODUCTS").toString().trim());
			nsBpMaster.setEmployeeId(bpInfoMap.get("EMPLOYEE_ID")==null?"":bpInfoMap.get("EMPLOYEE_ID").toString().trim());
			nsBpMaster.setLeaseOrganization(bpInfoMap.get("LEASE_ORGANIZATION")==null?"":bpInfoMap.get("LEASE_ORGANIZATION").toString().trim());
			nsBpMaster.setActualLessee(bpInfoMap.get("ACTUAL_LESSEE")==null?"":bpInfoMap.get("ACTUAL_LESSEE").toString().trim());
			nsBpMaster.setScoreTemplateId(bpInfoMap.get("SCORE_TEMPLATE_ID")==null?"":bpInfoMap.get("SCORE_TEMPLATE_ID").toString().trim());
			nsBpMaster.setMarketSize(bpInfoMap.get("MARKET_SIZE")==null?"0":bpInfoMap.get("MARKET_SIZE").toString().trim());
			nsBpMaster.setFinancingScale(bpInfoMap.get("FINANCING_SCALE")==null?"0":bpInfoMap.get("FINANCING_SCALE").toString().trim());
			
		}catch(Exception e) {
			log.info("更新客户信息失败！ ",e);
			throw e;
		}
		return nsBpMaster;
	}
	
	/**
	 * 组装评级所需的财报数据
	 * @param to_map
	 * @param from_map
	 * @param _numFinReport
	 * @return
	 */
	public Map<String, String> getCalculateFinData(Map<String, String> to_map,Map<String, Object> from_map,int _numFinReport) {
		if(0 == _numFinReport) {
			if (Constats.STR_F75.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F75, from_map.get("AMOUNT").toString());// 主营业务收入
			}else if (Constats.STR_F94.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F94, from_map.get("AMOUNT").toString());// 营业利润
			}
			else if (Constats.STR_F82.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F82, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F53.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F53, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F39.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F39, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F54.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F54, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F55.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F55, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F36.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F36, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F3.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F193, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F51.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F51, from_map.get("AMOUNT").toString());// 营业利润
			}else if (Constats.STR_F79.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F180, from_map.get("AMOUNT").toString());// 主营业务成本
			} else if (Constats.STR_F126.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F126, from_map.get("AMOUNT").toString());// 货币资金
			} else if (Constats.STR_F106.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F152, from_map.get("AMOUNT").toString());// 净利润
			} else if (Constats.STR_F6.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F302, from_map.get("AMOUNT").toString());// 应收账款
			} else if (Constats.STR_F5.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F305, from_map.get("AMOUNT").toString());// 应收票据
			} else if (Constats.STR_F49.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F306, from_map.get("AMOUNT").toString());// 一年内到期非流动负债
			} else if (Constats.STR_F11.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F309, from_map.get("AMOUNT").toString());// 存货
			} else if (Constats.STR_F61.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F311, from_map.get("AMOUNT").toString());// 负债合计
			} else if (Constats.STR_F71.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F312, from_map.get("AMOUNT").toString());// 净资产
			}
			/*else {
				to_map.put(from_map.get("FINSTATEMENTITEM").toString(), from_map.get("AMOUNT").toString());
			}*/
		}else if(1 == _numFinReport) {
			if (Constats.STR_F75.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F300, from_map.get("AMOUNT").toString());// 上期主营业务收入
			} else if (Constats.STR_F6.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F303, from_map.get("AMOUNT").toString());// 上期应收账款
			} else if (Constats.STR_F5.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F304, from_map.get("AMOUNT").toString());// 上期应收票据
			} else if (Constats.STR_F11.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F310, from_map.get("AMOUNT").toString());// 上期存货
			}
		}else{
			if(Constats.STR_F75.equals(from_map.get("FINSTATEMENTITEM").toString().trim())) {
				to_map.put(Constats.STR_F301, from_map.get("AMOUNT").toString());// 上上期主营业务收入
			}
		}
		return to_map;
	}
	
	
	public HashMap<String,String>  initCodesFinance(int _numReport) {
		HashMap<String,String> codes = null;
		if(0 == _numReport) {
			codes = new HashMap<String,String>();
			codes.put(Constats.STR_F75,"主营业务收入");
			codes.put(Constats.STR_F94,"营业利润");
			codes.put(Constats.STR_F79,"主营业务成本");
			codes.put(Constats.STR_F82,"销售费用");
			codes.put(Constats.STR_F53,"长期借款");
			codes.put(Constats.STR_F39,"短期借款");
			codes.put(Constats.STR_F54,"应付债券");
			codes.put(Constats.STR_F55,"长期应付款");
			codes.put(Constats.STR_F36,"资产总计");
			codes.put(Constats.STR_F3,"货币资金");
			codes.put(Constats.STR_F51,"流动负债合计");
			codes.put(Constats.STR_F106,"净利润");
			codes.put(Constats.STR_F126,"经营活动产生的现金流量净额");
			codes.put(Constats.STR_F6,"应收账款");
			codes.put(Constats.STR_F5,"应收票据");
			codes.put(Constats.STR_F49,"一年内到期非流动负债");
			codes.put(Constats.STR_F11,"存货");
			codes.put(Constats.STR_F61,"负债合计");
			codes.put(Constats.STR_F71,"净资产");
		}else if(1 ==_numReport) {
			codes = new HashMap<String,String>();
			codes.put(Constats.STR_F75,"主营业务收入");
			codes.put(Constats.STR_F5,"应收票据");
			codes.put(Constats.STR_F6,"应收账款");
			codes.put(Constats.STR_F11,"存货");
		}else if(2 ==  _numReport) {
			codes = new HashMap<String,String>();
			codes.put(Constats.STR_F75,"主营业务收入");
		}
		return codes;
	}
	
	/**
	 * 校验财报数据的合法性
	 * @param _financeMap
	 * @param num 财报批次
	 * @return	false：不合法，缺失模型计算所需的必要指标项
	 */
	public HashMap<String,String> checkLegalityOfFinance(List<Map<String, Object>> _listFinance,int num) {
		HashMap<String,String> codes = null;
			switch(num) {
				case 0:
					codes = initCodesFinance(num);
					for(Map<String, Object> _financeMap:_listFinance) {
						if(_financeMap.containsValue(Constats.STR_F75)
								||_financeMap.containsValue(Constats.STR_F94)
								||_financeMap.containsValue(Constats.STR_F79)
								||_financeMap.containsValue(Constats.STR_F82)
								||_financeMap.containsValue(Constats.STR_F53)
								||_financeMap.containsValue(Constats.STR_F39)
								||_financeMap.containsValue(Constats.STR_F54)
								||_financeMap.containsValue(Constats.STR_F55)
								||_financeMap.containsValue(Constats.STR_F36)
								||_financeMap.containsValue(Constats.STR_F3)
								||_financeMap.containsValue(Constats.STR_F51)
								||_financeMap.containsValue(Constats.STR_F106)
								||_financeMap.containsValue(Constats.STR_F126)
								||_financeMap.containsValue(Constats.STR_F6)
								||_financeMap.containsValue(Constats.STR_F5)
								||_financeMap.containsValue(Constats.STR_F49)
								||_financeMap.containsValue(Constats.STR_F11)
								||_financeMap.containsValue(Constats.STR_F61)
								||_financeMap.containsValue(Constats.STR_F71)) {
							codes.remove(_financeMap.get("FINSTATEMENTITEM").toString().trim());
							continue;
						}
					}
					break;
				case 1:
					codes = initCodesFinance(num);
					for(Map<String, Object> _financeMap:_listFinance) {
						if(_financeMap.containsValue(Constats.STR_F75)
								||_financeMap.containsValue(Constats.STR_F5)
								||_financeMap.containsValue(Constats.STR_F6)
								||_financeMap.containsValue(Constats.STR_F11)) {
							codes.remove(_financeMap.get("FINSTATEMENTITEM").toString().trim());
							continue;
						}
					}
					break;
				case 2:
					codes = initCodesFinance(num);
					for(Map<String, Object> _financeMap:_listFinance) {
						if(_financeMap.containsValue(Constats.STR_F75)) {
							codes.remove(_financeMap.get("FINSTATEMENTITEM").toString().trim());
							continue;
						}
					}
					break;
				default :
					break;
			}
		return codes;
	}
	
	/**
	 * 初始化评级所需的财报数据
	 * @return
	 */
	public  Map<String, String> initDataForModelCalculate(){
		Map<String, String> initMapForModelCalculate = new HashMap<String, String>();
		//第一年财报
		initMapForModelCalculate.put(Constats.STR_F75, "0");// 主营业务收入
		initMapForModelCalculate.put(Constats.STR_F94, "0");// 营业利润
		initMapForModelCalculate.put(Constats.STR_F180, "0");// 主营业务成本
		initMapForModelCalculate.put(Constats.STR_F82, "0");// 销售费用
		initMapForModelCalculate.put(Constats.STR_F53, "0");// 长期借款
		initMapForModelCalculate.put(Constats.STR_F39, "0");// 短期借款
		initMapForModelCalculate.put(Constats.STR_F54, "0");// 应付债券
		initMapForModelCalculate.put(Constats.STR_F55, "0");// 长期应付款
		initMapForModelCalculate.put(Constats.STR_F36, "0");// 资产总计
		initMapForModelCalculate.put(Constats.STR_F193, "0");// 货币资金
		initMapForModelCalculate.put(Constats.STR_F51, "0");// 流动负债合计
		initMapForModelCalculate.put(Constats.STR_F126, "0");// 经营活动产生的现金流量净额
		initMapForModelCalculate.put(Constats.STR_F152, "0");// 净利润
		initMapForModelCalculate.put(Constats.STR_F302, "0");// 应收账款
		initMapForModelCalculate.put(Constats.STR_F305, "0");// 应收票据
		initMapForModelCalculate.put(Constats.STR_F306, "0");// 一年内到期非流动负债
		initMapForModelCalculate.put(Constats.STR_F309, "0");// 存货
		initMapForModelCalculate.put(Constats.STR_F311, "0");// 负债合计
		initMapForModelCalculate.put(Constats.STR_F312, "0");// 净资产
		//第二年财报
		initMapForModelCalculate.put(Constats.STR_F300, "0");// 上期主营业务收入
		initMapForModelCalculate.put(Constats.STR_F303, "0");// 上期应收账款
		initMapForModelCalculate.put(Constats.STR_F304, "0");// 上期应收票据
		initMapForModelCalculate.put(Constats.STR_F310, "0");// 上期存货
		//第三年财报
		initMapForModelCalculate.put(Constats.STR_F301, "0");// 上上期主营业务收入
		return initMapForModelCalculate;
	}
	
	/**
	 * 评级计算入口
	 * @param rating
	 * @param status
	 * @param master
	 * @param reportKm
	 * @param mapResult
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String,String> rating(MainRating rating, String status,NsBpMaster master,Map<String, String> reportKm ,Map<String, String> mapResult) throws Exception{
		
		try {
			//模型类型 	承租人类型/企业类型（生产，服务，新建）
			if ("".equals(rating.getType())||rating.getType() == null) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+":实时接口评级信息中TRACK_TYPE或SCORE_TEMPLATE_ID数据异常，无法匹配评级类型");
				mapResult.put("MSG", "实时接口评级信息中TRACK_TYPE或SCORE_TEMPLATE_ID数据异常，无法匹配评级类型");
				return mapResult;
			}
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+":主体评级模型计算开始！"+master.getBpCode());
			MainRating mainRating = mainRatingServiceImpl.startTrial(rating.getType(), rating, reportKm, status, rating.getFdVersion());
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+":主体评级模型计算结束！"+master.getBpCode());
			if (mainRating == null) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" ：（"+rating.getCustCode()+")指标缺失或格式错误，计算失败！");
				mapResult.put("MSG", "指标缺失或格式错误，计算失败！");
				return mapResult;
			}
			mapResult.put("BP_CODE", mainRating.getCustCode());
			mapResult.put("BP_NAME", mainRating.getCustName());
			if (mainRating.getRatingStatus().equals("010")) {
				mapResult.put("MAIN_LEVEL", mainRating.getInternLevel());
			}else {
				mapResult.put("MAIN_LEVEL", mainRating.getFinalLevel());
				if(!"0".equals(mainRating.getSco())) {
					mapResult.put("REPORTURL", "/irs/mainRating/mainRatingReport?custNo=" + mainRating.getId());//评级报告url
				}
			}
			mapResult.put("RET_STATUS", Constats.SUCCESS_STATUS);
			mapResult.put("RET_CODE", Constats.SUCCESS_CODE);
			mapResult.put("MSG", Constats.SUCCESS_STR);
			return mapResult;
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" :（"+rating.getCustCode()+"）评级失败！ ",e);
			e.printStackTrace();
			mapResult.put("MSG", "指标缺失或格式错误，计算失败！");
			throw e;
		}
	}


}
