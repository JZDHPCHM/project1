package gbicc.irs.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.customer.entity.FinancialReportIndex;

public interface FinancialReportIndexRepository extends
		DaoRepository<FinancialReportIndex, String> {
	
	 @Query(value="select * from ns_financial_report_data where FD_FINANCIAL_REPORT_ID=?1 and FD_TARGET_TYPE=1" ,nativeQuery = true)
	 List<FinancialReportIndex> findTargetFinancialReportId(String reportId);
	 
	 List<FinancialReportIndex> findByCustomerNoAndReportSpecifAndReportDate(String customerNo,String reportSpecif,String reportDate);
	 
	 List<FinancialReportIndex> findByCustomerNoAndIndexNameOrderByReportDateDesc(String customerNo, String indexType);
}