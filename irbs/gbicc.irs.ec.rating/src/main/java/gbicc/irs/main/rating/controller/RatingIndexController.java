package gbicc.irs.main.rating.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.repository.RatingIndexRepository;
import gbicc.irs.main.rating.service.RatingIndexService;
import gbicc.irs.main.rating.support.RatingStepType;

@RestController
@RequestMapping("/irs/ratingIndex")
public class RatingIndexController extends
		SmartClientRestfulCrudController<RatingIndex, String, RatingIndexRepository, RatingIndexService> {
	
	/**
	 * 根据评级步骤获取指标数据
	 * @param stepId 评级步骤ID
	 * @return 评级指标列表
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingIndexsByStepId", method=RequestMethod.GET)
	public ResponseWrapper<RatingIndex> getRatingIndexsByStepId(String stepId)
			throws Exception{
		List<RatingIndex> indexs = service.getRatingIndexsByStepId(stepId);
//		indexs = indexs.stream().filter(p ->("财务指标".equals(p.getIndexCategory())||(RatingStepType.QUALITATIVE_EDIT.equals(p.getIndexType())&&p.getLevel().equals(CommonConstant.MODEL_LEVEL_THREE)))).
//				collect(Collectors.toList());
//		indexs = indexs.stream().filter(p ->(!"经营质量".equals(p.getIndexCategory()))).
//				collect(Collectors.toList());
		
		indexs = indexs.stream().sorted(Comparator.comparing(RatingIndex::getParentId,Comparator.nullsFirst(String::compareTo))).collect(Collectors.toList());
		
		return  ResponseWrapperBuilder.query(indexs);
	}
	
	/**
	 * 根据评级步骤类型获取指标数据
	 * @param indexType 评级步骤类型 
	 * @return 模型指标
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingIndexsByIndexType", method=RequestMethod.GET)
	public ResponseWrapper<RatingIndex> getRatingIndexsByIndexType(RatingStepType indexType) throws Exception{
		List<RatingIndex> indexs = service.getRatingIndexsByIndexType(indexType);
		return  ResponseWrapperBuilder.query(indexs);
	}
	
	////////////////////////// 主体信用评级报告 ///////////////////////////////
	@GetMapping("getMainHeader")
	public Map<String, Object> getMainHeader(String id){
		// 主体信用评级报告页面头
		Map<String, Object> nsMainLevelList = service.getMainHeader(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getLevel")
	public Map<String, Object> getLevel(String id){
		// 主体信用评级报告仪表盘
		Map<String, Object> nsMainLevelList = service.getNsMainLevelList(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getMainScore")
	public Map<String, Object> getMainScore(String id){
		// 主体信用评级报告综述(主体得分)
		Map<String, Object> nsMainLevelList = service.getMainScore(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getMainInfo")
	public Map<String, Object> getMainInfo(String id){
		// 主体信用评级报告综述(仪表盘右边那一块)
		Map<String, Object> nsMainLevelList = service.getNsMainLevelInfo(id);
		return nsMainLevelList;
	}

	@GetMapping("getIndicator")
	public List<Map<String, Object>> getIndicator(String id){
		// 主体信用评级报告综述(仪表盘下边那一块)存量维度图
		List<Map<String, Object>> nsMainLevelList = service.getIndicator(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getDepartment")
	public List<Map<String, Object>> getDepartment(String id){
		// 主体信用评级报告综述(仪表盘下边那一块)维度图部门级(下拉框那个选项)
		List<Map<String, Object>> nsMainLevelList = service.getDepartment(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getGrade")
	public List<Map<String, Object>> getGrade(String id){
		// 主体信用评级报告综述(仪表盘下边那一块)维度图同赛道(下拉框那个选项)
		List<Map<String, Object>> nsMainLevelList = service.getGrade(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getTrack")
	public Map<String, Object> getTrack(String id){
		// 主体信用评级报告综述(得分分位数)
		Map<String, Object> nsMainLevelList = service.getTrack(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getManage")
	public List<Map<String, Object>> getManage(String id){
		// 主体信用评级报告综述(经营质量)
		List<Map<String, Object>> nsMainLevelList = service.getManage(id);
		return nsMainLevelList;
	}

	@GetMapping("getAbility")
	public List<Map<String, Object>> getAbility(String id){
		// 主体信用评级报告综述(管理能力)
		List<Map<String, Object>> nsMainLevelList = service.getAbility(id);
		return nsMainLevelList;
	}

	@GetMapping("getEnvironment")
	public List<Map<String, Object>> getEnvironment(String id){
		// 主体信用评级报告综述(产业环境)
		List<Map<String, Object>> nsMainLevelList = service.getEnvironment(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getQuality")
	public List<Map<String, Object>> getQuality(String id){
		// 主体信用评级报告综述(产品品质)
		List<Map<String, Object>> nsMainLevelList = service.getQuality(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getPosition")
	public List<Map<String, Object>> getPosition(String id){
		// 主体信用评级报告综述(市场地位)
		List<Map<String, Object>> nsMainLevelList = service.getPosition(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getModel")
	public List<Map<String, Object>> getModel(String id){
		// 主体信用评级报告综述(商业模式)
		List<Map<String, Object>> nsMainLevelList = service.getModel(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getTip")
	public List<Map<String, Object>> getTip(String id){
		// 主体信用评级报告综述(风险提示)
		List<Map<String, Object>> nsMainLevelList = service.getTip(id);
		return nsMainLevelList;
	}
	
	@GetMapping("getMainTrait")
	public List<Map<String, Object>> getMainTrait(String id){
		// 主体信用评级报告综述(提示标签)
		List<Map<String, Object>> nsMainLevelList = service.getMainTrait(id);
		return nsMainLevelList;
	}
	
	
}
