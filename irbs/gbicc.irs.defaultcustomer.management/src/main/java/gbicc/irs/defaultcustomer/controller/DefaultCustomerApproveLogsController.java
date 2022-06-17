package gbicc.irs.defaultcustomer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.defaultcustomer.entity.DefaultCustomerApproveLogs;
import gbicc.irs.defaultcustomer.repository.DefaultCustomerApproveLogsRepository;
import gbicc.irs.defaultcustomer.service.DefaultCustomerApproveLogsService;

@Controller
@RequestMapping("/irs/defaultCustomerApprove")
public class DefaultCustomerApproveLogsController
		extends
		SmartClientRestfulCrudController<DefaultCustomerApproveLogs, String, DefaultCustomerApproveLogsRepository, DefaultCustomerApproveLogsService> {
	
}
