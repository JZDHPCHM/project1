package gbicc.irs.reportTemplate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccount;
import gbicc.irs.reportTemplate.jpa.repository.FinancialAccountRepository;
import gbicc.irs.reportTemplate.service.FinancialAccountService;

@Service
public class FinancialAccountServiceImpl extends DaoServiceImpl<FinancialAccount, String, FinancialAccountRepository>
		implements FinancialAccountService {

	@Override
	public List<FinancialAccount> queryAccoutByStatementTemplate(String statementTemplate) {
		List<FinancialAccount> allFinancialAccount = repository.findAll();
		List<FinancialAccount> resultFinancialAccount = new ArrayList<FinancialAccount>();
		for(FinancialAccount fa : allFinancialAccount) {
			if( fa.getStatementTemplate()!=null && fa.getStatementTemplate().contains(statementTemplate)) {
				resultFinancialAccount.add(fa);
			}
		}
		return resultFinancialAccount;
	}
	
}
