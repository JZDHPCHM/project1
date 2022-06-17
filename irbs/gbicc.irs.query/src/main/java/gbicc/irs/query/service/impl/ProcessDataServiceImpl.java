package gbicc.irs.query.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSONObject;

import gbicc.irs.query.entity.DCustomer;
import gbicc.irs.query.entity.FlowStatus;
import gbicc.irs.query.service.ProcessDataService;

@Service("ProcessDataService")
public class ProcessDataServiceImpl implements ProcessDataService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public JSONObject findFlowStatus() {
		
		List<FlowStatus> messages = jdbcTemplate.query("select distinct substr(t.fd_code , instr(t.fd_code,'.',-1) +1) code, t.fd_message message\r\n" + 
				"  from FR_SYS_I18N_MESSAGE t\r\n" + 
				" where t.fd_code like '%dictionary.DEFAULT_PROCESS_STATE.%'\r\n" + 
				"    or t.fd_code like '%dictionary.DEFAULT_REBORN_PROCESS_STATE.%'",new BeanPropertyRowMapper<FlowStatus>(FlowStatus.class));
		
		JSONObject json = new JSONObject();
		
		for (FlowStatus flow : messages) {
			json.put(flow.getCode(), flow.getMessage());
		}
		
		return json;
	}

	@Override
	public JSONObject findOrgs() {
		
//		String loginName = SecurityUtil.getLoginName();
//		
//		//当前账号所属机构
//		List<String> orgid = jdbcTemplate.queryForList("select fd_org_id from fr_aa_user_org where fd_user_id = '" + loginName + "'", String.class);
//		String org = "";
//		if(orgid != null && ! orgid.isEmpty()) {
//			org = orgid.get(0);
//		}
//		
//		List<Map<String, Object>> orgs = jdbcTemplate.queryForList("select fd_id,fd_name from fr_aa_org start with fd_id = '" + org + "' connect by prior fd_id = fd_parent_id");
		
//		if("admin".equals(loginName)) {
		List<Map<String, Object>> orgs = jdbcTemplate.queryForList("select fd_id,fd_name from fr_aa_org");
//		}
		
		JSONObject json = new JSONObject();
		
		for (Map<String, Object> map : orgs) {
			json.put(map.get("fd_id").toString(), map.get("fd_name").toString());
		}
		
		return json;
	}

	@Override
	public String findRatingLevels() {
		List<String> list = jdbcTemplate.queryForList("select distinct fd_pd_level from NS_R_CFG_SCALE_DETAIL order by fd_pd_level asc", String.class);
		list.add("D");
		return JSONObject.toJSONString(list);
	}

	public String findRatingLevel(String type) {
		List<String> list = jdbcTemplate.queryForList("select  fd_scale_level  from ns_cfg_main_scale where fd_type =? order by fd_sort asc ", String.class,type);
		return JSONObject.toJSONString(list);
	}
	
	@Override
	public String findCurrentOrg() {
		//当前账号所属机构
		List<String> orgIds = jdbcTemplate.queryForList("select fd_org_id from fr_aa_user_org where fd_user_id = '" + SecurityUtil.getUserId() + "'", String.class);
		String orgId = "";
		if(!CollectionUtils.isEmpty(orgIds)) {
			orgId = orgIds.get(0);
		}
		return orgId;
	}

	@Override
	public String findCurrentParentOrg() {
		// 当前机构
		String orgId = this.findCurrentOrg();
		String id = "";
		if(!StringUtils.isEmpty(orgId)) {
			List<String> parentIds = jdbcTemplate.queryForList("select o.fd_parent_id from fr_aa_org o where o.fd_code =?", String.class,orgId);
			if(!CollectionUtils.isEmpty(parentIds)) {
				id = parentIds.get(0);
			}
		}
		return id;
	}

}
