package gbicc.irs.defaultcustomer.controller;

import gbicc.irs.defaultcustomer.entity.DefaultEventLib;
import gbicc.irs.defaultcustomer.repository.DefaultEventLibRepository;
import gbicc.irs.defaultcustomer.service.DefaultEventLibService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

@Controller
@RequestMapping("/irs/defaultEventlib")
public class DefaultEventLibController
		extends
		SmartClientRestfulCrudController<DefaultEventLib, String, DefaultEventLibRepository, DefaultEventLibService> {
	
	/**
	 * 用于管理菜单页面
	 * @return
	 */
	@RequestMapping("defaultEventlib.html")
	@MenuContributionItem("menuitem.irs.defaultcustomer.defaultEventlib")
	public String ratingMain(){
		return "gbicc/irs/defaultcustomer/view/defaultEventlib.html";
	}
	
	//获取认定违约事件列表
	public Map<String,String> findAllDefaultEvent() throws Exception{
		Map<String,String> eventLib = new HashMap<String, String>();
		List<DefaultEventLib> eventList = service.getRepository().findAll();
		for(DefaultEventLib el : eventList){
			if("001".equals(el.getEventCode())) {
				continue;
			}
			eventLib.put(el.getEventCode(), el.getEventDescribe());
		}
		return eventLib;
	}
}
