package net.gbicc.app.pfm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.pfm.jpa.entity.PfmDepaResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmDepaResultRepository;
import net.gbicc.app.pfm.service.PfmDepaResultService;

/**
* 部门绩效结果
*/
@Service
public class PfmDepaResultServiceImpl extends DaoServiceImpl<PfmDepaResultEntity, String, PfmDepaResultRepository> 
	implements PfmDepaResultService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Map<String, Object> queryDepaResult(String taskId, String indexCode, String indexName, Long page,
            Integer size) {
        String sql = "SELECT PDR.SORT_NUM,PDR.TASK_BATCH,PDR.INDEX_NAME AS INDEXNAME,"
                + 
                "MAX((CASE PDR.ORG_NAME WHEN '投资部' THEN PDR.INDEX_SCORE END)) AS tzb,"
                + "MAX((CASE PDR.ORG_NAME WHEN '精算部' THEN PDR.INDEX_SCORE END)) AS jsb,"
                + "MAX((CASE PDR.ORG_NAME WHEN '财务管理部' THEN PDR.INDEX_SCORE END)) AS cwglb,"
                + "MAX((CASE PDR.ORG_NAME WHEN '会计运营部' THEN PDR.INDEX_SCORE END)) AS kjyyb,"
                + "MAX((CASE PDR.ORG_NAME WHEN 'IT' THEN PDR.INDEX_SCORE END)) AS IT "
                + "FROM T_PFM_DEPA_RESULT PDR WHERE PDR.INDEX_NAME IS NOT NULL ";
		if(StringUtils.isNotBlank(taskId)) {
            sql += "AND PDR.TASK_ID = '" + taskId + "' ";
		}
		if(StringUtils.isNotBlank(indexCode)) {
            sql += "AND PDR.INDEX_CODE LIKE '%" + indexCode + "%' ";
		}
		if(StringUtils.isNotBlank(indexName)) {
            sql += "AND PDR.INDEX_NAME LIKE '%" + indexName + "%' ";
		}
        sql += "GROUP BY PDR.INDEX_NAME,PDR.SORT_NUM,PDR.TASK_BATCH ORDER BY PDR.SORT_NUM";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		ResponsePage<Map<String, Object>> response=new ResponsePage<Map<String,Object>>();
        response.setSize(size);
        response.setNumber(page);
		response.setAllData(list);
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("response", response.getPageData());
		return map;
	}

}
