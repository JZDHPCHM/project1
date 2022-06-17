package gbicc.irs.customer.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.customer.entity.FinancialStatements;
import gbicc.irs.customer.repository.FinancialStatementsRepository;

public interface FinancialStatementsService extends
		DaoService<FinancialStatements, String, FinancialStatementsRepository> {
	
	
	
}
