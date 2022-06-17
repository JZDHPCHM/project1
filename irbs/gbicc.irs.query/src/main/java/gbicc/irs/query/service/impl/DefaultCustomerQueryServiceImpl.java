package gbicc.irs.query.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.jdbc.sql.dialect.Dialect;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import gbicc.irs.defaultcustomer.entity.CustomerWrapper;
import gbicc.irs.query.entity.DCQueryParams;
import gbicc.irs.query.entity.DCustomer;
import gbicc.irs.query.entity.HistoryDCustomer;
import gbicc.irs.query.service.DefaultCustomerQueryService;

@Service
public class DefaultCustomerQueryServiceImpl implements DefaultCustomerQueryService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired 
	private Dialect dialect;
	@Autowired
	private UserService userService;
	@Autowired
	private SystemParameterServiceImpl systemParameterServiceImpl;
	
	
	@Override
	public Page<DCustomer> fetchDefaultCustomer(DCQueryParams params, Pageable pageable) throws Exception{
		
	
		return null;
	}


	@Override
	public List<HistoryDCustomer> findAllDCTaskByCustNo(String custNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
