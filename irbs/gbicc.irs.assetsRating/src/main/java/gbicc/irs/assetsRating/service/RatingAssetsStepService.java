package gbicc.irs.assetsRating.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.assetsRating.entity.RatingAssetsStep;
import gbicc.irs.assetsRating.repository.RatingAssetsStepRepository;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.vo.CustRatingInfo;
import gbicc.irs.main.rating.vo.ScoreDetail;

public interface RatingAssetsStepService extends DaoService<RatingAssetsStep, String, RatingAssetsStepRepository> {

	//MainRating saveQualitativeAndNextStep(String stepId, Map<String, String> paramValue) throws Exception;
	
	List<RatingAssetsStep> getAdditionalStepByRatingId(String ratingId) throws Exception;

}
