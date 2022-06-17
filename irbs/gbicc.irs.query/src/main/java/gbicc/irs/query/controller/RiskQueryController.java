package gbicc.irs.query.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.risk.exposure.Wrapper.RiskWrapper;
import gbicc.irs.risk.exposure.entity.RiskEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskRepository;
import gbicc.irs.risk.exposure.service.RiskService;

@Controller
@RequestMapping("/irs/riskQuery")
public class RiskQueryController extends
SmartClientRestfulCrudController<RiskEntity, String, RiskRepository, RiskService>{
	/**
	 * 风险暴露分类查询列表页面
	 * @return
	 */
	@RequestMapping("riskList.html")
	@MenuContributionItem("menuitem.irs.risk.query")
	public String start(){
		return "gbicc/irs/query/view/riskList.html";
	}
	@RequestMapping(value="queryRiskData", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<RiskWrapper> fetchEnableRisk(RiskWrapper riskWrapper,Pageable pageable) throws Exception{
		return ResponseWrapperBuilder.query(service.fetchEnableRisk(riskWrapper,pageable));
	}
}
