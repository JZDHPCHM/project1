package gbicc.irs.main.rating.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.main.rating.entity.RatingSelectValuesConfig;


public interface RatingSelectValuesConfigRepository extends DaoRepository<RatingSelectValuesConfig, String> {

	/**
	 * 根据定义ID获取候选值列表
	 * @param defId	定义ID（调整项，补录项，外部支持）
	 * @return	候选值列表
	 */
	List<RatingSelectValuesConfig> findByDefId(String defId);
	
	/**
	 * 根据定义ID和候选值编号查询候选值
	 * @param defId	定义ID（调整项，补录项，外部支持）
	 * @param realValue 候选值真实值
	 * @return
	 */
	RatingSelectValuesConfig findByDefIdAndRealValue(String defId, String realValue);
	
}
