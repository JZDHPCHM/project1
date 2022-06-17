package gbicc.irs.report.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.core.support.FileDownloader;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.protocol.response.support.ResponseWrapperBuilder;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import gbicc.irs.report.service.impl.OfficeTemplateService;

@Controller
@RequestMapping("/irs/reportMain")
public class MainReport {

	@Autowired
	OfficeTemplateService officeTemplate;
	@Autowired
	JdbcTemplate jdbc;

	public String warningList(Integer start, Integer end, String sql) {
		sql = "select * from (select t.*,rownum as rn from (" + sql + ") t where rownum<=" + end + ") where rn>"
				+ start;
		return sql;
	}

	public Long count(String sql) {
		sql = "select count(*) from(" + sql + ")";
		Long count = jdbc.queryForObject(sql, Long.class);
		return count;
	}

	/**
	 * 主体-事业部维度
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryReportMain", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> queryReportMain(Integer page, Integer rows, String date1, String date2)
			throws Exception {
		String procedure = "{call P_NS_ZGC_RATING_PARITION('"+date1+"')}";
		jdbc.execute(procedure);
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDate(date2, date1, "data_date"));
		String shot = "order by fd_final_level ";
		String sql = "select * from (SELECT \r\n" + "       FD_FINAL_LEVEL,\r\n"
				+ "       SUM(大数据事业部客户数目) AS 大数据事业部客户数目,\r\n" + "       SUM(大数据事业部数目占比) AS 大数据事业部数目占比,\r\n"
				+ "       SUM(大数据事业部债项余额) AS 大数据事业部债项余额,\r\n" + "       SUM(大数据事业部余额占比) AS 大数据事业部余额占比,\r\n"
				+ "       SUM(大环境事业部客户数目) AS 大环境事业部客户数目,\r\n" + "       SUM(大环境事业部数目占比) AS 大环境事业部数目占比,\r\n"
				+ "       SUM(大环境事业部债项余额) AS 大环境事业部债项余额,\r\n" + "       SUM(大环境事业部余额占比) AS 大环境事业部余额占比,\r\n"
				+ "       SUM(大健康事业部客户数目) AS 大健康事业部客户数目,\r\n" + "       SUM(大健康事业部数目占比) AS 大健康事业部数目占比,\r\n"
				+ "       SUM(大健康事业部债项余额) AS 大健康事业部债项余额,\r\n" + "       SUM(大健康事业部余额占比) AS 大健康事业部余额占比,\r\n"
				+ "       SUM(大智造事业部客户数目) AS 大智造事业部客户数目,\r\n" + "       SUM(大智造事业部数目占比) AS 大智造事业部数目占比,\r\n"
				+ "       SUM(大智造事业部债项余额) AS 大智造事业部债项余额,\r\n" + "       SUM(大智造事业部余额占比) AS 大智造事业部余额占比,\r\n"
				+ "       SUM(大消费事业部客户数目) AS 大消费事业部客户数目,\r\n" + "       SUM(大消费事业部数目占比) AS 大消费事业部数目占比,\r\n"
				+ "       SUM(大消费事业部债项余额) AS 大消费事业部债项余额,\r\n" + "       SUM(大消费事业部余额占比) AS 大消费事业部余额占比,\r\n"
				+ "       SUM(客户数目) AS 客户数目,\r\n" + "       SUM(数目占比) AS 数目占比,\r\n" + "       SUM(债项余额) AS 债项余额,\r\n"
				+ "       SUM(余额占比) AS 余额占比\r\n" + "  FROM T_NS_MAIN_RATING_PARTITION_SUM\r\n" + " where 1=1 "
				+ "" + " " + " GROUP BY ROLLUP(FD_FINAL_LEVEL) " + shot + " )\r\n" + " ";
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String, Object>> list = jdbc.queryForList(sqlpage);
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String, Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}

	/**
	 * 主体-国标行业前五大
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryReportNational", method = RequestMethod.GET)
	@ResponseBody
	public List<List<Map<String, Object>>> queryReportNational(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		String procedure = "{call P_NS_MAIN_RATING_SEGMENT('"+request.getParameter("date1")+"')}";
		jdbc.execute(procedure);
		sqlQuery.append(sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "data_date"));
		String shot = "order by fd_final_level ";
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();

		List<Map<String, Object>> listNational = jdbc
				.queryForList("SELECT * FROM NS_MAIN_RATING_SEGMENT_FIVE where 1=1 " + "" + " order by rn");
		Map<String, Object> map = new HashMap<String, Object>();

		if (listNational.size() < 5) {
			map = new HashMap<String, Object>();
			for (int i = listNational.size(); i < 6; i++) {
				map.put("RN", i);
				map.put("PARENT_NAME", "无");
				listNational.add(map);
			}
		}
		list.add(listNational);

		return list;
	}

	@RequestMapping(value = "queryReportNationalData", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> queryReportNationalData(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "data_date"));
		String shot = "order by fd_final_level ";
		List<Map<String, Object>> listData = jdbc.queryForList("select * from (SELECT\r\n" + "fd_final_level,\r\n"
				+ "SUM(one_qty) AS one_qty,\r\n" + "SUM(one_qty_zb) AS one_qty_zb,\r\n" + "SUM(one_amt) AS one_amt,\r\n"
				+ "SUM(one_amt_zb)AS one_amt_zb,\r\n" + "SUM(two_qty)AS two_qty,\r\n"
				+ "SUM(two_qty_zb)AS two_qty_zb,\r\n" + "SUM(two_amt)AS two_amt,\r\n"
				+ "SUM(two_amt_zb)AS two_amt_zb,\r\n" + "SUM(three_qty)AS three_qty,\r\n"
				+ "SUM(three_qty_zb)AS three_qty_zb,\r\n" + "SUM(three_amt)AS three_amt,\r\n"
				+ "SUM(three_amt_zb)AS three_amt_zb,\r\n" + "SUM(four_qty)AS four_qty,\r\n"
				+ "SUM(four_qty_zb)AS four_qty_zb,\r\n" + "SUM(four_amt)AS four_amt,\r\n"
				+ "SUM(four_amt_zb)AS four_amt_zb,\r\n" + "SUM(five_qty)AS five_qty,\r\n"
				+ "SUM(five_qty_zb)AS five_qty_zb,\r\n" + "SUM(five_amt)AS five_amt,\r\n"
				+ "SUM(five_amt_zb)AS five_amt_zb,\r\n" + "SUM(sum_qty)AS sum_qty,\r\n"
				+ "SUM(sum_qty_zb)AS sum_qty_zb,\r\n" + "SUM(sum_amt)AS sum_amt,\r\n"
				+ "SUM(sum_amt_zb)AS sum_amt_zb\r\n" + "FROM T_NS_MAIN_RATING_SEGMENT\r\n" + " where 1=1 "  
				+ " " + "GROUP BY ROLLUP(fd_final_level) " + shot + " )");

		return listData;
	}

	/**
	 * @整体承租人评级分布
	 * @return
	 */
	@RequestMapping(value = "queryReportOverall", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> queryReportOverall(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		String procedure = "{call P_MAIN_RATING_TABLE_ZHUTI('"+request.getParameter("date1")+"')}";
		jdbc.execute(procedure);
		sqlQuery.append(sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "data_date"));
		String shot = " order by 客户评级";
		List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM (\r\n" + "SELECT \r\n" + "客户评级,\r\n"
				+ "SUM(客户数目)     AS 客户数目,\r\n" + "SUM(比上期增减额) AS 比上期增减额,\r\n" + "SUM(比年初增减额) AS 比年初增减额,\r\n"
				+ "SUM(客户数目占比) AS 客户数目占比,\r\n" + "SUM(占比比上期变动)AS 占比比上期变动,\r\n" + "SUM(占比比年初变动)AS 占比比年初变动,\r\n"
				+ "SUM(授信余额)     AS  授信余额,\r\n" + "SUM(余额比上期增减额)AS 余额比上期增减额,\r\n" + "SUM(余额比年初增减额)AS 余额比年初增减额,\r\n"
				+ "SUM(授信余额占比) AS 授信余额占比,\r\n" + "SUM(余额占比比上期变动)AS 余额占比比上期变动,\r\n" + "SUM(余额占比比年初变动)AS 余额占比比年初变动\r\n"
				+ "FROM T_MAIN_RATING_ZT_TABLE\r\n" + " where 1=1 " + " " + " " + "GROUP BY ROLLUP(客户评级) " + shot
				+ " )  ");
		return list;
	}

