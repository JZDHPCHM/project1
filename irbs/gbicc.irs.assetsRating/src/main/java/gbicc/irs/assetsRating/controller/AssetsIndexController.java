package gbicc.irs.assetsRating.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.assetsRating.entity.AssetsIndex;
import gbicc.irs.assetsRating.repository.AssetsIndexRepository;
import gbicc.irs.assetsRating.service.AssetsIndexService;
import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.support.CommonConstant;
import gbicc.irs.main.rating.support.RatingStepType;

@Controller
@RequestMapping("/irs/assetsIndex")
public class AssetsIndexController extends
		SmartClientRestfulCrudController<AssetsIndex, String, AssetsIndexRepository, AssetsIndexService> {
	
	/**
	 * 根据评级步骤获取指标数据
	 * @param stepId 评级步骤ID
	 * @return 评级指标列表
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingIndexsByStepId", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<AssetsIndex> getRatingIndexsByStepId(String stepId)
			throws Exception{
		List<AssetsIndex> indexs = service.getRatingIndexsByStepId(stepId);
//		indexs = indexs.stream().filter(p ->("财务指标".equals(p.getIndexCategory())||(RatingStepType.QUALITATIVE_EDIT.equals(p.getIndexType())&&p.getLevel().equals(CommonConstant.MODEL_LEVEL_THREE)))).
//				collect(Collectors.toList());
		//indexs = indexs.stream().filter(p ->("财务指标".equals(p.getIndexCategory())||(RatingStepType.QUALITATIVE_EDIT.equals(p.getIndexType())&&p.getLevel().equals(CommonConstant.MODEL_LEVEL_THREE)))).
		//		collect(Collectors.toList());
		
		indexs = indexs.stream().sorted(Comparator.comparing(AssetsIndex::getParentId,Comparator.nullsFirst(String::compareTo))).collect(Collectors.toList());
		
		return  ResponseWrapperBuilder.query(indexs);
	}
	
	/**
	 * 根据评级步骤类型获取指标数据
	 * @param indexType 评级步骤类型 
	 * @return 模型指标
	 * @throws Exception
	 */
	@RequestMapping(value="getRatingIndexsByIndexType", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingIndex> getRatingIndexsByIndexType(RatingStepType indexType) throws Exception{
		//List<RatingIndex> indexs = service.getRatingIndexsByIndexType(indexType);
		//return  ResponseWrapperBuilder.query(indexs);
		return null;
	}
	
}
