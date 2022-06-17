package gbicc.irs.main.rating.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tools {

	/**
	 * 指定日期增加N年
	 * @param _date 目标日期
	 * @param _num	增加的年份
	 * @return 增加后的日期
	 */
	public static Date addYear(Date _date,int _num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);  
		cal.add(Calendar.YEAR, _num);
		return cal.getTime();
	}
	

	public static Date addDay(Date _date,int _num) {
	
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);   //设置当前时间
		cal.add(Calendar.MONTH, _num);
		return cal.getTime();
	}
	
	/**
	 * 格式化日期
	 * @param date 目标日期
	 * @param pattern 目标格式
	 * @return 格式化后的日期
	 */
	public static String formatDate(Date date, String pattern) {
        SimpleDateFormat dateTimeFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        dateTimeFormat.applyPattern(pattern);
        return dateTimeFormat.format(date==null?new Date():date);
    }
}
