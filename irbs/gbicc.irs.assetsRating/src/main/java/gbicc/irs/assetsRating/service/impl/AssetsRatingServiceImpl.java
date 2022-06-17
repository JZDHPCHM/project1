package gbicc.irs.assetsRating.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.engine.model.client.Executor;
import org.wsp.engine.model.client.spring.service.ExecutorFactoryService;
import org.wsp.engine.model.core.code.impl.support.ModelResult;
import org.wsp.engine.model.core.code.impl.support.ParameterResult;
import org.wsp.engine.model.core.enums.ModelCategory;
import org.wsp.engine.model.core.po.Model;
import org.wsp.engine.model.core.po.ModelDefineWrapper;
import org.wsp.engine.model.core.po.Parameter;
import org.wsp.engine.model.core.po.ParameterOption;
import org.wsp.framework.core.util.JacksonObjectMapper;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.assetsRating.entity.AssetsIndex;
import gbicc.irs.assetsRating.entity.AssetsRating;
import gbicc.irs.assetsRating.entity.RatingAssetsStep;
import gbicc.irs.assetsRating.repository.AssetsRatingRepository;
import gbicc.irs.assetsRating.service.AssetsIndexService;
import gbicc.irs.assetsRating.service.AssetsRatingService;
import gbicc.irs.assetsRating.service.RatingAssetsStepService;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.support.CommonUtils;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.vo.RatingInit;

