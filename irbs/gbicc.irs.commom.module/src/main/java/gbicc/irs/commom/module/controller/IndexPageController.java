package gbicc.irs.commom.module.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;
import org.wsp.framework.security.util.UrlSsoToken;
import org.wsp.framework.security.util.UrlSsoTokenEncoder;

import gbicc.irs.commom.module.jpa.entity.FrSysAnnoUser;
import gbicc.irs.commom.module.service.FrSysAnnoUserService;
import gbicc.irs.commom.module.service.IndexService;

@Controller
@RequestMapping("/commom/index")
public class IndexPageController {

	@Autowired
	private SystemParameterServiceImpl systemParameterServiceImpl;
	
	@Autowired
	private IndexService indexSevice;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FrSysAnnoUserService frSysAnnoUserService;
	
	/**
	 * 评级默认首页面地址
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "indexPage", method = RequestMethod.GET)
	public String index() throws Exception {
		//判断当前登录人是否有角色，如果角色则跳转至默认首页
		List<String> roleIds = SecurityUtil.getAllRoleIds();
		if(roleIds == null) {
			return "gbicc/irs/commom/module/view/defaultIndex.html";
		}
		return "gbicc/irs/commom/module/view/index.html";
	}
	
	@RequestMapping(value = "task", method = RequestMethod.GET)
	public String task() throws Exception {
		return "gbicc/irs/commom/module/view/task.html";
	}
	

	@RequestMapping(value = "parameter", method = RequestMethod.POST)
	@ResponseBody
	public String parameter() throws Exception {
		String change = systemParameterServiceImpl.getParameter("Front_page_load");
		return change;

	}
	
	@RequestMapping(value = "getTokenAuthentication", method = RequestMethod.GET)
	@ResponseBody
	public String getTokenAuthentication() throws Exception {
		UrlSsoToken urlSsoToken = UrlSsoTokenEncoder.encode(SecurityUtil.getLoginName());
		return urlSsoToken.getUrl();
	}

	/**
	 * 返回首页页面及数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "portal", method = RequestMethod.GET)
	public ModelAndView findBacklogList() throws Exception {
		ModelAndView mv = new ModelAndView("gbicc/irs/commom/module/view/portlet.html");
		List<Map<String, Object>> custOreder = indexSevice.getCustOreder();
		//用户所有角色列表
/*		User currUser = userService.findById(SecurityUtil.getUserId());
		mv.addObject("roles", currUser.getRoles());*/
		//本机构授信金额排名前5的客户
		 mv.addObject("custOreder", custOreder);
		// 待办任务列表
		List<Map<String, Object>> taskList =queryTaskContent();
		mv.addObject("taskList", taskList.size()==0?null:taskList);
		// 已办任务列表
		List<Map<String, Object>> doneTaskList = haveToDoTask();
		mv.addObject("doneTaskList", doneTaskList.size()==0?null:doneTaskList);
		// 已完成任务列表
		List<Map<String, Object>> completeTaskList = completedTask();
		mv.addObject("completeTaskList", completeTaskList.size()==0?null:completeTaskList);
		//消息 公告
		mv.addObject("massageInfoListCnt",indexSevice.queryMsgCnt());
		
		mv.addObject("announceInfoListCnt", indexSevice.queryAnnoCnt());
		
