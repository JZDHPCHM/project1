package gbicc.irs.debtRating.debt.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.debtRating.debt.entity.RatingDebtIndex;
import gbicc.irs.main.rating.support.RatingStepType;

public interface DebtIndexRepository extends DaoRepository<RatingDebtIndex, String> {

	/**
	 * 根据评级步骤ID查询评级指标
	 * @param stepId	评级步骤ID
	 * @return	评级指标列表
	 */
	List<RatingDebtIndex> findByRatingStep_Id(String stepId);
	
	/**
	 * 根据评级步骤类型查询评级指标
	 * @param indexType 评级步骤类型
	 * @return 评级指标列表
	 */
	List<RatingDebtIndex> findByIndexType(RatingStepType indexType);
	
}
