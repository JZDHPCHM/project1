package org.wsp.framework.security.impl.jpa.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.security.service.RoleSynchronousService;

@Controller
@RequestMapping("/irs/inData")
public class RoleSynchronousController {
	
	@Autowired
	RoleSynchronousService roleSynchronousService;

	/**
	 * 初始化数据-同步角色
	 * @param request
	 * @param principal
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/roleSy",method=RequestMethod.GET)
	@ResponseBody
	public Integer roleSy(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer code = roleSynchronousService.prepare();
		if(code == 200) {
			return 0;
		}else {
			return 1;
		}
	}
	
	/**
	 * @初始化数据页面
	 * @return
	 */
	@RequestMapping("inDataIndex.html")
	@MenuContributionItem("menuitem.security.impl.jpa.initializationdata")
	public String ratingMainTask(){
		return "org/wsp/framework/security/impl/jpa/view/initializationData.html";
	}
}
