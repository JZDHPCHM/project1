package gbicc.irs.main.rating.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import gbicc.irs.customer.support.BindTableData;
import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.entity.RatingSelectValuesConfig;
import gbicc.irs.main.rating.repository.RatingIndexRepository;
import gbicc.irs.main.rating.service.RatingIndexService;
import gbicc.irs.main.rating.service.RatingSelectValuesConfigService;
import gbicc.irs.main.rating.support.RatingStepType;


@Service
public class RatingIndexServiceImpl extends DaoServiceImpl<RatingIndex, String, RatingIndexRepository> implements
	RatingIndexService {

	@Autowired
	RatingSelectValuesConfigService ratingSelectValuesConfigService;
	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public List<RatingIndex> getRatingIndexsByStepId(String stepId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	/*	List<RatingIndex> indexs =  jdbc.query("select ratinginde0_.fd_id as \"id\", ratinginde0_.fd_create_date as \"create_date\", ratinginde0_.fd_creator as \"creator\", \r\n" + 
				"ratinginde0_.fd_last_modifier as \"last_modifier\", ratinginde0_.fd_last_modifydate as \"last_modifydate\", \r\n" + 
				"ratinginde0_.fd_dx_text as \"dx_text\", ratinginde0_.fd_indexcategory as \"indexcategory\", ratinginde0_.fd_indexcode as \"indexcode\", \r\n" + 
				"ratinginde0_.fd_configid as \"configid\", ratinginde0_.fd_indexid as \"indexid\", ratinginde0_.fd_indexname as \"indexname\", \r\n" + 
				"ratinginde0_.fd_score as \"score\", ratinginde0_.fd_indextype as \"indextype\", ratinginde0_.fd_indexvalue as \"indexvalue\", \r\n" + 
				"ratinginde0_.fd_weight as \"weight\", ratinginde0_.fd_level as \"level\", ratinginde0_.fd_parent_id as \"parent_id\", \r\n" + 
				"ratinginde0_.fd_qualitative_options as \"qualitative_op18\", ratinginde0_.fd_stepid as \"stepid\" from \r\n" + 
				"ns_rating_indexes ratinginde0_ left outer join ns_rating_step ratingmain1_ on ratinginde0_.fd_stepid=ratingmain1_.fd_id where ratingmain1_.fd_id=?  order by \r\n" + 
				"                     decode(fd_indexcode,'TYZB003',1),\r\n" + 
				"                     decode(fd_indexcode,'TYZB004',2),\r\n" + 
				"					           decode(fd_indexcode,'TYZB005',3),\r\n" + 
				"								decode(fd_indexcode, 'XJZT003', 3),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT002', 1),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT001', 2),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT004', 4),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT006', 5),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT007', 6),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT008', 7),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT009', 8),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT0010', 9),\r\n" + 
				"					          decode(fd_indexcode, 'XJZT005', 10), + \r\n" + 
				"					           decode(fd_indexcode,'TYZB006',4),\r\n" + 
				"					           decode(fd_indexcode,'TYZB002',5),\r\n" + 
				"					           decode(fd_indexcode,'TYZB001',6),\r\n" + 
				"					           decode(fd_indexcode,'TYZB007',7),\r\n" + 
				"					           decode(fd_indexcode,'TYZB009',8),\r\n" + 
				"					           decode(fd_indexcode,'TYZB010',9),\r\n" + 
				"					           decode(fd_indexcode,'TYZB011',10),\r\n" + 
				"					           decode(fd_indexcode,'TYZB012',11),\r\n" + 
				"					           decode(fd_indexcode,'TYZB013',12),\r\n" + 
				"					           decode(fd_indexcode,'TYZB008',13)", new BeanPropertyRowMapper<RatingIndex>(RatingIndex.class),stepId);*/
				//repository.findByRatingStep_Id(stepId);
		List<RatingIndex> indexs = repository.findByRatingStep_Id(stepId);
		List<RatingIndex> resultIndexs =  new ArrayList<RatingIndex>();;
		Map<String,String> optionsMap;
		for(RatingIndex ri : indexs) {
			if(!(ri.getIndexCode().indexOf("TY")>-1&&ri.getIndexType().equals(RatingStepType.QUANTITATIVE))) {
				optionsMap= new HashMap<String,String>();
				List<RatingSelectValuesConfig> listOptions = ratingSelectValuesConfigService.getSelectValuesByDefId(ri.getIndexConfigId());
				for(RatingSelectValuesConfig va : listOptions) {
					optionsMap.put(va.getRealValue(), va.getDisplayValue());
				}
				ri.setOptions(mapper.writeValueAsString(optionsMap));
				resultIndexs.add(ri);
			}
		}
		return resultIndexs;
	}

	@Override
	public List<RatingIndex> getRatingIndexsByIndexType(RatingStepType stepType) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<RatingIndex> indexs =  repository.findByIndexType(stepType);
		List<RatingIndex> resultIndexs =  new ArrayList<RatingIndex>();;
		for(RatingIndex ri : indexs) {
			Map<String,String> optionsMap = new HashMap<String,String>();
			List<RatingSelectValuesConfig> listOptions = ratingSelectValuesConfigService.getSelectValuesByDefId(ri.getIndexConfigId());
			for(RatingSelectValuesConfig va : listOptions) {
				optionsMap.put(va.getRealValue(), va.getDisplayValue());
			}
			ri.setOptions(mapper.writeValueAsString(optionsMap));
			resultIndexs.add(ri);
		}
		return resultIndexs;
	}

	@Override
	public Map<String, String> getRatingIndexsValueByStepId(String stepId) throws Exception {
		Map<String,String> indexValueMap = new HashMap<String,String>();
		List<RatingIndex> indexs =  repository.findByRatingStep_Id(stepId);
		for(RatingIndex ri : indexs) {
			indexValueMap.put(ri.getIndexCode(), ri.getIndexValue());
		}
		return indexValueMap;
	}

	@Override
	public Map<String, Object> getMainHeader(String id) {
		// 原始 SQL
		// select FD_CUST_NAME,FD_CUST_CODE from NS_MAIN_RATING where FD_ID = '67d6b743-3801-461d-8bb4-f191d6f4aba5'

		String sql = "select FD_CUST_NAME,FD_CUST_CODE from NS_MAIN_RATING where FD_ID = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list.get(0);
		}
		return queryForList.get(0);
	}

	@Override
	public List<Map<String, Object>> getTip(String id) {
		// 原始 SQL
		// select *  from (select ns.TIP, ns.RISK_POINT, ma.SELECT_EXPLAIN  from NS_MAIN_RATING nmr  left join ns_major_record ma  on nmr.fd_id = ma.SERIAL_NUMBER  left join NS_MAJOR_RISKWARNINGDATAS ns  on ma.tip_number = ns.TIP_NUMBER  where ma.rate_type = '020'  and ma.isselected = 'Y'  and nmr.fd_id = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'),  (select count(ns.RISK_POINT) coun  from NS_MAIN_RATING nmr  left join ns_major_record ma  on nmr.fd_id = ma.SERIAL_NUMBER  left join NS_MAJOR_RISKWARNINGDATAS ns  on ma.tip_number = ns.TIP_NUMBER  where ma.rate_type = '020'  and ma.isselected = 'Y'  and nmr.fd_id = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a')

		String sql = "select *\r\n" + 
				"  from (select ns.TIP, ns.RISK_POINT, ma.SELECT_EXPLAIN\r\n" + 
				"          from NS_MAIN_RATING nmr\r\n" + 
				"          left join ns_major_record ma\r\n" + 
				"            on nmr.fd_id = ma.SERIAL_NUMBER\r\n" + 
				"          left join NS_MAJOR_RISKWARNINGDATAS ns\r\n" + 
				"            on ma.tip_number = ns.TIP_NUMBER\r\n" + 
				"         where ma.rate_type = '020'\r\n" + 
				"           and ma.isselected = 'Y'\r\n" + 
				"           and nmr.fd_id = ?),\r\n" + 
				"       (select count(ns.RISK_POINT) coun\r\n" + 
				"          from NS_MAIN_RATING nmr\r\n" + 
				"          left join ns_major_record ma\r\n" + 
				"            on nmr.fd_id = ma.SERIAL_NUMBER\r\n" + 
				"          left join NS_MAJOR_RISKWARNINGDATAS ns\r\n" + 
				"            on ma.tip_number = ns.TIP_NUMBER\r\n" + 
				"         where ma.rate_type = '020'\r\n" + 
				"           and ma.isselected = 'Y'\r\n" + 
				"           and nmr.fd_id = ?)"; 
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getModel(String id) {
		// 原始 SQL
		// SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score, i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '商业模式'  AND i.fd_indexname != '商业模式'  UNION  SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '商业模式'  AND i.fd_indexname != '商业模式'  AND to_number(i.fd_configid) =  (SELECT max(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '商业模式'  AND i.fd_indexname != '商业模式')  UNION  SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '商业模式'  AND i.fd_indexname != '商业模式'  AND to_number(i.fd_configid) =  (SELECT min(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '商业模式'  AND i.fd_indexname != '商业模式')

		String sql = "SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score, i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"  AND i.fd_indexname != '商业模式'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"  AND i.fd_indexname != '商业模式'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT max(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"       AND i.fd_indexname != '商业模式')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"  AND i.fd_indexname != '商业模式'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT min(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"       AND i.fd_indexname != '商业模式')";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		String maxInfo = "最大值描述";
		String minInfo = "最小值描述";
		List<Map<String, Object>> maxCount = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> minCount = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : queryForList) {
			String value = (String) map.get("FD_INDEXNAME");
            if (value.equals(maxInfo)) {
            	maxCount.add(map);
            }
            if (value.equals(minInfo)) {
            	minCount.add(map);
            }
		}
		if(maxCount.size() > 1) {
			queryForList.removeAll(maxCount);
		}
		if(minCount.size() > 1) {
			queryForList.removeAll(minCount);
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getPosition(String id) {
		// 原始 SQL
		// SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score, i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '市场地位'  AND i.fd_indexname != '市场地位'  UNION  SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '市场地位'  AND i.fd_indexname != '市场地位'  AND to_number(i.fd_configid) =  (SELECT max(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '市场地位'  AND i.fd_indexname != '市场地位')  UNION  SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '市场地位'  AND i.fd_indexname != '市场地位'  AND to_number(i.fd_configid) =  (SELECT min(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '市场地位'  AND i.fd_indexname != '市场地位')

		String sql = "SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score, i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"  AND i.fd_indexname != '市场地位'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"  AND i.fd_indexname != '市场地位'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT max(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"       AND i.fd_indexname != '市场地位')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"  AND i.fd_indexname != '市场地位'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT min(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"       AND i.fd_indexname != '市场地位')";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		String maxInfo = "最大值描述";
		String minInfo = "最小值描述";
		List<Map<String, Object>> maxCount = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> minCount = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : queryForList) {
			String value = (String) map.get("FD_INDEXNAME");
            if (value.equals(maxInfo)) {
            	maxCount.add(map);
            }
            if (value.equals(minInfo)) {
            	minCount.add(map);
            }
		}
		if(maxCount.size() > 1) {
			queryForList.removeAll(maxCount);
		}
		if(minCount.size() > 1) {
			queryForList.removeAll(minCount);
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getQuality(String id) {
		// 原始 SQL
		// SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score,  i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产品品质'  AND i.fd_indexname != '产品品质'  UNION  SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产品品质'  AND i.fd_indexname != '产品品质'  AND to_number(i.fd_configid) =  (SELECT max(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产品品质'  AND i.fd_indexname != '产品品质')  UNION  SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产品品质'  AND i.fd_indexname != '产品品质'  AND to_number(i.fd_configid) =  (SELECT min(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产品品质'  AND i.fd_indexname != '产品品质')

		String sql = "SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score,\r\n" + 
				"       i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"  AND i.fd_indexname != '产品品质'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"  AND i.fd_indexname != '产品品质'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT max(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"       AND i.fd_indexname != '产品品质')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"  AND i.fd_indexname != '产品品质'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT min(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"       AND i.fd_indexname != '产品品质')";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		String maxInfo = "最大值描述";
		String minInfo = "最小值描述";
		List<Map<String, Object>> maxCount = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> minCount = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : queryForList) {
			String value = (String) map.get("FD_INDEXNAME");
            if (value.equals(maxInfo)) {
            	maxCount.add(map);
            }
            if (value.equals(minInfo)) {
            	minCount.add(map);
            }
		}
		if(maxCount.size() > 1) {
			queryForList.removeAll(maxCount);
		}
		if(minCount.size() > 1) {
			queryForList.removeAll(minCount);
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getEnvironment(String id) {
		// 原始 SQL
		// SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score,  i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产业环境'  AND i.fd_indexname != '产业环境'  UNION  SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产业环境'  AND i.fd_indexname != '产业环境'  AND to_number(i.fd_configid) =  (SELECT max(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产业环境'  AND i.fd_indexname != '产业环境')  UNION  SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产业环境'  AND i.fd_indexname != '产业环境'  AND to_number(i.fd_configid) =  (SELECT min(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '307a955a-a0aa-474a-9d33-24fb225732b1'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '产业环境'  AND i.fd_indexname != '产业环境')

		String sql = "SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score,\r\n" + 
				"       i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"  AND i.fd_indexname != '产业环境'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"  AND i.fd_indexname != '产业环境'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT max(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"       AND i.fd_indexname != '产业环境')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"  AND i.fd_indexname != '产业环境'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT min(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"       AND i.fd_indexname != '产业环境')";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		String maxInfo = "最大值描述";
		String minInfo = "最小值描述";
		List<Map<String, Object>> maxCount = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> minCount = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : queryForList) {
			String value = (String) map.get("FD_INDEXNAME");
            if (value.equals(maxInfo)) {
            	maxCount.add(map);
            }
            if (value.equals(minInfo)) {
            	minCount.add(map);
            }
		}
		if(maxCount.size() > 1) {
			queryForList.removeAll(maxCount);
		}
		if(minCount.size() > 1) {
			queryForList.removeAll(minCount);
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getAbility(String id) {
		// 原始 SQL
		// SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score, i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '管理能力'  AND i.fd_indexname != '管理能力'  UNION  SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '管理能力'  AND i.fd_indexname != '管理能力'  AND to_number(i.fd_configid) =  (SELECT max(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '管理能力'  AND i.fd_indexname != '管理能力')  UNION  SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '管理能力'  AND i.fd_indexname != '管理能力'  AND to_number(i.fd_configid) =  (SELECT min(to_number(i.fd_configid))  FROM ns_rating_indexes i  WHERE i.fd_stepid IN  (SELECT FD_ID  FROM ns_rating_step s  WHERE s.fd_rateid = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'  AND s.FD_VAILD = '1')  AND i.FD_INDEXCATEGORY = '管理能力'  AND i.fd_indexname != '管理能力')

		String sql = "SELECT i.fd_indexname, to_char(i.fd_configid, '999990') fd_score, i.FD_INDEXVALUE||'档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"  AND i.fd_indexname != '管理能力'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"  AND i.fd_indexname != '管理能力'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT max(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"       AND i.fd_indexname != '管理能力')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(i.FD_QUALITATIVE_OPTIONS,3), i.fd_indexname\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID\r\n" + 
				"     FROM ns_rating_step s\r\n" + 
				"     WHERE s.fd_rateid = ?\r\n" + 
				"       AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"  AND i.fd_indexname != '管理能力'\r\n" + 
				"  AND to_number(i.fd_configid) =\r\n" + 
				"    (SELECT min(to_number(i.fd_configid))\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (SELECT FD_ID\r\n" + 
				"          FROM ns_rating_step s\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"            AND s.FD_VAILD = '1')\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"       AND i.fd_indexname != '管理能力')";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		String maxInfo = "最大值描述";
		String minInfo = "最小值描述";
		List<Map<String, Object>> maxCount = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> minCount = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : queryForList) {
			String value = (String) map.get("FD_INDEXNAME");
            if (value.equals(maxInfo)) {
            	maxCount.add(map);
            }
            if (value.equals(minInfo)) {
            	minCount.add(map);
            }
		}
		if(maxCount.size() > 1) {
			queryForList.removeAll(maxCount);
		}
		if(minCount.size() > 1) {
			queryForList.removeAll(minCount);
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getManage(String id) {
		// 主体经营质量柱状图

		String sql = "SELECT i.fd_indexname, to_char(round(i.fd_score/fd_weight, 2), '9990.99') fd_score, '0' TYPE\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID FROM ns_rating_step s WHERE s.fd_rateid = ? AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '经营质量' AND i.fd_indexname != '经营质量'\r\n" + 
				"UNION\r\n" + 
				"select mas.FD_INDEXNAME || '平均值' FD_INDEXNAME, mas.FD_SCORE, '1' TYPE\r\n" + 
				"  from (select FD_INDEXNAME, to_char(round(sum(FD_SCORE / fd_weight) / count(*), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"          from NS_RATING_INDEXES nri\r\n" + 
				"         where fd_stepid in\r\n" + 
				"               (select n.FD_ID from ns_rating_step n\r\n" + 
				"                 where n.fd_vaild = '1' and n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                   and n.fd_rateid in\r\n" + 
				"                       /* 获取通赛道的评级主体 */\r\n" + 
				"                       (select nmr.fd_id from ns_main_rating nmr\r\n" + 
				"                          left join NS_MAIN_RATING nmr2\r\n" + 
				"                            on nmr.TRACK_TYPE = nmr2.TRACK_TYPE\r\n" + 
				"                         where nmr.fd_version = '3.0' and nmr.FD_RATING_STATUS = '020' and nmr.fd_vaild = '1'\r\n" + 
				"                           and nmr2.fd_id = ?))\r\n" + 
				"           and FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"         group by FD_INDEXNAME) mas\r\n" + 
				" right join (select FD_INDEXNAME from NS_RATING_INDEXES nri\r\n" + 
				"              where fd_stepid in (select n.FD_ID from ns_rating_step n\r\n" + 
				"                                   where n.fd_rateid = ?\r\n" + 
				"                                     and (n.fd_step_name = '定量分析' or n.fd_step_name = '定性分析')\r\n" + 
				"                                     and n.FD_VAILD = 1)\r\n" + 
				"                and FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                and FD_INDEXNAME != '经营质量') sla\r\n" + 
				"    on mas.FD_INDEXNAME = sla.FD_INDEXNAME\r\n" + 
				"UNION\r\n" + 
				"SELECT i.fd_indexname, to_char(i.fd_score/i.fd_weight, '9990.99'), '-1' TYPE\r\n" + 
				"FROM ns_rating_indexes i\r\n" + 
				"WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID FROM ns_rating_step s WHERE s.fd_rateid = ? AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '经营质量' AND i.fd_indexname != '经营质量'\r\n" + 
				"  AND i.fd_score =\r\n" + 
				"    (SELECT min(i.fd_score) FROM ns_rating_indexes i WHERE i.fd_stepid IN\r\n" + 
				"         (select n.FD_ID from ns_rating_step n\r\n" + 
				"                 where n.fd_vaild = '1' and n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                   and n.fd_rateid in\r\n" + 
				"                       /* 获取通赛道的评级主体 */\r\n" + 
				"                       (select nmr.fd_id from ns_main_rating nmr\r\n" + 
				"                          left join NS_MAIN_RATING nmr2\r\n" + 
				"                            on nmr.TRACK_TYPE = nmr2.TRACK_TYPE\r\n" + 
				"                         where nmr.fd_version = '3.0' and nmr.FD_RATING_STATUS = '020' and nmr.fd_vaild = '1'\r\n" + 
				"                           and nmr2.fd_id = ?))\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '经营质量' AND i.fd_indexname != '经营质量')\r\n" + 
				"UNION\r\n" + 
				"SELECT i.fd_indexname, to_char(i.fd_score/i.fd_weight, '9990.99'), '-2' TYPE\r\n" + 
				"FROM ns_rating_indexes i WHERE i.fd_stepid IN\r\n" + 
				"    (SELECT FD_ID FROM ns_rating_step s WHERE s.fd_rateid = ? AND s.FD_VAILD = '1')\r\n" + 
				"  AND i.FD_INDEXCATEGORY = '经营质量' AND i.fd_indexname != '经营质量'\r\n" + 
				"  AND i.fd_score =\r\n" + 
				"    (SELECT max(i.fd_score)\r\n" + 
				"     FROM ns_rating_indexes i\r\n" + 
				"     WHERE i.fd_stepid IN\r\n" + 
				"         (select n.FD_ID from ns_rating_step n\r\n" + 
				"                 where n.fd_vaild = '1' and n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                   and n.fd_rateid in\r\n" + 
				"                       /* 获取通赛道的评级主体 */\r\n" + 
				"                       (select nmr.fd_id from ns_main_rating nmr\r\n" + 
				"                          left join NS_MAIN_RATING nmr2\r\n" + 
				"                            on nmr.TRACK_TYPE = nmr2.TRACK_TYPE\r\n" + 
				"                         where nmr.fd_version = '3.0' and nmr.FD_RATING_STATUS = '020' and nmr.fd_vaild = '1'\r\n" + 
				"                           and nmr2.fd_id = ?))\r\n" + 
				"       AND i.FD_INDEXCATEGORY = '经营质量' AND i.fd_indexname != '经营质量')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值' fd_indexname, '100' fd_score, '-3' TYPE FROM dual";
		
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		String maxInfo = "-2";
		String minInfo = "-1";
		List<Map<String, Object>> maxCount = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> minCount = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : queryForList) {
			String value = (String) map.get("TYPE");
            if (value.equals(maxInfo)) {
            	maxCount.add(map);
            }
            if (value.equals(minInfo)) {
            	minCount.add(map);
            }
		}
		if(minCount.size() > 1) {
			queryForList.removeAll(maxCount);
		}
		if(maxCount.size() > 1) {
			queryForList.removeAll(minCount);
		}
		return queryForList;
	}

	@Override
	public Map<String, Object> getTrack(String id) {
		// /* 主体得分分位数 */
		Map<String, Object> map2 = getLevelStatus(id);
		String FD_VAILD = (String)map2.get("FD_VAILD");
		String status = (String)map2.get("FD_RATING_STATUS");
		String sql = "";

		List<Map<String, Object>> queryForList = null;
		if("020".equals(status)) {
			if("1".equals(FD_VAILD)) {
				// 复评 SQL
				sql = "/* 主体得分分位数 */\r\n" + 
						"SELECT * FROM\r\n" + 
						"  (SELECT trim(to_char(round(((SELECT ro FROM\r\n" + 
						"      (SELECT f.ro FROM\r\n" + 
						"         (SELECT rownum - 1 ro, f.* FROM\r\n" + 
						"            (SELECT n.* FROM ns_main_rating n\r\n" + 
						"             WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_sco > 0 and fd_vaild = '1'\r\n" + 
						"               AND TRACK_TYPE = (SELECT TRACK_TYPE FROM ns_main_rating WHERE FD_ID = ?)\r\n" + 
						"             ORDER BY fd_sco) f) f\r\n" + 
						"       WHERE fd_id = ?)) /\r\n" + 
						"   (SELECT nu FROM\r\n" + 
						"      (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
						"         (SELECT count(*) - 1 nu FROM\r\n" + 
						"            (SELECT rownum ro, f.* FROM\r\n" + 
						"               (SELECT n.* FROM ns_main_rating n WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' and fd_vaild = '1' AND fd_sco > 0\r\n" + 
						"                  AND TRACK_TYPE = (SELECT TRACK_TYPE FROM ns_main_rating WHERE FD_ID = ?)\r\n" + 
						"                ORDER BY fd_sco) f))))),2),'99990.99')) * 100 || '%' AS rankingOnTrack /*同赛道中的位置*/\r\n" + 
						"   FROM dual),\r\n" + 
						"  (SELECT trim(to_char(round(( (SELECT ro FROM\r\n" + 
						"         (SELECT f.ro FROM\r\n" + 
						"            (SELECT rownum - 1 ro, f.* FROM\r\n" + 
						"               (SELECT n.* FROM ns_main_rating n WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' and fd_vaild = '1' AND fd_sco > 0\r\n" + 
						"                ORDER BY fd_sco) f) f\r\n" + 
						"          WHERE fd_id = ?)) /\r\n" + 
						"      (SELECT nu FROM\r\n" + 
						"         (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
						"            (SELECT count(*) - 1 nu FROM\r\n" + 
						"               (SELECT rownum ro, f.* FROM\r\n" + 
						"                  (SELECT n.* FROM ns_main_rating n WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' and fd_vaild = '1' AND fd_sco > 0\r\n" + 
						"                   ORDER BY fd_sco) f))))),2),'99990.99')) * 100 || '%' AS ranking /*存量评级中的位置*/\r\n" + 
						"   FROM dual)";
				queryForList = jdbc.queryForList(sql,id,id,id,id);
			} else {
				// 历史评级
				sql = "/* 主体得分分位数 */ \r\n" + 
						"SELECT * FROM \r\n" + 
						"  (SELECT trim(to_char(round(((SELECT ro FROM \r\n" + 
						"      (SELECT f.ro FROM \r\n" + 
						"         (SELECT rownum - 1 ro, f.* FROM \r\n" + 
						"            (SELECT * FROM \r\n" + 
						"              (SELECT n.* FROM ns_main_rating n \r\n" + 
						"               WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_sco > 0 and fd_vaild = '1' \r\n" + 
						"                 AND TRACK_TYPE = (SELECT TRACK_TYPE FROM ns_main_rating WHERE FD_ID = ?) \r\n" + 
						"               UNION \r\n" + 
						"               SELECT * FROM ns_main_rating WHERE FD_ID = ?)\r\n" + 
						"             ORDER BY fd_sco) f) f \r\n" + 
						"       WHERE fd_id = ?)) / \r\n" + 
						"   (SELECT nu FROM \r\n" + 
						"      (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM \r\n" + 
						"         (SELECT count(*) nu FROM \r\n" + 
						"            (SELECT rownum ro, f.* FROM \r\n" + 
						"               (SELECT n.* FROM ns_main_rating n WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' and fd_vaild = '1' AND fd_sco > 0 \r\n" + 
						"                  AND TRACK_TYPE = (SELECT TRACK_TYPE FROM ns_main_rating WHERE FD_ID = ?) \r\n" + 
						"                ORDER BY fd_sco) f))))),2),'99990.99')) * 100 || '%' AS rankingOnTrack /*同赛道中的位置*/ \r\n" + 
						"   FROM dual), \r\n" + 
						"  (SELECT trim(to_char(round(( (SELECT ro FROM \r\n" + 
						"         (SELECT f.ro FROM \r\n" + 
						"            (SELECT rownum - 1 ro, f.* FROM \r\n" + 
						"               (SELECT * from\r\n" + 
						"                 (SELECT n.* FROM ns_main_rating n WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' and fd_vaild = '1' AND fd_sco > 0 \r\n" + 
						"                 union\r\n" + 
						"                 SELECT * FROM ns_main_rating WHERE fd_id = ?)\r\n" + 
						"                ORDER BY fd_sco) f) f \r\n" + 
						"          WHERE fd_id = ?)) / \r\n" + 
						"      (SELECT nu FROM \r\n" + 
						"         (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM \r\n" + 
						"            (SELECT count(*) nu FROM \r\n" + 
						"               (SELECT rownum ro, f.* FROM \r\n" + 
						"                  (SELECT n.* FROM ns_main_rating n WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' and fd_vaild = '1' AND fd_sco > 0 \r\n" + 
						"                   ORDER BY fd_sco) f))))),2),'99990.99')) * 100 || '%' AS ranking /*存量评级中的位置*/ \r\n" + 
						"   FROM dual)";
				queryForList = jdbc.queryForList(sql,id,id,id,id,id,id);
			}
		} else {
			// 评级测算 SQL
			sql = "SELECT * FROM (SELECT trim(to_char(round(( \r\n" + 
					"     (SELECT ro FROM \r\n" + 
					"        (SELECT f.ro FROM \r\n" + 
					"           (SELECT rownum - 1 ro, f.* FROM \r\n" + 
					"              (SELECT * FROM (select n.* from ns_main_rating n \r\n" + 
					"                       where fd_version = '3.0' and FD_RATING_STATUS = '020' and fd_sco > 0  AND fd_vaild = '1'\r\n" + 
					"                         and TRACK_TYPE = (select TRACK_TYPE from ns_main_rating where FD_ID = ?) \r\n" + 
					"                      union \r\n" + 
					"                      select * from ns_main_rating where FD_ID = ?) \r\n" + 
					"               ORDER BY fd_sco) f) f \r\n" + 
					"         WHERE fd_id = ?)) / \r\n" + 
					"     (SELECT nu FROM \r\n" + 
					"        (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM \r\n" + 
					"           (SELECT count(*) - 1 nu FROM \r\n" + 
					"              (SELECT rownum ro, f.* FROM \r\n" + 
					"                 (SELECT n.* FROM ns_main_rating n \r\n" + 
					"                  WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_sco > 0  AND fd_vaild = '1'\r\n" + 
					"                    AND TRACK_TYPE = (SELECT TRACK_TYPE FROM ns_main_rating \r\n" + 
					"                       WHERE FD_ID = ?) \r\n" + 
					"                  ORDER BY fd_sco) f))))),2),'99990.99')) * 100 || '%' AS rankingOnTrack /*同赛道中的位置*/ \r\n" + 
					"   FROM dual), \r\n" + 
					"  (SELECT trim(to_char(round(( (SELECT ro FROM \r\n" + 
					"         (SELECT f.ro FROM \r\n" + 
					"            (SELECT rownum - 1 ro, f.* FROM \r\n" + 
					"               (select * from (SELECT n.* FROM ns_main_rating n  WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_sco > 0  AND fd_vaild = '1'\r\n" + 
					"                union \r\n" + 
					"                select * from ns_main_rating where FD_ID = ?) \r\n" + 
					"                ORDER BY fd_sco) f) f \r\n" + 
					"          WHERE fd_id = ?)) / \r\n" + 
					"      (SELECT nu FROM \r\n" + 
					"         (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu \r\n" + 
					"          FROM (SELECT count(*) - 1 nu FROM \r\n" + 
					"               (SELECT rownum ro, f.* FROM \r\n" + 
					"                  (SELECT n.* FROM ns_main_rating n \r\n" + 
					"                   WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_sco > 0  AND fd_vaild = '1'\r\n" + 
					"                   ORDER BY fd_sco) f))))),2),'99990.99')) * 100 || '%' AS ranking /*存量评级中的位置*/ \r\n" + 
					"   FROM dual)";
			queryForList = jdbc.queryForList(sql,id,id,id,id,id,id);
		}
		
		if(queryForList.isEmpty()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			return map;
		}
		Map<String, Object> result = queryForList.get(0);
		result.forEach((key, value) -> {
			String v = (String) value;
			String substring = v.substring(0, v.indexOf("%"));
			if(!substring.isEmpty()) {
				double d = Double.parseDouble(substring);
				if (d < 0) {
					result.put(key, "0%");
				}
			}
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getDepartment(String id) {
		// /* 主体雷达图部门级对比*/  

		String sql = "/* 主体雷达图部门级对比*/\r\n" + 
				"SELECT '经营质量' name,\r\n" + 
				"              to_char(round((FD_SCORE /\r\n" + 
				"                               (SELECT sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"                                FROM NS_RATING_INDEXES nri\r\n" + 
				"                                WHERE fd_stepid IN\r\n" + 
				"                                    (SELECT n.FD_ID\r\n" + 
				"                                     FROM ns_rating_step n\r\n" + 
				"                                     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"                                       AND n.FD_VAILD = '1'\r\n" + 
				"                                       AND n.fd_rateid = ?)\r\n" + 
				"                                  AND FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                                  AND FD_INDEXNAME != '经营质量')),2),'990.00') FD_SCORE,\r\n" + 
				"              '0' TYPE\r\n" + 
				"FROM NS_RATING_INDEXES nri\r\n" + 
				"WHERE fd_stepid IN\r\n" + 
				"    (SELECT n.FD_ID\r\n" + 
				"     FROM ns_rating_step n\r\n" + 
				"     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"       AND n.FD_VAILD = '1'\r\n" + 
				"       AND n.fd_rateid = ?)\r\n" + 
				"  AND FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"  AND FD_INDEXNAME = '经营质量'\r\n" + 
				"UNION\r\n" + 
				"SELECT '管理能力' name,\r\n" + 
				"              to_char(round((FD_SCORE /\r\n" + 
				"                               (SELECT sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"                                FROM NS_RATING_INDEXES nri\r\n" + 
				"                                WHERE fd_stepid IN\r\n" + 
				"                                    (SELECT n.FD_ID\r\n" + 
				"                                     FROM ns_rating_step n\r\n" + 
				"                                     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"                                       AND n.FD_VAILD = '1'\r\n" + 
				"                                       AND n.fd_rateid = ?)\r\n" + 
				"                                  AND FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                                  AND FD_INDEXNAME != '管理能力')),2),'990.00') FD_SCORE,\r\n" + 
				"              '0' TYPE\r\n" + 
				"FROM NS_RATING_INDEXES nri\r\n" + 
				"WHERE fd_stepid IN\r\n" + 
				"    (SELECT n.FD_ID\r\n" + 
				"     FROM ns_rating_step n\r\n" + 
				"     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"       AND n.FD_VAILD = '1'\r\n" + 
				"       AND n.fd_rateid = ?)\r\n" + 
				"  AND FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"  AND FD_INDEXNAME = '管理能力'\r\n" + 
				"UNION\r\n" + 
				"SELECT '产业环境' name,\r\n" + 
				"              to_char(round((FD_SCORE /\r\n" + 
				"                               (SELECT sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"                                FROM NS_RATING_INDEXES nri\r\n" + 
				"                                WHERE fd_stepid IN\r\n" + 
				"                                    (SELECT n.FD_ID\r\n" + 
				"                                     FROM ns_rating_step n\r\n" + 
				"                                     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"                                       AND n.FD_VAILD = '1'\r\n" + 
				"                                       AND n.fd_rateid = ?)\r\n" + 
				"                                  AND FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                                  AND FD_INDEXNAME != '产业环境')),2),'990.00') FD_SCORE,\r\n" + 
				"              '0' TYPE\r\n" + 
				"FROM NS_RATING_INDEXES nri\r\n" + 
				"WHERE fd_stepid IN\r\n" + 
				"    (SELECT n.FD_ID\r\n" + 
				"     FROM ns_rating_step n\r\n" + 
				"     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"       AND n.FD_VAILD = '1'\r\n" + 
				"       AND n.fd_rateid = ?)\r\n" + 
				"  AND FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"  AND FD_INDEXNAME = '产业环境'\r\n" + 
				"UNION\r\n" + 
				"SELECT '产品品质' name,\r\n" + 
				"              to_char(round((FD_SCORE /\r\n" + 
				"                               (SELECT sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"                                FROM NS_RATING_INDEXES nri\r\n" + 
				"                                WHERE fd_stepid IN\r\n" + 
				"                                    (SELECT n.FD_ID\r\n" + 
				"                                     FROM ns_rating_step n\r\n" + 
				"                                     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"                                       AND n.FD_VAILD = '1'\r\n" + 
				"                                       AND n.fd_rateid = ?)\r\n" + 
				"                                  AND FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                                  AND FD_INDEXNAME != '产品品质')),2),'990.00') FD_SCORE,\r\n" + 
				"              '0' TYPE\r\n" + 
				"FROM NS_RATING_INDEXES nri\r\n" + 
				"WHERE fd_stepid IN\r\n" + 
				"    (SELECT n.FD_ID\r\n" + 
				"     FROM ns_rating_step n\r\n" + 
				"     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"       AND n.FD_VAILD = '1'\r\n" + 
				"       AND n.fd_rateid = ?)\r\n" + 
				"  AND FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"  AND FD_INDEXNAME = '产品品质'\r\n" + 
				"UNION\r\n" + 
				"SELECT '市场地位' name,\r\n" + 
				"              to_char(round((FD_SCORE /\r\n" + 
				"                               (SELECT sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"                                FROM NS_RATING_INDEXES nri\r\n" + 
				"                                WHERE fd_stepid IN\r\n" + 
				"                                    (SELECT n.FD_ID\r\n" + 
				"                                     FROM ns_rating_step n\r\n" + 
				"                                     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"                                       AND n.FD_VAILD = '1'\r\n" + 
				"                                       AND n.fd_rateid = ?)\r\n" + 
				"                                  AND FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                                  AND FD_INDEXNAME != '市场地位')),2),'990.00') FD_SCORE,\r\n" + 
				"              '0' TYPE\r\n" + 
				"FROM NS_RATING_INDEXES nri\r\n" + 
				"WHERE fd_stepid IN\r\n" + 
				"    (SELECT n.FD_ID\r\n" + 
				"     FROM ns_rating_step n\r\n" + 
				"     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"       AND n.FD_VAILD = '1'\r\n" + 
				"       AND n.fd_rateid = ?)\r\n" + 
				"  AND FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"  AND FD_INDEXNAME = '市场地位'\r\n" + 
				"UNION\r\n" + 
				"SELECT '商业模式' name,\r\n" + 
				"              to_char(round(FD_SCORE /\r\n" + 
				"                              (SELECT sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"                               FROM NS_RATING_INDEXES nri\r\n" + 
				"                               WHERE fd_stepid IN\r\n" + 
				"                                   (SELECT n.FD_ID\r\n" + 
				"                                    FROM ns_rating_step n\r\n" + 
				"                                    WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"                                      AND n.FD_VAILD = '1'\r\n" + 
				"                                      AND n.fd_rateid = ?)\r\n" + 
				"                                 AND FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                                 AND FD_INDEXNAME != '商业模式'),2),'990.00') FD_SCORE,\r\n" + 
				"              '0' TYPE\r\n" + 
				"FROM NS_RATING_INDEXES nri\r\n" + 
				"WHERE fd_stepid IN\r\n" + 
				"    (SELECT n.FD_ID\r\n" + 
				"     FROM ns_rating_step n\r\n" + 
				"     WHERE n.fd_step_name IN ('定量分析', '定性分析')\r\n" + 
				"       AND n.FD_VAILD = '1'\r\n" + 
				"       AND n.fd_rateid = ?)\r\n" + 
				"  AND FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"  AND FD_INDEXNAME = '商业模式'\r\n" + 
				"UNION\r\n" + 
				"/* 存量平均值计算 */\r\n" + 
				"SELECT '经营质量平均值' name,\r\n" + 
				"                 to_char(round((SELECT  (SELECT sum(zong) FROM\r\n" + 
				"                                       (SELECT (sum((SELECT (nri2.FD_SCORE /\r\n" + 
				"                                                                 (SELECT sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                                                  FROM NS_RATING_INDEXES nri\r\n" + 
				"                                                                  WHERE nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                                    AND nri.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                                                                    AND nri.FD_INDEXNAME != '经营质量')) SCORE\r\n" + 
				"                                                       FROM NS_RATING_INDEXES nri2\r\n" + 
				"                                                       WHERE nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                         AND nri2.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                                                         AND nri2.FD_INDEXNAME = '经营质量')) / count(*)) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs2\r\n" + 
				"                                        on ns.fd_stepid = nrs2.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                                          AND nrs2.fd_vaild = '1'\r\n" + 
				"                                          AND nrs2.fd_rateid IN (SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) /\r\n" + 
				"                                    (SELECT count(*)\r\n" + 
				"                                     FROM\r\n" + 
				"                                       (SELECT count(ns.fd_stepid) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs\r\n" + 
				"                                        on ns.fd_stepid = nrs.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                                        AND nrs.fd_vaild = '1'\r\n" + 
				"                                        and nrs.fd_rateid in(SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) AS 经营质量\r\n" + 
				"                                  FROM dual live),2),'9990.99') FD_SCORE,\r\n" + 
				"                 '1' TYPE\r\n" + 
				"FROM dual\r\n" + 
				"UNION\r\n" + 
				"/* 存量平均值计算 */\r\n" + 
				"SELECT '管理能力平均值' name,\r\n" + 
				"                 to_char(round((SELECT(SELECT sum(zong) FROM\r\n" + 
				"                                       (SELECT (sum((SELECT (nri2.FD_SCORE /\r\n" + 
				"                                                                 (SELECT sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                                                  FROM NS_RATING_INDEXES nri\r\n" + 
				"                                                                  WHERE nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                                    AND nri.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                                                                    AND nri.FD_INDEXNAME != '管理能力')) SCORE\r\n" + 
				"                                                       FROM NS_RATING_INDEXES nri2\r\n" + 
				"                                                       WHERE nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                         AND nri2.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                                                         AND nri2.FD_INDEXNAME = '管理能力')) / count(*)) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs2\r\n" + 
				"                                        on ns.fd_stepid = nrs2.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                                          AND nrs2.fd_vaild = '1'\r\n" + 
				"                                          AND nrs2.fd_rateid IN (SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) /\r\n" + 
				"                                    (SELECT count(*)\r\n" + 
				"                                     FROM\r\n" + 
				"                                       (SELECT count(ns.fd_stepid) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs\r\n" + 
				"                                        on ns.fd_stepid = nrs.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                                        AND nrs.fd_vaild = '1'\r\n" + 
				"                                        and nrs.fd_rateid in(SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) AS 管理能力\r\n" + 
				"                                  FROM dual live),2),'9990.99') FD_SCORE,\r\n" + 
				"                 '1' TYPE\r\n" + 
				"FROM dual\r\n" + 
				"UNION\r\n" + 
				"/* 存量平均值计算 */\r\n" + 
				"SELECT '产业环境平均值' name,\r\n" + 
				"                 to_char(round((SELECT (SELECT sum(zong) FROM\r\n" + 
				"                                       (SELECT (sum(\r\n" + 
				"                                                      (SELECT (nri2.FD_SCORE /\r\n" + 
				"                                                                 (SELECT sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                                                  FROM NS_RATING_INDEXES nri\r\n" + 
				"                                                                  WHERE nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                                    AND nri.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                                                                    AND nri.FD_INDEXNAME != '产业环境')) SCORE\r\n" + 
				"                                                       FROM NS_RATING_INDEXES nri2\r\n" + 
				"                                                       WHERE nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                         AND nri2.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                                                         AND nri2.FD_INDEXNAME = '产业环境')) / count(*)) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs2\r\n" + 
				"                                        on ns.fd_stepid = nrs2.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                                          AND nrs2.fd_vaild = '1'\r\n" + 
				"                                          AND nrs2.fd_rateid IN (SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) /\r\n" + 
				"                                    (SELECT count(*)\r\n" + 
				"                                     FROM\r\n" + 
				"                                       (SELECT count(ns.fd_stepid) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs\r\n" + 
				"                                        on ns.fd_stepid = nrs.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                                        AND nrs.fd_vaild = '1'\r\n" + 
				"                                        and nrs.fd_rateid in(SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) AS 产业环境\r\n" + 
				"                                  FROM dual live),2),'9990.99') FD_SCORE,\r\n" + 
				"                 '1' TYPE\r\n" + 
				"FROM dual\r\n" + 
				"UNION\r\n" + 
				"/* 存量平均值计算 */\r\n" + 
				"SELECT '产品品质平均值' name,\r\n" + 
				"                 to_char(round((SELECT (SELECT sum(zong) FROM\r\n" + 
				"                                       (SELECT (sum(\r\n" + 
				"                                                      (SELECT (nri2.FD_SCORE /\r\n" + 
				"                                                                 (SELECT sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                                                  FROM NS_RATING_INDEXES nri\r\n" + 
				"                                                                  WHERE nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                                    AND nri.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                                                                    AND nri.FD_INDEXNAME != '产品品质')) SCORE\r\n" + 
				"                                                       FROM NS_RATING_INDEXES nri2\r\n" + 
				"                                                       WHERE nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                         AND nri2.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                                                         AND nri2.FD_INDEXNAME = '产品品质')) / count(*)) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs2\r\n" + 
				"                                        on ns.fd_stepid = nrs2.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                                          AND nrs2.fd_vaild = '1'\r\n" + 
				"                                          AND nrs2.fd_rateid IN (SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) /\r\n" + 
				"                                    (SELECT count(*)\r\n" + 
				"                                     FROM\r\n" + 
				"                                       (SELECT count(ns.fd_stepid) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs\r\n" + 
				"                                        on ns.fd_stepid = nrs.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                                        AND nrs.fd_vaild = '1'\r\n" + 
				"                                        and nrs.fd_rateid in(SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) AS 产品品质\r\n" + 
				"                                  FROM dual live),2),'9990.99') FD_SCORE,\r\n" + 
				"                 '1' TYPE\r\n" + 
				"FROM dual\r\n" + 
				"UNION\r\n" + 
				"/* 存量平均值计算 */\r\n" + 
				"SELECT '市场地位平均值' name,\r\n" + 
				"                 to_char(round((SELECT (SELECT sum(zong) FROM\r\n" + 
				"                                       (SELECT (sum(\r\n" + 
				"                                                      (SELECT (nri2.FD_SCORE /\r\n" + 
				"                                                                 (SELECT sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                                                  FROM NS_RATING_INDEXES nri\r\n" + 
				"                                                                  WHERE nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                                    AND nri.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                                                                    AND nri.FD_INDEXNAME != '市场地位')) SCORE\r\n" + 
				"                                                       FROM NS_RATING_INDEXES nri2\r\n" + 
				"                                                       WHERE nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                         AND nri2.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                                                         AND nri2.FD_INDEXNAME = '市场地位')) / count(*)) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs2\r\n" + 
				"                                        on ns.fd_stepid = nrs2.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                                          AND nrs2.fd_vaild = '1'\r\n" + 
				"                                          AND nrs2.fd_rateid IN (SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) /\r\n" + 
				"                                    (SELECT count(*)\r\n" + 
				"                                     FROM\r\n" + 
				"                                       (SELECT count(ns.fd_stepid) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs\r\n" + 
				"                                        on ns.fd_stepid = nrs.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                                        AND nrs.fd_vaild = '1'\r\n" + 
				"                                        and nrs.fd_rateid in(SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) AS 市场地位\r\n" + 
				"                                  FROM dual live),2),'9990.99') FD_SCORE,\r\n" + 
				"                 '1' TYPE\r\n" + 
				"FROM dual\r\n" + 
				"UNION\r\n" + 
				"/* 存量平均值计算 */\r\n" + 
				"SELECT '商业模式平均值' name,\r\n" + 
				"                 to_char(round((SELECT (SELECT sum(zong) FROM\r\n" + 
				"                                       (SELECT (sum(\r\n" + 
				"                                                      (SELECT (nri2.FD_SCORE /\r\n" + 
				"                                                                 (SELECT sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                                                  FROM NS_RATING_INDEXES nri\r\n" + 
				"                                                                  WHERE nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                                    AND nri.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                                                                    AND nri.FD_INDEXNAME != '商业模式')) SCORE\r\n" + 
				"                                                       FROM NS_RATING_INDEXES nri2\r\n" + 
				"                                                       WHERE nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                         AND nri2.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                                                         AND nri2.FD_INDEXNAME = '商业模式')) / count(*)) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs2\r\n" + 
				"                                        on ns.fd_stepid = nrs2.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                                          AND nrs2.fd_vaild = '1'\r\n" + 
				"                                          AND nrs2.fd_rateid IN (SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) /\r\n" + 
				"                                    (SELECT count(*)\r\n" + 
				"                                     FROM\r\n" + 
				"                                       (SELECT count(ns.fd_stepid) zong\r\n" + 
				"                                        FROM NS_RATING_INDEXES ns\r\n" + 
				"                                        left join ns_rating_step nrs\r\n" + 
				"                                        on ns.fd_stepid = nrs.fd_id\r\n" + 
				"                                        WHERE ns.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                                        AND nrs.fd_vaild = '1'\r\n" + 
				"                                        and nrs.fd_rateid in(SELECT nmr.FD_ID\r\n" + 
				"                                                               FROM ns_main_rating nmr\r\n" + 
				"                                                               left join ns_bp_master nbm\r\n" + 
				"                                                              on nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
				"                                                                    where nbm.FD_LEASE_ORGANIZATION IN\r\n" + 
				"                                                                            ( /* 获取当前客户的客户经理 */\r\n" + 
				"                                                                             SELECT nbm.FD_LEASE_ORGANIZATION\r\n" + 
				"                                                                               FROM ns_bp_master nbm\r\n" + 
				"                                                                               LEFT JOIN ns_main_rating nmr\r\n" + 
				"                                                                                 ON nmr.FD_CUST_CODE = nbm.FD_BP_CODE\r\n" + 
				"                                                                              WHERE nmr.fd_id = ?)\r\n" + 
				"                                                                AND nmr.fd_rating_status = '020'\r\n" + 
				"                                                                AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1')\r\n" + 
				"                                        GROUP BY fd_stepid)) AS 商业模式\r\n" + 
				"                                  FROM dual live),2),'9990.99') FD_SCORE,\r\n" + 
				"                 '1' TYPE\r\n" + 
				"FROM dual\r\n" + 
				"UNION\r\n" + 
				"SELECT '经营质量最大值' name, '1' fd_score, '-1' TYPE FROM dual\r\n" + 
				"UNION\r\n" + 
				"SELECT '管理能力最大值' name, '1' fd_score, '-1' TYPE FROM dual\r\n" + 
				"UNION\r\n" + 
				"SELECT '产业环境最大值' name, '1' fd_score, '-1' TYPE FROM dual\r\n" + 
				"UNION\r\n" + 
				"SELECT '产品品质最大值' name, '1' fd_score, '-1' TYPE FROM dual\r\n" + 
				"UNION\r\n" + 
				"SELECT '市场地位最大值' name, '1' fd_score, '-1' TYPE FROM dual\r\n" + 
				"UNION\r\n" + 
				"SELECT '商业模式最大值' name, '1' fd_score, '-1' TYPE FROM dual" ;
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String,Object>> empty = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			empty.add(map);
			return empty;
		}
		return queryForList;
	}
	
	@Override
	public List<Map<String, Object>> getGrade(String id) {
		// /* 主体雷达图同赛道对比 */  

		String sql = "/* 主体雷达图同赛道对比 */ \r\n" + 
				"SELECT '经营质量' name, to_char(trunc(FD_SCORE,1), '990.9') FD_SCORE, '0' TYPE FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT n.FD_ID FROM ns_rating_step n WHERE n.fd_rateid = ? AND n.fd_step_name IN ('定量分析', '定性分析') AND FD_VAILD = '1') \r\n" + 
				"  AND FD_INDEXCATEGORY = '经营质量' AND FD_INDEXNAME = '经营质量' \r\n" + 
				"UNION \r\n" + 
				"SELECT '管理能力' name, to_char(trunc(FD_SCORE,1), '990.9') FD_SCORE, '0' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT n.FD_ID FROM ns_rating_step n WHERE n.fd_rateid = ? AND n.fd_step_name IN ('定量分析', '定性分析') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND FD_INDEXCATEGORY = '管理能力' AND FD_INDEXNAME = '管理能力' \r\n" + 
				"UNION \r\n" + 
				"SELECT '产业环境' name, to_char(trunc(FD_SCORE,1), '990.9') FD_SCORE, '0' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT n.FD_ID FROM ns_rating_step n WHERE n.fd_rateid = ? AND n.fd_step_name IN ('定量分析', '定性分析') AND FD_VAILD = '1') \r\n" + 
				"  AND FD_INDEXCATEGORY = '产业环境' AND FD_INDEXNAME = '产业环境' \r\n" + 
				"UNION \r\n" + 
				"SELECT '产品品质' name, to_char(trunc(FD_SCORE,1), '990.9') FD_SCORE, '0' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT n.FD_ID FROM ns_rating_step n WHERE n.fd_rateid = ? AND n.fd_step_name IN ('定量分析', '定性分析') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND FD_INDEXCATEGORY = '产品品质' AND FD_INDEXNAME = '产品品质' \r\n" + 
				"UNION \r\n" + 
				"SELECT '市场地位' name, to_char(trunc(FD_SCORE,1), '990.9') FD_SCORE, '0' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT n.FD_ID FROM ns_rating_step n WHERE n.fd_rateid = ? AND n.fd_step_name IN ('定量分析', '定性分析') AND FD_VAILD = '1') \r\n" + 
				"  AND FD_INDEXCATEGORY = '市场地位' AND FD_INDEXNAME = '市场地位' \r\n" + 
				"UNION \r\n" + 
				"SELECT '商业模式' name, to_char(trunc(FD_SCORE,1), '990.9') FD_SCORE, '0' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT n.FD_ID FROM ns_rating_step n WHERE n.fd_rateid = ? AND n.fd_step_name IN ('定量分析', '定性分析') AND FD_VAILD = '1') \r\n" + 
				"  AND FD_INDEXCATEGORY = '商业模式' AND FD_INDEXNAME = '商业模式' \r\n" + 
				"UNION \r\n" + 
				"SELECT '产品品质赛道平均值' name, to_char(trunc(sum(nri.FD_SCORE) / count(nri.FD_SCORE),1), '990.9') FD_SCORE, '1' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT fd_id \r\n" + 
				"     FROM ns_rating_step \r\n" + 
				"     WHERE fd_rateid IN \r\n" + 
				"         (SELECT fd_id /* 所有同赛道中的主体 ID */ \r\n" + 
				"          FROM ns_main_rating \r\n" + 
				"          WHERE TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?) AND fd_rating_status = '020' AND FD_VERSION = '3.0' and fd_vaild = '1') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND nri.FD_INDEXCATEGORY = '产品品质' AND nri.FD_INDEXNAME = '产品品质' \r\n" + 
				"UNION \r\n" + 
				"SELECT '市场地位赛道平均值' name, to_char(trunc(sum(nri.FD_SCORE) / count(nri.FD_SCORE),1), '990.9') FD_SCORE, '1' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT fd_id \r\n" + 
				"     FROM ns_rating_step \r\n" + 
				"     WHERE fd_rateid IN \r\n" + 
				"         (SELECT fd_id /* 所有同赛道中的主体 ID */ \r\n" + 
				"          FROM ns_main_rating \r\n" + 
				"          WHERE TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?) AND fd_rating_status = '020' AND FD_VERSION = '3.0' and fd_vaild = '1') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND nri.FD_INDEXCATEGORY = '市场地位' AND nri.FD_INDEXNAME = '市场地位' \r\n" + 
				"UNION \r\n" + 
				"SELECT '商业模式赛道平均值' name, \r\n" + 
				"                   to_char(trunc(sum(nri.FD_SCORE) / count(nri.FD_SCORE),1), '990.9') FD_SCORE, \r\n" + 
				"                   '1' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT fd_id \r\n" + 
				"     FROM ns_rating_step \r\n" + 
				"     WHERE fd_rateid IN \r\n" + 
				"         (SELECT fd_id /* 所有同赛道中的主体 ID */ \r\n" + 
				"          FROM ns_main_rating \r\n" + 
				"          WHERE TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?) AND fd_rating_status = '020' AND FD_VERSION = '3.0' and fd_vaild = '1') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND nri.FD_INDEXCATEGORY = '商业模式' AND nri.FD_INDEXNAME = '商业模式' \r\n" + 
				"UNION \r\n" + 
				"SELECT '经营质量赛道平均值' name, to_char(trunc(sum(nri.FD_SCORE) / count(nri.FD_SCORE),1), '990.9') FD_SCORE, '1' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT fd_id \r\n" + 
				"     FROM ns_rating_step \r\n" + 
				"     WHERE fd_rateid IN \r\n" + 
				"         (SELECT fd_id /* 所有同赛道中的主体 ID */ \r\n" + 
				"          FROM ns_main_rating \r\n" + 
				"          WHERE TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?) AND fd_rating_status = '020' AND FD_VERSION = '3.0' and fd_vaild = '1') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND nri.FD_INDEXCATEGORY = '经营质量' AND nri.FD_INDEXNAME = '经营质量' \r\n" + 
				"UNION \r\n" + 
				"SELECT '管理能力赛道平均值' name, to_char(trunc(sum(nri.FD_SCORE) / count(nri.FD_SCORE),1), '990.9') FD_SCORE, '1' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT fd_id FROM ns_rating_step WHERE fd_rateid IN \r\n" + 
				"         (SELECT fd_id /* 所有同赛道中的主体 ID */ \r\n" + 
				"          FROM ns_main_rating \r\n" + 
				"          WHERE TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?) AND fd_rating_status = '020' AND FD_VERSION = '3.0' and fd_vaild = '1') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND nri.FD_INDEXCATEGORY = '管理能力' AND nri.FD_INDEXNAME = '管理能力' \r\n" + 
				"UNION \r\n" + 
				"SELECT '产业环境赛道平均值' name, to_char(trunc(sum(nri.FD_SCORE) / count(nri.FD_SCORE),1), '990.9') FD_SCORE, '1' TYPE \r\n" + 
				"FROM NS_RATING_INDEXES nri \r\n" + 
				"WHERE fd_stepid IN \r\n" + 
				"    (SELECT fd_id \r\n" + 
				"     FROM ns_rating_step \r\n" + 
				"     WHERE fd_rateid IN \r\n" + 
				"         (SELECT fd_id /* 所有同赛道中的主体 ID */ \r\n" + 
				"          FROM ns_main_rating \r\n" + 
				"          WHERE TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?) AND fd_rating_status = '020' AND FD_VERSION = '3.0' and fd_vaild = '1') \r\n" + 
				"       AND FD_VAILD = '1') \r\n" + 
				"  AND nri.FD_INDEXCATEGORY = '产业环境' \r\n" + 
				"  AND nri.FD_INDEXNAME = '产业环境' \r\n" + 
				"UNION \r\n" + 
				"SELECT '经营质量最大值' name, (CASE \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_LRX_1_FW' OR fd_type = 'ZTTYMX_LRX_2_SC' THEN '60.0' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_SZX_1_FW' OR fd_type = 'ZTTYMX_SZX_2_SC' THEN '40.0' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_ZSX_1_FW' OR fd_type = 'ZTTYMX_ZSX_2_SC' THEN '50.0' \r\n" + 
				"                        END) fd_score, '-1' TYPE \r\n" + 
				"FROM ns_main_rating \r\n" + 
				"WHERE fd_id = ? \r\n" + 
				"UNION \r\n" + 
				"SELECT '管理能力最大值' name, (CASE \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_LRX_1_FW' OR fd_type = 'ZTTYMX_LRX_2_SC' THEN '8.1' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_SZX_1_FW' OR fd_type = 'ZTTYMX_SZX_2_SC' THEN '20.7' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_ZSX_1_FW' OR fd_type = 'ZTTYMX_ZSX_2_SC' THEN '14.3' \r\n" + 
				"                        END) fd_score, '-1' TYPE \r\n" + 
				"FROM ns_main_rating \r\n" + 
				"WHERE fd_id = ? \r\n" + 
				"UNION \r\n" + 
				"SELECT '产业环境最大值' name, (CASE \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_LRX_1_FW' OR fd_type = 'ZTTYMX_LRX_2_SC' THEN '5.3' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_SZX_1_FW' OR fd_type = 'ZTTYMX_SZX_2_SC' THEN '7.8' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_ZSX_1_FW' OR fd_type = 'ZTTYMX_ZSX_2_SC' THEN '4.7' \r\n" + 
				"                        END) fd_score, '-1' TYPE \r\n" + 
				"FROM ns_main_rating \r\n" + 
				"WHERE fd_id = ? \r\n" + 
				"UNION \r\n" + 
				"SELECT '产品品质最大值' name, (CASE \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_LRX_1_FW' OR fd_type = 'ZTTYMX_LRX_2_SC' THEN '4.0' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_SZX_1_FW' OR fd_type = 'ZTTYMX_SZX_2_SC' THEN '4.6' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_ZSX_1_FW' OR fd_type = 'ZTTYMX_ZSX_2_SC' THEN '4.1' \r\n" + 
				"                        END) fd_score, '-1' TYPE \r\n" + 
				"FROM ns_main_rating \r\n" + 
				"WHERE fd_id = ? \r\n" + 
				"UNION \r\n" + 
				"SELECT '市场地位最大值' name, (CASE \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_LRX_1_FW' OR fd_type = 'ZTTYMX_LRX_2_SC' THEN '7.6' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_SZX_1_FW' OR fd_type = 'ZTTYMX_SZX_2_SC' THEN '6.7' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_ZSX_1_FW' OR fd_type = 'ZTTYMX_ZSX_2_SC' THEN '6.1' \r\n" + 
				"                        END) fd_score, '-1' TYPE \r\n" + 
				"FROM ns_main_rating \r\n" + 
				"WHERE fd_id = ? \r\n" + 
				"UNION \r\n" + 
				"SELECT '商业模式最大值' name, (CASE \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_LRX_1_FW' OR fd_type = 'ZTTYMX_LRX_2_SC' THEN '15.0' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_SZX_1_FW' OR fd_type = 'ZTTYMX_SZX_2_SC' THEN '20.2' \r\n" + 
				"                            WHEN fd_type = 'ZTTYMX_ZSX_1_FW' OR fd_type = 'ZTTYMX_ZSX_2_SC' THEN '20.7' \r\n" + 
				"                        END) fd_score, '-1' TYPE \r\n" + 
				"FROM ns_main_rating \r\n" + 
				"WHERE fd_id = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String,Object>> empty = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			empty.add(map);
			return empty;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getIndicator(String id) {
		// /* 主体雷达图存量对比*/  

		String sql = "/* 主体雷达图存量对比*/\r\n" + 
				"select '经营质量' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"           from NS_RATING_INDEXES nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_rating_step n\r\n" + 
				"                  where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                    and n.FD_VAILD = '1'\r\n" + 
				"                    and n.fd_rateid = ?)\r\n" + 
				"            and FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"            and FD_INDEXNAME != '经营质量')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from NS_RATING_INDEXES nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_rating_step n\r\n" + 
				"         where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"           and n.FD_VAILD = '1'\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"   and FD_INDEXNAME = '经营质量'\r\n" + 
				"union\r\n" + 
				"select '管理能力' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"           from NS_RATING_INDEXES nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_rating_step n\r\n" + 
				"                  where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                    and n.FD_VAILD = '1'\r\n" + 
				"                    and n.fd_rateid = ?)\r\n" + 
				"            and FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"            and FD_INDEXNAME != '管理能力')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from NS_RATING_INDEXES nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_rating_step n\r\n" + 
				"         where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"           and n.FD_VAILD = '1'\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"   and FD_INDEXNAME = '管理能力'\r\n" + 
				"union\r\n" + 
				"select '产业环境' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"           from NS_RATING_INDEXES nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_rating_step n\r\n" + 
				"                  where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                    and n.FD_VAILD = '1'\r\n" + 
				"                    and n.fd_rateid = ?)\r\n" + 
				"            and FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"            and FD_INDEXNAME != '产业环境')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from NS_RATING_INDEXES nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_rating_step n\r\n" + 
				"         where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"           and n.FD_VAILD = '1'\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"   and FD_INDEXNAME = '产业环境'\r\n" + 
				"union\r\n" + 
				"select '产品品质' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"           from NS_RATING_INDEXES nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_rating_step n\r\n" + 
				"                  where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                    and n.FD_VAILD = '1'\r\n" + 
				"                    and n.fd_rateid = ?)\r\n" + 
				"            and FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"            and FD_INDEXNAME != '产品品质')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from NS_RATING_INDEXES nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_rating_step n\r\n" + 
				"         where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"           and n.FD_VAILD = '1'\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"   and FD_INDEXNAME = '产品品质'\r\n" + 
				"union\r\n" + 
				"select '市场地位' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"           from NS_RATING_INDEXES nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_rating_step n\r\n" + 
				"                  where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                    and n.FD_VAILD = '1'\r\n" + 
				"                    and n.fd_rateid = ?)\r\n" + 
				"            and FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"            and FD_INDEXNAME != '市场地位')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from NS_RATING_INDEXES nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_rating_step n\r\n" + 
				"         where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"           and n.FD_VAILD = '1'\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"   and FD_INDEXNAME = '市场地位'\r\n" + 
				"union\r\n" + 
				"select '商业模式' name,to_char(round(FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) * 100 FD_SCORE\r\n" + 
				"           from NS_RATING_INDEXES nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_rating_step n\r\n" + 
				"                  where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"                    and n.FD_VAILD = '1'\r\n" + 
				"                    and n.fd_rateid = ?)\r\n" + 
				"            and FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"            and FD_INDEXNAME != '商业模式'),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from NS_RATING_INDEXES nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_rating_step n\r\n" + 
				"         where n.fd_step_name in ('定量分析', '定性分析')\r\n" + 
				"           and n.FD_VAILD = '1'\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"   and FD_INDEXNAME = '商业模式'\r\n" + 
				"union\r\n" + 
				"select '经营质量平均值' name,to_char(round((select (select sum(zong) from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                             (select sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                 from NS_RATING_INDEXES nri\r\n" + 
				"                                where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                  and nri.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                                  and nri.FD_INDEXNAME != '经营质量'\r\n" + 
				"                                  and nri.fd_stepid in\r\n" + 
				"                                      (select fd_id from ns_rating_step\r\n" + 
				"                                        where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) SCORE\r\n" + 
				"                        from NS_RATING_INDEXES nri2\r\n" + 
				"                       where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                         and nri2.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                         and nri2.FD_INDEXNAME = '经营质量'\r\n" + 
				"                         and nri2.fd_stepid in\r\n" + 
				"                             (select fd_id from ns_rating_step\r\n" + 
				"                               where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) /\r\n" + 
				"                  count(ns.fd_stepid)) zong\r\n" + 
				"             from NS_RATING_INDEXES ns\r\n" + 
				"            where ns.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"              and ns.fd_stepid in (select fd_id from ns_rating_step where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1')\r\n" + 
				"            group by fd_stepid)) /\r\n" + 
				"       (select count(*)\r\n" + 
				"          from (select count(ns.fd_stepid) zong\r\n" + 
				"                  from NS_RATING_INDEXES ns\r\n" + 
				"                 where ns.FD_INDEXCATEGORY = '经营质量'\r\n" + 
				"                   and ns.fd_stepid in\r\n" + 
				"                       (select fd_id\r\n" + 
				"                          from ns_rating_step\r\n" + 
				"                         where fd_rateid in\r\n" + 
				"                               (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1')\r\n" + 
				"                           and fd_vaild = '1')\r\n" + 
				"                 group by fd_stepid)) as 经营质量 from dual),2),'9990.99') FD_SCORE,'1' type from dual\r\n" + 
				"union\r\n" + 
				"select '管理能力平均值' name,to_char(round((select (select sum(zong) from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                             (select sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                 from NS_RATING_INDEXES nri\r\n" + 
				"                                where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                  and nri.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                                  and nri.FD_INDEXNAME != '管理能力'\r\n" + 
				"                                  and nri.fd_stepid in\r\n" + 
				"                                      (select fd_id from ns_rating_step\r\n" + 
				"                                        where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) SCORE\r\n" + 
				"                        from NS_RATING_INDEXES nri2\r\n" + 
				"                       where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                         and nri2.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                         and nri2.FD_INDEXNAME = '管理能力'\r\n" + 
				"                         and nri2.fd_stepid in\r\n" + 
				"                             (select fd_id\r\n" + 
				"                                from ns_rating_step\r\n" + 
				"                               where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) /\r\n" + 
				"                  count(ns.fd_stepid)) zong\r\n" + 
				"             from NS_RATING_INDEXES ns\r\n" + 
				"            where ns.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"              and ns.fd_stepid in (select fd_id from ns_rating_step where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1')\r\n" + 
				"            group by fd_stepid)) /\r\n" + 
				"       (select count(*)\r\n" + 
				"          from (select count(ns.fd_stepid) zong\r\n" + 
				"                  from NS_RATING_INDEXES ns\r\n" + 
				"                 where ns.FD_INDEXCATEGORY = '管理能力'\r\n" + 
				"                   and ns.fd_stepid in\r\n" + 
				"                       (select fd_id\r\n" + 
				"                          from ns_rating_step\r\n" + 
				"                         where fd_rateid in\r\n" + 
				"                               (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1')\r\n" + 
				"                           and fd_vaild = '1')\r\n" + 
				"                 group by fd_stepid)) as 管理能力 from dual),2),'9990.99') FD_SCORE,'1' type from dual\r\n" + 
				"union\r\n" + 
				"select '产业环境平均值' name,to_char(round((select (select sum(zong) from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                             (select sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                 from NS_RATING_INDEXES nri\r\n" + 
				"                                where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                  and nri.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                                  and nri.FD_INDEXNAME != '产业环境'\r\n" + 
				"                                  and nri.fd_stepid in\r\n" + 
				"                                      (select fd_id from ns_rating_step\r\n" + 
				"                                        where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) SCORE\r\n" + 
				"                        from NS_RATING_INDEXES nri2\r\n" + 
				"                       where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                         and nri2.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                         and nri2.FD_INDEXNAME = '产业环境'\r\n" + 
				"                         and nri2.fd_stepid in\r\n" + 
				"                             (select fd_id\r\n" + 
				"                                from ns_rating_step\r\n" + 
				"                               where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) /\r\n" + 
				"                  count(ns.fd_stepid)) zong\r\n" + 
				"             from NS_RATING_INDEXES ns\r\n" + 
				"            where ns.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"              and ns.fd_stepid in (select fd_id from ns_rating_step where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1')\r\n" + 
				"            group by fd_stepid)) /\r\n" + 
				"       (select count(*)\r\n" + 
				"          from (select count(ns.fd_stepid) zong\r\n" + 
				"                  from NS_RATING_INDEXES ns\r\n" + 
				"                 where ns.FD_INDEXCATEGORY = '产业环境'\r\n" + 
				"                   and ns.fd_stepid in\r\n" + 
				"                       (select fd_id\r\n" + 
				"                          from ns_rating_step\r\n" + 
				"                         where fd_rateid in\r\n" + 
				"                               (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1')\r\n" + 
				"                           and fd_vaild = '1')\r\n" + 
				"                 group by fd_stepid)) as 产业环境 from dual),2),'9990.99') FD_SCORE,'1' type from dual\r\n" + 
				"union\r\n" + 
				"select '产品品质平均值' name,to_char(round((select (select sum(zong) from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                             (select sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                 from NS_RATING_INDEXES nri\r\n" + 
				"                                where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                  and nri.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                                  and nri.FD_INDEXNAME != '产品品质'\r\n" + 
				"                                  and nri.fd_stepid in\r\n" + 
				"                                      (select fd_id from ns_rating_step\r\n" + 
				"                                        where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) SCORE\r\n" + 
				"                        from NS_RATING_INDEXES nri2\r\n" + 
				"                       where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                         and nri2.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                         and nri2.FD_INDEXNAME = '产品品质'\r\n" + 
				"                         and nri2.fd_stepid in\r\n" + 
				"                             (select fd_id\r\n" + 
				"                                from ns_rating_step\r\n" + 
				"                               where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) /\r\n" + 
				"                  count(ns.fd_stepid)) zong\r\n" + 
				"             from NS_RATING_INDEXES ns\r\n" + 
				"            where ns.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"              and ns.fd_stepid in (select fd_id from ns_rating_step where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1')\r\n" + 
				"            group by fd_stepid)) /\r\n" + 
				"       (select count(*)\r\n" + 
				"          from (select count(ns.fd_stepid) zong\r\n" + 
				"                  from NS_RATING_INDEXES ns\r\n" + 
				"                 where ns.FD_INDEXCATEGORY = '产品品质'\r\n" + 
				"                   and ns.fd_stepid in\r\n" + 
				"                       (select fd_id\r\n" + 
				"                          from ns_rating_step\r\n" + 
				"                         where fd_rateid in\r\n" + 
				"                               (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1')\r\n" + 
				"                           and fd_vaild = '1')\r\n" + 
				"                 group by fd_stepid)) as 产品品质 from dual),2),'9990.99') FD_SCORE,'1' type from dual\r\n" + 
				"union\r\n" + 
				"select '市场地位平均值' name,to_char(round((select (select sum(zong) from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                             (select sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                 from NS_RATING_INDEXES nri\r\n" + 
				"                                where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                  and nri.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                                  and nri.FD_INDEXNAME != '市场地位'\r\n" + 
				"                                  and nri.fd_stepid in\r\n" + 
				"                                      (select fd_id from ns_rating_step\r\n" + 
				"                                        where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) SCORE\r\n" + 
				"                        from NS_RATING_INDEXES nri2\r\n" + 
				"                       where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                         and nri2.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                         and nri2.FD_INDEXNAME = '市场地位'\r\n" + 
				"                         and nri2.fd_stepid in\r\n" + 
				"                             (select fd_id\r\n" + 
				"                                from ns_rating_step\r\n" + 
				"                               where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) /\r\n" + 
				"                  count(ns.fd_stepid)) zong\r\n" + 
				"             from NS_RATING_INDEXES ns\r\n" + 
				"            where ns.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"              and ns.fd_stepid in (select fd_id from ns_rating_step where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1')\r\n" + 
				"            group by fd_stepid)) /\r\n" + 
				"       (select count(*)\r\n" + 
				"          from (select count(ns.fd_stepid) zong\r\n" + 
				"                  from NS_RATING_INDEXES ns\r\n" + 
				"                 where ns.FD_INDEXCATEGORY = '市场地位'\r\n" + 
				"                   and ns.fd_stepid in\r\n" + 
				"                       (select fd_id\r\n" + 
				"                          from ns_rating_step\r\n" + 
				"                         where fd_rateid in\r\n" + 
				"                               (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1')\r\n" + 
				"                           and fd_vaild = '1')\r\n" + 
				"                 group by fd_stepid)) as 市场地位 from dual),2),'9990.99') FD_SCORE,'1' type from dual\r\n" + 
				"union\r\n" + 
				"select '商业模式平均值' name,to_char(round((select (select sum(zong) from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                             (select sum(nri.FD_WEIGHT) * 100 FD_WEIGHT\r\n" + 
				"                                 from NS_RATING_INDEXES nri\r\n" + 
				"                                where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                  and nri.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                                  and nri.FD_INDEXNAME != '商业模式'\r\n" + 
				"                                  and nri.fd_stepid in\r\n" + 
				"                                      (select fd_id from ns_rating_step\r\n" + 
				"                                        where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) SCORE\r\n" + 
				"                        from NS_RATING_INDEXES nri2\r\n" + 
				"                       where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                         and nri2.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                         and nri2.FD_INDEXNAME = '商业模式'\r\n" + 
				"                         and nri2.fd_stepid in\r\n" + 
				"                             (select fd_id\r\n" + 
				"                                from ns_rating_step\r\n" + 
				"                               where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1'))) /\r\n" + 
				"                  count(ns.fd_stepid)) zong\r\n" + 
				"             from NS_RATING_INDEXES ns\r\n" + 
				"            where ns.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"              and ns.fd_stepid in (select fd_id from ns_rating_step where fd_rateid in (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1') and fd_vaild = '1')\r\n" + 
				"            group by fd_stepid)) /\r\n" + 
				"       (select count(*)\r\n" + 
				"          from (select count(ns.fd_stepid) zong\r\n" + 
				"                  from NS_RATING_INDEXES ns\r\n" + 
				"                 where ns.FD_INDEXCATEGORY = '商业模式'\r\n" + 
				"                   and ns.fd_stepid in\r\n" + 
				"                       (select fd_id\r\n" + 
				"                          from ns_rating_step\r\n" + 
				"                         where fd_rateid in\r\n" + 
				"                               (select FD_ID from ns_main_rating where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1')\r\n" + 
				"                           and fd_vaild = '1')\r\n" + 
				"                 group by fd_stepid)) as 商业模式 from dual),2),'9990.99') FD_SCORE,'1' type from dual\r\n" + 
				"union\r\n" + 
				"select '经营质量最大值' name,'1' fd_score, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '管理能力最大值' name,'1' fd_score, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '产业环境最大值' name,'1' fd_score, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '产品品质最大值' name,'1' fd_score, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '市场地位最大值' name,'1' fd_score, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '商业模式最大值' name,'1' fd_score, '-1' type from dual";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String,Object>> empty = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			empty.add(map);
			return empty;
		}
		return queryForList;
	}

	@Override
	public Map<String, Object> getNsMainLevelInfo(String id) {

	     // 原始 SQL  round(NAR.fd_final_sco,2)
		 // select fd_sco, fd_intern_name, fd_final_name, FD_TYPE, FD_SEGMENT_INDUSTRY, TRACK_TYPE, FD_ADMISSION_SUGGEST, FD_END_VALUE, fd_intern_level, fd_final_level, FD_SCORE,describe  from (select round(NMR.fd_sco, 2) fd_sco, /* 评级得分 */  fd_intern_name, /* 初评人 */  fd_intern_level, /*初评等级*/  fd_final_name, /* 复评人 */  fd_final_level, /*复评等级*/  case NMR.FD_TYPE  when 'ZTTYMX_ZSX_1_FW' then '服务型'  when 'ZTTYMX_ZSX_2_SC' then '生产型'  when 'ZTTYMX_SZX_1_FW' then '服务型'  when 'ZTTYMX_SZX_2_SC' then '生产型'  when 'ZTTYMX_LRX_1_FW' then '服务型'  when 'ZTTYMX_LRX_2_SC' then '生产型'  end FD_TYPE, /* 评级(图片,服务型生产型)类型 */  (select value_name  from NS_ZGC_FATHER_LEVEL  where value_code = NBM.FD_SEGMENT_INDUSTRY) FD_SEGMENT_INDUSTRY, /*国标行业,图片 M7590出*/  NMR.TRACK_TYPE, /* 赛道类型(图片,收入-利润型) */  NCMS.FD_ADMISSION_SUGGEST, /* 等级描述 */  round(NFAD.FD_END_VALUE / 10000) FD_END_VALUE, /* 收入规模 */  case NMR.TRACK_TYPE  when '增速型' then trim(to_char((select ((select FD_SCORE  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F75' and FD_VAILD = '1'  and nrs.FD_RATEID = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a') -  (select FD_SCORE  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F300' and FD_VAILD = '1'  and nrs.FD_RATEID = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a')) /  (select FD_SCORE  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F300' and FD_VAILD = '1'  and nrs.FD_RATEID = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a') FD_SCORE  from dual), '9999999999990.99')) || '%'  when '市值型' then trim(to_char(round(to_number(NBM.MARKET_SIZE) / 10000), '9999999999990')) || ' 万元'  when '利润型' then trim(to_char((select FD_SCORE / 10000  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F94' and FD_VAILD = '1'  and nrs.FD_RATEID = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a'), '9999999999990')) || ' 万元'  end  FD_SCORE, /*主营业务收入增长率*/  case NMR.TRACK_TYPE  when '增速型' then '主营业务收入增长率'  when '市值型' then '市值规模'  when '利润型' then '利润规模'  end  describe /* FD_SCORE 字段上面那个 case 的取值字段 */  from NS_MAIN_RATING NMR  left join NS_BP_MASTER NBM  on nbm.fd_bp_code = NMR.fd_cust_code  left join NS_CFG_MAIN_SCALE NCMS  on NCMS.FD_SCALE_LEVEL = NMR.Fd_Intern_Level  and NCMS.FD_TYPE = case  when NMR.FD_TYPE = 'ZTTYMX_LRX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_LRX_2_SC' then '080'  when NMR.FD_TYPE = 'ZTTYMX_SZX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_SZX_2_SC' then '060'  when NMR.FD_TYPE = 'ZTTYMX_ZSX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_ZSX_2_SC' then '070'  end  left join ns_fin_stat NFS  on NFS.fd_cust_no = NMR.FD_CUST_CODE  and NMR.FD_FIR_REP = NFS.FD_REPORT_BUSS_DATE  left join ns_fin_account_data NFAD  on NFS.fd_id = NFAD.fd_report_id  left join ns_rating_step NRS  on NMR.FD_ID = NRS.fd_rateid  left join ns_rating_indexes NRI  on NRS.FD_ID = NRI.fd_stepid  where NFAD.fd_dataitem_code = 'F75'  and NMR.fd_id = '0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a')  group by fd_sco, fd_intern_name, fd_final_name, FD_TYPE, FD_SEGMENT_INDUSTRY, TRACK_TYPE, FD_ADMISSION_SUGGEST, FD_END_VALUE, fd_intern_level, fd_final_level, FD_SCORE,describe  
		// /*删减后*/ select round(NMR.fd_sco, 2) fd_sco, /* 评级得分 */  fd_intern_name, /* 初评人 */  fd_intern_level, /*初评等级*/  fd_final_name, /* 复评人 */  fd_final_level, /*复评等级*/  case NMR.FD_TYPE  when 'ZTTYMX_ZSX_1_FW' then '服务型'  when 'ZTTYMX_ZSX_2_SC' then '生产型'  when 'ZTTYMX_SZX_1_FW' then '服务型'  when 'ZTTYMX_SZX_2_SC' then '生产型'  when 'ZTTYMX_LRX_1_FW' then '服务型'  when 'ZTTYMX_LRX_2_SC' then '生产型'  end FD_TYPE, /* 评级(图片,服务型生产型)类型 */  (select value_name from NS_ZGC_FATHER_LEVEL where value_code = NBM.FD_SEGMENT_INDUSTRY) FD_SEGMENT_INDUSTRY, /*国标行业,图片 M7590出*/  '收入-' || NMR.TRACK_TYPE TRACK_TYPE, /* 赛道类型(图片,收入-利润型) */  NCMS.FD_ADMISSION_SUGGEST, /* 等级描述 */  round(NRI.FD_configid / 10000) FD_END_VALUE, /* 收入规模 */  case NMR.TRACK_TYPE when '增速型' then  trim(to_char((select (((select FD_SCORE  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F75'  and FD_VAILD = '1'  and nrs.FD_RATEID = 'ba529b48-55db-40b4-9042-26873bbefeb1') -  (select FD_SCORE  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F300'  and FD_VAILD = '1'  and nrs.FD_RATEID = 'ba529b48-55db-40b4-9042-26873bbefeb1')) /  (select FD_SCORE  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F300'  and FD_VAILD = '1'  and nrs.FD_RATEID = 'ba529b48-55db-40b4-9042-26873bbefeb1')) * 100 FD_SCORE  from dual), '9999999999990.99')) || '%'  when '市值型' then trim(to_char(round(to_number(NBM.MARKET_SIZE) / 10000), '9999999999990')) || ' 万元'  when '利润型' then  trim(to_char((select FD_SCORE / 10000  from NS_RATING_INDEXES nri  left join ns_rating_step nrs  on nri.fd_stepid = nrs.fd_id  where nri.FD_INDEXCODE = 'F94'  and FD_VAILD = '1'  and nrs.FD_RATEID = 'ba529b48-55db-40b4-9042-26873bbefeb1'), '9999999999990')) || ' 万元'  end FD_SCORE, /*主营业务收入增长率*/  case NMR.TRACK_TYPE  when '增速型' then '主营业务收入增长率'  when '市值型' then '市值规模'  when '利润型' then '利润规模'  end DESCRIBE /* FD_SCORE 字段上面那个 case 的取值字段 */  from NS_MAIN_RATING NMR  left join NS_BP_MASTER NBM  on nbm.fd_bp_code = NMR.fd_cust_code  left join NS_CFG_MAIN_SCALE NCMS  on NCMS.FD_SCALE_LEVEL = NMR.Fd_Intern_Level  and NCMS.FD_TYPE = case  when NMR.FD_TYPE = 'ZTTYMX_LRX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_LRX_2_SC' then '080'  when NMR.FD_TYPE = 'ZTTYMX_SZX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_SZX_2_SC' then '060'  when NMR.FD_TYPE = 'ZTTYMX_ZSX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_ZSX_2_SC' then '070'  end  left join ns_rating_step NRS  on NMR.FD_ID = NRS.fd_rateid  left join ns_rating_indexes NRI  on NRS.FD_ID = NRI.fd_stepid  where nri.fd_indexcode = 'F75'  and nrs.fd_vaild = '1'  and NMR.fd_id = 'ba529b48-55db-40b4-9042-26873bbefeb1'

		String sql = "select round(NMR.fd_sco, 2) fd_sco, /* 评级得分 */\r\n" + 
				"       fd_intern_name, /* 初评人 */\r\n" + 
				"       fd_intern_level, /*初评等级*/\r\n" + 
				"       fd_final_name, /* 复评人 */\r\n" + 
				"       fd_final_level, /*复评等级*/\r\n" + 
				"       case NMR.FD_TYPE\r\n" + 
				"         when 'ZTTYMX_ZSX_1_FW' then '服务型'\r\n" + 
				"         when 'ZTTYMX_ZSX_2_SC' then '生产型'\r\n" + 
				"         when 'ZTTYMX_SZX_1_FW' then '服务型'\r\n" + 
				"         when 'ZTTYMX_SZX_2_SC' then '生产型'\r\n" + 
				"         when 'ZTTYMX_LRX_1_FW' then '服务型'\r\n" + 
				"         when 'ZTTYMX_LRX_2_SC' then '生产型'\r\n" + 
				"       end FD_TYPE, /* 评级(图片,服务型生产型)类型 */\r\n" + 
				"       (select value_name from NS_ZGC_FATHER_LEVEL where value_code = NBM.FD_SEGMENT_INDUSTRY) FD_SEGMENT_INDUSTRY, /*国标行业,图片 M7590出*/\r\n" + 
				"       '收入-' || NMR.TRACK_TYPE TRACK_TYPE, /* 赛道类型(图片,收入-利润型) */\r\n" + 
				"       NCMS.FD_ADMISSION_SUGGEST, /* 等级描述 */\r\n" + 
				"       round(NRI.FD_configid / 10000) FD_END_VALUE, /* 收入规模 */\r\n" + 
				"       case NMR.TRACK_TYPE when '增速型' then\r\n" + 
				"          trim(to_char((select (((select FD_SCORE\r\n" + 
				"                                  from NS_RATING_INDEXES nri\r\n" + 
				"                                  left join ns_rating_step nrs\r\n" + 
				"                                    on nri.fd_stepid = nrs.fd_id\r\n" + 
				"                                 where nri.FD_INDEXCODE = 'F75'\r\n" + 
				"                                   and FD_VAILD = '1'\r\n" + 
				"                                   and nrs.FD_RATEID = ?) -\r\n" + 
				"                              (select FD_SCORE\r\n" + 
				"                                  from NS_RATING_INDEXES nri\r\n" + 
				"                                  left join ns_rating_step nrs\r\n" + 
				"                                    on nri.fd_stepid = nrs.fd_id\r\n" + 
				"                                 where nri.FD_INDEXCODE = 'F300'\r\n" + 
				"                                   and FD_VAILD = '1'\r\n" + 
				"                                   and nrs.FD_RATEID = ?)) /\r\n" + 
				"                              (select FD_SCORE\r\n" + 
				"                                 from NS_RATING_INDEXES nri\r\n" + 
				"                                 left join ns_rating_step nrs\r\n" + 
				"                                   on nri.fd_stepid = nrs.fd_id\r\n" + 
				"                                where nri.FD_INDEXCODE = 'F300'\r\n" + 
				"                                  and FD_VAILD = '1'\r\n" + 
				"                                  and nrs.FD_RATEID = ?)) * 100 FD_SCORE\r\n" + 
				"                         from dual), '9999999999990.99')) || '%'\r\n" + 
				"         when '市值型' then trim(to_char(round(to_number(NBM.MARKET_SIZE) / 10000), '9999999999990')) || ' 万元'\r\n" + 
				"         when '利润型' then\r\n" + 
				"          trim(to_char((select FD_SCORE / 10000\r\n" + 
				"                         from NS_RATING_INDEXES nri\r\n" + 
				"                         left join ns_rating_step nrs\r\n" + 
				"                           on nri.fd_stepid = nrs.fd_id\r\n" + 
				"                        where nri.FD_INDEXCODE = 'F94'\r\n" + 
				"                          and FD_VAILD = '1'\r\n" + 
				"                          and nrs.FD_RATEID = ?), '9999999999990')) || ' 万元'\r\n" + 
				"       end FD_SCORE, /*主营业务收入增长率*/\r\n" + 
				"       case NMR.TRACK_TYPE\r\n" + 
				"         when '增速型' then '主营业务收入增长率'\r\n" + 
				"         when '市值型' then '市值规模'\r\n" + 
				"         when '利润型' then '利润规模'\r\n" + 
				"       end DESCRIBE /* FD_SCORE 字段上面那个 case 的取值字段 */\r\n" + 
				"  from NS_MAIN_RATING NMR\r\n" + 
				"  left join NS_BP_MASTER NBM\r\n" + 
				"    on nbm.fd_bp_code = NMR.fd_cust_code\r\n" + 
				"  left join NS_CFG_MAIN_SCALE NCMS\r\n" + 
				"    on NCMS.FD_SCALE_LEVEL = NMR.Fd_FINAL_Level\r\n" + 
				"   and NCMS.FD_TYPE = case\r\n" + 
				"         when NMR.FD_TYPE = 'ZTTYMX_LRX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_LRX_2_SC' then '080'\r\n" + 
				"         when NMR.FD_TYPE = 'ZTTYMX_SZX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_SZX_2_SC' then '060'\r\n" + 
				"         when NMR.FD_TYPE = 'ZTTYMX_ZSX_1_FW' or NMR.FD_TYPE = 'ZTTYMX_ZSX_2_SC' then '070'\r\n" + 
				"       end\r\n" + 
				"  left join ns_rating_step NRS\r\n" + 
				"    on NMR.FD_ID = NRS.fd_rateid\r\n" + 
				"  left join ns_rating_indexes NRI\r\n" + 
				"    on NRS.FD_ID = NRI.fd_stepid\r\n" + 
				" where nri.fd_indexcode = 'F75'\r\n" + 
				"   and nrs.fd_vaild = '1'\r\n" + 
				"   and NMR.fd_id = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			return map;
		}
		Map<String, Object> result = queryForList.get(0);
		return result;
	}

	@Override
	public Map<String, Object> getNsMainLevelList(String id) {
		// 原始 SQL
		// select nmr.FD_FINAL_LEVEL,  TO_CHAR(nmr.fd_final_date, 'YYYY-MM-DD') FD_FINAL_DATE,  substr(nmr.F_DATE,1,10) FD_DATE,  '整体来看, 企业主体信用' || (case  when nmr.FD_FINAL_LEVEL in ('A', 'B', 'C') then '较好'  when nmr.FD_FINAL_LEVEL in ('D', 'E', 'F') then '一般'  else '较低'  end) describe,  '企业主体违约概率' || (case  when nmr.FD_FINAL_LEVEL in ('A', 'B', 'C') then '较低'  when nmr.FD_FINAL_LEVEL in ('D', 'E', 'F') then '中等'  else '较高'  end) warning,  '信用' || (case  when nmr.FD_FINAL_LEVEL in ('A', 'B', 'C') then '较好'  when nmr.FD_FINAL_LEVEL in ('D', 'E', 'F') then '一般'  else '较低'  end) leveled  from NS_MAIN_RATING nmr  where nmr.fd_id = '997342c1-7ac6-400d-ba73-f5b15ceff92e'

		String sql = "select nmr.FD_FINAL_LEVEL,\r\n" + 
				"       TO_CHAR(nmr.fd_final_date, 'YYYY-MM-DD') FD_FINAL_DATE,\r\n" + 
				"       substr(nmr.F_DATE,1,10) FD_DATE,\r\n" + 
				"       '企业主体信用' || (case\r\n" + 
				"         when nmr.FD_FINAL_LEVEL in ('A', 'B', 'C') then '较好'\r\n" + 
				"         when nmr.FD_FINAL_LEVEL in ('D', 'E', 'F') then '一般'\r\n" + 
				"         else '较低'\r\n" + 
				"       end) describe,\r\n" + 
				"       '企业主体违约概率' || (case\r\n" + 
				"         when nmr.FD_FINAL_LEVEL in ('A', 'B', 'C') then '较低'\r\n" + 
				"         when nmr.FD_FINAL_LEVEL in ('D', 'E', 'F') then '中等'\r\n" + 
				"         else '较高'\r\n" + 
				"       end) warning,\r\n" + 
				"       '信用' || (case\r\n" + 
				"         when nmr.FD_FINAL_LEVEL in ('A', 'B', 'C') then '较好'\r\n" + 
				"         when nmr.FD_FINAL_LEVEL in ('D', 'E', 'F') then '一般'\r\n" + 
				"         else '较低'\r\n" + 
				"       end) leveled\r\n" + 
				"  from NS_MAIN_RATING nmr\r\n" + 
				" where nmr.fd_id = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id);
		if(queryForList.isEmpty()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			return map;
		}
		Map<String, Object> result = queryForList.get(0);
		return result;
	}

	@Override
	public List<Map<String, Object>> getMainTrait(String id) {
		// 主体标签

		Map<String, Object> levelStatus = getLevelStatus(id);
		String status = (String)levelStatus.get("FD_VAILD");
		List<Map<String, Object>> queryForList = null;
		if("1".equals(status)) {
			//--复评 SQL
			String sql = "select DECODE(n.fd_intern_level, n.fd_final_level, null, 'Y') \"result\", '0' type /*初复评有差异*/ \r\n" + 
					"  from ns_main_rating n WHERE n.FD_ID = ? \r\n" + 
					"union \r\n" + 
					"select case \r\n" + 
					"         when count(ns.RISK_POINT) > 3 then  'Y' \r\n" + 
					"       end as co, '1' type /*重大风险提示超过 3 个*/ \r\n" + 
					"          from NS_MAIN_RATING nmr \r\n" + 
					"          left join ns_major_record ma \r\n" + 
					"            on nmr.fd_id = ma.SERIAL_NUMBER \r\n" + 
					"          left join NS_MAJOR_RISKWARNINGDATAS ns \r\n" + 
					"            on ma.tip_number = ns.TIP_NUMBER \r\n" + 
					"         where ma.rate_type = '020' \r\n" + 
					"           and ma.isselected = 'Y' \r\n" + 
					"           and nmr.fd_id = ? \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'2' type /*存量排名前十*/ \r\n" + 
					"  from (select rownum ro, t.* \r\n" + 
					"          from ( /*存量排序*/ \r\n" + 
					"                select n.* \r\n" + 
					"                  from ns_main_RATING n \r\n" + 
					"                 where n.fd_version = '3.0'  and n.fd_rating_status = '020' and fd_vaild = '1'\r\n" + 
					"                 order by n.fd_sco desc) t) ot where ot.ro <= 10 and ot.fd_id = ? \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'3' type /* 部门排名前三*/ \r\n" + 
					"  from (select rownum,t.* from(SELECT nmr.* \r\n" + 
					"  FROM ns_main_rating nmr \r\n" + 
					"  LEFT JOIN ns_bp_master nbm/* 获取当前客户的客户经理 */ \r\n" + 
					"    ON nmr.FD_CUST_CODE = nbm.fd_bp_code \r\n" + 
					"  left join ns_bp_master nbm2 \r\n" + 
					"    on nbm2.FD_LEASE_ORGANIZATION = nbm.FD_LEASE_ORGANIZATION \r\n" + 
					"  LEFT JOIN ns_main_rating nmr2 \r\n" + 
					"    ON nmr2.FD_CUST_CODE = nbm2.FD_BP_CODE \r\n" + 
					" WHERE nmr2.fd_id = ? \r\n" + 
					"   AND nmr.fd_rating_status = '020' AND nmr.fd_version = '3.0'  and nmr.fd_vaild = '1'\r\n" + 
					"   order by nmr.fd_sco desc) t where rownum <= 3) ot where fd_id = ? \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'4' type /* 赛道排名前五 */  \r\n" + 
					"  FROM (select rownum,t.* from (SELECT n.* /* 所有同赛道中的主体 ID */ \r\n" + 
					"  FROM ns_main_rating n \r\n" + 
					"  left join ns_main_rating nmr \r\n" + 
					"  on n.fd_id = nmr.fd_id \r\n" + 
					" WHERE nmr.TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?) \r\n" + 
					"   AND n.fd_rating_status = '020' AND n.FD_VERSION = '3.0' and n.fd_vaild = '1'\r\n" + 
					"   order by n.fd_sco desc) t where rownum <= 5) where fd_id = ?";
			queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id);
		} else {
			// 历史评级 SQL
			String sql = "select DECODE(n.fd_intern_level, n.fd_final_level, null, 'Y') \"result\", '0' type /*初复评有差异*/  \r\n" + 
					"  from ns_main_rating n WHERE n.FD_ID = ?  \r\n" + 
					"union  \r\n" + 
					"select case  \r\n" + 
					"         when count(ns.RISK_POINT) > 3 then  'Y'  \r\n" + 
					"       end as co, '1' type /*重大风险提示超过 3 个*/  \r\n" + 
					"          from NS_MAIN_RATING nmr  \r\n" + 
					"          left join ns_major_record ma  \r\n" + 
					"            on nmr.fd_id = ma.SERIAL_NUMBER  \r\n" + 
					"          left join NS_MAJOR_RISKWARNINGDATAS ns  \r\n" + 
					"            on ma.tip_number = ns.TIP_NUMBER  \r\n" + 
					"         where ma.rate_type = '020'  \r\n" + 
					"           and ma.isselected = 'Y'  \r\n" + 
					"           and nmr.fd_id = ?  \r\n" + 
					"union  \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'2' type /*存量排名前十*/  \r\n" + 
					"  from (select rownum ro, t.*  \r\n" + 
					"          from ( /*存量排序*/  \r\n" + 
					"                select * from (select n.*  \r\n" + 
					"                  from ns_main_RATING n  \r\n" + 
					"                 where n.fd_version = '3.0'  and n.fd_rating_status = '020' and fd_vaild = '1'\r\n" + 
					"                 union\r\n" + 
					"                 select * from ns_main_RATING where fd_id = ?)\r\n" + 
					"                 order by fd_sco desc) t) ot where ot.ro <= 10 and ot.fd_id = ?  \r\n" + 
					"union  \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'3' type /* 部门排名前三*/  \r\n" + 
					"  from (select rownum,t.* from(select * from (SELECT nmr.*  \r\n" + 
					"  FROM ns_main_rating nmr  \r\n" + 
					"  LEFT JOIN ns_bp_master nbm/* 获取当前客户的客户经理 */  \r\n" + 
					"    ON nmr.FD_CUST_CODE = nbm.fd_bp_code  \r\n" + 
					"  left join ns_bp_master nbm2  \r\n" + 
					"    on nbm2.FD_LEASE_ORGANIZATION = nbm.FD_LEASE_ORGANIZATION  \r\n" + 
					"  LEFT JOIN ns_main_rating nmr2  \r\n" + 
					"    ON nmr2.FD_CUST_CODE = nbm2.FD_BP_CODE  \r\n" + 
					" WHERE nmr2.fd_id = ?  \r\n" + 
					"   AND nmr.fd_rating_status = '020' AND nmr.fd_version = '3.0'  and nmr.fd_vaild = '1' \r\n" + 
					"   union\r\n" + 
					"   select * from ns_main_RATING where fd_id = ?)\r\n" + 
					"   order by fd_sco desc) t where rownum <= 3) ot where fd_id = ?  \r\n" + 
					"union  \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'4' type /* 赛道排名前五 */   \r\n" + 
					"  FROM (select rownum,t.* from (select * from (SELECT n.* /* 所有同赛道中的主体 ID */  \r\n" + 
					"  FROM ns_main_rating n  \r\n" + 
					"  left join ns_main_rating nmr  \r\n" + 
					"  on n.fd_id = nmr.fd_id  \r\n" + 
					" WHERE nmr.TRACK_TYPE = (SELECT DISTINCT TRACK_TYPE FROM ns_main_rating WHERE fd_id = ?)  \r\n" + 
					"   AND n.fd_rating_status = '020' AND n.FD_VERSION = '3.0' and n.fd_vaild = '1' \r\n" + 
					"   union\r\n" + 
					"   select * from ns_main_RATING where fd_id = ?)\r\n" + 
					"   order by fd_sco desc) t where rownum <= 5) where fd_id = ?";
			queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id,id);
		}
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	// 获取评级状态
	private Map<String, Object> getLevelStatus(String id) {
		String sql = "SELECT fd_vaild,fd_rating_status FROM NS_MAIN_RATING WHERE fd_id = ?  AND fd_version = '3.0'";
		List<Map<String,Object>> list = jdbc.queryForList(sql, id);
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			return map;
		}
		return null;
	}

	@Override
	public Map<String, Object> getMainScore(String id) {
		String sql = "select FD_SCO from NS_MAIN_RATING where fd_id = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return map;
		}
		return queryForList.get(0);
	}

}
