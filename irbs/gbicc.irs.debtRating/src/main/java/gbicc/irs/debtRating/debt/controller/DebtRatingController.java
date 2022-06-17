package gbicc.irs.debtRating.debt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSONObject;
import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.repository.DebtRatingRepository;
import gbicc.irs.debtRating.debt.service.DebtRatingService;
import gbicc.irs.debtRating.debt.service.DebtRatingStepService;
import gbicc.irs.main.rating.vo.RatingInit;

@Controller
@RequestMapping("/irs/DebtRatingResults")
public class DebtRatingController
		extends SmartClientRestfulCrudController<DebtRating, String, DebtRatingRepository, DebtRatingService> {

	@Autowired
	public JdbcTemplate jdbc;

	@Autowired
	DebtRatingService debt;

	@Autowired
	DebtRatingStepService stepService;
	/**
	 * @债项评级管理
	 * @return
	 */
	/*
	 * @RequestMapping("debtRating.html")
	 * 
	 * @MenuContributionItem("menuitem.irs.debtRating.contract") public String
	 * financialRatingStart() { return
	 * "gbicc/irs/rating/debt/debtRatingInitiate.html"; }
	 */

	/**
	 * @债项评级发起
	 * @return
	 */
	@RequestMapping("debtRatingInitiate")
	@MenuContributionItem("menuitem.irs.debtRating.contract")
	public String enterDebtRatingInitiate() {
		return "gbicc/irs/rating/debt/debtRatingInitiate.html";
	}

	@RequestMapping("debtParameter")
	@MenuContributionItem("menuitem.irs.debtRating.Parameter")
	public String enterDebtParameter() {
		return "gbicc/irs/rating/debt/debtParameter.html";
	}
	@RequestMapping("debtParameter2")
	@MenuContributionItem("menuitem.irs.debtRating.Parameter2")
	public String enterDebtParameter2() {
		return "gbicc/irs/rating/debt/debtParameter2.html";
	}
	// debtParameter.html

	/**
	 * @债项评级报告
	 * @return
	 */
	@RequestMapping("debtRatingReport")
	public String enterDebtRatingReport() {
		return "gbicc/irs/rating/debt/debtRatingReport.html";
	}
	/**
	 * @债项台账查询3.0接口
	 * @return
	 */
	@RequestMapping("debtReport")
	public String enterDebtRatingReport2(String custNo) {
		return "gbicc/irs/rating/debt/debtReport.html";
	}


	/**
	 * @债项评级明细
	 * @return
	 */
	@RequestMapping("debtRatingDetail")
	public String enterDebtRatingDetail() {
		return "gbicc/irs/rating/debt/debtRatingDetail.html";
	}

	/**
	 * @债项历史评级进入债项报告页
	 * @return
	 */
	@RequestMapping("enterReport2")
	public String enterReport( Model model,String custNo) {
		model.addAttribute("custNo", custNo);
		return "gbicc/irs/rating/debt/debtReport.html";
	}
	
	/**
	 * 债项台账查询2.0接口
	 */
	@RequestMapping("enterReport")
	public String enterReport2(String projectNo) {
		
		return "gbicc/irs/rating/debt/enterReport.html";
	}

	@RequestMapping("debtRating.action")
	@ResponseBody
	public Map<String, String> debtTest() {
		// 调用租赁物模型
		List<Map<String, Object>> listZlw = jdbc.queryForList("select * from NS_lease_item");
		Map<String, String> map = new HashMap<String, String>();
		// 测试数据
		Map<String, String> mapAll = new HashMap<String, String>();
		List<Map<String, Object>> list = jdbc.queryForList("select * from ns_debt_test");
		for (Map<String, Object> mapList : list) {
			mapAll.put(mapList.get("FD_CODE").toString(), mapList.get("FD_VALUE").toString());
		}
		
		//短期现金流
		String dqxjl = fxckItem("ZXPJ08", mapAll).get("ZXPJ08_RESULT");
		mapAll.put("CZ002", dqxjl);
		mapAll.put("P002", dqxjl);
		// 风险敞口
		String fxck = fxckItem("ZXPJ07", mapAll).get("ZXPJ07_RESULT");
		// 应收账款--接入数据需遍历此处为数组
		String yszk = yszkItem("ZXPJ02", mapAll).get("ZXPJ02_RESULT");
		//法人保障价值
		String frbz = yszkItem("ZXPJ04", mapAll).get("ZXPJ04_RESULT");
		//不动产保障价值
		String bdc = yszkItem("ZXPJ05", mapAll).get("ZXPJ05_RESULT");
		//股权调整前保障价值
		String gqtz = yszkItem("ZXPJ06", mapAll).get("ZXPJ06_RESULT");
		
		
		//筹资性现金流计算
		String czx = yszkItem("ZXPJ01", mapAll).get("CZX_RESULT");
		
		
		// 租赁物调整前保证倍数
		for (Map<String, Object> mapList : listZlw) {
			map.put("BS002_SM", mapList.get("FD_LEASE_TYPE").toString());
			map.put("BS002_INIT", mapList.get("FD_LEASE_INIT").toString());
			map.put("BS002_YX", mapList.get("FD_LEASE_YSY").toString());
			map.put("P037", fxck);
			leaseItem("ZXPJ03", map,
					"select * from ns_debt_test where fd_code in ('P007','P008','P009','P010','P011','P012','P013','P014','P015')");
		}

		return null;
	}

	/**
	 * 
	 * @param modelCode
	 * @param zlw
	 * @param sql
	 * @租赁物计算
	 * @return
	 */
	public Map<String, String> leaseItem(String modelCode, Map<String, String> zlw, String sql) {
		List<Map<String, Object>> list = jdbc.queryForList(sql);

		Map<String, String> map = new HashMap<String, String>();
		for (Map<String, Object> mapList : list) {
			map.put(mapList.get("FD_CODE").toString(), mapList.get("FD_VALUE").toString());
		}
		for (String key : zlw.keySet()) {
			map.put(key, zlw.get(key));
		}
		return debt.ratingResults(modelCode, map);
	}

	/**
	 * @param modelCode
	 * @param zlw
	 * @param sql
	 * @风险敞口
	 * @return
	 */
	public Map<String, String> fxckItem(String modelCode, Map<String, String> fxck) {
		return debt.ratingResults(modelCode, fxck);
	}

	/**
	 * @param modelCode
	 * @param 应收账款
	 * @应收账款
	 * @return
	 */
	public Map<String, String> yszkItem(String modelCode, Map<String, String> yszk) {
		return debt.ratingResults(modelCode, yszk);
	}

	@RequestMapping(value = "debtRatingTrial", method = RequestMethod.GET)
	@ResponseBody
	public DebtRating debtRatingTrial(@RequestParam(name = "map") String map,
			@RequestParam(name = "debtId") String debtId) {
		DebtRating debtRating = new DebtRating();
		try {
			Map jsons = (Map) JSONObject.parse(map);
			//debtRating = debt.startTrial("ZXPJ", debtId, jsons, "000");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return debtRating;
	}

	@RequestMapping(value = "parameterQuery", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		ResponseWrapper<RatingInit> result = service.parameterQuery(request, response, queryExampleEntity, page, rows);
		return result;
	}
	/**
	 * 债项台账查询2.0
	 * @param request
	 * @param response
	 * @param queryExampleEntity
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "parameterQuery2", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingInit> parameterQuery2(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		ResponseWrapper<RatingInit> result = service.parameterQuery2(request, response, queryExampleEntity, page, rows);
		return result;
	}
	@RequestMapping(value = "parameterQueryHistory", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception {
		ResponseWrapper<RatingInit> result = service.parameterQueryHistory(request, response, queryExampleEntity, page, rows);
		return result;
	}

	/**
	 * 根据评级Id获取评级对象
	 * 
	 * @param ratingId 评级ID
	 * @throws Exception
	 */
	@RequestMapping(value = "getRatingById", method = RequestMethod.GET)
	@ResponseBody
	public DebtRating getRatingById(@RequestParam(name = "ratingId") String ratingId,String tempType) throws Exception {
		if (StringUtils.isEmpty(ratingId) || "null".equals(ratingId)) {
			return null;
		}
		Optional<DebtRating> companyRatingOp = service.getRepository().findById(ratingId);
		DebtRating companyRating = companyRatingOp.get();
		if(!tempType.equals(companyRating.getType())) {
			return null;
		}
		return companyRating;
	}

	@RequestMapping(value = "/queryCount.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> queryCount(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, String fileName) {

		try {
			Map<String, Object> map = service.queryCount(request, response, queryExampleEntity, fileName);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return AppUtil.getMap(false);
		}
	}

	@RequestMapping(value = "/exportData.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> exportData(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, String fileName) {
		   String loginName = SecurityUtil.getLoginName();
		try {
			Map<String, Object> map = service.exportData(loginName,request, response, queryExampleEntity, fileName);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return AppUtil.getMap(false);
		}
	}
}