package net.gbicc.app.irr.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;

/**
* 指标结果关联
*
*/
public interface IrrIndexRelationRepository extends DaoRepository<IrrIndexRelationEntity, String> {

	/**
	 * 根据指标ID删除关联指标数据
	 * @param indexId 指标ID
	 */
	public void deleteByIndexId(String indexId);
	
	/**
	 * 根据指标ID查找关联指标数据
	 * @param indexId 指标ID
	 * @return
	 */
	public List<IrrIndexRelationEntity> findByIndexId(String indexId);
}
