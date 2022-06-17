package gbicc.irs.main.rating.service.impl;

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

import org.apache.commons.collections.CollectionUtils;
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
import org.wsp.framework.core.util.DateUtil;
import org.wsp.framework.core.util.JacksonObjectMapper;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gbicc.aicr.system.service.FrDownloadFileService;
import com.gbicc.aicr.system.support.enums.DownloadFileEnum;
import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.customer.service.FinAccountDataService;
import gbicc.irs.customer.service.FinancialStatementsService;
import gbicc.irs.customer.service.NsBpMasterService;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.repository.MainRatingRepository;
import gbicc.irs.main.rating.service.MainRatingService;
import gbicc.irs.main.rating.service.RatingIndexService;
import gbicc.irs.main.rating.service.RatingMainStepService;
import gbicc.irs.main.rating.support.CommonConstant;
import gbicc.irs.main.rating.support.CommonUtils;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.utils.Constats;
import gbicc.irs.main.rating.vo.RatingInit;

@Service("MainRatingServiceImpl")
public class MainRatingServiceImpl extends DaoServiceImpl<MainRating, String, MainRatingRepository>
		implements MainRatingService {
	private static Log log = LogFactory.getLog(MainRatingServiceImpl.class);
	@Autowired
	private ExecutorFactoryService executorFactoryService;
	@Autowired
	private MainRatingService mainRatingService;
	@Autowired
	private RatingMainStepService ratingMainStepService;
	@Autowired
	private RatingIndexService ratingIndexService;
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	FinancialStatementsService finaStatements;
	@Autowired
	FinAccountDataService finAccountData;
	@Autowired
	NsBpMasterService bpMasterService;
	@Autowired
	private FrDownloadFileService downloadFileService;
	//private List<Model> List<model> subModels;

	// ModelDefineWrapper model = executor.getLoader().getModelByCode("ZTTY1",
	// null);

	@Override
	public String getXlsxTemplate() {
		return "classpath:/report/MainRating_zh_CN";
	}

	public MainRating internRating() {

		return null;
	}

	/**
	 * @科目指标校验
	 * @定量，定性
	 */
	@Override
	public boolean check(Map<String, String> quantitative, Map<String, String> qualitative) {
		return false;
	}

	/**
	 * @主体模型计算并返回计算结果3.0
	 * 
	 * 		rating MainRating status ratingType 010 020
	 */
	@Transactional
	@Override
	public Map<String, String> subjects(MainRating rating, String status,NsBpMaster master) {
		Map<String, String> mapResult = new HashMap<String, String>();
		mapResult.put("BP_CODE", rating.getCustCode());
		mapResult.put("BP_NAME", rating.getCustName());
		mapResult.put("MAIN_LEVEL", null);
		mapResult.put("REPORTURL", null);
		mapResult.put("RET_STATUS", Constats.FAIL_STATUS);
		mapResult.put("RET_CODE", Constats.FAIL_CODE);
		mapResult.put("MSG", "指标缺失或格式错误，计算失败！");
		String ratingId = rating.getId();
		List<Map<String, Object>> list = jdbc.queryForList("SELECT FD_CUST_CODE,FD_FIR_REP,FD_SEC_REP,FD_THI_REP FROM NS_MAIN_RATING WHERE FD_ID=?", ratingId);
		String cust = (String) list.get(0).get("FD_CUST_CODE");
		String fir = (String) list.get(0).get("FD_FIR_REP");
		String sec = (String) list.get(0).get("FD_SEC_REP");
		String thi = (String) list.get(0).get("FD_THI_REP");
		// FD_DATAITEM_CODE数据项代码 FD_END_VALUE期末值
		List<Map<String, Object>> listFir = jdbc.queryForList("SELECT FD_DATAITEM_CODE,FD_END_VALUE FROM "
				+ "NS_FIN_ACCOUNT_DATA WHERE " + "FD_REPORT_ID=(SELECT FD_ID FROM NS_FIN_STAT "
				+ "WHERE FD_DATAITEM_CODE IN ('F75','F94','F79','F82','F53','F39','F54','F55','F36','F3','F51','F106','F126','F6','F5','F49','F11','F61','F71') AND (FD_CUST_NO = ? AND FD_REPORT_BUSS_DATE=? AND FD_VAILD='1')) "
				+ "", cust, fir);
		List<Map<String, Object>> listSec = jdbc.queryForList("SELECT FD_DATAITEM_CODE,FD_END_VALUE FROM "
				+ "NS_FIN_ACCOUNT_DATA WHERE " + "FD_REPORT_ID=(SELECT FD_ID FROM NS_FIN_STAT "
				+ "WHERE FD_DATAITEM_CODE IN ('F75','F6','F5','F11') AND (FD_CUST_NO = ? AND FD_REPORT_BUSS_DATE=? AND  FD_VAILD='1')) "
				+ "", cust, sec);
		List<Map<String, Object>> listThi = jdbc.queryForList("SELECT FD_DATAITEM_CODE,FD_END_VALUE FROM "
				+ "NS_FIN_ACCOUNT_DATA WHERE " + "FD_REPORT_ID=(SELECT FD_ID FROM NS_FIN_STAT "
				+ "WHERE FD_CUST_NO = ? AND FD_REPORT_BUSS_DATE=? AND FD_DATAITEM_CODE='F75' AND FD_VAILD='1') "
				+ "", cust, thi);
		Map<String, String> reportKm = new HashMap<String, String>();
		reportKm.put(Constats.STR_F307, master.getMarketSize());
		reportKm.put(Constats.STR_F308,master.getFinancingScale());
		for (Map<String, Object> map : listFir) {
			if (Constats.STR_F79.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F180, map.get("FD_END_VALUE").toString());// 主营业务成本
			} else if (Constats.STR_F3.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F193, map.get("FD_END_VALUE").toString());// 货币资金
			} else if (Constats.STR_F106.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F152, map.get("FD_END_VALUE").toString());// 净利润
			} else if (Constats.STR_F6.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F302, map.get("FD_END_VALUE").toString());// 应收账款
			} else if (Constats.STR_F5.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F305, map.get("FD_END_VALUE").toString());// 应收票据
			} else if (Constats.STR_F49.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F306, map.get("FD_END_VALUE").toString());// 一年内到期非流动负债
			} else if (Constats.STR_F11.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F309, map.get("FD_END_VALUE").toString());// 存货
			} else if (Constats.STR_F61.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F311, map.get("FD_END_VALUE").toString());// 负债合计
			} else if (Constats.STR_F71.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F312, map.get("FD_END_VALUE").toString());// 净资产
			}else {
				reportKm.put(map.get("FD_DATAITEM_CODE").toString(), map.get("FD_END_VALUE").toString());
			}
		}

		for (Map<String, Object> map : listSec) {
			if (Constats.STR_F75.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F300, map.get("FD_END_VALUE").toString());// 上期主营业务收入
			} else if (Constats.STR_F6.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F303, map.get("FD_END_VALUE").toString());// 上期应收账款
			} else if (Constats.STR_F5.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F304, map.get("FD_END_VALUE").toString());// 上期应收票据
			} else if (Constats.STR_F11.equals(map.get("FD_DATAITEM_CODE").toString().trim())) {
				reportKm.put(Constats.STR_F310, map.get("FD_END_VALUE").toString());// 上期存货
			}
		}
		
		if(listThi!=null && listThi.size()>0) {
			reportKm.put(Constats.STR_F301, listThi.get(0).get("FD_END_VALUE").toString());// 上上期主营业务收入
		}else {
			reportKm.put(Constats.STR_F301, "0");
		}

		List<Map<String, Object>> listDx = jdbc
				.queryForList("select * from ns_non_fin where FD_MAIN_ID=? and fd_vaild='1'", ratingId);
		
		for (Map<String, Object> map : listDx) {
			try {
				reportKm.put(map.get("FD_CODE")==null?null:map.get("FD_CODE").toString(),map.get("FD_VALUE")==null?null: map.get("FD_VALUE").toString());
			} catch (Exception e) {
				log.info(this.getClass().getName()+" : "+e.getMessage());
				mapResult.put("MSG", "评级失败，定性指标" + map.get("FD_CODE").toString() + "为空！");
				e.printStackTrace();
				return mapResult;
			}
		}

		//模型类型 	承租人类型/企业类型（生产，服务，新建）
		if (rating.getType() == null) {
			mapResult.put("MSG", "主体评级模板获取失败!");
			return mapResult;
		}
		try {
			/**
			 * type		企业类型
			 * mainId 	主体评级信息ID
			 * map		包含三年财报信息、定性指标信息
			 * status	评级类型 初评 复评 010  020
			 * version	版本号
			 */
			log.info("主体评级模型计算开始！"+master.toString());
			MainRating mainRating = startTrial(rating.getType(), ratingId, reportKm, status, rating.getFdVersion());
			log.info("主体评级模型计算结束！"+master.toString());
			if (mainRating == null) {
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
			mapResult.put("MSG", "成功");
			log.info(mapResult);
			return mapResult;
		} catch (Exception e) {
			log.info(this.getClass().getName()+" : "+e.getMessage());
			e.printStackTrace();
			return mapResult;
		}

	}

	/**
	 * @查询财报科目2.0
	 */
	public Map<String, String> subjects(MainRating rating, String status) {
		String ratingId = rating.getId();

		List<Map<String, Object>> list = jdbc
				.queryForList("select fd_cust_code,FD_FIR_REP,FD_SEC_REP from ns_main_rating where fd_id=?", ratingId);
		String cust = (String) list.get(0).get("FD_CUST_CODE");
		String fir = (String) list.get(0).get("FD_FIR_REP");
		String sec = (String) list.get(0).get("FD_SEC_REP");
		List<Map<String, Object>> listFir = jdbc.queryForList("select FD_DATAITEM_CODE,FD_END_VALUE from "
				+ "ns_fin_account_data where " + "fd_report_id=(select fd_id from ns_fin_stat "
				+ "where fd_cust_no = ? and fd_report_buss_date=? and fd_vaild='1')\r\n" + "", cust, fir);
		List<Map<String, Object>> listSec = jdbc.queryForList("select FD_DATAITEM_CODE,FD_END_VALUE from "
				+ "ns_fin_account_data where " + "fd_report_id=(select fd_id from ns_fin_stat "
				+ "where fd_cust_no = ? and fd_report_buss_date=? and fd_vaild='1')\r\n" + "", cust, sec);
		Map<String, String> reportKm = new HashMap<String, String>();
		for (Map<String, Object> map : listFir) {
			reportKm.put(map.get("FD_DATAITEM_CODE").toString(), map.get("FD_END_VALUE").toString());
		}

		for (Map<String, Object> map : listSec) {
			reportKm.put(map.get("FD_DATAITEM_CODE").toString() + "_S", map.get("FD_END_VALUE").toString());
		}
		List<Map<String, Object>> listDx = jdbc
				.queryForList("select * from ns_non_fin where FD_MAIN_ID=? and fd_vaild='1'", ratingId);

		Map<String, String> mapResult = new HashMap<String, String>();
		for (Map<String, Object> map : listDx) {
			try {
				reportKm.put(map.get("FD_CODE").toString(), map.get("FD_VALUE").toString());
			} catch (Exception e) {
				mapResult.put("flag", "020");
				mapResult.put("msg", "评级失败，定性指标" + map.get("FD_CODE").toString() + "为空！");
				e.printStackTrace();
				return mapResult;
			}
		}

		if (rating.getType() == null) {
			mapResult.put("flag", "020");
			mapResult.put("msg", "主体评级模板获取失败!");
			return mapResult;
		}
		try {
			MainRating mainRating = startTrial2(rating.getType(), ratingId, reportKm, status, rating.getFdVersion());
			if (mainRating == null) {
				mapResult.put("flag", "020");
				mapResult.put("msg", "指标缺失或格式错误，计算失败！");
				return mapResult;
			}
			mapResult.put("CLIENT_NO", mainRating.getCustCode());
			mapResult.put("CLIENT_NAME", mainRating.getCustName());
			// mapResult.put("GRADE_LEVEL", mainRating.get);
			mapResult.put("PD_RATE", mainRating.getPd());
			mapResult.put("SCORE", mainRating.getSco());
			mapResult.put("REPORTURL", "/irs/mainRating/report?custNo=" + mainRating.getId());
			mapResult.put("RATINGDETIAL", "/irs/mainRating/ratingSubsidiary?custNo=" + mainRating.getId());
			// mapResult.put("SCOREDETIAL","/irs/mainRating/scoringDetails?custNo="+mainRating.getId());
			mapResult.put("flag", "010");
			mapResult.put("msg", "成功");
			return mapResult;
		} catch (Exception e) {
			mapResult.put("flag", "020");
			mapResult.put("msg", e.getMessage());
			e.printStackTrace();
			return mapResult;
		}

	}
	
	
	@Transactional
	public MainRating calculate(MainRating mainRating) {
		String type = "";
//		if (mainRating.getFdVersion().equals("1.0")) {
//			if(mainRating.getType().equals("NEW_BUILD")) {
//				type = "020";
//			}else {
//				type = "010";
//			}
//			
//		} else {
//			type = "040";
//		}
		
		if("ZTTYMX_LRX_1_FW".equals(mainRating.getType())||"ZTTYMX_LRX_2_SC".equals(mainRating.getType())) {//利润型
			type="080";
		}else if("ZTTYMX_SZX_1_FW".equals(mainRating.getType())||"ZTTYMX_SZX_2_SC".equals(mainRating.getType())) {//市值型
			type="060";
		}else if("ZTTYMX_ZSX_1_FW".equals(mainRating.getType())||"ZTTYMX_ZSX_2_SC".equals(mainRating.getType())) {//增速型
			type="070";
		}
		
		String code = "";
		try {
			code = jdbc.queryForObject(
					"select FD_CODE from Ns_main_level where fd_type=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?)",
					String.class, type, mainRating.getSco(), mainRating.getSco());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Double pd = null;
		try {
			pd = jdbc.queryForObject(
					"select FD_PD as FD_PD from ns_cfg_main_scale where FD_TYPE=? and fd_scale_level = (select fd_code from Ns_main_level where fd_type=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?))",
					Double.class, type, type, mainRating.getSco(), mainRating.getSco());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainRating.setPd(String.valueOf(pd));
		if (mainRating.getRatingStatus().equals("010")) {
			mainRating.setInitSco(mainRating.getSco());
			mainRating.setSco(null);
			mainRating.setInternLevel(code);
		} else {
			mainRating.setFinalLevel(code);
		}

		jdbc.update("update ns_main_rating set fd_vaild='0' where FD_CUST_CODE=? and fd_version='3.0'", mainRating.getCustCode());
		repository.save(mainRating);
		return mainRating;
	}

	public void testmodel( Map<String, String> paramValue,String type) {
		System.out.println("~~~~~~请求的参数map："+JSONObject.toJSON(paramValue));
		Model model;
		try {
			model = this.getModel(type);
			List<RatingMainStep> ratingSteps = new ArrayList<RatingMainStep>();
			System.out.println("~~~~~~~~~看看模型SubModels："+JSONObject.toJSONString(model.getSubModels()));
			List<Model> subModels = model.getSubModels();
			
			Map<String, String> modelGetMap = ratingResults(model.getCode(), paramValue);
			System.out.println("~~~~~~调取模型之后的map："+JSONObject.toJSON(modelGetMap));
			
		//模型解析 利润型和增速型 不需要展示经营质量，市值型经营质量需要展示领投机构的知名度
			
			//模拟一个rating
			MainRating rating = new MainRating();
			rating.setId(UUID.randomUUID().toString());
			
			List<Map<String, Object>> list = jdbc
					.queryForList("select * from  NS_R_CFG_STEPS where FD_RATINGCFG_ID='1'  order by fd_stepnum asc");
			for (int i = 0; i < list.size(); i++) {
				//QUANTITATIVE-定量信息  QUALITATIVE_EDIT-定性信息
				if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())
						|| RatingStepType.QUALITATIVE_EDIT.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
					RatingMainStep step = new RatingMainStep();
					step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));  //设置步骤序号
					step.setRatingMain(rating);												//评级对象  FD_RATEID 评级id
					step.setStepName(list.get(i).get("FD_STEPNAME").toString());			//步骤名称  fd_step_name （定量分析、定性分析、评级报告）
					step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());	//fd_step_resources 步骤资源
					
					//FD_STEP_TYPE 步骤类型 QUANTITATIVE-定量信息  QUALITATIVE_EDIT-定性信息 REPORT_INFO-评级报告
					step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));	
					step.setVaild("1");//是否有效 1 是

					// 持久化步骤实例
					try {
						step = ratingMainStepService.add(step);
					} catch (Exception e) {
						throw new RuntimeException();  
					}
					if (ratingSteps.size() >= 1) {
						// 设置上一步
						step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
						// 设置下一步
						ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
					}
					ratingSteps.add(step);
					String typeXL = "";
					if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
						typeXL = "DL";
					} else {
						typeXL = "DX";
					}
					try {
						insertIndexes(subModels, paramValue, type, modelGetMap, step, typeXL);
						//initRatingIndex(step, models, type, map, paramValue);//方法实现在426行
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException();  
					}
				} else {
					RatingMainStep step = new RatingMainStep();
					step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
					step.setRatingMain(rating);
					step.setStepName(list.get(i).get("FD_STEPNAME").toString());
					step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
					step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
					step.setVaild("1");

					// 持久化步骤实例
					try {
						step = ratingMainStepService.add(step);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException();  
					}
					
					if (ratingSteps.size() >= 1) {
						// 设置上一步
						step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
						// 设置下一步
						ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
					}
					ratingSteps.add(step);
				}
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
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
	public void insertIndexes(List<Model> subModels,Map<String, String> paramValue,String type,Map<String,String> modelGetMap,RatingMainStep step,String types) throws Exception{
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
					RatingIndex index = new RatingIndex();
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
					if(model2.getCode().equals("DL_SC_ZBJCX")) {
						index.setIndexScore(null);
					}else {
						index.setIndexScore(new BigDecimal(modelGetMap.get(model2.getCode()+"_RESULT")));
					}
//					index.setCreator("system");
//					index.setLastModifier("system");
//					index.setCreateDate(new Date());
//					index.setLastModifyDate(new Date());
					index.setFdmodel(type);
					ratingIndexService.add(index);
				}
				//存放allParameters下的指标项
				for(Parameter param : allParameters) {
					insertDxIndex(type,allParameters,name, step, level, model2.getId(), paramValue, modelGetMap,param,codetype,code);
				}
			} 
