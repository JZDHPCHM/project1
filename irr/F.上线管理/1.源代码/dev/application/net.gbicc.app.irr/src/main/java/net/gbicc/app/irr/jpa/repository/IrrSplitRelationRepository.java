package net.gbicc.app.irr.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrSplitRelationEntity;

/**
* 拆分指标dao接口
*
*/
public interface IrrSplitRelationRepository extends DaoRepository<IrrSplitRelationEntity, String> {

	/**
	 * 根据拆分指标ID删除
	 * @param splitId 拆分指标ID
	 */
	public void deleteBySplitId(String splitId);
}
