package gbicc.irs.index.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reportAuto")
public class reportAuto {

	@Autowired
	public ReportAnalysis ras;
	@Autowired
	JdbcTemplate jdbc;

	
	// 每月上午10点15执行
	// 0 15 10 L * ?
	// 0 50 23 L * ?
	//@Scheduled(cron = "0 05 00 1 * ?")
	@RequestMapping(value = "/sendMainMsgSj")
	@ResponseBody
	@Scheduled(cron = "0 55 23 * * ?")
	public void mainRating() throws Exception {
		mainDashboard();
		mainChange();
		mainDesBl();
		mainShot();
		queryMainLevel();
		mainIndustry();
		mainRatingDepart();
		newRadar();
		newRadar1();
		TyRadar();
		TyRadar1();
		debtShotDe();
		debtDesBl();
		debtScoHighLow();
		debtReport();
		debtDashboard();
		queryDebtLevel();
		debtChange();
		debtRatingDepart();
		queryWarnNum();
		disposalNum();
		debtShot();
		queryWarn();
		queryWarnTrend();
		warnIndustry();
		warnRatingDepart();
		
	}

	public void delete(String table,String sj) {
		jdbc.update("delete "+table+" where fd_date=?",sj);
	}
	@RequestMapping(value = "/mainDashboard")
	@ResponseBody
	public void mainDashboard() throws Exception {
		// 主体评级数目
		List<String> title = ras.mainDashboard();
		delete("ns_main_monthreport",date());
		for (int i = 0; i < title.size(); i++) {
			String sql = "insert into ns_main_monthreport(fd_value,fd_date,fd_sort)values(?,?,?) ";
			jdbc.update(sql, title.get(i), date(), i);
		}
	}

