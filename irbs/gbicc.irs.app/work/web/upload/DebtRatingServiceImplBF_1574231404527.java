/*package gbicc.irs.debtRating.debt.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.engine.model.client.Executor;
import org.wsp.engine.model.client.spring.service.ExecutorFactoryService;
import org.wsp.engine.model.core.code.impl.support.ModelResult;
import org.wsp.engine.model.core.code.impl.support.ParameterResult;
import org.wsp.engine.model.core.po.Model;
import org.wsp.engine.model.core.po.ModelDefineWrapper;
import org.wsp.engine.model.core.po.Parameter;
import org.wsp.engine.model.core.po.ParameterOption;
import org.wsp.engine.model.core.util.JacksonObjectMapper;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.repository.DebtRatingRepository;
import gbicc.irs.debtRating.debt.service.DebtRatingService;
import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.support.CommonConstant;
import gbicc.irs.main.rating.support.CommonUtils;


@Service
public class DebtRatingServiceImplBF extends 
DaoServiceImpl<DebtRating, String, DebtRatingRepository> 
implements DebtRatingService{

	@Autowired
	public JdbcTemplate jdbc;
	
	@Autowired
	private ExecutorFactoryService executorFactoryService;
	
	@Override
	public void debtTest() {
		List<Map<String,Object>> list = jdbc.queryForList("select * from ns_debt_test");
		Map<String,String> map = new HashMap<String, String>();
		for (Map<String, Object> mapList : list) {
			map.put(mapList.get("FD_CODE").toString(),mapList.get("FD_VALUE").toString());
		}
		try {
			Map<String, String>  ZXPJ01 = ratingResults("ZXPJ01",map);
			ZXPJ01.get("CZX_RESULT");
			map.put("CZX001", ZXPJ01.get("CZX_RESULT"));
			Map<String, String>  mapResult = ratingResults("ZXPJ",map);
			parsingResult(mapResult,getModel("P010"));
			String json = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(mapResult);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	*//**
	 * @获取债项结果指标
	 * @param modelCode
	 * @param paramValue
	 * @return
	 * @throws Exception
	 *//*
	public void parsingResult(Map<String, String>  mapResult,Model model) {
		
		for (Parameter parameter : model.getParameters()) {
			for (Parameter parametee : parameter.getOptions()) {
				
			}
			if (parameter.getCode().contains("RESULT")) {
				System.out.println(parameter.getName());
			}
				
		}
			for (String key : paramValue.keySet()) {
				System.out.println(mapResult.get(key+"RESULT"));
			}
		
	}
	
	
	
	
	public Map<String, String> ratingResults(String modelCode, Map<String, String> paramValue) throws Exception {
		Executor executor = executorFactoryService.getExecutor();
		String jsonz = JacksonObjectMapper.getDefaultObjectMapper().writeValueAsString(paramValue);
		ModelResult result = executor.executeByCode(modelCode, null, jsonz);
		Map<String, String> value = indicatorsValue(result);
		return value;
	}
	
	public Map<String, String> indicatorsValue(ModelResult result) {
		Map<String, String> resultList = new HashMap<String, String>();
		List<ParameterResult> para = result.getData();
		for (ParameterResult parameterResult : para) {
			resultList.put(parameterResult.getCode(), parameterResult.getValue());
		}
		return resultList;
	}

	
	
}
*/