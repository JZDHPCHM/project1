package gbicc.irs.defaultcustomer.service;

import gbicc.irs.defaultcustomer.entity.DefaultEventLib;
import gbicc.irs.defaultcustomer.repository.DefaultEventLibRepository;

import org.wsp.framework.jpa.service.DaoService;

public interface DefaultEventLibService extends
		DaoService<DefaultEventLib, String,DefaultEventLibRepository> {

}
