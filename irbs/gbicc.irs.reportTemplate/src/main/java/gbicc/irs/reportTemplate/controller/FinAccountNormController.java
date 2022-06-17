package gbicc.irs.reportTemplate.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.reportTemplate.jpa.entity.FinAccountNorm;
import gbicc.irs.reportTemplate.jpa.repository.FinAccountNormRepository;
import gbicc.irs.reportTemplate.service.FinAccountNormService;


@Controller
@RequestMapping("/irs/FinAccountNormController/")
public class FinAccountNormController 
extends SmartClientRestfulCrudController<FinAccountNorm,String,FinAccountNormRepository,FinAccountNormService>{
	
	@RequestMapping(value="queryNorm", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<FinAccountNorm> queryNorm(){
		return  ResponseWrapperBuilder.query(service.queryNorm());
	}
	
	@RequestMapping(value="resurltSelect", method=RequestMethod.GET)
	@ResponseBody
	public Map<Object,Object> resurltSelect(){
		return  service.resurltSelect();
	}
}
