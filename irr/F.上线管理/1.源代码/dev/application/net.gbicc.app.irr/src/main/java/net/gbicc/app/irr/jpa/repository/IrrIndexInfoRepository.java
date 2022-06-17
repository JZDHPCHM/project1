package net.gbicc.app.irr.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;

/**
* 指标信息
*/
public interface IrrIndexInfoRepository extends DaoRepository<IrrIndexInfoEntity, String> {

	/**
	 * 根据指标编码查找指标
	 * @param indexCode 指标编码
	 * @return
	 */
	public List<IrrIndexInfoEntity> findByIndexCode(String indexCode);
	
	/**
	 * 根据指标编码和指标状态查询指标
	 * @param indexCode 指标编码
	 * @param indexStatus 指标状态
	 * @return
	 */
	public List<IrrIndexInfoEntity> findByIndexCodeAndIndexStatus(String indexCode,String indexStatus);
	
	/**
	 * 根据评估项目ID查询指标（根据行次排序）
	 * @param projTypeId 评估项目ID
	 * @return
	 */
	List<IrrIndexInfoEntity> findByProjTypeIdOrderByIndexLine(String projTypeId);
}
