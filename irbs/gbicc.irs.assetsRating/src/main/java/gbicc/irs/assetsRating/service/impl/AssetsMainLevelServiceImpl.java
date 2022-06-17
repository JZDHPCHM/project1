package gbicc.irs.assetsRating.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import gbicc.irs.assetsRating.service.AssetsMainLevelService;

@Service
public class AssetsMainLevelServiceImpl implements AssetsMainLevelService{

	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public Map<String, Object> getAssetsRatingInfo(String id) {

		String sql = "SELECT round(NAR.fd_final_sco, 2) fd_final_sco, /* 评级得分 */ \r\n" + 
				"       NAR.fd_intern_name, /*  初评人 */ \r\n" + 
				"       NAR.FD_ASSETS_NAME as fd_final_name, /*  复评人 */ \r\n" + 
				"       CASE NAR.PRODUCT_TYPE \r\n" + 
				"         WHEN 'ZC_CSZL_3' THEN '厂商租赁' \r\n" + 
				"         WHEN 'ZC_FWZL_5' THEN '服务租赁' \r\n" + 
				"         WHEN 'ZC_XMZL_2' THEN '项目租赁' \r\n" + 
				"         WHEN 'ZC_ZZHZ_1' THEN '一般产品' \r\n" + 
				"         WHEN 'ZC_ZLTZWT_4' THEN '租赁+投资+委托运营' \r\n" + 
				"       END PRODUCT_TYPE, /*  产品类型 */ \r\n" + 
				"       NAR.LEASE_ITEM_SHORT_NAME, /*  核心租赁物 */ \r\n" + 
				"       round((NAR.NET_VALUE / 10000)) NET_VALUE, /* 租赁物净值 */ \r\n" + 
				"       round((NAR.ASSESSED_VALUE / 10000)) ASSESSED_VALUE, /* 评估价值 */ \r\n" + 
				"       trim(to_char(round((NAR.CORELEASE_PROPORTION * 100), 2), '99990.99')) || '%' CORELEASE_PROPORTION, /* 核心租赁物占比 */ \r\n" + 
				"       '资产信用保障能力' || (CASE \r\n" + 
				"         WHEN INSTR(NAR.FD_FINAL_LEVEL, 'Ⅰ') > 0 THEN '强' \r\n" + 
				"         WHEN INSTR(NAR.FD_FINAL_LEVEL, 'Ⅱ') > 0 THEN '较强' \r\n" + 
				"         ELSE '一般' \r\n" + 
				"       END) FD_ADMISSION_SUGGEST, /*  等级描述 */ \r\n" + 
				"       NAR.fd_intern_level, /* 初评等级 */ \r\n" + 
				"       NAR.fd_final_level, /* 终评等级 */ \r\n" + 
				"       '资产信用保障能力' || (CASE \r\n" + 
				"         WHEN INSTR(NAR.FD_FINAL_LEVEL, 'Ⅰ') > 0 THEN '强' \r\n" + 
				"         WHEN INSTR(NAR.FD_FINAL_LEVEL, 'Ⅱ') > 0 THEN '较强' \r\n" + 
				"         ELSE '一般' \r\n" + 
				"       END) warning \r\n" + 
				"  FROM NS_ASSETS_RATING NAR \r\n" + 
				"  LEFT JOIN NS_CFG_MAIN_SCALE ncms \r\n" + 
				"    ON NAR.fd_FINAL_level = ncms.FD_SCALE_LEVEL \r\n" + 
				"   AND ncms.FD_TYPE = '090' \r\n" + 
				" WHERE NAR.fd_id = ?";
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
	public List<Map<String, Object>> getGroup(String id) {

		String sql = "/* 跟公司所有存量资产对比 */ \r\n" + 
				"select '创收性' name,to_char(round((FD_SCORE / \r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE \r\n" + 
				"           from ns_assets_indexes nri \r\n" + 
				"          where fd_stepid in \r\n" + 
				"                (select n.FD_ID \r\n" + 
				"                   from ns_assets_step n \r\n" + 
				"                  where n.fd_rateid = ? \r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) ) \r\n" + 
				"            and FD_INDEXCATEGORY = '创收性' \r\n" + 
				"            and FD_INDEXNAME != '创收性')),2),'990.00') FD_SCORE,'0' type \r\n" + 
				"  from ns_assets_indexes nri \r\n" + 
				" where fd_stepid in \r\n" + 
				"       (select n.FD_ID \r\n" + 
				"          from ns_assets_step n \r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) \r\n" + 
				"           and n.fd_rateid = ?) \r\n" + 
				"   and FD_INDEXCATEGORY = '创收性' \r\n" + 
				"   and FD_INDEXNAME = '创收性' \r\n" + 
				"union \r\n" + 
				"select '保值性' name,to_char(round((FD_SCORE / \r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE \r\n" + 
				"           from ns_assets_indexes nri \r\n" + 
				"          where fd_stepid in \r\n" + 
				"                (select n.FD_ID \r\n" + 
				"                   from ns_assets_step n \r\n" + 
				"                  where n.fd_rateid = ? \r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) ) \r\n" + 
				"            and FD_INDEXCATEGORY = '保值性' \r\n" + 
				"            and FD_INDEXNAME != '保值性')),2),'990.00') FD_SCORE,'0' type \r\n" + 
				"  from ns_assets_indexes nri \r\n" + 
				" where fd_stepid in \r\n" + 
				"       (select n.FD_ID \r\n" + 
				"          from ns_assets_step n \r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) \r\n" + 
				"           and n.fd_rateid = ?) \r\n" + 
				"   and FD_INDEXCATEGORY = '保值性' \r\n" + 
				"   and FD_INDEXNAME = '保值性' \r\n" + 
				"union \r\n" + 
				"select '可控性' name,to_char(round((FD_SCORE / \r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE \r\n" + 
				"           from ns_assets_indexes nri \r\n" + 
				"          where fd_stepid in \r\n" + 
				"                (select n.FD_ID \r\n" + 
				"                   from ns_assets_step n \r\n" + 
				"                  where n.fd_rateid = ? \r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) ) \r\n" + 
				"            and FD_INDEXCATEGORY = '可控性' \r\n" + 
				"            and FD_INDEXNAME != '可控性')),2),'990.00') FD_SCORE,'0' type \r\n" + 
				"  from ns_assets_indexes nri \r\n" + 
				" where fd_stepid in \r\n" + 
				"       (select n.FD_ID \r\n" + 
				"          from ns_assets_step n \r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) \r\n" + 
				"           and n.fd_rateid = ?) \r\n" + 
				"   and FD_INDEXCATEGORY = '可控性' \r\n" + 
				"   and FD_INDEXNAME = '可控性' \r\n" + 
				"union \r\n" + 
				"select '流通性' name,to_char(round((FD_SCORE / \r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE \r\n" + 
				"           from ns_assets_indexes nri \r\n" + 
				"          where fd_stepid in \r\n" + 
				"                (select n.FD_ID \r\n" + 
				"                   from ns_assets_step n \r\n" + 
				"                  where n.fd_rateid = ? \r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) ) \r\n" + 
				"            and FD_INDEXCATEGORY = '流通性' \r\n" + 
				"            and FD_INDEXNAME != '流通性')),2),'990.00') FD_SCORE,'0' type \r\n" + 
				"  from ns_assets_indexes nri \r\n" + 
				" where fd_stepid in \r\n" + 
				"       (select n.FD_ID \r\n" + 
				"          from ns_assets_step n \r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status \r\n" + 
				"                   from NS_ASSETS_RATING \r\n" + 
				"                  where fd_id = ? and fd_version = '3.0') \r\n" + 
				"                 when '000' then 1 \r\n" + 
				"                 when '020' then 3 \r\n" + 
				"               end) \r\n" + 
				"           and n.fd_rateid = ?) \r\n" + 
				"   and FD_INDEXCATEGORY = '流通性' \r\n" + 
				"   and FD_INDEXNAME = '流通性' \r\n" + 
				"union /* 存量 */ \r\n" + 
				"select '创收性平均值' name, \r\n" + 
				"       to_char(round((select (select sum(zong) \r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE / \r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT \r\n" + 
				"                                                            from ns_assets_indexes nri \r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '创收性' \r\n" + 
				"                                                             and nri.FD_INDEXNAME != '创收性' \r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE \r\n" + 
				"                                                   from ns_assets_indexes nri2 \r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '创收性' \r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '创收性' \r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step \r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                            and fd_vaild = '3'))) / \r\n" + 
				"                                            count(ns.fd_stepid)) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '创收性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id \r\n" + 
				"                                               from ns_assets_step \r\n" + 
				"                                              where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) / \r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '创收性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) as 创收性 \r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, \r\n" + 
				"       '1' type \r\n" + 
				"  from dual \r\n" + 
				"union \r\n" + 
				"select '流通性平均值' name, \r\n" + 
				"       to_char(round((select (select sum(zong) \r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE / \r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT \r\n" + 
				"                                                            from ns_assets_indexes nri \r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '流通性' \r\n" + 
				"                                                             and nri.FD_INDEXNAME != '流通性' \r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE \r\n" + 
				"                                                   from ns_assets_indexes nri2 \r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '流通性' \r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '流通性' \r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step \r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                            and fd_vaild = '3'))) / \r\n" + 
				"                                            count(ns.fd_stepid)) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '流通性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id \r\n" + 
				"                                               from ns_assets_step \r\n" + 
				"                                              where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) / \r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '流通性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) as 流通性 \r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, \r\n" + 
				"       '1' type \r\n" + 
				"  from dual \r\n" + 
				"union /*  用这个  */ \r\n" + 
				"select '可控性平均值' name, \r\n" + 
				"       to_char(round((select (select sum(zong) \r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE / \r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT \r\n" + 
				"                                                            from ns_assets_indexes nri \r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '可控性' \r\n" + 
				"                                                             and nri.FD_INDEXNAME != '可控性' \r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE \r\n" + 
				"                                                   from ns_assets_indexes nri2 \r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '可控性' \r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '可控性' \r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step \r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                            and fd_vaild = '3'))) / \r\n" + 
				"                                            count(ns.fd_stepid)) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '可控性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id \r\n" + 
				"                                               from ns_assets_step \r\n" + 
				"                                              where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) / \r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '可控性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) as 可控性 \r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, \r\n" + 
				"       '1' type \r\n" + 
				"  from dual \r\n" + 
				"union \r\n" + 
				"select '保值性平均值' name, \r\n" + 
				"       to_char(round((select (select sum(zong) \r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE / \r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT \r\n" + 
				"                                                            from ns_assets_indexes nri \r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '保值性' \r\n" + 
				"                                                             and nri.FD_INDEXNAME != '保值性' \r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE \r\n" + 
				"                                                   from ns_assets_indexes nri2 \r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid \r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '保值性' \r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '保值性' \r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step \r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                            and fd_vaild = '3'))) / \r\n" + 
				"                                            count(ns.fd_stepid)) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '保值性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id from ns_assets_step  where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) / \r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong \r\n" + 
				"                                       from ns_assets_indexes ns \r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '保值性' \r\n" + 
				"                                        and ns.fd_stepid in \r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in \r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1') \r\n" + 
				"                                                and fd_vaild = '3') \r\n" + 
				"                                      group by fd_stepid)) as 保值性 \r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, \r\n" + 
				"       '1' type \r\n" + 
				"  from dual \r\n" + 
				"union \r\n" + 
				"select '创收性最大值' name, '1' FD_SCORE, '-1' type from dual \r\n" + 
				"union \r\n" + 
				"select '保值性最大值' name, '1' FD_SCORE, '-1' type from dual \r\n" + 
				"union \r\n" + 
				"select '可控性最大值' name, '1' FD_SCORE, '-1' type from dual \r\n" + 
				"union \r\n" + 
				"select '流通性最大值' name, '1' FD_SCORE, '-1' type from dual";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> entity = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			entity.add(map);
			return entity;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getProduct(String id) {
		// 跟同产品的对比

		String sql = "/* 跟同产品的对比 */\r\n" + 
				"select '创收性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '创收性'\r\n" + 
				"            and FD_INDEXNAME != '创收性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '创收性'\r\n" + 
				"   and FD_INDEXNAME = '创收性'\r\n" + 
				"union\r\n" + 
				"select '保值性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '保值性'\r\n" + 
				"            and FD_INDEXNAME != '保值性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '保值性'\r\n" + 
				"   and FD_INDEXNAME = '保值性'\r\n" + 
				"union\r\n" + 
				"select '可控性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '可控性'\r\n" + 
				"            and FD_INDEXNAME != '可控性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '可控性'\r\n" + 
				"   and FD_INDEXNAME = '可控性'\r\n" + 
				"union\r\n" + 
				"select '流通性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '流通性'\r\n" + 
				"            and FD_INDEXNAME != '流通性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '流通性'\r\n" + 
				"   and FD_INDEXNAME = '流通性'\r\n" + 
				"union /* 存量 */\r\n" + 
				"select '创收性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '创收性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '创收性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 创收性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union\r\n" + 
				"select '保值性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '保值性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '保值性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 保值性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union /*  用这个  */\r\n" + 
				"select '可控性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '可控性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '可控性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 可控性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union\r\n" + 
				"select '流通性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '流通性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '流通性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' AND fd_vaild = '1' and PRODUCT_TYPE = (select n.PRODUCT_TYPE from ns_assets_RATING n where n.fd_id = ?))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 流通性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union\r\n" + 
				"select '创收性最大值' name, '1' FD_SCORE, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '保值性最大值' name, '1' FD_SCORE, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '可控性最大值' name, '1' FD_SCORE, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '流通性最大值' name, '1' FD_SCORE, '-1' type from dual";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> entity = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			entity.add(map);
			return entity;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getAssetsDepartment(String id) {
		// /* 跟所在部门的对比 */  

		String sql = "/* 跟所在部门的对比 */\r\n" + 
				"select '创收性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '创收性'\r\n" + 
				"            and FD_INDEXNAME != '创收性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '创收性'\r\n" + 
				"   and FD_INDEXNAME = '创收性'\r\n" + 
				"union\r\n" + 
				"select '保值性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '保值性'\r\n" + 
				"            and FD_INDEXNAME != '保值性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '保值性'\r\n" + 
				"   and FD_INDEXNAME = '保值性'\r\n" + 
				"union\r\n" + 
				"select '可控性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '可控性'\r\n" + 
				"            and FD_INDEXNAME != '可控性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '可控性'\r\n" + 
				"   and FD_INDEXNAME = '可控性'\r\n" + 
				"union\r\n" + 
				"select '流通性' name,to_char(round((FD_SCORE /\r\n" + 
				"       (select sum(FD_WEIGHT) FD_SCORE\r\n" + 
				"           from ns_assets_indexes nri\r\n" + 
				"          where fd_stepid in\r\n" + 
				"                (select n.FD_ID\r\n" + 
				"                   from ns_assets_step n\r\n" + 
				"                  where n.fd_rateid = ?\r\n" + 
				"                    and n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end) )\r\n" + 
				"            and FD_INDEXCATEGORY = '流通性'\r\n" + 
				"            and FD_INDEXNAME != '流通性')),2),'990.00') FD_SCORE,'0' type\r\n" + 
				"  from ns_assets_indexes nri\r\n" + 
				" where fd_stepid in\r\n" + 
				"       (select n.FD_ID\r\n" + 
				"          from ns_assets_step n\r\n" + 
				"         where n.FD_VAILD = (case (select fd_rating_status\r\n" + 
				"                   from NS_ASSETS_RATING\r\n" + 
				"                  where fd_id = ? and fd_version = '3.0')\r\n" + 
				"                 when '000' then 1\r\n" + 
				"                 when '020' then 3\r\n" + 
				"               end)\r\n" + 
				"           and n.fd_rateid = ?)\r\n" + 
				"   and FD_INDEXCATEGORY = '流通性'\r\n" + 
				"   and FD_INDEXNAME = '流通性'\r\n" + 
				"union\r\n" + 
				"select '创收性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '创收性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '创收性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 创收性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union\r\n" + 
				"select '保值性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '保值性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '保值性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 保值性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union\r\n" + 
				"select '可控性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '可控性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '可控性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 可控性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union\r\n" + 
				"select '流通性平均值' name, to_char(round((select (select sum(zong)\r\n" + 
				"                               from (select (sum((select (nri2.FD_SCORE /\r\n" + 
				"                                                        (select sum(nri.FD_WEIGHT) FD_WEIGHT\r\n" + 
				"                                                            from ns_assets_indexes nri\r\n" + 
				"                                                           where nri.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                             and nri.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                                             and nri.FD_INDEXNAME != '流通性'\r\n" + 
				"                                                             and nri.fd_stepid in (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                                         (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                                     and fd_vaild = '3'))) SCORE\r\n" + 
				"                                                   from ns_assets_indexes nri2\r\n" + 
				"                                                  where nri2.fd_stepid = ns.fd_stepid\r\n" + 
				"                                                    and nri2.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                                    and nri2.FD_INDEXNAME = '流通性'\r\n" + 
				"                                                    and nri2.fd_stepid in (select fd_id from ns_assets_step\r\n" + 
				"                                                          where fd_rateid in (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                            and fd_vaild = '3'))) /\r\n" + 
				"                                            count(ns.fd_stepid)) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id\r\n" + 
				"                                               from ns_assets_step\r\n" + 
				"                                              where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) /\r\n" + 
				"                            (select count(*) from (select count(ns.fd_stepid) zong\r\n" + 
				"                                       from ns_assets_indexes ns\r\n" + 
				"                                      where ns.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"                                        and ns.fd_stepid in\r\n" + 
				"                                            (select fd_id from ns_assets_step where fd_rateid in\r\n" + 
				"                                                    (select FD_ID from ns_assets_RATING where fd_version = '3.0' and fd_Rating_status = '020' and fd_vaild = '1' and FD_PROJECT_CODE in (select project_number from ns_prj_project where LEASE_ORGANIZATION = (select LEASE_ORGANIZATION from ns_prj_project where project_number = (select n.FD_PROJECT_CODE from ns_assets_RATING n where n.FD_ID = ?))))\r\n" + 
				"                                                and fd_vaild = '3')\r\n" + 
				"                                      group by fd_stepid)) as 流通性\r\n" + 
				"                       from dual), 2), '9990.99') FD_SCORE, '1' type\r\n" + 
				"  from dual\r\n" + 
				"union\r\n" + 
				"select '创收性最大值' name, '1' FD_SCORE, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '保值性最大值' name, '1' FD_SCORE, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '可控性最大值' name, '1' FD_SCORE, '-1' type from dual\r\n" + 
				"union\r\n" + 
				"select '流通性最大值' name, '1' FD_SCORE, '-1' type from dual";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id,id);
		if(queryForList.isEmpty()) {
			List<Map<String, Object>> entity = new ArrayList<>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg","没有数据");
			entity.add(map);
			return entity;
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getBarIncome(String id) {
		// 原始 SQL
		// /*复评状态 SQL*/SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME,  NVL(n.fd_configid, 0) FD_SCORE,  NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE,  '' FD_QUALITATIVE_OPTIONS,  '' indexname  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性'  UNION  SELECT '最大值描述',  '',  '',  substr(FD_QUALITATIVE_OPTIONS, 3),  FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')  UNION  SELECT '最小值描述',  '',  '',  substr(FD_QUALITATIVE_OPTIONS, 3),  FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')
		// /*评级测算 SQL*/ SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE,  '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_assets_indexes n  left join ns_assets_step nas  on n.FD_STEPID = nas.fd_id  WHERE nas.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (nas.fd_vaild = '1' or nas.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性'  UNION  SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(n.fd_configid))  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')  UNION  SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(n.fd_configid))  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '创收性'  AND n.fd_indexname != '创收性')

		// 复评状态 SQL
		String sql = "SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE,\r\n" + 
				"       '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"  FROM ns_assets_indexes n\r\n" + 
				"  left join ns_assets_step nas\r\n" + 
				"    on n.FD_STEPID = nas.fd_id\r\n" + 
				" WHERE nas.fd_rateid = ?\r\n" + 
				"   AND (nas.fd_vaild = '1' or nas.fd_vaild = '3')\r\n" + 
				"   AND n.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"   AND n.fd_indexname != '创收性'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"           AND n.fd_indexname != '创收性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT max(to_number(n.fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"           AND n.fd_indexname != '创收性')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"           AND n.fd_indexname != '创收性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT min(to_number(n.fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '创收性'\r\n" + 
				"           AND n.fd_indexname != '创收性')";
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
            if (value.equals(maxInfo)) 
            	maxCount.add(map);
            if (value.equals(minInfo)) 
            	minCount.add(map);
		}
		if(maxCount.size() > 1) 
			queryForList.removeAll(maxCount);
		if(minCount.size() > 1) 
			queryForList.removeAll(minCount);
		if(queryForList.size() == 3) {
			String json = JSON.toJSONString(queryForList);
			boolean contains = json.contains(maxInfo) && json.contains(minInfo);
			if(contains) {
				List<Map<String, Object>> max = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> min = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> map : queryForList) {
					String value = (String) map.get("FD_INDEXNAME");
		            if (value.equals(maxInfo)) 
		            	max.add(map);
		            if (value.equals(minInfo)) 
		            	min.add(map);
				}
				if(max.size() > 0) 
					queryForList.removeAll(max);
				if(min.size() > 0) 
					queryForList.removeAll(min);
			}
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getBarPreservation(String id) {
		// 原始 SQL
		// /*复评 SQL*/SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE,  NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性'  UNION  SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性')  UNION  SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性')
		// /*添加评级测算 SQL*/ SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性'  UNION  SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT FD_QUALITATIVE_OPTIONS, fd_configid, FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(n2.fd_configid))  FROM ns_assets_indexes n2  left join ns_assets_step s  on n2.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n2.FD_INDEXCATEGORY = '保值性'  AND n2.fd_indexname != '保值性')  UNION  SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT FD_QUALITATIVE_OPTIONS, fd_configid, FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(fd_configid))  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.fd_vaild = '1' or s.fd_vaild = '3')  AND n.FD_INDEXCATEGORY = '保值性'  AND n.fd_indexname != '保值性')

		String sql = "SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"  FROM ns_assets_indexes n\r\n" + 
				"  left join ns_assets_step s\r\n" + 
				"    on n.FD_STEPID = s.fd_id\r\n" + 
				" WHERE s.fd_rateid = ?\r\n" + 
				"   AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"   AND n.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"   AND n.fd_indexname != '保值性'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT FD_QUALITATIVE_OPTIONS, fd_configid, FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"            on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"           AND n.fd_indexname != '保值性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT max(to_number(n2.fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n2\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"            on n2.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n2.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"           AND n2.fd_indexname != '保值性')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT FD_QUALITATIVE_OPTIONS, fd_configid, FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"           AND n.fd_indexname != '保值性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT min(to_number(fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"         left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.fd_vaild = '1' or s.fd_vaild = '3')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '保值性'\r\n" + 
				"           AND n.fd_indexname != '保值性')";
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
            if (value.equals(maxInfo))
            	maxCount.add(map);
            if (value.equals(minInfo)) 
            	minCount.add(map);
		}
		if(maxCount.size() > 1) 
			queryForList.removeAll(maxCount);
		if(minCount.size() > 1) 
			queryForList.removeAll(minCount);
		if(queryForList.size() == 3) {
			String json = JSON.toJSONString(queryForList);
			boolean contains = json.contains(maxInfo) && json.contains(minInfo);
			if(contains) {
				List<Map<String, Object>> max = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> min = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> map : queryForList) {
					String value = (String) map.get("FD_INDEXNAME");
		            if (value.equals(maxInfo)) 
		            	max.add(map);
		            if (value.equals(minInfo)) 
		            	min.add(map);
				}
				if(max.size() > 0) 
					queryForList.removeAll(max);
				if(min.size() > 0) 
					queryForList.removeAll(min);
			}
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getBarCirculation(String id) {
		// 原始 SQL
		// SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性'  UNION  SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')  UNION  SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')
		// /*添加评级测算 SQL*/ SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.FD_VAILD = '3' or s.fd_vaild = '1')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性'  UNION  SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.FD_VAILD = '3' or s.fd_vaild = '1')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(n.fd_configid))  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.FD_VAILD = '3' or s.fd_vaild = '1')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')  UNION  SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT FD_QUALITATIVE_OPTIONS, fd_configid, FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.FD_VAILD = '3' or s.fd_vaild = '1')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(fd_configid))  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = 'fd578776-51cb-4411-83e7-939d96564b10'  AND (s.FD_VAILD = '3' or s.fd_vaild = '1')  AND n.FD_INDEXCATEGORY = '流通性'  AND n.fd_indexname != '流通性')

		String sql = "SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"  FROM ns_assets_indexes n\r\n" + 
				"  left join ns_assets_step s\r\n" + 
				"  on n.FD_STEPID = s.fd_id\r\n" + 
				"WHERE s.fd_rateid = ?\r\n" + 
				"   AND (s.FD_VAILD = '3' or s.fd_vaild = '1')\r\n" + 
				"   AND n.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"   AND n.fd_indexname != '流通性'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.fd_vaild = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"           AND n.fd_indexname != '流通性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT max(to_number(n.fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.fd_vaild = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"           AND n.fd_indexname != '流通性')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT FD_QUALITATIVE_OPTIONS, fd_configid, FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.fd_vaild = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"           AND n.fd_indexname != '流通性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT min(to_number(fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"         left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.fd_vaild = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '流通性'\r\n" + 
				"           AND n.fd_indexname != '流通性')";
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
            if (value.equals(maxInfo)) 
            	maxCount.add(map);
            if (value.equals(minInfo)) 
            	minCount.add(map);
		}
		if(maxCount.size() > 1) 
			queryForList.removeAll(maxCount);
		if(minCount.size() > 1) 
			queryForList.removeAll(minCount);
		if(queryForList.size() == 3) {
			String json = JSON.toJSONString(queryForList);
			boolean contains = json.contains(maxInfo) && json.contains(minInfo);
			if(contains) {
				List<Map<String, Object>> max = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> min = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> map : queryForList) {
					String value = (String) map.get("FD_INDEXNAME");
		            if (value.equals(maxInfo)) 
		            	max.add(map);
		            if (value.equals(minInfo)) 
		            	min.add(map);
				}
				if(max.size() > 0) 
					queryForList.removeAll(max);
				if(min.size() > 0) 
					queryForList.removeAll(min);
			}
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getBarSteerable(String id) {
		// 原始 SQL
		// SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE,  NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性'  UNION  SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')  UNION  SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM  (SELECT FD_QUALITATIVE_OPTIONS,  fd_configid,  FD_INDEXNAME  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(fd_configid))  FROM ns_assets_indexes n  WHERE n.FD_STEPID IN  (SELECT fd_id  FROM ns_assets_step s  WHERE s.fd_rateid = '48df9735-b388-47d7-a841-84455dced1be'  AND s.FD_VAILD = '3')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')
		// /*评级测算 SQL*/ SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = '244e999a-845d-4243-a48a-25f0562b5328'  AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性'  UNION  SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = '244e999a-845d-4243-a48a-25f0562b5328'  AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')  WHERE to_number(fd_configid) =  (SELECT max(to_number(fd_configid))  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = '244e999a-845d-4243-a48a-25f0562b5328'  AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')  UNION  SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = '244e999a-845d-4243-a48a-25f0562b5328'  AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')  WHERE to_number(fd_configid) =  (SELECT min(to_number(n.fd_configid))  FROM ns_assets_indexes n  left join ns_assets_step s  on n.FD_STEPID = s.fd_id  WHERE s.fd_rateid = '244e999a-845d-4243-a48a-25f0562b5328'  AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')  AND n.FD_INDEXCATEGORY = '可控性'  AND n.fd_indexname != '可控性')

		String sql = "SELECT NVL(n.FD_INDEXNAME, 0) FD_INDEXNAME, NVL(n.fd_configid, 0) FD_SCORE, NVL(n.FD_INDEXVALUE, '') || '档' FD_INDEXVALUE, '' FD_QUALITATIVE_OPTIONS, '' indexname\r\n" + 
				"  FROM ns_assets_indexes n\r\n" + 
				"  left join ns_assets_step s\r\n" + 
				"  on n.FD_STEPID = s.fd_id\r\n" + 
				"WHERE s.fd_rateid = ?\r\n" + 
				"   AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')\r\n" + 
				"   AND n.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"   AND n.fd_indexname != '可控性'\r\n" + 
				"UNION\r\n" + 
				"SELECT '最大值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"          WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"           AND n.fd_indexname != '可控性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT max(to_number(fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"           AND n.fd_indexname != '可控性')\r\n" + 
				"UNION\r\n" + 
				"SELECT '最小值描述', '', '', substr(FD_QUALITATIVE_OPTIONS, 3), FD_INDEXNAME\r\n" + 
				"  FROM (SELECT n.FD_QUALITATIVE_OPTIONS, n.fd_configid, n.FD_INDEXNAME\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"            on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"           AND n.fd_indexname != '可控性')\r\n" + 
				" WHERE to_number(fd_configid) =\r\n" + 
				"       (SELECT min(to_number(n.fd_configid))\r\n" + 
				"          FROM ns_assets_indexes n\r\n" + 
				"          left join ns_assets_step s\r\n" + 
				"          on n.FD_STEPID = s.fd_id\r\n" + 
				"         WHERE s.fd_rateid = ?\r\n" + 
				"           AND (s.FD_VAILD = '3' or s.FD_VAILD = '1')\r\n" + 
				"           AND n.FD_INDEXCATEGORY = '可控性'\r\n" + 
				"           AND n.fd_indexname != '可控性')";
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
		if(queryForList.size() == 3) {
			String json = JSON.toJSONString(queryForList);
			boolean contains = json.contains(maxInfo) && json.contains(minInfo);
			if(contains) {
				List<Map<String, Object>> max = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> min = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> map : queryForList) {
					String value = (String) map.get("FD_INDEXNAME");
		            if (value.equals(maxInfo)) {
		            	max.add(map);
		            }
		            if (value.equals(minInfo)) {
		            	min.add(map);
		            }
				}
				if(max.size() > 0) {
					queryForList.removeAll(max);
				}
				if(min.size() > 0) {
					queryForList.removeAll(min);
				}
			}
		}
		return queryForList;
	}

