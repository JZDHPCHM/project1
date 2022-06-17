package gbicc.irs.assetsRating.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.assetsRating.entity.AssetsIndex;
import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.support.RatingStepType;

public interface AssetsIndexRepository extends DaoRepository<AssetsIndex, String> {

	/**
	 * 根据评级步骤ID查询评级指标
	 * @param stepId	评级步骤ID
	 * @return	评级指标列表
	 */
	List<AssetsIndex> findByRatingStep_Id(String stepId);
	
	/**
	 * 根据评级步骤类型查询评级指标
	 * @param indexType 评级步骤类型
	 * @return 评级指标列表
	 */
	List<AssetsIndex> findByIndexType(RatingStepType indexType);
	
}
