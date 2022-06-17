package gbicc.irs.debtRating.debt.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.debtRating.debt.entity.CreditIndex;
import gbicc.irs.debtRating.debt.repository.CreditIndexRepository;
import gbicc.irs.debtRating.debt.service.CreditIndexService;
import gbicc.irs.main.rating.service.RatingSelectValuesConfigService;

@Service("CreditIndexServiceImpl")
public class CreditIndexServiceImpl extends DaoServiceImpl<CreditIndex, String, CreditIndexRepository>
		implements CreditIndexService {

	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	RatingSelectValuesConfigService ratingSelectValuesConfigService;
	

	
	

}
