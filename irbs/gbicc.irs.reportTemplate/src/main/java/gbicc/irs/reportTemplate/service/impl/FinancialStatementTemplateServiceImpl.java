package gbicc.irs.reportTemplate.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccount;
import gbicc.irs.reportTemplate.jpa.entity.FinancialAccountTemplateMapping;
import gbicc.irs.reportTemplate.jpa.entity.FinancialStatementTemplate;
import gbicc.irs.reportTemplate.jpa.repository.FinancialStatementTemplateRepository;
import gbicc.irs.reportTemplate.service.FinancialAccountService;
import gbicc.irs.reportTemplate.service.FinancialAccountTemplateMappingService;
import gbicc.irs.reportTemplate.service.FinancialStatementTemplateService;

@Service
public class FinancialStatementTemplateServiceImpl extends DaoServiceImpl<FinancialStatementTemplate, String, FinancialStatementTemplateRepository>
		implements FinancialStatementTemplateService {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private FinancialAccountService financialAccountService;
	@Autowired
	private FinancialAccountTemplateMappingService financialAccountTemplateMappingService;
	
	@Override
	public void generateStatementTemplate() throws Exception {
		jdbc.update("delete from NS_RT_FINAN_STAT_TEMPLATE");
		List<FinancialAccount> accounts =  financialAccountService.getRepository().findAll();
		Map<String,List<FinancialAccount>> groupAccount = new HashMap<String,List<FinancialAccount>>();
		if(accounts != null) {
			for(FinancialAccount fa : accounts) {
				Set<String> statementList = fa.getStatementTemplate();
				if(statementList != null) {
					for(String statementTemplate : statementList) {
						if(groupAccount.containsKey(statementTemplate)) {
							groupAccount.get(statementTemplate).add(fa);
						}else {
							List<FinancialAccount> mapList = new ArrayList<FinancialAccount>();
							mapList.add(fa);
							groupAccount.put(statementTemplate, mapList);
						}
					}
				}
			}
		}
		Iterator<String> itKey = groupAccount.keySet().iterator();
		while(itKey.hasNext()) {
			String k = itKey.next();
			FinancialStatementTemplate financialStatementTemplate = new FinancialStatementTemplate();
			financialStatementTemplate.setFsType(k);
			financialStatementTemplate.setFsValid(true);
			FinancialStatementTemplate saveEntity = this.add(financialStatementTemplate);
			for(FinancialAccount fa : groupAccount.get(k)) {
				FinancialAccountTemplateMapping accountMapping = new FinancialAccountTemplateMapping();
				accountMapping.setFinanStateTemplate(saveEntity);
				accountMapping.setFaCategory(fa.getFaCategory());
				accountMapping.setAmNumber(fa.getFaNumber());
				accountMapping.setFaFormerName(fa.getFaName());
				accountMapping.setTemplateFaCode(fa.getFaCode());
				accountMapping.setTemplateFaName(fa.getFaName());
				if(fa.getFaNumber()!=null&& fa.getFaNumber()<10){
					accountMapping.setFaFormerCode("000"+fa.getFaNumber());
				}
				if(fa.getFaNumber()!=null&&fa.getFaNumber()>=10&&fa.getFaNumber()<=100){
					accountMapping.setFaFormerCode("00"+fa.getFaNumber());
				}
				if(fa.getFaNumber()!=null&&fa.getFaNumber()>=100&&fa.getFaNumber()<=999){
					accountMapping.setFaFormerCode("0"+fa.getFaNumber());
				}
				financialAccountTemplateMappingService.add(accountMapping);
			}
		}
	}
	
}
