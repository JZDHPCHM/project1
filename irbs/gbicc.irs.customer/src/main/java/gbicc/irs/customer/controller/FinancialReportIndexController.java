package gbicc.irs.customer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.controller.support.SmartClientRestfulCrudController;

import gbicc.irs.customer.entity.FinancialReportIndex;
import gbicc.irs.customer.repository.FinancialReportIndexRepository;
import gbicc.irs.customer.service.FinancialReportIndexService;
import gbicc.irs.customer.support.BenchmarkingCompany;


@Controller
@RequestMapping("/irs/financial/report/data")
public class FinancialReportIndexController extends SmartClientRestfulCrudController<FinancialReportIndex,String,FinancialReportIndexRepository,FinancialReportIndexService>{
	
	
	@RequestMapping(value="queryReportDtAndReportType", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryReportDtAndReportType(@RequestParam(name="reportDt")String reportDt,
			@RequestParam(name="reportType")String reportType,
			@RequestParam(name="benchmarkingCompany")BenchmarkingCompany benchmarkingCompany,
			@RequestParam(name="ctmNo")String ctmNo) throws Exception{
		return service.queryReportDtAndReportType(reportDt,reportType,benchmarkingCompany,ctmNo) ;
	}
	
	
	@RequestMapping(value="queryCustomerIndexReportDate", method=RequestMethod.GET)
	@ResponseBody
	public List<String> queryCustomerIndexReportDate(String customerNo) {
		List<String> years = service.queryCustomerIndexReportDate(customerNo);
		return years;
	}
}
