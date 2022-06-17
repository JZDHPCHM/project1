package gbicc.irs.defaultcustomer.repository;

import gbicc.irs.defaultcustomer.entity.DefaultEventLib;

import org.wsp.framework.jpa.repository.DaoRepository;

public interface DefaultEventLibRepository extends
		DaoRepository<DefaultEventLib, String> {
	
	DefaultEventLib findByEventCode(String eventCode);
}
