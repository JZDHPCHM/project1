package com.gbicc.aicr.system.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.org.repository.OrgRepository;
import org.wsp.framework.jpa.service.DaoService;

import com.gbicc.aicr.system.support.dto.JsTreeNodeDTO;

/**
 * 机构service
 * 
 * @author FC
 * @version v1.0 2019年6月20日
 */
public interface SystemOrgService extends DaoService<Org, String, OrgRepository> {

    /**
     * 根据父ID查询机构
     * 
     * @param parentId
     *            父ID
     * @return
     */
    public List<Map<String, Object>> findOrgByParentId(String parentId) throws Exception;
    
    /**
     * 查询机构（jsTree格式返回）
     *
     * @param parentId
     *            父级ID
     * @return
     * @throws Exception
     */
    public List<JsTreeNodeDTO> findOrgJsTree(String parentId) throws Exception;

    /**
     * 根据用户ID查询唯一的机构（如果为多机构，则默认取第一个）
     *
     * @param useId
     *            用户ID
     * @return
     * @throws Exception
     */
    public Org findDefaultOrgByUserId(String useId) throws Exception;

	/**
	 * 查询所有机构
	 *
	 * @return
	 */
	public Map<String, String> findAllOrgs();

	/**
	 * 根据机构编码查询机构
	 *
	 * @param orgCode
	 * @return
	 */
	public List<Map<String, Object>> findByOrgCode(String orgCode);

	/**
	 * 查询所有分公司
	 *
	 * @return
	 */
	public List<Map<String, Object>> findAllBranchOrg();
	
    /**
     * 根据编码长度查询机构
     *
     * @param codeLength
     *            编码长度
     * @param isDept
     *            是否为部门
     * @return
     */
    public List<Map<String, Object>> findOrgByCodeLength(Integer codeLength, Boolean isDept);

}
