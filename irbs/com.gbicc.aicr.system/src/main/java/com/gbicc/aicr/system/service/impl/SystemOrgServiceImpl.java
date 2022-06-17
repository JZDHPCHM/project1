package com.gbicc.aicr.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.org.repository.OrgRepository;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.UserService;

import com.gbicc.aicr.system.service.SystemOrgService;
import com.gbicc.aicr.system.support.dto.JsTreeNodeDTO;
import com.gbicc.aicr.system.support.enums.AppEnum;
import com.gbicc.aicr.system.support.enums.JsTreeEnum;

import net.sf.json.JSONObject;

/**
 * 系统机构实现类
 * 
 * @author FC
 * @version v1.0 2019年6月20日
 */
@Service
@Transactional
public class SystemOrgServiceImpl extends DaoServiceImpl<Org, String, OrgRepository> implements SystemOrgService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;

    @Override
    public List<Map<String, Object>> findOrgByParentId(String parentId) throws Exception {
        String sql = null;
        if (StringUtils.isBlank(parentId)) {
            sql = "SELECT * FROM FR_AA_ORG WHERE FD_PARENT_ID IS NULL";
        } else {
            sql = "SELECT * FROM FR_AA_ORG WHERE FD_PARENT_ID='" + parentId + "'";
        }
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<JsTreeNodeDTO> findOrgJsTree(String parentId) throws Exception {
        List<JsTreeNodeDTO> list = new ArrayList<JsTreeNodeDTO>();
        if (StringUtils.isBlank(parentId)) {
            return list;
        }
        List<Map<String, Object>> orgs = findOrgByParentId(
                JsTreeEnum.JSTREE_ROOT.getValue().equals(parentId) ? null : parentId);
        if (orgs == null || orgs.size() <= 0) {
            return list;
        }
        for (Map<String, Object> org : orgs) {
            JsTreeNodeDTO dto = new JsTreeNodeDTO();
            dto.setId(org.get("FD_ID") == null ? null : org.get("FD_ID").toString());
            dto.setText(org.get("FD_NAME") == null ? null : org.get("FD_NAME").toString());
            dto.setParent(org.get("FD_PARENT_ID") == null ? JsTreeEnum.JSTREE_ROOT.getValue()
                    : org.get("FD_PARENT_ID").toString());
            JSONObject json = new JSONObject();
            json.put("orgCode", org.get("FD_CODE") == null ? null : org.get("FD_CODE").toString());
            json.put("orgId", org.get("FD_ID") == null ? null : org.get("FD_ID").toString());
            json.put("orgName", org.get("FD_NAME") == null ? null : org.get("FD_NAME").toString());
            dto.setLi_attr(json);
            List<Map<String, Object>> children = findOrgByParentId(
                    org.get("FD_ID") == null ? null : org.get("FD_ID").toString());
            if (children != null && children.size() > 0) {
                dto.setChildren(true);
            } else {
                dto.setChildren(false);
            }
            list.add(dto);
        }
        return list;
    }

    @Override
    public Org findDefaultOrgByUserId(String useId) throws Exception {
        if (StringUtils.isBlank(useId)) {
            return null;
        }
        User user = userService.findById(useId);
        List<Org> orgList = user.getOrgs();
        if (CollectionUtils.isEmpty(orgList)) {
            return null;
        }
        return orgList.get(0);
    }

	@Override
	public Map<String, String> findAllOrgs() {
		List<Org> list = repository.findAll();
		Map<String, String> map = new HashMap<String, String>();
		for (Org org : list) {
			map.put(org.getId(), org.getName());
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findByOrgCode(String orgCode) {
		String sql = "SELECT * FROM FR_AA_ORG WHERE FD_CODE='" + orgCode + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	@Override
	public List<Map<String, Object>> findAllBranchOrg() {
		String sql = "SELECT FD_ID AS ORGID,FD_NAME AS FDNAME FROM FR_AA_ORG WHERE FD_NAME LIKE '%" + AppEnum.BRANCH_ORG_SUFFIX.getValue() + "'";
		return jdbcTemplate.queryForList(sql);
	}

    @Override
    public List<Map<String, Object>> findOrgByCodeLength(Integer codeLength, Boolean isDept) {
        String sql = "SELECT * FROM FR_AA_ORG WHERE FD_ENABLE="+AppEnum.FR_YESNO_Y.getValue();
        if (codeLength != null) {
            sql += " AND LENGTH(FD_CODE)=" + codeLength;
        }
        if(isDept != null) {
            if (isDept) {
                sql += " AND FD_IS_DEPARTMENT=" + AppEnum.FR_YESNO_Y.getValue();
            } else {
                sql += " AND FD_IS_DEPARTMENT=" + AppEnum.FR_YESNO_N.getValue();
            }
        }
        return jdbcTemplate.queryForList(sql);
    }

}
