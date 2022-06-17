package gbicc.irs.defaultcustomer.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.defaultcustomer.entity.DefaultCustomerApproveLogs;
import gbicc.irs.defaultcustomer.repository.DefaultCustomerApproveLogsRepository;
import gbicc.irs.defaultcustomer.service.DefaultCustomerApproveLogsService;

@Service
public class DefaultCustomerApproveLogsServiceImpl
		extends
		DaoServiceImpl<DefaultCustomerApproveLogs, String, DefaultCustomerApproveLogsRepository>
		implements DefaultCustomerApproveLogsService {

}
