package gbicc.irs.rti.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.assetsRating.entity.AssetsRating;
import gbicc.irs.assetsRating.repository.AssetsRatingRepository;


public interface RTIAssetsRatingService extends DaoService<AssetsRating, String, AssetsRatingRepository> {

	public Map<String, String> parsingData(Map<String, Object> map);
}
