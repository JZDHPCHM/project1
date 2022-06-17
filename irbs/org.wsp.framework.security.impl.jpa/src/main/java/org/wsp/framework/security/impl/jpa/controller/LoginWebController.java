package org.wsp.framework.security.impl.jpa.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wsp.framework.mvc.service.SystemParameterService;
import org.wsp.framework.security.service.OuthLoginService;

@Controller
public class LoginWebController {
	private static final String SPRING_THYMELEAF_PREFIX ="spring.thymeleaf.prefix";
	private static final String DEFAULT_LOGIN_TEMPLATE ="org/wsp/framework/security/impl/jpa/view/login.html";
	private static final String DEFAULT_LOGIN_ERROR_TEMPLATE ="org/wsp/framework/security/impl/jpa/view/login.html";
	@Autowired ConfigurableEnvironment env;
	@Autowired SystemParameterService systemParameterService;
	@Autowired OuthLoginService outhLoginService;
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView loginByPage(HttpServletRequest request, Principal principal, HttpServletResponse response) throws Exception{
		String code = request.getParameter("code");
		String tyrz = request.getParameter("authorizeCode");
		if(principal!=null && principal.getName()!=null){
			return new ModelAndView("redirect:/");
		}/*else {
			if(StringUtils.isNotBlank(code)) {
				Integer login = outhLoginService.outhLogin(request, response);
				if(login == 0) {
					return new ModelAndView("redirect:/enterIndex.action");
				}
			}else {
				outhLoginService.authorize(request, response);
			}
		}*/
		if(StringUtils.isNotBlank(tyrz) || StringUtils.isNotBlank(code)) {
			if(StringUtils.isNotBlank(code)) {
				Integer login = outhLoginService.outhLogin(request, response);
				if(login == 0) {
					return new ModelAndView("redirect:/enterIndex.action");
				}
			}else {
				outhLoginService.authorize(request, response);
			}
		}
		
		String loginPagetemplate =getLoginPageTemplate();
		ModelAndView mv =new ModelAndView(loginPagetemplate);
		return mv;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET,consumes={"application/json"})
	public String loginByAjax(HttpServletResponse response) throws Exception{
		throw new Exception("Session Expired, Please Login again.");
	}
	
	@RequestMapping(value="/login-error",method=RequestMethod.GET)
	public String loginError(Model model) throws Exception{
		model.addAttribute("error", true);
		return getLoginErrorPageTemplate();
	}
	
	private String getLoginPageTemplate() throws Exception{
		String loginPagetemplate =systemParameterService.getParameter("loginPageTemplate",DEFAULT_LOGIN_TEMPLATE);
		String thymeleafPrefix =env.getProperty(SPRING_THYMELEAF_PREFIX, String.class, "classpath:/ui/");
		Resource resource =new DefaultResourceLoader().getResource(thymeleafPrefix + loginPagetemplate);
		if(!resource.exists()){
			loginPagetemplate =DEFAULT_LOGIN_TEMPLATE;
		}
		return loginPagetemplate;
	}
	
	private String getLoginErrorPageTemplate() throws Exception{
		String loginPagetemplate =systemParameterService.getParameter("loginErrorPageTemplate",DEFAULT_LOGIN_ERROR_TEMPLATE);
		String thymeleafPrefix =env.getProperty(SPRING_THYMELEAF_PREFIX, String.class, "classpath:/ui/");
		Resource resource =new DefaultResourceLoader().getResource(thymeleafPrefix + loginPagetemplate);
		if(!resource.exists()){
			loginPagetemplate =DEFAULT_LOGIN_ERROR_TEMPLATE;
		}
		return loginPagetemplate;
	}
}
