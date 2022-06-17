package net.gbicc.app.irr.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;

/**
* 评估项目
*/
public interface IrrProjTypeRepository extends DaoRepository<IrrProjTypeEntity, String> {

	/**
	 * 根据父编码查询评估项目
	 * @param pCode 父编码
	 * @return
	 */
	public List<IrrProjTypeEntity> findByPCode(String pCode);
	/**
	 * 根据编码查询评估项目
	 * @param typeCode 类型编码
	 * @return
	 */
	public List<IrrProjTypeEntity> findByTypeCode(String typeCode);
}
