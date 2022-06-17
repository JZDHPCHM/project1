package gbicc.irs.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.customer.entity.CustomerEarlyWarning;
import gbicc.irs.customer.repository.CustomerEarlyWarningRepository;
import gbicc.irs.customer.service.CustomerEarlyWarningService;


@Controller
@RequestMapping("/irs/Customer/EarlyWarning")
public class CustomerEarlyWarningController extends SmartClientRestfulCrudController<CustomerEarlyWarning,String,CustomerEarlyWarningRepository,CustomerEarlyWarningService>{
	
	
	
	
}
