package gbicc.irs.commom.module.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wsp.framework.jpa.model.announcement.entity.Announcement;
import org.wsp.framework.jpa.model.announcement.repository.AnnouncementRepository;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.service.AnnouncementService;

/**
 * 系统公告控制器
 * @author wangshaoping
 *
 */
@Controller
@RequestMapping("/system/announcement")
public class AnnouncementWebController extends SmartClientRestfulCrudController<Announcement,String,AnnouncementRepository,AnnouncementService>{
	/**
	 * 系统管理/消息管理菜单入口
	 * @return 视图
	 */
	@RequestMapping("announcement.html")
	@MenuContributionItem("menuitem.profile.announcement")
	public ModelAndView notification(){
		ModelAndView mv =new ModelAndView("gbicc/irs/commom/module/view/announcement.html");
		return mv;
	}

}