	/**
	 * @主体-生产型-底表
	 * @return
	 */
	@RequestMapping(value = "productionBaseTable", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> productionBaseTable() throws Exception {
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("select * from V_NS_ZGC_ZTSCXDB where 评级模板 = '生产型'");
		// key集合
		// 遍历结果值
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> key = new ArrayList<String>();
		Map<String, Object> map = list.get(0);

		// value集合
		for (String keyStr : map.keySet()) {
			key.add(keyStr);
		}
		result.add(key);
		return result;
	}

	@RequestMapping(value = "productionBaseTableS", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> productionBaseTableS(Integer page, Integer rows, String date1,
			String date2) throws Exception {

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDateTwo(date2, date1, "data_date"));
		String sql = "select * from V_NS_ZGC_ZTSCXDB where 评级模板 = '生产型'" + sqlQuery;
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String, Object>> list = jdbc.queryForList(sqlpage);
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String, Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}

	/**
	 * @主体-服务型-底表
	 * @return
	 */
	@RequestMapping(value = "productionService", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> productionService() throws Exception {
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("select * from V_ns_zgc_ztfwxdb where 评级模板 = '服务型'");
		// key集合
		// 遍历结果值
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> key = new ArrayList<String>();
		Map<String, Object> map = list.get(0);

		// value集合
		for (String keyStr : map.keySet()) {
			key.add(keyStr);
		}
		result.add(key);
		return result;
	}

	@RequestMapping(value = "productionServiceS", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> productionServiceS(Integer page, Integer rows, String date1,
			String date2) throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDateTwo(date2, date1, "data_date"));
		String sql = "select * from V_ns_zgc_ztfwxdb where 评级模板 = '服务型' " + sqlQuery;
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String, Object>> list = jdbc.queryForList(sqlpage);
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String, Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}

	/**
	 * @主体-新建型-底表
	 * @return
	 */
	@RequestMapping(value = "newTypeData", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> newTypeData() throws Exception {
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("select * from V_ns_zgc_ztxjxdt where 评级模板 = '新建型'");
		// key集合
		// 遍历结果值
		List<Object> result = new ArrayList<Object>();
		List<String> key = new ArrayList<String>();
		Map<String, Object> map = list.get(0);
		// value集合
		for (String keyStr : map.keySet()) {
			key.add(keyStr);
		}
		result.add(key);
		return result;
	}

	@RequestMapping(value = "newTypeDataS", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> newTypeDataS(Integer page, Integer rows, String date1, String date2)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDateTwo(date2, date1, "data_date"));
		// sql查询
		String sql = "select * from V_ns_zgc_ztxjxdt where 评级模板 = '新建型' " + sqlQuery;
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String, Object>> list = jdbc.queryForList(sqlpage);
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String, Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}

	@RequestMapping(value = "debtDibiao", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper<Map<String, Object>> debtDibiao(Integer page, Integer rows, String date1, String date2)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDateTwo(date2, date1, "data_date"));

		String sql = "select * from V_T_NS_ZGC_ZXDB where 1=1 " + sqlQuery;
		Integer size = rows;
		Integer number = page;
		Integer start = size * number - size;
		Integer end = size * number;
		String sqlpage = warningList(start, end, sql);
		List<Map<String, Object>> list = jdbc.queryForList(sqlpage);
		Integer totalpager = (int) Math.ceil((double) count(sql) / (double) size);
		ResponseWrapper<Map<String, Object>> re = ResponseWrapperBuilder.query(list);
		re.getResponse().setTotalPages((long) totalpager);
		re.getResponse().setTotalRows(count(sql));
		return re;
	}

