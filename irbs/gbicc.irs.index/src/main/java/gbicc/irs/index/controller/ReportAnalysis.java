package gbicc.irs.index.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/indexReport")
public class ReportAnalysis {
	@Autowired
	JdbcTemplate jdbc;

	/**
	 * @主体等级分析
	 * @return
	 */
	@RequestMapping(value = "queryMainLevel", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryMainLevel() throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list = jdbc.queryForList(
				"select FD_CODE from ns_main_level where fd_type='040' order by TO_NUMBER(fd_type),TO_NUMBER(fd_sort) ASC\r\n"
						+ "",
				String.class);
		String temp = "";
		String temp2 = "";
		for (int i = 0; i < list.size(); i++) {
			temp = list.get(i);
			if (list.size() > i && i != 0) {
				list.set(i, temp2);
			}
			if (i == 0) {
				list.set(i, "总量");
			}
			if (list.size() - 1 <= i) {
				list.add(temp);
				break;
			}
			temp2 = temp;
		}
		result.add(list);
		/*
		 * List<String> listData = jdbc.queryForList("select \r\n" +
		 * "		 count(levelz.fd_code) from ns_main_level levelz \r\n" +
		 * "		 left join ns_main_rating rating on fd_code=fd_final_level and FD_RATING_VAILD='1'\r\n"
		 * + "		 where levelz.fd_type='010'\r\n" +
		 * "		 group by rollup((levelz.fd_code,levelz.fd_type,levelz.fd_sort))\r\n"
		 * + "		 order by TO_NUMBER(levelz.fd_type),TO_NUMBER(levelz.fd_sort) ASC ",
		 * String.class);
		 */
		List<String> listData = jdbc.queryForList(
				" select  count(sco)\r\n" + "  from ns_main_level lev\r\n" + "  left join (select (case\r\n"
						+ "                      when FD_SCO > 81.69 then\r\n" + "                       'A'\r\n"
						+ "                      when FD_SCO > 77.76 and FD_SCO <= 81.69 then\r\n"
						+ "                       'B'\r\n"
						+ "                      when FD_SCO > 73.81 and FD_SCO <= 77.76 then\r\n"
						+ "                       'C'\r\n"
						+ "                      when FD_SCO > 69.80 and FD_SCO <= 73.81 then\r\n"
						+ "                       'D'\r\n"
						+ "                      when FD_SCO > 65.68 and FD_SCO <= 69.80 then\r\n"
						+ "                       'E'\r\n"
						+ "                      when FD_SCO > 61.34 and FD_SCO <= 65.68 then\r\n"
						+ "                       'F'\r\n"
						+ "                      when FD_SCO > 56.49 and FD_SCO <= 61.34 then\r\n"
						+ "                       'G'\r\n"
						+ "                      when FD_SCO > 50.14 and FD_SCO <= 56.49 then\r\n"
						+ "                       'H'\r\n"
						+ "                      when FD_SCO > 0.00 and FD_SCO <= 50.14 then\r\n"
						+ "                       'I'\r\n" + "                      when FD_SCO=0 then \r\n"
						+ "                       'J'\r\n" + "                    end) sco\r\n"
						+ "               from ns_main_rating rating\r\n"
						+ "              where rating.fd_rating_vaild = '1'  and fd_rating_status='020' ) ra\r\n"
						+ "    on ra.sco = lev.fd_code\r\n" + " where lev.fd_type = '040'\r\n"
						+ " group by rollup((lev.fd_code, lev.fd_type, lev.fd_sort))\r\n"
						+ " order by TO_NUMBER(lev.fd_type), TO_NUMBER(lev.fd_sort) ASC\r\n" + " ",
				String.class);
		temp = "";
		temp2 = "";
		for (int i = 0; i < listData.size(); i++) {
			temp = listData.get(i);
			if (listData.size() > i && i != 0) {
				listData.set(i, temp2);
			}
			if (i == 0) {
				listData.set(i, listData.get(listData.size() - 1));
			}
			if (listData.size() < i) {
				listData.set(i + 1, temp);
			}
			temp2 = temp;
		}
		result.add(listData);
		List<String> bl = new ArrayList<String>();
		for (int i = 0; i < listData.size(); i++) {
			Double blResult = Double.parseDouble(listData.get(i)) / Double.parseDouble(listData.get(0));
			bl.add(String.valueOf(String.format("%.0f", blResult * 100)));
		}
		result.add(bl);

		List<String> hidden = new ArrayList<String>();
		Integer tempResult = Integer.parseInt(listData.get(0));
		hidden.add("0");
		for (int i = 1; i < listData.size(); i++) {
			tempResult = tempResult - Integer.parseInt(listData.get(i));
			hidden.add(String.valueOf(tempResult));
		}
		result.add(hidden);
		return result;
	}

	/**
	 * @债项
	 * @return
	 */
	@RequestMapping(value = "queryDebtLevel", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryDebtLevel() throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list = jdbc.queryForList(
				"select FD_CODE from ns_main_level where fd_type='030'  order by TO_NUMBER(fd_type),TO_NUMBER(fd_sort) DESC",
				String.class);
		result.add(list);
		List<String> listData = jdbc.queryForList("select count(rating.fd_final_level)\r\n"
				+ "  from ns_main_level levelz\r\n" + "  left join ns_debt_rating rating\r\n"
				+ "    on fd_code = fd_final_level\r\n"
				+ "   and rating.FD_RATING_VAILD='1' and rating.fd_rating_status='020'\r\n"
				+ " where levelz.fd_type = '030'\r\n" + " group by (levelz.fd_code, levelz.fd_type, levelz.fd_sort)\r\n"
				+ " order by TO_NUMBER(levelz.fd_type), TO_NUMBER(levelz.fd_sort) DESC", String.class);
		result.add(listData);
		return result;
	}

	/**
	 * @债风险预警分析
	 * @return
	 */
	@RequestMapping(value = "queryWarn", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryWarn() throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> listAll = new ArrayList<String>();

		List<String> list = jdbc.queryForList(
				"select to_char(add_months(sysdate, -level + 1), 'yyyymm') timeDate from dual connect by level <= 6 order by timeDate asc",
				String.class);
		listAll = recently12(6);
		result.add(listAll);
		List<String> listData = jdbc.queryForList(" select count(WARN_LEVEL)\r\n"
				+ "                        from (select to_char(add_months(sysdate, -level + 1),\r\n"
				+ "                                             'yyyymm') mm\r\n"
				+ "                                from dual\r\n"
				+ "                              connect by level <= 6\r\n"
				+ "                               order by mm asc)\r\n"
				+ "                        left join T_AFT_WARN_INFO_DISTINCT info\r\n"
				+ "                          on mm = to_char(warn_time, 'yyyyMM') and  info.push_status = '010' \r\n"
				+ "                    left join t_aft_warn_rule rule  \r\n"
				+ "                       on info.RULE_CODE = rule.RULE_CODE and rule.WARN_LEVEL='high'\r\n"
				+ "                      group by mm order by mm asc", String.class);
		// result.add(listData);
		// listData= recently(6,list,listData);
		result.add(listData);
		List<String> listData2 = jdbc.queryForList(" select count(WARN_LEVEL)\r\n"
				+ "                        from (select to_char(add_months(sysdate, -level + 1),\r\n"
				+ "                                             'yyyymm') mm\r\n"
				+ "                                from dual\r\n"
				+ "                              connect by level <= 6\r\n"
				+ "                               order by mm asc)\r\n"
				+ "                        left join T_AFT_WARN_INFO_DISTINCT info\r\n"
				+ "                          on mm = to_char(warn_time, 'yyyyMM') and  info.push_status = '010' \r\n"
				+ "                    left join t_aft_warn_rule rule  \r\n"
				+ "                       on info.RULE_CODE = rule.RULE_CODE and rule.WARN_LEVEL='med'\r\n"
				+ "                      group by mm order by mm asc", String.class);
		// listData2= recently(6,list,listData2);
		result.add(listData2);

		return result;
	}

	/**
	 * @获取最近12个月份
	 * @param num
	 * @return
	 */
	public List<String> recently12(Integer num) {
		List<String> list = new ArrayList<String>();
		LocalDate today = LocalDate.now();
		for (int i = 0; i < num; i++) {
			LocalDate localDate = today.minusMonths(i);
			String ss = localDate.toString().substring(0, 7);
			list.add(ss.replace("-", ""));
		}
		Collections.reverse(list);
		return list;
	}

	/**
	 * @获取最近12个月份 已有数据的下标
	 * @param num
	 * @return
	 */
	public List<String> recently(Integer num, List<String> monthExisting, List<String> data) {
		List<String> list = new ArrayList<String>();
		LocalDate today = LocalDate.now();
		for (int i = 0; i < num; i++) {
			LocalDate localDate = today.minusMonths(i);
			String ss = localDate.toString().substring(0, 7);
			list.add(ss.replace("-", ""));
		}
		Collections.reverse(list);
		List<Integer> subscript = subscript(list, monthExisting);
		List<String> allData = new ArrayList<String>();
		for (int i = 0; i < num; i++) {
			allData.add("0");
		}
		for (int i = 0; i < subscript.size(); i++) {
			allData.set(subscript.get(i), data.get(i));
		}
		return allData;
	}

	/**
	 * @获取已存在月份的下标
	 * @param num
	 * @return
	 */
	public List<Integer> subscript(List<String> month12, List<String> monthExisting) {
		List<Integer> thereHaveBeen = new ArrayList<Integer>();
		for (int i = 0; i < month12.size(); i++) {
			for (int j = 0; j < monthExisting.size(); j++) {
				if (month12.get(i).equals(monthExisting.get(j))) {
					thereHaveBeen.add(i);
				}
			}
		}
		return thereHaveBeen;
	}

