package gbicc.irs.reportTemplate.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccountTemplateMapping;
import gbicc.irs.reportTemplate.jpa.repository.FinancialAccountTemplateMappingRepository;
import gbicc.irs.reportTemplate.service.FinancialAccountTemplateMappingService;

@Service
public class FinancialAccountTemplateMappingServiceImpl extends DaoServiceImpl<FinancialAccountTemplateMapping, String, FinancialAccountTemplateMappingRepository>
		implements FinancialAccountTemplateMappingService {
	
}
