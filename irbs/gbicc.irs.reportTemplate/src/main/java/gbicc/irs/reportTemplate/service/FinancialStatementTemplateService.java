package gbicc.irs.reportTemplate.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinancialStatementTemplate;
import gbicc.irs.reportTemplate.jpa.repository.FinancialStatementTemplateRepository;

public interface FinancialStatementTemplateService extends DaoService<FinancialStatementTemplate, String, FinancialStatementTemplateRepository> {

	/**
	 * 生成内置财报模板
	 * @throws Exception
	 */
	void generateStatementTemplate() throws Exception;
	
}
