package com.gbicc.aicr.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.gbicc.aicr.system.dto.UserInfoDTO;
import com.gbicc.aicr.system.entiry.SystemRole;
import com.gbicc.aicr.system.repository.SystemRoleRepository;
import com.gbicc.aicr.system.service.SystemRoleService;

@Service
public class SystemRoleServiceImpl extends DaoServiceImpl<SystemRole, String, SystemRoleRepository>
		implements SystemRoleService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
//	@Autowired
//	private SystemOrleRepository pository;

	@Override
	public List<Map<String, Object>> queryRoleActionLeft() {
		String sql = "SELECT * FROM FR_AA_ROLE ";
		List<Map<String, Object>> forList = jdbcTemplate.queryForList(sql);
		return forList;

	}

	@Override
	public List<Map<String, Object>> queryRoleActionRight() {

		String sql = "SELECT * FROM SYS_ROLE ";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public Map<String,Object>  saveRole(String leftId, String[] rightIds) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if (!StringUtils.isBlank(leftId)) {
				String sql = "delete FROM  FR_BRC_ROLE where SYS_ROLE_ID = '" + leftId + "'";
				jdbcTemplate.update(sql);
			}  

				if (rightIds.length > 0) {
					String ids[] = rightIds;
					for (String string : ids) {
						String sql2 = "INSERT INTO FR_BRC_ROLE values ('" + leftId + "','" + string + "')";
						jdbcTemplate.update(sql2);
					}
					
				}

			
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("msg", false);
			return map;
		}
		

		map.put("msg", true);
		return map;
	}

	@Override
	public List<SystemRole> queryRoleById(String id) {
		String sql = "select hx.*,decode(gl.brc_role_id,NULL,0,1) as isSelected  from SYS_ROLE hx left join (\r\n"
				+ "select b.brc_role_id ,a.fd_id from fr_aa_role a inner join fr_brc_role b on a.fd_id=b.sys_role_id \r\n"
				+ "where a.fd_id = '" + id + "' ) gl\r\n" + "on hx.role_id=gl.brc_role_id \r\n"
				+ "--where hx.role_status='1'\r\n" + "order by hx.role_id";
		List<SystemRole> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(SystemRole.class));

		return list;
	}

	@Override
	public List<UserInfoDTO> queryUserInfoDto() {
		String findUser = "select u.fd_loginname loginName,\n" + 
				"       u.fd_username userName,\n" + 
				"       r.fd_code roleCode,\n" + 
				"       r.fd_name roleName,\n" + 
				"       o.fd_code depCode,\n" + 
				"       o.fd_name depName\n" + 
				"         from fr_aa_user u \n" + 
				"         left join fr_aa_user_role ur on u.fd_id = ur.fd_user_id\n" + 
				"         left join fr_aa_role r on ur.fd_role_id = r.fd_id\n" + 
				"         left join fr_aa_user_org uo on u.fd_id = uo.fd_user_id\n" + 
				"         left join fr_aa_org o on uo.fd_org_id = o.fd_id ";
		List<UserInfoDTO> list = jdbcTemplate.query(findUser, new BeanPropertyRowMapper(UserInfoDTO.class));
		return list;
	}

}
