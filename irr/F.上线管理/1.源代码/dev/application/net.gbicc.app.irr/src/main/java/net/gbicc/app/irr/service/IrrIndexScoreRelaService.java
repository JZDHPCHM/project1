package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexScoreRelaEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexScoreRelaRepository;

/**
* 指标得分关联指标
*/
public interface IrrIndexScoreRelaService extends DaoService<IrrIndexScoreRelaEntity, String, IrrIndexScoreRelaRepository> {

	/**
	 * 保存关联关系
	 * @param indexId 指标ID
	 * @param indexCode 指标编码
	 * @param indexName 指标名称
	 * @param relaIds   关联指标ID（多个并中间用，隔开）
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveScoreRela(String indexId,String indexCode,String indexName,String relaIds) throws Exception;

	/**
	 * 通过条件查询关联指标编码
	 * @param indexId 指标ID
	 * @return
	 * @throws Exception
	 */
	public List<String> findRelaIndexCodeByCondition(String indexId) throws Exception;

}
