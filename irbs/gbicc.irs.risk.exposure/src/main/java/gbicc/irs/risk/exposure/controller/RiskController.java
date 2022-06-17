package gbicc.irs.risk.exposure.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.flowable.controller.support.ProcessProperties;
import org.wsp.framework.flowable.support.CompleteTaskException;
import org.wsp.framework.flowable.support.CompleteTaskResponse;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.risk.exposure.Wrapper.RiskWrapper;
import gbicc.irs.risk.exposure.entity.RiskEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskRepository;
import gbicc.irs.risk.exposure.service.RiskService;

@Controller
@RequestMapping("/irs/risk")
public class RiskController extends
SmartClientRestfulCrudController<RiskEntity, String, RiskRepository, RiskService>{
	/**
	 * 用于风险暴露申请
	 * @return 
	 */
	@RequestMapping("applyfor.html")
	@MenuContributionItem("menuitem.irs.risk.apply.for")
	public String applyForPage(){
		return "gbicc/irs/risk/exposure/view/riskApplyfor.html";
	}
	/**
	 * 用于风险暴露申请
	 * @return 
	 */
	@RequestMapping("approval.html")
	@MenuContributionItem("menuitem.irs.risk.approve")
	public String approvalPage(){
		return "gbicc/irs/risk/exposure/view/riskApproval.html";
	}
	/**
	 * 用于风险暴露申请
	 * @return 
	 */
	@RequestMapping("config.html")
	@MenuContributionItem("menuitem.irs.risk.riskMdz")
	public String riskMdz(){
		return "gbicc/irs/risk/exposure/view/riskTypeCtmConfig.html";
	}
	/*,
	{
		"id": "gbicc.irs.risk.type.management",
		"parentId": "gbicc.irs.risk.dictonary",
		"order": 2,
		"urlType": "HTML",
		"url": "/irs/risk/ctm/config.html?removeNavbar=true&removeTab=true"
	}*/
	
	@RequestMapping(value="queryRiskApplyFor", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RiskWrapper> fetchRisk(RiskWrapper riskWrapper,Pageable pageable) throws Exception{
		return ResponseWrapperBuilder.query(service.fetchRisk(riskWrapper,pageable));
	}
	
	@RequestMapping(value="queryRiskApprove", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RiskWrapper> queryRiskApprove(RiskWrapper riskWrapper,Pageable pageable) throws Exception{
		return ResponseWrapperBuilder.query(service.fetchRiskApprove(riskWrapper,pageable));
	}
	@RequestMapping(value="applyfor", method=RequestMethod.POST)
	@ResponseBody
	public CompleteTaskResponse applyfor(@RequestBody(required=false) ProcessProperties properties) throws Exception{
		try{
			service.applyfor(properties);			
			return new CompleteTaskResponse();
		}catch(CompleteTaskException exception){
			return CompleteTaskResponse.fromAssigneesJson(exception.getMessage());
		}catch(Exception e){
			throw e;
		}
		
	}
	/**
	 * 撤销
	 * @param id
	 * @param taskId
	 * @throws Exception
	 */
	@RequestMapping(value="cancelRiskApply", method=RequestMethod.GET)
	@ResponseBody
	public void cancelDefaultCust(@RequestParam(name="id",required=false) String id,String taskId) throws Exception{
		 service.removeRiskApplyfor(id,taskId);		
	}
	
	/**
	 * 人工终止
	 * @param pkId
	 * @param taskId
	 * @param doneType
	 * @throws Exception
	 */
	@RequestMapping(value="riskPeopleDone", method=RequestMethod.GET)
	@ResponseBody
	public CompleteTaskResponse peopleDone(String pkId,String taskId,String doneType) throws Exception{
		try{
			service.riskPeopleDone(pkId,taskId,doneType);			
			return new CompleteTaskResponse();
		}catch(CompleteTaskException exception){
			return CompleteTaskResponse.fromAssigneesJson(exception.getMessage());
		}catch(Exception e){
			throw e;
		}
	}
	@RequestMapping(value="approve", method=RequestMethod.POST)
	@ResponseBody
	public CompleteTaskResponse approve(@RequestBody(required=false) ProcessProperties properties) throws Exception{
		try{
			service.approve(properties);			
			return new CompleteTaskResponse();
		}catch(CompleteTaskException exception){
			return CompleteTaskResponse.fromAssigneesJson(exception.getMessage());
		}catch(Exception e){
			throw e;
		}
		
	}
}
