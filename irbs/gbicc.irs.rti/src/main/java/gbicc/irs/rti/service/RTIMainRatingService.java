package gbicc.irs.rti.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.repository.MainRatingRepository;

public interface RTIMainRatingService extends DaoService<MainRating, String, MainRatingRepository> {

	public Map<String, String> parsingData(Map<String, Object> map);

}
