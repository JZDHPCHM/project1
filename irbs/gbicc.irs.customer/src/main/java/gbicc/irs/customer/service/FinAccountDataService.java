package gbicc.irs.customer.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.customer.entity.FinAccountData;
import gbicc.irs.customer.repository.FinAccountDataRepository;

public interface FinAccountDataService extends
		DaoService<FinAccountData, String, FinAccountDataRepository> {

}