	/**
	 * @12个月变化趋势
	 * @return
	 */
	@RequestMapping(value = "queryWarnTrend", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryWarnTrend() throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> listAll = new ArrayList<String>();
		List<String> list = jdbc.queryForList(
				"select to_char(add_months(sysdate, -level + 1), 'yyyymm') timeDate from dual connect by level <= 12 order by timeDate asc",
				String.class);
		// listAll= recently12(12);
		result.add(list);

		List<String> listData = jdbc.queryForList("select  count(warn_time)\r\n"
				+ "  from (select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n" + "          from dual\r\n"
				+ "        connect by level <= 12)\r\n" + "  left join T_AFT_WARN_INFO_DISTINCT info\r\n"
				+ "    on mm = to_char(warn_time, 'yyyyMM') and info.push_status = '010'  and info.Rule_code like 'R001%'\r\n"
				+ "  left join t_aft_warn_rule rule\r\n" + "    on info.RULE_CODE = rule.RULE_CODE\r\n"
				+ " group by mm\r\n" + " order by mm ASC", String.class);
		// listData= recently(12,list,listData);
		result.add(listData);

		/*
		 * list = jdbc.queryForList("select mm, count(warn_time)\r\n" +
		 * "  from (select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n" +
		 * "          from dual\r\n" + "        connect by level <= 12)\r\n" +
		 * "  left join T_AFT_WARN_INFO_DISTINCT info\r\n" +
		 * "    on mm = to_char(warn_time, 'yyyyMM') and info.push_status = '010'  and info.Rule_code like 'R003%'\r\n"
		 * + "  left join t_aft_warn_rule rule\r\n" +
		 * "    on info.RULE_CODE = rule.RULE_CODE\r\n" + " group by mm\r\n" +
		 * " order by mm ASC",String.class); listAll= recently12(12);
		 */

		List<String> listData2 = jdbc.queryForList("select  count(warn_time)\r\n"
				+ "  from (select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n" + "          from dual\r\n"
				+ "        connect by level <= 12)\r\n" + "  left join T_AFT_WARN_INFO_DISTINCT info\r\n"
				+ "    on mm = to_char(warn_time, 'yyyyMM') and info.push_status = '010'  and info.Rule_code like 'R003%'\r\n"
				+ "  left join t_aft_warn_rule rule\r\n" + "    on info.RULE_CODE = rule.RULE_CODE\r\n"
				+ " group by mm\r\n" + " order by mm ASC", String.class);
		// listData2= recently(12,list,listData2);
		result.add(listData2);

		/*
		 * list = jdbc.queryForList("select mm, count(warn_time)\r\n" +
		 * "  from (select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n" +
		 * "          from dual\r\n" + "        connect by level <= 12)\r\n" +
		 * "  left join T_AFT_WARN_INFO_DISTINCT info\r\n" +
		 * "    on mm = to_char(warn_time, 'yyyyMM') and info.push_status = '010'  and info.Rule_code like 'R002%'\r\n"
		 * + "  left join t_aft_warn_rule rule\r\n" +
		 * "    on info.RULE_CODE = rule.RULE_CODE\r\n" + " group by mm\r\n" +
		 * " order by mm ASC",String.class);
		 */
		// listAll= recently12(12);

		List<String> listData3 = jdbc.queryForList("select  count(warn_time)\r\n"
				+ "  from (select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n" + "          from dual\r\n"
				+ "        connect by level <= 12)\r\n" + "  left join T_AFT_WARN_INFO_DISTINCT info\r\n"
				+ "    on mm = to_char(warn_time, 'yyyyMM') and info.push_status = '010'  and info.Rule_code like 'R002%'\r\n"
				+ "  left join t_aft_warn_rule rule\r\n" + "    on info.RULE_CODE = rule.RULE_CODE\r\n"
				+ " group by mm\r\n" + " order by mm ASC", String.class);
		// listData3= recently(12,list,listData3);
		result.add(listData3);

		return result;
	}

	/**
	 * @12个月主体变化趋势
	 * @return
	 */
	@RequestMapping(value = "queryMainChange", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryMainChange() throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list = jdbc.queryForList(
				"select to_char(add_months(sysdate, -level + 1), 'yyyymm') timeDate from dual connect by level <= 12 order by timeDate asc",
				String.class);
		result.add(list);
		List<String> listData = jdbc.queryForList("select count(FD_FINAL_DATE) as countz\r\n" + "  from (select *\r\n"
				+ "          from ((select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n"
				+ "                   from dual\r\n" + "                 connect by level <= 12))\r\n"
				+ "          left join ns_main_rating\r\n" + "            on mm = to_char(FD_FINAL_DATE, 'yyyyMM')\r\n"
				+ "           and FD_RATING_VAILD = '1'\r\n" + "           and fd_rating_status = '020')\r\n"
				+ " group by mm\r\n" + " order by mm ASC", String.class);
		// listData= recently(12,list,listData);
		result.add(listData);
		List<String> listData2 = jdbc.queryForList("select (case when avg(FD_SCO) is null then 0 else avg(FD_SCO) end) as countz\r\n" + "  from (select *\r\n"
				+ "          from ((select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n"
				+ "                   from dual\r\n" + "                 connect by level <= 12))\r\n"
				+ "          left join ns_main_rating\r\n" + "            on mm = to_char(FD_FINAL_DATE, 'yyyyMM')\r\n"
				+ "           and FD_RATING_VAILD = '1'\r\n" + "           and fd_rating_status = '020')\r\n"
				+ " group by mm\r\n" + " order by mm ASC", String.class);

		for (int i = 0; i < listData2.size(); i++) {
			listData2.set(i, String.format("%.2f", Double.parseDouble(listData2.get(i))));
		}

		// listData2= recently(11,list,listData2);
		result.add(listData2);

		return result;
	}

	/**
	 * @12个月债项变化趋势
	 * @return
	 */
	@RequestMapping(value = "queryDebtChange", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryDebtChange() throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> listAll = new ArrayList<String>();
		List<String> list = jdbc.queryForList(
				"select to_char(add_months(sysdate, -level + 1), 'yyyymm') timeDate from dual connect by level <= 12 order by timeDate asc",
				String.class);

		// and FD_RATING_VAILD='1'
		// listAll= recently12(12);
		result.add(list);
		List<String> listData = jdbc.queryForList("select count(FD_FINAL_DATE) as countz\r\n" + "  from (select *\r\n"
				+ "          from ((select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n"
				+ "                   from dual\r\n" + "                 connect by level <= 12))\r\n"
				+ "          left join ns_debt_rating\r\n" + "            on mm = to_char(FD_FINAL_DATE, 'yyyyMM')\r\n"
				+ "           and FD_RATING_VAILD = '1'\r\n" + "           and fd_rating_status = '020')\r\n"
				+ " group by mm\r\n" + " order by mm ASC", String.class);
		// listData= recently(12,list,listData);
		result.add(listData);
		List<String> listData2 = jdbc
				.queryForList("select case when avg(FD_SCO) is null then 0 else avg(FD_SCO) end as countz\r\n"
						+ "  from (select *\r\n"
						+ "          from ((select to_char(add_months(sysdate, -level + 1), 'yyyymm') mm\r\n"
						+ "                   from dual\r\n" + "                 connect by level <= 12))\r\n"
						+ "          left join ns_debt_rating\r\n"
						+ "            on mm = to_char(FD_FINAL_DATE, 'yyyyMM')\r\n"
						+ "           and FD_RATING_VAILD = '1'\r\n" + "           and fd_rating_status = '020')\r\n"
						+ " group by mm\r\n" + " order by mm ASC", String.class);

		for (int i = 0; i < listData2.size(); i++) {
			listData2.set(i, String.format("%.2f", Double.parseDouble(listData2.get(i))));
		}

		// listData2= recently(12,list,listData2);
		result.add(listData2);

		return result;
	}

	/**
	 * @预警数量
	 * @return
	 */
	@RequestMapping(value = "queryWarnNum", method = RequestMethod.GET)
	@ResponseBody
	public List<String> queryWarnNum() throws Exception {
		List<String> result = new ArrayList<String>();
		// 总计高预警数目
		Integer countSum = jdbc.queryForObject("select count(*) from T_AFT_WARN_INFO_DISTINCT info \r\n"
				+ "         inner join t_aft_warn_rule rule \r\n"
				+ "            on info.RULE_CODE = rule.RULE_CODE \r\n"
				+ "          where 1=1 and push_status = '010' and WARN_LEVEL='high'  ", Integer.class);
		result.add(String.valueOf(countSum));
		// 当月高预警数目
		Integer count = jdbc.queryForObject("select count(*)  from T_AFT_WARN_INFO_DISTINCT info \r\n"
				+ "         inner join t_aft_warn_rule rule \r\n"
				+ "            on info.RULE_CODE = rule.RULE_CODE \r\n"
				+ "          where 1=1 and push_status = '010'  and WARN_LEVEL='high' and \r\n"
				+ "         to_char(warn_time,'yyyyMM') =to_char(sysdate,'yyyyMM')", Integer.class);
		result.add(String.valueOf(count));

		// 总计中预警数目
		countSum = jdbc.queryForObject("select count(*) from T_AFT_WARN_INFO_DISTINCT info \r\n"
				+ "         inner join t_aft_warn_rule rule \r\n"
				+ "            on info.RULE_CODE = rule.RULE_CODE \r\n"
				+ "          where 1=1 and push_status = '010' and WARN_LEVEL='med'  ", Integer.class);
		result.add(String.valueOf(countSum));
		// 当月高预警数目
		count = jdbc.queryForObject("select count(*)  from T_AFT_WARN_INFO_DISTINCT info \r\n"
				+ "         inner join t_aft_warn_rule rule \r\n"
				+ "            on info.RULE_CODE = rule.RULE_CODE \r\n"
				+ "          where 1=1 and push_status = '010'  and WARN_LEVEL='med' and \r\n"
				+ "         to_char(warn_time,'yyyyMM') =to_char(sysdate,'yyyyMM')", Integer.class);
		result.add(String.valueOf(count));

		return result;
	}

