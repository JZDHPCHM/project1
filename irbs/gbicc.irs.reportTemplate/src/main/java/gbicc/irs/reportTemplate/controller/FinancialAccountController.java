package gbicc.irs.reportTemplate.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccount;
import gbicc.irs.reportTemplate.jpa.repository.FinancialAccountRepository;
import gbicc.irs.reportTemplate.service.FinancialAccountService;

@Controller
@RequestMapping("/reportTemplate/financialAccount")
public class FinancialAccountController extends SmartClientRestfulCrudController<FinancialAccount, String, FinancialAccountRepository, FinancialAccountService> {

	/**
	 * 用于管理菜单页面
	 * @return
	 */
	@RequestMapping("financialAccount.html")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialAccount")
	public String financialAccount(){
		return "gbicc/irs/reportTemplate/view/financialAccount.html";
	}
	
	/**
	 * 主体-生产型-底表
	 * @return
	 */
	@RequestMapping("principalProductionType")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialAccount")
	public String principalProductionType(){
		return "gbicc/irs/reportTemplate/view/principalProductionType.html";
	}
	/**
	 * 主体-服务型-底表
	 * @return
	 */
	@RequestMapping("serviceOriented")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialAccount")
	public String serviceOriented(){
		return "gbicc/irs/reportTemplate/view/serviceOriented.html";
	}
	/**
	 * 主体-新建型-底表
	 * @return
	 */
	@RequestMapping("newType")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialAccount")
	public String newType(){
		return "gbicc/irs/reportTemplate/view/newType.html";
	}
	/**
	 * 债项-底表
	 * @return
	 */
	@RequestMapping("debtBaseList")
	@MenuContributionItem("menuitem.irs.reportTemplate.financialAccount")
	public String debtBaseList(){
		return "gbicc/irs/reportTemplate/view/debtBaseList.html";
	}
	/**
	 * 主体-承租人评级分布
	 * @return
	 */
	@RequestMapping("tenantRatingDistribution")
	public String tenantRatingDistribution(){
		return "gbicc/irs/reportTemplate/view/tenantRatingDistribution.html";
	}
	/**
	 * 主体-事业部维度
	 * @return
	 */
	@RequestMapping("mainDivisionalDimension")
	public String mainDivisionalDimension(){
		return "gbicc/irs/reportTemplate/view/mainDivisionalDimension.html";
	}
	/**
	 * 主体-国标行业
	 * @return
	 */
	@RequestMapping("mainNationalStandardIndustry")
	public String mainNationalStandardIndustry(){
		return "gbicc/irs/reportTemplate/view/mainNationalStandardIndustry.html";
	}
	/**
	 * 债项-整体债项评级分布
	 * @return
	 */
	@RequestMapping("debtRatingDistribution")
	public String debtRatingDistribution(){
		return "gbicc/irs/reportTemplate/view/debtRatingDistribution.html";
	}
	/**
	 * 债项-事业部维度
	 * @return
	 */
	@RequestMapping("debtDivisionalDimension")
	public String debtDivisionalDimension(){
		return "gbicc/irs/reportTemplate/view/debtDivisionalDimension.html";
	}
	/**
	 * 债项-国标行业
	 * @return
	 */
	@RequestMapping("debtNationalStandardIndustry")
	public String debtNationalStandardIndustry(){
		return "gbicc/irs/reportTemplate/view/debtNationalStandardIndustry.html";
	}
	@RequestMapping("/queryAccoutByStatementTemplate")
	@ResponseBody
	public ResponseWrapper<FinancialAccount> validateFormula(String statementTemplate) throws Exception {
		List<FinancialAccount> reslutFinancialAccount = service.queryAccoutByStatementTemplate(statementTemplate);
		return ResponseWrapperBuilder.query(reslutFinancialAccount);
	}
	
}
