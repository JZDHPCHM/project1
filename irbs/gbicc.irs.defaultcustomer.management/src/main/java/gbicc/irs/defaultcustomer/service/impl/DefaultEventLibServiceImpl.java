package gbicc.irs.defaultcustomer.service.impl;

import gbicc.irs.defaultcustomer.entity.DefaultEventLib;
import gbicc.irs.defaultcustomer.repository.DefaultEventLibRepository;
import gbicc.irs.defaultcustomer.service.DefaultEventLibService;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

@Service
public class DefaultEventLibServiceImpl
		extends
		DaoServiceImpl<DefaultEventLib, String, DefaultEventLibRepository>
		implements DefaultEventLibService {
	
}