	/**
	 * @预警数量
	 * @return
	 */
	@RequestMapping(value = "disposalNum", method = RequestMethod.GET)
	@ResponseBody
	public List<String> disposalNum() throws Exception {
		List<String> result = new ArrayList<String>();
		// 未处理
		Integer countSum = jdbc.queryForObject(
				"select count(*) from T_AFT_WARN_INFO_DISTINCT where  PUSH_STATUS ='010'", Integer.class);
		result.add(String.valueOf(countSum));
		// 处理中
		Integer count = jdbc.queryForObject(
				"select count(*) from T_AFT_WARN_INFO_DISTINCT where PUSH_STATUS ='010' and RESULT_TEMP='处理中'",
				Integer.class);
		result.add(String.valueOf(count));
		// 已处理
		Integer countSumY = jdbc.queryForObject(
				" select count(*) from T_AFT_WARN_INFO_DISTINCT where PUSH_STATUS ='010' and RESULT_TEMP='已处理'",
				Integer.class);
		result.add(String.valueOf(countSumY));
		result.set(0, String.valueOf(countSum - count - countSumY));
		return result;
	}

	/**
	 * @mainDashboard 主体仪表盘
	 * @return
	 */
	@RequestMapping(value = "mainDashboard", method = RequestMethod.GET)
	@ResponseBody
	public List<String> mainDashboard() throws Exception {
		List<String> result = new ArrayList<String>();
		// 总平均分
		Double countAvg = jdbc.queryForObject(
				"select avg(fd_sco) from ns_main_rating where FD_RATING_VAILD='1' and fd_sco is not null  and fd_rating_status='020'",
				Double.class);
		// 当月数量
		String countSum = jdbc.queryForObject(
				"select count(*) from ns_main_rating where  to_char(fd_final_date,'yyyyMM') =to_char(sysdate,'yyyyMM') and FD_RATING_VAILD='1' and fd_sco is not null  and fd_rating_status='020'",
				String.class);
		// 总数量
		String countAll = jdbc.queryForObject(
				"select count(*) from ns_main_rating where  FD_RATING_VAILD='1' and fd_sco is not null  and fd_rating_status='020'",
				String.class);
		// 当月平均分
		Double avgAll = jdbc.queryForObject(
				"select avg(fd_sco) from ns_main_rating where  FD_RATING_VAILD='1' and fd_sco is not null  and fd_rating_status='020' and to_char(sysdate,'yyyyMM')=to_char(fd_final_date,'yyyyMM')",
				Double.class);
		// 前一个月的平均分
		Double beforeavgAll = jdbc.queryForObject(
				"select avg(fd_sco) from ns_main_rating where  FD_RATING_VAILD='1' and fd_sco is not null  and fd_rating_status='020' and to_char(sysdate,'yyyyMM')>to_char(fd_final_date,'yyyyMM')",
				Double.class);

		// 上月数量
		String lastAvgAll = jdbc.queryForObject(
				"select  count(*) from ns_main_rating where  FD_RATING_VAILD='1' and fd_sco is not null  and fd_rating_status='020' and to_char(add_months(sysdate,-1),'yyyyMM')=to_char(fd_final_date,'yyyyMM')",
				String.class);

		// 上月平均分
		String lastAvgSco = jdbc.queryForObject(
				"select  avg(fd_sco) from ns_main_rating where  FD_RATING_VAILD='1' and fd_sco is not null  and fd_rating_status='020' and to_char(add_months(sysdate,-1),'yyyyMM')=to_char(fd_final_date,'yyyyMM')",
				String.class);

		if (countAvg == null) {
			countAvg = 0.0;
		}
		if (avgAll == null) {
			avgAll = 0.0;
		}
		result.add(String.format("%.2f", avgAll));
		result.add(String.valueOf(countSum));
		result.add(String.valueOf(countAll));
		result.add(String.format("%.2f", countAvg));
		if (beforeavgAll == 0) {
			result.add("0");
		} else {
			result.add(String.format("%.2f", (countAvg - beforeavgAll)));
		}
		result.add(lastAvgAll);
		result.add(lastAvgSco);
		return result;
	}

	/**
	 * @mainDashboard
	 * @仪表盘
	 * @return
	 */
	@RequestMapping(value = "debtDashboard", method = RequestMethod.GET)
	@ResponseBody
	public List<String> debtDashboard() throws Exception {
		List<String> result = new ArrayList<String>();
		// 平均分
		Double countAvg = jdbc.queryForObject(
				"select avg(fd_sco) from ns_debt_rating where  to_char(fd_final_date,'yyyyMM') =to_char(sysdate,'yyyyMM') and FD_RATING_VAILD='1' and fd_rating_status='020'",
				Double.class);
		// 当月数量
		String countSum = jdbc.queryForObject(
				"select count(*) from ns_debt_rating where  to_char(fd_final_date,'yyyyMM') =to_char(sysdate,'yyyyMM') and FD_RATING_VAILD='1' and fd_rating_status='020'",
				String.class);
		// 当月数量
		String countAll = jdbc.queryForObject(
				"select count(*) from ns_debt_rating where  FD_RATING_VAILD='1' and fd_rating_status='020'",
				String.class);

		Double avgAll = jdbc.queryForObject(
				"select avg(fd_sco) from ns_debt_rating where  FD_RATING_VAILD='1' and fd_rating_status='020'",
				Double.class);

		if (countAvg == null) {
			countAvg = 0.0;
		}
		if (avgAll == null) {
			avgAll = 0.0;
		}
		result.add(String.format("%.2f", countAvg));
		result.add(String.valueOf(countSum));
		result.add(String.valueOf(countAll));
		result.add(String.format("%.2f", avgAll));
		result.add(String.format("%.2f", (avgAll - countAvg)));
		return result;
	}

	/**
	 * @newRadar新建2.0
	 * 
	 * @return
	 */
	@RequestMapping(value = "newRadar", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> newRadar() throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<Map<String, Object>> one = jdbc.queryForList("    select fd_name as \"name\",fd_value as \"max\" from ns_standard_score where fd_code in('XJZT001',\r\n" + 
				"            'XJZT002', 'XJZT003', 'XJZT004', 'XJZT005', 'XJZT006',\r\n" + 
				"            'XJZT007', 'XJZT008', 'XJZT009'\r\n" + 
				"            ) and fd_vaild='2.0'order by fd_code asc\r\n" + 
				"            ");

		List<String> two = jdbc.queryForList(
				"select round(avg(fd_score),2) from ns_rating_indexes where fd_indexcode in('XJZT001',\r\n"
						+ "'XJZT002',\r\n" + "'XJZT003',\r\n" + "'XJZT004',\r\n" + "'XJZT005',\r\n" + "'XJZT006',\r\n"
						+ "'XJZT007',\r\n" + "'XJZT008',\r\n" + "'XJZT009'\r\n"
						+ ") and fd_stepId in (select fd_id from ns_rating_step where fd_rateid in(select FD_ID from ns_main_rating where fd_rating_vaild='1' and fd_version='2.0' and fd_rating_status='020'  and fd_type='NEW_BUILD') and fd_vaild='1')\r\n"
						+ "group by fd_indexName,fd_indexCode order by fd_indexCode asc",
				String.class);
		result.add(one);
		result.add(two);
		return result;
	}

	/**
	 * @newRadar通用2.0
	 * 
	 * @return
	 */
	@RequestMapping(value = "TyRadar", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> TyRadar() throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<Map<String, Object>> one = jdbc.queryForList(
				" select fd_name as \"name\",fd_value as \"max\" from ns_standard_score where fd_code in('TYZB001',\r\n" + 
				"            'TYZB002', 'TYZB003', 'TYZB004', 'TYZB005', 'TYZB006',\r\n" + 
				"            'TYZB007', 'TYZB008', 'TYZB009', 'TYZB010', 'TYZB011',\r\n" + 
				"            'TYZB012', 'TYZB013'\r\n" + 
				"            ) and fd_vaild='2.0'order by fd_code asc");

		List<String> two = jdbc
				.queryForList("select round(avg(fd_score),2)  from ns_rating_indexes where fd_indexcode in(\r\n"
						+ "'TYZB001',\r\n" + "'TYZB002',\r\n" + "'TYZB003',\r\n" + "'TYZB004',\r\n" + "'TYZB005',\r\n"
						+ "'TYZB006',\r\n" + "'TYZB007',\r\n" + "'TYZB008',\r\n" + "'TYZB009',\r\n" + "'TYZB010',\r\n"
						+ "'TYZB011',\r\n" + "'TYZB012',\r\n" + "'TYZB013'\r\n"
						+ ") and fd_stepId in (select fd_id from ns_rating_step where fd_rateid in(select FD_ID from ns_main_rating where fd_rating_vaild='1' and fd_type<>'NEW_BUILD' and fd_version='2.0' and fd_rating_status='020' ) and fd_vaild='1')\r\n"
						+ "group by fd_indexName,fd_indexCode order by fd_indexCode asc", String.class);
		result.add(one);
		result.add(two);
		return result;
	}

	/**
	 * @newRadar新建1.0
	 * 
	 * @return
	 */
	@RequestMapping(value = "newRadar1", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> newRadar1() throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<Map<String, Object>> one = jdbc
				.queryForList("  select fd_name as \"name\",fd_value as \"max\" from ns_standard_score where fd_code in('XJZT001', \r\n" + 
						"                       'XJZT002',             'XJZT003',             'XJZT004', \r\n" + 
						"                       'XJZT005',             'XJZT006',             'XJZT007', \r\n" + 
						"                       'XJZT008',             'XJZT009' \r\n" + 
						"                       ) and fd_vaild='1.0'order by fd_code asc");

		List<String> two = jdbc.queryForList(
				"select round(avg(fd_score),2) from ns_rating_indexes where fd_indexcode in('XJZT001',\r\n"
						+ "'XJZT002',\r\n" + "'XJZT003',\r\n" + "'XJZT004',\r\n" + "'XJZT005',\r\n" + "'XJZT006',\r\n"
						+ "'XJZT007',\r\n" + "'XJZT008',\r\n" + "'XJZT009'\r\n"
						+ ") and fd_stepId in (select fd_id from ns_rating_step where fd_rateid in(select FD_ID from ns_main_rating where fd_rating_vaild='1' and fd_version='1.0' and fd_rating_status='020'  and fd_type='NEW_BUILD') and fd_vaild='1')\r\n"
						+ "group by fd_indexName,fd_indexCode order by fd_indexCode asc",
				String.class);
		result.add(one);
		result.add(two);
		return result;
	}

