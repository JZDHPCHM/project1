package gbicc.irs.esb.service.support;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/**
	 * 当前时间
	 * @return
	 */
	public static String currentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date());
		return date;
	}
	
	/**
	 * 字符串转换为时间类型
	 * @return
	 */
	public static Date ConverDate(String date) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = df.parse(date);
		return d;
	}
}
