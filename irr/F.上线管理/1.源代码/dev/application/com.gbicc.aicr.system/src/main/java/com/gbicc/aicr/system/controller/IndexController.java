package com.gbicc.aicr.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("index")
	public String index(){
		return "gbicc/aicr/view/index.html";
	}
	@RequestMapping("home")
	public String home(){
		return "gbicc/aicr/view/home.html";
	}

}