	@RequestMapping(value = "divisionDebtTitle", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> divisionDebtTitle() throws Exception {
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("select * from V_ZX_ZGC_RATING_PARTITION_SUM");
		// key集合
		// 遍历结果值
		List<Object> result = new ArrayList<Object>();
		List<String> key = new ArrayList<String>();
		Map<String, Object> map = list.get(0);
		// value集合
		for (String keyStr : map.keySet()) {
			key.add(keyStr);
		}
		result.add(key);
		return result;
	}

	@RequestMapping(value = "divisionDebt", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> divisionDebt(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "data_date"));
		String procedure = "{call P_NS_ZX_RATING_PARITION('"+request.getParameter("date1")+"')}";
		jdbc.execute(procedure);
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList(
				"select * from (\r\n" + "SELECT \r\n" + "FD_FINAL_LEVEL,\r\n" + "SUM(大数据事业部债项数目)AS 大数据事业部客户数目,\r\n"
						+ "SUM(大数据事业部数目占比)AS 大数据事业部数目占比, \r\n" + "SUM(大数据事业部债项余额)AS 大数据事业部债项余额,\r\n"
						+ "SUM(大数据事业部余额占比)AS 大数据事业部余额占比,\r\n" + "SUM(大环境事业部债项数目)AS 大环境事业部客户数目,\r\n"
						+ "SUM(大环境事业部数目占比)AS 大环境事业部数目占比,\r\n" + "SUM(大环境事业部债项余额)AS 大环境事业部债项余额,\r\n"
						+ "SUM(大环境事业部余额占比)AS 大环境事业部余额占比,\r\n" + "SUM(大健康事业部债项数目)AS 大健康事业部客户数目,\r\n"
						+ "SUM(大健康事业部数目占比)AS 大健康事业部数目占比,\r\n" + "SUM(大健康事业部债项余额)AS 大健康事业部债项余额,\r\n"
						+ "SUM(大健康事业部余额占比)AS 大健康事业部余额占比,\r\n" + "SUM(大智造事业部债项数目)AS 大智造事业部客户数目,\r\n"
						+ "SUM(大智造事业部数目占比)AS 大智造事业部数目占比,\r\n" + "SUM(大智造事业部债项余额)AS 大智造事业部债项余额,\r\n"
						+ "SUM(大智造事业部余额占比)AS 大智造事业部余额占比,\r\n" + "SUM(大消费事业部债项数目)AS 大消费事业部客户数目,\r\n"
						+ "SUM(大消费事业部数目占比)AS 大消费事业部数目占比,\r\n" + "SUM(大消费事业部债项余额)AS 大消费事业部债项余额,\r\n"
						+ "SUM(大消费事业部余额占比)AS 大消费事业部余额占比,\r\n" + "SUM(客户数目) AS 债项数目,\r\n" + "SUM(数目占比) AS 数目占比,\r\n"
						+ "SUM(债项余额) AS 债项余额,\r\n" + "SUM(余额占比) AS 余额占比\r\n" + "FROM T_NS_ZGC_RATING_PARTITION_SUM\r\n"
						+ " where 1=1 " + "" + " " + "GROUP BY ROLLUP(FD_FINAL_LEVEL) ) ");
		return list;
	}

