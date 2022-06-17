package gbicc.irs.reportTemplate.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinancialIndicators;
import gbicc.irs.reportTemplate.jpa.repository.FinancialIndicatorsRepository;

public interface FinancialIndicatorsService extends DaoService<FinancialIndicators, String, FinancialIndicatorsRepository> {

}
