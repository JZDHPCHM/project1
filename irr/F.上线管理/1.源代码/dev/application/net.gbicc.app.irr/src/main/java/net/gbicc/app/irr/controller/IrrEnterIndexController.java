package net.gbicc.app.irr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.security.util.SecurityUtil;

import net.gbicc.app.irr.service.IrrAuthService;

/**
 *	irr模块进入菜单页面的controller
 */
@Controller
@RequestMapping("/irr")
public class IrrEnterIndexController {
	
	private static final String PATH = "net/gbicc/app/irr/view/";
    @Autowired
    private IrrAuthService irrAuthService;
	
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
	
	/**
	 * 进入采集指标查询
	 * @return
	 */
	@RequestMapping("irrCollIndexSearch.action")
	@MenuContributionItem("menuitem.irr.collindexsearch")
	public String enterIrrCollIndexSearch() {
		return PATH + "irrcollindexsearch.html";
	}
	
	/**
	 * 进入历史数据导入界面
	 * @return
	 */
	@RequestMapping("irrHisDataImpl.action")
	@MenuContributionItem("menuitem.irr.hisdataimp")
	public String enterIrrHisDataImpl() {
		return PATH + "irrhisdataimpl.html";
	}

    /**
     * 进入五大类导入界面
     *
     * @return
     */
    @RequestMapping("irrFiveCate.html")
    @MenuContributionItem("menuitem.irr.irrfivecate")
    public String enterFiveCateImp() {
        return PATH + "irrfivecate.html";
    }

    /**
     * 进入授权管理界面
     *
     * @return
     */
    @RequestMapping("authManage.action")
    @MenuContributionItem("menuitem.irr.authmanage")
    public String enterAuthManage(Model model) {
        try {
            String userId = SecurityUtil.getUserId();
            Boolean isAdmin = irrAuthService.isAdmin(userId);
            model.addAttribute("userId", userId);
            model.addAttribute("isAdmin", isAdmin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PATH + "authmanage.html";
    }

    /**
     * 进入授权任务日志界面
     *
     * @return
     */
    @RequestMapping("authTaskLog.action")
    @MenuContributionItem("menuitem.irr.authtasklog")
    public String enterAuthTaskLog() {
        return PATH + "authtasklog.html";
    }
}
