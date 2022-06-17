package gbicc.irs.debtRating.debt.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.entity.RatingDebtStep;
import gbicc.irs.debtRating.debt.repository.RatingDebtStepRepository;
import gbicc.irs.debtRating.debt.service.DebtRatingStepService;
import gbicc.irs.debtRating.debt.support.Dxfx;
import gbicc.irs.debtRating.debt.support.ProjectRatingInfo;

@Controller
@RequestMapping("/irs/DebtStep")
public class DebtStepController extends
		SmartClientRestfulCrudController<RatingDebtStep, String, RatingDebtStepRepository, DebtRatingStepService> {

	/**
	 * 评级下一步操作
	 * 
	 * @param stepId 评级步骤ID
	 * @return 客户评级对象
	 * @throws Exception
	 */
	@RequestMapping(value = "nextStep", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<DebtRating> nextStep(String stepId) throws Exception {
		DebtRating rating = service.nextStep(stepId);
		return ResponseWrapperBuilder.crud(rating);
	}

	@RequestMapping(value = "lastStep", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<DebtRating> lastStep(String stepId) throws Exception {
		DebtRating rating = service.lastStep(stepId);
		return ResponseWrapperBuilder.crud(rating);
	}

	@RequestMapping(value = "checkQualitative", method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<DebtRating> checkQualitative(String stepId, @RequestBody Map<String, String> paramValue)
			throws Exception {
		DebtRating rating = service.checkQualitative(stepId, paramValue);
		return ResponseWrapperBuilder.crud(rating);
	}

	/**
	 * 保存定性指标并且进行评级下一步
	 * 
	 * @param stepId     评级步骤ID
	 * @param paramValue 定性指标值
	 * @return 客户评级对象
	 * @throws Exception
	 */
	@RequestMapping(value = "saveQualitativeAndNextStep", method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper<DebtRating> saveQualitativeAndNextStep(String stepId,
			@RequestBody Map<String, String> paramValue) throws Exception {
		DebtRating rating = service.saveQualitativeAndNextStep(stepId, paramValue);
		return ResponseWrapperBuilder.crud(rating);
	}

	/**
	 * 根据评级id获取补录步骤(构建补录页面)
	 * 
	 * @param ratingId 评级ID
	 * @return 评级步骤对象
	 * @throws Exception
	 */
	/*
	 * @RequestMapping(value="getAdditionalStepByRatingId",
	 * method=RequestMethod.GET)
	 * 
	 * @ResponseBody public ResponseWrapper<RatingMainStep>
	 * getAdditionalStepByRatingId(String ratingId) throws Exception{ RatingMainStep
	 * additionalStep = service.getAdditionalStepByRatingId(ratingId); return
	 * ResponseWrapperBuilder.query(additionalStep); }
	 */

	
	@RequestMapping(value = "projectInfo", method = RequestMethod.GET)
	@ResponseBody
	public ProjectRatingInfo projectInfo(String projectNo) throws Exception {
		return service.projectInfo(projectNo);
	}
	
	@RequestMapping(value="bigType", method=RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> bigType(String id,String type) throws Exception{
			return  service.bigType(id,type);
	}
	
}