	/**
	 * @newRadar通用2.0
	 * 
	 * @return
	 */
	@RequestMapping(value = "TyRadar1", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> TyRadar1() throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<Map<String, Object>> one = jdbc.queryForList(" select fd_name as \"name\",fd_value as \"max\" from ns_standard_score where fd_code in('TYZB001',                         'TYZB002',\r\n" + 
				"                                'TYZB003',                         'TYZB004',\r\n" + 
				"                                'TYZB005',                         'TYZB006',\r\n" + 
				"                                'TYZB007',                         'TYZB008',\r\n" + 
				"                                'TYZB009',                         'TYZB010',\r\n" + 
				"                                'TYZB011',                         'TYZB012',\r\n" + 
				"                                'TYZB013') and fd_vaild='1.0'order by fd_code asc");

		List<String> two = jdbc
				.queryForList("select round(avg(fd_score),2)  from ns_rating_indexes where fd_indexcode in(\r\n"
						+ "'TYZB001',\r\n" + "'TYZB002',\r\n" + "'TYZB003',\r\n" + "'TYZB004',\r\n" + "'TYZB005',\r\n"
						+ "'TYZB006',\r\n" + "'TYZB007',\r\n" + "'TYZB008',\r\n" + "'TYZB009',\r\n" + "'TYZB010',\r\n"
						+ "'TYZB011',\r\n" + "'TYZB012',\r\n" + "'TYZB013'\r\n"
						+ ") and fd_stepId in (select fd_id from ns_rating_step where fd_rateid in(select FD_ID from ns_main_rating where fd_rating_vaild='1' and fd_type<>'NEW_BUILD' and fd_version='1.0' and fd_rating_status='020' ) and fd_vaild='1')\r\n"
						+ "group by fd_indexName,fd_indexCode order by fd_indexCode asc", String.class);
		result.add(one);
		result.add(two);
		return result;
	}

	/**
	 * @newRadar
	 * @return
	 */
	@RequestMapping(value = "focusOn", method = RequestMethod.GET)
	@ResponseBody
	public List<String> focusOn() throws Exception {
		List<String> result = new ArrayList<String>();
		// String one = jdbc.queryForObject("select count(*) from t_aft_atten_customer
		// where IS_ATTEN='Y'",String.class);
		String one = jdbc.queryForObject(
				"select count(distinct lessee_id) from t_aft_warn_info_distinct where push_status='010'", String.class);
		result.add(one);
		// String two = jdbc.queryForObject("select count(*) from t_aft_atten_customer
		// where IS_ATTEN='Y' and to_char(DATA_DT,'yyyyMM') =
		// to_char(sysdate,'yyyyMM')",String.class);
		String two = jdbc.queryForObject(
				"select count(distinct lessee_id) from t_aft_warn_info_distinct where to_char(warn_time,'yyyyMM') = to_char(sysdate,'yyyyMM') and push_status='010'",
				String.class);
		result.add(two);
		// 监控总数
		String three = jdbc.queryForObject("select count(*) from t_aft_atten_customer where is_atten='Y'",
				String.class);
		result.add(three);
		// 较上个月监控数量；
		String four = jdbc.queryForObject(
				"select (select count(*) from t_aft_atten_customer where is_atten='Y' and to_char(data_dt,'yyyyMM')>=to_char(add_months(sysdate,-1),'yyyyMM'))-(select count(*) from t_aft_atten_customer where is_atten='Y' and to_char(add_months(sysdate,-1),'yyyyMM')=to_char(data_dt,'yyyyMM')\r\n"
						+ ") as cf from dual",
				String.class);
		result.add(four);

		return result;
	}

	/**
	 * @newRadar
	 * @return
	 */
	@RequestMapping(value = "mainIndustry", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> mainIndustry() throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<Map<String, Object>> one = jdbc.queryForList(
				"select count(fd_cust_code) AS COUNTALL,PARENT_NAME from ( \r\n" + "           SELECT \r\n"
						+ "           A.FD_FINAL_LEVEL, \r\n" + "           A.FD_CUST_CODE, \r\n"
						+ "           C.PARENT_NAME , \r\n" + "           D.RN, \r\n"
						+ "           COUNT(*) AS QTY \r\n" + "           FROM NS_MAIN_RATING A \r\n"
						+ "           JOIN NS_BP_MASTER B \r\n" + "           ON A.FD_CUST_CODE=B.FD_BP_CODE \r\n"
						+ "           JOIN V_NS_ZGC_FATHER_LEVEL C \r\n"
						+ "           ON B.FD_SEGMENT_INDUSTRY=C.VALUE_CODE \r\n"
						+ "           JOIN         (SELECT\r\n" + "PARENT_CODE,\r\n" + "RN\r\n" + "FROM\r\n" + "(\r\n"
						+ "SELECT\r\n" + "PARENT_CODE,\r\n" + "ROW_NUMBER()OVER(ORDER BY QTY DESC) RN\r\n" + "FROM\r\n"
						+ "(\r\n" + "SELECT\r\n" + "C.PARENT_CODE,\r\n" + "COUNT(*) QTY\r\n"
						+ "FROM NS_MAIN_RATING A\r\n" + "JOIN NS_BP_MASTER B\r\n" + "ON A.FD_CUST_CODE=B.FD_BP_CODE\r\n"
						+ "JOIN V_NS_ZGC_FATHER_LEVEL C\r\n" + "ON B.FD_SEGMENT_INDUSTRY=C.VALUE_CODE\r\n"
						+ "WHERE A.FD_RATING_STATUS='020'\r\n" + "AND A.FD_RATING_VAILD='1'\r\n" + "GROUP BY\r\n"
						+ "C.PARENT_CODE\r\n" + "))\r\n" + "WHERE RN <=5) D \r\n"
						+ "           ON C.PARENT_CODE=D.PARENT_CODE \r\n"
						+ "           WHERE A.FD_RATING_STATUS='020' \r\n"
						+ "           AND A.FD_RATING_VAILD='1'  and fd_rating_status='020' \r\n"
						+ "           GROUP BY  \r\n" + "           A.FD_FINAL_LEVEL, \r\n"
						+ "           A.FD_CUST_CODE, \r\n" + "           C.PARENT_NAME, \r\n"
						+ "           D.RN)group by PARENT_NAME");
		Double allCust = jdbc.queryForObject("select sum(COUNTALL) from (\r\n"
				+ "select count(fd_cust_code) AS COUNTALL,PARENT_NAME from ( \r\n" + "           SELECT \r\n"
				+ "           A.FD_FINAL_LEVEL, \r\n" + "           A.FD_CUST_CODE, \r\n"
				+ "           C.PARENT_NAME , \r\n" + "           COUNT(*) AS QTY \r\n"
				+ "           FROM NS_MAIN_RATING A \r\n" + "           JOIN NS_BP_MASTER B \r\n"
				+ "           ON A.FD_CUST_CODE=B.FD_BP_CODE \r\n" + "           JOIN V_NS_ZGC_FATHER_LEVEL C \r\n"
				+ "           ON B.FD_SEGMENT_INDUSTRY=C.VALUE_CODE \r\n"
				+ "           inner JOIN V_NS_ZGC_FATHER_LEVEL D \r\n"
				+ "           ON C.PARENT_CODE=D.PARENT_CODE \r\n" + "           WHERE A.FD_RATING_STATUS='020' \r\n"
				+ "           AND A.FD_RATING_VAILD='1'  and fd_rating_status='020' \r\n" + "           GROUP BY  \r\n"
				+ "           A.FD_FINAL_LEVEL, \r\n" + "           A.FD_CUST_CODE, \r\n"
				+ "           C.PARENT_NAME\r\n" + "			 		)group by PARENT_NAME)", Double.class);

		Double allSum = jdbc.queryForObject("select sum(count(fd_cust_code)) from (\r\n" + "SELECT\r\n"
				+ "A.FD_FINAL_LEVEL,\r\n" + "A.FD_CUST_CODE,\r\n" + "C.PARENT_NAME ,\r\n" + "D.RN,\r\n"
				+ "COUNT(*) AS QTY\r\n" + "FROM NS_MAIN_RATING A\r\n" + "JOIN NS_BP_MASTER B\r\n"
				+ "ON A.FD_CUST_CODE=B.FD_BP_CODE\r\n" + "JOIN V_NS_ZGC_FATHER_LEVEL C\r\n"
				+ "ON B.FD_SEGMENT_INDUSTRY=C.VALUE_CODE\r\n" + "JOIN V_NS_MAIN_RATING_SEGMENT_01 D\r\n"
				+ "ON C.PARENT_CODE=D.PARENT_CODE\r\n" + "WHERE A.FD_RATING_STATUS='020'\r\n"
				+ "AND A.FD_RATING_VAILD='1'  and fd_rating_status='020'\r\n" + "GROUP BY \r\n"
				+ "A.FD_FINAL_LEVEL,\r\n" + "A.FD_CUST_CODE,\r\n" + "C.PARENT_NAME,\r\n" + "D.RN)group by PARENT_NAME",
				Double.class);

		List<String> dataOne = new ArrayList<String>();
		List<String> dataTwo = new ArrayList<String>();
		List<String> dataFour = new ArrayList<String>();
		List<Map<String, String>> dataThree = new ArrayList<Map<String, String>>();
		for (int i = 0; i < one.size(); i++) {
			dataOne.add(
					one.get(i).get("PARENT_NAME") == null ? "" : one.get(i).get("PARENT_NAME").toString().substring(2));
			dataTwo.add(one.get(i).get("COUNTALL") == null ? "0"
					: String.format("%.4f", (Double.parseDouble(one.get(i).get("COUNTALL").toString())) / allCust));
			dataFour.add(one.get(i).get("COUNTALL") == null ? "0" : one.get(i).get("COUNTALL").toString());
		}
		dataOne.add("其他");
		dataTwo.add(String.format("%.4f", ((allCust - allSum) / allCust)));
		dataFour.add(String.valueOf((allCust - allSum)));

		for (int i = 0; i < dataTwo.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", dataOne.get(i));
			map.put("value", dataTwo.get(i));
			map.put("valuez", dataFour.get(i));
			dataThree.add(map);
		}

		result.add(dataOne);
		result.add(dataThree);
		return result;
	}

