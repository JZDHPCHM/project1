package net.gbicc.app.irr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;

/**
 *	irr模块进入菜单页面的controller
 */
@Controller
@RequestMapping("/irr")
public class IrrEnterIndexController {
	
	private static final String PATH = "net/gbicc/app/irr/view/";
	
	/**
	 * 进入指标库维护界面
	 * @return
	 */
	@RequestMapping("irrIndexMain.action")
	@MenuContributionItem("menuitem.irr.indexmain")
	public String enterIrrIndexMain() {
		return PATH + "irrindexmain.html";
	}
	
	/**
	 * 进入评价计划管理界面
	 * @return
	 */
	@RequestMapping("irrPlanManage.action")
	@MenuContributionItem("menuitem.irr.planmanage")
	public String enterIrrPlanManage() {
		return PATH + "irrplanmanage.html";
	}
	
	/**
	 * 进入报送指标查询
	 * @return
	 */
	@RequestMapping("irrIndexSearch.action")
	@MenuContributionItem("menuitem.irr.indexsearch")
	public String enterIrrHeadIndexSearch() {
		return PATH + "irrindexsearch.html";
	}
	
	/**
	 * 进入指标采集权限管理
	 * @return
	 */
	@RequestMapping("irrCollPerMamage.action")
	@MenuContributionItem("menuitem.irr.collpermanage")
	public String enterIrrCollPerMamage() {
		return PATH + "irrcollpermanage.html";
	}
	
	/**
	 * 进入评估项目管理
	 * @return
	 */
	@RequestMapping("irrProjTypeManage.action")
	@MenuContributionItem("menuitem.irr.projtypemana")
	public String enterIrrProjTypeManage(HttpServletRequest request) {
		return PATH + "irrprojtypemanage.html";
	}
}
