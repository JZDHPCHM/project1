package gbicc.irs.defaultcustomer.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.defaultcustomer.entity.DefaultCustomerApproveLogs;
import gbicc.irs.defaultcustomer.repository.DefaultCustomerApproveLogsRepository;

public interface DefaultCustomerApproveLogsService extends
		DaoService<DefaultCustomerApproveLogs, String,DefaultCustomerApproveLogsRepository> {
	
}
