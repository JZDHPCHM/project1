package gbicc.irs.main.rating.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.repository.RatingMainStepRepository;
import gbicc.irs.main.rating.service.RatingMainStepService;
import gbicc.irs.main.rating.support.RatingStepType;
import gbicc.irs.main.rating.vo.CustRatingInfo;
import gbicc.irs.main.rating.vo.ScoreDetail;


@Controller
@RequestMapping("/irs/ratingMainStep")
public class RatingMainStepController extends
SmartClientRestfulCrudController<RatingMainStep, String,RatingMainStepRepository, RatingMainStepService> {

	
	/**
	 * 评级下一步操作
	 * @param stepId 评级步骤ID
	 * @return	客户评级对象
	 * @throws Exception
	 */
	@RequestMapping(value="nextStep", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<MainRating> nextStep(String stepId) throws Exception{
		MainRating rating = service.nextStep(stepId);
		return  ResponseWrapperBuilder.crud(rating);
	}
	
	@RequestMapping(value="lastStep", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<MainRating> lastStep(String stepId) throws Exception{
		MainRating rating = service.lastStep(stepId);
		return  ResponseWrapperBuilder.crud(rating);
	}
	@RequestMapping(value="checkQualitative", method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<MainRating> checkQualitative(String stepId,@RequestBody Map<String,String> paramValue) throws Exception{
		MainRating rating = service.checkQualitative(stepId, paramValue);
		return  ResponseWrapperBuilder.crud(rating);
	}
	/**
	 * 保存定性指标并且进行评级下一步
	 * @param stepId 评级步骤ID
	 * @param paramValue 定性指标值
	 * @return	客户评级对象
	 * @throws Exception
	 */
	@RequestMapping(value="saveQualitativeAndNextStep", method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<MainRating> saveQualitativeAndNextStep(String stepId,@RequestBody Map<String,String> paramValue) throws Exception{
		MainRating rating = service.saveQualitativeAndNextStep(stepId, paramValue);
		return  ResponseWrapperBuilder.crud(rating);
	}
	/**
	 * 根据评级id获取补录步骤(构建补录页面)
	 * @param ratingId 评级ID
	 * @return	评级步骤对象
	 * @throws Exception
	 */
	/*@RequestMapping(value="getAdditionalStepByRatingId", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RatingMainStep> getAdditionalStepByRatingId(String ratingId) throws Exception{
		RatingMainStep additionalStep = service.getAdditionalStepByRatingId(ratingId);
		return  ResponseWrapperBuilder.query(additionalStep);
	}*/
	
	/**
	 * 保存定性指标并且进行评级下一步
	 * @param 评级报告
	 * @param 
	 * @return	客户及二级指标信息
	 * @throws Exception
	 */
	@RequestMapping(value="custInfo", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<CustRatingInfo> custInfo(String Custno,String type) throws Exception{
		return  ResponseWrapperBuilder.crud(service.custInfo(Custno,type));
	}
	
	/**
	 * 保存定性指标并且进行评级下一步
	 * @param 评级报告
	 * @定性指标
	 */
	@RequestMapping(value="scoreDetail", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<List<ScoreDetail>> scoreDetail(String Custno,String type) throws Exception{
		return  ResponseWrapperBuilder.crud(service.scoreDetail(Custno,type));
	}
	
	/**
	 * 保存定性指标并且进行评级下一步
	 * @param 评级报告
	 * @param 
	 * @return	客户及二级指标信息
	 * @throws Exception
	 */
	@RequestMapping(value="queryByParent", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<List<Map<String, Object>>> queryByParent(String parentId) throws Exception{
		return  ResponseWrapperBuilder.crud(service.queryByParent(parentId));
	}
	
	@RequestMapping(value="bigType", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<List<Map<String, Object>>> bigType(String custNo) throws Exception{
		return  ResponseWrapperBuilder.crud(service.bigType(custNo));
	}
	
	@RequestMapping(value="createRating", method=RequestMethod.GET)
	@ResponseBody
	public String createRating(String bpCode,String tempType,String version) throws Exception{
		return service.createRating(bpCode,tempType,"3.0");
	}

	@RequestMapping(value="findScore", method=RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> findScore(String id) throws Exception{
		return   service.findScore(id);
	}
	
	
}