@Service("assetsRatingService")
public class AssetsRatingServiceImpl extends DaoServiceImpl<AssetsRating, String, AssetsRatingRepository>
		implements AssetsRatingService {
	
	private static Log log = LogFactory.getLog(AssetsRatingServiceImpl.class);
	@Autowired
	private ExecutorFactoryService executorFactoryService;
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private AssetsIndexService assetsIndexService;
	@Autowired
	private RatingAssetsStepService ratingAssetsStepService;
	@Autowired
	private AssetsRatingService assetsRatingService;


	/**
	 * 获取数据库连接
	 * @return
	 * @throws SQLException
	 */
	/*public Connection achieveConnection() throws SQLException{
		Connection conn =  jdbc.getDataSource().getConnection();
		return conn;
	}
	
	public String choseModeType(String _productType) {
		String _modeType = "";
		switch(_productType){
		case Constats.YBCP:
			_modeType = Constats.YBCP_M;
			break;
		case Constats.CSZL:
			_modeType = Constats.CSZL_M;
			break;
		case Constats.FWZL:
			_modeType = Constats.FWZL_M;
			break;
		case Constats.XMZL:
			_modeType = Constats.XMZL_M;
			break;
		case Constats.ZLTZWTYY:
			_modeType = Constats.ZLTZWTYY_M;
			break;
		default:
			break;
		}
		return _modeType;
	}
	*/
	/**
	 * 资产评级信息解析并封装
	 * @param assetsRatingMap
	 * @param nsPrjProject
	 * @return	AssetsRating
	 */
	/*public AssetsRating setAssetsRating(AssetsRating assetsRating,Map<String,Object> assetsRatingMap,NsPrjProject nsPrjProject,NsBpMaster nsBpMaster) {
		assetsRating.setAssessedValue(nsPrjProject.getAssessedValue());
		assetsRating.setAssessmentMethods(nsPrjProject.getAssessmentMethods());
		assetsRating.setAssetsCode(assetsRatingMap.get("FD_ASSETS_CODE")==null?"":assetsRatingMap.get("FD_ASSETS_CODE").toString().trim());
		assetsRating.setAssetsName(assetsRatingMap.get("FD_ASSETS_NAME")==null?"":assetsRatingMap.get("FD_ASSETS_NAME").toString().trim());
		assetsRating.setCoreleaseProportion(nsPrjProject.getCoreleaseProportion());
		assetsRating.setRatingStatus(assetsRatingMap.get("FD_RATING_STATUS")==null?"":assetsRatingMap.get("FD_RATING_STATUS").toString().trim());
		assetsRating.setRatingType(assetsRatingMap.get("FD_RATING_TYPE")==null?"":assetsRatingMap.get("FD_RATING_TYPE").toString().trim());
		if("010".equals(assetsRating.getRatingStatus())) {
			if(Double.parseDouble(assetsRating.getCoreleaseProportion()) < Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
				assetsRating.setPd(new BigDecimal(0));
				assetsRating.setInternSco(new BigDecimal(0));
				assetsRating.setInternLevel("CⅣ");
			}
		}else {
			if(Double.parseDouble(assetsRating.getCoreleaseProportion()) < Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
				assetsRating.setFinalPd(new BigDecimal(0));
				assetsRating.setFinalSco(new BigDecimal(0));
				assetsRating.setFinalLevel("CⅣ");
			}else {
				assetsRating.setInternSco(assetsRating.getInternSco());
				assetsRating.setInternLevel(assetsRating.getInternLevel());
			}
		}
		assetsRating.setCreateDate(new Date());
		assetsRating.setCustCode(nsBpMaster.getBpCode());//承租人编号
		assetsRating.setCustName(nsBpMaster.getBpName());//承租人名称
		assetsRating.setFdVersion(assetsRatingMap.get("FD_VERSION")==null?"":assetsRatingMap.get("FD_VERSION").toString().trim());
		assetsRating.setFinalAdvice(assetsRatingMap.get("FD_FINAL_ADVICE")==null?"":assetsRatingMap.get("FD_FINAL_ADVICE").toString().trim());
		assetsRating.setFinalCode(assetsRatingMap.get("FD_FINAL_CODE")==null?"":assetsRatingMap.get("FD_FINAL_CODE").toString().trim());
//		assetsRating.setFinalLevel(finalLevel);
		assetsRating.setFinalName(assetsRatingMap.get("FD_FINAL_NAME")==null?"":assetsRatingMap.get("FD_FINAL_NAME").toString().trim());
//		assetsRating.setFinalPd(finalPd);//复评资产保障倍数
//		assetsRating.setFinalSco(finalSco);//复评分数
		assetsRating.setFinanceAmount(nsPrjProject.getFinanceAmount());
//		assetsRating.setId(id);//uuid
		assetsRating.setInternCode(assetsRatingMap.get("FD_INTERN_CODE")==null?assetsRating.getInternCode():assetsRatingMap.get("FD_INTERN_CODE").toString().trim());
//		assetsRating.setInternLevel(internLevel);
		assetsRating.setInternName(assetsRatingMap.get("FD_INTERN_NAME")==null?assetsRating.getInternName():assetsRatingMap.get("FD_INTERN_NAME").toString().trim());
//		assetsRating.setInternSco(internSco);//评级资产得分
		assetsRating.setIsCorelease(nsPrjProject.getIscorelease());
//		assetsRating.setLastModifier(lastModifier);
//		assetsRating.setLastModifyDate(lastModifyDate);
		assetsRating.setLeaseame(nsPrjProject.getLeaseItemShortName());
		assetsRating.setLeaseTerm(nsPrjProject.getLeaseTerm());
		assetsRating.setMajorWarning(assetsRatingMap.get("MAJOR_RISK_WARNING")==null?"":assetsRatingMap.get("MAJOR_RISK_WARNING").toString().trim());
		assetsRating.setMargin(nsPrjProject.getMargin());
		assetsRating.setNetValue(nsPrjProject.getNetValue());
		assetsRating.setOriginalValue(nsPrjProject.getOriginalValue());
//		assetsRating.setPd(pd);//资产保障倍数
		assetsRating.setProjectCode(nsPrjProject.getProjectNumber());
		assetsRating.setProjectName(nsPrjProject.getProjectName());
		assetsRating.setInternDate("010".equals(assetsRating.getRatingStatus())?new Date():assetsRating.getInternDate());
		assetsRating.setFinalDate("020".equals(assetsRating.getRatingStatus())?new Date():assetsRating.getFinalDate());
		assetsRating.setFdDate("020".equals(assetsRating.getRatingStatus())?Tools.addYear(new Date(), 1):null);
		assetsRating.setStartDate(nsPrjProject.getLeaseStartDate());
//		assetsRating.setSteps(steps);//RatingAssetsStep
		assetsRating.setType(choseModeType(nsPrjProject.getProductType()));//
		assetsRating.setRatingVaild("1");
		assetsRating.setVaild("1");
		if("010".equals(assetsRating.getRatingStatus())) {
			assetsRating.setCreator(assetsRating.getInternName());//创建人
			assetsRating.setCreateDate(new Date());
		}else {
			assetsRating.setLastModifier(assetsRating.getFinalName());
			assetsRating.setLastModifyDate(new Date());
		}
		return assetsRating;
	}
	*/
	/**
	 * 检验资产评级指标是否有更新
	 * @param projectNum 资产编号
	 * @return	false 已更新		true 无更新
	 */
	/*public boolean checkChanged(String id,String _projetNum,List<Map<String, Object>> newItems) {
		boolean isChange = true;
		String sql = "SELECT * FROM NS_ASSETS_RATE_ITEMS WHERE FD_PROJECT_NUMBER = ? AND FD_VAILD = '1' AND FD_ASSETS_RATE_ID=? ";
		List<Map<String, Object>> oldItems = jdbc.queryForList(sql,_projetNum,id);
		if(oldItems.size() < 1) {
			return false;
		}
		for(int i = 0 ; i < oldItems.size() ; i++ ) {
			for(Map<String, Object> tmp :newItems) {
				if(oldItems.get(i).get("FD_CODE").equals(tmp.get("CODE"))) {
					if(!oldItems.get(i).get("FD_VALUE").equals(tmp.get("VALUE"))) {
						return isChange = false ;
					}
					break;
				}
			}
		}
		
		return isChange;
	}

*/
	
	/**
	 * 解析报文
	 * @param map报文
	 * @return 处理结果
	 */
	/*@Override
	public Map<String, String> parsingData(Map<String, Object> map){
		Map<String, String> result = new HashMap<String, String>();
		Map<String,Object> projectInfo = JSONObject.parseObject(map.get("projectInfo").toString());
		Map<String,Object> assetsRatingInfo = JSONObject.parseObject(map.get("assetsRatingInfo").toString());
		result.put("PROJECT_NUMBER", projectInfo.get("PROJECT_NUMBER")==null?null:projectInfo.get("PROJECT_NUMBER").toString().trim());
		result.put("PROJECT_NAME",projectInfo.get("PROJECT_NAME")==null?null:projectInfo.get("PROJECT_NAME").toString().trim());
		result.put("ASSETS_LEVEL", null);
		result.put("REPORTURL", null);
		result.put("RET_STATUS", "F");
		result.put("RET_CODE", "111111");
		result.put("MSG", "评级失败!");
		Connection conn	= null;
		Statement stmt 	= null;
		List<String> sqls = new ArrayList<String>();
		try {
			conn = achieveConnection(); 
			if(projectInfo.get("PROJECT_NUMBER")==null||"".equals(projectInfo.get("PROJECT_NUMBER").toString().trim())) {
				log.info("项目编号为空，评级失败!");
				result.put("MSG", "项目编号为空，评级失败!");
				return result;
			}
			NsPrjProject nsPrjProject = NsPrjProjectServiceImpl.setNsPrjProject(projectInfo);
			
			sqls.add("DELETE NS_PRJ_PROJECT WHERE PROJECT_NUMBER = '"+nsPrjProject.getProjectNumber()+"'");
			sqls.add("INSERT INTO NS_PRJ_PROJECT (PROJECT_NUMBER,BP_ID_TENANT,RISK_MANAGER_NAME,DOCUMENT_TYPE,LEASE_CHANNEL,"
					+ "PROJECT_NAME,EMPLOYEE_ID,LEASE_ORGANIZATION,ENTERPRISE_SCALE,ASSISTING_PM_A,LEASE_START_DATE,LEASE_TERM,"
					+ "FINANCE_AMOUNT,MARGIN,LEASE_ITEM_SHORT_NAME,PRODUCT_TYPE,ISCORELEASE,CORELEASE_PROPORTION,ORIGINAL_VALUE,"
					+ "NET_VALUE,ASSESSED_VALUE,ASSESSMENT_METHODS)values("
					+ "'"+nsPrjProject.getProjectNumber()+"',"+nsPrjProject.getBpIdTenant()+",'"+nsPrjProject.getRiskManagerName()+"'"
					+ ",'"+nsPrjProject.getDocumentType()+"','"+nsPrjProject.getLeaseChannel()+"','"+nsPrjProject.getProjectName()+"'"
					+ ",'"+nsPrjProject.getEmployeeId()+"','"+nsPrjProject.getLeaseOrganization()+"','"+nsPrjProject.getEnterpriseCcale()+"'"
					+ ",'"+nsPrjProject.getAssistingPmA()+"','"+nsPrjProject.getLeaseStartDate()+"','"+nsPrjProject.getLeaseTerm()+"','"
					+ nsPrjProject.getFinanceAmount()+"','"+nsPrjProject.getMargin()+"','"+nsPrjProject.getLeaseItemShortName()+"',"
					+ "'"+nsPrjProject.getProductType()+"','"+nsPrjProject.getIscorelease()+"','"+nsPrjProject.getCoreleaseProportion()+"'"
					+ ",'"+nsPrjProject.getOriginalValue()+"','"+nsPrjProject.getNetValue()+"','"+nsPrjProject.getAssessedValue()+"'"
					+ ",'"+nsPrjProject.getAssessmentMethods()+"')");
			AssetsRating assetsRating = new	AssetsRating();
			String _queryCustInfo = "SELECT FD_BP_NAME AS bpName,FD_BP_CODE AS bpCode FROM NS_BP_MASTER WHERE FD_ID=?";
			NsBpMaster nsBpMaster = (NsBpMaster) jdbc.queryForObject(_queryCustInfo, new BeanPropertyRowMapper(NsBpMaster.class), nsPrjProject.getBpIdTenant());
			
			String currentDate = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date());
			String _finalDate = "";
			String _internDate = "";
			String _fDDate = "";
			String id = "";
			//复评
			if ("020".equals(assetsRatingInfo.get("FD_RATING_STATUS"))) {
				log.info("资产复评开始："+nsPrjProject.toString());
				String queryF = "SELECT COUNT(*) 	FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010' ";
				Integer count 	= jdbc.queryForObject(queryF, Integer.class, nsPrjProject.getProjectNumber());
				if (count > 0) {
					String sql 	= "SELECT FD_ID 	FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					sqls.add("UPDATE NS_ASSETS_STEP SET FD_VAILD='2' WHERE FD_RATEID='"+id+"' AND FD_VAILD='1'");
				} else {
					String queryS	= "SELECT COUNT(*) 	FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020' ";
					count 		= jdbc.queryForObject(queryS, Integer.class, nsPrjProject.getProjectNumber());
					if (count > 0) {
						String sql 	= "SELECT FD_ID FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
						id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
						sqls.add("UPDATE NS_ASSETS_STEP SET FD_VAILD='0' WHERE FD_RATEID='"+id+"' AND FD_VAILD='1'");	
					} else {
						log.info("无初评记录，无法复评!");
						result.put("MSG", "无初评记录，无法复评!");
						return result;
					}
				}
				assetsRating = setAssetsRating(assetsRatingService.getRepository().findOne(id),assetsRatingInfo,nsPrjProject,nsBpMaster);
				_finalDate = assetsRating.getFinalDate()==null?null:Tools.formatDate(assetsRating.getFinalDate(),"yyyy-MM-dd hh:mm:ss");
				_fDDate = assetsRating.getFdDate()==null?null:Tools.formatDate(assetsRating.getFdDate(),"yyyy-MM-dd hh:mm:ss");
				if(Double.parseDouble(assetsRating.getCoreleaseProportion()) < Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
					sqls.add("UPDATE NS_ASSETS_RATING SET FD_DATE=TO_DATE(DECODE('"+_fDDate+"','null',null,'"+_fDDate+"'),'YYYY-MM-DD hh:mi:ss')"
							+ ",FD_FINAL_DATE=TO_DATE(DECODE('"+_finalDate+"','null',null,'"+_finalDate+"'),'YYYY-MM-DD hh:mi:ss')"
							+ ",ASSESSMENT_METHODS='"+assetsRating.getAssessmentMethods()+"',ASSESSED_VALUE='"+assetsRating.getAssessedValue()+"'"
							+ ",NET_VALUE='"+assetsRating.getNetValue()+"',ORIGINAL_VALUE='"+assetsRating.getOriginalValue()+"'"
							+ ",CORELEASE_PROPORTION='"+assetsRating.getCoreleaseProportion()+"',ISCORELEASE='"+assetsRating.getIsCorelease()+"'"
							+ ",LEASE_ITEM_SHORT_NAME='"+assetsRating.getLeaseame()+"',MARGIN='"+assetsRating.getMargin()+"',FINANCE_AMOUNT='"+assetsRating.getFinanceAmount()+"'"
							+ ",LEASE_TERM='"+assetsRating.getLeaseTerm()+"',LEASE_START_DATE='"+assetsRating.getStartDate()+"',PRODUCT_TYPE='"+assetsRating.getType()+"'"
							+ ",FD_PROJECT_NAME='"+assetsRating.getProjectName()+"',FD_PROJECT_CODE='"+assetsRating.getProjectCode()+"',FD_CUST_NAME='"+assetsRating.getCustName()+"'"
							+ ",FD_CUST_CODE='"+assetsRating.getCustCode()+"',FD_INTERN_CODE='"+assetsRating.getInternCode()+"',FD_INTERN_NAME='"+assetsRating.getInternName()+"'"
							+ ",FD_ASSETS_CODE='"+assetsRating.getAssetsCode()+"',FD_ASSETS_NAME='"+assetsRating.getAssetsName()+"',FD_FINAL_CODE='"+assetsRating.getFinalCode()+"'"
							+ ",FD_FINAL_NAME='"+assetsRating.getFinalName()+"',FD_FINAL_ADVICE='"+assetsRating.getFinalAdvice()+"'"
							+ ",MAJOR_RISK_WARNING='"+assetsRating.getMajorWarning()+"',FD_RATING_STATUS='"+assetsRating.getRatingStatus()+"',FD_RATING_TYPE='"+assetsRating.getRatingType()+"'"
							+ ",FD_VERSION='"+assetsRating.getFdVersion()+"',FD_FINAL_PD='0',FD_FINAL_SCO='0',FD_FINAL_LEVEL='CⅣ' "
							+ ",FD_LAST_MODIFIER='"+assetsRating.getFinalName()+"',FD_LAST_MODIFYDATE=TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss')  WHERE FD_ID='"+assetsRating.getId()+"'");
				
//					assetsRatingService.getRepository().save(entity)
				}else {
					sqls.add("UPDATE NS_ASSETS_RATING SET FD_DATE=TO_DATE(DECODE('"+_fDDate+"','null',null,'"+_fDDate+"'),'YYYY-MM-DD hh:mi:ss')"
							+ ",FD_FINAL_DATE=TO_DATE(DECODE('"+_finalDate+"','null',null,'"+_finalDate+"'),'YYYY-MM-DD hh:mi:ss')"
							+ ",ASSESSMENT_METHODS='"+assetsRating.getAssessmentMethods()+"',ASSESSED_VALUE='"+assetsRating.getAssessedValue()+"'"
							+ ",NET_VALUE='"+assetsRating.getNetValue()+"',ORIGINAL_VALUE='"+assetsRating.getOriginalValue()+"'"
							+ ",CORELEASE_PROPORTION='"+assetsRating.getCoreleaseProportion()+"',ISCORELEASE='"+assetsRating.getIsCorelease()+"'"
							+ ",LEASE_ITEM_SHORT_NAME='"+assetsRating.getLeaseame()+"',MARGIN='"+assetsRating.getMargin()+"',FINANCE_AMOUNT='"+assetsRating.getFinanceAmount()+"'"
							+ ",LEASE_TERM='"+assetsRating.getLeaseTerm()+"',LEASE_START_DATE='"+assetsRating.getStartDate()+"',PRODUCT_TYPE='"+assetsRating.getType()+"'"
							+ ",FD_PROJECT_NAME='"+assetsRating.getProjectName()+"',FD_PROJECT_CODE='"+assetsRating.getProjectCode()+"',FD_CUST_NAME='"+assetsRating.getCustName()+"'"
							+ ",FD_CUST_CODE='"+assetsRating.getCustCode()+"',FD_INTERN_CODE='"+assetsRating.getInternCode()+"',FD_INTERN_NAME='"+assetsRating.getInternName()+"'"
							+ ",FD_ASSETS_CODE='"+assetsRating.getAssetsCode()+"',FD_ASSETS_NAME='"+assetsRating.getAssetsName()+"',FD_FINAL_CODE='"+assetsRating.getFinalCode()+"'"
							+ ",FD_FINAL_NAME='"+assetsRating.getFinalName()+"',FD_FINAL_ADVICE='"+assetsRating.getFinalAdvice()+"'"
							+ ",MAJOR_RISK_WARNING='"+assetsRating.getMajorWarning()+"',FD_RATING_STATUS='"+assetsRating.getRatingStatus()+"',FD_RATING_TYPE='"+assetsRating.getRatingType()+"'"
							+ ",FD_VERSION='"+assetsRating.getFdVersion()+"' "
							+ ",FD_LAST_MODIFIER='"+assetsRating.getFinalName()+"',FD_LAST_MODIFYDATE=TO_DATE('"+currentDate+"','YYYY-MM-DD hh:mi:ss')  WHERE FD_ID='"+assetsRating.getId()+"'");
				}
			}
			//初评:设置现有的评级信息评级步骤为无效
			else if ("010".equals(assetsRatingInfo.get("FD_RATING_STATUS"))) {
				log.info("资产初评开始："+nsPrjProject.toString());
				String queryStatementF = "SELECT COUNT(*) FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='010'";
				Integer _count = jdbc.queryForObject(queryStatementF, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					sqls.add("UPDATE NS_ASSETS_STEP SET FD_VAILD='0' WHERE FD_RATEID='"+id+"'");
				}
				//复评
				String queryStatementS = "SELECT COUNT(*) FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='020'";
				_count = jdbc.queryForObject(queryStatementS, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					sqls.add("UPDATE NS_ASSETS_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE FD_ID='"+id+"'");
				}

				assetsRating.setId(UUID.randomUUID().toString());
				assetsRating = setAssetsRating(assetsRating,assetsRatingInfo,nsPrjProject,nsBpMaster);
				_internDate = assetsRating.getInternDate()==null?null:Tools.formatDate(assetsRating.getInternDate(),"yyyy-MM-dd hh:mm:ss");
				
				if(Double.parseDouble(assetsRating.getCoreleaseProportion()) < Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
					sqls.add("INSERT INTO NS_ASSETS_RATING (FD_ID,FD_CUST_CODE,FD_CUST_NAME,FD_PROJECT_CODE,FD_PROJECT_NAME,PRODUCT_TYPE,LEASE_START_DATE,LEASE_TERM,FINANCE_AMOUNT"
							+ ",MARGIN,LEASE_ITEM_SHORT_NAME,ISCORELEASE,CORELEASE_PROPORTION,ORIGINAL_VALUE,NET_VALUE,ASSESSED_VALUE,ASSESSMENT_METHODS,FD_INTERN_CODE,FD_INTERN_NAME"
							+ ",FD_INTERN_DATE,FD_ASSETS_CODE,FD_ASSETS_NAME,MAJOR_RISK_WARNING,FD_RATING_STATUS"
							+ ",FD_VAILD,FD_RATING_VAILD,FD_VERSION,FD_CREATE_DATE,FD_PD,FD_INTERN_SCO,FD_INTERN_LEVEL,FD_CREATOR) values("
							+ "'"+assetsRating.getId()+"','"+assetsRating.getCustCode()+"','"+assetsRating.getCustName()+"','"+assetsRating.getProjectCode()+"','"+assetsRating.getProjectName()+"'"
							+ ",'"+assetsRating.getType()+"','"+assetsRating.getStartDate()+"','"+assetsRating.getLeaseTerm()+"','"+assetsRating.getFinanceAmount()+"'"
							+ ",'"+assetsRating.getMargin()+"','"+assetsRating.getLeaseame()+"','"+assetsRating.getIsCorelease()+"','"+assetsRating.getCoreleaseProportion()+"','"+assetsRating.getOriginalValue()+"'"
							+ ",'"+assetsRating.getNetValue()+"','"+assetsRating.getAssessedValue()+"','"+assetsRating.getAssessmentMethods()+"','"+assetsRating.getInternCode()+"','"+assetsRating.getInternName()+"'"
							+ ",to_date(decode('"+_internDate+"','null',null,'"+_internDate+"'),'yyyy-MM-dd hh:mi:ss'),'"+assetsRating.getAssetsCode()+"','"+assetsRating.getAssetsName()+"','"+assetsRating.getMajorWarning()+"','"+assetsRating.getRatingStatus()+"','"+assetsRating.getVaild()+"','"+assetsRating.getRatingVaild()+"','"+assetsRating.getFdVersion()+"'"
							+ ",to_date('"+currentDate+"','YYYY-MM-DD hh:mi:ss'),'0','0','CⅣ','"+assetsRating.getAssetsName()+"')");
				}else {
					sqls.add("INSERT INTO NS_ASSETS_RATING (FD_ID,FD_CUST_CODE,FD_CUST_NAME,FD_PROJECT_CODE,FD_PROJECT_NAME,PRODUCT_TYPE,LEASE_START_DATE,LEASE_TERM,FINANCE_AMOUNT"
							+ ",MARGIN,LEASE_ITEM_SHORT_NAME,ISCORELEASE,CORELEASE_PROPORTION,ORIGINAL_VALUE,NET_VALUE,ASSESSED_VALUE,ASSESSMENT_METHODS,FD_INTERN_CODE,FD_INTERN_NAME"
							+ ",FD_INTERN_DATE,FD_ASSETS_CODE,FD_ASSETS_NAME,MAJOR_RISK_WARNING,FD_RATING_STATUS"
							+ ",FD_VAILD,FD_RATING_VAILD,FD_VERSION,FD_CREATE_DATE,FD_CREATOR) values("
							+ "'"+assetsRating.getId()+"','"+assetsRating.getCustCode()+"','"+assetsRating.getCustName()+"','"+assetsRating.getProjectCode()+"','"+assetsRating.getProjectName()+"'"
							+ ",'"+assetsRating.getType()+"','"+assetsRating.getStartDate()+"','"+assetsRating.getLeaseTerm()+"','"+assetsRating.getFinanceAmount()+"'"
							+ ",'"+assetsRating.getMargin()+"','"+assetsRating.getLeaseame()+"','"+assetsRating.getIsCorelease()+"','"+assetsRating.getCoreleaseProportion()+"','"+assetsRating.getOriginalValue()+"'"
							+ ",'"+assetsRating.getNetValue()+"','"+assetsRating.getAssessedValue()+"','"+assetsRating.getAssessmentMethods()+"','"+assetsRating.getInternCode()+"','"+assetsRating.getInternName()+"'"
							+ ",to_date(decode('"+_internDate+"','null',null,'"+_internDate+"'),'yyyy-MM-dd hh:mi:ss'),'"+assetsRating.getAssetsCode()+"','"+assetsRating.getAssetsName()+"','"+assetsRating.getMajorWarning()+"','"+assetsRating.getRatingStatus()+"','"+assetsRating.getVaild()+"','"+assetsRating.getRatingVaild()+"','"+assetsRating.getFdVersion()+"'"
							+ ",to_date('"+currentDate+"','YYYY-MM-DD hh:mi:ss'),'"+assetsRating.getAssetsName()+"')");
				}
			}
			
			List<Map<String, Object>> lsitRateItems = (List<Map<String, Object>>) map.get("assetsInfo");
			if (checkChanged(assetsRating.getId(),nsPrjProject.getProjectNumber(),lsitRateItems)) {
				log.info("此次指标无更新，请更新指标后再次发起评级!");
				result.put("MSG", "重复评级，请更新指标后再次发起评级!");
				return result;
			}
			//解析资产评级指标数据
			sqls.add("UPDATE NS_ASSETS_RATE_ITEMS SET FD_VAILD='0' WHERE FD_PROJECT_NUMBER='"+assetsRating.getProjectCode()+"' AND FD_ASSETS_RATE_ID='"+assetsRating.getId()+"'");
			Map<String,String> assetsRateItemsMap = null;//封装模型计算所需数据
			if(Double.parseDouble(assetsRating.getCoreleaseProportion()) >= Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
				assetsRateItemsMap = new HashMap<String,String>();
				String uuidAssetsRateItems = "";
				for (int i = 0; i < lsitRateItems.size(); i++) {
					uuidAssetsRateItems = UUID.randomUUID().toString();
					AssetsRateItems assetsRateItems = new AssetsRateItems();
					assetsRateItems.setAssetsRateId(assetsRating.getId());
					assetsRateItems.setCode(lsitRateItems.get(i).get("CODE")==null?"":lsitRateItems.get(i).get("CODE").toString().trim());
					assetsRateItems.setProjectNumber(assetsRating.getProjectCode());
					assetsRateItems.setValue(lsitRateItems.get(i).get("VALUE")==null?"":lsitRateItems.get(i).get("VALUE").toString().trim());
		//			assetsRateItems.setValid("1");
					assetsRateItemsMap.put(assetsRateItems.getCode(), assetsRateItems.getValue());
					sqls.add("INSERT INTO NS_ASSETS_RATE_ITEMS VALUES('"+uuidAssetsRateItems+"','"+assetsRateItems.getAssetsRateId()+"','"+assetsRateItems.getProjectNumber()+"','"+assetsRateItems.getCode()+"','"+assetsRateItems.getValue()+"','1')");
				}
			}
			long insertStart = System.currentTimeMillis();
			conn.setAutoCommit(false);  //关闭事务
			stmt = conn.createStatement();
			for(String sqlStr : sqls){
				System.out.println(sqlStr);
				System.out.println("---------------------------");
				stmt.addBatch(sqlStr);
			}
			System.out.println("start execute sqlBatch ---------------------------");
			stmt.executeBatch();
			conn.commit();	//事务提交
			System.out.println("end execute sqlBatch ---------------------------");
			log.debug("execute sqls : " + sqls.size()+" execute time (ms) "+(System.currentTimeMillis() - insertStart));
			if(Double.parseDouble(assetsRating.getCoreleaseProportion()) >= Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
				assetsRating = testmodel(assetsRateItemsMap,assetsRating.getType(),assetsRating.getId(),assetsRating.getRatingStatus());
				if (assetsRating == null) {
					result.put("MSG", "指标缺失或格式错误，计算失败！");
					throw new Exception(this.getClass().getName()+".parsingData():指标缺失或格式错误，计算失败！");
				}
			}
			result.put("PROJECT_NUMBER", assetsRating.getProjectCode());
			result.put("PROJECT_NAME",assetsRating.getProjectName());
			if ("010".equals(assetsRating.getRatingStatus())) {
				result.put("ASSETS_LEVEL", assetsRating.getInternLevel());
			}else {
				result.put("ASSETS_LEVEL", assetsRating.getFinalLevel());
			}
			result.put("REPORTURL", "/irs/assetsRating/report?custNo="+assetsRating.getId());//评级报告url
			result.put("RET_STATUS", Constats.SUCCESS_STATUS);
			result.put("RET_CODE", Constats.SUCCESS_CODE);
			result.put("MSG", "成功");
		} catch (Exception e) {
			log.info(this.getClass().getName()+" : "+e.getMessage());
			e.printStackTrace();
			if(conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.info(e1.getMessage());
					e1.printStackTrace();
					return result;
				}
			}
			return result;
		}finally {
			if(stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.info(e.getMessage());
					e.printStackTrace();
					return result;
				}
			}
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.info(e.getMessage());
					e.printStackTrace();
					return result;
				}
			}
		}
		return result;
	}
	*/
	
	@Override
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取模型
	 */
	@Override
	public Model getModel(String modelCode) throws Exception {
		String modelId = "";
		Model model = null;
		try {
			Executor executor = executorFactoryService.getExecutor();
			ModelDefineWrapper map = executor.getLoader().getModelByCode(modelCode, null);
			if (map != null) {
				model = map.getModel();
				if (model != null) {
					modelId = model.getId();
					if (!StringUtils.isEmpty(modelId)) {
						return model;
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("模型引擎未配置！");
		}
		return null;
	}

	@Override
	public MainRating startTrial(String type, String mainId, Map<String, String> map, String status, String version)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> subjects(MainRating rating, String status) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/**
	 * 计算评级结果
	 * 
	 * @throws Exception
	 */

	public Map<String, String> ratingResults(String modelCode, Map<String, String> paramValue) {
		Executor executor = executorFactoryService.getExecutor();
		Map<String, String> value = new HashMap<String, String>();
		try {
			String jsonz = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(paramValue);
			ModelResult result = executor.executeByCode(modelCode, null, jsonz);
			value = indicatorsValue(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);  
		}

		return value;
	}
	
	
	/**
	 * 
	 * @param result
	 * @param code
	 * @return 解析评级结果，获取code及结果值
	 */
	public Map<String, String> indicatorsValue(ModelResult result) {
		Map<String, String> resultList = new HashMap<String, String>();
		List<ParameterResult> para = result.getData();
		for (ParameterResult parameterResult : para) {
			resultList.put(parameterResult.getCode(), parameterResult.getValue());
		}
		return resultList;
	}

	@Override
	@Transactional
	public AssetsRating testmodel(Map<String, String> paramValue,String type,AssetsRating mainG,String status) {
		System.out.println("~~~~~~请求的参数map："+JSONObject.toJSON(paramValue));
		
		//根据前端传入的资产主体记录id获取该条记录
		//Optional<AssetsRating> assetsRating = assetsRatingService.getRepository().findById(mainId);
		
		//根据项目编号、客户编号获取assets_rating 表中的资产评级记录id
//		String mainVaildId="";
//		try {
//			mainVaildId=jdbc.queryForObject("select FD_ID FROM NS_ASSETS_RATING where fd_cust_code=? and fd_project_code=? and fd_vaild=1",
//					String.class,assetsRating.get().getProjectCode(),assetsRating.get().getCustCode());
//		} catch (Exception e) {
//			mainVaildId="";
//		}
		Model model;
		try {
			model = this.getModel(type);
			//AssetsRating mainG = assetsRating.get();//根据前端传入的id获取到的资产评级记录
			//mainG.setRatingStatus(status);
			//将获取到的评级记录copy一份出来
//			AssetsRating mainR = new AssetsRating();//
//			if(status.equals("000")) {
//				mainR.setCustCode(mainG.getCustCode());
//				mainR.setCustName(mainG.getCustName());
//				mainR.setProjectCode(mainG.getProjectCode());
//				mainR.setProjectName(mainG.getProjectName());
//				mainR.setType(mainG.getType());
//				mainR.setVaild("1");
//				mainR.setRatingStatus("000");
//				//mainR.setFdVersion(version);
//				mainR.setFinalDate(new Date());
//				mainG = repository.save(mainR);
//			}
			AssetsRating rating = saveRatingStepAndIndex(mainG, model, paramValue, type,status);
			if (rating == null) {
//				assetsRatingService.getRepository().deleteById(mainG.getId());
//				mainR.setVaild("1");
//				mainG = repository.save(mainR);
				return null;
			}
			rating.setRatingStatus(status);
			rating.setVaild("1");
			Map<String, String> value = ratingResults(type, paramValue);
			System.out.println("~~~~模型结果："+JSONObject.toJSON(value));
			//rating.setQuanSco(value.get("DL_RESULT"));
			//rating.setQualSco(value.get("DX_RESULT"));
			//rating.setSco(value.get(type + "_RESULT"));
			BigDecimal internSco = new BigDecimal("0");
			if(status.equals("010")) {//初评
				rating.setInternSco(new BigDecimal(value.get("DX_"+type+"_RESULT")));
				internSco = rating.getInternSco();
				rating.setInternDate(new Date());
				rating.setPd(new BigDecimal(value.get("DX_"+type+"_BZBS")));
			}else {//复评
				rating.setFinalSco(new BigDecimal(value.get("DX_"+type+"_RESULT")));
				internSco = rating.getFinalSco();
				rating.setFinalPd(new BigDecimal(value.get("DX_"+type+"_BZBS")));
				Date date = new Date();
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);//设置起时间
			    //System.out.println("111111111::::"+cal.getTime());
			    cal.add(Calendar.YEAR, 1);//增加一年
				rating.setFinalDate(date);//设置复评时间
				//rating.setFdDate(fdDate);
				if(status.equals("000")) {
					//设置复评经理
					rating.setFinalName(SecurityUtil.getUserName());
					rating.setFdDate(cal.getTime());
				}
			}
			rating = calculate(rating,type,status,internSco);
			
			return rating;
			
			
			
			
		}catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：评级失败！",e);
			// TODO: handle exception
//			jdbc.update("update ns_assets_rating set fd_vaild='0' where FD_CUST_CODE=? and fd_project_code=? and fd_id = ?", 
//					assetsRating.get().getProjectCode(),assetsRating.get().getCustCode(),assetsRating.get().getId());
			e.printStackTrace();
			return null;
		}
	}
	@Transactional
	public AssetsRating calculate(AssetsRating mainRating,String modelType,String status,BigDecimal internSco) {
		String type = "050";
		String version="";
		
		if("ZC_XMZL_2".equals(modelType)) {//如果是项目租赁
			version="1.0";
		}else {
			version="2.0";
		}
		
		
	/**
	 * 版本1.0 设置type为 “020” 或者 “010”
	 * 为本2.0 设置type为 “040”
	 */
		String code = "";
		try {
			code = jdbc.queryForObject(
					"select FD_CODE from Ns_main_level where fd_type='050' and fd_rating_vaild=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?)",
					String.class,version, internSco, internSco);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Double pd = null;
		try {
//			pd = jdbc.queryForObject(
//					"select FD_PD as FD_PD from ns_cfg_main_scale where FD_TYPE=? and fd_scale_level = (select fd_code from Ns_main_level where fd_type=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?))",
	//				Double.class, type, type, mainRating.getSco(), mainRating.getSco());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//mainRating.setPd(String.valueOf(pd));
		if(status.equals("010")) {
			mainRating.setInternLevel(code);
		}else {
			mainRating.setFinalLevel(code);
		}
		if("000".equals(status)) {
			jdbc.update("update ns_assets_rating set fd_vaild='0' where FD_CUST_CODE=? AND FD_PROJECT_CODE=? AND fd_id <> ? ",
					mainRating.getCustCode(),mainRating.getProjectCode(),mainRating.getId());
			assetsRatingService.getRepository().save(mainRating);
		}
		
		return mainRating;
	}
	@Transactional
	public AssetsRating saveRatingStepAndIndex(AssetsRating rating, Model model, Map<String, String> paramValue,String type,String status) {
		
		
		
		//获取模型的解析参数
		List<Model> subModels = model.getSubModels();
		
		List<RatingAssetsStep> ratingSteps = new ArrayList<RatingAssetsStep>();
		
		//获取模型返回的数据
		Map<String, String> modelGetMap = new HashMap<String, String>();
		try {
			modelGetMap = ratingResults(model.getCode(), paramValue);
		} catch (Exception e) {
//			assetsRatingService.getRepository().deleteById(rating.getId());
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line：",e);
			throw new RuntimeException(e);  
		}
		
		if (modelGetMap == null || modelGetMap.size() == 0) {
			jdbc.update("delete ns_assets_rating  where fd_id = ?", rating.getId());
			return null;
		}
		System.out.println("~~~~~~调取模型之后的map："+JSONObject.toJSON(modelGetMap));
		//获取步骤表，用来匹配 是定性还是定量
		List<Map<String, Object>> list = jdbc
				.queryForList("select * from  NS_R_CFG_STEPS where FD_RATINGCFG_ID='1'  order by fd_stepnum asc");
		System.out.println("~~~~:"+subModels);
//		String uuids = UUID.randomUUID().toString();
		System.out.println("~~~~:"+JSONObject.toJSON(subModels));
		//将步骤表中的评级id设置为其他
		
		if("000".equals(status)) {
			//这块主要是将页面记录的评级步骤的指标信息清空
			String countnum = jdbc.queryForObject("select count(1) countnum from ns_assets_step where fd_rateid=?", String.class,rating.getId());
			
			if(Integer.parseInt(countnum)>0) {
				jdbc.update("update ns_assets_step set fd_rateid='1' where fd_rateid=?",rating.getId());
			}
		}
		
		if("020".equals(status)) {
			//判断如果是复评的，为防止多次调用复评，造成step表中有多条复评数据
			String countnum = jdbc.queryForObject("select count(1) countnum from ns_assets_step where fd_rateid=? and fd_vaild='3'", String.class,rating.getId());
			
			if(Integer.parseInt(countnum)>0) {
				jdbc.update("update ns_assets_step set fd_rateid='1' where fd_rateid=?",rating.getId());
			}
		}
		
		//如果是评级测算 将step表中的fd_vaild设置为1，如果是初评将step表中的fd_vaild设置为2，如果是复评将fd_vaild设置为3
		for (Map<String, Object> stepMap : list) {
			if (RatingStepType.QUANTITATIVE.toString().equals(stepMap.get("FD_STEPTYPE").toString())
					|| RatingStepType.QUALITATIVE_EDIT.toString().equals(stepMap.get("FD_STEPTYPE").toString())) {
				//如果是定性或者定量的步骤，就存取结果
				RatingAssetsStep step = new RatingAssetsStep();
				step.setSno(Integer.parseInt(stepMap.get("FD_STEPNUM").toString()));
				//设置步骤序号
				step.setRatingMain(rating);												//评级对象  FD_RATEID 评级id
				step.setStepName(stepMap.get("FD_STEPNAME").toString());			//步骤名称  fd_step_name （定量分析、定性分析、评级报告）
				step.setStepResources(stepMap.get("FD_RESOURCEPATH").toString());	//fd_step_resources 步骤资源
				//FD_STEP_TYPE 步骤类型 QUANTITATIVE-定量信息  QUALITATIVE_EDIT-定性信息 REPORT_INFO-评级报告
				step.setStepType(RatingStepType.valueOf(stepMap.get("FD_STEPTYPE").toString()));	
				if("000".equals(status)) {
					step.setVaild("1");// 1 评级测算 2 初评 3 复评
				}else if("010".equals(status)) {
					step.setVaild("2");//1 评级测算 2 初评 3 复评
				}else if("020".equals(status)) {
					step.setVaild("3");//1 评级测算 2 初评 3 复评
				}
				//step.setVaild("1");//是否有效 1 是
				try {
					step = ratingAssetsStepService.add(step);
				} catch (Exception e) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line：",e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
				if(RatingStepType.QUANTITATIVE.toString().equals(stepMap.get("FD_STEPTYPE").toString())) {
					//添加定量的指标
					try {
						insertIndexes(subModels, paramValue, type, modelGetMap, step,"DL");
					} catch (Exception e) {
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line：",e);
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}else {
					//添加定性的指标
					try {
						insertIndexes(subModels, paramValue, type, modelGetMap, step,"DX");
					} catch (Exception e) {
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line：",e);
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
				
			}
		}
		
		rating.setCurrentStepId(ratingSteps.get(0).getId());
		rating.setCurrentStep(ratingSteps.get(0));
		return rating;
		
		
		
	}
	
	
	/**
	 * 
	 * @param List<Model> subModels       模型的subModel
	 * @param Map         paramValue  	  传入模型的参数
	 * @param String      type		  	  模型类型
	 * @param Map         modelGetMap 	  模型返回来的参数
	 * @param AssetsRatingStep stepMap	  步骤实体
	 * @param String 	  types		  	  DL DX
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void insertIndexes(List<Model> subModels,Map<String, String> paramValue,String type,Map<String,String> modelGetMap,RatingAssetsStep step,String types) throws Exception{
		for (Model model2 : subModels) {
			List<Parameter> allParameters = model2.getAllParameters();
			String name = model2.getName();
			String level = "1";
			String code = model2.getCode();
			String codetype = code.substring(0,code.indexOf("_"));
			if(codetype.equals(types)) {
				//存放一级指标
				//如果1级指标下没有数据就不需要存放
				List<Model> subModels2 = model2.getSubModels();
				if(subModels2.size()!=0) {//如果一级指标下面没有二级指标，一级指标就不需要存储，直接解析allParameters下的数据
					AssetsIndex index = new AssetsIndex();
					index.setId(UUID.randomUUID().toString());//主键id
					index.setRatingStep(step);//设置步骤id
					if(codetype.equals("DL")) {
						index.setIndexType(RatingStepType.QUANTITATIVE);//指标类型定量
					}else if(codetype.equals("DX")) {
						index.setIndexType(RatingStepType.QUALITATIVE_EDIT);//指标类型定量
					}
					index.setIndexCategory(name);//指标分类
					String indexname = model2.getName();
					index.setIndexName(indexname);//指标名称
					index.setIndexId(model2.getId());//指标id
					index.setIndexCode(model2.getCode());//指标编号
					//index.setIndexValue(paramValue.get(param.getCode()));
					//index.setParentId(parentid);
					index.setLevel(level);
					index.setIndexScore(new BigDecimal(modelGetMap.get(model2.getCode()+"_RESULT")));
//					index.setCreator("system");
//					index.setLastModifier("system");
//					index.setCreateDate(new Date());
//					index.setLastModifyDate(new Date());
					index.setFdmodel(type);
					assetsIndexService.add(index);
				}
				//存放allParameters下的指标项
				for(Parameter param : allParameters) {
					insertDxIndex(type,allParameters,name, step, level, model2.getId(), paramValue, modelGetMap,param,codetype);
				}
			}
				continue;
		}
	}

	/**
	 * 
	 * @param name 模型指标分类名称
	 * @param step 评级步骤
	 * @param level  模型等级
	 * @param modelparentid 模型指标的父id
	 * @param paramValue	传入模型的参数
	 * @param modelGetMap  模型计算返回参数
	 * @param param		Parameter具体参数
	 * @param codetype	DX 或 DL
	 * @throws Exception
	 */
	public void insertDxIndex(String type,List<Parameter> allParameters,String name,RatingAssetsStep step,String level,String modelparentid,Map<String, String> paramValue,Map<String,String> modelGetMap,Parameter param,String codetype) throws Exception {
		if (!StringUtils.isEmpty(param.getCode()) && param.getName().contains("输入值")) {
			AssetsIndex index = new AssetsIndex();
			System.out.println("~~~~~~kankanparam:"+JSONObject.toJSON(param));
			String parentid=null;
			String indexWeight = null;
			
			//根据编码去匹配权重
			for(Parameter params : allParameters) {
				if(params.getCode().equals(param.getCode()+"_WEIGHT")) {
					indexWeight = params.getDefaultValue();
				}
			}
			index.setIndexWeight(indexWeight);//权重
			index.setIndexId(param.getId());//指标id
			index.setIndexCode(param.getCode());//指标编号
			index.setIndexCategory(name);//指标分类
		
			
			//index.setId(UUID.randomUUID().toString());//主键id
			index.setRatingStep(step);//设置步骤id
			if(codetype.equals("DL")) {
				index.setIndexType(RatingStepType.QUANTITATIVE);//指标类型定量
			}else if(codetype.equals("DX")) {
				index.setIndexType(RatingStepType.QUALITATIVE_EDIT);//指标类型定量
			}
			
			
			String indexname = param.getName().replace("输入值", "");
			if(!indexname.equals(name)) {
				level = "2";
				parentid=modelparentid;
			}
			index.setIndexName(indexname);//指标名称
			List<ParameterOption> options = param.getOptions();
			if("DX".equals(codetype)) {
				for (ParameterOption parameterOption : options) {
					if(parameterOption.getInputValue().equals(paramValue.get(param.getCode()))) {
						//index.setIndexValue();//指标的输入值
						index.setIndexConfigId(parameterOption.getValue());//指标选项映射得分
						
					}
				}
			}else {
				index.setIndexConfigId(paramValue.get(param.getCode()));//指标选项映射得分
			}
			index.setIndexValue(paramValue.get(param.getCode()));//指标的输入值
			
			index.setLevel(level);//指标等级
			index.setParentId(parentid);//指标的parentid
			index.setIndexScore(new BigDecimal(modelGetMap.get(param.getCode()+"_RESULT")));//指标得分
			index.setFdmodel(type);
//			index.setCreator("system");
//			index.setLastModifier("system");
//			index.setCreateDate(new Date());
//			index.setLastModifyDate(new Date());
			StringBuffer dataStr = new StringBuffer("{");
			if("DX".equals(codetype)) {
				for (ParameterOption po : param.getOptions()) {
					if (paramValue.get(param.getCode()).equals(po.getInputValue())) {
						index.setQuOptions(po.getInputValue() + "." + po.getTitle());
					}
					dataStr.append("\"" + po.getInputValue() + "\"");
					dataStr.append(":");
					dataStr.append("\"" + po.getTitle() + "\"");
					dataStr.append(",");
				}
				String data = dataStr.toString();
				data = data.substring(0, data.length() - 1) + "}";
				// 定性文本
				index.setDxText(data.replace("\n", ""));
			}
			assetsIndexService.add(index);
		}
		
	}
	
	
	
	
	/**
	 * 将资产相关模型的指标存入ns_assets_report表中，该方法每个模型只需要调用一次就可以。
	 * @param subModels  模型的submodel
	 * @param paramValue  传入模型的参数
	 * @param type		模型类型
	 */
	public void insertReport(List<Model> subModels,Map<String, String> paramValue,String type) {
		for (Model model2 : subModels) {
			List<Parameter> allParameters = model2.getAllParameters();
			ModelCategory modelcategory = model2.getCategory();
			String category = modelcategory.toString();
			String categorytype = "QUALITATIVE_EDIT";
			String name = model2.getName();
			String level = "1";
			if(!"QUALITATIVE".equals(category)) {//如果不是定性，全部按照定量处理 QUALITATIVE
				categorytype = "QUANTITATIVE";
				
				for(Parameter param : allParameters) {
					if(!StringUtils.isEmpty(param.getCode()) && param.getType().toString().equals("IN")) {
						AssetsIndex index = new AssetsIndex();
						index.setId(UUID.randomUUID().toString());//主键id
						index.setIndexType(RatingStepType.QUANTITATIVE);//指标类型定量
						index.setIndexCategory(name);//指标分类
						String indexname = param.getName().replace("输入值", "");
						if(!indexname.equals(name)) {
							level = "2";
						}
						index.setIndexName(indexname);//指标名称
						index.setIndexId(param.getId());//指标id
						index.setIndexCode(param.getCode());//指标编号
						index.setLevel(level);
						index.setCreator("system");
						index.setLastModifier("system");
						index.setCreateDate(new Date());
						index.setLastModifyDate(new Date());
						String sql="insert into ns_assets_report (FD_ID,FD_INDEXTYPE,FD_INDEXCATEGORY,FD_INDEXNAME,FD_INDEXID,FD_INDEXCODE,FD_LEVEL,FD_CREATOR,FD_LAST_MODIFIER,FD_CREATE_DATE,FD_LAST_MODIFYDATE,FD_MODEL)"
								+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
						String uuid = UUID.randomUUID().toString();
						jdbc.update(sql,index.getId(),index.getIndexType().toString(),index.getIndexCategory(),index.getIndexName(),index.getIndexId(),index.getIndexCode(),index.getLevel(),
								index.getCreator(),index.getLastModifier(),index.getCreateDate(),index.getLastModifyDate(),type);
						
						//ratingIndexService.add(index);
					}
				}
				 
				
			}else {//如果是定性
				categorytype = "QUALITATIVE_EDIT";
				for (Parameter param : allParameters) {
					if (!StringUtils.isEmpty(param.getCode()) && param.getType().toString().equals("IN_OPTION")) {
						System.out.println("~~~~~~kankanparam:"+JSONObject.toJSON(param));
						AssetsIndex index = new AssetsIndex();
						index.setId(UUID.randomUUID().toString());//主键id
						index.setIndexType(RatingStepType.QUALITATIVE_EDIT);//指标类型定量
						index.setIndexCategory(name);//指标分类
						String indexname = param.getName().replace("输入值", "");
						if(!indexname.equals(name)) {
							level = "2";
						}
						index.setIndexName(indexname);//指标名称
						index.setIndexId(param.getId());//指标id
						index.setIndexCode(param.getCode());//指标编号
						index.setLevel(level);
						index.setCreator("system");
						index.setLastModifier("system");
						index.setCreateDate(new Date());
						index.setLastModifyDate(new Date());
						StringBuffer dataStr = new StringBuffer("{");
						for (ParameterOption po : param.getOptions()) {
							
							
							if (paramValue.get(param.getCode()).equals(po.getInputValue())) {
								index.setQuOptions(po.getInputValue() + "." + po.getTitle());
								
							}
							dataStr.append("\"" + po.getInputValue() + "\"");
							dataStr.append(":");
							dataStr.append("\"" + po.getTitle() + "\"");
							dataStr.append(",");
						}
						String data = dataStr.toString();
						data = data.substring(0, data.length() - 1) + "}";
						// 定性文本
						index.setDxText(data.replace("\n", ""));
						String sql="insert into ns_assets_report (FD_ID,FD_INDEXTYPE,FD_INDEXCATEGORY,FD_INDEXNAME,FD_INDEXID,FD_INDEXCODE,FD_LEVEL,"
								+ "FD_DX_TEXT,FD_QUALITATIVE_OPTIONS,FD_CREATOR,FD_LAST_MODIFIER,FD_CREATE_DATE,FD_LAST_MODIFYDATE,FD_MODEL)"
								+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						String uuid = UUID.randomUUID().toString();
						jdbc.update(sql,index.getId(),index.getIndexType().toString(),index.getIndexCategory(),index.getIndexName(),index.getIndexId(),index.getIndexCode(),index.getLevel(),
								index.getDxText(),index.getQuOptions(),index.getCreator(),index.getLastModifier(),index.getCreateDate(),index.getLastModifyDate(),type);
					}
				}
			}
			
		}
	}	
	
	
	
	

	@Override
	@Transactional
	public String createAssetsRating(String bpCode,String tempType,String version,String proCode,String ratingid) throws Exception {
		String sqlCust = "select fd_bp_code,fd_bp_name,FD_SCORE_TEMPLATE_ID from NS_BP_MASTER where fd_bp_code=?";
		List<Map<String,Object>> map = jdbc.queryForList(sqlCust,bpCode);
		String code = map.get(0).get("FD_BP_CODE").toString();
		String name = map.get(0).get("FD_BP_NAME").toString();
		
		String prosqlCust = "select PROJECT_NUMBER,PROJECT_NAME,nvl(FINANCE_AMOUNT,'0')-nvl(margin,'0') DL_FXCK from NS_PRJ_PROJECT where PROJECT_NUMBER=?";
		List<Map<String,Object>> proMap = jdbc.queryForList(prosqlCust,proCode);
		String procode = proMap.get(0).get("PROJECT_NUMBER").toString();
		String proName = proMap.get(0).get("PROJECT_NAME").toString();
		String TemplateId="";
		if(map.get(0).get("FD_SCORE_TEMPLATE_ID")!=null) {
			if(!tempType.equals(map.get(0).get("FD_SCORE_TEMPLATE_ID").toString())) {
				TemplateId=tempType;
			}else {
				TemplateId=map.get(0).get("FD_SCORE_TEMPLATE_ID").toString();
			}
		}else {
			TemplateId=tempType;
		}
		version="3.0";
		String sql="insert into ns_assets_rating (FD_ID,FD_PROJECT_CODE,FD_PROJECT_NAME,FD_CUST_CODE,FD_CUST_NAME,PRODUCT_TYPE,fd_version)values(?,?,?,?,?,?,?)";
		//String uuid = UUID.randomUUID().toString();
		String uuid = ratingid;
		jdbc.update(sql,uuid,procode,proName,code,name,TemplateId,version);
		List<RatingAssetsStep> ratingSteps = new ArrayList<RatingAssetsStep>();
		AssetsRating assetsRating = new AssetsRating();
		assetsRating=repository.findOne(uuid);
		List<Map<String, Object>> list = jdbc.queryForList("select * from  NS_R_CFG_STEPS  where FD_RATINGCFG_ID='1'  order by fd_stepnum asc");
		for(int i=0;i<list.size();i++) {
			//如果获取的 评级步骤是 “定量分析-QUANTITATIVE” 或者评级步骤是 “定性指标录入-QUALITATIVE_EDIT”	
			if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())
					|| RatingStepType.QUALITATIVE_EDIT.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
				RatingAssetsStep step = new RatingAssetsStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(assetsRating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");
				// 持久化步骤实例
				step = ratingAssetsStepService.add(step);
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
			} else {
				RatingAssetsStep step = new RatingAssetsStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(assetsRating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("4");
				// 持久化步骤实例
				step = ratingAssetsStepService.add(step);
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
			}
		}
		assetsRating.setCurrentStepId(ratingSteps.get(0).getId());
		assetsRating.setCurrentStep(ratingSteps.get(0));
//		if(version.equals("2.0")) {
//			tempType=tempType+"_2";
//		}
		for (RatingAssetsStep RatingMainStep : ratingSteps) {
			/*这块只会保存定性、定量的数据，因为在fd_step_type表中有三种模板类型分别是（REPORT_INFO、QUANTITATIVE、QUALITATIVE_EDIT）
			  但是在ns_main_report中并没有 “REPORT_INFO” 的模板类型，所以只会保存前两步的数据，第三步的数据将无法保存
			 * */
			RatingMainStep.getStepType();
			String sqlIndex = "update ns_assets_report set FD_STEPID=?,FD_INDEXVALUE=? where fd_INDEXTYPE=? and FD_MODEL=?";
			jdbc.update(sqlIndex,RatingMainStep.getId(),"",RatingMainStep.getStepType().toString(),tempType);
		}
		
		List<Map<String,Object>> listMap =jdbc.queryForList("select * from ns_assets_report where FD_MODEL=?",tempType);
		for (Map<String, Object> map2 : listMap) {
			String indexValue="";
			if("QUANTITATIVE".equals(map2.get("FD_INDEXTYPE").toString())) {
				if("DL_HXZLWJZ".equals(map2.get("FD_INDEXCODE").toString())) {
					//ratingid
					String hxzlwjzSql = "select sum(lease_value) hxzlwjz from NS_LEASEITEM_LIST where assets_rating_id=? and iscorelease='Y' " ; 
					String hxzlwjz = jdbc.queryForObject(hxzlwjzSql, String.class,ratingid);
					indexValue=hxzlwjz;
				}
				if("DL_FXCK".equals(map2.get("FD_INDEXCODE").toString())) {
					indexValue=proMap.get(0).get("DL_FXCK").toString(); 
				}
			}
			String sqlIndex = "update ns_assets_report set FD_ID=?,FD_INDEXVALUE=? where fd_ID=? and FD_MODEL=?";
			jdbc.update(sqlIndex,UUID.randomUUID().toString(),indexValue,map2.get("FD_ID").toString(),tempType);
		}
		repository.flush();
		String insertIndex = "insert into ns_assets_indexes value select * from ns_assets_report where FD_MODEL=?";
		jdbc.update(insertIndex,tempType);
		
		return assetsRating.getId();
	}

	
	@Override
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			AssetsRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("highprecision"), "FD_HIGH_PRECISION"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("enterprisehonor"), "FD_ENTERPRISE_HONOR"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("economic"), "FD_ECONOMIC_TYPE"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		// ---
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("custName"), "FD_BP_NAME"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("productType"), "rating.PRODUCT_TYPE"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("custCode"), "FD_BP_CODE"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("scoreTemplateId"), "FD_SCORE_TEMPLATE_ID"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("entryName"), "PROJECT_NAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("entryCode"), "PROJECT_NUMBER"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("employee"), "FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("finalName"), "FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("assetreview"), "FD_ASSETS_NAME"));// 资产经理-----

		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("leaseOrganization"), "org.fd_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType1"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("project"), "FD_PROJECT_NAME"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("documentType"), "document_type"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("vaild"), "FD_VAILD"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("proCode"), "FD_PROJECT_CODE"));// 业务部
		sqlQuery.append(CommonUtils.sqlNotPar(request.getParameter("ratingStatus"), "fd_rating_status"));// 评级状态
		String str="";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("vailds"))) {
			 str =" and rating.fd_vaild<>'0' ";
		}
		List<String> list =CommonUtils.getAllRoleIds();
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " master.FD_EMPLOYEE_ID"));// 业务部
		}
		//FD_ASSETS_NAME  fd_asset_review
		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "FD_FINAL_DATE"));
		
		String sql = "select \r\n" + "master.FD_BP_NAME as custName,\r\n" + "master.FD_BP_CODE as custCode,\r\n"
				+ "pro.project_number as proCode,\r\n" + "pro.project_name as proName,pro.lease_item_short_name as proItemName,\r\n"
				+ "master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" + "FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n"
				+ "FD_HIGH_PRECISION as highPrecision,\r\n" + "FD_ENTERPRISE_HONOR as enterpriseHonor,\r\n"
				+ "FD_ECONOMIC_TYPE as economic,\r\n" + "rating.FD_ID as id,\r\n" + "rating.product_type as type,\r\n"
				+ "master.FD_EMPLOYEE_ID as employeeId,FD_INTERN_LEVEL as internLevel,org.fd_name as orgName, fd_pd as internBs,\r\n" + "FD_FINAL_LEVEL as finalLevel,\r\n"
				+ "FD_final_pd as finalBs,\r\n" + "FD_RATING_STATUS as ratingStatus,\r\n"
				+ "FD_INTERN_NAME as internName," + "FD_FINAL_NAME as finalName,"+ "FD_ASSETS_NAME as assetReview," 
				+ "to_char(FD_FINAL_DATE,'yyyy-MM-dd') as finalDate\r\n" + "from   NS_PRJ_PROJECT pro   \r\n"
				+ "left join (select * from ns_assets_rating where fd_vaild='1') rating on pro.PROJECT_NUMBER = rating.FD_PROJECT_CODE  \r\n"+str
				+ "left join NS_BP_MASTER master on to_char(pro.bp_id_tenant) = to_char(master.fd_ID) \r\n"
				+ "left join FR_AA_USER_PARTITION init on rating.FD_INTERN_CODE = init.employee_id  "
				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id " 
				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
				+ "where  1=1  "
				+ sqlQuery + " order by rating.FD_FINAL_DATE desc nulls last ";
		System.out.println("~~~sql:"+sql);
		return getResult(sql, page, rows);
	}
	

	@Override
	public ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			AssetsRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("highprecision"), "FD_HIGH_PRECISION"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("enterprisehonor"), "FD_ENTERPRISE_HONOR"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("economic"), "FD_ECONOMIC_TYPE"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		// ---
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("custName"), "FD_BP_NAME"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("custCode"), "FD_BP_CODE"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("entryCode"), "PROJECT_NUMBER"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("entryName"), "PROJECT_NAME"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("scoreTemplateId"), "FD_SCORE_TEMPLATE_ID"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("employee"), "FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("finalName"), "FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("assetreview"), "FD_ASSET_REVIEW"));// 资产经理

		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("leaseOrganization"), "org.fd_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("productType"), "rating.PRODUCT_TYPE"));// 产品类型
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType1"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "rating.fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("project"), "FD_PROJECT_NAME"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("documentType"), "document_type"));// 业务部
		//sqlQuery.append(CommonUtils.sqlPar(request.getParameter("vaild"), "FD_VAILD"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("proCode"), "rating.FD_PROJECT_CODE"));// 业务部
		sqlQuery.append(CommonUtils.sqlNotPar(request.getParameter("ratingStatus"), "rating.fd_rating_status"));// 评级状态
		if(StringUtils.isEmpty(request.getParameter("fdVaild"))) {
			sqlQuery.append(CommonUtils.sqlPar("1", "rating.fd_vaild"));// 
		}
		if(!StringUtils.isEmpty(request.getParameter("finallevel"))) {
			sqlQuery.append(" and rating.FD_FINAL_LEVEL is not null ");
		}
		List<String> list =CommonUtils.getAllRoleIds();
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " master.FD_EMPLOYEE_ID"));// 业务部
		}
		
		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "rating.FD_FINAL_DATE"));
		
		String sql = "SELECT\r\n" + 
				"	rating.fd_vaild,\r\n" + 
				"	rating.fd_id as id,\r\n" + 
				"	master.FD_BP_NAME AS custname,\r\n" + 
				"	master.FD_BP_CODE AS custcode,\r\n" + 
				"	pro.project_number AS proCode,\r\n" + 
				"	pro.project_name AS proName,\r\n" + 
				"	org.fd_name AS orgName,\r\n" + 
				"	master.FD_EMPLOYEE_ID AS employeeId,\r\n" + 
				"	rating.FD_FINAL_NAME AS finalName,\r\n" + 
				"	rating.FD_ASSETS_NAME AS assetReview,\r\n" + 
				"	rating.FD_INTERN_LEVEL AS internlevel,\r\n" + 
				"	rating.FD_INTERN_SCO as initSco,\r\n" + 
				"	rating.FD_PD as internBs,\r\n" + 
				"	rating.FD_FINAL_LEVEL as finallevel,\r\n" + 
				"	rating.FD_FINAL_SCO as finaSco,\r\n" + 
				"	rating.FD_FINAL_PD as finalBs,\r\n" + 
				"	to_char( rating.FD_FINAL_DATE, 'yyyy-MM-dd' ) AS finalDate,\r\n" + 
				"	to_char( rating.FD_DATE, 'yyyy-MM-dd' ) as fDate,\r\n" + 
				"	rating.FD_RATING_STATUS AS ratingstatus,\r\n" + 
				"	rating.fd_version as version,\r\n" + 
				"	rating.PRODUCT_TYPE AS type\r\n" + 
				"FROM\r\n" + 
				"	NS_ASSETS_rating rating\r\n" + 
				"	LEFT JOIN NS_PRJ_PROJECT pro ON pro.PROJECT_NUMBER = rating.FD_PROJECT_CODE \r\n" + 
				"	LEFT JOIN NS_BP_MASTER master ON to_char( pro.bp_id_tenant ) = to_char( master.fd_ID )\r\n" + 
				"	LEFT JOIN FR_AA_USER_PARTITION init ON rating.FD_INTERN_CODE = init.employee_id\r\n" + 
				"	LEFT JOIN fr_aa_org org ON master.FD_LEASE_ORGANIZATION = org.fd_id\r\n" + 
				"	LEFT JOIN fr_aa_user fruser ON master.fd_employee_id = fruser.fd_id \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1  \r\n" + sqlQuery +
				" ORDER BY\r\n" + 
				"	rating.FD_FINAL_DATE DESC nulls last";
		System.out.println("sql:"+sql);
		return getResult(sql, page, rows);
	}

	
	/**
	 * 获取结果封装
	 *
	 * @param sql
	 * @param page
	 * @param rows
	 * @return
	 */
	private ResponseWrapper<RatingInit> getResult(String sql, Integer page, Integer rows) {
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		List<RatingInit> list = jdbc.query(page(start, end, sql),
				new BeanPropertyRowMapper<RatingInit>(RatingInit.class));
		ResponseWrapper<RatingInit> re = ResponseWrapperBuilder.query(list);
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}
	
	public String page(Integer start, Integer end, String sql) {
		sql = "select * from (select t.*,rownum as rn from (" + sql + ") t where rownum<=" + end + ") where rn>"
				+ start;
		return sql;
	}
	
	public Long count(String sql) {
		sql = "select count(*) from(" + sql + ")";
		Long count = jdbc.queryForObject(sql, Long.class);
		return count;
	}
	

}
