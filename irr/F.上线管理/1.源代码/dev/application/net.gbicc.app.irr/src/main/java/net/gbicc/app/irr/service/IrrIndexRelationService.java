package net.gbicc.app.irr.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexRelationRepository;

/**
* 指标关联指标
*
*/
public interface IrrIndexRelationService extends DaoService<IrrIndexRelationEntity, String, IrrIndexRelationRepository> {

	/**
	 * 保存关联指标
	 * @param indexId 指标ID
	 * @param indexCode 指标编码
	 * @param indexName 指标名称
	 * @param relaIds 关联指标ID字符串，中间用,隔开
	 * @return
	 */
	public Map<String, Object> saveIndexRela(String indexId,String indexCode,String indexName,String relaIds) throws Exception;
}