//			if(codetype.equals("ZB")) {
//				RatingIndex index = new RatingIndex();
//				index.setId(UUID.randomUUID().toString());//主键id
//				index.setRatingStep(step);//设置步骤id
//				index.setIndexType(RatingStepType.QUANTITATIVE);//指标类型定量
//				index.setIndexCategory(name);//指标分类
//				String indexname = model2.getName();
//				index.setIndexName(indexname);//指标名称
//				index.setIndexId(model2.getId());//指标id
//				index.setIndexCode(model2.getCode());//指标编号
//				//index.setIndexValue(paramValue.get(param.getCode()));
//				//index.setParentId(parentid);
//				index.setLevel(level);
//				//index.setIndexScore(new BigDecimal(modelGetMap.get(model2.getCode()+"_RESULT")));
////				index.setCreator("system");
////				index.setLastModifier("system");
////				index.setCreateDate(new Date());
////				index.setLastModifyDate(new Date());
//				index.setFdmodel(type);
//				ratingIndexService.add(index);
//				for(Parameter param : allParameters) {
//					insertDxIndex(type,allParameters,name, step, level, model2.getId(), paramValue, modelGetMap,param,codetype);
//				}
//			}
				continue;
		}
	}
	
	public void insertDxIndex(String type,List<Parameter> allParameters,String name,RatingMainStep step,String level,String modelparentid,Map<String, String> paramValue,Map<String,String> modelGetMap,Parameter param,String codetype,String code) throws Exception {
		if (!StringUtils.isEmpty(param.getCode()) && param.getName().contains("输入值")) {
			RatingIndex index = new RatingIndex();
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
			if(codetype.equals("DL")||codetype.equals("ZB")) {
				index.setIndexType(RatingStepType.QUANTITATIVE);//指标类型定量
			}else if(codetype.equals("DX")||param.getCode().equals("DX_SCSZ_F001")) {
				index.setIndexType(RatingStepType.QUALITATIVE_EDIT);//指标类型定量
			}
			
			
			String indexname = param.getName().replace("输入值", "");
			if(!indexname.equals(name)) {
				level = "2";
				parentid=modelparentid;
			}
			index.setIndexName(indexname);//指标名称
			List<ParameterOption> options = param.getOptions();
			if("DX".equals(codetype)||param.getCode().equals("DX_SCSZ_F001")) {
				for (ParameterOption parameterOption : options) {
					if(parameterOption.getInputValue().equals(paramValue.get(param.getCode()))) {
						//index.setIndexValue();//指标的输入值
						index.setIndexConfigId(parameterOption.getValue());//指标选项映射得分
					}
				}
			}else {
				if("DL_SC_JYZL".equals(code)&&!param.getCode().equals("DX_SCSZ_F001")) {
					index.setIndexConfigId(modelGetMap.get(param.getCode()+"_TEMP_RESULT_5"));//指标选项映射得分
				}else {
					index.setIndexConfigId(paramValue.get(param.getCode()));//指标选项映射得分
				}
				
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
			if("DX".equals(codetype)||param.getCode().equals("DX_SCSZ_F001")) {
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
			ratingIndexService.add(index);
		}
		
	}
	
	public MainRating startTrial2(String type, String mainId, Map<String, String> map, String status, String version) {
		// 更新定性指标
		// String json =
		// JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(map);
		Optional<MainRating> mainRating = mainRatingService.getRepository().findById(mainId);
		String mainVaildId="";
		try {
			mainVaildId=jdbc.queryForObject("select FD_ID FROM NS_MAIN_RATING where fd_cust_code=? and fd_vaild=1 and fd_version='2.0' ",String.class, mainRating.get().getCustCode());
		} catch (Exception e) {
			mainVaildId="";
		}
		try {
			MainRating mainG = mainRating.get();
			if (version.equals("2.0")) {
				type = type + "_2";
			}
			Model model = this.getModel(type);
			MainRating mainR = new MainRating();
			if (status.equals("000")) {
				mainR.setCustCode(mainRating.get().getCustCode());
				mainR.setCustName(mainRating.get().getCustName());
				mainR.setType(mainRating.get().getType());
				mainR.setVaild(true);
				mainR.setRatingStatus("000");
				mainR.setFdVersion(version);
				mainR.setFinalDate(new Date());
				mainG = repository.save(mainR);
			}

			MainRating rating = saveRatingStepAndIndex2(mainG, model, map);
			if (rating == null) {
				mainR.setVaild(false);
				mainG = repository.save(mainR);
				return null;
			}
			rating.setRatingStatus(status);
			rating.setVaild(true);
			Map<String, String> value = ratingResults2(type, map);
			rating.setQuanSco(value.get("DL_RESULT"));
			rating.setQualSco(value.get("DX_RESULT"));
			rating.setSco(value.get(type + "_RESULT"));
			rating = calculate2(rating);
			return rating;
		} catch (Exception e) {
			jdbc.update("update ns_main_rating set fd_vaild='0' where FD_CUST_CODE=? and fd_id <> ? and fd_version='2.0'", mainRating.get().getCustCode(),mainVaildId);
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, String> ratingResults2(String modelCode, Map<String, String> paramValue) {
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
	
	@Transactional
	public MainRating calculate2(MainRating mainRating) {
		String type = "";
		if (mainRating.getFdVersion().equals("1.0")) {
			if(mainRating.getType().equals("NEW_BUILD")) {
				type = "020";
			}else {
				type = "010";
			}
			
		} else {
			type = "040";
		}
		String code = "";
		try {
			code = jdbc.queryForObject(
					"select FD_CODE from Ns_main_level where fd_type=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?)",
					String.class, type, mainRating.getSco(), mainRating.getSco());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Double pd = null;
		try {
			pd = jdbc.queryForObject(
					"select FD_PD as FD_PD from ns_cfg_main_scale where FD_TYPE=? and fd_scale_level = (select fd_code from Ns_main_level where fd_type=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?))",
					Double.class, type, type, mainRating.getSco(), mainRating.getSco());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainRating.setPd(String.valueOf(pd));
		if (mainRating.getRatingStatus().equals("010")) {
			mainRating.setInitSco(mainRating.getSco());
			mainRating.setInternLevel(code);
		} else {
			mainRating.setFinalLevel(code);
		}

		jdbc.update("update ns_main_rating set fd_vaild='0' where FD_CUST_CODE=? and fd_version='2.0'", mainRating.getCustCode());
		repository.save(mainRating);
		return mainRating;
	}
	
	@Transactional
	public MainRating saveRatingStepAndIndex2(MainRating rating, Model model, Map<String, String> paramValue)
			{
		// String json = JSONObject.toJSON(paramValue).toString();
		List<RatingMainStep> ratingSteps = new ArrayList<RatingMainStep>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = ratingResults(model.getCode(), paramValue);
		} catch (Exception e) {
			mainRatingService.getRepository().deleteById(rating.getId());
			throw new RuntimeException();  
		}
		
		if (map == null || map.size() == 0) {
			
			jdbc.update("delete ns_main_rating  where fd_id = ?", rating.getId());
			return null;
		}
		// 定量，定性，评级报告

		/*
		 * if(!rating.getRatingStatus().equals("020")) {
		 * jdbc.update("update ns_rating_step set fd_vaild='0' where FD_rateid=?",
		 * rating.getId()); }
		 */
		// 获取定量定性指标
		List<Model> models = model.getSubModels();
		List<Map<String, Object>> list = jdbc
				.queryForList("select * from  NS_R_CFG_STEPS where FD_RATINGCFG_ID='1'  order by fd_stepnum asc");
		for (int i = 0; i < list.size(); i++) {
			if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())
					|| RatingStepType.QUALITATIVE_EDIT.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
				RatingMainStep step = new RatingMainStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(rating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");

				// 持久化步骤实例
				try {
					step = ratingMainStepService.add(step);
				} catch (Exception e) {
					throw new RuntimeException();  
				}
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
				String type = "";
				if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
					type = "DL";
				} else {
					type = "DX";
				}
				try {
					initRatingIndex2(step, models, type, map, paramValue);
				} catch (Exception e) {
					throw new RuntimeException();  
				}
			} else {
				RatingMainStep step = new RatingMainStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(rating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");

				// 持久化步骤实例
				try {
					step = ratingMainStepService.add(step);
				} catch (Exception e) {
					throw new RuntimeException();  
				}
				
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
			}
		}
		rating.setCurrentStepId(ratingSteps.get(0).getId());
		rating.setCurrentStep(ratingSteps.get(0));
		return rating;
	}
	
	
	/**
	 * 进入评级模型计算
	 * @param type
	 * @param mainRating
	 * @param map
	 * @param status
	 * @param version
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class)
	public MainRating startTrial(String type, MainRating mainRating, Map<String, String> map, String status, String version) throws Exception {
		log.info("**********"+mainRating.getCustCode()+"准备模型计算必要数据集："+map);
		try {
			Model model = this.getModel(type);
			if(model==null) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" :（"+mainRating.getCustCode()+"）无法获取到与之匹配的评级模型："+type);
				throw new Exception("无法获取到与之匹配的评级模型："+type+"，评级失败！");
			}
			mainRating = saveRatingStepAndRatinIndex(mainRating, model, map);
			if (mainRating == null) {
				log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" :评级模型计算失败！");
				throw new Exception("评级模型计算失败！");
			}
			mainRating.setRatingStatus(status);
			mainRating.setVaild(true);
			Map<String, String> value = ratingResults(type, map);
			mainRating.setSco(value.get("DX_"+type + "_RESULT"));
			mainRating = calculateAfter(mainRating);
			return mainRating;
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" : （"+mainRating.getCustCode()+"）评级失败！",e);
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 更新计算分数和等级
	 * @param mainRating
	 * @return
	 */
	public MainRating calculateAfter(MainRating mainRating) {
		try {
			String type = "";
			String code = "";
			if("ZTTYMX_LRX_1_FW".equals(mainRating.getType())||"ZTTYMX_LRX_2_SC".equals(mainRating.getType())) {//利润型
				type="080";
			}else if("ZTTYMX_SZX_1_FW".equals(mainRating.getType())||"ZTTYMX_SZX_2_SC".equals(mainRating.getType())) {//市值型
				type="060";
			}else if("ZTTYMX_ZSX_1_FW".equals(mainRating.getType())||"ZTTYMX_ZSX_2_SC".equals(mainRating.getType())) {//增速型
				type="070";
			}
			code = jdbc.queryForObject(
					"select FD_CODE from Ns_main_level where fd_type=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?)",
					String.class, type, mainRating.getSco(), mainRating.getSco());
			Double pd = null;
			pd = jdbc.queryForObject(
					"select FD_PD as FD_PD from ns_cfg_main_scale where FD_TYPE=? and fd_scale_level = (select fd_code from Ns_main_level where fd_type=? and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?))",
					Double.class, type, type, mainRating.getSco(), mainRating.getSco());
			mainRating.setPd(String.valueOf(pd));
			if (mainRating.getRatingStatus().equals("010")) {
				mainRating.setInitSco(mainRating.getSco());
				mainRating.setSco(null);
				mainRating.setInternLevel(code);
			} else {
				mainRating.setFinalLevel(code);
			}
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+mainRating.getCustCode()+"）计算评级等级时出现错误！",e);
			e.printStackTrace();
			throw e;
		}
		return mainRating;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public MainRating saveRatingStepAndRatinIndex(MainRating rating, Model model, Map<String, String> paramValue) throws Exception
	{
		List<RatingMainStep> ratingSteps = new ArrayList<RatingMainStep>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = ratingResults(model.getCode(), paramValue);
			log.info("**********"+rating.getCustCode()+"模型计算返回结果集 ："+JSONObject.toJSON(map));
			if (map == null || map.size() == 0) {
				log.info(this.getClass().getName()+"（"+rating.getCustCode()+"）主体评级模型计算结果为空！");
				return null;
			}
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：（"+rating.getCustCode()+"）主体评级模型计算失败 ！",e);
			return null;  
		}
		try {
			List<Model> models = model.getSubModels();
			List<Map<String, Object>> list = jdbc.queryForList("select * from  NS_R_CFG_STEPS where FD_RATINGCFG_ID='1'  order by fd_stepnum asc");
			for (int i = 0; i < list.size(); i++) {
				//QUANTITATIVE-定量信息  QUALITATIVE_EDIT-定性信息
				if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())
						|| RatingStepType.QUALITATIVE_EDIT.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
					// 持久化步骤实例
					RatingMainStep step = new RatingMainStep();
					try {
						step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));  //设置步骤序号
						step.setRatingMain(rating);												//评级对象  FD_RATEID 评级id
						step.setStepName(list.get(i).get("FD_STEPNAME").toString());			//步骤名称  fd_step_name （定量分析、定性分析、评级报告）
						step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());	//fd_step_resources 步骤资源
						
						//FD_STEP_TYPE 步骤类型 QUANTITATIVE-定量信息  QUALITATIVE_EDIT-定性信息 REPORT_INFO-评级报告
						step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));	
						step.setVaild("1");//是否有效 1 是
						step = ratingMainStepService.add(step);
						if (ratingSteps.size() >= 1) {
							// 设置上一步
							step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
							// 设置下一步
							ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
						}
						ratingSteps.add(step);
					} catch (Exception e) {
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" ：持久化评级步骤时出现错误！",e);
						e.printStackTrace();
						throw e;  
					}
					try {
						String type = "";
						if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
							type = "DL";
						} else {
							type = "DX";
						}
						insertIndexes(models, paramValue, model.getCode(), map, step, type);
					} catch (Exception e) {
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" ：持久化评级计算明细时出现错误！",e);
						e.printStackTrace();
						throw e;  
					}
				} else {
					// 持久化步骤实例
					try {
						RatingMainStep step = new RatingMainStep();
						step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
						step.setRatingMain(rating);
						step.setStepName(list.get(i).get("FD_STEPNAME").toString());
						step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
						step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
						step.setVaild("1");
						step = ratingMainStepService.add(step);
						if (ratingSteps.size() >= 1) {
							// 设置上一步
							step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
							// 设置下一步
							ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
						}
						ratingSteps.add(step);
					} catch (Exception e) {
						log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+" ：持久化评级步骤时出现错误！",e);
						e.printStackTrace();
						throw e;   
					}
				}
			}
			rating.setCurrentStepId(ratingSteps.get(0).getId());
			rating.setCurrentStep(ratingSteps.get(0));
			return rating;
		}catch(Exception e ) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：评级失败！",e);
			e.printStackTrace();
			throw e;  
		}
		
	}
	
	
	//type 模型类型 mainId 主体评级id  status  ？   version 模型版本
	@Override
	@Transactional
	public MainRating startTrial(String type, String mainId, Map<String, String> map, String status, String version) {
		System.out.println("~~~~~~~~~看看模型paramValue："+JSONObject.toJSONString(map));
		System.out.println("************************"+map);
		// 更新定性指标
		// String json =
		// JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(map);
		Optional<MainRating> mainRating = mainRatingService.getRepository().findById(mainId);
		/*String mainVaildId = "";
		try {
			mainVaildId = jdbc.queryForObject("select FD_ID FROM NS_MAIN_RATING where fd_cust_code=? and fd_vaild=1",
					String.class, mainRating.get().getCustCode());
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			mainVaildId = "";
			throw e;
		}*/
		try {
			MainRating mainG = mainRating.get();
//			if (version.equals("3.0")) {
//				type = type + "_3";
//			}
			Model model = this.getModel(type);
			MainRating mainR = new MainRating();
			//测算
			if (status.equals("000")) {
				mainR.setCustCode(mainRating.get().getCustCode());
				mainR.setCustName(mainRating.get().getCustName());
				mainR.setType(mainRating.get().getType());
				mainR.setVaild(true);
				mainR.setRatingStatus("000");
				if(StringUtils.isEmpty(version)) {
					version="3.0";
				}
				mainR.setFdVersion(version);
				Date date = new Date();
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(date);//设置起时间
			    //System.out.println("111111111::::"+cal.getTime());
			    cal.add(Calendar.YEAR, 1);//增加一年
			    mainR.setFinalDate(date);//评级日期(终评日期)
			    String formatDate = DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd");
				mainR.setfDate(formatDate);
				mainR.setFinalName(SecurityUtil.getUserName());
				mainG = repository.save(mainR);
			}

			//
			MainRating rating = saveRatingStepAndIndex(mainG, model, map);
			if (rating == null) {
				mainR.setVaild(false);
				mainG = repository.save(mainR);
				return null;
			}
			rating.setRatingStatus(status);
			rating.setVaild(true);
			Map<String, String> value = ratingResults(type, map);
			//rating.setQuanSco(value.get("DL_RESULT"));
			//rating.setQualSco(value.get("DX_RESULT"));
			rating.setSco(value.get("DX_"+type + "_RESULT"));
			rating = calculate(rating);
			return rating;
		} catch (Exception e) {
			log.info(e.getMessage());
//			jdbc.update("update ns_main_rating set fd_vaild='0' where FD_CUST_CODE=? and fd_id <> ?",
//					mainRating.get().getCustCode(), mainVaildId);
			e.printStackTrace();
			return null;
		}
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

	//276行调用
	@Transactional
	public MainRating saveRatingStepAndIndex(MainRating rating, Model model, Map<String, String> paramValue)
			{
		// String json = JSONObject.toJSON(paramValue).toString();
		List<RatingMainStep> ratingSteps = new ArrayList<RatingMainStep>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = ratingResults(model.getCode(), paramValue);
			System.out.println("~~~~~~调取模型之后的map："+JSONObject.toJSON(map));
			System.out.println("~~~~~~请求的参数map："+JSONObject.toJSON(paramValue));
		} catch (Exception e) {
//			mainRatingService.getRepository().deleteById(rating.getId());
			throw new RuntimeException();  
		}
		
		if (map == null || map.size() == 0) {
			
			jdbc.update("delete ns_main_rating  where fd_id = ?", rating.getId());
			return null;
		}
		// 定量，定性，评级报告

		/*
		 * if(!rating.getRatingStatus().equals("020")) {
		 * jdbc.update("update ns_rating_step set fd_vaild='0' where FD_rateid=?",
		 * rating.getId()); }
		 */
		// 获取定量定性指标
//		  Object resultz = JSONObject.toJSON(model);
//		  System.out.println("~~~~~~~~model:"+resultz);
		  //Map map = (Map) com.alibaba.fastjson.JSONObject.parseObject(resultz.toString());
			 //resultz = JSONObject.fromObject(map);
		List<Model> models = model.getSubModels();
//		System.out.println("~~~~~~~models:"+JSONObject.toJSON(models));
//		System.out.println(models);
		List<Map<String, Object>> list = jdbc
				.queryForList("select * from  NS_R_CFG_STEPS where FD_RATINGCFG_ID='1'  order by fd_stepnum asc");
		for (int i = 0; i < list.size(); i++) {
			//QUANTITATIVE-定量信息  QUALITATIVE_EDIT-定性信息
			if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())
					|| RatingStepType.QUALITATIVE_EDIT.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
				RatingMainStep step = new RatingMainStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));  //设置步骤序号
				step.setRatingMain(rating);												//评级对象  FD_RATEID 评级id
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());			//步骤名称  fd_step_name （定量分析、定性分析、评级报告）
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());	//fd_step_resources 步骤资源
				
				//FD_STEP_TYPE 步骤类型 QUANTITATIVE-定量信息  QUALITATIVE_EDIT-定性信息 REPORT_INFO-评级报告
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));	
				step.setVaild("1");//是否有效 1 是

				// 持久化步骤实例
				try {
					step = ratingMainStepService.add(step);
				} catch (Exception e) {
					throw new RuntimeException();  
				}
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
				String type = "";
				if (RatingStepType.QUANTITATIVE.toString().equals(list.get(i).get("FD_STEPTYPE").toString())) {
					type = "DL";
				} else {
					type = "DX";
				}
				try {
					//initRatingIndex(step, models, type, map, paramValue);//方法实现在426行
					insertIndexes(models, paramValue, model.getCode(), map, step, type);
				} catch (Exception e) {
					log.info(e.getMessage());
					e.printStackTrace();
					throw new RuntimeException();  
				}
			} else {
				RatingMainStep step = new RatingMainStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(rating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");

				// 持久化步骤实例
				try {
					step = ratingMainStepService.add(step);
				} catch (Exception e) {
					throw new RuntimeException();  
				}
				
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
			}
		}
		rating.setCurrentStepId(ratingSteps.get(0).getId());
		rating.setCurrentStep(ratingSteps.get(0));
		return rating;
	}
	
	private void initRatingIndex2(RatingMainStep step, List<Model> models, String type, Map<String, String> map,
			Map<String, String> paramValue) throws Exception {
		RatingIndex index;
		jdbc.update("delete ns_rating_indexes where FD_STEPID=?", step.getId());
		if (!CollectionUtils.isEmpty(models)) {
			// 模型下的定性和定量
			for (Model m1 : models) {
				if (type.equals(m1.getCode())) {
					// 设置步骤对应的modelId
					step.setModelId(m1.getId());
					// 定量或者定性下的指标大类
					for (Model m2 : m1.getSubModels()) {
						index = this.saveRatingIndex(step, m1.getName(), m2, CommonConstant.MODEL_LEVEL_TWO, null, map,
								paramValue);
						// 大类下的每个指标
						for (Model m3 : m2.getSubModels()) {
							this.saveRatingIndex(step, m2.getName(), m3, CommonConstant.MODEL_LEVEL_THREE,
									index.getId(), map, paramValue);
						}
					}
				} else if ("YRZL".equals(m1.getCode()) && type.equals("DL")) {
					// 设置步骤对应的modelId
					step.setModelId(m1.getId());
					// 定量或者定性下的指标大类
					for (Model m2 : m1.getSubModels()) {
						index = this.saveRatingIndex(step, m1.getName(), m2, CommonConstant.MODEL_LEVEL_THREE, null,
								map, paramValue);
					}
				}
			}
		}
	}

	/**
	 * 初始化指标
	 * 
	 * @param step 设置的步骤
	 * @param models
	 * @param type  dl/dx
	 * @param map	
	 * @param paramValue
	 * @throws Exception
	 * 379行调用
	 */
	private void initRatingIndex(RatingMainStep step, List<Model> models, String type, Map<String, String> map,
			Map<String, String> paramValue) throws Exception {
		RatingIndex index;
		jdbc.update("delete ns_rating_indexes where FD_STEPID=?", step.getId());
		if (!CollectionUtils.isEmpty(models)) {
			// 模型下的定性和定量
			for (Model m1 : models) {
				if (type.equals(m1.getCode())) {
					// 设置步骤对应的modelId
					step.setModelId(m1.getId());
					// 定量或者定性下的指标大类
					for (Model m2 : m1.getSubModels()) {
						
						index = this.saveRatingIndex(step, m1.getName(), m2, CommonConstant.MODEL_LEVEL_TWO, null, map, //方法实现在477行
								paramValue);
						// 大类下的每个指标
						for (Model m3 : m2.getSubModels()) {
							this.saveRatingIndex(step, m2.getName(), m3, CommonConstant.MODEL_LEVEL_THREE,
									index.getId(), map, paramValue);
						}
					}
				} else if ("YRZL".equals(m1.getCode()) && type.equals("DL")) {
					// 设置步骤对应的modelId
					step.setModelId(m1.getId());
					// 定量或者定性下的指标大类
					for (Model m2 : m1.getSubModels()) {
						index = this.saveRatingIndex(step, m1.getName(), m2, CommonConstant.MODEL_LEVEL_THREE, null,
								map, paramValue);
					}
				}
			}
		}
	}

	/**
	 * 初始化指标
	 * 
	 * @param step          步骤
	 * @param IndexCategory 指标分类
	 * @param model         当前模型
	 * @param level         当前模型层级
	 * @param parentId      指标父ID
	 * @param map
	 * @param paramValue
	 * @return
	 * @throws Exception
	 * 442行有调用
	 */
	private RatingIndex saveRatingIndex(RatingMainStep step, String IndexCategory, Model model, String level,
			String parentId, Map<String, String> map, Map<String, String> paramValue) throws Exception {
		RatingIndex index = new RatingIndex();
		// 指标ID
		index.setRatingStep(step);
		index.setIndexId(model.getId());
		// 指标名称
		index.setIndexName(model.getName());
		// 评级步骤
		index.setRatingStep(step);
		// 指标类型
		index.setIndexType(step.getStepType());
		// 指标分类
		index.setIndexCategory(IndexCategory);

		// 层级
		index.setLevel(level);
		if (CommonConstant.MODEL_LEVEL_THREE.equals(level)) {
			// 父级ID
			index.setParentId(parentId);
		}
		for (Parameter parameter : model.getParameters()) {
			if (parameter.getCode().contains("RESULT")) {
				//先通过parameter获取到code（XJZT005_RESULT），然后在通过模型计算出来的map来获取对应code的分数
				BigDecimal bigdecimal = new BigDecimal(map.get(parameter.getCode()));//获取模型给指标打的分数
				
				index.setIndexScore(bigdecimal);//设置指标得分 FD_SCORE
				
				//通过parameter获取到code（XJZT005_RESULT）将code替换成（XJZT005），然后通过前台传入的参数map来获取（XJZT005）对应的值，
				index.setIndexValue(paramValue.get(parameter.getCode().replace("_RESULT", "")));//获取指标的值
				
				//如果选择的指标值为空，则将指标的值设置为模型计算的map里面的
				if (!org.apache.commons.lang3.StringUtils.isNotBlank(index.getIndexValue())) {
					index.setIndexValue(map.get(parameter.getCode().replace("_RESULT", "_TEMP_RESULT_5")));
				}
			}

			if (!StringUtils.isEmpty(parameter.getName()) && parameter.getName().contains("输入值")) {
				// 指标code
				index.setIndexCode(parameter.getCode());
			} else {
				index.setIndexCode(parameter.getCode().replace("_RESULT", ""));
			}
			// 权重
			if (!StringUtils.isEmpty(parameter.getCode()) && parameter.getCode().contains("WEIGHT")) {
				index.setIndexWeight(CommonUtils.getString(parameter.getDefaultValue()));
			}
			if (!StringUtils.isEmpty(parameter.getCode()) && parameter.getType().toString().equals("IN_OPTION")) {
				StringBuffer dataStr = new StringBuffer("{");
				for (ParameterOption po : parameter.getOptions()) {
					if (paramValue.get(parameter.getCode()).equals(po.getInputValue())) {
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
		}
		ratingIndexService.add(index);
		return index;
	}


	/**
	 * 获取模型
	 * 
	 * @param modelCode
	 * @return
	 * @throws Exception
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
			e.printStackTrace();
			throw new Exception("模型引擎未配置！");
		}
		return null;
	}
	
	@Override
	public ResponseWrapper<RatingInit> parameterQueryNew(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception {
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
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("employee"), "FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("finalName"), "FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("leaseOrganization"), "init.unit_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType1"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "rating.fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部

		List<String> list =CommonUtils.getAllRoleIds();
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " master.FD_EMPLOYEE_ID"));// 业务部
		}
		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "FD_FINAL_DATE"));

		String sql = "select \r\n" + 
				"rating.fd_id,\r\n" + 
				"rating.fd_final_name as finalName,\r\n" + 
				"rating.fd_vaild,\r\n" + 
				"rating.fd_final_date,\r\n" + 
				"rating.fd_rating_status,\r\n" + 
				"master.FD_BP_NAME as custName,\r\n" + 
				"master.FD_BP_CODE as custCode,\r\n" + 
				"btype.FD_DESCRIPTION as description，\r\n" + 
				"FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n" + 
				"org.fd_name as orgName,\r\n" + 
				"master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" + 
				"master.FD_EMPLOYEE_ID as employeeId,\r\n" + 
				"fruser.fd_username\r\n" + 
				" From ns_bp_master master \r\n" + 
				" left join (select * from ns_main_rating where fd_vaild = '1' and fd_version = '3.0' ) rating on rating.fd_cust_code = master.fd_bp_code\r\n" + 
				"LEFT JOIN fr_aa_user fruser ON master.fd_employee_id = fruser.fd_id\r\n" + 
				"left join FR_AA_USER_PARTITION init on rating.FD_INTERN_CODE = init.employee_id  "+
				"left join NS_BP_TYPE btype on master.fd_bp_type = btype.fd_bp_type "
				+ "LEFT JOIN fr_aa_org org ON master.FD_LEASE_ORGANIZATION = org.fd_id where 1=1\r\n" + sqlQuery+
				"and master.FD_bp_type <> 'GUTA_NP' ORDER BY\r\n" + 
				"	rating.FD_FINAL_DATE DESC nulls last ";
		System.out.println("~~~sql:"+sql);
		return getResult(sql, page, rows);
	}

	@Override
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception {
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
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("employee"), "FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("finalName"), "FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("leaseOrganization"), "init.unit_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType1"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部

		List<String> list =CommonUtils.getAllRoleIds();
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " master.FD_EMPLOYEE_ID"));// 业务部
		}
		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "FD_FINAL_DATE"));

		String sql = "select \r\n" + "master.FD_BP_NAME as custName,\r\n" 
		+ "master.FD_BP_CODE as custCode,\r\n"
				+ "master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" 
				+ "FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n"
				+ "FD_HIGH_PRECISION as highPrecision,\r\n" 
				+ "FD_ENTERPRISE_HONOR as enterpriseHonor,org.fd_name as orgName,\r\n"
				+ "FD_ECONOMIC_TYPE as economic,\r\n" 
				+ "rating.FD_ID as id,\r\n" + "rating.FD_TYPE as type,\r\n"
				+ "scInit.fd_code as internLevel,master.FD_EMPLOYEE_ID as employeeId,\r\n" 
				+ "sc.fd_code as finalLevel, rating.FD_PD as pd,"
				+ "czr.fd_description as description, "
				+ "FD_RATING_STATUS as ratingStatus,\r\n" 
				+ "to_char(FD_FINAL_DATE,'yyyy-MM-dd') as finalDate,"
				+ "FD_FINAL_NAME as finalName,rating.fd_version as version\r\n"
				+ "from NS_BP_MASTER master  " 
				+ "left join ns_main_rating rating "
				+ "on rating.FD_CUST_CODE = master.FD_BP_CODE and rating.fd_vaild<>'0'  "
				+ "left join FR_AA_USER_PARTITION init on rating.FD_INTERN_CODE = init.employee_id  "
				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id "
				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
				+ "left join ns_main_level sc on rating.fd_sco>sc.fd_code_lower and rating.fd_sco<=sc.fd_code_upper and sc.fd_type='040' "
				+ "left join NS_BP_TYPE czr on czr.fd_bp_type = master.fd_bp_category "
				+ "left join ns_main_level sc on to_number(rating.fd_sco)>to_number(sc.fd_code_lower) and to_number(rating.fd_sco)<=to_number(sc.fd_code_upper) and sc.fd_type='040' " 
				+ "left join ns_main_level scInit on to_number(rating.FD_INITSCO)>to_number(scInit.fd_code_lower) and to_number(rating.FD_INITSCO)<=to_number(scInit.fd_code_upper) and scInit.fd_type='040' " 				+ "where  1=1 and master.FD_bp_type <>'GUTA_NP' " + sqlQuery
				+ " order by rating.FD_FINAL_DATE desc nulls last ";
		System.out.println("~~~sql:"+sql);
		return getResult(sql, page, rows);
	}

	@Override
	public ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception {
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
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("employee"), "fruser.FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("finalName"), "FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("leaseOrganization"), "init.unit_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType"), "rating.FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType1"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部
		
		if(StringUtils.isEmpty(request.getParameter("fdVaild"))) {
			sqlQuery.append(CommonUtils.sqlPar("1", "fd_vaild"));// 
		}
		if(!StringUtils.isEmpty(request.getParameter("finallevel"))) {
			sqlQuery.append(" and rating.FD_FINAL_LEVEL is not null ");
		}

		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "rating.FD_FINAL_DATE"));
		String join="and  rating.fd_vaild<>'0' ";
		if(org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("vailds"))) {
			if(request.getParameter("vailds").equals("all")) {
				join="";
			}
		}
		List<String> list =CommonUtils.getAllRoleIds();
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " master.FD_EMPLOYEE_ID"));// 业务部
		}
//		String sql = "select \r\n" + "master.FD_BP_NAME as custName,\r\n" + "master.FD_BP_CODE as custCode,\r\n"
//				+ "master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" + "FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n"
//				+ "FD_HIGH_PRECISION as highPrecision,\r\n" 
//				+ "FD_ENTERPRISE_HONOR as enterpriseHonor,\r\n"
//				+ "FD_ECONOMIC_TYPE as economic,\r\n" + "rating.FD_ID as id,\r\n" 
//				+ "rating.FD_TYPE as type,rating.fd_final_Name as finalName,\r\n"
//				+ "scInit.fd_code as internLevel,"
//				+ "org.fd_name as orgName,"
//				+ "master.FD_EMPLOYEE_ID as employeeId,\r\n" 
//				+ "sc.fd_code as finalLevel, rating.FD_PD as pd,"
//				+ "czr.fd_description as description, "
//				+ "FD_RATING_STATUS as ratingStatus,\r\n" 
//				+ "to_char(FD_FINAL_DATE,'yyyy-MM-dd') as finalDate,rating.fd_version as version,"
//				+ "FD_INITSCO as initSco,FD_SCO AS finaSco,SEGMENT_INDUSTRY_TOP as segmentIndustryTop,type.CODE_NAME as enterpriseScale,FD_FOUNDED_DATE as foundenDate ,F_DATE as fDate,treat_N as treatN\r\n"
//				+ "from ns_main_rating rating   " + "inner join  NS_BP_MASTER master "
//				+ "on rating.FD_CUST_CODE = master.FD_BP_CODE "+join
//				+ "left join FR_AA_USER_PARTITION init on master.fd_employee_id = init.employee_id  "
//				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id "
//				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
//				+ "left join FD_ES_AL_ST_TYPE type on master.FD_ENTERPRISE_SCALE = type.code and type.type='ENTERPRISE_SCALE_TYPE' "
//				+ "left join ns_main_level sc on to_number(rating.fd_sco)>to_number(sc.fd_code_lower) and to_number(rating.fd_sco)<=to_number(sc.fd_code_upper) and sc.fd_type='040' " 
//				+ "left join ns_main_level scInit on to_number(rating.FD_INITSCO)>to_number(scInit.fd_code_lower) and to_number(rating.FD_INITSCO)<=to_number(scInit.fd_code_upper) and scInit.fd_type='040' " 
//				+ "left join NS_BP_TYPE czr on czr.fd_bp_type = master.fd_bp_category "
//				+ "where  1=1 and master.FD_bp_type <>'GUTA_NP'  and rating.fd_version='3.0' and rating.fd_vaild='1' " + sqlQuery
//				+ " order by rating.FD_FINAL_DATE desc nulls last ";
		
		String sql="select rating.fd_id as id,master.FD_BP_NAME as custName,master.FD_BP_CODE as custCode, \r\n" + 
				"org.fd_name as orgName,\r\n" +
				"czr.fd_description as description,"+
				"master.FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n" + 
				"master.FD_EMPLOYEE_ID as employeeId,\r\n" + 
				"rating.FD_FINAL_NAME as finalName,\r\n" + 
				"rating.FD_SCO as finaSco,\r\n" + 
				"rating.FD_FINAL_LEVEL as finallevel,\r\n" + 
				"rating.FD_INTERN_LEVEL as internlevel,\r\n" + 
				"rating.FD_INITSCO as initSco,\r\n" + 
				"rating.FD_TYPE as type,\r\n" + 
				"to_char(rating.FD_FINAL_DATE,'yyyy-MM-dd') as finaldate,\r\n" + 
				"to_char(to_date(f_date,'yyyy-MM-dd hh24:mi:ss'),'yyyy-MM-dd') as fDate,\r\n"+
				"rating.FD_RATING_STATUS ratingstatus,rating.FD_PD as pd,\r\n" + 
				"rating.FD_VERSION as version\r\n" + 
				"from ns_main_rating rating left join ns_bp_master master on rating.FD_CUST_CODE = master.FD_BP_CODE\r\n" + 
				"left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id \r\n" + 
				"left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id \r\n" + 
				"left join FR_AA_USER_PARTITION init on master.fd_employee_id = init.employee_id "+
				"left join NS_BP_TYPE czr on czr.fd_bp_type = master.fd_bp_category where rating.FD_VERSION='3.0' \r\n" + 
				sqlQuery +" order by rating.FD_FINAL_DATE desc nulls last ";
		System.out.println("~~~~~~~~sql:"+sql);
		return getResult(sql, page, rows);
	}

	@Override
	public ResponseWrapper<RatingInit> parameterQueryHistory2(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception {
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
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("employee"), "FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("finalName"), "FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("leaseOrganization"), "init.unit_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType"), "rating.FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType1"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部
		sqlQuery.append(CommonUtils.sqlNotPar(request.getParameter("ratingStatus"), "fd_rating_status"));// 评级状态

		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "FD_FINAL_DATE"));
		String join="and  rating.fd_vaild<>'0'  ";
		if(org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("vailds"))) {
			if(request.getParameter("vailds").equals("all")) {
				join="";
			}
		}
		List<String> list =CommonUtils.getAllRoleIds();
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " master.FD_EMPLOYEE_ID"));// 业务部
		}
		String sql = "select \r\n" + "master.FD_BP_NAME as custName,\r\n" + "master.FD_BP_CODE as custCode,\r\n"
				+ "master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" + "FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n"
				+ "FD_HIGH_PRECISION as highPrecision,\r\n" 
				+ "FD_ENTERPRISE_HONOR as enterpriseHonor,\r\n"
				+ "FD_ECONOMIC_TYPE as economic,\r\n" + "rating.FD_ID as id,\r\n" 
				+ "rating.FD_TYPE as type,rating.fd_final_Name as finalName,\r\n"
				+ "scInit.fd_code as internLevel,"
				+ "org.fd_name as orgName,"
				+ "master.FD_EMPLOYEE_ID as employeeId,\r\n" 
				+ "sc.fd_code as finalLevel, rating.FD_PD as pd,"
				+ "czr.fd_description as description, "
				+ "FD_RATING_STATUS as ratingStatus,\r\n"
				+ "to_char(to_date(f_date,'yyyy-MM-dd hh24:mi:ss'),'yyyy-MM-dd') as fDate,\r\n"
				+ "to_char(FD_FINAL_DATE,'yyyy-MM-dd') as finalDate,rating.fd_version as version,"
				+ "FD_INITSCO as initSco,FD_SCO AS finaSco,SEGMENT_INDUSTRY_TOP as segmentIndustryTop,type.CODE_NAME as enterpriseScale,FD_FOUNDED_DATE as foundenDate ,treat_N as treatN\r\n"
				+ "from ns_main_rating rating   " + "inner join  NS_BP_MASTER master "
				+ "on rating.FD_CUST_CODE = master.FD_BP_CODE "+join
				+ "left join FR_AA_USER_PARTITION init on master.fd_employee_id = init.employee_id  "
				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id "
				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
				+ "left join FD_ES_AL_ST_TYPE type on master.FD_ENTERPRISE_SCALE = type.code and type.type='ENTERPRISE_SCALE_TYPE' "
				+ "left join ns_main_level sc on to_number(rating.fd_sco)>to_number(sc.fd_code_lower) and to_number(rating.fd_sco)<=to_number(sc.fd_code_upper) and sc.fd_type='040' " 
				+ "left join ns_main_level scInit on to_number(rating.FD_INITSCO)>to_number(scInit.fd_code_lower) and to_number(rating.FD_INITSCO)<=to_number(scInit.fd_code_upper) and scInit.fd_type='040' " 
				+ "left join NS_BP_TYPE czr on czr.fd_bp_type = master.fd_bp_category "
				+ "where  1=1 and master.FD_bp_type <>'GUTA_NP'  and rating.fd_version <> '3.0' " + sqlQuery
				+ " order by rating.FD_FINAL_DATE desc nulls last ";
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

	public Long count(String sql) {
		sql = "select count(*) from(" + sql + ")";
		Long count = jdbc.queryForObject(sql, Long.class);
		return count;
	}

	public String page(Integer start, Integer end, String sql) {
		sql = "select * from (select t.*,rownum as rn from (" + sql + ") t where rownum<=" + end + ") where rn>"
				+ start;
		return sql;
	}

	public static <T> T parseBean(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public Map<String, Object> queryCount(String custName, String fdType, String industry, String highprecision,
			String employee, String finalName, String leaseOrganization, String method, String date1, String date2,String vailds) {

		String sql = getExportSql(custName, fdType, industry, highprecision, employee, finalName, leaseOrganization,
				method, date1, date2,vailds);

		Long count = count(sql);

		if (count == null || count <= 0) {
			return AppUtil.getMap(false, "该查询条件无结果\r\n请确认后操作");
		}

		return AppUtil.getMap(true);
	}

	private String getExportSql(String custName, String fdType, String industry, String highprecision, String employee,
			String finalName, String leaseOrganization, String method, String date1, String date2,String vailds) {

		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append(CommonUtils.sqlFuzzyPar(custName, "master.FD_BP_NAME"));
		sqlQuery.append(CommonUtils.sqlPar(fdType, "rating.FD_TYPE"));
		sqlQuery.append(CommonUtils.sqlPar(industry, "master.FD_SEGMENT_INDUSTRY"));
		sqlQuery.append(CommonUtils.sqlPar(highprecision, "master.FD_HIGH_PRECISION"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(employee, "FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(finalName, "rating.FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlPar(leaseOrganization, "init.unit_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(method, "rating.fd_rating_status"));
		sqlQuery.append(CommonUtils.sqlParDate(date1, date2, "FD_FINAL_DATE"));
		
		String join="and  rating.fd_vaild<>'0' ";
		if(org.apache.commons.lang3.StringUtils.isNotBlank(vailds)) {
			if(vailds.equals("all")) {
				join="";
			}
		}

		String sql = "select\r\n" + "master.FD_BP_NAME as \"承租人名称\",\r\n" + "master.FD_BP_CODE as \"承租人编号\",\r\n"
				+ "msg2.fd_message as \"主体评级模版(承租人类型)\",\r\n" + "highBj1.value_name as \"国标行业\",\r\n"
				+ "highBj2.value_name as \"高精尖产业分类\",\r\n" 
				+ "userz.fd_username as \"业务经理\",\r\n" 
				+ "org.fd_name as \"业务部\","
				+ "scInit.fd_code as \"初评等级\",\r\n"
				//+ "FD_INITSCO as \"初评得分\","
				+ "case when rating.FD_INITSCO is null then '' else to_char(rating.FD_INITSCO,'fm9999999990.00') end as \"初评得分\",\r\n"
				+ "rating.fd_final_Name as \"评审经理\", "
				+ "sc.fd_code as \"复核等级\",\r\n"
				+ "case when rating.FD_SCO is null then '' else to_char(rating.FD_SCO,'fm9999999990.00') end as \"复核得分\",\r\n"
				+ "case when rating.fd_pd is null then '' else to_char(rating.FD_PD*100,'fm9999999990.00') || '%' end as \"违约概率\",\r\n"
				+ "(case when fd_rating_status='010' then '业务经理发起-初评' when fd_rating_status='020' then '评审经理复核-复评' when fd_rating_status='000' then '评级测算' end)as \"评级状态\", " 
				+ "to_char(rating.FD_FINAL_DATE,'yyyy-MM-dd') as \"评级生效日期\"\r\n"
				+ "from ns_main_rating rating\r\n"
				+ "inner join  NS_BP_MASTER master on rating.FD_CUST_CODE = master.FD_BP_CODE "+join
				+ "left join FR_AA_USER_PARTITION init on rating.FD_INTERN_CODE = init.employee_id\r\n"
				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id "
				+ "left join fr_aa_user userz on to_char(init.employee_id) = to_char(userz.FD_ID)  "
				+ "left join fr_sys_i18n_message msg2 on replace(msg2.fd_code,'dictionary.RatingTemplate.','')=rating.fd_type \r\n"
				+ "left join NS_ZGC_HIGH_BJ highBj1 on highBj1.value_code=master.FD_SEGMENT_INDUSTRY and highBj1.flag='01'\r\n"
				+ "left join NS_ZGC_HIGH_BJ highBj2 on highBj2.value_code=master.FD_HIGH_PRECISION and highBj2.flag='02'\r\n"
				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
				+ "left join ns_main_level sc on to_number(rating.fd_sco)>sc.fd_code_lower and to_number(rating.fd_sco)<=sc.fd_code_upper and sc.fd_type='040' "
				+ "left join ns_main_level scInit on to_number(rating.FD_INITSCO)>scInit.fd_code_lower and to_number(rating.FD_INITSCO)<=scInit.fd_code_upper and scInit.fd_type='040' " 
				+ "where  1=1 and master.FD_bp_type <>'GUTA_NP'\r\n" + sqlQuery
				+ " order by rating.FD_FINAL_DATE desc nulls last ";
		return sql;
	}

	@Override
	public Map<String, Object> exportData(String loginName,String custName, String fdType, String industry, String highprecision,
			String employee, String finalName, String leaseOrganization, String method, String date1, String date2,
			String fileName,String vailds) {

		String sql = getExportSql(custName, fdType, industry, highprecision, employee, finalName, leaseOrganization,
				method, date1, date2,vailds);
		if (fileName.endsWith(DownloadFileEnum.EXCEL.getValue())) {
			fileName = fileName.substring(0, fileName.lastIndexOf(DownloadFileEnum.EXCEL.getValue()));
		}
		downloadFileService.exportDataToExcel(loginName,sql, fileName + DownloadFileEnum.EXCEL.getValue());

		return AppUtil.getMap(true);
	}
	

}