	/**
	 * @warnIndustry 预警行业
	 * @return
	 */
	@RequestMapping(value = "warnIndustry", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> warnIndustry() throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<Map<String, Object>> one = jdbc.queryForList("SELECT \r\n" + "PARENT_NAME,\r\n" + "RN,QTY as COUNTALL\r\n"
				+ "FROM\r\n" + "(\r\n" + "SELECT\r\n" + "PARENT_NAME,\r\n" + "QTY,\r\n"
				+ "ROW_NUMBER()OVER(ORDER BY QTY DESC) RN\r\n" + "FROM\r\n" + "(\r\n" + "SELECT\r\n"
				+ "C.PARENT_NAME,\r\n" + "COUNT(*) QTY\r\n" + "FROM t_aft_warn_info_distinct A\r\n"
				+ "JOIN NS_BP_MASTER B\r\n" + "ON A.LESSEE_ID=B.FD_BP_CODE\r\n" + "JOIN V_NS_ZGC_FATHER_LEVEL C\r\n"
				+ "ON B.FD_SEGMENT_INDUSTRY=C.VALUE_CODE\r\n"
				+ " WHERE 1=1 and push_status = '010'--AND TO_CHAR(A.FD_FINAL_DATE,'YYYYMM')=TO_CHAR(SYSDATE,'YYYYMM')-1\r\n"
				+ "GROUP BY \r\n" + "C.PARENT_NAME\r\n" + "))\r\n" + "WHERE RN <=5\r\n" + "");
		Double allCust = jdbc.queryForObject(
				"select count(*) from t_aft_warn_info_distinct where    push_status = '010'", Double.class);

		Double allSum = jdbc.queryForObject("SELECT \r\n" + "sum(QTY)\r\n" + "FROM\r\n" + "(\r\n" + "SELECT\r\n"
				+ "PARENT_NAME,\r\n" + "QTY,\r\n" + "ROW_NUMBER()OVER(ORDER BY QTY DESC) RN\r\n" + "FROM\r\n" + "(\r\n"
				+ "SELECT\r\n" + "C.PARENT_NAME,\r\n" + "COUNT(*) QTY\r\n" + "FROM t_aft_warn_info_distinct A\r\n"
				+ "JOIN NS_BP_MASTER B\r\n" + "ON A.LESSEE_ID=B.FD_BP_CODE\r\n" + "JOIN V_NS_ZGC_FATHER_LEVEL C\r\n"
				+ "ON B.FD_SEGMENT_INDUSTRY=C.VALUE_CODE\r\n"
				+ "WHERE 1=1 and push_status = '010'--AND TO_CHAR(A.FD_FINAL_DATE,'YYYYMM')=TO_CHAR(SYSDATE,'YYYYMM')-1\r\n"
				+ "GROUP BY \r\n" + "C.PARENT_NAME\r\n" + "))\r\n" + "WHERE RN <=5\r\n" + "", Double.class);

		List<String> dataOne = new ArrayList<String>();
		List<String> dataTwo = new ArrayList<String>();
		List<String> dataFour = new ArrayList<String>();
		List<Map<String, String>> dataThree = new ArrayList<Map<String, String>>();
		for (int i = 0; i < one.size(); i++) {
			dataOne.add(
					one.get(i).get("PARENT_NAME") == null ? "" : one.get(i).get("PARENT_NAME").toString().substring(2));
			dataTwo.add(one.get(i).get("COUNTALL") == null ? "0"
					: String.format("%.4f", (Double.parseDouble(one.get(i).get("COUNTALL").toString())) / allCust));
			dataFour.add(one.get(i).get("COUNTALL") == null ? "0" : one.get(i).get("COUNTALL").toString());
		}
		dataOne.add("其他");
		dataTwo.add(String.format("%.4f", ((allCust - allSum) / allCust)));
		dataFour.add(String.valueOf(allCust - allSum));

		for (int i = 0; i < dataTwo.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", dataOne.get(i));
			map.put("value", dataTwo.get(i));
			map.put("valuez", dataFour.get(i));
			dataThree.add(map);
		}

		result.add(dataOne);
		result.add(dataThree);
		return result;
	}

	/**
	 * @newRadar
	 * @return
	 */
	@RequestMapping(value = "debtReport", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> debtReport() throws Exception {
		List<Map<String, Object>> result = jdbc
				.queryForList("select sumNum,mothCount,((sumNum-mothCount)/mothCount)bl from ( \r\n"
						+ "                select to_char(FD_FINAL_DATE,'yyyyMM'),count(to_char(FD_FINAL_DATE,'yyyyMM')) as sumNum, \r\n"
						+ "                            ( select sumNum from ( \r\n"
						+ "                select to_char(FD_FINAL_DATE,'yyyyMM'),count(to_char(FD_FINAL_DATE,'yyyyMM')) as sumNum, \r\n"
						+ "                            (select count(*) from ns_debt_rating where FD_RATING_VAILD='1' and fd_rating_status='020') as mothCount from ns_debt_rating where FD_RATING_VAILD='1' and fd_rating_status='020' \r\n"
						+ "                            and to_char(FD_FINAL_DATE,'yyyyMM') = to_char(add_months(sysdate,-1),'yyyyMM') \r\n"
						+ "                            group by to_char(FD_FINAL_DATE,'yyyyMM')) ) mothCount from ns_debt_rating where FD_RATING_VAILD='1' and fd_rating_status='020' \r\n"
						+ "                            and to_char(FD_FINAL_DATE,'yyyyMM') = to_char(sysdate,'yyyyMM') \r\n"
						+ "                            group by to_char(FD_FINAL_DATE,'yyyyMM'))   ");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map=result.get(0);
			return map;
		} catch (Exception e) {
			return map;
		}
	}

