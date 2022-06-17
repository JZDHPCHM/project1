package gbicc.irs.customer.services.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.customer.entity.FinAccountData;
import gbicc.irs.customer.repository.FinAccountDataRepository;
import gbicc.irs.customer.service.FinAccountDataService;

@Service
public class FinAccountDataServiceImpl
		extends
		DaoServiceImpl<FinAccountData, String,FinAccountDataRepository>
		implements FinAccountDataService {
	
}
