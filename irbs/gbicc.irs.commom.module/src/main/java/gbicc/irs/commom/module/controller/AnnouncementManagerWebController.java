package gbicc.irs.commom.module.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wsp.framework.jpa.model.announcement.entity.Announcement;
import org.wsp.framework.jpa.model.announcement.repository.AnnouncementRepository;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.service.AnnouncementService;

/**
 * 系统公告控制器
 * @author wangshaoping
 *
 */
@Controller
@RequestMapping("/system/announcementManager")
public class AnnouncementManagerWebController extends SmartClientRestfulCrudController<Announcement,String,AnnouncementRepository,AnnouncementService>{
	/**
	 * 系统管理/消息管理菜单入口
	 * @return 视图
	 */
	@RequestMapping("announcementManager.html")
	@MenuContributionItem("menuitem.system.announcement")
	public ModelAndView notification(){
		ModelAndView mv =new ModelAndView("gbicc/irs/commom/module/view/announcementManager.html");
		return mv;
	}
	
}
