package gbicc.irs.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.customer.entity.FinAccountData;
import gbicc.irs.customer.repository.FinAccountDataRepository;
import gbicc.irs.customer.service.FinAccountDataService;

/**
 * 财报科目数据控制器
 * @author kou
 *
 */
@Controller
@RequestMapping("/irs/finAccountData")
public class FinAccountDataController extends SmartClientRestfulCrudController<FinAccountData,String,FinAccountDataRepository,FinAccountDataService>{
	
}
