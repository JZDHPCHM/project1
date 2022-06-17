package gbicc.irs.defaultcustomer.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.defaultcustomer.entity.CustomerOverdue;
import gbicc.irs.defaultcustomer.repository.CustomerOverdueRepository;
import gbicc.irs.defaultcustomer.service.CustomerOverdueService;

@Service
public class CustomerOverdueServiceImpl extends DaoServiceImpl<CustomerOverdue, String, CustomerOverdueRepository> implements CustomerOverdueService{

}
