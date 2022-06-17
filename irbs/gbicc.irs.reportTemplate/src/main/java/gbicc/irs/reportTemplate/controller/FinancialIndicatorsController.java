package gbicc.irs.reportTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.reportTemplate.jpa.entity.FinancialIndicators;
import gbicc.irs.reportTemplate.jpa.repository.FinancialIndicatorsRepository;
import gbicc.irs.reportTemplate.service.FinancialIndicatorsService;

@Controller
@RequestMapping("/reportTemplate/financialIndicators")
public class FinancialIndicatorsController extends SmartClientRestfulCrudController<FinancialIndicators, String, FinancialIndicatorsRepository, FinancialIndicatorsService> {

	/**
	 * 财务指标菜单页面
	 * @return
	 */
	@RequestMapping("financialIndicators.html")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialIndicators")
	public String financialIndicators(){
		return "gbicc/irs/reportTemplate/view/financialIndicators.html";
	}
}
