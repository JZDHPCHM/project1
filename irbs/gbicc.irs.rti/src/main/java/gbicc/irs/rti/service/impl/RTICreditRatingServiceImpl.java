package gbicc.irs.rti.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.assetsRating.entity.AssetsRating;
import gbicc.irs.assetsRating.service.AssetsRatingService;
import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.debtRating.debt.entity.CreditRating;
import gbicc.irs.debtRating.debt.entity.FacilityRating;
import gbicc.irs.debtRating.debt.entity.NsPrjProject;
import gbicc.irs.debtRating.debt.repository.CreditRatingRepository;
import gbicc.irs.debtRating.debt.service.CreditRatingService;
import gbicc.irs.debtRating.debt.service.FacilityRatingService;
import gbicc.irs.debtRating.debt.service.impl.NsPrjProjectServiceImpl;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.service.MainRatingService;
import gbicc.irs.main.rating.utils.Constats;
import gbicc.irs.main.rating.utils.Tools;
import gbicc.irs.rti.service.RTICreditRatingService;

/**
 * @author hzw
 *	债项评级3.0
 */
@Service("rTICreditRatingServiceImpl")
public class RTICreditRatingServiceImpl extends DaoServiceImpl<CreditRating, String, CreditRatingRepository>
		implements RTICreditRatingService {
	
	private static Log log = LogFactory.getLog(RTICreditRatingServiceImpl.class);
	@Autowired
	private NsPrjProjectServiceImpl nsPrjProjectService;
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private CreditRatingService creditRatingService;
	@Autowired
	private FacilityRatingService facilityRatingServiceImpl;
	@Autowired
	private MainRatingService mainRatingService;
	@Autowired
	private AssetsRatingService assetsRatingService;

	
	/**
	 * 增信评级信息解析并封装
	 * @param creditRatingMap
	 * @param nsPrjProject
	 * @return	CreditRating
	 */
	public CreditRating setCreditRating(CreditRating creditRating,Map<String,Object> creditRatingMap,NsPrjProject nsPrjProject,NsBpMaster nsBpMaster) {
		try {
			creditRating.setAssetsCode(creditRatingMap.get("FD_ASSETS_CODE")==null?"":creditRatingMap.get("FD_ASSETS_CODE").toString().trim());
			creditRating.setAssetsName(creditRatingMap.get("FD_ASSETS_NAME")==null?"":creditRatingMap.get("FD_ASSETS_NAME").toString().trim());
			creditRating.setCustCode(nsBpMaster.getBpCode());//承租人编号
			creditRating.setCustName(nsBpMaster.getBpName());//承租人名称
			creditRating.setFdVersion(creditRatingMap.get("FD_VERSION")==null?"":creditRatingMap.get("FD_VERSION").toString().trim());
			creditRating.setProjectCode(nsPrjProject.getProjectNumber());
			creditRating.setProjectName(nsPrjProject.getProjectName());
			creditRating.setRatingStatus(creditRatingMap.get("FD_RATING_STATUS")==null?"":creditRatingMap.get("FD_RATING_STATUS").toString().trim());
			creditRating.setRatingType(creditRatingMap.get("FD_RATING_TYPE")==null?"":creditRatingMap.get("FD_RATING_TYPE").toString().trim());
			creditRating.setType(Constats.STR_ZX_M);//
			creditRating.setRatingVaild("1");
			creditRating.setVaild("1");
			creditRating.setCreditType(nsPrjProject.getCreditType()==null?"":nsPrjProject.getCreditType().trim());
			if("010".equals(creditRating.getRatingStatus())) {
				creditRating.setInternDate(new Date());
				creditRating.setCreator(creditRating.getInternName());
				creditRating.setCreateDate(new Date());
				creditRating.setInternCode(creditRatingMap.get("FD_INTERN_CODE")==null?"":creditRatingMap.get("FD_INTERN_CODE").toString().trim());
				creditRating.setInternName(creditRatingMap.get("FD_INTERN_NAME")==null?"":creditRatingMap.get("FD_INTERN_NAME").toString().trim());
//				if(flag==false) {//实时接口无增信数据
//					creditRating.setInternLevel("Ⅳ");
//					creditRating.setInternSco(new BigDecimal(0));
//					creditRating.setPd(new BigDecimal(0));
//				}
			}else {
				creditRating.setFinalDate(new Date());
				creditRating.setFdDate(Tools.addYear(new Date(), 1));
				creditRating.setInternLevel(creditRating.getInternLevel()!=null?creditRating.getInternLevel():null);
				creditRating.setInternSco(creditRating.getInternSco()!=null?creditRating.getInternSco():null);
				creditRating.setPd(creditRating.getPd()!=null?creditRating.getPd():null);//资产保障倍数
				creditRating.setLastModifier(creditRating.getFinalName());
				creditRating.setLastModifyDate(new Date());
				creditRating.setCreateDate(creditRating.getCreateDate()!=null?creditRating.getCreateDate():null);
				creditRating.setFinalAdvice(creditRatingMap.get("FD_FINAL_ADVICE")==null?"":creditRatingMap.get("FD_FINAL_ADVICE").toString().trim());
				creditRating.setFinalCode(creditRatingMap.get("FD_FINAL_CODE")==null?creditRating.getFinalCode():creditRatingMap.get("FD_FINAL_CODE").toString().trim());
				creditRating.setFinalName(creditRatingMap.get("FD_FINAL_NAME")==null?creditRating.getFinalName():creditRatingMap.get("FD_FINAL_NAME").toString().trim());
//				if(flag==false) {//实时接口无增信数据
//					creditRating.setFinalLevel("Ⅳ");
//					creditRating.setFinalPd(new BigDecimal(0));//复评资产保障倍数
//					creditRating.setFinalSco(new BigDecimal(0));//复评分数
//				}
			}
		}catch(Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")更新增信评级信息失败！",e);
			throw e;
		}
		return creditRating;
	}
	
	
	public FacilityRating setFacilityRating(FacilityRating facilityRating,Map<String,Object> facilityRatingMap,NsPrjProject nsPrjProject,NsBpMaster nsBpMaster) {
		try {
	//		facilityRating.setAssetsId(assetsId);
	//		facilityRating.setMainId(mainId);
	//		facilityRating.setCreditId(creditId);
			facilityRating.setRatingStatus(facilityRatingMap.get("FD_RATING_STATUS")==null?"":facilityRatingMap.get("FD_RATING_STATUS").toString().trim());
			facilityRating.setAssetsCode(facilityRatingMap.get("FD_ASSETS_CODE")==null?"":facilityRatingMap.get("FD_ASSETS_CODE").toString().trim());
			facilityRating.setAssetsName(facilityRatingMap.get("FD_ASSETS_NAME")==null?"":facilityRatingMap.get("FD_ASSETS_NAME").toString().trim());
			facilityRating.setCustCode(nsBpMaster.getBpCode());//承租人编号
			facilityRating.setCustName(nsBpMaster.getBpName());//承租人名称
			facilityRating.setFdVersion(facilityRatingMap.get("FD_VERSION")==null?"3.0":facilityRatingMap.get("FD_VERSION").toString().trim());
			facilityRating.setProjectCode(nsPrjProject.getProjectNumber());
			facilityRating.setProjectName(nsPrjProject.getProjectName());
			facilityRating.setRatingType(facilityRatingMap.get("FD_RATING_TYPE")==null?"":facilityRatingMap.get("FD_RATING_TYPE").toString().trim());
			facilityRating.setRatingVaild("1");
			facilityRating.setVaild("1");
			if("010".equals(facilityRating.getRatingStatus())) {
				facilityRating.setInternDate(new Date());
				facilityRating.setCreator(facilityRating.getInternName());//创建人
				facilityRating.setCreateDate(new Date());
				facilityRating.setInternCode(facilityRatingMap.get("FD_INTERN_CODE")==null?"":facilityRatingMap.get("FD_INTERN_CODE").toString().trim());
		//		facilityRating.setInternLevel(internLevel);
				facilityRating.setInternName(facilityRatingMap.get("FD_INTERN_NAME")==null?"":facilityRatingMap.get("FD_INTERN_NAME").toString().trim());
		//		facilityRating.setInternSco(internSco);//评级资产得分
		//		facilityRating.setPd(pd);//资产保障倍数
			}else {
				facilityRating.setFinalName(facilityRatingMap.get("FD_FINAL_NAME")==null?"":facilityRatingMap.get("FD_FINAL_NAME").toString().trim());
				facilityRating.setFinalAdvice(facilityRatingMap.get("FD_FINAL_ADVICE")==null?"":facilityRatingMap.get("FD_FINAL_ADVICE").toString().trim());
				facilityRating.setFinalCode(facilityRatingMap.get("FD_FINAL_CODE")==null?"":facilityRatingMap.get("FD_FINAL_CODE").toString().trim());
				facilityRating.setLastModifier(facilityRating.getFinalName());
				facilityRating.setLastModifyDate(new Date());
				facilityRating.setFinalDate(new Date());
				facilityRating.setFdDate(Tools.addYear(new Date(), 1));
		//		facilityRating.setFinalPd(finalPd);//复评资产保障倍数
		//		facilityRating.setFinalSco(finalSco);//复评分数
		//		facilityRating.setFinalLevel(finalLevel);
			}
		}catch(Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")更新债项评级信息失败！",e);
			throw e;
		}
		return facilityRating;
	}
	
	
	/**
	 * 封装模型计算所需数据
	 * @param creditInfos
	 * @param _flag	是否增信评级
	 * @return 模型计算所需数据
	 */
	public  Map<String,Object> getCreditMap(List<Map<String,Object>> creditInfos) {
		Map<String,Object> para_calculate_model = null;
		try {
			para_calculate_model = new HashMap<String,Object>();//封装模型计算所需数据
			Map<String,Object> _creditInfo = new HashMap<String,Object>();
			List<Map<String, Object>> _realEstateInfos = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> _equityInfos = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> _usufructInfos = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> _landUseRightinfos = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> _receivablesInfos = new ArrayList<Map<String,Object>>();
			_creditInfo.put("realEstateInfo", _realEstateInfos);
			_creditInfo.put("equityInfo", _equityInfos);
			_creditInfo.put("usufructInfo", _usufructInfos);
			_creditInfo.put("landUseRightinfo", _landUseRightinfos);
			_creditInfo.put("receivablesInfo", _receivablesInfos);
			para_calculate_model.put("creditInfo", _creditInfo);
			for (Map<String,Object> creditInfo : creditInfos) {
				if(creditInfo.containsKey("CODE")) {
					para_calculate_model.put(creditInfo.get("CODE").toString(),creditInfo.get("VALUE").toString());
					continue;
				}
				if(creditInfo.containsKey("realEstateInfo")) {
					List<Map<String,Object>> realEstateInfos =  (List<Map<String, Object>>) creditInfo.get("realEstateInfo");
					if(realEstateInfos!=null && realEstateInfos.size()>0) {
						Map<String, Object> _realEInfo_m = new HashMap<String, Object>();
						for(Map<String,Object> _realEInfo : realEstateInfos) {
							_realEInfo_m.put(_realEInfo.get("CODE").toString(), _realEInfo.get("VALUE"));
						}
						_realEstateInfos.add(_realEInfo_m);
					}
					continue;
				}
				if(creditInfo.containsKey("equityInfo")) {
					List<Map<String,Object>> equityInfos =  (List<Map<String, Object>>) creditInfo.get("equityInfo");
					if(equityInfos!=null && equityInfos.size()>0) {
						Map<String, Object> _equityInfo_m = new HashMap<String, Object>();
						for(Map<String,Object> _equityInfo :equityInfos) {
							_equityInfo_m.put(_equityInfo.get("CODE").toString(), _equityInfo.get("VALUE"));
						}
						_equityInfos.add(_equityInfo_m);
					}
					continue;
				}
				if(creditInfo.containsKey("usufructInfo")) {
					List<Map<String,Object>> usufructInfos =  (List<Map<String, Object>>) creditInfo.get("usufructInfo");
					if(usufructInfos!=null && usufructInfos.size()>0) {
						Map<String, Object> _usufructInfo_m = new HashMap<String, Object>();
						for(Map<String,Object> _usufructInfo :usufructInfos) {
							_usufructInfo_m.put(_usufructInfo.get("CODE").toString(), _usufructInfo.get("VALUE"));
						}
						_usufructInfos.add(_usufructInfo_m);
					}
					continue;
				}
				if(creditInfo.containsKey("landUseRightinfo")) {
					List<Map<String,Object>> landUseRightinfos =  (List<Map<String, Object>>) creditInfo.get("landUseRightinfo");
					if(landUseRightinfos!=null && landUseRightinfos.size()>0) {
						Map<String, Object> _landUseRightinfo_m = new HashMap<String, Object>();
						for(Map<String,Object> _landUseRightinfo :landUseRightinfos) {
							_landUseRightinfo_m.put(_landUseRightinfo.get("CODE").toString(), _landUseRightinfo.get("VALUE"));
						}
						_landUseRightinfos.add(_landUseRightinfo_m);
					}
					continue;
				}
				if(creditInfo.containsKey("receivablesInfo")) {
					List<Map<String,Object>> receivablesInfos =  (List<Map<String, Object>>) creditInfo.get("receivablesInfo");
					if(receivablesInfos!=null && receivablesInfos.size()>0) {
						Map<String, Object> _receivablesInfo_m = new HashMap<String, Object>();
						for(Map<String,Object> _receivablesInfo :receivablesInfos) {
							_receivablesInfo_m.put(_receivablesInfo.get("CODE").toString(), _receivablesInfo.get("VALUE"));
						}
						_receivablesInfos.add(_receivablesInfo_m);
					}
					continue;
				}
			}
		}catch(Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：组装模型计算所需数据集失败！",e);
			throw e;
		}
		return para_calculate_model;
	}
	
	/**
	 * @throws Exception 
	 * @报文解析入库
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, String> parsingData(Map<String, Object> map) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put("BP_CODE", null);
		result.put("BP_NAME",null);
		result.put("CREDIT_LEVEL", null);
		result.put("DEBT_LEVEL", null);
		result.put("REPORTURL", null);
		result.put("RET_STATUS", Constats.FAIL_STATUS);
		result.put("RET_CODE", Constats.FAIL_CODE);
		result.put("MSG", "评级失败");
		try {
			Map<String,Object> projectInfo = JSONObject.parseObject(map.get("projectInfo").toString());
			List<Map<String,Object>> creditInfos = (List<Map<String, Object>>) map.get("creditInfo");
			Map<String,Object> creditRatingInfo = JSONObject.parseObject(map.get("debtRatingInfo").toString());
			result.put("BP_CODE", projectInfo.get("PROJECT_NUMBER")==null?null:projectInfo.get("PROJECT_NUMBER").toString().trim());
			result.put("BP_NAME",projectInfo.get("PROJECT_NAME")==null?null:projectInfo.get("PROJECT_NAME").toString().trim());
			if(projectInfo.get("PROJECT_NUMBER")==null||"".equals(projectInfo.get("PROJECT_NUMBER").toString().trim())) {
				log.info("项目编号为空，评级失败!");
				result.put("MSG", "项目编号为空，评级失败!");
				return result;
			}
			NsPrjProject nsPrjProject = NsPrjProjectServiceImpl.setNsPrjProject(projectInfo);
			jdbc.update("DELETE NS_PRJ_PROJECT WHERE PROJECT_NUMBER = ? ", nsPrjProject.getProjectNumber());
			nsPrjProject = nsPrjProjectService.getRepository().save(nsPrjProject);
			String _queryCustInfo = "SELECT FD_BP_NAME AS bpName,FD_BP_CODE AS bpCode FROM NS_BP_MASTER WHERE FD_ID=?";
			NsBpMaster nsBpMaster = (NsBpMaster) jdbc.queryForObject(_queryCustInfo, new BeanPropertyRowMapper(NsBpMaster.class), nsPrjProject.getBpIdTenant());
			String id = "";
			CreditRating creditRating = new CreditRating();
			FacilityRating facilityRating = new FacilityRating();
			Map<String,Object> para_calculate_model = getCreditMap(creditInfos);//解析并封装增信评级所需指标
			//复评
			if ("020".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：增信复评开始 ："+nsPrjProject.getProjectNumber());
				String queryF = "SELECT COUNT(*) 	FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010' ";
				Integer count 	= jdbc.queryForObject(queryF, Integer.class, nsPrjProject.getProjectNumber());
				if (count > 0) {
					String sql 	= "SELECT FD_ID 	FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
				} else {
					String queryS	= "SELECT COUNT(*) 	FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020' ";
					count 		= jdbc.queryForObject(queryS, Integer.class, nsPrjProject.getProjectNumber());
					if (count > 0) {
						String sql 	= "SELECT FD_ID FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
						id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					} else {
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")无增信初评记录，无法增信复评!");
						result.put("MSG", "无增信初评记录，无法增信复评!");
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return result;
					}
				}
				creditRating = creditRatingService.getRepository().findOne(id);
				creditRating = setCreditRating(creditRating,creditRatingInfo,nsPrjProject,nsBpMaster);
				creditRatingService.getRepository().save(creditRating);
			}else if ("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				//初评:设置现有的评级信息评级步骤为无效
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：增信初评开始 ："+nsPrjProject.getProjectNumber());
				String queryStatementF = "SELECT COUNT(*) FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='010'";
				Integer _count = jdbc.queryForObject(queryStatementF, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					jdbc.update("UPDATE NS_CREDIT_RATING SET FD_VAILD='0' , FD_RATING_VAILD='0' WHERE FD_ID=?",id);
				}
				//复评
				String queryStatementS = "SELECT COUNT(*) FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='020'";
				_count = jdbc.queryForObject(queryStatementS, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					jdbc.update("UPDATE NS_CREDIT_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE FD_ID=?",id);
				}
				creditRating = setCreditRating(creditRating,creditRatingInfo,nsPrjProject,nsBpMaster);
				creditRatingService.getRepository().save(creditRating);
			}
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：增信评级模型计算开始！"+nsPrjProject.getProjectNumber());
			CreditRating creditRating_temp = creditRatingService.startModel(para_calculate_model,creditRating.getType(),creditRating.getRatingStatus(),creditRating.getId());
			if (creditRating_temp == null) {
				result.put("MSG", "增信评级所需指标缺失或格式错误，计算失败！");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：("+nsPrjProject.getProjectNumber()+")增信评级所需指标缺失或格式错误，计算失败！");
				return result;
			}
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：增信评级模型计算结束 ！"+nsPrjProject.getProjectNumber());
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：增信评级结束！"+nsPrjProject.getProjectNumber());
			Map<String,String> resultMap = new HashMap<String,String>();//债项评级数据封装Map
			id = "";
			String queryAssetsRating = "SELECT FD_ID FROM  NS_ASSETS_RATING WHERE FD_CUST_CODE=? AND FD_VAILD='1' AND FD_VERSION='3.0' AND FD_RATING_STATUS=?";
			String queryMainRating = "SELECT FD_ID,FD_CUST_CODE,ACTUAL_RATE_SUBJECT_ID FROM  NS_MAIN_RATING WHERE FD_CUST_CODE=? AND FD_VAILD='1' AND FD_VERSION='3.0' AND FD_RATING_STATUS=?";
			List<Map<String, Object>> mainRatings = jdbc.queryForList(queryMainRating,nsBpMaster.getBpCode(),creditRatingInfo.get("FD_RATING_STATUS"));
			List<Map<String, Object>> assetsRatings = jdbc.queryForList(queryAssetsRating, nsBpMaster.getBpCode(),creditRatingInfo.get("FD_RATING_STATUS"));
			String mainRatingID = "";
			String assetsRatingID = "";
			//复评
			if ("020".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：债项复评开始 ："+nsPrjProject.getProjectNumber());
				if(mainRatings!=null&&mainRatings.size()>0) {
					if(!mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID").equals(mainRatings.get(0).get("FD_CUST_CODE"))) {
						mainRatings = jdbc.queryForList(queryMainRating,mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID"),creditRatingInfo.get("FD_RATING_STATUS"));
						if(mainRatings==null||mainRatings.size()<=0) {
							log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsBpMaster.getBpCode()+"无相关评级主体的复评记录");
							result.put("MSG", "无评级主体复评记录");
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							return result;
						}
					}
					mainRatingID = mainRatings.get(0).get("FD_ID").toString();
				}else {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"无主体复评记录！");
					result.put("MSG", "无主体复评记录");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return result;
				}
				if(assetsRatings!=null&&assetsRatings.size()>0) {
					assetsRatingID = assetsRatings.get(0).get("FD_ID").toString();
				}else {
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"无资产复评记录！");
					result.put("MSG", "无资产复评记录");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return result;
				}
				String queryF = "SELECT COUNT(*) 	FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010' ";
				Integer count 	= jdbc.queryForObject(queryF, Integer.class, nsPrjProject.getProjectNumber());
				if (count > 0) {
					String sql 	= "SELECT FD_ID 	FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
				} else {
					String queryS	= "SELECT COUNT(*) 	FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020' ";
					count 		= jdbc.queryForObject(queryS, Integer.class, nsPrjProject.getProjectNumber());
					if (count > 0) {
						String sql 	= "SELECT FD_ID FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
						id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					} else {
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"无债项初评记录，无法复评!");
						result.put("MSG", "无债项初评记录");
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return result;
					}
				}
				facilityRating = facilityRatingServiceImpl.getRepository().findOne(id);
				facilityRating = setFacilityRating(facilityRating,creditRatingInfo,nsPrjProject,nsBpMaster);
			}
			//初评:设置现有的评级信息评级步骤为无效
			if ("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：债项初评开始："+nsPrjProject.getProjectNumber());
				if(mainRatings!=null&&mainRatings.size()>0) {
					if(!mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID").equals(mainRatings.get(0).get("FD_CUST_CODE"))) {
						queryMainRating = "SELECT FD_ID,FD_CUST_CODE,ACTUAL_RATE_SUBJECT_ID FROM  NS_MAIN_RATING WHERE FD_CUST_CODE=? AND FD_VAILD='1' AND FD_VERSION='3.0' ";
						mainRatings = jdbc.queryForList(queryMainRating,mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID"));
						if(mainRatings==null||mainRatings.size()<=0) {
							log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsBpMaster.getBpCode()+"无相关评级主体的评级记录");
							result.put("MSG", "无评级主体相关评级记录");
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							return result;
						}
					}
					mainRatingID = mainRatings.get(0).get("FD_ID").toString();
				}else {
					result.put("MSG", "无主体初评记录");
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"无主体初评记录！");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return result;
				}
				if(assetsRatings!=null&&assetsRatings.size()>0) {
					assetsRatingID = assetsRatings.get(0).get("FD_ID").toString();
				}else {
					result.put("MSG", "无资产初评记录");
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"无资产初评记录！");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return result;
				}
				String queryStatementF = "SELECT COUNT(*) FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='010'";
				Integer _count = jdbc.queryForObject(queryStatementF, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					jdbc.update("UPDATE NS_FACILITY_RATING SET FD_VAILD='0' , FD_RATING_VAILD='0' WHERE FD_ID=?",id);
				}
				//复评
				String queryStatementS = "SELECT COUNT(*) FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='020'";
				_count = jdbc.queryForObject(queryStatementS, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_FACILITY_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
					jdbc.update("UPDATE NS_FACILITY_RATING SET FD_VAILD='0' , FD_RATING_VAILD='0' WHERE FD_ID=?",id);
				}
				facilityRating = setFacilityRating(facilityRating,creditRatingInfo,nsPrjProject,nsBpMaster);
			}
			
			MainRating mainRating = mainRatingService.getRepository().findOne(mainRatingID);
			AssetsRating assetsRating = assetsRatingService.getRepository().findOne(assetsRatingID);
			facilityRating.setMainId(mainRatingID);
			facilityRating.setAssetsId(assetsRatingID);
			facilityRating.setCreditId(creditRating==null?null:creditRating.getId());
			switch(mainRating.getTrackType()){
				case Constats.STR_L:
					if("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
						resultMap.put(Constats.STR_DL_ZT_SRLRX, mainRating==null?"0":(mainRating.getInitSco()==null?"0":mainRating.getInitSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getPd()==null?"0":assetsRating.getPd().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getPd()==null?"0":creditRating.getPd().toString()));
					}else {
						resultMap.put(Constats.STR_DL_ZT_SRLRX, mainRating==null?"0":(mainRating.getSco()==null?"0":mainRating.getSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getFinalPd()==null?"0":assetsRating.getFinalPd().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getFinalPd()==null?"0":creditRating.getFinalPd().toString()));
					}
					facilityRating.setType(Constats.ZX_LRX_M);
					break;
				case Constats.STR_S:
					if("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
						resultMap.put(Constats.STR_DL_ZT_SRSZX, mainRating==null?"0":(mainRating.getInitSco()==null?"0":mainRating.getInitSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getPd()==null?"0":assetsRating.getPd().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getPd()==null?"0":creditRating.getPd().toString()));
					}else{
						resultMap.put(Constats.STR_DL_ZT_SRSZX, mainRating==null?"0":(mainRating.getSco()==null?"0":mainRating.getSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getFinalPd()==null?"0":assetsRating.getFinalPd().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getFinalPd()==null?"0":creditRating.getFinalPd().toString()));
					}
					facilityRating.setType(Constats.ZX_SZX_M);
					break;
				case Constats.STR_Z:
					if("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
						resultMap.put(Constats.STR_DL_ZT_SRZSX, mainRating==null?"0":(mainRating.getInitSco()==null?"0":mainRating.getInitSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getPd()==null?"0":assetsRating.getPd().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getPd()==null?"0":creditRating.getPd().toString()));
					}else {
						resultMap.put(Constats.STR_DL_ZT_SRZSX, mainRating==null?"0":(mainRating.getSco()==null?"0":mainRating.getSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getFinalPd()==null?"0":assetsRating.getFinalPd().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getFinalPd()==null?"0":creditRating.getFinalPd().toString()));
					}
					facilityRating.setType(Constats.ZX_ZSX_M);
					break;
				default:
					result.put("MSG", "评级失败，无匹配的债项评级类型!");
					log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"评级失败，无匹配的债项评级类型！");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return result;
			}
			facilityRating = (FacilityRating)facilityRatingServiceImpl.getRepository().save(facilityRating);//inser or update
			if(resultMap==null||resultMap.isEmpty()) {
				result.put("MSG", "债项评级信息计算失败");
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"评级失败，债项评级信息计算失败！");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return result;
			}
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：债项评级模型计算开始！"+nsPrjProject.getProjectNumber());
			facilityRating = facilityRatingServiceImpl.startFacilityModel(resultMap, facilityRating.getType(), facilityRating.getRatingStatus(), facilityRating.getId());
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：债项评级模型计算结束始！"+nsPrjProject.getProjectNumber());
			if (facilityRating == null) {
				result.put("MSG", "指标缺失或格式错误，计算失败！");
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：当前评级对象"+nsPrjProject.getProjectNumber()+"指标缺失或格式错误，计算失败！");
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return result;
			}
			if("010".equals(facilityRating.getRatingStatus())) {
				result.put("CREDIT_LEVEL",creditRating==null?null:creditRating.getInternLevel());
				result.put("DEBT_LEVEL",facilityRating.getInternLevel());
			}else {
				result.put("CREDIT_LEVEL",creditRating==null?null:creditRating.getFinalLevel());
				result.put("DEBT_LEVEL",facilityRating.getFinalLevel());
				if(new BigDecimal(0).compareTo(facilityRating.getFinalSco()==null?new BigDecimal(0):facilityRating.getFinalSco())!=0) {
					result.put("REPORTURL", "/irs/DebtRatingResults/debtReport?custNo="+facilityRating.getId());
				}
			}
			result.put("RET_STATUS", Constats.SUCCESS_STATUS);
			result.put("RET_CODE", Constats.SUCCESS_CODE);
			result.put("MSG", "成功");
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：债项评级结束！"+nsPrjProject.getProjectNumber());
			return result;
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：评级失败！",e);
			throw e;
		}
		
	}
	
}
