package gbicc.irs.customer.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.customer.entity.CustomerEarlyWarning;
import gbicc.irs.customer.repository.CustomerEarlyWarningRepository;

public interface CustomerEarlyWarningService extends
		DaoService<CustomerEarlyWarning, String, CustomerEarlyWarningRepository> {
	
	
}
