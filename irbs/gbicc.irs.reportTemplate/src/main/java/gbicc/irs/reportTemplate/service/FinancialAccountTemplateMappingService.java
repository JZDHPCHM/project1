package gbicc.irs.reportTemplate.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccountTemplateMapping;
import gbicc.irs.reportTemplate.jpa.repository.FinancialAccountTemplateMappingRepository;

public interface FinancialAccountTemplateMappingService extends DaoService<FinancialAccountTemplateMapping, String, FinancialAccountTemplateMappingRepository> {

}