	@RequestMapping(value = "/mainChange")
	@ResponseBody
	public void mainChange() throws Exception {
		List<List<String>> list = ras.queryMainChange();
		for (int i = 0; i < list.size(); i++) {
			delete("ns_main_change_" + (i + 1),date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into ns_main_change_" + (i + 1) + "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	@RequestMapping(value = "/mainDesBl")
	@ResponseBody
	public void mainDesBl() throws Exception {
		List<Map<String,Object>> data= ras.mainDesBl();
		delete("ns_main_DesBl",date());
		for (int i = 0; i < data.size(); i++) {
				String sql = "insert into ns_main_DesBl(FD_FINAL_LEVEL,NUM,BL,fd_date,fd_sort)values(?,?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("FD_FINAL_LEVEL")==null?"":data.get(i).get("FD_FINAL_LEVEL").toString(),data.get(i).get("NUM")==null?"0":data.get(i).get("NUM").toString(),data.get(i).get("BL")==null?"0":data.get(i).get("BL").toString(), date(), i);
		}
	}
	
	@RequestMapping(value = "/mainShot")
	@ResponseBody
	public void mainShot() throws Exception {
		List<Map<String,Object>> data= ras.mainShot();
		delete("NS_main_Shot",date());
		for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_main_Shot(NAME,NUM,SCO,fd_date,fd_sort)values(?,?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("NAME"),data.get(i).get("NUM")==null?"0":data.get(i).get("NUM").toString(),data.get(i).get("SCO")==null?"0":data.get(i).get("SCO").toString(), date(), i);
		}
	}
	
	@RequestMapping(value = "/queryMainLevel")
	@ResponseBody
	public void queryMainLevel() throws Exception {
		List<List<String>> list = ras.queryMainLevel();
		for (int i = 0; i < list.size(); i++) {
			delete("NS_Main_Level_" +i,date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into NS_Main_Level_" +i+ "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	
	@RequestMapping(value = "/mainIndustry")
	@ResponseBody
	public void mainIndustry() throws Exception {
		List<Object> list = ras.mainIndustry();
			List<String> five = (List<String>) list.get(0);
			delete("NS_main_Industry",date());
			for (int j = 0; j < five.size(); j++)  {
				String sql = "insert into NS_main_Industry(FD_VALUE,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, five.get(j), date(), j);
			}
			List<Map<String,Object>> data = (List<Map<String, Object>>) list.get(1);
			delete("NS_main_Industry_data",date());
			for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_main_Industry_data(name,valuez,value,fd_date,fd_sort)values(?,?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("name"),data.get(i).get("valuez"),data.get(i).get("value"), date(), i);
		}
	}
	
	@RequestMapping(value = "/mainRatingDepart")
	@ResponseBody
	public void mainRatingDepart() throws Exception {
		List<List<String>> list = ras.mainRatingDepart();
		for (int i = 0; i < list.size(); i++) {
			delete("NS_main_mainRating_Depart_" +i,date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into NS_main_mainRating_Depart_" +i+ "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	
	@RequestMapping(value = "/newRadar")
	@ResponseBody
	public void newRadar() throws Exception {
		List<Object> list = ras.newRadar();
			List<String> five = (List<String>) list.get(1);
			delete("NS_new_Radar",date());
			for (int j = 0; j < five.size(); j++)  {
				String sql = "insert into NS_new_Radar(FD_VALUE,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, five.get(j), date(), j);
			}
			delete("NS_new_Radar_data",date());
			List<Map<String,Object>> data = (List<Map<String, Object>>) list.get(0);
			for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_new_Radar_data(name,max,fd_date,fd_sort)values(?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("name"),data.get(i).get("max"), date(), i);
		}
	}
	
	
	@RequestMapping(value = "/newRadar1")
	@ResponseBody
	public void newRadar1() throws Exception {
		List<Object> list = ras.newRadar1();
			List<String> five = (List<String>) list.get(1);
			delete("NS_new_Radar_1",date());
			for (int j = 0; j < five.size(); j++)  {
				String sql = "insert into NS_new_Radar_1(FD_VALUE,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, five.get(j), date(), j);
			}
			delete("NS_new_Radar_data_1",date());
			List<Map<String,Object>> data = (List<Map<String, Object>>) list.get(0);
			for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_new_Radar_data_1(name,max,fd_date,fd_sort)values(?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("name"),data.get(i).get("max"), date(), i);
		}
	}
	
	
	@RequestMapping(value = "/TyRadar")
	@ResponseBody
	public void TyRadar() throws Exception {
		List<Object> list = ras.TyRadar();
			List<String> five = (List<String>) list.get(1);
			delete("NS_Ty_Radar",date());
			for (int j = 0; j < five.size(); j++)  {
				String sql = "insert into NS_Ty_Radar(FD_VALUE,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, five.get(j), date(), j);
			}
			delete("NS_Ty_Radar_data",date());
			List<Map<String,Object>> data = (List<Map<String, Object>>) list.get(0);
			for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_Ty_Radar_data(name,max,fd_date,fd_sort)values(?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("name"),data.get(i).get("max"), date(), i);
		}
	}
	
	@RequestMapping(value = "/TyRadar1")
	@ResponseBody
	public void TyRadar1() throws Exception {
		List<Object> list = ras.TyRadar1();
			List<String> five = (List<String>) list.get(1);
			delete("NS_Ty_Radar_1",date());
			for (int j = 0; j < five.size(); j++)  {
				String sql = "insert into NS_Ty_Radar_1(FD_VALUE,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, five.get(j), date(), j);
			}
			delete("NS_Ty_Radar_data_1",date());
			List<Map<String,Object>> data = (List<Map<String, Object>>) list.get(0);
			for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_Ty_Radar_data_1(name,max,fd_date,fd_sort)values(?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("name"),data.get(i).get("max"), date(), i);
		}
	}
	
	@RequestMapping(value = "/debtShotDe")
	@ResponseBody
	public void debtShotDe() throws Exception {
		List<Map<String,Object>> data= ras.debtShotDe();
		delete("NS_debt_ShotDe",date());
		for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_debt_ShotDe(NAME,NUM,SCO,fd_date,fd_sort)values(?,?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("NAME"),data.get(i).get("NUM")==null?"0":data.get(i).get("NUM").toString(),data.get(i).get("SCO")==null?"0":data.get(i).get("SCO").toString(), date(), i);
		}
	}
	@RequestMapping(value = "/debtDesBl")
	@ResponseBody
	public void debtDesBl() throws Exception {
		List<Map<String,Object>> data= ras.debtDesBl();
		delete("ns_debt_DesBl",date());
		for (int i = 0; i < data.size(); i++) {
				String sql = "insert into ns_debt_DesBl(FD_FINAL_LEVEL,NUM,BL,fd_date,fd_sort)values(?,?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("FD_FINAL_LEVEL")==null?"":data.get(i).get("FD_FINAL_LEVEL").toString(),data.get(i).get("NUM")==null?"0":data.get(i).get("NUM").toString(),data.get(i).get("BL")==null?"0":data.get(i).get("BL").toString(), date(), i);
		}
	}
	@RequestMapping(value = "/debtScoHighLow")
	@ResponseBody
	public void debtScoHighLow() throws Exception {
		List<String> list = ras.debtScoHighLow();
			delete("ns_debt_Sco_HighLowl",date());
			for (int j = 0; j < list.size(); j++) {
				String sql = "insert into ns_debt_Sco_HighLowl(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(j), date(), j);
			}
	}
	@RequestMapping(value = "/debtReport")
	@ResponseBody
	public void debtReport() throws Exception {
		Map<String, Object> map = ras.debtReport();
			delete("ns_debt_Report",date());
			for (int j = 0; j < map.size(); j++) {
				String sql = "insert into ns_debt_Report(SUMNUM,MOTHCOUNT,BL,fd_date,fd_sort)values(?,?,?,?,?) ";
				jdbc.update(sql, map.get("SUMNUM"),map.get("MOTHCOUNT")==null?"0":map.get("MOTHCOUNT").toString(),map.get("BL")==null?"0":map.get("BL").toString(), date(), j);
			}
	}
	@RequestMapping(value = "/debtDashboard")
	@ResponseBody
	public void debtDashboard() throws Exception {
		// 主体评级数目
		List<String> title = ras.debtDashboard();
		delete("NS_DEBT_MONTHREPORT",date());
		for (int i = 0; i < title.size(); i++) {
			String sql = "insert into NS_DEBT_MONTHREPORT(fd_value,fd_date,fd_sort)values(?,?,?) ";
			jdbc.update(sql, title.get(i), date(), i);
		}
	}
	@RequestMapping(value = "/queryDebtLevel")
	@ResponseBody
	public void queryDebtLevel() throws Exception {
		List<List<String>> list = ras.queryDebtLevel();
		for (int i = 0; i < list.size(); i++) {
			delete("NS_debt_Level_" +i,date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into NS_debt_Level_" +i+ "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	
	@RequestMapping(value = "/debtChange")
	@ResponseBody
	public void debtChange() throws Exception {
		List<List<String>> list = ras.queryDebtChange();
		for (int i = 0; i < list.size(); i++) {
			delete("ns_Debt_change_" + (i),date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into ns_Debt_change_" + (i) + "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	
	@RequestMapping(value = "/debtRatingDepart")
	@ResponseBody
	public void debtRatingDepart() throws Exception {
		List<List<String>> list = ras.debtRatingDepart();
		for (int i = 0; i < list.size(); i++) {
			delete("NS_Debt_DebtRating_Depart_" +i,date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into NS_Debt_DebtRating_Depart_" +i+ "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	
	@RequestMapping(value = "/queryWarnNum")
	@ResponseBody
	public void queryWarnNum() throws Exception {
		List<String> title = ras.queryWarnNum();
		delete("NS_query_WarnNum",date());
		for (int i = 0; i < title.size(); i++) {
			String sql = "insert into NS_query_WarnNum(fd_value,fd_date,fd_sort)values(?,?,?) ";
			jdbc.update(sql, title.get(i), date(), i);
		}
	}
	
	@RequestMapping(value = "/disposalNum")
	@ResponseBody
	public void disposalNum() throws Exception {
		List<String> title = ras.disposalNum();
		delete("NS_disposal_Num",date());
		for (int i = 0; i < title.size(); i++) {
			String sql = "insert into NS_disposal_Num(fd_value,fd_date,fd_sort)values(?,?,?) ";
			jdbc.update(sql, title.get(i), date(), i);
		}
	}
	@RequestMapping(value = "/debtShot")
	@ResponseBody
	public void debtShot() throws Exception {
		List<Map<String,Object>> data= ras.debtShot();
		delete("NS_debt_Shot",date());
		for (int i = 0; i < data.size(); i++) {
			String sql = "insert into NS_debt_Shot(FD_NAME,NUM,fd_date,fd_sort)values(?,?,?,?) ";
			jdbc.update(sql, data.get(i).get("FD_NAME"),data.get(i).get("NUM")==null?"0":data.get(i).get("NUM").toString(), date(), i);
		}
	}
	@RequestMapping(value = "/queryWarn")
	@ResponseBody
	public void queryWarn() throws Exception {
		List<List<String>> list = ras.queryWarn();
		for (int i = 0; i < list.size(); i++) {
			delete("NS_query_Warn_" +i,date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into NS_query_Warn_" +i+ "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	@RequestMapping(value = "/queryWarnTrend")
	@ResponseBody
	public void queryWarnTrend() throws Exception {
		List<List<String>> list = ras.queryWarnTrend();
		for (int i = 0; i < list.size(); i++) {
			delete("NS_Warn_Trend_" +i,date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into NS_Warn_Trend_" +i+ "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	
	
	@RequestMapping(value = "/warnIndustry")
	@ResponseBody
	public void warnIndustry() throws Exception {
		List<Object> list = ras.warnIndustry();
			List<String> five = (List<String>) list.get(0);
			delete("NS_warn_Industry",date());
			for (int j = 0; j < five.size(); j++)  {
				String sql = "insert into NS_warn_Industry(FD_VALUE,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, five.get(j), date(), j);
			}
			List<Map<String,Object>> data = (List<Map<String, Object>>) list.get(1);
			delete("NS_warn_Industry_data",date());
			for (int i = 0; i < data.size(); i++) {
				String sql = "insert into NS_warn_Industry_data(name,valuez,value,fd_date,fd_sort)values(?,?,?,?,?) ";
				jdbc.update(sql, data.get(i).get("name"),data.get(i).get("valuez"),data.get(i).get("value"), date(), i);
		}
	}
	
	
	@RequestMapping(value = "/warnRatingDepart")
	@ResponseBody
	public void warnRatingDepart() throws Exception {
		List<List<String>> list = ras.warnRatingDepart();
		for (int i = 0; i < list.size(); i++) {
			delete("NS_warn_Depart_" +i,date());
			for (int j = 0; j < list.get(i).size(); j++) {
				String sql = "insert into NS_warn_Depart_" +i+ "(fd_value,fd_date,fd_sort)values(?,?,?) ";
				jdbc.update(sql, list.get(i).get(j), date(), j);
			}
		}
	}
	
	public static String date() {
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		return formateDate(today.getYear() + "-" + sjc);
	}

	public static String formateDate(String strDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date date = sdf.parse(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH,0);

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			String strMo = "";
			if (month < 10) {
				strMo = "0" + month;
			} else {
				strMo = "" + month;
			}

			return year + strMo;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
