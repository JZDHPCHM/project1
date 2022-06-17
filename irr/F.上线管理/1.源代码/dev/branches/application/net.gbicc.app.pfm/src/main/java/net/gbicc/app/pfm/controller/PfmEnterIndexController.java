package net.gbicc.app.pfm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;

/**
 * 进入绩效考核页面
 */
@Controller
@RequestMapping("/pfm")
public class PfmEnterIndexController {

	public static final String PATH = "net/gbicc/app/pfm/view/";
	
	/**
	 * 进入数据补录页面
	 */
	@RequestMapping("enterPfmDataSupp.action")
	@MenuContributionItem("menuitem.pfm.datasupp")
	public String enterPfmDataSupp() {
		return PATH + "pfmdatasupp.html";
	}
	
	
	/**
	 * 进入绩效考核结果页面
	 */
	@RequestMapping("enterPfmResult.action")
	@MenuContributionItem("menuitem.pfm.result")
	public String enterPfmResult() {
		return PATH + "pfmresult.html";
	}
	
}
