package gbicc.irs.defaultcustomer.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.defaultcustomer.entity.CustomerOverdue;
import gbicc.irs.defaultcustomer.repository.CustomerOverdueRepository;

public interface CustomerOverdueService extends DaoService<CustomerOverdue, String, CustomerOverdueRepository>{

}
