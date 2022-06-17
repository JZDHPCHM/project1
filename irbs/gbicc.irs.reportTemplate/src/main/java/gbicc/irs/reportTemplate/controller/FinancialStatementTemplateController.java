package gbicc.irs.reportTemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.reportTemplate.jpa.entity.FinancialStatementTemplate;
import gbicc.irs.reportTemplate.jpa.repository.FinancialStatementTemplateRepository;
import gbicc.irs.reportTemplate.service.FinAccountNormService;
import gbicc.irs.reportTemplate.service.FinancialStatementTemplateService;

@Controller
@RequestMapping("/reportTemplate/financialStatementTemplate")
public class FinancialStatementTemplateController extends SmartClientRestfulCrudController<FinancialStatementTemplate, String, FinancialStatementTemplateRepository, FinancialStatementTemplateService> {

	@Autowired
	FinAccountNormService normSer;
	/**
	 * 用于管理菜单页面
	 * @return
	 */
	@RequestMapping("financialStatementTemplate.html")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialStatementTemplate")
	public String financialStatementTemplate(){
		return "gbicc/irs/reportTemplate/view/financialStatementTemplate.html";
	}
	
	/**
	 * 生成系统内置财报模板
	 */
	@RequestMapping(value="/generateStatementTemplate",method=RequestMethod.GET)
	@ResponseBody
	public void generateStatementTemplate() throws Exception{
		service.generateStatementTemplate();
		normSer.initNorm();
	}
	
}
