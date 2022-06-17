package gbicc.irs.main.rating.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.entity.RatingMainStep;
import gbicc.irs.main.rating.support.RatingStepType;

public interface RatingMainStepRepository extends DaoRepository<RatingMainStep, String> {


	/**
	 * 根据评级ID和评级步骤类型查询评级步骤
	 * @param ratingId	评级ID
	 * @param stepType	评级步骤类型
	 * @return 评级步骤
	 */
	RatingMainStep findByRatingMain_idAndStepType(String ratingId,RatingStepType stepType);
	
	/**
	 * 根据评级ID和评级步骤类型查询评级步骤
	 * @param ratingId	评级ID
	 * @param stepType	评级步骤类型
	 * @return 评级步骤
	 */
	List<RatingMainStep> findByRatingMain_id(String ratingId);
	
}