	@RequestMapping(value = "divisionAsWholeTitle", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> divisionAsWholeTitle() throws Exception {
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("select * from T_MAIN_RATING_ZX_TABLE");
		// key集合
		// 遍历结果值
		List<Object> result = new ArrayList<Object>();
		List<String> key = new ArrayList<String>();
		Map<String, Object> map = list.get(0);
		// value集合
		for (String keyStr : map.keySet()) {
			key.add(keyStr);
		}
		result.add(key);
		return result;
	}

	@RequestMapping(value = "divisionAsWhole", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> divisionAsWhole(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "data_date"));
		String procedure = "{call P_MAIN_RATING_TABLE_ZX_ZHENGTI('"+request.getParameter("date1")+"')}";
		jdbc.execute(procedure);
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("select * from (select \r\n" + "债项评级,\r\n"
				+ "SUM(债项数目)       AS 客户数目,\r\n" + "SUM(比上期增减额)   AS 比上期增减额,\r\n" + "SUM(比年初增减额)   AS 比年初增减额,\r\n"
				+ "SUM(债项数目占比)   AS 客户数目占比,\r\n" + "SUM(占比比上期变动) AS 占比比上期变动,\r\n" + "SUM(占比比年初变动) AS 占比比年初变动,\r\n"
				+ "SUM(债项余额)       AS 授信余额,\r\n" + "SUM(余额比上期增减额)AS 余额比上期增减额,\r\n" + "SUM(余额比年初增减额)AS 余额比年初增减额,\r\n"
				+ "SUM(债项余额占比)    AS 授信余额占比,\r\n" + "SUM(余额占比比上期变动)AS 余额占比比上期变动,\r\n" + "SUM(余额占比比年初变动)AS 余额占比比年初变动\r\n"
				+ "FROM T_MAIN_RATING_ZX_TABLE\r\n" + " where 1=1 " + "" + " " + "GROUP BY ROLLUP (债项评级)) ");
		return list;
	}

