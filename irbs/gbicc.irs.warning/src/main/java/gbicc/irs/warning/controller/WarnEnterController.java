package gbicc.irs.warning.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.service.SystemParameterService;

/**
 * 进入菜单controller
 * 
 * @author FC
 * @version v1.0 2019年9月19日
 */
@Controller
@RequestMapping("/warn")
public class WarnEnterController {

    private static final String PATH = "gbicc/irs/warning/view/";
    @Autowired
    private SystemParameterService systemParameterService;
    
    /**
     * 进入规则配置页面
     *
     * @return
     */
    @RequestMapping("/warnConfig.html")
    @MenuContributionItem("menuitem.rule.batchrulez")
    public String enterWarnConfig(HttpServletRequest request, Model model) {
    	
        String ruleTypeUrl = "";
        String ruleSubTypeUrl = "";
        try {
            ruleTypeUrl = this.systemParameterService.getParameter("parameter.after.rule.type.url");
            ruleSubTypeUrl = this.systemParameterService.getParameter("parameter.after.rule.sub.type.url");
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("ruleTypeUrl", ruleTypeUrl);
        model.addAttribute("ruleSubTypeUrl", ruleSubTypeUrl);
        return PATH + "ruleProperties.html";
    }
    
	@RequestMapping("/enterWarningHistory.html")
	 @MenuContributionItem("menuitem.irs.warning.history")
	public String enterWarningHistory() {
		return PATH + "warningHistory.html";
	}

}
