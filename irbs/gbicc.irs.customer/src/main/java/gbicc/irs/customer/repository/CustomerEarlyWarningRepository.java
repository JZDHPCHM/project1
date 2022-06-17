package gbicc.irs.customer.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.customer.entity.CustomerEarlyWarning;

public interface CustomerEarlyWarningRepository extends
		DaoRepository<CustomerEarlyWarning, String> {
	
	public List<CustomerEarlyWarning> findByCtmNoAndEarlyWarning(String ctmNo, String earlyWarning);

}
