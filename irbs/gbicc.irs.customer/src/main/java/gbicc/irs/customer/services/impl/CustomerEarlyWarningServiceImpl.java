package gbicc.irs.customer.services.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.customer.entity.CustomerEarlyWarning;
import gbicc.irs.customer.repository.CustomerEarlyWarningRepository;
import gbicc.irs.customer.service.CustomerEarlyWarningService;

@Service
public class CustomerEarlyWarningServiceImpl
		extends
		DaoServiceImpl<CustomerEarlyWarning, String, CustomerEarlyWarningRepository>
		implements CustomerEarlyWarningService {

	
}
