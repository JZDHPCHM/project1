package gbicc.irs.query.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import gbicc.irs.query.entity.DCQueryParams;
import gbicc.irs.query.entity.DCustomer;
import gbicc.irs.query.entity.HistoryDCustomer;

public interface DefaultCustomerQueryService {
	
	public Page<DCustomer> fetchDefaultCustomer(DCQueryParams params, Pageable pageable) throws Exception;
	
	public List<HistoryDCustomer> findAllDCTaskByCustNo(String custNo);

}
