package gbicc.irs.rti.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.debtRating.debt.entity.CreditRating;
import gbicc.irs.debtRating.debt.repository.CreditRatingRepository;


public interface RTICreditRatingService extends DaoService<CreditRating, String, CreditRatingRepository> {

	public Map<String, String> parsingData(Map<String, Object> map) throws Exception;

}
