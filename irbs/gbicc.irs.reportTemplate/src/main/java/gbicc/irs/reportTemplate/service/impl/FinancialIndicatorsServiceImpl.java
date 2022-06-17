package gbicc.irs.reportTemplate.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.reportTemplate.jpa.entity.FinancialIndicators;
import gbicc.irs.reportTemplate.jpa.repository.FinancialIndicatorsRepository;
import gbicc.irs.reportTemplate.service.FinancialIndicatorsService;

@Service
public class FinancialIndicatorsServiceImpl extends DaoServiceImpl<FinancialIndicators, String, FinancialIndicatorsRepository>
		implements FinancialIndicatorsService {
	
}
