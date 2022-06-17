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
import net.gbicc.app.pfm.jpa.entity.PfmChannelResultEntity;
import net.gbicc.app.pfm.jpa.repository.PfmChannelResultRepository;
import net.gbicc.app.pfm.service.PfmChannelResultService;

/**
 * 渠道绩效结果
 */
@Service
public class PfmChannelResultServiceImpl extends
		DaoServiceImpl<PfmChannelResultEntity, String, PfmChannelResultRepository> implements PfmChannelResultService {

	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> queryChannelResult(String taskId, String indexCode, String indexName, Long page,
            Integer size) {
        String sql = "SELECT PCR.SORT_NUM,PCR.TASK_BATCH,PCR.INDEX_NAME AS INDEXNAME,"
                + "MAX((CASE PCR.CHANNEL_NAME WHEN '客服' THEN PCR.INDEX_SCORE END)) AS KF,"
                + "MAX((CASE PCR.CHANNEL_NAME WHEN '个险' THEN PCR.INDEX_SCORE END)) AS FC,"
                + "MAX((CASE PCR.CHANNEL_NAME WHEN '团险' THEN PCR.INDEX_SCORE END)) AS GP,"
                + "MAX((CASE PCR.CHANNEL_NAME WHEN '银保' THEN PCR.INDEX_SCORE END)) AS BK,"
                + "MAX((CASE PCR.CHANNEL_NAME WHEN '多元' THEN PCR.INDEX_SCORE END)) AS AD,"
                + "MAX((CASE PCR.CHANNEL_NAME WHEN '收展' THEN PCR.INDEX_SCORE END)) AS RP "
                + "FROM T_PFM_CHANNEL_RESULT PCR WHERE PCR.INDEX_NAME IS NOT NULL ";
		if(StringUtils.isNotBlank(taskId)) {
            sql += "AND PCR.TASK_ID = '" + taskId + "' ";
		}
		if(StringUtils.isNotBlank(indexCode)) {
            sql += "AND PCR.INDEX_CODE LIKE '%" + indexCode + "%' ";
		}
		if(StringUtils.isNotBlank(indexName)) {
            sql += "AND PCR.INDEX_NAME LIKE '%" + indexName + "%' ";
		}
        sql += "GROUP BY PCR.INDEX_NAME,PCR.SORT_NUM,PCR.TASK_BATCH ORDER BY PCR.SORT_NUM";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		ResponsePage<Map<String, Object>> response = new ResponsePage<Map<String, Object>>();
        response.setSize(size);
        response.setNumber(page);
		response.setAllData(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("response", response.getPageData());
		return map;
	}

}
