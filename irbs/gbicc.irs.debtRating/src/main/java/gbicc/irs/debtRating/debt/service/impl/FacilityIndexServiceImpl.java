package gbicc.irs.debtRating.debt.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.debtRating.debt.entity.FacilityIndex;
import gbicc.irs.debtRating.debt.repository.FacilityIndexRepository;
import gbicc.irs.debtRating.debt.service.FacilityIndexService;

@Service("FacilityIndexServiceImpl")
public class FacilityIndexServiceImpl extends DaoServiceImpl<FacilityIndex, String, FacilityIndexRepository>
		implements FacilityIndexService {

	@Autowired
	private JdbcTemplate jdbc;


	@Override
	public List<Map<String, Object>> getAssetsDescribe(String id) {
		// /*债项页面整体描述 SQL*/
		Map<String, Object> stauts = getHistoryStatus(id);
		String r = (String)stauts.get("FD_VAILD");
		List<Map<String, Object>> queryForList =null;
		
		if("1".equals(r)) {
			String sql = "/*债项页面整体描述 SQL*/\r\n" + 
				"SELECT round(nfr.fd_final_sco, 2) fd_final_sco, /*评级得分*/\r\n" + 
				"       round(nar.FINANCE_AMOUNT / 10000) FINANCE_AMOUNT, /*租赁本金*/\r\n" + 
				"       nar.LEASE_TERM || '年' LEASE_TERM, /*租赁期限*/\r\n" + 
				"       nfr.fd_intern_name, /*初评人*/\r\n" + 
				"       nfr.FD_INTERN_LEVEL, /* 初评等级 */\r\n" + 
				"       nfr.fd_final_name || '，' || nfr.FD_ASSETS_NAME as fd_final_name, /*复评人,资产经理*/\r\n" + 
				"       nfr.FD_FINAL_LEVEL, /* 复评等级 */\r\n" + 
				"       NCMS.FD_ADMISSION_SUGGEST,\r\n" + 
				"       '本项目债项安全性' || (CASE\r\n" + 
				"                          WHEN nfr.FD_FINAL_LEVEL IN ('Ⅰ', 'Ⅱ') THEN '较强'\r\n" + 
				"                          WHEN nfr.FD_FINAL_LEVEL IN ('Ⅲ', 'Ⅳ') THEN '一般'\r\n" + 
				"                          ELSE '较低'\r\n" + 
				"                      END) || '，风险等级为' || (CASE\r\n" + 
				"                                                WHEN nfr.FD_FINAL_LEVEL IN ('Ⅰ', 'Ⅱ') THEN '低风险'\r\n" + 
				"                                                WHEN nfr.FD_FINAL_LEVEL IN ('Ⅲ', 'Ⅳ') THEN '中风险'\r\n" + 
				"                                                WHEN nfr.FD_FINAL_LEVEL IN ('Ⅴ') THEN '高风险'\r\n" + 
				"                                                ELSE '极高风险'\r\n" + 
				"                                            END) DESCRIBE, /*债项等级描述*/\r\n" + 
				"                        trim(to_char((SELECT ( (SELECT ro FROM\r\n" + 
				"                                                     (SELECT f.ro FROM\r\n" + 
				"                                                        (SELECT rownum - 1 ro, f.* FROM\r\n" + 
				"                                                           (SELECT n.* FROM NS_FACILITY_RATING n WHERE fd_version = '3.0' AND fd_vaild = '1'\r\n" + 
				"                                                              /* 从 NS_FACILITY_RATING 表中读取评级状态 */\r\n" + 
				"                                                              AND FD_RATING_STATUS = (select FD_RATING_STATUS from NS_FACILITY_RATING where fd_version = '3.0' and fd_id = ?)\r\n" + 
				"                                                              AND fd_final_sco > 0\r\n" + 
				"                                                            ORDER BY fd_final_sco) f) f\r\n" + 
				"                                                      WHERE fd_id = ?)) /\r\n" + 
				"                                                  (SELECT nu FROM\r\n" + 
				"                                                     (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu\r\n" + 
				"                                                      FROM (SELECT count(*) - 1 nu FROM\r\n" + 
				"                                                           (SELECT rownum ro, f.* FROM\r\n" + 
				"                                                              (SELECT n.*\r\n" + 
				"                                                               FROM NS_FACILITY_RATING n\r\n" + 
				"                                                               WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_final_sco > 0 AND fd_vaild = '1'\r\n" + 
				"                                                               ORDER BY fd_final_sco) f))))) AS rankingOnGroup /*存量资产评级中的得分分位数*/\r\n" + 
				"                                        FROM dual),'99990.99')) * 100 || '%' AS rankingOnGroup,\r\n" + 
				"                        /*在公司中的得分分位数*/\r\n" + 
				"                        trim(to_char((SELECT ( (SELECT ro FROM\r\n" + 
				"                                                     (SELECT f.ro FROM\r\n" + 
				"                                                        (SELECT rownum - 1 ro, f.* FROM\r\n" + 
				"                                                           (SELECT n.* FROM NS_FACILITY_RATING n\r\n" + 
				"                                                            WHERE fd_version = '3.0' AND fd_vaild = '1'\r\n" + 
				"                                                              /* 从 NS_FACILITY_RATING 表中读取评级状态 */\r\n" + 
				"                                                              AND FD_RATING_STATUS = (select FD_RATING_STATUS from NS_FACILITY_RATING where fd_version = '3.0' and fd_id = ?)\r\n" + 
				"                                                              AND fd_final_sco > 0 AND FD_PROJECT_CODE IN\r\n" + 
				"                                                                (SELECT project_number FROM ns_prj_project\r\n" + 
				"                                                                 WHERE LEASE_ORGANIZATION =\r\n" + 
				"                                                                     /* 读取项目所属部门 */\r\n" + 
				"                                                                     (SELECT LEASE_ORGANIZATION FROM Ns_Prj_Project\r\n" + 
				"                                                                      WHERE project_number = (SELECT FD_PROJECT_CODE FROM NS_FACILITY_RATING WHERE FD_ID = ?)))\r\n" + 
				"                                                            ORDER BY fd_final_sco) f) f\r\n" + 
				"                                                      WHERE fd_id = ?)) /\r\n" + 
				"                                                  (SELECT nu FROM (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
				"                                                        (SELECT count(*) - 1 nu FROM\r\n" + 
				"                                                           (SELECT rownum ro, f.* FROM\r\n" + 
				"                                                              (SELECT n.* FROM NS_FACILITY_RATING n\r\n" + 
				"                                                               WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_final_sco > 0 AND fd_vaild = '1'\r\n" + 
				"                                                                 AND FD_PROJECT_CODE IN\r\n" + 
				"                                                                   (SELECT project_number FROM ns_prj_project\r\n" + 
				"                                                                    WHERE LEASE_ORGANIZATION = (SELECT LEASE_ORGANIZATION\r\n" + 
				"                                                                         FROM Ns_Prj_Project WHERE project_number = (SELECT FD_PROJECT_CODE FROM NS_FACILITY_RATING WHERE FD_ID = ?)))\r\n" + 
				"                                                               ORDER BY fd_final_sco) f))))) AS rankingOnClique /*在部门中所处的位置*/\r\n" + 
				"                                        FROM dual),'99990.99')) * 100 || '%' AS rankingOnClique /*在部门中所处的位置*/\r\n" + 
				"FROM NS_FACILITY_RATING nfr\r\n" + 
				"LEFT JOIN NS_CFG_MAIN_SCALE ncms ON nfr.fd_final_level = ncms.FD_SCALE_LEVEL\r\n" + 
				"AND ncms.fd_type = '030'\r\n" + 
				"LEFT JOIN NS_ASSETS_RATING nar ON nfr.ASSETS_RATING_ID = nar.fd_id\r\n" + 
				"WHERE nfr.FD_ID = ?";
				queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id);
		} else {
			String sql = "/*债项页面整体描述 SQL*/ \r\n" + 
					"SELECT round(nfr.fd_final_sco, 2) fd_final_sco, /*评级得分*/ \r\n" + 
					"       round(nar.FINANCE_AMOUNT / 10000) FINANCE_AMOUNT, /*租赁本金*/ \r\n" + 
					"       nar.LEASE_TERM || '年' LEASE_TERM, /*租赁期限*/ \r\n" + 
					"       nfr.fd_intern_name, /*初评人*/ \r\n" + 
					"       nfr.FD_INTERN_LEVEL, /* 初评等级 */ \r\n" + 
					"       nfr.fd_final_name || '，' || nfr.FD_ASSETS_NAME as fd_final_name, /*复评人,资产经理*/ \r\n" + 
					"       nfr.FD_FINAL_LEVEL, /* 复评等级 */ \r\n" + 
					"       NCMS.FD_ADMISSION_SUGGEST, \r\n" + 
					"       '本项目债项安全性' || (CASE \r\n" + 
					"                          WHEN nfr.FD_FINAL_LEVEL IN ('Ⅰ', 'Ⅱ') THEN '较强' \r\n" + 
					"                          WHEN nfr.FD_FINAL_LEVEL IN ('Ⅲ', 'Ⅳ') THEN '一般' \r\n" + 
					"                          ELSE '较低' \r\n" + 
					"                      END) || '，风险等级为' || (CASE \r\n" + 
					"                                                WHEN nfr.FD_FINAL_LEVEL IN ('Ⅰ', 'Ⅱ') THEN '低风险' \r\n" + 
					"                                                WHEN nfr.FD_FINAL_LEVEL IN ('Ⅲ', 'Ⅳ') THEN '中风险' \r\n" + 
					"                                                WHEN nfr.FD_FINAL_LEVEL IN ('Ⅴ') THEN '高风险' \r\n" + 
					"                                                ELSE '极高风险' \r\n" + 
					"                                            END) DESCRIBE, /*债项等级描述*/ \r\n" + 
					"                        trim(to_char((SELECT ( (SELECT ro FROM \r\n" + 
					"                                                     (SELECT f.ro FROM \r\n" + 
					"                                                        (SELECT rownum - 1 ro, f.* FROM \r\n" + 
					"                                                           (SELECT * FROM (SELECT n.* FROM NS_FACILITY_RATING n WHERE fd_version = '3.0' AND fd_vaild = '1' \r\n" + 
					"                                                              /* 从 NS_FACILITY_RATING 表中读取评级状态 */ \r\n" + 
					"                                                              AND FD_RATING_STATUS = (select FD_RATING_STATUS from NS_FACILITY_RATING where fd_version = '3.0' and fd_id = ?) \r\n" + 
					"                                                              AND fd_final_sco > 0\r\n" + 
					"                                UNION\r\n" + 
					"                                SELECT * FROM NS_FACILITY_RATING WHERE fd_id = ?) \r\n" + 
					"                                                            ORDER BY fd_final_sco) f) f \r\n" + 
					"                                                      WHERE fd_id = ?)) / \r\n" + 
					"                                                  (SELECT nu FROM \r\n" + 
					"                                                     (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu \r\n" + 
					"                                                      FROM (SELECT count(*) nu FROM \r\n" + 
					"                                                           (SELECT rownum ro, f.* FROM \r\n" + 
					"                                                              (SELECT n.* \r\n" + 
					"                                                               FROM NS_FACILITY_RATING n \r\n" + 
					"                                                               WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_final_sco > 0 AND fd_vaild = '1' \r\n" + 
					"                                                               ORDER BY fd_final_sco) f))))) AS rankingOnGroup /*存量资产评级中的得分分位数*/ \r\n" + 
					"                                        FROM dual),'99990.99')) * 100 || '%' AS rankingOnGroup, \r\n" + 
					"                        /*在公司中的得分分位数*/ \r\n" + 
					"                        trim(to_char((SELECT ( (SELECT ro FROM \r\n" + 
					"                                                     (SELECT f.ro FROM \r\n" + 
					"                                                        (SELECT rownum - 1 ro, f.* FROM \r\n" + 
					"                                                           (SELECT * FROM (SELECT n.* FROM NS_FACILITY_RATING n \r\n" + 
					"                                                            WHERE fd_version = '3.0' AND fd_vaild = '1' \r\n" + 
					"                                                              /* 从 NS_FACILITY_RATING 表中读取评级状态 */ \r\n" + 
					"                                                              AND FD_RATING_STATUS = (select FD_RATING_STATUS from NS_FACILITY_RATING where fd_version = '3.0' and fd_id = ?) \r\n" + 
					"                                                              AND fd_final_sco > 0 AND FD_PROJECT_CODE IN \r\n" + 
					"                                                                (SELECT project_number FROM ns_prj_project \r\n" + 
					"                                                                 WHERE LEASE_ORGANIZATION = \r\n" + 
					"                                                                     /* 读取项目所属部门 */ \r\n" + 
					"                                                                     (SELECT LEASE_ORGANIZATION FROM Ns_Prj_Project \r\n" + 
					"                                                                      WHERE project_number = (SELECT FD_PROJECT_CODE FROM NS_FACILITY_RATING WHERE FD_ID = ?)))\r\n" + 
					"                                UNION\r\n" + 
					"                              SELECT * FROM NS_FACILITY_RATING WHERE fd_id = ?)\r\n" + 
					"                                                            ORDER BY fd_final_sco) f) f \r\n" + 
					"                                                      WHERE fd_id = ?)) / \r\n" + 
					"                                                  (SELECT nu FROM (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM \r\n" + 
					"                                                        (SELECT count(*) nu FROM \r\n" + 
					"                                                           (SELECT rownum ro, f.* FROM \r\n" + 
					"                                                              (SELECT n.* FROM NS_FACILITY_RATING n \r\n" + 
					"                                                               WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_final_sco > 0 AND fd_vaild = '1' \r\n" + 
					"                                                                 AND FD_PROJECT_CODE IN \r\n" + 
					"                                                                   (SELECT project_number FROM ns_prj_project \r\n" + 
					"                                                                    WHERE LEASE_ORGANIZATION = (SELECT LEASE_ORGANIZATION \r\n" + 
					"                                                                         FROM Ns_Prj_Project WHERE project_number = (SELECT FD_PROJECT_CODE FROM NS_FACILITY_RATING WHERE FD_ID = ?))) \r\n" + 
					"                                                               ORDER BY fd_final_sco) f))))) AS rankingOnClique /*在部门中所处的位置*/ \r\n" + 
					"                                        FROM dual),'99990.99')) * 100 || '%' AS rankingOnClique /*在部门中所处的位置*/ \r\n" + 
					"FROM NS_FACILITY_RATING nfr \r\n" + 
					"LEFT JOIN NS_CFG_MAIN_SCALE ncms ON nfr.fd_final_level = ncms.FD_SCALE_LEVEL \r\n" + 
					"AND ncms.fd_type = '030' \r\n" + 
					"LEFT JOIN NS_ASSETS_RATING nar ON nfr.ASSETS_RATING_ID = nar.fd_id \r\n" + 
					"WHERE nfr.FD_ID = ?";
			queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id);
		}
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			list.add(map);
			return list;
		}
		queryForList.forEach(action -> {
			String RANKINGONGROUP = (String) action.get("RANKINGONGROUP");
			String RANKINGONCLIQUE = (String) action.get("RANKINGONCLIQUE");
			String substring = RANKINGONGROUP.substring(0, RANKINGONGROUP.indexOf("%"));
			if(!substring.isEmpty()) {
				double d = Double.parseDouble(substring);
				if (d < 0) {
					action.put("RANKINGONGROUP", "0%");
				}
			}
			String sub = RANKINGONCLIQUE.substring(0, RANKINGONCLIQUE.indexOf("%"));
			if(!sub.isEmpty()) {
				double d = Double.parseDouble(substring);
				if (d < 0) {
					action.put("RANKINGONCLIQUE", "0%");
				}
			}
		});
		return queryForList;
	}

	// 获取债项的历史评级状态
	private Map<String, Object> getHistoryStatus(String id) {
		String sql = "SELECT fd_vaild,fd_rating_status FROM NS_FACILITY_RATING WHERE fd_id = ?  AND fd_version = '3.0'";
		List<Map<String,Object>> list = jdbc.queryForList(sql, id);
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			return map;
		}
		return null;
	}

	@Override
	public Map<String, Object> getAssetsLevel(String id) {
		// 原始 SQL
		// select TO_CHAR(FD_FINAL_DATE, 'YYYY-MM-DD') FD_FINAL_DATE,  TO_CHAR(FD_DATE, 'YYYY-MM-DD') FD_DATE,  FD_FINAL_LEVEL,  (case  when FD_FINAL_LEVEL in ('Ⅰ', 'Ⅱ') then  '较强'  when FD_FINAL_LEVEL in ('Ⅲ', 'Ⅳ') then  '一般'  else  '较低'  end) info  from NS_FACILITY_RATING  where FD_ID = 'fb3f662a-f9aa-4ea6-ae39-fd753a3b2d10'

		String sql = "select TO_CHAR(FD_FINAL_DATE, 'YYYY-MM-DD') FD_FINAL_DATE,\r\n" + 
				"TO_CHAR(FD_DATE, 'YYYY-MM-DD') FD_DATE,\r\n" + 
				"FD_FINAL_LEVEL,\r\n" + 
				"(case\r\n" + 
				"when FD_FINAL_LEVEL in ('Ⅰ', 'Ⅱ') then\r\n" + 
				"'较强'\r\n" + 
				"when FD_FINAL_LEVEL in ('Ⅲ', 'Ⅳ') then\r\n" + 
				"'一般'\r\n" + 
				"else\r\n" + 
				"'较低'\r\n" + 
				"end) info\r\n" + 
				"from NS_FACILITY_RATING\r\n" + 
				"where FD_ID = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return map;
		}
		return queryForList.get(0);
	}

	@Override
	public List<Map<String, Object>> getFacilityMainInfo(String id) {
		// /* 债项中的主体信息 */ 
		List<Map<String,Object>> main = getFacilityMainScore(id);
		String FD_VAILD = (String)main.get(0).get("FD_VAILD");

		List<Map<String, Object>> queryForList = null;
		if("1".equals(FD_VAILD)) {
			// 复评 SQL
			String sql = "/* 债项中的主体信息 */\r\n" + 
					"SELECT round(NMR.fd_sco, 2) fd_sco, /* 评级得分 */\r\n" + 
					"       fd_intern_name, /* 初评人 */\r\n" + 
					"       fd_intern_level, /*初评等级*/\r\n" + 
					"       fd_final_name, /* 复评人 */\r\n" + 
					"       fd_final_level, /*复评等级*/\r\n" + 
					"       CASE NMR.FD_TYPE\r\n" + 
					"           WHEN 'ZTTYMX_ZSX_1_FW' THEN '服务型'\r\n" + 
					"           WHEN 'ZTTYMX_ZSX_2_SC' THEN '生产型'\r\n" + 
					"           WHEN 'ZTTYMX_SZX_1_FW' THEN '服务型'\r\n" + 
					"           WHEN 'ZTTYMX_SZX_2_SC' THEN '生产型'\r\n" + 
					"           WHEN 'ZTTYMX_LRX_1_FW' THEN '服务型'\r\n" + 
					"           WHEN 'ZTTYMX_LRX_2_SC' THEN '生产型'\r\n" + 
					"       END FD_TYPE, /* 评级(图片,服务型生产型)类型 */\r\n" + 
					"  (SELECT value_name\r\n" + 
					"   FROM NS_ZGC_FATHER_LEVEL\r\n" + 
					"   WHERE value_code = NBM.FD_SEGMENT_INDUSTRY) FD_SEGMENT_INDUSTRY, /*国标行业,图片 M7590出*/\r\n" + 
					"           '收入-' || NMR.TRACK_TYPE TRACK_TYPE, /* 赛道类型(图片,收入-利润型) */\r\n" + 
					"           NCMS.FD_ADMISSION_SUGGEST, /* 等级描述 */\r\n" + 
					"           round(NRI.FD_configid / 10000) FD_END_VALUE, /* 收入规模 */\r\n" + 
					"           CASE NMR.TRACK_TYPE\r\n" + 
					"               WHEN '增速型' THEN trim(to_char( (SELECT ( (SELECT FD_SCORE\r\n" + 
					"                                                          FROM NS_RATING_INDEXES nri\r\n" + 
					"                                                          LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id\r\n" + 
					"                                                          WHERE nri.FD_INDEXCODE = 'F75' AND FD_VAILD = '1' AND nrs.FD_RATEID =\r\n" + 
					"                                                              (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?)) -\r\n" + 
					"                                                         (SELECT FD_SCORE FROM NS_RATING_INDEXES nri\r\n" + 
					"                                                          LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id\r\n" + 
					"                                                          WHERE nri.FD_INDEXCODE = 'F300' AND FD_VAILD = '1' AND nrs.FD_RATEID =\r\n" + 
					"                                                              (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING\r\n" + 
					"                                                               WHERE FD_ID = ?))) /\r\n" + 
					"                                                 (SELECT FD_SCORE FROM NS_RATING_INDEXES nri\r\n" + 
					"                                                  LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id\r\n" + 
					"                                                  WHERE nri.FD_INDEXCODE = 'F300' AND FD_VAILD = '1' AND nrs.FD_RATEID =\r\n" + 
					"                                                      (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?)) FD_SCORE\r\n" + 
					"                                               FROM dual), '9999999999990.99')) || '%'\r\n" + 
					"               WHEN '市值型' THEN trim(to_char(round(to_number(NBM.MARKET_SIZE) / 10000), '9999999999990')) || ' 万元'\r\n" + 
					"               WHEN '利润型' THEN trim(to_char(\r\n" + 
					"                                              (SELECT FD_SCORE / 10000\r\n" + 
					"                                               FROM NS_RATING_INDEXES nri\r\n" + 
					"                                               LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id\r\n" + 
					"                                               WHERE nri.FD_INDEXCODE = 'F94' AND FD_VAILD = '1' AND nrs.FD_RATEID =\r\n" + 
					"                                                   (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING\r\n" + 
					"                                                    WHERE FD_ID = ?)), '9999999999990')) || ' 万元'\r\n" + 
					"           END FD_SCORE, /*主营业务收入增长率*/\r\n" + 
					"           CASE NMR.TRACK_TYPE\r\n" + 
					"               WHEN '增速型' THEN '主营业务收入增长率'\r\n" + 
					"               WHEN '市值型' THEN '市值规模'\r\n" + 
					"               WHEN '利润型' THEN '利润规模'\r\n" + 
					"           END DESCRIBE, /* FD_SCORE 字段上面那个 case 的取值字段 */\r\n" + 
					"               trim(to_char(round( (SELECT ( (SELECT ro FROM\r\n" + 
					"                                                      (SELECT f.ro FROM\r\n" + 
					"                                                         (SELECT rownum - 1 ro, f.* FROM\r\n" + 
					"                                                            (SELECT n.*\r\n" + 
					"                                                             FROM ns_main_rating n\r\n" + 
					"                                                             WHERE fd_version = '3.0' AND fd_vaild = '1'\r\n" + 
					"                                                               AND FD_RATING_STATUS = (select FD_RATING_STATUS FROM ns_main_rating WHERE fd_id = (SELECT MAIN_RATING_ID/* 获取债项对应的主体的评级状态 */\r\n" + 
					"                                                                                                 FROM NS_FACILITY_RATING WHERE FD_ID = ?))\r\n" + 
					"                                                               AND fd_sco > 0\r\n" + 
					"                                                             ORDER BY fd_sco) f) f\r\n" + 
					"                                                       WHERE fd_id = (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?))) /\r\n" + 
					"                                                   (SELECT nu FROM\r\n" + 
					"                                                      (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
					"                                                         (SELECT count(*) - 1 nu FROM\r\n" + 
					"                                                            (SELECT rownum ro, f.* FROM\r\n" + 
					"                                                               (SELECT n.* FROM ns_main_rating n\r\n" + 
					"                                                                WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_sco > 0 AND fd_vaild = '1'\r\n" + 
					"                                                                ORDER BY fd_sco) f))))) AS ranking\r\n" + 
					"                                         FROM dual), 2),'99990.99')) * 100 || '%' AS ranking /*存量评级中的位置*/\r\n" + 
					"FROM NS_MAIN_RATING NMR\r\n" + 
					"LEFT JOIN NS_BP_MASTER NBM ON nbm.fd_bp_code = NMR.fd_cust_code\r\n" + 
					"LEFT JOIN NS_CFG_MAIN_SCALE NCMS ON NCMS.FD_SCALE_LEVEL = NMR.Fd_Intern_Level\r\n" + 
					"AND NCMS.FD_TYPE = CASE\r\n" + 
					"                       WHEN NMR.FD_TYPE = 'ZTTYMX_LRX_1_FW' OR NMR.FD_TYPE = 'ZTTYMX_LRX_2_SC' THEN '080'\r\n" + 
					"                       WHEN NMR.FD_TYPE = 'ZTTYMX_SZX_1_FW' OR NMR.FD_TYPE = 'ZTTYMX_SZX_2_SC' THEN '060'\r\n" + 
					"                       WHEN NMR.FD_TYPE = 'ZTTYMX_ZSX_1_FW' OR NMR.FD_TYPE = 'ZTTYMX_ZSX_2_SC' THEN '070'\r\n" + 
					"                   END\r\n" + 
					"LEFT JOIN ns_rating_step NRS ON NMR.FD_ID = NRS.fd_rateid\r\n" + 
					"LEFT JOIN ns_rating_indexes NRI ON NRS.FD_ID = NRI.fd_stepid\r\n" + 
					"WHERE nri.fd_indexcode = 'F75'\r\n" + 
					"  AND nrs.fd_vaild = '1'\r\n" + 
					"  AND NMR.fd_id = (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?)";
			queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id);
		} else {
			// 历史评级 SQL
			String sql = "/* 债项中的主体信息 */ \r\n" + 
					"SELECT round(NMR.fd_sco, 2) fd_sco, /* 评级得分 */ \r\n" + 
					"       fd_intern_name, /* 初评人 */ \r\n" + 
					"       fd_intern_level, /*初评等级*/ \r\n" + 
					"       fd_final_name, /* 复评人 */ \r\n" + 
					"       fd_final_level, /*复评等级*/ \r\n" + 
					"       CASE NMR.FD_TYPE \r\n" + 
					"           WHEN 'ZTTYMX_ZSX_1_FW' THEN '服务型' \r\n" + 
					"           WHEN 'ZTTYMX_ZSX_2_SC' THEN '生产型' \r\n" + 
					"           WHEN 'ZTTYMX_SZX_1_FW' THEN '服务型' \r\n" + 
					"           WHEN 'ZTTYMX_SZX_2_SC' THEN '生产型' \r\n" + 
					"           WHEN 'ZTTYMX_LRX_1_FW' THEN '服务型' \r\n" + 
					"           WHEN 'ZTTYMX_LRX_2_SC' THEN '生产型' \r\n" + 
					"       END FD_TYPE, /* 评级(图片,服务型生产型)类型 */ \r\n" + 
					"  (SELECT value_name \r\n" + 
					"   FROM NS_ZGC_FATHER_LEVEL \r\n" + 
					"   WHERE value_code = NBM.FD_SEGMENT_INDUSTRY) FD_SEGMENT_INDUSTRY, /*国标行业,图片 M7590出*/ \r\n" + 
					"           '收入-' || NMR.TRACK_TYPE TRACK_TYPE, /* 赛道类型(图片,收入-利润型) */ \r\n" + 
					"           NCMS.FD_ADMISSION_SUGGEST, /* 等级描述 */ \r\n" + 
					"           round(NRI.FD_configid / 10000) FD_END_VALUE, /* 收入规模 */ \r\n" + 
					"           CASE NMR.TRACK_TYPE \r\n" + 
					"               WHEN '增速型' THEN trim(to_char( (SELECT ( (SELECT FD_SCORE \r\n" + 
					"                                                          FROM NS_RATING_INDEXES nri \r\n" + 
					"                                                          LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id \r\n" + 
					"                                                          WHERE nri.FD_INDEXCODE = 'F75' AND FD_VAILD = '1' AND nrs.FD_RATEID = \r\n" + 
					"                                                              (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?)) - \r\n" + 
					"                                                         (SELECT FD_SCORE FROM NS_RATING_INDEXES nri \r\n" + 
					"                                                          LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id \r\n" + 
					"                                                          WHERE nri.FD_INDEXCODE = 'F300' AND FD_VAILD = '1' AND nrs.FD_RATEID = \r\n" + 
					"                                                              (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING \r\n" + 
					"                                                               WHERE FD_ID = ?))) / \r\n" + 
					"                                                 (SELECT FD_SCORE FROM NS_RATING_INDEXES nri \r\n" + 
					"                                                  LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id \r\n" + 
					"                                                  WHERE nri.FD_INDEXCODE = 'F300' AND FD_VAILD = '1' AND nrs.FD_RATEID = \r\n" + 
					"                                                      (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?)) FD_SCORE \r\n" + 
					"                                               FROM dual), '9999999999990.99')) || '%' \r\n" + 
					"               WHEN '市值型' THEN trim(to_char(round(to_number(NBM.MARKET_SIZE) / 10000), '9999999999990')) || ' 万元' \r\n" + 
					"               WHEN '利润型' THEN trim(to_char( \r\n" + 
					"                                              (SELECT FD_SCORE / 10000 \r\n" + 
					"                                               FROM NS_RATING_INDEXES nri \r\n" + 
					"                                               LEFT JOIN ns_rating_step nrs ON nri.fd_stepid = nrs.fd_id \r\n" + 
					"                                               WHERE nri.FD_INDEXCODE = 'F94' AND FD_VAILD = '1' AND nrs.FD_RATEID = \r\n" + 
					"                                                   (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING \r\n" + 
					"                                                    WHERE FD_ID = ?)), '9999999999990')) || ' 万元' \r\n" + 
					"           END FD_SCORE, /*主营业务收入增长率*/ \r\n" + 
					"           CASE NMR.TRACK_TYPE \r\n" + 
					"               WHEN '增速型' THEN '主营业务收入增长率' \r\n" + 
					"               WHEN '市值型' THEN '市值规模' \r\n" + 
					"               WHEN '利润型' THEN '利润规模' \r\n" + 
					"           END DESCRIBE, /* FD_SCORE 字段上面那个 case 的取值字段 */ \r\n" + 
					"               trim(to_char(round( (SELECT ( (SELECT ro FROM \r\n" + 
					"                                                      (SELECT f.ro FROM \r\n" + 
					"                                                         (SELECT rownum - 1 ro, f.* FROM \r\n" + 
					"                                                            (SELECT * FROM \r\n" + 
					"                                                            (SELECT n.* FROM ns_main_rating n \r\n" + 
					"                                                             WHERE fd_version = '3.0' AND fd_vaild = '1' \r\n" + 
					"                                                               AND FD_RATING_STATUS = (select FD_RATING_STATUS FROM ns_main_rating WHERE fd_id = (SELECT MAIN_RATING_ID/* 获取债项对应的主体的评级状态 */ \r\n" + 
					"                                                                                                 FROM NS_FACILITY_RATING WHERE FD_ID = ?)) \r\n" + 
					"                                                               AND fd_sco > 0\r\n" + 
					"                                                               UNION \r\n" + 
					"                                                               SELECT * FROM ns_main_rating WHERE fd_id = (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?))\r\n" + 
					"                                                             ORDER BY fd_sco) f) f \r\n" + 
					"                                                       WHERE fd_id = (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?))) / \r\n" + 
					"                                                   (SELECT nu FROM \r\n" + 
					"                                                      (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM \r\n" + 
					"                                                         (SELECT count(*) nu FROM \r\n" + 
					"                                                            (SELECT rownum ro, f.* FROM \r\n" + 
					"                                                               (SELECT n.* FROM ns_main_rating n \r\n" + 
					"                                                                WHERE fd_version = '3.0' AND FD_RATING_STATUS = '020' AND fd_sco > 0 AND fd_vaild = '1' \r\n" + 
					"                                                                ORDER BY fd_sco) f))))) AS ranking \r\n" + 
					"                                         FROM dual), 2),'99990.99')) * 100 || '%' AS ranking /*存量评级中的位置*/ \r\n" + 
					"FROM NS_MAIN_RATING NMR \r\n" + 
					"LEFT JOIN NS_BP_MASTER NBM ON nbm.fd_bp_code = NMR.fd_cust_code \r\n" + 
					"LEFT JOIN NS_CFG_MAIN_SCALE NCMS ON NCMS.FD_SCALE_LEVEL = NMR.Fd_Intern_Level \r\n" + 
					"AND NCMS.FD_TYPE = CASE \r\n" + 
					"                       WHEN NMR.FD_TYPE = 'ZTTYMX_LRX_1_FW' OR NMR.FD_TYPE = 'ZTTYMX_LRX_2_SC' THEN '080' \r\n" + 
					"                       WHEN NMR.FD_TYPE = 'ZTTYMX_SZX_1_FW' OR NMR.FD_TYPE = 'ZTTYMX_SZX_2_SC' THEN '060' \r\n" + 
					"                       WHEN NMR.FD_TYPE = 'ZTTYMX_ZSX_1_FW' OR NMR.FD_TYPE = 'ZTTYMX_ZSX_2_SC' THEN '070' \r\n" + 
					"                   END \r\n" + 
					"LEFT JOIN ns_rating_step NRS ON NMR.FD_ID = NRS.fd_rateid \r\n" + 
					"LEFT JOIN ns_rating_indexes NRI ON NRS.FD_ID = NRI.fd_stepid \r\n" + 
					"WHERE nri.fd_indexcode = 'F75' \r\n" + 
					"  AND nrs.fd_vaild = '1' \r\n" + 
					"  AND NMR.fd_id = (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?)";
			queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id);
		}
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		queryForList.forEach(action -> {
			String object = (String) action.get("RANKING");
			String substring = object.substring(0, object.indexOf("%"));
			if(!substring.isEmpty()) {
				double d = Double.parseDouble(substring);
				if (d < 0) {
					action.put("RANKING", "0%");
				}
			}
		});
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFacilityAssetsInfo(String id) {
		// /* 债项页面资产描述 */ 
		List<Map<String,Object>> main = getFacilityMainScore(id);
		String FD_VAILD = (String)main.get(0).get("FD_VAILD");

		List<Map<String, Object>> queryForList = null;
		if("1".equals(FD_VAILD)) {
			// 复评 SQL
			String sql = "select round(NAR.fd_final_sco, 2) fd_final_sco, /* 评级得分 */\r\n" + 
					"       NAR.fd_intern_name, /*  初评人 */\r\n" + 
					"       NAR.FD_ASSETS_NAME  as fd_final_name, /*  复评人 */\r\n" + 
					"       case NAR.PRODUCT_TYPE\r\n" + 
					"         when 'ZC_CSZL_3' then '厂商租赁'\r\n" + 
					"         when 'ZC_FWZL_5' then '服务租赁'\r\n" + 
					"         when 'ZC_XMZL_2' then '项目租赁'\r\n" + 
					"         when 'ZC_ZZHZ_1' then '一般产品'\r\n" + 
					"         when 'ZC_ZLTZWT_4' then '租赁+投资+委托运营'\r\n" + 
					"       end PRODUCT_TYPE, /*  产品类型 */\r\n" + 
					"       NAR.LEASE_ITEM_SHORT_NAME, /*  核心租赁物 */\r\n" + 
					"       round((NAR.NET_VALUE / 10000)) NET_VALUE, /* 租赁物净值 */\r\n" + 
					"       round((NAR.ASSESSED_VALUE / 10000)) ASSESSED_VALUE, /* 评估价值 */\r\n" + 
					"       trim(to_char(round((NAR.CORELEASE_PROPORTION * 100), 2), '99990.99')) || '%' CORELEASE_PROPORTION, /* 核心租赁物占比 */\r\n" + 
					"       NCMS.FD_ADMISSION_SUGGEST, /*  等级描述 */\r\n" + 
					"       NAR.fd_intern_level, /* 初评等级 */\r\n" + 
					"       NAR.fd_final_level, /* 终评等级 */\r\n" + 
					"       '资产信用保障能力' || (case\r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅰ') > 0 then '强'\r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅱ') > 0 then '较强'\r\n" + 
					"         else '一般'\r\n" + 
					"       end) warning,\r\n" + 
					"       '资产信用保障能力' || (case\r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅰ') > 0 then '强'\r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅱ') > 0 then '较强'\r\n" + 
					"         else '一般'\r\n" + 
					"       end) FD_ADMISSION_SUGGEST, /* 等级描述 */\r\n" + 
					"       trim(to_char(round((select ((select ro\r\n" + 
					"                    from (select f.ro from (select rownum - 1 ro, f.*\r\n" + 
					"                                    from (select n.*  from NS_ASSETS_RATING n\r\n" + 
					"                                           where fd_version = '3.0' AND fd_vaild = '1'\r\n" + 
					"                                             and FD_RATING_STATUS = (select FD_RATING_STATUS from NS_ASSETS_RATING where fd_id =\r\n" + 
					"													(select ASSETS_RATING_ID from NS_FACILITY_RATING where FD_ID = ?))/* 债项对应的资产评级状态 */\r\n" + 
					"                                             and fd_final_sco > 0\r\n" + 
					"                                           order by fd_final_sco) f) f\r\n" + 
					"                           where fd_id = (select ASSETS_RATING_ID from NS_FACILITY_RATING\r\n" + 
					"                                           where FD_ID = ?))) /\r\n" + 
					"                (select nu from (select case nu\r\n" + 
					"                                   when 0 then 1\r\n" + 
					"                                   else nu\r\n" + 
					"                                 end nu\r\n" + 
					"                            from (select count(*) - 1 nu\r\n" + 
					"                                    from (select rownum ro, f.*\r\n" + 
					"                                            from (select n.* from NS_ASSETS_RATING n\r\n" + 
					"                                                   where fd_version = '3.0' and FD_RATING_STATUS = '020' and fd_final_sco > 0 AND fd_vaild = '1'\r\n" + 
					"                                                   order by fd_final_sco) f))))) as ranking\r\n" + 
					"           from dual), 2), '99990.99')) * 100 || '%' as ranking /*存量资产评级中的得分分位数*/\r\n" + 
					"  from NS_ASSETS_RATING NAR\r\n" + 
					"  left join NS_CFG_MAIN_SCALE ncms\r\n" + 
					"    on NAR.fd_intern_level = ncms.FD_SCALE_LEVEL\r\n" + 
					"   and ncms.FD_TYPE = '090'\r\n" + 
					" where NAR.fd_id = (select ASSETS_RATING_ID from NS_FACILITY_RATING where FD_ID = ?)";
			queryForList = jdbc.queryForList(sql, id, id, id);
		} else {
			// 历史评级 SQL
			String sql = "select round(NAR.fd_final_sco, 2) fd_final_sco, /* 评级得分 */ \r\n" + 
					"       NAR.fd_intern_name, /*  初评人 */ \r\n" + 
					"       NAR.FD_ASSETS_NAME  as fd_final_name, /*  复评人 */ \r\n" + 
					"       case NAR.PRODUCT_TYPE \r\n" + 
					"         when 'ZC_CSZL_3' then '厂商租赁' \r\n" + 
					"         when 'ZC_FWZL_5' then '服务租赁' \r\n" + 
					"         when 'ZC_XMZL_2' then '项目租赁' \r\n" + 
					"         when 'ZC_ZZHZ_1' then '一般产品' \r\n" + 
					"         when 'ZC_ZLTZWT_4' then '租赁+投资+委托运营' \r\n" + 
					"       end PRODUCT_TYPE, /*  产品类型 */ \r\n" + 
					"       NAR.LEASE_ITEM_SHORT_NAME, /*  核心租赁物 */ \r\n" + 
					"       round((NAR.NET_VALUE / 10000)) NET_VALUE, /* 租赁物净值 */ \r\n" + 
					"       round((NAR.ASSESSED_VALUE / 10000)) ASSESSED_VALUE, /* 评估价值 */ \r\n" + 
					"       trim(to_char(round((NAR.CORELEASE_PROPORTION * 100), 2), '99990.99')) || '%' CORELEASE_PROPORTION, /* 核心租赁物占比 */ \r\n" + 
					"       NCMS.FD_ADMISSION_SUGGEST, /*  等级描述 */ \r\n" + 
					"       NAR.fd_intern_level, /* 初评等级 */ \r\n" + 
					"       NAR.fd_final_level, /* 终评等级 */ \r\n" + 
					"       '资产信用保障能力' || (case \r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅰ') > 0 then '强' \r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅱ') > 0 then '较强' \r\n" + 
					"         else '一般' \r\n" + 
					"       end) warning, \r\n" + 
					"       '资产信用保障能力' || (case \r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅰ') > 0 then '强' \r\n" + 
					"         when INSTR(NAR.FD_FINAL_LEVEL, 'Ⅱ') > 0 then '较强' \r\n" + 
					"         else '一般' \r\n" + 
					"       end) FD_ADMISSION_SUGGEST, /* 等级描述 */ \r\n" + 
					"       trim(to_char(round((select ((select ro \r\n" + 
					"                    from (select f.ro from (select rownum - 1 ro, f.* \r\n" + 
					"                                    from (select * from (select n.*  from NS_ASSETS_RATING n \r\n" + 
					"                                           where fd_version = '3.0' AND fd_vaild = '1' \r\n" + 
					"                                             and FD_RATING_STATUS = (select FD_RATING_STATUS from NS_ASSETS_RATING where fd_id = \r\n" + 
					"                                                                    (select ASSETS_RATING_ID from NS_FACILITY_RATING where FD_ID = ?))/* 债项对应的资产评级状态 */ \r\n" + 
					"                                             and fd_final_sco > 0\r\n" + 
					"                                             union\r\n" + 
					"                                             select * from NS_ASSETS_RATING where fd_id = (select ASSETS_RATING_ID from NS_FACILITY_RATING where FD_ID = ?))\r\n" + 
					"                                           order by fd_final_sco) f) f \r\n" + 
					"                           where fd_id = (select ASSETS_RATING_ID from NS_FACILITY_RATING \r\n" + 
					"                                           where FD_ID = ?))) / \r\n" + 
					"                (select nu from (select case nu \r\n" + 
					"                                   when 0 then 1 \r\n" + 
					"                                   else nu \r\n" + 
					"                                 end nu \r\n" + 
					"                            from (select count(*) nu \r\n" + 
					"                                    from (select rownum ro, f.* \r\n" + 
					"                                            from (select n.* from NS_ASSETS_RATING n \r\n" + 
					"                                                   where fd_version = '3.0' and FD_RATING_STATUS = '020' and fd_final_sco > 0 AND fd_vaild = '1' \r\n" + 
					"                                                   order by fd_final_sco) f))))) as ranking \r\n" + 
					"           from dual), 2), '99990.99')) * 100 || '%' as ranking /*存量资产评级中的得分分位数*/ \r\n" + 
					"  from NS_ASSETS_RATING NAR \r\n" + 
					"  left join NS_CFG_MAIN_SCALE ncms \r\n" + 
					"    on NAR.fd_intern_level = ncms.FD_SCALE_LEVEL \r\n" + 
					"   and ncms.FD_TYPE = '090' \r\n" + 
					" where NAR.fd_id = (select ASSETS_RATING_ID from NS_FACILITY_RATING where FD_ID = ?)";
			queryForList = jdbc.queryForList(sql, id, id, id, id);
		}
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		queryForList.forEach(action -> {
			String object = (String) action.get("RANKING");
			String substring = object.substring(0, object.indexOf("%"));
			if(!substring.isEmpty()) {
				double d = Double.parseDouble(substring);
				if (d < 0) {
					action.put("RANKING", "0%");
				}
			}
		});
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFacilityCredit(String id) {
		// 原始 SQL
		// select ncr.FD_INTERN_NAME, /*初评人*/  ncr.FD_INTERN_LEVEL, /* 初评等级 */  ncr.FD_FINAL_NAME, /*复评人*/  ncr.FD_FINAL_LEVEL, /*终评级等级*/  ncr.CREDIT_TYPE /*基本信息(增信措施)*/  from NS_CREDIT_RATING ncr  where ncr.FD_ID = (select nfr.CREDIT_RATING_ID  from NS_FACILITY_RATING nfr  where nfr.FD_ID = ?)

		String sql = "select ncr.FD_INTERN_NAME, /*初评人*/\r\n" + 
				"       ncr.FD_INTERN_LEVEL, /* 初评等级 */\r\n" + 
				"       ncr.FD_FINAL_NAME, /*复评人*/\r\n" + 
				"       ncr.FD_FINAL_LEVEL, /*终评级等级*/\r\n" + 
				"       ncr.CREDIT_TYPE /*基本信息(增信措施)*/\r\n" + 
				"  from NS_CREDIT_RATING ncr\r\n" + 
				" where ncr.FD_ID = (select nfr.CREDIT_RATING_ID\r\n" + 
				"                      from NS_FACILITY_RATING nfr\r\n" + 
				"                     where nfr.FD_ID = ?)";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFacilityWarning(String id) {
		// 原始 SQL
		// /*主体重大潜在风险提示*/  select '主体重大潜在风险提示' name,  mr.RISK_POINT SELECT_EXPLAIN, /*选择说明*/  mr.TIP, /*重大潜在风险提示*/  (select trim(to_char(count(*),'9999'))  from ns_major_record nmr  left join NS_MAJOR_RISKWARNINGDATAS mr  on nmr.TIP_NUMBER = mr.TIP_NUMBER  where ISSELECTED = 'Y'  and RATE_TYPE = '020'  and nmr.SERIAL_NUMBER =  (select nfr.MAIN_RATING_ID  from NS_FACILITY_RATING nfr  where nfr.FD_ID = '34eebad6-9f88-49d6-8d8f-902bd1677638')) "count" /* 主体重大风险提示个数 */  from ns_major_record nmr  left join NS_MAJOR_RISKWARNINGDATAS mr  on nmr.TIP_NUMBER = mr.TIP_NUMBER  where ISSELECTED = 'Y'  and RATE_TYPE = '020'  and nmr.SERIAL_NUMBER =  (select nfr.MAIN_RATING_ID  from NS_FACILITY_RATING nfr  where nfr.FD_ID = '34eebad6-9f88-49d6-8d8f-902bd1677638')  union /*资产重大风险提示*/  select '资产重大风险提示' name, nar.MAJOR_RISK_WARNING, '' 占位, '' 占位  from NS_ASSETS_RATING nar  where nar.fd_id =  (select ASSETS_RATING_ID  from NS_FACILITY_RATING  where fd_id = '34eebad6-9f88-49d6-8d8f-902bd1677638')

		String sql = "/*主体重大潜在风险提示*/\r\n" + 
				"select '主体重大潜在风险提示' name,\r\n" + 
				"       mr.RISK_POINT SELECT_EXPLAIN, /*选择说明*/\r\n" + 
				"       mr.TIP, /*重大潜在风险提示*/\r\n" + 
				"       (select trim(to_char(count(*),'9999'))\r\n" + 
				"          from ns_major_record nmr\r\n" + 
				"          left join NS_MAJOR_RISKWARNINGDATAS mr\r\n" + 
				"            on nmr.TIP_NUMBER = mr.TIP_NUMBER\r\n" + 
				"         where ISSELECTED = 'Y'\r\n" + 
				"           and RATE_TYPE = '020'\r\n" + 
				"           and nmr.SERIAL_NUMBER =\r\n" + 
				"               (select nfr.MAIN_RATING_ID\r\n" + 
				"                  from NS_FACILITY_RATING nfr\r\n" + 
				"                 where nfr.FD_ID = ?)) \"count\" /* 主体重大风险提示个数 */\r\n" + 
				"  from ns_major_record nmr\r\n" + 
				"  left join NS_MAJOR_RISKWARNINGDATAS mr\r\n" + 
				"    on nmr.TIP_NUMBER = mr.TIP_NUMBER\r\n" + 
				" where ISSELECTED = 'Y'\r\n" + 
				"   and RATE_TYPE = '020'\r\n" + 
				"   and nmr.SERIAL_NUMBER =\r\n" + 
				"       (select nfr.MAIN_RATING_ID\r\n" + 
				"          from NS_FACILITY_RATING nfr\r\n" + 
				"         where nfr.FD_ID = ?)\r\n" + 
				"union /*资产重大风险提示*/\r\n" + 
				"select '资产重大风险提示' name, nar.MAJOR_RISK_WARNING, '' 占位, '' 占位\r\n" + 
				"  from NS_ASSETS_RATING nar\r\n" + 
				" where nar.fd_id =\r\n" + 
				"       (select ASSETS_RATING_ID\r\n" + 
				"          from NS_FACILITY_RATING\r\n" + 
				"         where fd_id = ?)";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id, id, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	@Override
	public Map<String, Object> getFacilityHeader(String id) {
		// 原始 SQL
		// select FD_CUST_NAME,FD_PROJECT_CODE,FD_PROJECT_NAME from NS_FACILITY_RATING where FD_ID = 'fe72c6dc-d23d-4592-8aad-fe4cde9a8f1f'

		String sql = "select FD_CUST_NAME,FD_PROJECT_CODE,FD_PROJECT_NAME from NS_FACILITY_RATING where FD_ID = ?";
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
	public List<Map<String, Object>> getFacilityCompare(String id) {
		// 原始 SQL
		// select case  when (to_number(nfi.fd_indexvalue) > to_number(al.fd_indexvalue)) then '1'  when (to_number(nfi.fd_indexvalue) < to_number(al.fd_indexvalue)) then '2'  when (to_number(nfi.fd_indexvalue) = to_number(al.fd_indexvalue)) then '0'  else null  end  "value"   from NS_FACILITY_INDEXES nfi  left join NS_FACILITY_RATING nfr  on nfi.fd_ratingid = nfr.fd_id  left join (select nfi.fd_ratingid,nfi.fd_indexvalue  from NS_FACILITY_INDEXES nfi  left join NS_FACILITY_RATING nfr  on nfi.fd_ratingid = nfr.fd_id  where (nfi.fd_rating_status = '020' or nfi.fd_rating_status = '000')  and fd_indexcategory = '资产保障倍数'  and nfr.fd_id = '34eebad6-9f88-49d6-8d8f-902bd1677638') al  on al.fd_ratingid = nfi.fd_ratingid  where (nfi.fd_rating_status = '020' or nfi.fd_rating_status = '000')  and fd_indexcategory like '%主体%'  and nfr.fd_id = '34eebad6-9f88-49d6-8d8f-902bd1677638'

		String sql = "select case\r\n" + 
				"         when (to_number(nfi.fd_indexvalue) > to_number(al.fd_indexvalue)) then '1'\r\n" + 
				"         when (to_number(nfi.fd_indexvalue) < to_number(al.fd_indexvalue)) then '2'\r\n" + 
				"         when (to_number(nfi.fd_indexvalue) = to_number(al.fd_indexvalue)) then '0'\r\n" + 
				"         else null\r\n" + 
				"       end  \"value\" \r\n" + 
				"  from NS_FACILITY_INDEXES nfi\r\n" + 
				"  left join NS_FACILITY_RATING nfr\r\n" + 
				"    on nfi.fd_ratingid = nfr.fd_id\r\n" + 
				"  left join (select nfi.fd_ratingid,nfi.fd_indexvalue\r\n" + 
				"               from NS_FACILITY_INDEXES nfi\r\n" + 
				"               left join NS_FACILITY_RATING nfr\r\n" + 
				"                 on nfi.fd_ratingid = nfr.fd_id\r\n" + 
				"              where (nfi.fd_rating_status = '020' or nfi.fd_rating_status = '000')\r\n" + 
				"                and fd_indexcategory = '资产保障倍数'\r\n" + 
				"                and nfr.fd_id = ?) al\r\n" + 
				"                on al.fd_ratingid = nfi.fd_ratingid\r\n" + 
				" where (nfi.fd_rating_status = '020' or nfi.fd_rating_status = '000')\r\n" + 
				"   and fd_indexcategory like '%主体%'\r\n" + 
				"   and nfr.fd_id = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFacilityTrait(String id) {
		// 

		Map<String, Object> status = getFacilityStatus(id);
		String r = (String)status.get("FD_VAILD");
		List<Map<String, Object>> queryForList = null;
		if("1".equals(r)) {
			// 复评 SQL
			String  sql = "select DECODE(n.fd_intern_level, n.fd_final_level, null, 'Y') \"result\", '0' type /*初复评有差异*/ \r\n" + 
					"  from ns_Facility_rating n \r\n" + 
					" WHERE n.FD_ID = ? \r\n" + 
					"union \r\n" + 
					"SELECT CASE \r\n" + 
					"         WHEN count(ns.RISK_POINT) > 3 THEN 'Y' \r\n" + 
					"       END AS \"result\", '1' TYPE /* 主体重大风险提示超过 3 个*/ \r\n" + 
					"  FROM NS_MAIN_RATING nmr \r\n" + 
					"  LEFT JOIN ns_major_record ma ON nmr.fd_id = ma.SERIAL_NUMBER \r\n" + 
					"  LEFT JOIN NS_MAJOR_RISKWARNINGDATAS ns ON ma.tip_number = ns.TIP_NUMBER \r\n" + 
					" WHERE ma.rate_type = '020' AND ma.isselected = 'Y' \r\n" + 
					"   AND nmr.fd_id = (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE fd_id = ?) \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*), 1, 'Y', null) \"result\", '2' type /*存量排名前十*/ \r\n" + 
					"  from (select rownum ro, t.* \r\n" + 
					"          from ( /*存量排序*/ \r\n" + 
					"                select n.* \r\n" + 
					"                  from NS_FACILITY_RATING n \r\n" + 
					"                 where n.fd_version = '3.0' and n.fd_rating_status = '020' and n.fd_vaild = '1'\r\n" + 
					"                 order by n.fd_final_sco desc) t) ot \r\n" + 
					" where ot.ro <= 10 \r\n" + 
					"   and ot.fd_id = ? \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'3' type /* 部门排名前三*/ \r\n" + 
					"  from (select rownum,t.* from(SELECT nmr.* \r\n" + 
					"  FROM NS_FACILITY_RATING nmr \r\n" + 
					"  LEFT JOIN ns_bp_master nbm/* 获取当前客户的客户经理 */ \r\n" + 
					"    ON nmr.FD_CUST_CODE = nbm.fd_bp_code \r\n" + 
					"  left join ns_bp_master nbm2 \r\n" + 
					"    on nbm2.FD_LEASE_ORGANIZATION = nbm.FD_LEASE_ORGANIZATION \r\n" + 
					"  LEFT JOIN NS_FACILITY_RATING nmr2 \r\n" + 
					"    ON nmr2.FD_CUST_CODE = nbm2.FD_BP_CODE \r\n" + 
					" WHERE nmr2.fd_id = ? \r\n" + 
					"   AND nmr.fd_rating_status = '020' AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1'\r\n" + 
					"   order by nmr.fd_final_sco desc) t where rownum <= 3) ot where fd_id = ? \r\n" + 
					"union \r\n" + 
					"SELECT CASE \r\n" + 
					"           WHEN count(n.major_risk_warning)>0 THEN 'Y' \r\n" + 
					"           ELSE '' \r\n" + 
					"       END \"result\", '4' TYPE /* 存在重大风险的 */ \r\n" + 
					"FROM ns_assets_rating n \r\n" + 
					"WHERE n.fd_id = (SELECT ASSETS_RATING_ID FROM NS_FACILITY_RATING WHERE fd_id = ?)";
			queryForList = jdbc.queryForList(sql, id, id, id, id, id, id);
		} else {
			// 历史评级 SQL
			String  sql = "select DECODE(n.fd_intern_level, n.fd_final_level, null, 'Y') \"result\", '0' type /*初复评有差异*/  \r\n" + 
					"  from ns_Facility_rating n  \r\n" + 
					" WHERE n.FD_ID = ?  \r\n" + 
					"union  \r\n" + 
					"SELECT CASE  \r\n" + 
					"         WHEN count(ns.RISK_POINT) > 3 THEN 'Y'  \r\n" + 
					"       END AS \"result\", '1' TYPE /* 主体重大风险提示超过 3 个*/  \r\n" + 
					"  FROM NS_MAIN_RATING nmr  \r\n" + 
					"  LEFT JOIN ns_major_record ma ON nmr.fd_id = ma.SERIAL_NUMBER  \r\n" + 
					"  LEFT JOIN NS_MAJOR_RISKWARNINGDATAS ns ON ma.tip_number = ns.TIP_NUMBER  \r\n" + 
					" WHERE ma.rate_type = '020' AND ma.isselected = 'Y'  \r\n" + 
					"   AND nmr.fd_id = (SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE fd_id = ?)  \r\n" + 
					"union  \r\n" + 
					"select DECODE(count(*), 1, 'Y', null) \"result\", '2' type /*存量排名前十*/  \r\n" + 
					"  from (select rownum ro, t.* from ( /*存量排序*/  \r\n" + 
					"                select * from (select n.*  \r\n" + 
					"                  from NS_FACILITY_RATING n  \r\n" + 
					"                 where n.fd_version = '3.0' and n.fd_rating_status = '020' and n.fd_vaild = '1'\r\n" + 
					"                 union\r\n" + 
					"                 select * from NS_FACILITY_RATING where fd_id = ?)\r\n" + 
					"                 order by fd_final_sco desc) t) ot  \r\n" + 
					" where ot.ro <= 10  \r\n" + 
					"   and ot.fd_id = ?  \r\n" + 
					"union  \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'3' type /* 部门排名前三*/  \r\n" + 
					"  from (select rownum,t.* from(select * from \r\n" + 
					"  (SELECT nmr.*  \r\n" + 
					"  FROM NS_FACILITY_RATING nmr  \r\n" + 
					"  LEFT JOIN ns_bp_master nbm/* 获取当前客户的客户经理 */  \r\n" + 
					"    ON nmr.FD_CUST_CODE = nbm.fd_bp_code  \r\n" + 
					"  left join ns_bp_master nbm2  \r\n" + 
					"    on nbm2.FD_LEASE_ORGANIZATION = nbm.FD_LEASE_ORGANIZATION  \r\n" + 
					"  LEFT JOIN NS_FACILITY_RATING nmr2  \r\n" + 
					"    ON nmr2.FD_CUST_CODE = nbm2.FD_BP_CODE  \r\n" + 
					" WHERE nmr2.fd_id = ?  \r\n" + 
					"   AND nmr.fd_rating_status = '020' AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1'\r\n" + 
					"   union\r\n" + 
					"   select * from NS_FACILITY_RATING where fd_id = ?)\r\n" + 
					"   order by fd_final_sco desc) t where rownum <= 3) ot where fd_id = ?  \r\n" + 
					"union  \r\n" + 
					"SELECT CASE  \r\n" + 
					"           WHEN count(n.major_risk_warning)>0 THEN 'Y'  \r\n" + 
					"           ELSE ''  \r\n" + 
					"       END \"result\", '4' TYPE /* 存在重大风险的 */  \r\n" + 
					"FROM ns_assets_rating n  \r\n" + 
					"WHERE n.fd_id = (SELECT ASSETS_RATING_ID FROM NS_FACILITY_RATING WHERE fd_id = ?)";
			queryForList = jdbc.queryForList(sql, id, id, id, id, id, id, id, id);
		}
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	private Map<String, Object> getFacilityStatus(String id) {
		String sql = "select fd_vaild from NS_FACILITY_RATING where fd_id = ?  AND fd_version = '3.0'";
		List<Map<String,Object>> list = jdbc.queryForList(sql, id);
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			return map;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getFacilityToAssets(String id) {
		String  sql = "SELECT ASSETS_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFacilityToMain(String id) {
		String  sql = "SELECT MAIN_RATING_ID FROM NS_FACILITY_RATING WHERE FD_ID = ?";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFacilityAssetsScore(String id) {
		String  sql = "select n.fd_final_sco,FD_VAILD,fd_id as custNo from NS_ASSETS_RATING n where n.fd_id = (select ASSETS_RATING_ID from Ns_Facility_Rating where fd_id = ?)";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFacilityMainScore(String id) {
		String  sql = "select FD_SCO,FD_VAILD,fd_id as custno from NS_MAIN_RATING where fd_id = (select MAIN_RATING_ID from Ns_Facility_Rating where fd_id = ?)";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql, id);
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
			return list;
		}
		return queryForList;
	}
	

}
