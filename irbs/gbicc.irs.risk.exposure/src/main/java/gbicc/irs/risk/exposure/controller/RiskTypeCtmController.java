package gbicc.irs.risk.exposure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.risk.exposure.entity.RiskTypeCtmEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskTypeCtmRepository;
import gbicc.irs.risk.exposure.service.RiskTypeCtmService;

@Controller
@RequestMapping("/irs/risk/ctm")
public class RiskTypeCtmController extends SmartClientRestfulCrudController<RiskTypeCtmEntity, String, RiskTypeCtmRepository, RiskTypeCtmService> {
	/**
	 * 用于管理菜单页面
	 * @return
	 */
	@RequestMapping("config.html")
	public String modelConfig(){
		return "gbicc/irs/risk/exposure/view/riskTypeCtmConfig.html";
	}
	/**
	 * 是否已经名单制记录
	 * @param custNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="isExistsRiskTypeCtmEntity",method=RequestMethod.GET)
	@ResponseBody
	public String isExistsRiskTypeCtmEntity(String ctmNo) throws Exception{
		RiskTypeCtmEntity riskTypeCtmEntity = service.findById(ctmNo);
		if(riskTypeCtmEntity!=null) {
			return "1";
		}
		return "0";
	}
}