	@Override
	public List<Map<String, Object>> getFooter(String id) {
		// 原始 SQL
		// select n.major_risk_warning  from ns_assets_rating n  where fd_current_stepid in  (select fd_id  from ns_assets_step s  where s.fd_rateid = '098266ca-5e83-48f4-ae44-99fbf2df57a3')

		String sql = "select n.major_risk_warning\r\n" + 
				"  from ns_assets_rating n\r\n" + 
				" where fd_current_stepid in\r\n" + 
				"       (select fd_id\r\n" + 
				"          from ns_assets_step s\r\n" + 
				"         where s.fd_rateid = ?)";
		List<Map<String, Object>> queryForList = jdbc.queryForList(sql,id);
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
	public Map<String, Object> getAssetsLevelSurface(String id) {
		// 原始 SQL
		// select TO_CHAR(FD_FINAL_DATE, 'YYYY-MM-DD') FD_FINAL_DATE,  TO_CHAR(FD_DATE, 'YYYY-MM-DD') FD_DATE,  FD_FINAL_LEVEL,  (case  when INSTR(FD_FINAL_LEVEL, 'A') > 0 then  'A类（项目租赁）'  when INSTR(FD_FINAL_LEVEL, 'B') > 0 then  'B类（非项目租赁）'  end) type,  (case  when INSTR(FD_FINAL_LEVEL, 'Ⅰ') > 0 then  '强'  when INSTR(FD_FINAL_LEVEL, 'Ⅱ') > 0 then  '较强'  else  '一般'  end) info,  '本项资产信用保障倍数为' ||  NVL(trim(to_char(round(fd_final_pd, 2), '9990.99')), 0) || '倍; ' fd_pd  from NS_ASSETS_RATING  where FD_ID = '9db3897c-ae00-443b-b1fb-c91c2407724c'

		String sql = "select TO_CHAR(FD_FINAL_DATE, 'YYYY-MM-DD') FD_FINAL_DATE,\r\n" + 
				"       TO_CHAR(FD_DATE, 'YYYY-MM-DD') FD_DATE,\r\n" + 
				"       FD_FINAL_LEVEL,\r\n" + 
				"       (case\r\n" + 
				"         when INSTR(FD_FINAL_LEVEL, 'A') > 0 then\r\n" + 
				"          'A类（项目租赁）'\r\n" + 
				"         when INSTR(FD_FINAL_LEVEL, 'B') > 0 then\r\n" + 
				"          'B类（非项目租赁）'\r\n" + 
				"       end) type,\r\n" + 
				"       (case\r\n" + 
				"         when INSTR(FD_FINAL_LEVEL, 'Ⅰ') > 0 then\r\n" + 
				"          '强'\r\n" + 
				"         when INSTR(FD_FINAL_LEVEL, 'Ⅱ') > 0 then\r\n" + 
				"          '较强'\r\n" + 
				"         else\r\n" + 
				"          '一般'\r\n" + 
				"       end) info,\r\n" + 
				"       '资产信用保障倍数为' ||\r\n" + 
				"       NVL(trim(to_char(round(fd_final_pd, 2), '9990.99')), 0) || '倍' fd_pd\r\n" + 
				"  from NS_ASSETS_RATING\r\n" + 
				" where FD_ID = ?";
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
	public Map<String, Object> getAssetsPosition(String id) {
//		String status = getLevelStatus(id);
		Map<String, Object> map2 = getStatus(id);
		String status = (String)map2.get("FD_RATING_STATUS");
		String effectStatus = (String)map2.get("FD_VAILD");

		List<Map<String, Object>> queryForList = null;
		String sql = "";
		if("020".equals(status)) {
			if("1".equals(effectStatus)) {
				// 复评 SQL
				sql = "/* 资产得分分位数 */\r\n" + 
						"SELECT * FROM (SELECT trim(to_char(round((\r\n" + 
						"       (SELECT ro FROM\r\n" + 
						"          (SELECT f.ro FROM\r\n" + 
						"             (SELECT rownum - 1 ro, f.* FROM\r\n" + 
						"                (SELECT n.* FROM NS_ASSETS_RATING n\r\n" + 
						"                 WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1'\r\n" + 
						"                   AND n.product_type = (SELECT nfr.product_type FROM NS_ASSETS_RATING nfr WHERE nfr.FD_ID = ?)\r\n" + 
						"                 ORDER BY n.fd_final_sco) f) f\r\n" + 
						"           WHERE fd_id = ?)) /\r\n" + 
						"       (SELECT nu FROM\r\n" + 
						"          (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
						"             (SELECT count(*) - 1 nu FROM\r\n" + 
						"                (SELECT rownum ro, f.* FROM\r\n" + 
						"                   (SELECT n.* FROM NS_ASSETS_RATING n\r\n" + 
						"                    WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1'\r\n" + 
						"                      AND n.product_type = (SELECT nfr.product_type FROM NS_ASSETS_RATING nfr WHERE nfr.FD_ID = ?)\r\n" + 
						"                    ORDER BY n.fd_final_sco) f))))),2),'99990.99')) * 100 || '%' AS rankingOnTrack /*在同产品中的得分分位数*/\r\n" + 
						"   FROM dual),\r\n" + 
						"  (SELECT trim(to_char(round((\r\n" + 
						"         (SELECT ro FROM\r\n" + 
						"            (SELECT f.ro FROM\r\n" + 
						"               (SELECT rownum - 1 ro, f.* FROM\r\n" + 
						"                  (SELECT n.* FROM NS_ASSETS_RATING n WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1'\r\n" + 
						"                   ORDER BY n.fd_final_sco) f) f\r\n" + 
						"             WHERE fd_id = ?)) /\r\n" + 
						"         (SELECT nu FROM\r\n" + 
						"            (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
						"               (SELECT count(*) - 1 nu FROM\r\n" + 
						"                  (SELECT rownum ro, f.* FROM\r\n" + 
						"                     (SELECT n.* FROM NS_ASSETS_RATING n WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1'\r\n" + 
						"                      ORDER BY n.fd_final_sco) f))))),2),'99990.99')) * 100 || '%' AS ranking /*存量资产评级中的得分分位数*/\r\n" + 
						"   FROM dual)";
				queryForList = jdbc.queryForList(sql, id, id, id, id);
			} else {
				// 历史评级 SQL 
				sql = "/* 资产得分分位数 */ \r\n" + 
						"SELECT * FROM (SELECT trim(to_char(round(( \r\n" + 
						"       (SELECT ro FROM \r\n" + 
						"          (SELECT f.ro FROM \r\n" + 
						"             (SELECT rownum - 1 ro, f.* FROM \r\n" + 
						"                (select * from (SELECT n.* FROM NS_ASSETS_RATING n \r\n" + 
						"                 WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1' \r\n" + 
						"                   AND n.product_type = (SELECT nfr.product_type FROM NS_ASSETS_RATING nfr WHERE nfr.FD_ID = ?)\r\n" + 
						"                 union\r\n" + 
						"                 select * from Ns_Assets_Rating where fd_id = ?)  n\r\n" + 
						"                 ORDER BY n.fd_final_sco) f) f \r\n" + 
						"           WHERE fd_id = ?)) / \r\n" + 
						"       (SELECT nu FROM \r\n" + 
						"          (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM \r\n" + 
						"             (SELECT count(*) nu FROM\r\n" + 
						"                (SELECT rownum ro, f.* FROM \r\n" + 
						"                   (SELECT n.* FROM NS_ASSETS_RATING n \r\n" + 
						"                    WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1' \r\n" + 
						"                      AND n.product_type = (SELECT nfr.product_type FROM NS_ASSETS_RATING nfr WHERE nfr.FD_ID = ?) \r\n" + 
						"                    ORDER BY n.fd_final_sco) f))))),2),'99990.99')) * 100 || '%' AS rankingOnTrack /*在同产品中的得分分位数*/ \r\n" + 
						"   FROM dual), \r\n" + 
						"  (SELECT trim(to_char(round(( \r\n" + 
						"         (SELECT ro FROM \r\n" + 
						"            (SELECT f.ro FROM \r\n" + 
						"               (SELECT rownum - 1 ro, f.* FROM \r\n" + 
						"                  (select * from\r\n" + 
						"                    (SELECT n.* FROM NS_ASSETS_RATING n WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1' \r\n" + 
						"                     union\r\n" + 
						"                     select * from Ns_Assets_Rating where fd_id = ?)\r\n" + 
						"                   ORDER BY fd_final_sco) f) f \r\n" + 
						"             WHERE fd_id = ?)) / \r\n" + 
						"         (SELECT nu FROM \r\n" + 
						"            (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM \r\n" + 
						"               (SELECT count(*) nu FROM\r\n" + 
						"                  (SELECT rownum ro, f.* FROM \r\n" + 
						"                     (SELECT n.* FROM NS_ASSETS_RATING n WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 AND n.fd_vaild = '1' \r\n" + 
						"                      ORDER BY n.fd_final_sco) f))))),2),'99990.99')) * 100 || '%' AS ranking /*存量资产评级中的得分分位数*/ \r\n" + 
						"   FROM dual)";
				queryForList = jdbc.queryForList(sql, id, id, id, id, id, id);
			}
		} else {
			// 评级测算 SQL
			sql = "SELECT * FROM (SELECT trim(to_char(round(((SELECT ro FROM\r\n" + 
					"        (SELECT f.ro FROM\r\n" + 
					"           (SELECT rownum - 1 ro, f.* FROM\r\n" + 
					"              (select * from\r\n" + 
					"                      (select n.* from NS_ASSETS_RATING n where n.fd_version = '3.0' and n.FD_RATING_STATUS = '020' and n.fd_final_sco > 0 and n.fd_vaild = '1'\r\n" + 
					"                                and n.product_type =  (select nfr.product_type from NS_ASSETS_RATING nfr where nfr.FD_ID = ?)\r\n" + 
					"                             union\r\n" + 
					"                             select * from NS_ASSETS_RATING where fd_id = ?)\r\n" + 
					"                      order by fd_final_pd) f) f\r\n" + 
					"         WHERE fd_id = ?)) /\r\n" + 
					"     (SELECT nu FROM\r\n" + 
					"        (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
					"           (SELECT count(*) -1 nu FROM\r\n" + 
					"              (SELECT rownum ro, f.* FROM\r\n" + 
					"                 (SELECT n.* FROM NS_ASSETS_RATING n\r\n" + 
					"                  WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 and n.fd_vaild = '1'\r\n" + 
					"                    AND n.product_type =\r\n" + 
					"                      (SELECT nfr.product_type FROM NS_ASSETS_RATING nfr WHERE nfr.FD_ID = ?)\r\n" + 
					"                  ORDER BY n.fd_final_sco) f))))), 2), '99990.99')) * 100 || '%' AS rankingOnTrack /*在同产品中的得分分位数*/\r\n" + 
					"   FROM dual),\r\n" + 
					"  (SELECT trim(to_char(round(((SELECT ro FROM\r\n" + 
					"         (SELECT f.ro FROM\r\n" + 
					"            (SELECT rownum - 1 ro, f.* FROM\r\n" + 
					"               (SELECT n.* FROM (select n.* from NS_ASSETS_RATING n where n.fd_version = '3.0' and n.FD_RATING_STATUS = '020' and n.fd_final_sco > 0 and n.fd_vaild = '1'\r\n" + 
					"                              union\r\n" + 
					"                              select * from NS_ASSETS_RATING where fd_id = ?) n\r\n" + 
					"                ORDER BY n.fd_final_sco) f) f\r\n" + 
					"          WHERE fd_id = ?)) /\r\n" + 
					"      (SELECT nu FROM\r\n" + 
					"         (SELECT CASE nu WHEN 0 THEN 1 ELSE nu END nu FROM\r\n" + 
					"            (SELECT count(*) - 1 nu FROM\r\n" + 
					"               (SELECT rownum ro, f.* FROM\r\n" + 
					"                  (SELECT n.* FROM NS_ASSETS_RATING n WHERE n.fd_version = '3.0' AND n.FD_RATING_STATUS = '020' AND n.fd_final_sco > 0 and n.fd_vaild = '1'\r\n" + 
					"                   ORDER BY n.fd_final_sco) f))))), 2), '99990.99')) * 100 || '%' AS ranking /*存量资产评级中的得分分位数*/\r\n" + 
					"   FROM dual)";
			queryForList = jdbc.queryForList(sql, id, id, id, id, id, id);
		}
		
		if (queryForList.isEmpty()) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "没有数据");
			list.add(map);
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
	public Map<String, Object> getAssetsHeader(String id) {
		// 原始 SQL
		// select FD_CUST_NAME,FD_PROJECT_CODE,FD_PROJECT_NAME from NS_ASSETS_RATING where FD_ID = '917961eb-730e-4848-a40b-e0504caf936c'

		String sql = "select FD_CUST_NAME,FD_PROJECT_CODE,FD_PROJECT_NAME from NS_ASSETS_RATING where FD_ID = ?";
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
	public List<Map<String, Object>> getAssetsTrait(String id) {
		//--资产标签
		Map<String, Object> status = getStatus(id);
		String r = (String)status.get("FD_VAILD");

		List<Map<String, Object>> queryForList = null;
		if("1".equals(r)) {
			//--复评 SQL
			String sql = "select DECODE(n.fd_intern_level, n.fd_final_level, null, 'Y') \"result\", '0' type /*初复评有差异*/ \r\n" + 
					"  from ns_Assets_rating n \r\n" + 
					" WHERE n.FD_ID = ? \r\n" + 
					"union \r\n" + 
					"select case \r\n" + 
					"        when count(n.major_risk_warning)>0 then 'Y' \r\n" + 
					"        else '' \r\n" + 
					"       end \"result\", '1' type /* 存在重大风险的 */ \r\n" + 
					"  from ns_assets_rating n \r\n" + 
					"  left join ns_assets_step s \r\n" + 
					"    on s.fd_id = n.fd_current_stepid \r\n" + 
					" where s.fd_rateid = ? \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*), 1, 'Y', null) \"result\", '2' type /*存量排名前十*/ \r\n" + 
					"  from (select rownum ro, t.* \r\n" + 
					"          from ( /*存量排序*/ \r\n" + 
					"                select n.* from ns_assets_rating n \r\n" + 
					"                 where n.fd_version = '3.0' and n.fd_rating_status = '020' and fd_vaild = '1'\r\n" + 
					"                 order by n.fd_final_sco desc) t) ot \r\n" + 
					" where ot.ro <= 10 and ot.fd_id = ? \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'3' type /* 部门排名前三*/ \r\n" + 
					"  from (select rownum,t.* from(SELECT nmr.* \r\n" + 
					"  FROM ns_assets_rating nmr \r\n" + 
					"  LEFT JOIN ns_bp_master nbm/* 获取当前客户的客户经理 */ \r\n" + 
					"    ON nmr.FD_CUST_CODE = nbm.fd_bp_code \r\n" + 
					"  left join ns_bp_master nbm2 \r\n" + 
					"    on nbm2.FD_LEASE_ORGANIZATION = nbm.FD_LEASE_ORGANIZATION \r\n" + 
					"  LEFT JOIN ns_assets_rating nmr2 \r\n" + 
					"    ON nmr2.FD_CUST_CODE = nbm2.FD_BP_CODE \r\n" + 
					" WHERE nmr2.fd_id = ? \r\n" + 
					"   AND nmr.fd_rating_status = '020' AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1'\r\n" + 
					"   order by nmr.fd_final_sco desc) t where rownum <= 3) ot where fd_id = ? \r\n" + 
					"union \r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'4' type /* 产品排名前五 */  \r\n" + 
					"  FROM (select rownum,t.* from (SELECT n.* \r\n" + 
					"  FROM ns_assets_RATING n \r\n" + 
					" WHERE n.fd_version = '3.0' AND n.fd_Rating_status = '020' and n.fd_vaild = '1'\r\n" + 
					"   AND n.PRODUCT_TYPE = (SELECT n.PRODUCT_TYPE FROM ns_assets_RATING n WHERE n.fd_id = ?) \r\n" + 
					"  order by n.fd_final_sco desc) t where rownum <= 5) where fd_id = ?";
			queryForList = jdbc.queryForList(sql, id, id, id, id, id, id, id);
		} else {
			//--历史评级 SQL
			String sql = "select DECODE(n.fd_intern_level, n.fd_final_level, null, 'Y') \"result\", '0' type /*初复评有差异*/\r\n" + 
					"  from ns_Assets_rating n\r\n" + 
					" WHERE n.FD_ID = ?\r\n" + 
					"union\r\n" + 
					"select case\r\n" + 
					"        when count(n.major_risk_warning)>0 then 'Y'\r\n" + 
					"        else ''\r\n" + 
					"       end \"result\", '1' type /* 存在重大风险的 */\r\n" + 
					"  from ns_assets_rating n\r\n" + 
					"  left join ns_assets_step s\r\n" + 
					"    on s.fd_id = n.fd_current_stepid\r\n" + 
					" where s.fd_rateid = ?\r\n" + 
					"union\r\n" + 
					"select DECODE(count(*), 1, 'Y', null) \"result\", '2' type /*存量排名前十*/\r\n" + 
					"  from (select rownum ro, t.* from (select * from /*存量排序*/\r\n" + 
					"                (select n.* from ns_assets_rating n\r\n" + 
					"                 where n.fd_version = '3.0' and n.fd_rating_status = '020' and fd_vaild = '1'\r\n" + 
					"                 union\r\n" + 
					"                 select * from ns_assets_rating where fd_id = ?)\r\n" + 
					"                 order by fd_final_sco desc) t) ot\r\n" + 
					" where ot.ro <= 10 and ot.fd_id = ?\r\n" + 
					"union\r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'3' type /* 部门排名前三*/\r\n" + 
					"  from (select rownum,t.* from(select * from (\r\n" + 
					"  SELECT nmr.* FROM ns_assets_rating nmr\r\n" + 
					"  LEFT JOIN ns_bp_master nbm/* 获取当前客户的客户经理 */\r\n" + 
					"    ON nmr.FD_CUST_CODE = nbm.fd_bp_code\r\n" + 
					"  left join ns_bp_master nbm2\r\n" + 
					"    on nbm2.FD_LEASE_ORGANIZATION = nbm.FD_LEASE_ORGANIZATION\r\n" + 
					"  LEFT JOIN ns_assets_rating nmr2\r\n" + 
					"    ON nmr2.FD_CUST_CODE = nbm2.FD_BP_CODE\r\n" + 
					" WHERE nmr2.fd_id = ?\r\n" + 
					"   AND nmr.fd_rating_status = '020' AND nmr.fd_version = '3.0' and nmr.fd_vaild = '1'\r\n" + 
					" union\r\n" + 
					" select * from ns_assets_rating where fd_id = ?)\r\n" + 
					"   order by fd_final_sco desc) t where rownum <= 3) ot where fd_id = ?\r\n" + 
					"union\r\n" + 
					"select DECODE(count(*),1,'Y',null) \"result\",'4' type /* 产品排名前五 */\r\n" + 
					"  FROM (select rownum,t.* from (select * from (SELECT n.* FROM ns_assets_RATING n\r\n" + 
					" WHERE n.fd_version = '3.0' AND n.fd_Rating_status = '020' and n.fd_vaild = '1'\r\n" + 
					"   AND n.PRODUCT_TYPE = (SELECT n.PRODUCT_TYPE FROM ns_assets_RATING n WHERE n.fd_id = ?)\r\n" + 
					" union\r\n" + 
					" select * from ns_assets_rating where fd_id = ?)\r\n" + 
					"  order by fd_final_sco desc) t where rownum <= 5) where fd_id = ?";
			queryForList = jdbc.queryForList(sql, id, id, id, id, id, id, id, id, id, id);
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
	
	// 获取评级的初复评状态
	private String getLevelStatus(String id) {
		String sql = "SELECT fd_rating_status\r\n" + 
				"  FROM NS_ASSETS_RATING\r\n" + 
				" WHERE fd_id = ?\r\n" + 
				"   AND fd_version = '3.0'";
		List<Map<String,Object>> list = jdbc.queryForList(sql, id);
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			String object = (String)map.get("FD_RATING_STATUS");
			return object;
		}
		return null;
	}
	
	// 获取评级生效状态
	private Map<String, Object> getStatus(String id) {
		String sql = "select fd_vaild,fd_rating_status from ns_assets_rating where fd_id = ?  AND fd_version = '3.0'";
		List<Map<String,Object>> list = jdbc.queryForList(sql, id);
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			return map;
		}
		return null;
	}

	@Override
	public Map<String, Object> getAssetsScore(String id) {
		String sql = "select fd_final_sco from NS_ASSETS_RATING where fd_id = ?";
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
	
	
	
}