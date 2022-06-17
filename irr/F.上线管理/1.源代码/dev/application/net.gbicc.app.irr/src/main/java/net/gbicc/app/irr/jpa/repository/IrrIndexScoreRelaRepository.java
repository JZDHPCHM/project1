package net.gbicc.app.irr.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrIndexScoreRelaEntity;

/**
* 指标得分关联
*
*/
public interface IrrIndexScoreRelaRepository extends DaoRepository<IrrIndexScoreRelaEntity, String> {

	/**
	 * 根据指标ID查询关联数据
	 * @param indexId 指标ID
	 * @return
	 */
	public List<IrrIndexScoreRelaEntity> findByIndexId(String indexId);
	/**
	 * 根据指标ID删除关联关系
	 * @param indexId 指标ID
	 */
	public void deleteByIndexId(String indexId);
	
}
