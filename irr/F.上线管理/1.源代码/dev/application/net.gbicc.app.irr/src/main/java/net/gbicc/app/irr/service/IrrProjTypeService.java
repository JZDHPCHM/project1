package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.repository.IrrProjTypeRepository;
import net.gbicc.app.irr.jpa.support.dto.JqGridTreeNodeDTO;

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
	
    /**
     * 得到五大类的下拉框
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> fiveProjTypeSelect() throws Exception;

	/**
	 * 查询评估项目选项框数据(key：编码；name：名称)
	 * @param length 编码的长度(null不作为条件)
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> projTypeCodeSelect(Integer codeLength) throws Exception;

	/**
	 * 根据条件查询评估项目
	 * @param childCode 子评估项目编码
	 * @return
	 * @throws Exception
	 */
	public IrrProjTypeEntity findProjByCondition(String childCode) throws Exception;
	
	/**
	 * 通过父级编码查询子级
	 * @param parentCode 父级编码
	 * @return
	 * @throws Exception
	 */
	public List<IrrProjTypeEntity> findProjChildByParentCode(String parentCode) throws Exception;
	
	/**
	 * 查询评估项目表格树的内容
	 * @param typeCode 评估项目编码
	 * @return
	 * @throws Exception
	 */
	public List<JqGridTreeNodeDTO> findJqGridTreeProjType(String typeCode) throws Exception;
}
