package gbicc.irs.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.customer.entity.FinancialStatements;
import gbicc.irs.customer.support.ReportCycle;

public interface FinancialStatementsRepository extends
		DaoRepository<FinancialStatements, String> {
	

}
