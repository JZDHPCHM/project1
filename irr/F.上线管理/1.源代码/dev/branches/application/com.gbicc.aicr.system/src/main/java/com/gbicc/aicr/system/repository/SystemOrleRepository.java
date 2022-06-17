package com.gbicc.aicr.system.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class SystemOrleRepository {
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	
	public List<Map<String, Object>> queryForList(String sql) {
		List<Map<String, Object>> forList = jdbctemplate.queryForList(sql);
		return forList;
	}




	public boolean saveRole(String sql) {
		
		if(jdbctemplate.update(sql)>0) {
			return true;
		}
		return false;
		
	}




	public boolean deleteRole(String leftId) {
		String sql = "delete * from FR_BRC_ROLE where SYS_ROLE'"+leftId+"'";
		return jdbctemplate.update(sql)>0?true:false;
		
	}





}
