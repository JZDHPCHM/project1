package gbicc.irs.rti.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.assetsRating.entity.AssetsRateItems;
import gbicc.irs.assetsRating.entity.AssetsRating;
import gbicc.irs.assetsRating.repository.AssetsRatingRepository;
import gbicc.irs.assetsRating.service.AssetsRatingService;
import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.debtRating.debt.entity.NsPrjProject;
import gbicc.irs.debtRating.debt.service.impl.NsPrjProjectServiceImpl;
import gbicc.irs.main.rating.utils.Constats;
import gbicc.irs.main.rating.utils.Tools;
import gbicc.irs.rti.service.RTIAssetsRatingService;

/**
 * @author hzw
 *	资产评级
 */
@Service("rTIAssetsRatingService")
public class RTIAssetsRatingServiceImpl extends DaoServiceImpl<AssetsRating, String, AssetsRatingRepository>
		implements RTIAssetsRatingService {
	
	private static Log log = LogFactory.getLog(RTIAssetsRatingServiceImpl.class);
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private AssetsRatingService assetsRatingService;


	/**
	 * 获取数据库连接
	 * @return
	 * @throws SQLException
	 */
	public Connection achieveConnection() throws SQLException{
		Connection conn =  jdbc.getDataSource().getConnection();
		return conn;
	}
	
	/**
	 * 选择模型类型
	 * @param _productType
	 * @return 模型类型
	 */
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
	
	/**
	 * 资产评级信息解析并封装
	 * @param assetsRatingMap
	 * @param nsPrjProject
	 * @return	AssetsRating
	 */
	public AssetsRating setAssetsRating(AssetsRating assetsRating,Map<String,Object> assetsRatingMap,NsPrjProject nsPrjProject,NsBpMaster nsBpMaster) {
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
		assetsRating.setFinalDate("020".equals(assetsRating.getRatingStatus())?new Date():null);
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
	
	/**
	 * 检验资产评级指标是否有更新
	 * @param projectNum 资产编号
	 * @return	false 已更新		true 无更新
	 */
	public boolean checkChanged(String id,String _projetNum,List<Map<String, Object>> newItems) {
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


	
	/**
	 * 解析报文
	 * @param map报文
	 * @return 处理结果
	 */
	@Override
	public Map<String, String> parsingData(Map<String, Object> map){
		Map<String, String> result = new HashMap<String, String>();
		result.put("PROJECT_NUMBER", null);
		result.put("PROJECT_NAME",null);
		result.put("ASSETS_LEVEL", null);
		result.put("REPORTURL", null);
		result.put("RET_STATUS", Constats.FAIL_STATUS);
		result.put("RET_CODE", Constats.FAIL_CODE);
		result.put("MSG", "评级失败!");
		Connection conn	= null;
		Statement stmt 	= null;
		List<String> sqls = new ArrayList<String>();
		try {
			conn = achieveConnection(); 
			Map<String,Object> projectInfo = JSONObject.parseObject(map.get("projectInfo").toString());
			Map<String,Object> assetsRatingInfo = JSONObject.parseObject(map.get("assetsRatingInfo").toString());
			result.put("PROJECT_NUMBER", projectInfo.get("PROJECT_NUMBER")==null?null:projectInfo.get("PROJECT_NUMBER").toString().trim());
			result.put("PROJECT_NAME",projectInfo.get("PROJECT_NAME")==null?null:projectInfo.get("PROJECT_NAME").toString().trim());
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
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：资产复评开始："+nsPrjProject.getProjectNumber());
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
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：无初评记录，无法复评!"+nsPrjProject.getProjectNumber());
						result.put("MSG", "无初评记录，无法复评!");
						return result;
					}
				}
				assetsRating = setAssetsRating(assetsRatingService.getRepository().findOne(id),assetsRatingInfo,nsPrjProject,nsBpMaster);
				_finalDate = Tools.formatDate(assetsRating.getFinalDate(),"yyyy-MM-dd hh:mm:ss");
				_fDDate = Tools.formatDate(assetsRating.getFdDate(),"yyyy-MM-dd hh:mm:ss");
				//核心租赁物占比0.6
				if(Double.parseDouble(assetsRating.getCoreleaseProportion()) < Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：核心租赁物占比<0.6  ："+nsPrjProject.getProjectNumber());
					sqls.add("UPDATE NS_ASSETS_RATING SET FD_DATE=TO_DATE('"+_fDDate+"','YYYY-MM-DD hh:mi:ss')"
							+ ",FD_FINAL_DATE=TO_DATE('"+_finalDate+"','YYYY-MM-DD hh:mi:ss')"
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
				
				}else {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：核心租赁物占比>0.6  ："+nsPrjProject.getProjectNumber());
					sqls.add("UPDATE NS_ASSETS_RATING SET FD_DATE=TO_DATE('"+_fDDate+"','YYYY-MM-DD hh:mi:ss')"
							+ ",FD_FINAL_DATE=TO_DATE('"+_finalDate+"','YYYY-MM-DD hh:mi:ss')"
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
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：资产初评开始："+nsPrjProject.getProjectNumber());
				String queryStatementF = "SELECT COUNT(*) FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='010'";
				Integer _count = jdbc.queryForObject(queryStatementF, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					sqls.add("UPDATE NS_ASSETS_STEP SET FD_VAILD='0' WHERE FD_RATEID='"+id+"'");
//					sqls.add("UPDATE NS_ASSETS_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE FD_ID='"+id+"'");
				}
				//复评
//				String queryStatementS = "SELECT COUNT(*) FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='020'";
//				_count = jdbc.queryForObject(queryStatementS, Integer.class, nsPrjProject.getProjectNumber());
//				if (_count > 0) {
//					String sql = "SELECT FD_ID FROM  NS_ASSETS_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
//					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
//					sqls.add("UPDATE NS_ASSETS_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE FD_ID='"+id+"'");
//				}
				sqls.add("UPDATE NS_ASSETS_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE  FD_VERSION='3.0' AND FD_RATING_VAILD='1' and FD_VAILD='1'  AND  FD_PROJECT_CODE='"+nsPrjProject.getProjectNumber()+"' ");
				assetsRating.setId(UUID.randomUUID().toString());
				assetsRating = setAssetsRating(assetsRating,assetsRatingInfo,nsPrjProject,nsBpMaster);
				_internDate = Tools.formatDate(assetsRating.getInternDate(),"yyyy-MM-dd hh:mm:ss");
				//核心租赁物占比0.6
				if(Double.parseDouble(assetsRating.getCoreleaseProportion()) < Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：核心租赁物占比<0.6 ： "+nsPrjProject.getProjectNumber());
					sqls.add("INSERT INTO NS_ASSETS_RATING (FD_ID,FD_CUST_CODE,FD_CUST_NAME,FD_PROJECT_CODE,FD_PROJECT_NAME,PRODUCT_TYPE,LEASE_START_DATE,LEASE_TERM,FINANCE_AMOUNT"
							+ ",MARGIN,LEASE_ITEM_SHORT_NAME,ISCORELEASE,CORELEASE_PROPORTION,ORIGINAL_VALUE,NET_VALUE,ASSESSED_VALUE,ASSESSMENT_METHODS,FD_INTERN_CODE,FD_INTERN_NAME"
							+ ",FD_INTERN_DATE,FD_ASSETS_CODE,FD_ASSETS_NAME,MAJOR_RISK_WARNING,FD_RATING_STATUS"
							+ ",FD_VAILD,FD_RATING_VAILD,FD_VERSION,FD_CREATE_DATE,FD_PD,FD_INTERN_SCO,FD_INTERN_LEVEL,FD_CREATOR) values("
							+ "'"+assetsRating.getId()+"','"+assetsRating.getCustCode()+"','"+assetsRating.getCustName()+"','"+assetsRating.getProjectCode()+"','"+assetsRating.getProjectName()+"'"
							+ ",'"+assetsRating.getType()+"','"+assetsRating.getStartDate()+"','"+assetsRating.getLeaseTerm()+"','"+assetsRating.getFinanceAmount()+"'"
							+ ",'"+assetsRating.getMargin()+"','"+assetsRating.getLeaseame()+"','"+assetsRating.getIsCorelease()+"','"+assetsRating.getCoreleaseProportion()+"','"+assetsRating.getOriginalValue()+"'"
							+ ",'"+assetsRating.getNetValue()+"','"+assetsRating.getAssessedValue()+"','"+assetsRating.getAssessmentMethods()+"','"+assetsRating.getInternCode()+"','"+assetsRating.getInternName()+"'"
							+ ",to_date('"+_internDate+"','yyyy-MM-dd hh:mi:ss'),'"+assetsRating.getAssetsCode()+"','"+assetsRating.getAssetsName()+"','"+assetsRating.getMajorWarning()+"','"+assetsRating.getRatingStatus()+"','"+assetsRating.getVaild()+"','"+assetsRating.getRatingVaild()+"','"+assetsRating.getFdVersion()+"'"
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
							+ ",to_date('"+_internDate+"','yyyy-MM-dd hh:mi:ss'),'"+assetsRating.getAssetsCode()+"','"+assetsRating.getAssetsName()+"','"+assetsRating.getMajorWarning()+"','"+assetsRating.getRatingStatus()+"','"+assetsRating.getVaild()+"','"+assetsRating.getRatingVaild()+"','"+assetsRating.getFdVersion()+"'"
							+ ",to_date('"+currentDate+"','YYYY-MM-DD hh:mi:ss'),'"+assetsRating.getAssetsName()+"')");
				}
			}
			
			List<Map<String, Object>> lsitRateItems = (List<Map<String, Object>>) map.get("assetsInfo");
			/*if (checkChanged(assetsRating.getId(),nsPrjProject.getProjectNumber(),lsitRateItems)) {
				log.info("此次指标无更新，请更新指标后再次发起评级!");
				result.put("MSG", "重复评级，请更新指标后再次发起评级!");
				return result;
			}*/
			//解析资产评级指标数据
			sqls.add("UPDATE NS_ASSETS_RATE_ITEMS SET FD_VAILD='0' WHERE FD_PROJECT_NUMBER='"+assetsRating.getProjectCode()+"' AND FD_ASSETS_RATE_ID='"+assetsRating.getId()+"'");
			Map<String,String> assetsRateItemsMap = null;//封装模型计算所需数据
			//核心租赁物占比0.6
			if(Double.parseDouble(assetsRating.getCoreleaseProportion()) >= Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
				if(lsitRateItems==null||lsitRateItems.size()<1) {
					result.put("MSG", "无资产评级所需指标");
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")评级失败，核心租赁物占>=0.6，无资产评级所需指标");
					return result;
				}
				assetsRateItemsMap = new HashMap<String,String>();
				String uuidAssetsRateItems = "";
				for (int i = 0; i < lsitRateItems.size(); i++) {
					if(StringUtils.isEmpty(lsitRateItems.get(i).get("CODE"))||StringUtils.isEmpty(lsitRateItems.get(i).get("CODE").toString().trim())||StringUtils.isEmpty(lsitRateItems.get(i).get("VALUE"))||StringUtils.isEmpty(lsitRateItems.get(i).get("VALUE").toString().trim())) {
						result.put("MSG", "资产评级所需指标中第"+(i+1)+"个指标数据异常");
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")评级失败，资产评级所需指标中第"+(i+1)+"个指标数据异常");
						return result;
					}
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
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+nsPrjProject.getProjectNumber()+"）资产评级实时接口处理sqls : ");
			for(String sqlStr : sqls){
				log.info(sqlStr);
				stmt.addBatch(sqlStr);
			}
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")start execute sqlBatch ---------------------------");
			stmt.executeBatch();
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")end execute sqlBatch ---------------------------");
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")execute sqls : " + sqls.size()+" execute time (ms) "+(System.currentTimeMillis() - insertStart/1000));
			//核心租赁物占比0.6
			if(Double.parseDouble(assetsRating.getCoreleaseProportion()) >= Double.parseDouble(Constats.STR_ASSETS_CORELEASE_PROPORTION)) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：资产评级模型计算开始！"+nsPrjProject.getProjectNumber());
				assetsRating = assetsRatingService.testmodel(assetsRateItemsMap,assetsRating.getType(),assetsRating,assetsRating.getRatingStatus());
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：资产评级模型计算结束！"+nsPrjProject.getProjectNumber());
				if (assetsRating == null) {
					result.put("MSG", "指标缺失或格式错误，计算失败！");
					conn.rollback();
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"（"+nsPrjProject.getProjectNumber()+"）评级处理失败，批处理事务回滚！");
					return result;
				}else {
					String updateAssetsRatingSQL = "UPDATE NS_ASSETS_RATING SET FD_CURRENT_STEPID=decode('"+assetsRating.getCurrentStepId()+"','null',null,'"+assetsRating.getCurrentStepId()+"'),"
							+ "FD_RATING_STATUS='"+assetsRating.getRatingStatus()+"',FD_VAILD='1',FD_FINAL_SCO=decode('"+assetsRating.getFinalSco()+"','null',null,'"+assetsRating.getFinalSco()+"'),"
							+ "FD_PD=decode('"+assetsRating.getPd()+"','null',null,'"+assetsRating.getPd()+"'),FD_FINAL_PD=decode('"+assetsRating.getFinalPd()+"','null',null,'"+assetsRating.getFinalPd()+"'),FD_INTERN_LEVEL=decode('"+assetsRating.getInternLevel()+"','null',null,'"+assetsRating.getInternLevel()+"'),"
							+ "FD_INTERN_SCO=decode('"+assetsRating.getInternSco()+"','null',null,'"+assetsRating.getInternSco()+"'),FD_FINAL_LEVEL=decode('"+assetsRating.getFinalLevel()+"','null',null,'"+assetsRating.getFinalLevel()+"') "
									+ "where FD_ID='"+assetsRating.getId()+"'";
					stmt.execute(updateAssetsRatingSQL);
					conn.commit();
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")batch transaction commit ---------------------------");
				}
			}
			result.put("PROJECT_NUMBER", assetsRating.getProjectCode());
			result.put("PROJECT_NAME",assetsRating.getProjectName());
			if ("010".equals(assetsRating.getRatingStatus())) {
				result.put("ASSETS_LEVEL", assetsRating.getInternLevel());
			}else {
				result.put("ASSETS_LEVEL", assetsRating.getFinalLevel());
				if(new BigDecimal(0).compareTo(assetsRating.getFinalPd()==null?new BigDecimal(0):assetsRating.getFinalPd())!=0) {
					result.put("REPORTURL", "/irs/assetsRating/assetsRatingReport?custNo="+assetsRating.getId());
				}
			}
			result.put("RET_STATUS", Constats.SUCCESS_STATUS);
			result.put("RET_CODE", Constats.SUCCESS_CODE);
			result.put("MSG", "成功");
			conn.commit();
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")batch transaction commit ---------------------------");
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：资产评级结束！"+nsPrjProject.getProjectNumber());
			return result;
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" : ",e);
			e.printStackTrace();
			if(conn != null) {
				try {
					conn.rollback();
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：batch transaction rollback ---------------------------");
				} catch (SQLException e1) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：",e);
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
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：",e);
					e.printStackTrace();
					return result;
				}
			}
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：",e);
					e.printStackTrace();
					return result;
				}
			}
		}
		
	}
	
	
}
