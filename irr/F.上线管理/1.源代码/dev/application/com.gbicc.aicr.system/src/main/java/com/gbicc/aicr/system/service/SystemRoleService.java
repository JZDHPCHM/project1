package com.gbicc.aicr.system.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import com.gbicc.aicr.system.dto.UserInfoDTO;
import com.gbicc.aicr.system.entiry.SystemRole;
import com.gbicc.aicr.system.repository.SystemRoleRepository;


public interface SystemRoleService extends DaoService<SystemRole,String,SystemRoleRepository>
{
	/**
	 * left
	 * @return
	 */
	List<Map<String, Object>>  queryRoleActionLeft();
	/**
	 * Right
	 * @return
	 */
	List<Map<String, Object>>  queryRoleActionRight();
	/**
	 * save
	 * @param leftId
	 * @param rightId
	 * @return
	 */
	Map<String,Object> saveRole(String leftId,String [] rightIds);

	

	public List<SystemRole> queryRoleById(String id);

	/**
	 * 查询用户角色机构
	 * @return
	 */
	List<UserInfoDTO> queryUserInfoDto();
}
