package gbicc.irs.reportTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.reportTemplate.jpa.entity.FinancialBasicsLib;
import gbicc.irs.reportTemplate.jpa.repository.FinancialBasicsLibRepository;
import gbicc.irs.reportTemplate.service.FinancialBasicsLibService;

@Controller
@RequestMapping("/reportTemplate/financialBasicsLib")
public class FinancialBasicsLibController extends SmartClientRestfulCrudController<FinancialBasicsLib, String, FinancialBasicsLibRepository, FinancialBasicsLibService> {

	/**
	 * 用于管理菜单页面
	 * @return
	 */
	@RequestMapping("financialBasicsLib.html")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialBasicsLib")
	public String financialBasicsLib(){
		return "gbicc/irs/reportTemplate/view/financialBasicsLib.html";
	}
	
	
}