		mv.addObject("zhFlag", indexSevice.isZH());
		return mv;
	}

	@RequestMapping(value = "findCustNo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findCustNo(@RequestParam(value="custNo") String custNo) throws Exception {
		return indexSevice.findCustNo(custNo);
	}

	@RequestMapping(value = "custScore", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> custScore(@RequestParam(value="custNo") String custNo) throws Exception {
		//1.获取评级指标数据
		Map<String,List<Map<String,Object>>> indexDataMap = indexSevice.queryModelIndexData(custNo);
		//2.转图标所需数据及设置风险关注点
		Map<String,Object> res = indexSevice.formatScore(indexDataMap);
		//风险关注点
		res.put("risk", indexSevice.getRiskFocus(custNo));
		return res;
	}
	
	@RequestMapping(value = "custScore2", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> custScore2(@RequestParam(value="ratingId") String ratingId,
			@RequestParam(value="custNo") String custNo) throws Exception {
		//1.获取评级指标数据
		Map<String,List<Map<String,Object>>> indexDataMap = indexSevice.queryModelIndexData2(ratingId,custNo);
		//2.转图标所需数据及设置风险关注点
		Map<String,Object> res = indexSevice.formatScore(indexDataMap);
		//风险关注点
		res.put("risk", indexSevice.getRiskFocus2(ratingId,custNo));
		return res;
	}
	

	@RequestMapping(value = "ratingGroup", method = RequestMethod.POST)
	@ResponseBody
	public List<Object> ratingGroup(@RequestParam(value="type") String type) {
		return indexSevice.ratingGroup(type);
	}
	
	@RequestMapping(value = "queryTaskDbList", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> queryTaskDbList(Pageable pageable) {
		Integer size=pageable.getPageSize();
		Integer number=pageable.getPageNumber();
		Integer start = size*number;
		Integer end = size*number+size;
		String loginNo = SecurityUtil.getLoginName();
		List<Map<String, Object>> list = indexSevice.queryTask(loginNo,start,end);
		Integer total =0;
		if(list.size()>0) {
			total=Integer.parseInt(list.get(0).get("total").toString());
		}
		return ResponseWrapperBuilder.query(new PageImpl<Map<String, Object>>(list,pageable,total)) ;
	}
	
	@RequestMapping(value = "queryTaskYbList", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> queryTaskYbList(Pageable pageable) {
		Integer size=pageable.getPageSize();
		Integer number=pageable.getPageNumber();
		Integer start = size*number;
		Integer end = size*number+size;
		List<Map<String, Object>> list = indexSevice.haveToDoTask(start,end);
		Integer total =0;
		if(list.size()>0) {
			total=Integer.parseInt(list.get(0).get("total").toString());
		}
		return ResponseWrapperBuilder.query(new PageImpl<Map<String, Object>>(list,pageable,total)) ;
	}
	
	@RequestMapping(value = "queryTaskYwcList", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> queryTaskYwcList(Pageable pageable) {
		Integer size=pageable.getPageSize();
		Integer number=pageable.getPageNumber();
		Integer start = size*number;
		Integer end = size*number+size;
		List<Map<String, Object>> list = indexSevice.completedTask(start,end);
		Integer total =0;
		if(list.size()>0) {
			total=Integer.parseInt(list.get(0).get("total").toString());
		}
		return ResponseWrapperBuilder.query(new PageImpl<Map<String, Object>>(list,pageable,total)) ;
	}
	
	
	@RequestMapping(value = "queryTask", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> queryTaskContent() {
		String loginNo = SecurityUtil.getLoginName();
		return indexSevice.queryTask(loginNo,0,10);

	}

	
	@RequestMapping(value = "completedTask", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> completedTask() {
		return indexSevice.completedTask(0,10);
	}

	@RequestMapping(value = "haveToDoTask", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> haveToDoTask() {
		return indexSevice.haveToDoTask(0,10);
	}
	
	//异步刷新
	@RequestMapping(value = "queryMsg", method = RequestMethod.GET)
	public String queryMsg(Model model) {
		 model.addAttribute("massageInfoList",indexSevice.queryMsg());
		 return "gbicc/irs/commom/module/view/portlet::msg_reresh";
	}
	
	//异步刷新
	@RequestMapping(value = "queryAnnouncement", method = RequestMethod.GET)
	public String queryAnnouncement(Model model) {
		 model.addAttribute("announceInfoList",indexSevice.queryAnnouncement());
		 return "gbicc/irs/commom/module/view/portlet::announce_reresh";
	}

	@RequestMapping(value = "queryDetails", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryDetails(String id) {
		return indexSevice.queryDetails(id);
	}

	@RequestMapping(value = "ratingFxfb", method = RequestMethod.POST)
	@ResponseBody
	public List<Object> ratingFxfb() {
		return indexSevice.ratingFxfb();
	}

	@RequestMapping(value = "queryMsgCnt", method = RequestMethod.POST)
	@ResponseBody
	public int queryMsgCnt() {
		return indexSevice.queryMsgCnt();
	}
	
	
	@RequestMapping(value = "saveAnno", method = RequestMethod.POST)
	@ResponseBody
	public void saveAnno(@RequestParam(value="aId") String aId) throws Exception {
		FrSysAnnoUser u = new FrSysAnnoUser();
		u.setaId(aId);
		u.setuId(SecurityUtil.getUserId());
		frSysAnnoUserService.add(u);
	}
	
	@RequestMapping(value = "queryAnnoCnt", method = RequestMethod.POST)
	@ResponseBody
	public int queryAnnoCnt() {
		return indexSevice.queryAnnoCnt();
	}
}
