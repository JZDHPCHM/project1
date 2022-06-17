package gbicc.irs.debtRating.debt.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.wsp.engine.model.core.enums.ParameterType;
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
import com.gbicc.aicr.system.service.FrDownloadFileService;
import com.gbicc.aicr.system.support.enums.DownloadFileEnum;
import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.debtRating.debt.entity.CreditRating;
import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.entity.NsPrjProject;
import gbicc.irs.debtRating.debt.entity.RatingDebtIndex;
import gbicc.irs.debtRating.debt.entity.RatingDebtStep;
import gbicc.irs.debtRating.debt.repository.DebtRatingRepository;
import gbicc.irs.debtRating.debt.service.DebtIndexService;
import gbicc.irs.debtRating.debt.service.DebtRatingService;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.support.CommonConstant;
import gbicc.irs.main.rating.support.CommonUtils;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.utils.Tools;
import gbicc.irs.main.rating.vo.RatingInit;

@Service
public class DebtRatingServiceImpl extends DaoServiceImpl<DebtRating, String, DebtRatingRepository>
		implements DebtRatingService {
	
	
	@Autowired
	private DebtIndexService ratingIndexService;
	@Autowired
	private ExecutorFactoryService executorFactoryService;
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	DebtRatingStepServiceImpl ratingMainStepService;
	@Autowired
	private FrDownloadFileService downloadFileService;
	
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
			throw new Exception("模型引擎未配置！");
		}
		return null;
	}

	@Override
	public DebtRating startTrial(String type, DebtRating debt, Map<String, String> map, String status) {
		DebtRating mainG = null;
		try {
			Model model = this.getModel(type);
			String codeBS = "";
			try {
				codeBS = jdbc.queryForObject(
						"select FD_CODE from Ns_main_level where fd_type='030' and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?)",
						String.class, map.get("ZXPJ_RESULT"), map.get("ZXPJ_RESULT"));
			} catch (Exception e) {
				codeBS = "Ⅵ";
				e.printStackTrace();
			}
			if (status.equals("010")) {
				jdbc.update("update Ns_debt_Temp set FD_STATUS='0' where fd_debt_id=?",debt.getId());
				jdbc.update("insert into Ns_debt_Temp values(?,?,?)",debt.getId(), debt.getProId(),"1");
				jdbc.update("update ns_debt_rating set fd_vaild='0',FD_RATING_VAILD='0' where fd_project_code=? and fd_id <>?",
						debt.getProjectCode(),debt.getId());
				debt.setVaild(true);
				debt.setRatingVaild("1");
				debt.setRatingStatus("010");
				debt.setFdVersion("1.0");
				debt.setFinalDate(new Date());
				debt.setInternBs(map.get("ZXPJ_RESULT"));
				debt.setPd(codeBS);
				debt.setInternLevel(codeBS);
			}else if (status.equals("020")) {
				jdbc.update("update Ns_debt_Temp set FD_STATUS='0' where fd_debt_id=? and FD_STATUS='2'",debt.getId());
				jdbc.update("insert into Ns_debt_Temp values(?,?,?)",debt.getId(), debt.getProId(),"2");
				jdbc.update("update ns_debt_rating set fd_vaild='0',FD_RATING_VAILD='0' where fd_project_code=? and fd_id <>?",
						debt.getProjectCode(),debt.getId());
				debt.setVaild(true);
				debt.setRatingStatus("020");
				debt.setFdVersion("1.0");
				debt.setRatingVaild("1");
				debt.setFinalDate(new Date());
				debt.setPd(codeBS);
				debt.setFinalLevel(codeBS);
				debt.setFinalBs(map.get("ZXPJ_RESULT"));
			}else {
				jdbc.update("update Ns_debt_Temp set FD_STATUS='0' where fd_debt_id=?",debt.getId());
				jdbc.update("insert into Ns_debt_Temp values(?,?,?)",debt.getId(), debt.getProId(),"1");
				jdbc.update("update ns_debt_rating set fd_vaild='0' where fd_project_code=? and fd_id <>?",
						debt.getProjectCode(),debt.getId());
				debt.setVaild(true);
				debt.setRatingStatus("000");
				debt.setFdVersion("1.0");
				debt.setFinalDate(new Date());
				debt.setInternBs(map.get("ZXPJ_RESULT"));
				debt.setPd(codeBS);
				debt.setInternLevel(codeBS);
			}
			debt.setXjlSco(map.get("BS001_RESULT"));
			debt.setZlwSco(map.get("BS002_RESULT"));
			debt.setZxcsSco(map.get("BS003_RESULT"));
			debt.setKhxySco(map.get("BS004_RESULT"));
			debt.setSco(map.get("ZXPJ_RESULT"));
			mainG = repository.save(debt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mainG;
	}

	/*
	 * @Override public DebtRating startTrial(String type,String debtId, Map<String,
	 * String> map,String status) { DebtRating mainG = new DebtRating(); DebtRating
	 * rating = new DebtRating(); try {
	 * debtId="cd05f811-9782-4c1c-83cb-e4dbb041c582"; Optional<DebtRating>
	 * debtRating = repository.findById(debtId); mainG = debtRating.get(); Model
	 * model = this.getModel(type); DebtRating debtR = new DebtRating();
	 * if(status.equals("000")) {
	 * jdbc.update("update ns_debt_rating set fd_vaild='0' where fd_project_code=?",
	 * debtRating.get().getProjectCode());
	 * debtR.setCustCode(debtRating.get().getCustCode());
	 * debtR.setCustName(debtRating.get().getCustName());
	 * debtR.setProjectCode(debtRating.get().getProjectCode());
	 * debtR.setProjectName(debtRating.get().getProjectName());
	 * debtR.setType(debtRating.get().getType()); debtR.setVaild(true);
	 * debtR.setRatingStatus("000"); debtR.setFdVersion("1.0");
	 * debtR.setFinalDate(new Date()); mainG=repository.save(debtR); } Map<String,
	 * String> ZXPJ01 = ratingResults("ZXPJ01",map); ZXPJ01.get("CZX_RESULT");
	 * map.put("CZX001", ZXPJ01.get("CZX_RESULT")); String json =
	 * JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(map);
	 * 
	 * Map<String, String> mapResult = ratingResults("ZXPJ",map); // json =
	 * JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(mapResult);
	 * model = this.getModel(type); rating = saveRatingStepAndIndex(mainG, model,
	 * map); rating.setXjlSco(mapResult.get("BS001_RESULT"));
	 * rating.setZlwSco(mapResult.get("BS002_RESULT"));
	 * rating.setZxcsSco(mapResult.get("BS003_RESULT"));
	 * rating.setKhxySco(mapResult.get("BS004_RESULT"));
	 * rating.setSco(mapResult.get("ZXPJ_RESULT")); String codeBS =""; try { codeBS
	 * = jdbc.
	 * queryForObject("select FD_CODE from Ns_main_level where fd_type='020' and fd_CODE_LOWER<to_number(?) and fd_CODE_UPPER>=to_number(?)"
	 * , String.class,mapResult.get("ZXPJ_RESULT"),mapResult.get("ZXPJ_RESULT")); }
	 * catch (Exception e) { codeBS="Ⅵ"; e.printStackTrace(); }
	 * rating.setPd(codeBS); rating.setFinalLevel(codeBS);
	 * 
	 * if(status.equals("010")) { rating.setInternBs(mapResult.get("ZXPJ_RESULT"));
	 * }else { rating.setFinalBs(mapResult.get("ZXPJ_RESULT")); }
	 * 
	 * czResult(rating,map);
	 * 
	 * 
	 * } catch (Exception e) { repository.deleteById(mainG.getId()); DebtRating
	 * ratingz = repository.findById(debtId).get(); ratingz.setVaild(true);
	 * repository.save(ratingz); e.printStackTrace(); }
	 * 
	 * return rating; }
	 */

	public void czResult(DebtRating rating, Map<String, String> map) {
		try {
			Model modelCzx = this.getModel("ZXPJ01");
			List<Model> models = modelCzx.getSubModels().get(0).getSubModels();
			for (Model ModelChild : models) {
				RatingDebtIndex debtIndex = new RatingDebtIndex();

				if (ModelChild.getCode().indexOf("CZ005") > -1 || ModelChild.getCode().indexOf("CZ004") > -1) {
					debtIndex.setRatingStep(rating.getCurrentStep());
					//debtIndex.setIndexType(RatingStepType.INIT);
					debtIndex.setIndexCategory("债项评级要素");
					debtIndex.setRatingStep(rating.getCurrentStep());
				} else {
					//debtIndex.setIndexType(RatingStepType.BS001);
					debtIndex.setIndexCategory("筹资性现金流保障倍数");
					String id = jdbc.queryForObject(
							"select fd_id from ns_debt_indexes where fd_stepid = ? and fd_indexcode ='ZX001'",
							String.class, rating.getCurrentStep().getNextStep().getId());
					debtIndex.setParentId(id);
					debtIndex.setRatingStep(rating.getCurrentStep().getNextStep());
				}

				if (!ModelChild.getCode().equals("P043_INPUT")) {
					debtIndex.setIndexCode(ModelChild.getCode().replace("_INPUT", ""));
					debtIndex.setIndexName(ModelChild.getName().replace("输入值", ""));
					String code = ModelChild.getCode().replace("_INPUT", "");
					debtIndex.setIndexValue(map.get(ModelChild.getCode().replace("_INPUT", "")));
					for (int i = 0; i < ModelChild.getParameters().size(); i++) {
						Parameter parameter = ModelChild.getParameters().get(i);
						if (!StringUtils.isEmpty(parameter.getCode())
								&& parameter.getType().toString().equals("IN_OPTION")) {
							StringBuffer dataStr = new StringBuffer("{");
							for (ParameterOption po : parameter.getOptions()) {
								if (map.get(parameter.getCode()).equals(po.getInputValue())) {
									debtIndex.setQuOptions(po.getInputValue() + "." + po.getTitle());
								}
								dataStr.append("\"" + po.getInputValue() + "\"");
								dataStr.append(":");
								dataStr.append("\"" + po.getTitle() + "\"");
								dataStr.append(",");
							}
							String data = dataStr.toString();
							data = data.substring(0, data.length() - 1) + "}";
							// 定性文本
							debtIndex.setDxText(data.replace("\n", ""));
						}

					}
					ratingIndexService.getRepository().save(debtIndex);
				}
			}

		} catch (Exception e) {
			// repository.deleteById(mainG.getId());
			e.printStackTrace();
		}

	}

	/*
	 * / * @获取债项结果指标
	 * 
	 * @param modelCode
	 * 
	 * @param paramValue
	 * 
	 * @return
	 * 
	 * @throws Exception
	 **/
	public void parsingResult(Map<String, String> mapResult, Model model) {

	}

	@Transactional
	public DebtRating saveRatingStepAndIndex(DebtRating rating, Model model, Map<String, String> paramValue)
			throws Exception {
		List<RatingDebtStep> ratingSteps = new ArrayList<RatingDebtStep>();
		Map<String, String> map = ratingResults(model.getCode(), paramValue);
		List<Model> models = model.getSubModels();
		List<Map<String, Object>> list = jdbc
				.queryForList("select * from  NS_R_CFG_STEPS where FD_RATINGCFG_ID='2' order by fd_stepnum asc");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get("FD_STEPTYPE").toString().indexOf("BS") > -1
					|| list.get(i).get("FD_STEPTYPE").toString().equals("INIT")) {
				RatingDebtStep step = new RatingDebtStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(rating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				// step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");
				// 持久化步骤实例
				step = ratingMainStepService.add(step);
				if (ratingSteps.size() >= 1) {
					// 设置上一步
					step.setLastStep(ratingSteps.get(ratingSteps.size() - 1));
					// 设置下一步
					ratingSteps.get(ratingSteps.size() - 1).setNextStep(step);
				}
				ratingSteps.add(step);
				String type = list.get(i).get("FD_STEPTYPE").toString();
				try {
					initRatingIndex(step, models, type, map, paramValue);
				} catch (Exception e) {
					return null;
				}

			} else {
				RatingDebtStep step = new RatingDebtStep();
				step.setSno(Integer.parseInt(list.get(i).get("FD_STEPNUM").toString()));
				step.setRatingMain(rating);
				step.setStepName(list.get(i).get("FD_STEPNAME").toString());
				// step.setStepResources(list.get(i).get("FD_RESOURCEPATH").toString());
				step.setStepType(RatingStepType.valueOf(list.get(i).get("FD_STEPTYPE").toString()));
				step.setVaild("1");

				// 持久化步骤实例
				step = ratingMainStepService.add(step);
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

	private void initRatingIndex(RatingDebtStep step, List<Model> models, String type, Map<String, String> map,
			Map<String, String> paramValue) throws Exception {
		RatingDebtIndex index;
		jdbc.update("delete ns_debt_indexes where FD_STEPID=?", step.getId());
		if (!CollectionUtils.isEmpty(models)) {
			// 模型下的定性和定量
			for (Model m1 : models) {
				if (m1.getCode().equals(type)) {
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
				}
			}
		}
	}

	private RatingDebtIndex saveRatingIndex(RatingDebtStep step, String IndexCategory, Model model, String level,
			String parentId, Map<String, String> map, Map<String, String> paramValue) throws Exception {
		RatingDebtIndex index = new RatingDebtIndex();
		// 指标ID
		index.setRatingStep(step);
		index.setIndexId(model.getId());
		// 指标名称
		index.setIndexName(model.getName());
		// 评级步骤
		index.setRatingStep(step);
		// 指标类型
		//index.setIndexType(step.getStepType());
		// 指标分类
		index.setIndexCategory(IndexCategory);
		// 层级
		index.setLevel(level);
		if (CommonConstant.MODEL_LEVEL_THREE.equals(level)) {
			// 父级ID
			index.setParentId(parentId);
		}

		for (int i = 0; i < model.getParameters().size(); i++) {
			Parameter parameter = model.getParameters().get(i);
			if (parameter.getCode().contains("RESULT") && !parameter.getCode().contains("TEMP")) {
				BigDecimal bigdecimal = new BigDecimal(map.get(parameter.getCode()));
				index.setIndexScore(bigdecimal);
			}

			if (!StringUtils.isEmpty(parameter.getName()) && (parameter.getType().equals(ParameterType.IN)
					|| parameter.getType().equals(ParameterType.IN_OPTION))) {
				// 指标code
				index.setIndexCode(parameter.getCode());

				index.setIndexValue(paramValue.get(parameter.getCode()));
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
	 * 计算评级结果
	 * 
	 * @throws Exception
	 */

	@Override
	public Map<String, String> ratingResults(String modelCode, Map<String, String> paramValue) {
		Executor executor = executorFactoryService.getExecutor();
		Map<String, String> value = new HashMap<String, String>();
		try {
			String jsonz = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(paramValue);
			ModelResult result = executor.executeByCode(modelCode, null, jsonz);
			value = indicatorsValue(result);
		} catch (Exception e) {
			e.printStackTrace();
			return value;
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
	public Map<String, String> subjects(DebtRating rating, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean check(Map<String, String> quantitative, Map<String, String> qualitative) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	@Override
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception {
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
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("project"), "FD_PROJECT_NAME"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("documentType"), "document_type"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("vaild"), "FD_VAILD"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("proCode"), "FD_PROJECT_CODE"));// 业务部
		sqlQuery.append(CommonUtils.sqlNotPar(request.getParameter("ratingStatus"), "fd_rating_status"));// 评级状态
		String str="";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("vailds"))) {
			 str =" and rating.fd_vaild='1' ";
		}
		List<String> list =CommonUtils.getAllRoleIds();
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " master.FD_EMPLOYEE_ID"));// 业务部
		}
		
		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "FD_FINAL_DATE"));
		
		String sql = "select \r\n" + 
				"master.FD_BP_NAME as custName,\r\n" + 
				"master.FD_BP_CODE as custCode,\r\n" + 
				"pro.project_number as proCode,\r\n" + 
				"pro.project_name as proName,\r\n" + 
				"master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" + 
				"FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n" + 
				"FD_HIGH_PRECISION as highPrecision,\r\n" + 
				"FD_ENTERPRISE_HONOR as enterpriseHonor,\r\n" + 
				"FD_ECONOMIC_TYPE as economic,\r\n" + 
				"rating.FD_ID as id,\r\n" + 
				"rating.PRODUCT_TYPE as type,\r\n" + 
				"master.FD_EMPLOYEE_ID as employeeId,FD_INTERN_LEVEL as internLevel,org.fd_name as orgName, \r\n" + 
				"rating.FD_RATING_STATUS as ratingStatus,\r\n" + 
				"rating.FD_INTERN_NAME as internName,rating.FD_FINAL_NAME as finalName,rating.FD_ASSETS_NAME as assetReview,to_char(FD_FINAL_DATE,'yyyy-MM-dd') as finalDate\r\n" 
				+ "from   NS_PRJ_PROJECT pro   \r\n"
				+ "left join ns_facility_rating rating on pro.PROJECT_NUMBER = rating.FD_PROJECT_CODE    \r\n"+str
				+ "left join NS_BP_MASTER master on to_char(pro.bp_id_tenant) = to_char(master.fd_ID) \r\n"
				+ "left join FR_AA_USER_PARTITION init on rating.FD_INTERN_CODE = init.employee_id  "
				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id " 
				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
				+ "where  1=1  "
				+ sqlQuery + " order by rating.FD_FINAL_DATE desc nulls last ";
		System.out.println("sql:"+sql);
		return getResult(sql, page, rows);
	}

	@Override
	public ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception {
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
				"	case rating.FD_RATING_STATUS when '010' then assets.FD_INTERN_LEVEL else assets.FD_FINAL_LEVEL end assetsLevl,\r\n" + 
				"	case rating.FD_RATING_STATUS when '010' then credit.FD_INTERN_LEVEL else credit.FD_FINAL_LEVEL end creditLevl,\r\n" + 
				"	case rating.FD_RATING_STATUS when '010' then main.FD_INTERN_LEVEL else main.FD_FINAL_LEVEL end mainLevl,\r\n" + 
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
				"	rating.FD_FINAL_LEVEL as finallevel,\r\n" + 
				"	rating.FD_FINAL_SCO as finaSco,\r\n" + 
				"	to_char( rating.FD_FINAL_DATE, 'yyyy-MM-dd' ) AS finalDate,\r\n" + 
				"	to_char( rating.FD_DATE, 'yyyy-MM-dd' ) as fDate,\r\n" + 
				"	rating.FD_RATING_STATUS AS ratingstatus,\r\n" + 
				"	rating.fd_version as version,\r\n" + 
				"	rating.PRODUCT_TYPE AS type\r\n" + 
				"FROM\r\n" + 
				"	ns_facility_rating rating\r\n" + 
				"	LEFT JOIN NS_PRJ_PROJECT pro ON pro.PROJECT_NUMBER = rating.FD_PROJECT_CODE \r\n" + 
				"	left join NS_ASSETS_rating assets on assets.fd_id = rating.ASSETS_RATING_ID\r\n" + 
				"	left join ns_main_rating main on main.fd_id = rating.MAIN_RATING_ID\r\n" + 
				"	left join NS_CREDIT_RATING credit on credit.fd_id = rating.CREDIT_RATING_ID\r\n" + 
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

	@Override
	public ResponseWrapper<RatingInit> parameterQuery2(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception {
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
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("assetreview"), "FD_ASSET_REVIEW"));// 评审经理

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
		
		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date1"), request.getParameter("date2"), "FD_FINAL_DATE"));
		
		String sql = "select \r\n" + "master.FD_BP_NAME as custName,\r\n" + "master.FD_BP_CODE as custCode,\r\n"
				+ "pro.project_number as proCode,\r\n" + "pro.project_name as proName,\r\n"
				+ "master.FD_SCORE_TEMPLATE_ID as scoreTemplateId,\r\n" + "FD_SEGMENT_INDUSTRY as segmentIndustry,\r\n"
				+ "FD_HIGH_PRECISION as highPrecision,\r\n" + "FD_ENTERPRISE_HONOR as enterpriseHonor,\r\n"
				+ "FD_ECONOMIC_TYPE as economic,\r\n" + "rating.FD_ID as id,\r\n" + "FD_TYPE as type,\r\n"
				+ "master.FD_EMPLOYEE_ID as employeeId,FD_INTERN_LEVEL as internLevel,org.fd_name as orgName, FD_INTERN_Bs as internBs,\r\n" + "FD_FINAL_LEVEL as finalLevel,\r\n" + "FD_PD as pd,\r\n"
				+ "FD_final_bs as finalBs,\r\n" + "FD_RATING_STATUS as ratingStatus,\r\n"
				+ "FD_INTERN_NAME as internName," + "FD_FINAL_NAME as finalName," + "FD_ASSET_REVIEW as assetReview,"
				+ "to_char(FD_FINAL_DATE,'yyyy-MM-dd') as finalDate\r\n" + "from   NS_PRJ_PROJECT pro   \r\n"
				+ "left join ns_DEBT_rating rating on pro.PROJECT_NUMBER = rating.FD_PROJECT_CODE    \r\n"+str
				+ "left join NS_BP_MASTER master on to_char(pro.bp_id_tenant) = to_char(master.fd_ID) \r\n"
				+ "left join FR_AA_USER_PARTITION init on rating.FD_INTERN_CODE = init.employee_id  "
				+ "left join fr_aa_org org on master.FD_LEASE_ORGANIZATION = org.fd_id " 
				+ "left join fr_aa_user fruser on master.fd_employee_id = fruser.fd_id "
				+ "where  1=1  "
				+ sqlQuery + " order by rating.FD_FINAL_DATE desc nulls last ";
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

	private String getExportSql(HttpServletRequest request, HttpServletResponse response, DebtRating queryExampleEntity,
			String fileName) {

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("highprecision"), "FD_HIGH_PRECISION"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("enterprisehonor"), "FD_ENTERPRISE_HONOR"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("economic"), "FD_ECONOMIC_TYPE"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("industry"), "FD_SEGMENT_INDUSTRY"));
		// ---
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("vaild"), "FD_VAILD"));// 业务
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("custName"), "FD_BP_NAME"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("custCode"), "FD_BP_CODE"));
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("scoreTemplateId"), "FD_SCORE_TEMPLATE_ID"));
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("employee"), "FD_USERNAME"));// 业务经理
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("finalName"), "FD_FINAL_NAME"));// 评审经理
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("leaseOrganization"), "org.fd_code"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdType1"), "FD_TYPE"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("method"), "fd_rating_status"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("fdVersion"), "FD_VERSION"));// 业务部
		sqlQuery.append(CommonUtils.sqlFuzzyPar(request.getParameter("project"), "FD_PROJECT_NAME"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("documentType"), "document_type"));// 业务部
		sqlQuery.append(CommonUtils.sqlPar(request.getParameter("proCode"), "FD_PROJECT_CODE"));// 业务部
		sqlQuery.append(
				CommonUtils.sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "FD_FINAL_DATE"));
		List<String> list =CommonUtils.getAllRoleIds();
		
		String str="";
		if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getParameter("vailds"))) {
			 str =" and rating.fd_vaild<>'0' ";
		}
		if(list.contains("manager")) {
			sqlQuery.append(CommonUtils.sqlPar(SecurityUtil.getUserId(), " bp.FD_EMPLOYEE_ID"));// 业务部
		}
		String sql = "select PROJECT_NUMBER as 项目编号,project_name as 项目名称,FD_BP_NAME as 承租人名称,highBj1.value_name  as 国标行业,\r\n"
		+ "      highBj2.value_name as 高精尖,FD_INTERN_LEVEL as 初评等级,FD_INTERN_BS as 初评保障倍数, FD_FINAL_LEVEL as 复核等级,FD_FINAL_BS as 复核保障倍数,\r\n"
		+ "      org.fd_name as 业务部,fruser.fd_username as 项目经理,FD_FINAL_NAME as 评审经理, FD_ASSET_REVIEW as 资产经理,\r\n"
		+ "      (case when FD_RATING_STATUS='000' then '评级测算' when  FD_RATING_STATUS='010' then '初评' else '复评' end) as 评级类型,\r\n"
		+ "      (case when FD_RATING_VAILD=1 then '有效' else '无效' end) as 评级状态,to_char(debt.FD_FINAL_DATE,'yyyy-MM-dd') as 评级生效日期\r\n"
		+ "      from ns_prj_project prj \r\n" + "left join ns_bp_master bp  on prj.bp_id_tenant=bp.fd_id\r\n"
		+ "      left join ns_debt_rating debt on debt.fd_project_code = prj.project_number "+str
		+ "      left join NS_ZGC_HIGH_BJ highBj1 on highBj1.value_code=bp.FD_SEGMENT_INDUSTRY and highBj1.flag='01'\r\n"
		+ "      left join NS_ZGC_HIGH_BJ highBj2 on highBj2.value_code=bp.FD_HIGH_PRECISION and highBj2.flag='02' "
		+ "      left join fr_aa_user fruser on bp.fd_employee_id = fruser.fd_id "
		+ "      left join fr_aa_org org on bp.FD_LEASE_ORGANIZATION = org.fd_id "
		+ "      where 1=1 "
		+ sqlQuery;
		return sql;
	}

	@Override
	public Map<String, Object> exportData(String loginName,HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, String fileName) {

		String sql = getExportSql(request, response, queryExampleEntity, fileName);
		if (fileName.endsWith(DownloadFileEnum.EXCEL.getValue())) {
			fileName = fileName.substring(0, fileName.lastIndexOf(DownloadFileEnum.EXCEL.getValue()));
		}
		downloadFileService.exportDataToExcel(loginName,sql, fileName + DownloadFileEnum.EXCEL.getValue());

		return AppUtil.getMap(true);
	}

	@Override
	public Map<String, Object> queryCount(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, String fileName) {

		String sql = getExportSql(request, response, queryExampleEntity, fileName);

		Long count = count(sql);

		if (count == null || count <= 0) {
			return AppUtil.getMap(false, "该查询条件无结果\r\n请确认后操作");
		}

		return AppUtil.getMap(true);
	}

}
