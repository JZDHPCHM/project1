package net.gbicc.app.irr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* IRR流程进入页面controller
*
*/
@Controller
@RequestMapping("/irr/process")
public class IrrProcessEnterIndexController {

	private static final String PATH = "net/gbicc/app/irr/view/process/";
	
	/**
	 * 进入采集数据页面
	 * @return
	 */
	@RequestMapping("/collIndex.html")
	public String enterProcessCollIndex() {
		return PATH + "collindex.html";
	}
	
	/**
	 * 进入审核数据页面
	 * @return
	 */
	@RequestMapping("/examIndex.html")
	public String enterProcessExamIndex() {
		return PATH + "examindex.html";
	}
	
	/**
	 * 进入复核数据页面
	 * @return
	 */
	@RequestMapping("/reviewIndex.html")
	public String enterProcessReviewIndex() {
		return PATH + "reviewindex.html";
	}
	
	/**
	 * 进入汇总数据页面
	 * @return
	 */
	@RequestMapping("/summIndex.html")
	public String enterProcessSummIndex() {
		return PATH + "summindex.html";
	}
	
	/**
	 * 进入总经理审核数据页面
	 * @return
	 */
	@RequestMapping("/managerIndex.html")
	public String enterProcessManagerIndex() {
		return PATH + "managerindex.html";
	}
	
	/**
	 * 进入数据报数页面
	 * @return
	 */
	@RequestMapping("/reportIndex.html")
	public String enterReportIndex() {
		return PATH + "reportindex.html";
	}
}
