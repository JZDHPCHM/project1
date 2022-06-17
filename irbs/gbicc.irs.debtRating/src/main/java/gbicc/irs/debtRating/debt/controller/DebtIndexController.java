package gbicc.irs.debtRating.debt.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import com.alibaba.fastjson.JSON;

import gbicc.irs.debtRating.debt.entity.RatingDebtIndex;
import gbicc.irs.debtRating.debt.repository.DebtIndexRepository;
import gbicc.irs.debtRating.debt.service.DebtIndexService;
import gbicc.irs.debtRating.debt.service.FacilityIndexService;
import gbicc.irs.main.rating.repository.RatingIndexRepository;
import gbicc.irs.main.rating.service.RatingIndexService;
import gbicc.irs.main.rating.support.CommonConstant;
import gbicc.irs.main.rating.support.RatingStepType;

@RestController
@RequestMapping("/irs/DebtIndexController")
public class DebtIndexController extends
		SmartClientRestfulCrudController<RatingDebtIndex, String, DebtIndexRepository, DebtIndexService> {
	private static Log log = LogFactory.getLog(DebtIndexController.class);
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	private FacilityIndexService facilityIndexServiceImpl;
	
	/**
	 * 根据评级步骤获取指标数据
	 * @param stepId 评级步骤ID
	 * @return 评级指标列表
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingIndexsByStepId", method=RequestMethod.GET)
	public ResponseWrapper<RatingDebtIndex> getRatingIndexsByStepId(String stepId)
			throws Exception{
		List<RatingDebtIndex> indexs = service.getRatingIndexsByStepId(stepId);
		
		indexs = indexs.stream().filter(p -> CommonConstant.MODEL_LEVEL_THREE.equals(p.getLevel())).collect(Collectors.toList());
		
		indexs = indexs.stream().sorted(Comparator.comparing(RatingDebtIndex::getParentId,Comparator.nullsFirst(String::compareTo))).collect(Collectors.toList());
		
		return  ResponseWrapperBuilder.query(indexs);
	}

	/**
	 * 根据评级步骤类型获取指标数据
	 * @param indexType 评级步骤类型 
	 * @return 模型指标
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingIndexsByIndexType", method=RequestMethod.GET)
	public ResponseWrapper<RatingDebtIndex> getRatingIndexsByIndexType(RatingStepType indexType) throws Exception{
		List<RatingDebtIndex> indexs = service.getRatingIndexsByIndexType(indexType);
		return  ResponseWrapperBuilder.query(indexs);
	}
	
	////////////////////////// 债项安全评级报告 ///////////////////////////////
	@GetMapping("getFacilityHeader")
	public Map<String, Object> getFacilityHeader(String id){
		// 债项报告页头
		Map<String, Object> nsMainLevelList = facilityIndexServiceImpl.getFacilityHeader(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getFacilityLevel")
	public Map<String, Object> getFacilityLevel(String id){
		// 债项安全评级报告仪表盘
		Map<String, Object> nsMainLevelList = facilityIndexServiceImpl.getAssetsLevel(id);
		return nsMainLevelList;
	}

	@GetMapping("getAssetsDescribe")
	public Map<String, Object> getAssetsDescribe(String id){
		// 债项安全评级报告综述(仪表盘右边那一块)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getAssetsDescribe(id);
		return nsMainLevelList.get(0);
	}
	
	@GetMapping("getFacilityMainInfo")
	public Map<String, Object> getFacilityMainInfo(String id){
		// 债项安全评级报告(主体信用评级)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityMainInfo(id);
		return nsMainLevelList.get(0);
	}
	
	@GetMapping("getFacilityAssetsInfo")
	public Map<String, Object> getFacilityAssetsInfo(String id){
		// 债项安全评级报告(资产信用评级)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityAssetsInfo(id);
		return nsMainLevelList.get(0);
	}
	
	@GetMapping("getFacilityCredit")
	public Map<String, Object> getFacilityCredit(String id){
		// 债项安全评级报告(增信措施评价)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityCredit(id);
		return nsMainLevelList.get(0);
	}
	
	@GetMapping("getFacilityWarning")
	public List<Map<String, Object>> getFacilityWarning(String id){
		// 债项安全评级报告(风险提示)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityWarning(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getFacilityCompare")
	public List<Map<String, Object>> getFacilityCompare(String id){
		// 债项安全评级报告(资产和主体的大小)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityCompare(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getFacilityTrait")
	public List<Map<String, Object>> getFacilityTrait(String id){
		// 债项安全评级报告(提示标签)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityTrait(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getFacilityAssetsScore")
	public Map<String, Object> getFacilityAssetsScore(String id){
		// 债项安全评级报告(债项对应的资产得分)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityAssetsScore(id);
		return nsMainLevelList.get(0);
	}
	
	@GetMapping("getFacilityMainScore")
	public Map<String, Object> getFacilityMainScore(String id){
		// 债项安全评级报告(债项对应的主体得分)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityMainScore(id);
		return nsMainLevelList.get(0);
	}
	
	@GetMapping("getFacilityToAssets")
	public void getFacilityToAssets(String id,HttpServletRequest request,HttpServletResponse response){
		// 债项安全评级报告(跳转到资产评级)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityToAssets(id);
		Map<String, Object> map = nsMainLevelList.get(0);
		String assetsId = (String)map.get("ASSETS_RATING_ID");
		try {
			response.sendRedirect("/irs/assetsRating/assetsRatingReport?custNo=" + assetsId);
		} catch (Exception e) {
			log.warn("重定向错误, 请求地址: /irs/DebtIndexController/getFacilityToAssets " + "\t\n请求参数:  " + id);
			log.warn("错误信息: " + e);
		}
	}

	@GetMapping("getFacilityToMain")
	public void getFacilityToMain(String id, HttpServletRequest request, HttpServletResponse response) {
		// 债项安全评级报告(跳转到主体评级)
		List<Map<String, Object>> nsMainLevelList = facilityIndexServiceImpl.getFacilityToMain(id);
		Map<String, Object> map = nsMainLevelList.get(0);
		String mainId = (String) map.get("MAIN_RATING_ID");
		try {
			response.sendRedirect("/irs/mainRating/mainRatingReport?custNo=" + mainId);
		} catch (Exception e) {
			log.warn("重定向错误, 请求地址: /irs/DebtIndexController/getFacilityToMain " + "\t\n请求参数:  " + id);
			log.warn("错误信息: " + e);
		}
	}
	
	
	
}
