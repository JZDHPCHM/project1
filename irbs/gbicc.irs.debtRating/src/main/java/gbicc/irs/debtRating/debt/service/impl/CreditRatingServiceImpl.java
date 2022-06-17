package gbicc.irs.debtRating.debt.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
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
import org.wsp.engine.model.core.po.Model;
import org.wsp.engine.model.core.po.ModelDefineWrapper;
import org.wsp.engine.model.core.po.Parameter;
import org.wsp.engine.model.core.po.ParameterOption;
import org.wsp.framework.core.util.JacksonObjectMapper;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import gbicc.irs.debtRating.debt.entity.CreditIndex;
import gbicc.irs.debtRating.debt.entity.CreditRating;
import gbicc.irs.debtRating.debt.repository.CreditRatingRepository;
import gbicc.irs.debtRating.debt.service.CreditIndexService;
import gbicc.irs.debtRating.debt.service.CreditRatingService;
import gbicc.irs.main.rating.support.CommonUtils;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.vo.RatingInit;

@Service("CreditRatingServiceImpl")
public class CreditRatingServiceImpl extends DaoServiceImpl<CreditRating, String, CreditRatingRepository>
		implements CreditRatingService {
	
	private static Log log = LogFactory.getLog(CreditRatingServiceImpl.class);
//	@Autowired
//	private NsPrjProjectServiceImpl nsPrjProjectService;

	@Autowired
	private ExecutorFactoryService executorFactoryService;
	@Autowired
	private CreditIndexService creditIndexService;
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private CreditRatingService creditRatingService;
//	@Autowired
//	private FacilityRatingService facilityRatingServiceImpl;
//	@Autowired
//	private MainRatingService mainRatingService;
//	@Autowired
//	private AssetsRatingService assetsRatingService;

	
	/**
	 * 增信评级信息解析并封装
	 * @param creditRatingMap
	 * @param nsPrjProject
	 * @return	CreditRating
	 */
	/*public CreditRating setCreditRating(CreditRating creditRating,Map<String,Object> creditRatingMap,NsPrjProject nsPrjProject,NsBpMaster nsBpMaster) {
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
			creditRating.setCreditType(creditRatingMap.get("CREDIT_TYPE")==null?"":creditRatingMap.get("CREDIT_TYPE").toString().trim());
			if("010".equals(creditRating.getRatingStatus())) {
				creditRating.setInternDate(new Date());
				creditRating.setCreator(creditRating.getInternName());
				creditRating.setCreateDate(new Date());
				creditRating.setInternCode(creditRatingMap.get("FD_INTERN_CODE")==null?"":creditRatingMap.get("FD_INTERN_CODE").toString().trim());
				creditRating.setInternName(creditRatingMap.get("FD_INTERN_NAME")==null?"":creditRatingMap.get("FD_INTERN_NAME").toString().trim());
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
		//		creditRating.setFinalLevel(finalLevel);
				creditRating.setFinalName(creditRatingMap.get("FD_FINAL_NAME")==null?creditRating.getFinalName():creditRatingMap.get("FD_FINAL_NAME").toString().trim());
		//		creditRating.setFinalPd(finalPd);//复评资产保障倍数
		//		creditRating.setFinalSco(finalSco);//复评分数
			}
		}catch(Exception e) {
			log.info(this.getClass().getName()+" : "+e.getMessage());
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
			log.info(this.getClass().getName()+" : "+e.getMessage());
			throw e;
		}
		return facilityRating;
	}
	*/
	/**
	 * 解析并存储债项评级表数据
	 * @param _result
	 * @param _facilityRating
	 * @param creditRatingInfo
	 * @param nsPrjProject
	 * @return 返回的报文信息
	 * @throws Exception 
	 */
	/*private Map<String,String> parseFacilityRating(Map<String, String> _result,FacilityRating _facilityRating,Map<String,Object> creditRatingInfo,NsPrjProject nsPrjProject,CreditRating _creditRating) throws Exception {
		
		}catch (Exception e) {
			log.info(this.getClass().getName()+".parseFacilityRating() : "+e.getMessage());
			throw e;
		}
		return resultMap;
	}*/
	
	/**
	 * 封装模型计算所需数据
	 * @param creditInfos
	 * @return 模型计算所需数据
	 */
	/*public  Map<String,Object> getCreditMap(List<Map<String,Object>> creditInfos) {
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
				}
			}
		}catch(Exception e) {
			log.info(this.getClass().getName()+" : "+e.getMessage());
			throw e;
		}
		return para_calculate_model;
	}
	*/
	/**
	 * @报文解析入库
	 */
	@Transactional
	/*public Map<String, String> parsingData(Map<String, Object> map) {
		Map<String,Object> projectInfo = JSONObject.parseObject(map.get("projectInfo").toString());
		List<Map<String,Object>> creditInfos = (List<Map<String, Object>>) map.get("creditInfo");
		Map<String,Object> creditRatingInfo = JSONObject.parseObject(map.get("debtRatingInfo").toString());
		Map<String, String> result = new HashMap<String, String>();
		result.put("BP_CODE", projectInfo.get("PROJECT_NUMBER")==null?null:projectInfo.get("PROJECT_NUMBER").toString().trim());
		result.put("BP_NAME",projectInfo.get("PROJECT_NAME")==null?null:projectInfo.get("PROJECT_NAME").toString().trim());
		result.put("CREDIT_LEVEL", null);
		result.put("DEBT_LEVEL", null);
		result.put("REPORTURL", null);
		result.put("RET_STATUS", "F");
		result.put("RET_CODE", "111111");
		result.put("MSG", "评级失败");
		try {
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
			
			//解析并封装增信评级所需指标
			Map<String,Object> para_calculate_model = getCreditMap(creditInfos);
			CreditRating creditRating = new	CreditRating();
			FacilityRating facilityRating = new FacilityRating();
			String id = "";
			//复评
			if ("020".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				log.info("增信复评开始："+nsPrjProject.toString());
				String queryF = "SELECT COUNT(*) 	FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010' ";
				Integer count 	= jdbc.queryForObject(queryF, Integer.class, nsPrjProject.getProjectNumber());
				if (count > 0) {
					String sql 	= "SELECT FD_ID 	FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
//					jdbc.update("UPDATE NS_CREDIT_STEP SET FD_VAILD='2' WHERE FD_RATEID=? AND FD_VAILD='1'",id);
				} else {
					String queryS	= "SELECT COUNT(*) 	FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020' ";
					count 		= jdbc.queryForObject(queryS, Integer.class, nsPrjProject.getProjectNumber());
					if (count > 0) {
						String sql 	= "SELECT FD_ID FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='020'";
						id 	= jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
//						jdbc.update("UPDATE NS_CREDIT_RATING SET FD_VAILD='0',FD_RATING_VAILD='0' WHERE FD_RATEID=? AND FD_VAILD='1'",id);	
					} else {
						log.info("无增信初评记录，无法增信复评!");
						result.put("MSG", "无增信初评记录，无法增信复评!");
						return result;
					}
				}
				creditRating = creditRatingService.getRepository().findOne(id);
				creditRating = setCreditRating(creditRating,creditRatingInfo,nsPrjProject,nsBpMaster);
				creditRatingService.getRepository().save(creditRating);
			}
			//初评:设置现有的评级信息评级步骤为无效
			else if ("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				log.info("增信初评开始："+nsPrjProject.toString());
				String queryStatementF = "SELECT COUNT(*) FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1'  AND FD_RATING_STATUS='010'";
				Integer _count = jdbc.queryForObject(queryStatementF, Integer.class, nsPrjProject.getProjectNumber());
				if (_count > 0) {
					String sql = "SELECT FD_ID FROM  NS_CREDIT_RATING WHERE FD_PROJECT_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS='010'";
					id = jdbc.queryForObject(sql, String.class, nsPrjProject.getProjectNumber());
//					creditRating = creditRatingService.findById(id);
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
			//计算增信
			CreditRating creditRating_temp = startModel(para_calculate_model,creditRating.getType(),creditRating.getRatingStatus(),creditRating.getId());
			if (creditRating_temp == null) {
				result.put("MSG", "增信评级所需指标缺失或格式错误，计算失败！");
				throw new Exception(this.getClass().getName()+".parsingData():增信评级所需指标缺失或格式错误，计算失败！");
			}
			log.info("增信评级已成功！");
			Map<String,String> resultMap = new HashMap<String,String>();
			id = "";
			String queryAssetsRating = "SELECT FD_ID FROM  NS_ASSETS_RATING WHERE FD_CUST_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS=?";
			String queryMainRating = "SELECT FD_ID,FD_CUST_CODE,ACTUAL_RATE_SUBJECT_ID FROM  NS_MAIN_RATING WHERE FD_CUST_CODE=? AND FD_VAILD='1' AND FD_RATING_STATUS=?";
			List<Map<String, Object>> mainRatings = jdbc.queryForList(queryMainRating,nsBpMaster.getBpCode(),creditRatingInfo.get("FD_RATING_STATUS"));
			String mainRatingID = "";
			List<Map<String, Object>> assetsRatings = jdbc.queryForList(queryAssetsRating, nsBpMaster.getBpCode(),creditRatingInfo.get("FD_RATING_STATUS"));
			String assetsRatingID = "";
			
			//复评
			if ("020".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				log.info("债项复评开始："+nsPrjProject.toString());
				if(mainRatings!=null&&mainRatings.size()>0) {
					if(!mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID").equals(mainRatings.get(0).get("FD_CUST_CODE"))) {
						mainRatings = jdbc.queryForList(queryMainRating,mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID"),creditRatingInfo.get("FD_RATING_STATUS"));
						if(mainRatings==null||mainRatings.size()<=0) {
							result.put("MSG", "无主体复评记录");
							throw new Exception("无主体复评记录");
						}
					}
					mainRatingID = mainRatings.get(0).get("FD_ID").toString();
				}else {
					result.put("MSG", "无主体复评记录");
					throw new Exception("无主体复评记录");
				}
				if(assetsRatings!=null&&assetsRatings.size()>0) {
					assetsRatingID = assetsRatings.get(0).get("FD_ID").toString();
				}else {
					result.put("MSG", "无资产复评记录");
					throw new Exception("无资产复评记录");
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
						log.info("无债项初评记录，无法复评!");
						result.put("MSG", "无债项初评记录");
						throw new Exception(this.getClass().getName()+".parseFacilityRating():无债项初评记录，无法复评!");
					}
				}
				
				facilityRating = facilityRatingServiceImpl.getRepository().findOne(id);
				facilityRating = setFacilityRating(facilityRating,creditRatingInfo,nsPrjProject,nsBpMaster);
				facilityRatingServiceImpl.getRepository().save(facilityRating);
			}
			//初评:设置现有的评级信息评级步骤为无效
			if ("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
				log.info("债项初评开始："+nsPrjProject.toString());
				if(mainRatings!=null&&mainRatings.size()>0) {
					if(!mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID").equals(mainRatings.get(0).get("FD_CUST_CODE"))) {
						mainRatings = jdbc.queryForList(queryMainRating,mainRatings.get(0).get("ACTUAL_RATE_SUBJECT_ID"),creditRatingInfo.get("FD_RATING_STATUS"));
						if(mainRatings==null||mainRatings.size()<=0) {
							result.put("MSG", "无主体初评记录");
							throw new Exception("无主初评评记录");
						}
					}
					mainRatingID = mainRatings.get(0).get("FD_ID").toString();
				}else {
					result.put("MSG", "无主体初评记录");
					throw new Exception("无主体初评记录");
				}
				if(assetsRatings!=null&&assetsRatings.size()>0) {
					assetsRatingID = assetsRatings.get(0).get("FD_ID").toString();
				}else {
					result.put("MSG", "无资产初评记录");
					throw new Exception("无资产初评记录");
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
				facilityRating = facilityRatingServiceImpl.getRepository().save(facilityRating);
			}
			
			MainRating mainRating = mainRatingService.getRepository().findOne(mainRatingID);
			AssetsRating assetsRating = assetsRatingService.getRepository().findOne(assetsRatingID);
			facilityRating.setMainId(mainRatingID);
			facilityRating.setAssetsId(assetsRatingID);
			facilityRating.setCreditId(creditRating.getId());
			switch(mainRating.getTrackType()){
				case Constats.STR_L:
					if("010".equals(creditRatingInfo.get("FD_RATING_STATUS"))) {
						resultMap.put(Constats.STR_DL_ZT_SRLRX, mainRating==null?"0":(mainRating.getInitSco()==null?"0":mainRating.getInitSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getPd()==null?"0":assetsRating.getPd().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getPd()==null?"0":creditRating.getPd().toString()));
					}else {
						resultMap.put(Constats.STR_DL_ZT_SRLRX, mainRating==null?"0":(mainRating.getSco()==null?"0":mainRating.getSco().toString()));
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getFinalSco()==null?"0":assetsRating.getFinalSco().toString()));
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
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getFinalSco()==null?"0":assetsRating.getFinalSco().toString()));
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
						resultMap.put(Constats.STR_DL_ZC_BZBS, assetsRating==null?"0":(assetsRating.getFinalSco()==null?"0":assetsRating.getFinalSco().toString()));
						resultMap.put(Constats.STR_DL_ZX_BZBS, creditRating==null?"0":(creditRating.getFinalPd()==null?"0":creditRating.getFinalPd().toString()));
					}
					facilityRating.setType(Constats.ZX_ZSX_M);
					break;
				default:
					break;
			}
			facilityRating = (FacilityRating)facilityRatingServiceImpl.getRepository().save(facilityRating);//inser or update
			if(resultMap==null||resultMap.isEmpty()) {
				result.put("MSG", "债项评级信息计算失败");
				throw new Exception(this.getClass().getName()+".parsingData():债项评级信息计算失败！");
			}
			facilityRating = facilityRatingServiceImpl.startFacilityModel(resultMap, facilityRating.getType(), facilityRating.getRatingStatus(), facilityRating.getId());
			if (facilityRating == null) {
				result.put("MSG", "指标缺失或格式错误，计算失败！");
				throw new Exception(this.getClass().getName()+".parsingData():指标缺失或格式错误，计算失败！");
			}
			if("010".equals(creditRating.getRatingStatus())) {
				result.put("CREDIT_LEVEL",creditRating.getInternLevel());
				result.put("DEBT_LEVEL",creditRating.getInternLevel());
			}else {
				result.put("CREDIT_LEVEL",creditRating.getFinalLevel());
				result.put("DEBT_LEVEL",creditRating.getFinalLevel());
			}
			result.put("REPORTURL", "/irs/facilityRating/report?custNo="+creditRating.getId());//评级报告url
			result.put("RET_STATUS", "S");
			result.put("RET_CODE", "000000");
			result.put("MSG", "成功");
			return result;
		} catch (Exception e) {
			log.info(this.getClass().getName()+" : "+e.getMessage());
			e.printStackTrace();
			return result;
		}
		
	}

	*/
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
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" : 模型计算失败！",e);
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
	public Map<String,String> insertFacilityModel(Map<String, String> paramValue, String type,String status) {
		System.out.println("~~~~~~~~~看看模型paramValue："+JSONObject.toJSONString(paramValue));
		Model model;
		try {
			model = this.getModel(type);
			
			System.out.println(model.getCode()+"  "+model.getName());
			
			List<Model> subModels = model.getSubModels();
			System.out.println("~~~~~~~~~看看模型SubModels："+JSONObject.toJSONString(subModels));
			Map<String, String> modelGetMap = ratingResults(model.getCode(), paramValue);
			System.out.println("~~~~~~调取模型之后的map："+JSONObject.toJSON(modelGetMap));
			return modelGetMap;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,String> data = new HashMap<String, String>();
			data.put("msg","计算出错！");
			return null;
			
		}
		
		
	}

	public Map<String,String> insertModel(Map<String, String> paramValue, String type,String status) {
		Model model;
		System.out.println("~~~~~~~~~看看模型paramValue："+JSONObject.toJSONString(paramValue));
		try {
			model = this.getModel(type);
			
			System.out.println(model.getCode()+"  "+model.getName());
			
			List<Model> subModels = model.getSubModels();
			System.out.println("~~~~~~~~~看看模型SubModels："+JSONObject.toJSONString(subModels));
			
			Map<String, String> modelGetMap = ratingResults(model.getCode(), paramValue);
			System.out.println("~~~~~~调取模型之后的map："+JSONObject.toJSON(modelGetMap));
			
			//解析定量数据
			insertIndexes(subModels, paramValue, type, modelGetMap, "DL",status,1,"111");
			//解析定项数据
			insertIndexes(subModels, paramValue, type, modelGetMap, "DX",status,1,"111");
			//解析总数据
			insertModelIndex(model, modelGetMap, type, status, 1,"111");
			return modelGetMap;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,String> maps = new HashMap<String, String>();
			maps.put("msg", "模型计算失败！");
			return null;
		}
		
	}
	@Transactional
	public CreditRating calculate(CreditRating mainRating,String status,BigDecimal internSco) {
		String code = "";
		try {
			code = jdbc.queryForObject(
					"select FD_CODE from Ns_main_level where fd_type='120' and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?)",
					String.class, internSco, internSco);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();  
		}
		Double pd = null;
		int i = internSco.compareTo(BigDecimal.ZERO);
//		i=0: 表示bigDecimal的值  等于 0
//		i=1: 表示bigDecimal的值与 大于0
//		i=-1: 表示bigDecimal的值与 小于 0

		//if(i==1) {
			if(status.equals("010")) {
				mainRating.setInternLevel(code);
			}else {
				mainRating.setFinalLevel(code);
			}
		//}
		

		jdbc.update("update ns_credit_rating set fd_vaild='0' where FD_CUST_CODE=? AND FD_PROJECT_CODE=? AND fd_id <> ? ",
				mainRating.getCustCode(),mainRating.getProjectCode(),mainRating.getId());
		
		creditRatingService.getRepository().save(mainRating);
		return mainRating;
	}
	public CreditRating startModel(Map<String, Object> paramValue, String type,String status,String mainId) {
		CreditRating ratingByid = null;
		
		//根据增信记录id获取 增信的记录
		try {
			ratingByid = creditRatingService.findById(mainId);
			if(ratingByid==null) {
				return null;
			}
			
			Model model = this.getModel(type);
			
			//解析 增信5个指标细项（不动产、股权调整项等）
			Map<String, String> zxbzbsMap = saveRatingStepAndIndex(ratingByid, paramValue, type, status);
			
			if(MapUtils.isEmpty(zxbzbsMap)) {
				
				return null;
			}
			
			
			ratingByid.setRatingStatus(status);//设置测算状态 000-测算 010-初评 020-复评
			ratingByid.setVaild("1");//设置状态为有效
			BigDecimal internSco = new BigDecimal("0");//获取保障倍数，映射等级
			if(status.equals("010")) {//初评
				ratingByid.setInternSco(new BigDecimal(zxbzbsMap.get("DX_ZX_BZBS_RESULT")));//得分
				//internSco = ratingByid.getInternSco();
				ratingByid.setInternDate(new Date());
				ratingByid.setPd(new BigDecimal(zxbzbsMap.get("DX_"+type+"_RESULT")));
				internSco = ratingByid.getPd();
			}else {//复评
				ratingByid.setFinalSco(new BigDecimal(zxbzbsMap.get("DX_ZX_BZBS_RESULT")));//得分
				//internSco = ratingByid.getFinalSco();
				ratingByid.setFinalPd(new BigDecimal(zxbzbsMap.get("DX_"+type+"_RESULT")));
				internSco = ratingByid.getFinalPd();
				Date date = new Date();
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);//设置起时间
			    //System.out.println("111111111::::"+cal.getTime());
			    cal.add(Calendar.YEAR, 1);//增加一年
			    ratingByid.setFinalDate(date);//设置复评时间
				//rating.setFdDate(fdDate);
				if(status.equals("000")) {
					//设置复评经理
					ratingByid.setFinalName(SecurityUtil.getUserName());
					ratingByid.setFdDate(cal.getTime());
				}
			}
			
			//映射增信等级
			calculate(ratingByid, status, internSco);
			
			
		} catch (Exception e) {
			log.info(e.getMessage());
//			jdbc.update("update ns_assets_rating set fd_vaild='0' where FD_CUST_CODE=? and fd_project_code=? and fd_id = ?", 
//					ratingByid.getProjectCode(),ratingByid.getCustCode(),ratingByid.getId());
			e.printStackTrace();
			throw new RuntimeException();
		}
		return ratingByid;
		
		
		
		
	}
	
	@Transactional
	public Map<String, String> saveRatingStepAndIndex(CreditRating rating, Map<String, Object> paramValue,String type,String status) {
		try {
			//判断如果是复评的，为防止多次调用复评，造成indexes表中有多条复评数据
			if("020".equals(status)) {
				String sql = "delete NS_CREDIT_INDEXES where FD_RATINGID=? and FD_RATING_STATUS='020'";
				jdbc.update(sql, rating.getId());
			}
//			System.out.println("~~~~前端传递参数paramValue:"+JSONObject.toJSONString(paramValue));
			paramValue = com.alibaba.fastjson.JSONObject.parseObject(net.sf.json.JSONObject.fromObject(paramValue).toString());
			String dlFXCK = paramValue.get("DL_FXCK").toString();//获取到风险敞口的值。
			if(StringUtils.isEmpty(dlFXCK)) {
				//如果解析到的DL_FXCK为空，设置为默认值3000
				dlFXCK="3000";
			}
			
			//初始化增信保障倍数模型参数为0
			BigDecimal BDCTZXJGZ = new BigDecimal("0.00");//不动产调整项结果值
			BigDecimal TDSYQTZXSRZ = new BigDecimal("0.00");//土地使用权调整项结果值
			BigDecimal YSZKTZXSRZ = new BigDecimal("0.00");//应收账款调整项结果值
			BigDecimal SYQTZXSRZ = new BigDecimal("0.00");//收益权调整项结果值
			BigDecimal GQTZXSRZ = new BigDecimal("0.00");//股权调整项结果值
			
			
			
			//解析模型参数列表
			String creditInfostr = paramValue.get("creditInfo").toString();//获取所有模型参数
			Map creditInfo = (Map) JSONObject.parse(creditInfostr);
			
			
			//不动产调整项解析入库
			String realEstateInfostr = creditInfo.get("realEstateInfo").toString();//不动产调整项
			List<Map<String,String>> realEstateInfo = (List) JSONObject.parse(realEstateInfostr);
			//验证不动产模型调整项参数list集合是否为空，不为空，进行模型调用并计算出模型得分总和
			if(realEstateInfo.size()>0) {
				
				String modeltype="ZX_BDCTZ_1";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : realEstateInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,rating.getId());
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,rating.getId());
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,rating.getId());
					BDCTZXJGZ = BDCTZXJGZ.add(insertModelIndex);
				}
			}
			
			//股权调整项解析入库
			String equityInfostr = creditInfo.get("equityInfo").toString();//股权调整项
			List<Map<String,String>> equityInfo = (List) JSONObject.parse(equityInfostr);
			//验证不动产模型调整项参数list集合是否为空，不为空，进行模型调用并计算出模型得分总和
			if(equityInfo.size()>0) {
				
				String modeltype="ZX_GQTZX_5";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : equityInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,rating.getId());
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,rating.getId());
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,rating.getId());
					GQTZXSRZ = GQTZXSRZ.add(insertModelIndex);
				}
			}
			
			
			//收益权调整项解析入库
			String usufructInfostr = creditInfo.get("usufructInfo").toString();//收益权调整项
			List<Map<String,String>> usufructInfo = (List) JSONObject.parse(usufructInfostr);
			if(usufructInfo.size()>0) {
				
				String modeltype="ZX_SYQTZ_4";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : usufructInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,rating.getId());
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,rating.getId());
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,rating.getId());
					SYQTZXSRZ = SYQTZXSRZ.add(insertModelIndex);
				}
			}
			
			
			//土地使用权调整项解析入库
			String landUseRightinfostr = creditInfo.get("landUseRightinfo").toString();//土地使用权调整项
			List<Map<String,String>> landUseRightinfo = (List) JSONObject.parse(landUseRightinfostr);
			if(landUseRightinfo.size()>0) {
				
				String modeltype="ZX_TDSYQ_2";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : landUseRightinfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,rating.getId());
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,rating.getId());
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,rating.getId());
					TDSYQTZXSRZ = TDSYQTZXSRZ.add(insertModelIndex);
				}
			}
			//应收账款调整项解析入库
			String receivablesInfostr = creditInfo.get("receivablesInfo").toString();//应收账款调整项
			List<Map<String,String>> receivablesInfo = (List) JSONObject.parse(receivablesInfostr);
			if(receivablesInfo.size()>0) {
				
				String modeltype="ZX_YSZKTZ_3";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : receivablesInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,rating.getId());
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,rating.getId());
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,rating.getId());
					YSZKTZXSRZ = YSZKTZXSRZ.add(insertModelIndex);
				}
			}
			
			
			//解析增信保障倍数模型
			Map<String,String> bzbsMap = new HashMap<String, String>();
			bzbsMap.put("DL_BDCTZXJGZ", BDCTZXJGZ.toString());
			bzbsMap.put("DL_TDSYQTZXSRZ", TDSYQTZXSRZ.toString());
			bzbsMap.put("DL_YSZKTZXSRZ", YSZKTZXSRZ.toString());
			bzbsMap.put("DL_SYQTZXSRZ", SYQTZXSRZ.toString());
			bzbsMap.put("DL_GQTZXSRZ", GQTZXSRZ.toString());
			bzbsMap.put("DL_FXCK", dlFXCK);
			
			
			Model model = this.getModel(type);
			
			System.out.println(model.getCode()+"  "+model.getName());
			
			List<Model> subModels = model.getSubModels();
			Map<String, String> modelGetMap = ratingResults(model.getCode(), bzbsMap);
			
			//解析定量数据
			insertIndexes(subModels, bzbsMap, type, modelGetMap, "DL",status,0,rating.getId());
			//解析定项数据
			insertIndexes(subModels, bzbsMap, type, modelGetMap, "DX",status,0,rating.getId());
			//解析总数据
			insertModelIndex(model, modelGetMap, type, status, 0,rating.getId());
			
			
			
			return modelGetMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);  
			
			
		}
		
		
	}
	
	@Override
	public Map<String, Object> testmodel(Map<String, Object> paramValue, String type,String status) {
		// TODO Auto-generated method stub
		try {
			
			HashMap<String,Object> returndata = new HashMap<String, Object>();
			System.out.println("~~~~前端传递参数paramValue:"+JSONObject.toJSONString(paramValue));
			String dlFXCK = paramValue.get("DL_FXCK").toString();//获取到风险敞口的值。
			if(StringUtils.isEmpty(dlFXCK)) {
				//如果解析到的DL_FXCK为空，设置为默认值3000
				dlFXCK="3000";
			}
			
			//初始化增信保障倍数模型参数为0
			BigDecimal BDCTZXJGZ = new BigDecimal("0.00");//不动产调整项结果值
			BigDecimal TDSYQTZXSRZ = new BigDecimal("0.00");//土地使用权调整项结果值
			BigDecimal YSZKTZXSRZ = new BigDecimal("0.00");//应收账款调整项结果值
			BigDecimal SYQTZXSRZ = new BigDecimal("0.00");//收益权调整项结果值
			BigDecimal GQTZXSRZ = new BigDecimal("0.00");//股权调整项结果值
			
			
			
			//解析模型参数列表
			String creditInfostr = paramValue.get("creditInfo").toString();//获取所有模型参数
			Map creditInfo = (Map) JSONObject.parse(creditInfostr);
			
			
			//不动产调整项解析入库
			String realEstateInfostr = creditInfo.get("realEstateInfo").toString();//不动产调整项
			List<Map<String,String>> realEstateInfo = (List) JSONObject.parse(realEstateInfostr);
			//验证不动产模型调整项参数list集合是否为空，不为空，进行模型调用并计算出模型得分总和
			if(realEstateInfo.size()>0) {
				
				String modeltype="ZX_BDCTZ_1";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : realEstateInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					returndata.put(model.getName()+i,modelGetMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,"111");
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,"111");
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,"111");
					BDCTZXJGZ = BDCTZXJGZ.add(insertModelIndex);
				}
			}
			
			//股权调整项解析入库
			String equityInfostr = creditInfo.get("equityInfo").toString();//股权调整项
			List<Map<String,String>> equityInfo = (List) JSONObject.parse(equityInfostr);
			//验证不动产模型调整项参数list集合是否为空，不为空，进行模型调用并计算出模型得分总和
			if(equityInfo.size()>0) {
				
				String modeltype="ZX_GQTZX_5";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : equityInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					returndata.put(model.getName()+i,modelGetMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,"111");
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,"111");
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,"111");
					GQTZXSRZ = GQTZXSRZ.add(insertModelIndex);
				}
			}
			
			
			//收益权调整项解析入库
			String usufructInfostr = creditInfo.get("usufructInfo").toString();//收益权调整项
			List<Map<String,String>> usufructInfo = (List) JSONObject.parse(usufructInfostr);
			if(usufructInfo.size()>0) {
				
				String modeltype="ZX_SYQTZ_4";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : usufructInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					returndata.put(model.getName()+i,modelGetMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,"111");
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,"111");
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,"111");
					SYQTZXSRZ = SYQTZXSRZ.add(insertModelIndex);
				}
			}
			
			
			//土地使用权调整项解析入库
			String landUseRightinfostr = creditInfo.get("landUseRightinfo").toString();//土地使用权调整项
			List<Map<String,String>> landUseRightinfo = (List) JSONObject.parse(landUseRightinfostr);
			if(landUseRightinfo.size()>0) {
				
				String modeltype="ZX_TDSYQ_2";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : landUseRightinfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					returndata.put(model.getName()+i,modelGetMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,"111");
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,"111");
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,"111");
					TDSYQTZXSRZ = TDSYQTZXSRZ.add(insertModelIndex);
				}
			}
			//应收账款调整项解析入库
			String receivablesInfostr = creditInfo.get("receivablesInfo").toString();//应收账款调整项
			List<Map<String,String>> receivablesInfo = (List) JSONObject.parse(receivablesInfostr);
			if(receivablesInfo.size()>0) {
				
				String modeltype="ZX_YSZKTZ_3";//获取模型类型
				
				Model model = this.getModel(modeltype);
				System.out.println(model.getCode()+"  "+model.getName());
				List<Model> subModels = model.getSubModels();
				
				//模型解析并存入indexes表
				int i = 0;
				for (Map<String, String> bdcMap : receivablesInfo) {
					i++;
					Map<String, String> modelGetMap = ratingResults(model.getCode(), bdcMap);
					returndata.put(model.getName()+i,modelGetMap);
					//解析定量数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DL",status,i,"111");
					//解析定项数据
					insertIndexes(subModels, bdcMap, modeltype, modelGetMap, "DX",status,i,"111");
					//解析总数据
					BigDecimal insertModelIndex = insertModelIndex(model, modelGetMap, modeltype, status, i,"111");
					YSZKTZXSRZ = YSZKTZXSRZ.add(insertModelIndex);
				}
			}
			
			
			//解析增信保障倍数模型
			Map<String,String> bzbsMap = new HashMap<String, String>();
			bzbsMap.put("DL_BDCTZXJGZ", BDCTZXJGZ.toString());
			bzbsMap.put("DL_TDSYQTZXSRZ", TDSYQTZXSRZ.toString());
			bzbsMap.put("DL_YSZKTZXSRZ", YSZKTZXSRZ.toString());
			bzbsMap.put("DL_SYQTZXSRZ", SYQTZXSRZ.toString());
			bzbsMap.put("DL_GQTZXSRZ", GQTZXSRZ.toString());
			bzbsMap.put("DL_FXCK", dlFXCK);
			
			
			Model model = this.getModel(type);
			
			System.out.println(model.getCode()+"  "+model.getName());
			
			List<Model> subModels = model.getSubModels();
			Map<String, String> modelGetMap = ratingResults(model.getCode(), bzbsMap);
			returndata.put(model.getName(),modelGetMap);
			
			//解析定量数据
			insertIndexes(subModels, bzbsMap, type, modelGetMap, "DL",status,0,"111");
			//解析定项数据
			insertIndexes(subModels, bzbsMap, type, modelGetMap, "DX",status,0,"111");
			//解析总数据
			insertModelIndex(model, modelGetMap, type, status, 0,"111");
			
			
			
			
			return returndata;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,String> maps = new HashMap<String, String>();
			maps.put("msg", "模型计算失败！");
			return null;
		}
	}
	
	
	public BigDecimal insertModelIndex(Model model,Map<String, String> modelGetMap,String type,String status,int i,String ratingId) {
		CreditIndex index = new CreditIndex();
		index.setRatingId(ratingId);//~~~~~~~~~~~~~~~~~~~~设置主体id
		index.setId(UUID.randomUUID().toString());//主键id
		index.setRatingId(ratingId);
		//index.setRatingStep(step);//设置步骤id
		index.setIndexType(RatingStepType.QUALITATIVE_EDIT);//指标类型定项
		index.setIndexCategory(model.getName());//指标分类
		index.setIndexName(model.getName());//指标名称
		index.setIndexId(model.getId());//指标id
		index.setIndexCode(model.getCode());//指标编号
		index.setLevel("0");
		index.setIndexScore(new BigDecimal(modelGetMap.get("DX_"+model.getCode()+"_RESULT")));
		index.setFdmodel(type);
		index.setRatingStatus(status);// 000 评级测算  010 初评  020 复评
		index.setCreditType(type);
		index.setCreditNumber(i);
		try {
			creditIndexService.add(index);
			return new BigDecimal(modelGetMap.get("DX_"+model.getCode()+"_RESULT"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new BigDecimal("0.00");
		}
		
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
	public void insertIndexes(List<Model> subModels,Map<String, String> paramValue,String type,
			Map<String,String> modelGetMap,String types,String Status,int i,String ratingId) throws Exception{
		for (Model model2 : subModels) {
			List<Parameter> allParameters = model2.getAllParameters();
			String name = model2.getName();
			String level = "1";
			String code = model2.getCode();
			String codetype = code.substring(0,code.indexOf("_"));
			if(codetype.equals(types)) {// ||
				//存放一级指标
				//如果1级指标下没有数据就不需要存放
				List<Model> subModels2 = model2.getSubModels();
				if(subModels2.size()!=0) {//如果一级指标下面没有二级指标，一级指标就不需要存储，直接解析allParameters下的数据
					CreditIndex index = new CreditIndex();
					index.setRatingId(UUID.randomUUID().toString());//~~~~~~~~~~~~~~~~~~~~设置主体id
					index.setId(UUID.randomUUID().toString());//主键id
					index.setRatingId(ratingId);
					//index.setRatingStep(step);//设置步骤id
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
					index.setLevel(level);
					index.setRatingStatus(Status);// 000 评级测算  010 初评  020 复评
					index.setCreditType(type);
					index.setCreditNumber(i);
					if(model2.getCode().equals("DL_SC_ZBJCX")) {
						index.setIndexScore(null);
					}else {
						index.setIndexScore(new BigDecimal(modelGetMap.get(model2.getCode()+"_RESULT")));
					}
					index.setFdmodel(type);
					creditIndexService.add(index);
				}
				//存放allParameters下的指标项
				for(Parameter param : allParameters) {
					insertDxIndex(type,allParameters,name, level, model2.getId(), paramValue, modelGetMap,param,codetype,Status,i,ratingId);
				}
			} 
				continue;
		}
	}
	
	
	public void insertDxIndex(String type,List<Parameter> allParameters,String name,String level,String modelparentid,
			Map<String, String> paramValue,Map<String,String> modelGetMap,Parameter param,String codetype,
			String Status,int i,String ratingId) throws Exception {
		if (!StringUtils.isEmpty(param.getCode()) && param.getName().contains("输入值")) {
			CreditIndex index = new CreditIndex();
			System.out.println("~~~~~~kankanparam:"+JSONObject.toJSON(param));
			index.setRatingId(ratingId);
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
		
			
			//index.setRatingStep(step);//设置步骤id
			if(codetype.equals("DL")||codetype.equals("ZB")) {
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
			index.setIndexValue(paramValue.get(param.getCode()));//指标的输入值
			index.setLevel(level);//指标等级
			index.setParentId(parentid);//指标的parentid
			index.setIndexScore(new BigDecimal(modelGetMap.get(param.getCode()+"_RESULT")));//指标得分
			index.setFdmodel(type);
			index.setRatingStatus(Status);// 000 评级测算  010 初评  020 复评
			index.setCreditType(type);
			index.setCreditNumber(i);
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
			creditIndexService.add(index);
		}
		
	}
	
	
	public Map<String,Object> getCreditIndexInput(){
		
		//获取所有的模型类型
		String sql = "select distinct fd_model from ns_credit_report order by  to_number(REGEXP_SUBSTR(fd_model,'[0-9]+')) asc";
		List<Map<String, Object>> fdModelList = jdbc.queryForList(sql);
		HashMap<String,Object> data = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : fdModelList) {
			HashMap<String,Object> modelInfo = new HashMap<String, Object>();
			modelInfo.put("modelCode", map.get("fd_model"));
			modelInfo.put("modelName",getModelName(map.get("fd_model").toString()));//模型名称
			
			String sql1="select FD_MODEL,FD_INDEXCATEGORY,FD_INDEXNAME,FD_INDEXCODE,FD_INDEXTYPE,FD_LEVEL,FD_DX_TEXT From ns_credit_report where fd_model=? and FD_LEVEL=1";
			
			//查询一级指标
			List<Map<String, Object>> sqlList = jdbc.queryForList(sql1, map.get("fd_model").toString());
			List<Map<String, Object>> modelList = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> model : sqlList) {
				HashMap<String,Object> indexInfo = new HashMap<String, Object>();
				indexInfo.put("FD_INDEXCATEGORY", model.get("FD_INDEXCATEGORY"));
				indexInfo.put("FD_INDEXCODE", model.get("FD_INDEXCODE"));
				//一级指标为定量
				if("QUANTITATIVE".equals(model.get("FD_INDEXTYPE"))) {
					indexInfo.put("FD_INDEXTYPE", model.get("FD_INDEXTYPE"));
					ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();//用于存放二级指标
					//二级指标map存放
					HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
					hashMap1.put("FD_INDEXNAME", model.get("FD_INDEXNAME"));
					hashMap1.put("FD_INDEXCODE", model.get("FD_INDEXCODE"));
					
					arrayList.add(hashMap1);
					indexInfo.put("sonList", arrayList);
					modelList.add(indexInfo);
				}
				//如果是定性的则查询二级指标具体指标
				if("QUALITATIVE_EDIT".equals(model.get("FD_INDEXTYPE"))) {
					indexInfo.put("FD_INDEXTYPE", model.get("FD_INDEXTYPE"));
					String sql2="select FD_INDEXNAME,FD_INDEXCODE,FD_DX_TEXT From ns_credit_report where fd_model=? and FD_INDEXCATEGORY=? and FD_LEVEL=2";
					List<Map<String, Object>> sqlList2 = jdbc.queryForList(sql2, model.get("FD_MODEL").toString(),model.get("FD_INDEXCATEGORY"));
					HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
					hashMap1.put("FD_INDEXNAME", model.get("FD_INDEXNAME"));
					hashMap1.put("FD_INDEXCODE", model.get("FD_INDEXCODE"));
					indexInfo.put("sonList", sqlList2);
					modelList.add(indexInfo);
				}
				modelInfo.put("modelList",modelList);
			}
			
			list.add(modelInfo);
		}
		data.put("data", list);
		data.put("status", true);
		
		System.out.println(JSON.toJSON(list));
		
		
		
		
		return data;
		
	}
	
	public String getModelName(String ModelCode) {
		if("ZX_BDCTZ_1".equals(ModelCode)) {
			return "不动产调整项";
		}else if("ZX_TDSYQ_2".equals(ModelCode)) {
			return "土地使用权调整项";
		}else if("ZX_YSZKTZ_3".equals(ModelCode)) {
			return "应收账款调整项";
		}else if("ZX_SYQTZ_4".equals(ModelCode)) {
			return "收益权调整项";
		}else if("ZX_GQTZX_5".equals(ModelCode)) {
			return "股权调整项";
		}else {
			return null;
		}
		
		
		
		
	}
	
	

	
	
	/**
	 * 创建增信评级记录
	 */
	public String creatCreditRating(String bpCode,String proCode) {
		String sqlCust = "select fd_bp_code,fd_bp_name,FD_SCORE_TEMPLATE_ID from NS_BP_MASTER where fd_bp_code=?";
		List<Map<String,Object>> map = jdbc.queryForList(sqlCust,bpCode);
		String code = map.get(0).get("FD_BP_CODE").toString();
		String name = map.get(0).get("FD_BP_NAME").toString();
		
		String prosqlCust = "select PROJECT_NUMBER,PROJECT_NAME from NS_PRJ_PROJECT where PROJECT_NUMBER=?";
		List<Map<String,Object>> proMap = jdbc.queryForList(prosqlCust,proCode);
		String procode = proMap.get(0).get("PROJECT_NUMBER").toString();
		String proName = proMap.get(0).get("PROJECT_NAME").toString();
		String TemplateId="";
		String sql="insert into ns_credit_rating (FD_ID,FD_PROJECT_CODE,FD_PROJECT_NAME,FD_CUST_CODE,FD_CUST_NAME,FD_VERSION)values(?,?,?,?,?,?)";
		//String uuid = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		jdbc.update(sql,uuid,procode,proName,code,name,"3.0");
		CreditRating creditRating = repository.findOne(uuid);
		
		return creditRating.getId();
	}
	
	
	@Override
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			CreditRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("highprecision"), "FD_HIGH_PRECISION"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("enterprisehonor"), "FD_ENTERPRISE_HONOR"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("economic"), "FD_ECONOMIC_TYPE"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		// ---
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("custName"), "FD_BP_NAME"));
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
				CommonUtils.sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "FD_FINAL_DATE"));
		
		String sql = "select \r\n" + "master.FD_BP_NAME as custName,\r\n" + "master.FD_BP_CODE as custCode,\r\n"
				+ "pro.project_number as proCode,\r\n" + "pro.project_name as proName,pro.lease_item_short_name as proItemName,\r\n"
				+ "master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" + "FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n"
				+ "FD_HIGH_PRECISION as highPrecision,\r\n" + "FD_ENTERPRISE_HONOR as enterpriseHonor,\r\n"
				+ "FD_ECONOMIC_TYPE as economic,\r\n" + "rating.FD_ID as id,\r\n" + "rating.product_type as type,\r\n"
				+ "master.FD_EMPLOYEE_ID as employeeId,FD_INTERN_LEVEL as internLevel,org.fd_name as orgName, fd_pd as internBs,\r\n" + "FD_FINAL_LEVEL as finalLevel,\r\n"
				+ "FD_final_pd as finalBs,\r\n" + "FD_RATING_STATUS as ratingStatus,\r\n"
				+ "FD_INTERN_NAME as internName," + "FD_FINAL_NAME as finalName,"+ "FD_ASSETS_NAME as assetReview," 
				+ "to_char(FD_FINAL_DATE,'yyyy-MM-dd') as finalDate\r\n" + "from   NS_PRJ_PROJECT pro   \r\n"
				+ "left join ns_assets_rating rating on pro.PROJECT_NUMBER = rating.FD_PROJECT_CODE    \r\n"+str
				+ "left join NS_BP_MASTER master on to_char(pro.bp_id_tenant) = to_char(master.fd_ID) \r\n"
				+ "left join FR_AA_USER_PARTITION init on rating.FD_INTERN_CODE = init.employee_id  "
				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id " 
				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
				+ "where  1=1  "
				+ sqlQuery + " order by rating.FD_FINAL_DATE desc nulls last ";
		System.out.println("~~~sql:"+sql);
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
