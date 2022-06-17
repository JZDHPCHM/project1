package gbicc.irs.debtRating.debt.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.debtRating.debt.entity.FacilityIndex;
import gbicc.irs.debtRating.debt.entity.FacilityRating;
import gbicc.irs.debtRating.debt.repository.FacilityRatingRepository;
import gbicc.irs.debtRating.debt.service.FacilityIndexService;
import gbicc.irs.debtRating.debt.service.FacilityRatingService;
import gbicc.irs.main.rating.support.RatingStepType;

@Service("FacilityRatingServiceImpl")
public class FacilityRatingServiceImpl extends DaoServiceImpl<FacilityRating, String, FacilityRatingRepository>
		implements FacilityRatingService {
	
	private static Log log = LogFactory.getLog(FacilityRatingServiceImpl.class);
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ExecutorFactoryService executorFactoryService;
	@Autowired
	private FacilityRatingService facilityRatingService;
	@Autowired
	private FacilityIndexService facilityIndexService;
	
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
			throw new RuntimeException();  
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
	public Map<String, String> testmodel(Map<String, String> paramValue, String type,String status){
		
		Model model;
		try {
			model = this.getModel(type);
			
			System.out.println(model.getCode()+"  "+model.getName());
			
			List<Model> subModels = model.getSubModels();
			System.out.println("~~~~~~~~~看看模型SubModels："+JSONObject.toJSONString(subModels));
			Map<String, String> modelGetMap = ratingResults(model.getCode(), paramValue);
			System.out.println("~~~~~~调取模型之后的map："+JSONObject.toJSON(modelGetMap));
			//insertIndexes(subModels, paramValue, type, modelGetMap, "DL", status,"111");
			return modelGetMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	/**
	 * 债项模型测算
	 * @param paramValue
	 * @param type
	 * @param status
	 * @param facilityId
	 * @return
	 */
	public FacilityRating startFacilityModel(Map<String, String> paramValue, String type,String status,String facilityId){
		Model model;
		FacilityRating facimityByid = null;
		try {
			facimityByid = facilityRatingService.findById(facilityId);
			if(facimityByid==null) {
				return null;
			}
			
			
			model = this.getModel(type);
			List<Model> subModels = model.getSubModels();
			System.out.println("~~~~~~~~~看看模型SubModels："+JSONObject.toJSONString(subModels));
			Map<String, String> modelGetMap = ratingResults(model.getCode(), paramValue);
			System.out.println("~~~~~~调取模型之后的map："+JSONObject.toJSON(modelGetMap));
			//判断如果是复评的，为防止多次调用复评，造成indexes表中有多条复评数据
			if("020".equals(status)) {
				String sql = "delete NS_FACILITY_INDEXES where FD_RATINGID=? and FD_RATING_STATUS='020'";
				jdbc.update(sql, facilityId);
			}
			insertIndexes(subModels, paramValue, type, modelGetMap, "DL", status,facimityByid.getId());
			
			facimityByid.setRatingStatus(status);//设置测算状态 000-测算 010-初评 020-复评
			facimityByid.setVaild("1");//设置状态为有效
			BigDecimal internSco = new BigDecimal("0");
			if(status.equals("010")) {//初评
				facimityByid.setInternSco(new BigDecimal(modelGetMap.get("DL_"+type+"_RESULT")));
				internSco = facimityByid.getInternSco();
				facimityByid.setInternDate(new Date());
			}else {//复评
				facimityByid.setFinalSco(new BigDecimal(modelGetMap.get("DL_"+type+"_RESULT")));
				internSco = facimityByid.getFinalSco();
				facimityByid.setFinalDate(new Date());//设置复评时间
				Date date = new Date();
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);//设置起时间
			    //System.out.println("111111111::::"+cal.getTime());
			    cal.add(Calendar.YEAR, 1);//增加一年
			    facimityByid.setFinalDate(date);//设置复评时间
				//rating.setFdDate(fdDate);
				if(status.equals("000")) {
					//设置复评经理
					facimityByid.setFinalName(SecurityUtil.getUserName());
					facimityByid.setFdDate(cal.getTime());
				}
			}
			
			//映射增信等级
			calculate(facimityByid, status, internSco);
			return facimityByid;
		} catch (Exception e) {
			log.info("债项评级计算失败！",e);
//			jdbc.update("update ns_facility_rating set fd_vaild='0' where FD_CUST_CODE=? and fd_project_code=? and fd_id = ?", 
//					facimityByid.getProjectCode(),facimityByid.getCustCode(),facimityByid.getId());
			e.printStackTrace();
			// TODO: handle exception
			return null;
		}
		
		
		
		
		
		
	}
	
	
	/**
	 * 创建债项评级记录
	 * @param bpCode
	 * @param proCode
	 * @return
	 */
	public String creatFacilityRating(String bpCode,String proCode,String mainId,String assetsId,String creditId) {
		String sqlCust = "select fd_bp_code,fd_bp_name,FD_SCORE_TEMPLATE_ID from NS_BP_MASTER where fd_bp_code=?";
		List<Map<String,Object>> map = jdbc.queryForList(sqlCust,bpCode);
		String code = map.get(0).get("FD_BP_CODE").toString();
		String name = map.get(0).get("FD_BP_NAME").toString();
		
		String prosqlCust = "select PROJECT_NUMBER,PROJECT_NAME from NS_PRJ_PROJECT where PROJECT_NUMBER=?";
		List<Map<String,Object>> proMap = jdbc.queryForList(prosqlCust,proCode);
		String procode = proMap.get(0).get("PROJECT_NUMBER").toString();
		String proName = proMap.get(0).get("PROJECT_NAME").toString();
		//ASSETS_RATING_ID  MAIN_RATING_ID  CREDIT_RATING_ID
		String TemplateId="";
		String sql="insert into ns_facility_rating (FD_ID,FD_PROJECT_CODE,FD_PROJECT_NAME,FD_CUST_CODE,FD_CUST_NAME,ASSETS_RATING_ID,MAIN_RATING_ID,CREDIT_RATING_ID,FD_VERSION)values(?,?,?,?,?,?,?,?,?)";
		//String uuid = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		jdbc.update(sql,uuid,procode,proName,code,name,assetsId,mainId,creditId,"3.0");
		FacilityRating facilityRating = repository.findOne(uuid);
		
		return facilityRating.getId();
	}
	@Transactional
	public FacilityRating calculate(FacilityRating facilityRating,String status,BigDecimal internSco) {
		String code = "";
		try {
			code = jdbc.queryForObject(
					"select FD_CODE from Ns_main_level where fd_type='130' and fd_CODE_LOWER<=to_number(?) and fd_CODE_UPPER>to_number(?)",
					String.class, internSco, internSco);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		if(status.equals("010")) {
			facilityRating.setInternLevel(code);
		}else {
			facilityRating.setFinalLevel(code);
		}
		jdbc.update("update ns_facility_rating set fd_vaild='0' where FD_CUST_CODE=? AND FD_PROJECT_CODE=? AND fd_id <> ? ",
				facilityRating.getCustCode(),facilityRating.getProjectCode(),facilityRating.getId());
		facilityRatingService.getRepository().save(facilityRating);
		return facilityRating;
	}
	@SuppressWarnings("unlikely-arg-type")
	public void insertIndexes(List<Model> subModels,Map<String, String> paramValue,String type,
			Map<String,String> modelGetMap,String types,String Status,String failityId) throws Exception{
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
					FacilityIndex index = new FacilityIndex();
					index.setRatingId(UUID.randomUUID().toString());//~~~~~~~~~~~~~~~~~~~~设置主体id
					index.setId(UUID.randomUUID().toString());//主键id
					index.setRatingId(failityId);
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
					if(model2.getCode().equals("DL_SC_ZBJCX")) {
						index.setIndexScore(null);
					}else {
						index.setIndexScore(new BigDecimal(modelGetMap.get(model2.getCode()+"_RESULT")));
					}
					index.setFdmodel(type);
					facilityIndexService.add(index);
				}
				//存放allParameters下的指标项
				for(Parameter param : allParameters) {
					insertDxIndex(type,allParameters,name, level, model2.getId(), paramValue, modelGetMap,param,codetype,Status,failityId);
				}
			} 
				continue;
		}
	}
	
	
	public void insertDxIndex(String type,List<Parameter> allParameters,String name,String level,String modelparentid,
			Map<String, String> paramValue,Map<String,String> modelGetMap,Parameter param,String codetype,
			String Status,String failityId) throws Exception {
		if (!StringUtils.isEmpty(param.getCode()) && param.getName().contains("输入值")) {
			FacilityIndex index = new FacilityIndex();
			System.out.println("~~~~~~kankanparam:"+JSONObject.toJSON(param));
			index.setRatingId(failityId);
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
			index.setIndexValue(String.valueOf(paramValue.get(param.getCode())));//指标的输入值
			index.setLevel(level);//指标等级
			index.setParentId(parentid);//指标的parentid
			index.setIndexScore(new BigDecimal(modelGetMap.get(param.getCode()+"_RESULT")));//指标得分
			index.setFdmodel(type);
			index.setRatingStatus(Status);// 000 评级测算  010 初评  020 复评
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
			facilityIndexService.add(index);
		}
		
	}
}
