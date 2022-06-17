package gbicc.irs.reportTemplate.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.reportTemplate.jpa.entity.FinancialStatementTemplate;

public interface FinancialStatementTemplateRepository extends DaoRepository<FinancialStatementTemplate, String> {

	List<FinancialStatementTemplate> findByFsType(String fsType);
}
