package net.gbicc.app.irr.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;

/**
* 拆分指标信息
*/
public interface IrrSplitIndexRepository extends DaoRepository<IrrSplitIndexEntity, String> {

	/**
	 * 根据源指标ID查询有多少拆分指标
	 * @param sourceIndexId 源指标ID
	 * @return
	 */
	public Long countBySourceIndexId(String sourceIndexId);
	
	/**
	 * 根据拆分指标编码查询拆分指标
	 * @param splitCode
	 * @return
	 */
	public List<IrrSplitIndexEntity> findBySplitCode(String splitCode);
}
