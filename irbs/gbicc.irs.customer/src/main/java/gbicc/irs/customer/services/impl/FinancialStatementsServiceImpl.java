package gbicc.irs.customer.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.customer.entity.FinancialStatements;
import gbicc.irs.customer.repository.FinancialStatementsRepository;
import gbicc.irs.customer.service.FinancialStatementsService;
import gbicc.irs.customer.support.ReportCycle;

@Service
public class FinancialStatementsServiceImpl extends DaoServiceImpl<FinancialStatements, String, FinancialStatementsRepository>
		implements FinancialStatementsService {

	
}
