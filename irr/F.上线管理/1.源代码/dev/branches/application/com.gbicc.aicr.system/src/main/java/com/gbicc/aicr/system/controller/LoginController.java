package com.gbicc.aicr.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	/**
	 * 跳转至login.html
	 * @return
	 */
	@RequestMapping("user_login.html")
	public String gotoUserLogin(){
		return "gbicc/aicr/view/login.html";
	}
	
	/**
	 * 登录失败跳转
	 * @param model
	 * @return
	 */
	@RequestMapping("/logFailure.html")
	public String failureLog(Model model) {
		model.addAttribute("error", true);
		return "gbicc/aicr/view/login.html";
	}
}
