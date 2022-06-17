package gbicc.irs.assetsRating.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import com.alibaba.fastjson.JSON;

import gbicc.irs.assetsRating.entity.RatingAssetsStep;
import gbicc.irs.assetsRating.repository.RatingAssetsStepRepository;
import gbicc.irs.assetsRating.service.AssetsMainLevelService;
import gbicc.irs.assetsRating.service.RatingAssetsStepService;


@RestController
@RequestMapping("/irs/table")
public class RatingAssetsStepController extends
SmartClientRestfulCrudController<RatingAssetsStep, String,RatingAssetsStepRepository, RatingAssetsStepService> {
	private static Log log = LogFactory.getLog(RatingAssetsStepController.class);
	
	@Autowired
	private AssetsMainLevelService assetsMainLevelService;
	

    ////////////////////////// 资产信用评级报告 ///////////////////////////////
	@GetMapping("getAssetsHeader")
	public Map<String, Object> getAssetsHeader(String id){
		log.info("请求路径:" + "/irs/table/getAssetsHeader\n\t" + "请求参数 : " + id);
		// 债项报告页头
		Map<String, Object> nsMainLevelList = assetsMainLevelService.getAssetsHeader(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}

	@GetMapping("getAssetsLevel")
	public Map<String, Object> getAssetsLevel(String id){
		log.info("请求路径:" + "/irs/table/getAssetsLevel\n\t" + "请求参数 : " + id);
		// 资产信用评级报告仪表盘
		Map<String, Object> nsMainLevelList = assetsMainLevelService.getAssetsLevelSurface(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getAssetsPosition")
	public Map<String, Object> getAssetsPosition(String id){
		log.info("请求路径:" + "/irs/table/getAssetsPosition\n\t" + "请求参数 : " + id);
		// 资产信用评级(存量资产评级中的位置)
		Map<String, Object> nsMainLevelList = assetsMainLevelService.getAssetsPosition(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getAssetsScore")
	public Map<String, Object> getAssetsScore(String id){
		log.info("请求路径:" + "/irs/table/getAssetsScore\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(获取资产终评得分)
		Map<String, Object> nsMainLevelList = assetsMainLevelService.getAssetsScore(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getAssetsInfo")
	public Map<String, Object> getAssetsInfo(String id){
		log.info("请求路径:" + "/irs/table/getAssetsInfo\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(仪表盘右边那一块)
		Map<String, Object> nsMainLevelList = assetsMainLevelService.getAssetsRatingInfo(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getGroup")
	public List<Map<String, Object>> getGroup(String id){
		log.info("请求路径:" + "/irs/table/getGroup\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(维度图-跟公司所有存量资产对比)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getGroup(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getProduct")
	public List<Map<String, Object>> getProduct(String id){
		log.info("请求路径:" + "/irs/table/getProduct\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(维度图-跟同产品的对比)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getProduct(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getAssetsDepartment")
	public List<Map<String, Object>> getAssetsDepartment(String id){
		log.info("请求路径:" + "/irs/table/getAssetsDepartment\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(维度图-跟所在部门的对比)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getAssetsDepartment(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getBarIncome")
	public List<Map<String, Object>> getBarIncome(String id){
		log.info("请求路径:" + "/irs/table/getBarIncome\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(柱状图创收性)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getBarIncome(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getBarPreservation")
	public List<Map<String, Object>> getBarPreservation(String id){
		log.info("请求路径:" + "/irs/table/getBarPreservation\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(柱状图保值性) 
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getBarPreservation(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getBarCirculation")
	public List<Map<String, Object>> getBarCirculation(String id){
		log.info("请求路径:" + "/irs/table/getBarCirculation\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(柱状图流通性)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getBarCirculation(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getBarSteerable")
	public List<Map<String, Object>> getBarSteerable(String id){
		log.info("请求路径:" + "/irs/table/getBarSteerable\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(柱状图可控性)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getBarSteerable(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList;
	}
	
	@GetMapping("getFooter")
	public Map<String, Object> getFooter(String id){
		log.info("请求路径:" + "/irs/table/getFooter\n\t" + "请求参数 : " + id);
		// 资产信用评级报告描述综述(风险提示)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getFooter(id);
		log.info(JSON.toJSONString(nsMainLevelList));
		return nsMainLevelList.get(0);
	}
	
	@GetMapping("getAssetsTrait")
	public List<Map<String, Object>> getAssetsTrait(String id){
		// 资产信用评级报告综述(提示标签)
		List<Map<String, Object>> nsMainLevelList = assetsMainLevelService.getAssetsTrait(id);
		return nsMainLevelList;
	}
	
}
