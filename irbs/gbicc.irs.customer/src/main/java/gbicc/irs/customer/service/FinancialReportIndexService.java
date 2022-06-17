package gbicc.irs.customer.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.customer.entity.FinancialReportIndex;
import gbicc.irs.customer.repository.FinancialReportIndexRepository;
import gbicc.irs.customer.support.BenchmarkingCompany;

public interface FinancialReportIndexService extends
DaoService<FinancialReportIndex, String, FinancialReportIndexRepository> {
	
	public Map<String, Object> queryReportDtAndReportType(String reportDt,String reportType,BenchmarkingCompany benchmarkingCompany,String ctmNo)throws Exception ;

	/**
	 * 根据客户编号查看客户指标期次
	 * @param customerNo
	 * @return
	 */
	public List<String> queryCustomerIndexReportDate(String customerNo);
}