	/**
	 * @newRadar
	 * @return
	 */
	@RequestMapping(value = "debtDesBl", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> debtDesBl() throws Exception {
		List<Map<String, Object>> result = jdbc.queryForList("SELECT FD_FINAL_level, count(*) as num,\r\n"
				+ "			 		       round(count(*) / \r\n"
				+ "			 		             (SELECT COUNT(*) FROM ns_debt_rating WHERE FD_RATING_VAILD='1' and fd_rating_status='020'), \r\n"
				+ "			 		             4)*100 Bl \r\n" + "             FROM ns_debt_rating \r\n"
				+ "            WHERE FD_RATING_VAILD='1' and fd_rating_status='020'  and to_char(fd_final_date,'yyyyMM')=to_char(sysdate,'yyyyMM')  \r\n"
				+ "            GROUP BY FD_FINAL_level \r\n" + "            order by Bl desc,FD_FINAL_level asc");

		return result;
	}

	/**
	 * @newRadar
	 * @return
	 */
	@RequestMapping(value = "debtScoHighLow", method = RequestMethod.GET)
	@ResponseBody
	public List<String> debtScoHighLow() throws Exception {
		List<String> result = jdbc.queryForList("select des from (\r\n" + 
				"select des ,ROWNUM rn from (\r\n" + 
				"select * from (\r\n" + 
				"select (case\r\n" + 
				"         when FD_XJL_SCO > FD_ZLW_SCO and FD_XJL_SCO > FD_ZXCS_SCO and\r\n" + 
				"              FD_XJL_SCO > FD_KHXY_SCO then\r\n" + 
				"          round(FD_XJL_SCO, 2)\r\n" + 
				"         when FD_ZLW_SCO > FD_XJL_SCO and FD_ZLW_SCO > FD_ZXCS_SCO and\r\n" + 
				"              FD_ZLW_SCO > FD_KHXY_SCO then\r\n" + 
				"          round(FD_ZLW_SCO, 2)\r\n" + 
				"         when FD_ZXCS_SCO > FD_XJL_SCO and FD_ZXCS_SCO > FD_ZLW_SCO and\r\n" + 
				"              FD_ZXCS_SCO > FD_KHXY_SCO then\r\n" + 
				"         round(FD_ZXCS_SCO, 2)\r\n" + 
				"         when FD_KHXY_SCO > FD_XJL_SCO and FD_KHXY_SCO > FD_ZLW_SCO and\r\n" + 
				"              FD_KHXY_SCO > FD_ZXCS_SCO then\r\n" + 
				"         round(FD_KHXY_SCO, 2)\r\n" + 
				"       end)shot,\r\n" + 
				"       (case\r\n" + 
				"         when FD_XJL_SCO > FD_ZLW_SCO and FD_XJL_SCO > FD_ZXCS_SCO and\r\n" + 
				"              FD_XJL_SCO > FD_KHXY_SCO then\r\n" + 
				"          '现金流保障倍数(' || round(FD_XJL_SCO, 2) || '分)'\r\n" + 
				"         when FD_ZLW_SCO > FD_XJL_SCO and FD_ZLW_SCO > FD_ZXCS_SCO and\r\n" + 
				"              FD_ZLW_SCO > FD_KHXY_SCO then\r\n" + 
				"          '租赁物保障倍数(' || round(FD_ZLW_SCO, 2) || '分)'\r\n" + 
				"         when FD_ZXCS_SCO > FD_XJL_SCO and FD_ZXCS_SCO > FD_ZLW_SCO and\r\n" + 
				"              FD_ZXCS_SCO > FD_KHXY_SCO then\r\n" + 
				"          '增信措施保障倍数(' || round(FD_ZXCS_SCO, 2) ||  '分)'\r\n" + 
				"         when FD_KHXY_SCO > FD_XJL_SCO and FD_KHXY_SCO > FD_ZLW_SCO and\r\n" + 
				"              FD_KHXY_SCO > FD_ZXCS_SCO then\r\n" + 
				"          '客户信用保障倍数(' || round(FD_KHXY_SCO, 2) || '分)'\r\n" + 
				"       end)des\r\n" + 
				"  from ns_debt_rating\r\n" + 
				" WHERE FD_RATING_VAILD='1' and fd_rating_status='020' \r\n" + 
				")group by shot,des  order by shot desc nulls last ) where ROWNUM>0 and ROWNUM<=1)\r\n" + 
				" union all\r\n" + 
				"select des from (\r\n" + 
				"select des ,ROWNUM rn from (\r\n" + 
				"select des,shot from (\r\n" + 
				"select (case\r\n" + 
				"         when FD_XJL_SCO < FD_ZLW_SCO and FD_XJL_SCO < FD_ZXCS_SCO and\r\n" + 
				"              FD_XJL_SCO < FD_KHXY_SCO then\r\n" + 
				"          round(FD_XJL_SCO, 2)\r\n" + 
				"         when FD_ZLW_SCO < FD_XJL_SCO and FD_ZLW_SCO < FD_ZXCS_SCO and\r\n" + 
				"              FD_ZLW_SCO < FD_KHXY_SCO then\r\n" + 
				"          round(FD_ZLW_SCO, 2)\r\n" + 
				"         when FD_ZXCS_SCO < FD_XJL_SCO and FD_ZXCS_SCO < FD_ZLW_SCO and\r\n" + 
				"              FD_ZXCS_SCO < FD_KHXY_SCO then\r\n" + 
				"         round(FD_ZXCS_SCO, 2)\r\n" + 
				"         when FD_KHXY_SCO < FD_XJL_SCO and FD_KHXY_SCO < FD_ZLW_SCO and\r\n" + 
				"              FD_KHXY_SCO < FD_ZXCS_SCO then\r\n" + 
				"         round(FD_KHXY_SCO, 2)\r\n" + 
				"       end)shot,\r\n" + 
				"       (case\r\n" + 
				"         when FD_XJL_SCO < FD_ZLW_SCO and FD_XJL_SCO < FD_ZXCS_SCO and\r\n" + 
				"              FD_XJL_SCO < FD_KHXY_SCO then\r\n" + 
				"          '现金流保障倍数(' || rtrim(to_char(round(FD_XJL_SCO, 2), 'fm9990.99'), '.') || '分)'\r\n" + 
				"         when FD_ZLW_SCO < FD_XJL_SCO and FD_ZLW_SCO < FD_ZXCS_SCO and\r\n" + 
				"              FD_ZLW_SCO < FD_KHXY_SCO then\r\n" + 
				"          '租赁物保障倍数(' || rtrim(to_char(round(FD_ZLW_SCO, 2), 'fm9990.99'), '.') || '分)'\r\n" + 
				"         when FD_ZXCS_SCO < FD_XJL_SCO and FD_ZXCS_SCO < FD_ZLW_SCO and\r\n" + 
				"              FD_ZXCS_SCO < FD_KHXY_SCO then\r\n" + 
				"          '增信措施保障倍数(' || rtrim(to_char(round(FD_ZXCS_SCO, 2), 'fm9990.99'), '.') ||  '分)'\r\n" + 
				"         when FD_KHXY_SCO < FD_XJL_SCO and FD_KHXY_SCO < FD_ZLW_SCO and\r\n" + 
				"              FD_KHXY_SCO < FD_ZXCS_SCO then\r\n" + 
				"          '客户信用保障倍数(' || rtrim(to_char(round(FD_KHXY_SCO, 2), 'fm9990.99'), '.') || '分)'\r\n" + 
				"       end)des\r\n" + 
				"  from ns_debt_rating\r\n" + 
				" WHERE FD_RATING_VAILD='1' and fd_rating_status='020' \r\n" + 
				" )group by des,shot  order by shot asc nulls last) where ROWNUM>0 and ROWNUM<=1)\r\n" + 
				" ", String.class);

		return result;
	}

	/**
	 * @newRadar
	 * @return
	 */
	@RequestMapping(value = "samePeriod", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> samePeriod() throws Exception {
		List<Map<String, Object>> result = jdbc
				.queryForList("select sumNum, mothCount, round(sumNum / mothCount * 100, 2) bl\r\n"
						+ "  from (select to_char(FD_FINAL_DATE, 'yyyyMM'),\r\n"
						+ "               count(to_char(FD_FINAL_DATE, 'yyyyMM')) as sumNum,\r\n"
						+ "               (select count(*) from ns_main_rating where fd_rating_vaild = '1'  and fd_rating_status='020') as mothCount\r\n"
						+ "          from ns_main_rating\r\n"
						+ "         where FD_RATING_VAILD='1' and fd_rating_status='020'\r\n"
						+ "           and to_char(FD_FINAL_DATE, 'yyyyMM') = to_char(sysdate, 'yyyyMM')\r\n"
						+ "         group by to_char(FD_FINAL_DATE, 'yyyyMM'))");

		return result;
	}

	/**
	 * @主体
	 * @return
	 */
	@RequestMapping(value = "mainDesBl", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> mainDesBl() throws Exception {
		List<Map<String, Object>> result = jdbc
				.queryForList("select levelz as FD_FINAL_level, count(levelz) as num ,\r\n"
						+ "			 		       (round(count(levelz) / \r\n"
						+ "			 		             (SELECT COUNT(*) \r\n"
						+ "			 		                FROM ns_main_rating \r\n"
						+ "			 		               WHERE fd_rating_status = '020' \r\n"
						+ "			 		                 and fd_Rating_vaild = '1' \r\n"
						+ "			 		                 and FD_FINAL_level is not null and  to_char(fd_final_date,'yyyyMM')=to_char(sysdate,'yyyyMM')), \r\n"
						+ "			 		             2) * 100)||'%' Bl \r\n"
						+ "			 		  from (SELECT (case \r\n"
						+ "			 		                 when FD_SCO > 81.69 then \r\n"
						+ "                             'A' \r\n"
						+ "                            when FD_SCO > 77.76 and FD_SCO <= 81.69 then \r\n"
						+ "                             'B' \r\n"
						+ "                            when FD_SCO > 73.81 and FD_SCO <= 77.76 then \r\n"
						+ "                             'C' \r\n"
						+ "                            when FD_SCO > 69.80 and FD_SCO <= 73.81 then \r\n"
						+ "                             'D' \r\n"
						+ "                            when FD_SCO > 65.68 and FD_SCO <= 69.80 then \r\n"
						+ "                             'E' \r\n"
						+ "                            when FD_SCO > 61.34 and FD_SCO <= 65.68 then \r\n"
						+ "                             'F' \r\n"
						+ "                            when FD_SCO > 56.49 and FD_SCO <= 61.34 then \r\n"
						+ "                             'G' \r\n"
						+ "                            when FD_SCO > 50.14 and FD_SCO <= 56.49 then \r\n"
						+ "                             'H' \r\n"
						+ "                            when FD_SCO > 0.00 and FD_SCO <= 50.14 then \r\n"
						+ "                             'I' \r\n"
						+ "                            when FD_SCO = 0 then \r\n"
						+ "                             'J' \r\n" + "                          end) as levelz \r\n"
						+ "                     from ns_main_rating \r\n"
						+ "                    WHERE fd_Rating_vaild = '1' \r\n"
						+ "                      and FD_FINAL_level is not null and to_char(fd_final_date,'yyyyMM')=to_char(sysdate,'yyyyMM')) \r\n"
						+ "            GROUP BY levelz \r\n" + "            order by Bl desc, levelz asc");

		return result;
	}

	/**
	 * @主体
	 * @return
	 */
	@RequestMapping(value = "mainRatingDepart", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> mainRatingDepart() throws Exception {
		List<Map<String, Object>> result = jdbc.queryForList("select (case unit_name when '资产管理部' then '资产部' else unit_name end) as name,\r\n" + 
				"				       count(distinct rating.fd_id) as num,        avg(rating.fd_sco) as sco\r\n" + 
				"				  from FR_AA_USER_PARTITION org  \r\n" + 
				"                      left join ns_bp_master bp on bp.fd_employee_id = org.employee_id   \r\n" + 
				"            left join ns_main_rating rating on rating.fd_cust_code = bp.fd_bp_code  and fd_rating_vaild='1'  and fd_rating_status='020'\r\n" + 
				"            where (unit_name = '大健康事业部' or unit_name = '大智造事业部'     or unit_name = '大数据事业部'     or unit_name = '大环境事业部'\r\n" + 
				"				    or unit_name = '大消费事业部' or unit_name = '资产管理部') \r\n" + 
				"         group by unit_name order by decode(unit_name,'大数据事业部',1),\r\n" + 
				"         decode(unit_name,'大环境事业部',2),decode(unit_name,'大健康事业部',3),\r\n" + 
				"         decode(unit_name,'大智造事业部',4),decode(unit_name,'大消费事业部',5),decode(unit_name,'资产管理部',6)\r\n" + 
				"         \r\n" + 
				"         ");

		List<List<String>> resultArr = new ArrayList<List<String>>();
		List<String> name = new ArrayList<String>();
		List<String> num = new ArrayList<String>();
		List<String> sco = new ArrayList<String>();
		for (Map<String, Object> map : result) {
			name.add(map.get("NAME") == null ? "" : map.get("NAME").toString().substring(0, 3));
			num.add(map.get("NUM") == null ? "" : map.get("NUM").toString());
			sco.add(map.get("SCO") == null ? "" : String.format("%.2f", Double.parseDouble(map.get("SCO").toString())));
		}
		resultArr.add(name);
		resultArr.add(num);
		resultArr.add(sco);

		return resultArr;
	}

	/**
	 * @债项评级主表
	 * @return
	 */
	@RequestMapping(value = "debtRatingDepart", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> debtRatingDepart() throws Exception {
		List<Map<String, Object>> result = jdbc.queryForList("select (case unit_name when '资产管理部' then '资产部' else unit_name end) as name,\r\n" + 
				"				       count(distinct rating.fd_id) as num, \r\n" + 
				"                (case when  to_number(avg(rating.fd_sco)) is null then 0 else to_number(avg(rating.fd_sco)) end) sco\r\n" + 
				"				  from FR_AA_USER_PARTITION org  \r\n" + 
				"                      left join ns_bp_master bp on bp.fd_employee_id = org.employee_id   \r\n" + 
				"            left join ns_debt_rating rating on rating.fd_cust_code = bp.fd_bp_code  and fd_rating_vaild='1'  and fd_rating_status='020'\r\n" + 
				"            where (unit_name = '大健康事业部' or unit_name = '大智造事业部'     or unit_name = '大数据事业部'     or unit_name = '大环境事业部'\r\n" + 
				"				    or unit_name = '大消费事业部' or unit_name = '资产管理部') \r\n" + 
				"         group by unit_name order by decode(unit_name,'大数据事业部',1),\r\n" + 
				"         decode(unit_name,'大环境事业部',2),decode(unit_name,'大健康事业部',3),\r\n" + 
				"         decode(unit_name,'大智造事业部',4),decode(unit_name,'大消费事业部',5),decode(unit_name,'资产管理部',6)");
		List<List<String>> resultArr = new ArrayList<List<String>>();
		List<String> name = new ArrayList<String>();
		List<String> num = new ArrayList<String>();
		List<String> sco = new ArrayList<String>();
		for (Map<String, Object> map : result) {
			name.add(map.get("NAME") == null ? "" : map.get("NAME").toString().substring(0, 3));
			num.add(map.get("NUM") == null ? "0" : map.get("NUM").toString());
			sco.add(map.get("SCO") == null ? "0"
					: String.format("%.2f", Double.parseDouble(map.get("SCO").toString())));
		}
		resultArr.add(name);
		resultArr.add(num);
		resultArr.add(sco);

		return resultArr;
	}

	/**
	 * @债项评级主表
	 * @return
	 */
	@RequestMapping(value = "warnRatingDepart", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> warnRatingDepart() throws Exception {
		List<Map<String, Object>> resultHigh = jdbc.queryForList("select (case unit_name when '资产管理部' then '资产部' else unit_name end) as name, count(warn_level) num\r\n" + 
				"				  from FR_AA_USER_PARTITION org    left join ns_bp_master bp\r\n" + 
				"				    on bp.fd_employee_id = org.employee_id   left join t_aft_warn_info_distinct rating \r\n" + 
				"				    on rating.lessee_id = bp.fd_bp_code and rating.push_status = '010'  left join t_aft_warn_rule rule  \r\n" + 
				"				    on rating.RULE_CODE = rule.rule_code  and rule.warn_level = 'high' \r\n" + 
				"				 WHERE 1=1 \r\n" + 
				"				   and (unit_name = '大健康事业部' or unit_name = '大智造事业部' or unit_name = '大数据事业部' or\r\n" + 
				"				       unit_name = '大环境事业部' or unit_name = '大消费事业部'  or unit_name = '资产管理部')  group by unit_name\r\n" + 
				"				 order by decode(unit_name,'大数据事业部',1),decode(unit_name,'大环境事业部',2),decode(unit_name,'大健康事业部',3),\r\n" + 
				"         decode(unit_name,'大智造事业部',4),decode(unit_name,'大消费事业部',5),decode(unit_name,'资产管理部',6)\r\n" + 
				"         \r\n" + 
				"         ");

		List<Map<String, Object>> resultMed = jdbc.queryForList("select (case unit_name when '资产管理部' then '资产部' else unit_name end) as name, count(warn_level) num\r\n" + 
				"				  from FR_AA_USER_PARTITION org    left join ns_bp_master bp\r\n" + 
				"				    on bp.fd_employee_id = org.employee_id   left join t_aft_warn_info_distinct rating \r\n" + 
				"				    on rating.lessee_id = bp.fd_bp_code and rating.push_status = '010'  left join t_aft_warn_rule rule  \r\n" + 
				"				    on rating.RULE_CODE = rule.rule_code  and rule.warn_level = 'med' \r\n" + 
				"				 WHERE 1=1 \r\n" + 
				"				   and (unit_name = '大健康事业部' or unit_name = '大智造事业部' or unit_name = '大数据事业部' or\r\n" + 
				"				       unit_name = '大环境事业部' or unit_name = '大消费事业部'  or unit_name = '资产管理部')  group by unit_name\r\n" + 
				"				 order by decode(unit_name,'大数据事业部',1),decode(unit_name,'大环境事业部',2),decode(unit_name,'大健康事业部',3),\r\n" + 
				"         decode(unit_name,'大智造事业部',4),decode(unit_name,'大消费事业部',5),decode(unit_name,'资产管理部',6)\r\n" + 
				"         \r\n" + 
				"         ");
		
		List<String> name = jdbc.queryForList("select (case fd_name when '资产管理部' then '资产部' else fd_name end) as name from fr_aa_org org where  (fd_name = '大健康事业部' or fd_name = '大智造事业部' or fd_name = '大数据事业部' or\r\n" + 
				"               fd_name = '大环境事业部' or fd_name = '大消费事业部'  or fd_name = '资产管理部') order by decode(fd_name,'大数据事业部',1),decode(fd_name,'大环境事业部',2),decode(fd_name,'大健康事业部',3),decode(fd_name,'大智造事业部',4),decode(fd_name,'大消费事业部',5)", String.class);
		List<List<String>> resultArr = new ArrayList<List<String>>();
		//List<String> name = new ArrayList<String>();
		List<String> num = new ArrayList<String>();
		List<String> sco = new ArrayList<String>();
		for (int i = 0; i < resultMed.size(); i++) {
			sco.add(resultMed.get(i).get("NUM") == null ? "" : resultMed.get(i).get("NUM").toString());
		}
		for (int i = 0; i < name.size(); i++) {
			name.set(i,name.get(i) == null ? ""
					: name.get(i).substring(0, 3));
		}
		for (int i = 0; i < resultHigh.size(); i++) {
			num.add(resultHigh.get(i).get("NUM") == null ? "" : resultHigh.get(i).get("NUM").toString());
		}
		
		resultArr.add(name);
		resultArr.add(num);
		resultArr.add(sco);
		return resultArr;
	}

	/**
	 * @最多和最少得事业部
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "debtShot", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> debtShot() throws Exception {
		List<Map<String, Object>> result = jdbc.queryForList("select fd_name, count(warn_level) num\r\n"
				+ "          from fr_aa_org org\r\n" + "          left join ns_bp_master bp\r\n"
				+ "            on bp.fd_lease_organization = org.fd_id\r\n"
				+ "          left join t_aft_warn_info_distinct rating\r\n"
				+ "            on rating.lessee_id = bp.fd_bp_code\r\n" + "          left join t_aft_warn_rule rule\r\n"
				+ "            on rating.RULE_CODE = rule.rule_code\r\n"
				+ "         where push_status = '010' and warn_level = 'high'\r\n"
				+ "           and (fd_name = '大健康事业部' or fd_name = '大智造事业部' or\r\n"
				+ "               fd_name = '大数据事业部' or fd_name = '大环境事业部' or\r\n"
				+ "               fd_name = '大消费事业部')\r\n" + "         group by fd_name\r\n"
				+ "         order by num desc\r\n" + "");
		return result;
	}

	/**
	 * @最多和最少得事业部
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "debtShotDe", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> debtShotDe() throws Exception {
		List<Map<String, Object>> result = jdbc
				.queryForList("select fd_name as name,\r\n" + "       count(distinct c.fd_id) num,\r\n"
						+ "       to_number(avg(c.fd_sco)) sco\r\n" + "  from fr_aa_org a\r\n"
						+ "  left join ns_bp_master b\r\n" + "    on a.fd_id = b.fd_lease_organization\r\n"
						+ "  left join ns_debt_rating c\r\n" + "    on b.fd_bp_code = c.fd_cust_code\r\n"
						+ "   and fd_rating_vaild = '1'\r\n" + "   and fd_rating_status = '020'\r\n"
						+ "   and to_char(fd_final_date, 'yyyyMM') = to_char(sysdate, 'yyyyMM')\r\n"
						+ " where (fd_name = '大健康事业部' or fd_name = '大智造事业部' or fd_name = '大数据事业部' or\r\n"
						+ "       fd_name = '大环境事业部' or fd_name = '大消费事业部')\r\n" + " group by fd_name\r\n"
						+ " order by num desc\r\n" + "");
		return result;
	}

	/**
	 * @最多和最少得事业部
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "mainShot", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> mainShot() throws Exception {
		List<Map<String, Object>> result1 = jdbc.queryForList("select fd_name as name,\r\n"
				+ "       count(distinct c.fd_id) num,\r\n" + "       case\r\n"
				+ "         when to_number(avg(c.fd_sco)) is null then\r\n" + "          0\r\n" + "         else\r\n"
				+ "          to_number(avg(c.fd_sco))\r\n" + "       end sco\r\n" + "  from fr_aa_org a\r\n"
				+ "  left join ns_bp_master b\r\n" + "    on a.fd_id = b.fd_lease_organization\r\n"
				+ "  left join ns_main_rating c\r\n" + "    on b.fd_bp_code = c.fd_cust_code\r\n" + "\r\n"
				+ " where (fd_name = '大健康事业部' or fd_name = '大智造事业部' or fd_name = '大数据事业部' or\r\n"
				+ "       fd_name = '大环境事业部' or fd_name = '大消费事业部')\r\n" + "   and FD_RATING_VAILD = '1'\r\n"
				+ "   and fd_rating_status = '020'\r\n"
				+ "   and to_char(sysdate, 'yyyyMM') = to_char(FD_FINAL_DATE, 'yyyyMM')\r\n" + " group by fd_name\r\n"
				+ " order by num desc\r\n" + "");

		List<Map<String, Object>> result2 = jdbc.queryForList("select fd_name as name,\r\n"
				+ "       count(distinct c.fd_id) num,\r\n" + "       case\r\n"
				+ "         when to_number(avg(c.fd_sco)) is null then\r\n" + "          0\r\n" + "         else\r\n"
				+ "          to_number(avg(c.fd_sco))\r\n" + "       end sco\r\n" + "  from fr_aa_org a\r\n"
				+ "  left join ns_bp_master b\r\n" + "    on a.fd_id = b.fd_lease_organization\r\n"
				+ "  left join ns_main_rating c\r\n" + "    on b.fd_bp_code = c.fd_cust_code\r\n" + "\r\n"
				+ " where (fd_name = '大健康事业部' or fd_name = '大智造事业部' or fd_name = '大数据事业部' or\r\n"
				+ "       fd_name = '大环境事业部' or fd_name = '大消费事业部')\r\n" + "   and FD_RATING_VAILD = '1'\r\n"
				+ "   and fd_rating_status = '020'\r\n"
				+ "   and to_char(sysdate, 'yyyyMM') = to_char(FD_FINAL_DATE, 'yyyyMM')\r\n" + " group by fd_name\r\n"
				+ " order by sco desc\r\n" + "");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		result.add(result1.get(0));
		result.add(result2.get(0));
		result.add(result2.get(result2.size() - 1));
		return result;
	}

	/**
	 * @mainDashboard 移动端
	 * @return
	 */
	@RequestMapping(value = "mainDashboardSj", method = RequestMethod.GET)
	@ResponseBody
	public List<String> mainDashboardSj(String sj) throws Exception {
		// 总平均分
		List<String> list = jdbc.queryForList(
				"select fd_value from ns_main_monthreport where fd_date=? order by fd_sort asc", String.class, sj);
		return list;
	}

	/**
	 * @12个月主体变化趋势
	 * @return
	 */
	@RequestMapping(value = "queryMainChangeSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryMainChangeSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from ns_main_change_1 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from ns_main_change_2 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from ns_main_change_3 where fd_date=? order by fd_sort asc", String.class, sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		return result;
	}

	/**
	 * @主体
	 * @return
	 */
	@RequestMapping(value = "mainDesBlSj", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> mainDesBlSj(String sj) throws Exception {
		List<Map<String, Object>> result = jdbc.queryForList(
				"select FD_FINAL_LEVEL,NUM,BL from ns_main_DesBl where fd_date=? order by num desc,fd_final_level asc", sj);
		return result;
	}

	/**
	 * @最多和最少得事业部
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "mainShotSj", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> mainShotSj(String sj) throws Exception {
		List<Map<String, Object>> result = jdbc
				.queryForList("select*from NS_main_Shot where fd_date=? order by fd_sort asc", sj);
		return result;
	}

	/**
	 * @主体等级分析
	 * @return
	 */
	@RequestMapping(value = "queryMainLevelSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryMainLevelSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_Main_Level_0 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from NS_Main_Level_1 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from NS_Main_Level_2 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list4 = jdbc.queryForList(
				"select fd_value from NS_Main_Level_3 where fd_date=? order by fd_sort asc", String.class, sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		result.add(list4);
		return result;
	}

	@RequestMapping(value = "mainIndustrySj", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> mainIndustrySj(String sj) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_main_Industry where fd_date=? order by fd_sort asc", String.class, sj);
		List<Map<String, Object>> list2 = jdbc.queryForList(
				"select name as \"name\",valuez as \"valuez\",value as \"value\" from NS_main_Industry_data where fd_date=? order by fd_sort asc",
				sj);
		result.add(list1);
		result.add(list2);
		return result;
	}

	@RequestMapping(value = "mainRatingDepartSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> mainRatingDepartSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_main_mainRating_Depart_0 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from NS_main_mainRating_Depart_1 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from NS_main_mainRating_Depart_2 where fd_date=? order by fd_sort asc", String.class,
				sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		return result;
	}

	@RequestMapping(value = "newRadarSj", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> newRadarSj(String sj) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<String> list1 = jdbc.queryForList("select fd_value from NS_new_Radar where fd_date=? order by fd_sort asc",
				String.class, sj);
		List<Map<String, Object>> list2 = jdbc.queryForList(
				"select name as \"name\",max as \"max\" from NS_new_Radar_data where fd_date=? order by fd_sort asc",
				sj);
		result.add(list2);
		result.add(list1);
		return result;
	}

	@RequestMapping(value = "newRadarSj1", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> newRadarSj1(String sj) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_new_Radar_1 where fd_date=? order by fd_sort asc", String.class, sj);
		List<Map<String, Object>> list2 = jdbc.queryForList(
				"select name as \"name\",max as \"max\" from NS_new_Radar_data_1 where fd_date=? order by fd_sort asc",
				sj);
		result.add(list2);
		result.add(list1);
		return result;
	}

	@RequestMapping(value = "TyRadarSj", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> TyRadarSj(String sj) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<String> list1 = jdbc.queryForList("select fd_value from NS_Ty_Radar where fd_date=? order by fd_sort asc",
				String.class, sj);
		List<Map<String, Object>> list2 = jdbc.queryForList(
				"select name as \"name\",max as \"max\" from NS_Ty_Radar_data where fd_date=? order by fd_sort asc",
				sj);
		result.add(list2);
		result.add(list1);
		return result;
	}

	@RequestMapping(value = "TyRadarSj1", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> TyRadarSj1(String sj) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_Ty_Radar_1 where fd_date=? order by fd_sort asc", String.class, sj);
		List<Map<String, Object>> list2 = jdbc.queryForList(
				"select name as \"name\",max as \"max\" from NS_Ty_Radar_data_1 where fd_date=? order by fd_sort asc",
				sj);
		result.add(list2);
		result.add(list1);
		return result;
	}

	/**
	 * @最多和最少得事业部
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "debtShotDeSj", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> debtShotDeSj(String sj) throws Exception {
		List<Map<String, Object>> result = jdbc
				.queryForList("select * from NS_debt_ShotDe where fd_date=? order by fd_sort asc", sj);
		return result;
	}

	/**
	 * @主体
	 * @return
	 */
	@RequestMapping(value = "debtDesBlSj", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> debtDesBlSj(String sj) throws Exception {
		List<Map<String, Object>> result = jdbc.queryForList(
				"select FD_FINAL_LEVEL,NUM,BL from ns_debt_DesBl where fd_date=? order by fd_sort asc", sj);
		return result;
	}

	/**
	 * @mainDashboard 移动端
	 * @return
	 */
	@RequestMapping(value = "debtScoHighLowSj", method = RequestMethod.GET)
	@ResponseBody
	public List<String> debtScoHighLowSj(String sj) throws Exception {
		// 总平均分
		List<String> list = jdbc.queryForList(
				"select fd_value from ns_debt_Sco_HighLowl where fd_date=? order by fd_sort asc", String.class, sj);
		return list;
	}

	/**
	 * @mainDashboard 移动端
	 * @return
	 */
	@RequestMapping(value = "debtReportSj", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> debtReportSj(String sj) throws Exception {
		// 总平均分
		List<Map<String, Object>> list = jdbc
				.queryForList("select * from ns_debt_Report where fd_date=? order by fd_sort asc", sj);
		return list.get(0);
	}

	@RequestMapping(value = "debtDashboardSj", method = RequestMethod.GET)
	@ResponseBody
	public List<String> debtDashboardSj(String sj) throws Exception {
		// 总平均分
		List<String> list = jdbc.queryForList(
				"select FD_VALUE from NS_DEBT_MONTHREPORT where fd_date=? order by fd_sort asc", String.class, sj);
		return list;
	}

	/**
	 * @主体等级分析
	 * @return
	 */
	@RequestMapping(value = "queryDebtLevelSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryDebtLevelSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_Debt_Level_0 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from NS_Debt_Level_1 where fd_date=? order by fd_sort asc", String.class, sj);
		result.add(list1);
		result.add(list2);
		return result;
	}

	/**
	 * @12个月主体变化趋势
	 * @return
	 */
	@RequestMapping(value = "queryDebtChangeSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryDebtChangeSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from ns_Debt_change_0 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from ns_Debt_change_1 where fd_date=? order by fd_sort asc", String.class, sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from ns_Debt_change_2 where fd_date=? order by fd_sort asc", String.class, sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		return result;
	}

	@RequestMapping(value = "debtRatingDepartSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> debtRatingDepartSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_Debt_DebtRating_Depart_0 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from NS_Debt_DebtRating_Depart_1 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from NS_Debt_DebtRating_Depart_2 where fd_date=? order by fd_sort asc", String.class,
				sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		return result;
	}

	@RequestMapping(value = "queryWarnNumSj", method = RequestMethod.GET)
	@ResponseBody
	public List<String> queryWarnNumSj(String sj) throws Exception {
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_query_WarnNum where fd_date=? order by fd_sort asc", String.class, sj);
		return list1;
	}

	@RequestMapping(value = "disposalNumSj", method = RequestMethod.GET)
	@ResponseBody
	public List<String> disposalNumSj(String sj) throws Exception {
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_disposal_Num where fd_date=? order by fd_sort asc", String.class, sj);
		return list1;
	}

	@RequestMapping(value = "debtShotSj", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> debtShotSj(String sj) throws Exception {
		List<Map<String, Object>> list1 = jdbc
				.queryForList("select * from NS_debt_Shot where fd_date=? order by fd_sort asc", sj);
		return list1;
	}

	
	@RequestMapping(value = "queryWarnSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryWarnSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_query_Warn_0 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from NS_query_Warn_1 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from NS_query_Warn_2 where fd_date=? order by fd_sort asc", String.class,
				sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		return result;
	}
	@RequestMapping(value = "queryWarnTrendSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> queryWarnTrendSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_Warn_Trend_0 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from NS_Warn_Trend_1 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from NS_Warn_Trend_2 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list4 = jdbc.queryForList(
				"select fd_value from NS_Warn_Trend_3 where fd_date=? order by fd_sort asc", String.class,
				sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		result.add(list4);
		return result;
	}
	@RequestMapping(value = "warnIndustrySj", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> warnIndustrySj(String sj) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_warn_Industry where fd_date=? order by fd_sort asc", String.class, sj);
		List<Map<String, Object>> list2 = jdbc.queryForList(
				"select name as \"name\",valuez as \"valuez\",value as \"value\" from NS_warn_Industry_data where fd_date=? order by fd_sort asc",
				sj);
		result.add(list1);
		result.add(list2);
		return result;
	}
	
	
	@RequestMapping(value = "warnRatingDepartSj", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> warnRatingDepartSj(String sj) throws Exception {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> list1 = jdbc.queryForList(
				"select fd_value from NS_warn_Depart_0 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list2 = jdbc.queryForList(
				"select fd_value from NS_warn_Depart_1 where fd_date=? order by fd_sort asc", String.class,
				sj);
		List<String> list3 = jdbc.queryForList(
				"select fd_value from NS_warn_Depart_2 where fd_date=? order by fd_sort asc", String.class,
				sj);
		result.add(list1);
		result.add(list2);
		result.add(list3);
		return result;
	}
}
