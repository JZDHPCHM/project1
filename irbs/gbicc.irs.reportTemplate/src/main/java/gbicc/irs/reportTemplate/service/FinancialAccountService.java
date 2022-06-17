package gbicc.irs.reportTemplate.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccount;
import gbicc.irs.reportTemplate.jpa.repository.FinancialAccountRepository;

public interface FinancialAccountService extends DaoService<FinancialAccount, String, FinancialAccountRepository> {

	List<FinancialAccount> queryAccoutByStatementTemplate(String statementTemplate);
}