	@RequestMapping(value = "debtGbhyTitle", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> debtGbhyTitle() throws Exception {
		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM V_NS_ZGC_RATING_PARTITION_GBHY");
		// key集合
		// 遍历结果值
		List<Object> result = new ArrayList<Object>();
		List<String> key = new ArrayList<String>();
		Map<String, Object> map = list.get(0);
		// value集合
		for (String keyStr : map.keySet()) {
			key.add(keyStr);
		}
		result.add(key);
		return result;
	}

	/**
	 * 主体-国标行业前五大
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryReportNationalDebt", method = RequestMethod.GET)
	@ResponseBody
	public List<List<Map<String, Object>>> queryReportNationalDebt(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		String procedure = "{call P_NS_ZX_RATING_SEGMENT('"+request.getParameter("date1")+"')}";
		jdbc.execute(procedure);
		sqlQuery.append(sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "data_date"));
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();

		List<Map<String, Object>> listNational = jdbc
				.queryForList("SELECT * FROM NS_ZGC_RATING_SEGMENT_HIS5 where 1=1 " + "" + " order by rn");
		Map<String, Object> map = new HashMap<String, Object>();

		if (listNational.size() < 5) {
			map = new HashMap<String, Object>();
			for (int i = listNational.size(); i < 6; i++) {
				map.put("RN", i);
				map.put("PARENT_NAME", "无");
				listNational.add(map);
			}
		}
		list.add(listNational);

		return list;
	}

	@RequestMapping(value = "debtGbhy", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> debtGbhy(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(sqlParDate(request.getParameter("date2"), request.getParameter("date1"), "data_date"));

		// sql查询
		List<Map<String, Object>> list = jdbc.queryForList("select * from (\r\n" + "select\r\n" +

				"fd_final_level, \r\n" + "SUM(one_qty) AS one_qty, \r\n" + "SUM(one_qty_zb) AS one_qty_zb, \r\n"
				+ "SUM(one_amt) AS one_amt , \r\n" + "SUM(one_amt_zb) AS one_amt_zb, \r\n"
				+ "SUM(two_qty) AS two_qty, \r\n" + "SUM(two_qty_zb) AS two_qty_zb, \r\n"
				+ "SUM(two_amt)AS two_amt, \r\n" + "SUM(two_amt_zb)AS two_amt_zb, \r\n"
				+ "SUM(three_qty)AS three_qty, \r\n" + "SUM(three_qty_zb)AS three_qty_zb, \r\n"
				+ "SUM(three_amt)AS three_amt, \r\n" + "SUM(three_amt_zb) AS three_amt_zb, \r\n"
				+ "SUM(four_qty) AS four_qty, \r\n" + "SUM(four_qty_zb) AS four_qty_zb, \r\n"
				+ "SUM(four_amt)AS four_amt, \r\n" + "SUM(four_amt_zb)AS four_amt_zb, \r\n"
				+ "SUM(five_qty) AS five_qty, \r\n" + "SUM(five_qty_zb) AS five_qty_zb, \r\n"
				+ "SUM(five_amt) AS five_amt, \r\n" + "SUM(five_amt_zb) AS five_amt_zb, \r\n"
				+ "SUM(sum_qty)AS sum_qty, \r\n" + "SUM(sum_qty_zb)AS sum_qty_zb, \r\n" + "SUM(sum_amt)AS sum_amt, \r\n"
				+ "SUM(sum_amt_zb) AS sum_amt_zb\r\n" + "FROM T_NS_ZGC_RATING_PARTITION_GBHY \r\n" + " where 1=1 "
				+ "" + " " + "GROUP BY ROLLUP (fd_final_level)\r\n" + ") ");
		return list;
	}

	/**
	 * 主体-生产型-底表
	 * 
	 * @return
	 */
	@RequestMapping("principalProductionType.html")
	@MenuContributionItem("menuitem.irs.report.financialAccount")
	public String principalProductionType() {
		return "gbicc/irs/report/view/principalProductionType.html";
	}

	/**
	 * 主体-服务型-底表
	 * 
	 * @return
	 */
	@RequestMapping("serviceOriented.html")
	@MenuContributionItem("menuitem.irs.report.serviceOriented")
	public String serviceOriented() {
		return "gbicc/irs/report/view/serviceOriented.html";
	}

	/**
	 * 主体-新建型-底表
	 * 
	 * @return
	 */
	@RequestMapping("newType.html")
	@MenuContributionItem("menuitem.irs.report.newType")
	public String newType() {
		return "gbicc/irs/report/view/newType.html";
	}

	/**
	 * 债项-底表
	 * 
	 * @return
	 */

	@RequestMapping("debtBaseList")
	@MenuContributionItem("menuitem.irs.report.debtBaseList")
	public String debtBaseList() {
		return "gbicc/irs/report/view/debtBaseList.html";
	}

	/**
	 * 主体-承租人评级分布
	 * 
	 * @return
	 */
	@RequestMapping("tenantRatingDistribution.html")
	@MenuContributionItem("menuitem.irs.report.tenantRatingDistribution")
	public String tenantRatingDistribution() {
		return "gbicc/irs/report/view/tenantRatingDistribution.html";
	}

	/**
	 * 主体-事业部维度
	 * 
	 * @return
	 */
	@RequestMapping("mainDivisionalDimension.html")
	@MenuContributionItem("menuitem.irs.report.queryReportMain")
	public String mainDivisionalDimension() {
		return "gbicc/irs/report/view/mainDivisionalDimension.html";
	}

	/**
	 * 主体-国标行业
	 * 
	 * @return
	 */
	@RequestMapping("mainNationalStandardIndustry.html")
	@MenuContributionItem("menuitem.irs.report.queryReportNational")
	public String mainNationalStandardIndustry() {
		return "gbicc/irs/report/view/mainNationalStandardIndustry.html";
	}

	/**
	 * 债项-整体债项评级分布
	 * 
	 * @return
	 */
	@RequestMapping("debtRatingDistribution")
	@MenuContributionItem("menuitem.irs.report.debtReportAsWhole")
	public String debtRatingDistribution() {
		return "gbicc/irs/report/view/debtRatingDistribution.html";
	}

	/**
	 * 债项-事业部维度
	 * 
	 * @return
	 */
	@RequestMapping("debtDivisionalDimension")
	@MenuContributionItem("menuitem.irs.report.debtReportNational")
	public String debtDivisionalDimension() {
		return "gbicc/irs/report/view/debtDivisionalDimension.html";
	}

	/**
	 * 债项-国标行业
	 * 
	 * @return
	 */
	@RequestMapping("debtNationalStandardIndustry")
	@MenuContributionItem("menuitem.irs.report.debtNationalStandardIndustry")
	public String debtNationalStandardIndustry() {
		return "gbicc/irs/report/view/debtNationalStandardIndustry.html";
	}

	public static String sqlParDate(String date1, String date2, String sqlCol) {
		String sqlQuery = "";
		if (StringUtils.isNotBlank(date2)) {
			sqlQuery += " and " + sqlCol + "='" + date2.replace("-", "").substring(0, 8) + "'";
		}
		return sqlQuery;
	}

	public static String sqlParDateTwo(String date1, String date2, String sqlCol) {
		String sqlQuery = "";
		if (StringUtils.isNotBlank(date1)) {
			sqlQuery += " and " + sqlCol + ">='" + date1.replace("-", "").substring(0, 8) + "'";
		}

		if (StringUtils.isNotBlank(date2)) {
			sqlQuery += " and " + sqlCol + "<'" + formateDate(date2).replace("-", "").substring(0, 8) + "'";
		}
		return sqlQuery;
	}

	public static String formateDateNulls(String strDate) {
		return null;
	}

	public static String formateDate(String strDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, 1);

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH)+ 1;
			String strDay = "";
			if (day < 10) {
				strDay = "0" + day;
			} else {
				strDay = "" + day;
			}
			String strMo = "";
			if (month < 10) {
				strMo = "0" + month;
			} else {
				strMo = "" + month;
			}

			return year + "-" + strMo + "-" + strDay;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@RequestMapping(value = "/export.do",produces = "text/html;charset=utf-8")
	@ResponseBody
	public String htmlTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String name =request.getParameter("name");
		String sj = request.getParameter("sj");
		String frequency = request.getParameter("frequency");
		String[] date = {};
		if(sj.indexOf("至")>-1) {
			date = sj.split("至");
			date[0]=date[0].replace("-", "");
			if(date.length==2) {
				date[1]=formateDate(date[1]).replace("-", "").substring(0, 8);
			}
		}
		Resource resource = new DefaultResourceLoader().getResource("classpath:exportExcel/"+name);
		officeTemplate.export(name,resource, sj,date, frequency);
		return "base/test.html";
	}
	

}
