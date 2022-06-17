package gbicc.irs.debtRating.debt.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.debtRating.debt.entity.RatingDebtStep;

public interface RatingDebtStepRepository extends DaoRepository<RatingDebtStep, String> {


	/**
	 * 根据评级ID和评级步骤类型查询评级步骤
	 * @param ratingId	评级ID
	 * @param stepType	评级步骤类型
	 * @return 评级步骤
	 */
	RatingDebtStep findByRatingMain_idAndStepType(String ratingId,RatingDebtStep stepType);
	
	/**
	 * 根据评级ID和评级步骤类型查询评级步骤
	 * @param ratingId	评级ID
	 * @param stepType	评级步骤类型
	 * @return 评级步骤
	 */
	List<RatingDebtStep> findByRatingMain_id(String ratingId);
	
}
