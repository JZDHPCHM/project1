package gbicc.irs.reportTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccountTemplateMapping;
import gbicc.irs.reportTemplate.jpa.repository.FinancialAccountTemplateMappingRepository;
import gbicc.irs.reportTemplate.service.FinancialAccountTemplateMappingService;

@Controller
@RequestMapping("/reportTemplate/financialAccountTemplateMapping")
public class FinancialAccountTemplateMappingController extends SmartClientRestfulCrudController<FinancialAccountTemplateMapping, String, FinancialAccountTemplateMappingRepository, FinancialAccountTemplateMappingService> {

	/**
	 * 用于管理菜单页面
	 * @return
	 */
	@RequestMapping("financialAccountTemplateMapping.html")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialAccountTemplateMapping")
	public String financialAccountTemplateMapping(){
		return "gbicc/irs/reportTemplate/view/financialAccountTemplateMapping.html";
	}
}
