package gbicc.irs.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;

import gbicc.irs.customer.entity.FinancialStatements;
import gbicc.irs.customer.repository.FinancialStatementsRepository;
import gbicc.irs.customer.service.FinancialStatementsService;

/**
 * 客户主财报管理控制器
 * @author kou
 *
 */
@Controller
@RequestMapping("/irs/cusFinancialStatements")
public class FinancialStatementsController extends SmartClientRestfulCrudController<FinancialStatements,String,FinancialStatementsRepository,FinancialStatementsService>{
	
	
}
