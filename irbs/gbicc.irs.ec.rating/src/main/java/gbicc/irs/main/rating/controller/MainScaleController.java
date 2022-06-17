package gbicc.irs.main.rating.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.main.rating.entity.MainScale;
import gbicc.irs.main.rating.repository.MainScaleRepository;
import gbicc.irs.main.rating.service.MainScaleService;

@Controller
@RequestMapping("/irs/mainScale")
public class MainScaleController extends
		SmartClientRestfulCrudController<MainScale, String,MainScaleRepository, MainScaleService> {

	/**
	 * 客户CM配置页面
	 * @return
	 */
	@RequestMapping("mainScale.html")
	public String modelScaleConfig(){
		return "gbicc/irs/rating/config/view/mainScale.html";
	}
	
	
}
