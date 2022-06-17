package com.gbicc.aicr.system.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;

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
	//移动端主体评级报告
	@RequestMapping("mainRating")
	public String mainRating(){
		return "gbicc/aicr/view/mainRating.html";
	}
	//移动端债项评级报告
	@RequestMapping("debtRating")
	public String debtRating(){
		return "gbicc/aicr/view/debtRating.html";
	}
	//移动端风险预警报告
	@RequestMapping("riskWarning")
	public String riskWarning(){
		return "gbicc/aicr/view/riskWarning.html";
	}
}
