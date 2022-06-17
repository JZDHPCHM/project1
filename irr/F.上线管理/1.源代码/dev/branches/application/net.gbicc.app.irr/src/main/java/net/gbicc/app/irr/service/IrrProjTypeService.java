package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjTypeRepository;

/**
* 评估项目
*/
public interface IrrProjTypeService extends DaoService<IrrProjTypeEntity, String, IrrProjTypeRepository> {

	/**
	 * 查询评估项目选项框数据
	 * @param length 编码的长度(null不作为条件)
	 * @return
	 * @throws Exception
	 */
	public List<IrrProjTypeEntity> findProjTypeSelect(Integer codeLength) throws Exception;
	
	/**
	 * 查询评估项目选项框数据
	 * @param length 编码的长度(null不作为条件)
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> projTypeSelect(Integer codeLength) throws Exception;
	
}