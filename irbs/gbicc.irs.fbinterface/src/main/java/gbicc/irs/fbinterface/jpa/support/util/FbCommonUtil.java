package gbicc.irs.fbinterface.jpa.support.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;

/**
 * 风报接口常用工具类
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
public class FbCommonUtil {

    /**
     * 字符串是否无效
     *
     * @param value
     * @return true：无效，false：有效
     */
    public static boolean stringIsNotValid(String value) {
        if(StringUtils.isBlank(value) || StringUtils.isEmpty(value) || "null".equalsIgnoreCase(value)) {
            return true;
        }
        return false;
    }
    /**
     * 字符串是否有效
     *
     * @param value
     * @return true：有效，false：无效
     */
    public static boolean stringIsValid(String value) {
        return !stringIsNotValid(value);
    }
    /**
     * 判断分页参数是否有效
     *
     * @param value
     * @return
     */
    public static boolean pageIntegerIsNotValid(Integer value) {
        if(value==null || value<=0) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断分页参数是否有效
     *
     * @param value
     * @return
     */
    public static boolean pageIntegerIsValid(Integer value) {
        return !pageIntegerIsNotValid(value);
    }
    /**
     * 获取增量接口请求时间endTime
     *
     * @return
     */
    public static String getEndTimeRequest() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date());
    }
    /**
     * 获取增量接口请求时间一个月startTime,返回yyyy/MM/dd
     *
     * @return
     */
    public static String getStartTimeRequest(Integer before) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -before);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(calendar.getTime());
    }
    /**
     * 获取增量接口请求时间一天前,返回yyyy/MM/dd
     *
     * @return
     */
    public static String getStartTimeOneDayRequest() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(calendar.getTime());
    }
    /**
     * 获取当前时间戳：10位
     *
     * @param timestamp 13位时间戳
     * @return
     * @throws Exception
     */
    public static String getCurrentTimeStamp(String timestamp) throws Exception{
        if(stringIsNotValid(timestamp)) {
            String timestamp13 = getCurrentTimeStamp13();
            return timestamp13.substring(0,timestamp13.length()-3);
        }else {
            return timestamp.substring(0,timestamp.length()-3);
        }
    }
    /**
     * 获取当前年月日时分秒时间戳：13位
     *
     * @return
     */
    public static String getCurrentTimeStamp13() throws Exception{
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(new Date());
        Date date = format.parse(strDate);
        calendar.setTime(date);
        return String.valueOf(calendar.getTimeInMillis());
    }
    /**
     * 获取传入时间before个月前日期时间戳：10位
     *
     * @return
     */
    public static String getMonthBeforeTimeStamp(String strDate, int before) throws Exception{
        String timestamp13 = getBeforeTimeStamp13(strDate,Calendar.MONTH,before);
        return timestamp13.substring(0,timestamp13.length()-3);
    }
    /**
     * 获取传入时间before个天前日期时间戳：10位
     *
     * @return
     */
    public static String getDayBeforeTimeStamp(String strDate, int before) throws Exception{
        String timestamp13 = getBeforeTimeStamp13(strDate,Calendar.DAY_OF_MONTH,before);
        return timestamp13.substring(0,timestamp13.length()-3);
    }
    /**
     * 获取传入时间before个月前日期时间戳：13位
     * @param before
     *
     * @return
     */
    public static String getBeforeTimeStamp13(String strDate, int type, int before) throws Exception{
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate2 = format.format(new Date(Long.parseLong(strDate)));
        Date date = format.parse(strDate2);
        calendar.setTime(date);
        calendar.add(type, -before);
        return String.valueOf(calendar.getTimeInMillis());
    }
    
    /**
     * 获取当前线程需要等待毫秒数
     *
     * @return
     */
    public static int getSleepSeconds() {
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        return (60-second)*1000;
    }
    
    /**
     * 判断AppUtil.getMap的flag
     *
     * @param map
     * @return
     */
    public static boolean getApputilMapFlag(Map<String, Object> map) {
        if(map==null) {
            return false;
        }
        if(stringIsNotValid(map.get(FbCommonEnums.APPUTIL_MAP_FLAG.getValue()).toString())) {
            return false;
        }
        return (boolean) map.get(FbCommonEnums.APPUTIL_MAP_FLAG.getValue());
    }
    /**
     * 获取规则跑批批次号，返回yyyyMMdd
     *
     * @return
     */
    public static String getTaskseqno() {
        
        String taskseqno = "";
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        
        taskseqno = sdf.format(new Date());
        
        return taskseqno;
    }
}
