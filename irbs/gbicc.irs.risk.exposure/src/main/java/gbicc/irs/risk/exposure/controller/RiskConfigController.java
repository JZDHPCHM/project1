package gbicc.irs.risk.exposure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.risk.exposure.entity.RiskTypeEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskTypeRepository;
import gbicc.irs.risk.exposure.service.RiskTypeService;

@Controller
@RequestMapping("/irs/risk/config")
public class RiskConfigController extends SmartClientRestfulCrudController<RiskTypeEntity, String, RiskTypeRepository, RiskTypeService> {
	/**
	 * 用于管理菜单页面
	 * @return
	 */
	@RequestMapping("type.html")
	public String modelConfig(){
		return "gbicc/irs/risk/exposure/view/riskTypeConfig.html";
	}
	
	@RequestMapping("saveRiskTypeEntity")
	@ResponseBody
	public ResponseWrapper<RiskTypeEntity> saveRiskTypeEntity(@RequestBody RiskTypeEntity risk) throws Exception{
		return ResponseWrapperBuilder.crud(service.getRepository().save(risk));		
	}
	
	
	
}
