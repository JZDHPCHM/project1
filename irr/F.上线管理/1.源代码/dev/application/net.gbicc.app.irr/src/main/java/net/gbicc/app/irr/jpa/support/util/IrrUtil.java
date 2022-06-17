package net.gbicc.app.irr.jpa.support.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* IRR工具类
*
*/
public class IrrUtil {

    private final static Logger LOG = LogManager.getLogger(IrrUtil.class);

	/**
	 * 一年中第一个期次的数字
	 */
	public static final String YEAR_ONE_BATCH_NUM = "1";
	/**
	 * 一年中第四个期次的数字
	 */
	public static final String YEAR_FOUR_BATCH_NUM = "4";
	/**
	 * 期次后缀
	 */
	public static final String TASK_BATCH_SUFFIX = "Q";
	
	/**
	 * 获取上一期次
	 * @param taskBatch 当前期次
	 * @return
	 */
	public static String getPreviousPeriod(String taskBatch) {
		if(StringUtils.isBlank(taskBatch)) {
			return null;
		}
		String year = taskBatch.substring(0, 4);
		String many = taskBatch.substring(5);
		if(YEAR_ONE_BATCH_NUM.equals(many)) {
			many = YEAR_FOUR_BATCH_NUM;
			year = String.valueOf(Integer.valueOf(year)-1);
		}else {
			many = String.valueOf(Integer.valueOf(many)-1);
		}
		return year+TASK_BATCH_SUFFIX+many;
	}
	
	/**
	 * 返回map信息对象
	 * @param flag 操作成功标识
	 * @param data 信息
	 * @return
	 */
	public static Map<String, Object> getMap(Boolean flag,Object data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		map.put("data", data);
		return map;
	}
	
	/**
	 * 返回map信息对象
	 * @param flag 操作成功标识
	 * @return
	 */
	public static Map<String, Object> getMap(Boolean flag){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		return map;
	}
	
	/**
	 * 获取jqGrid分页数据
	 * @param data 数据
	 * @param records 共有多少条记录
	 * @param page	     当前页数
	 * @param size	    每页有多少记录
	 * @return
	 */
	public static Map<String,Object> getPageMap(Object data,Integer records,Integer page,Integer size){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		map.put("records", records);
		map.put("total", calcToltalPage(records,size));
		map.put("page", page);
		return map;
	}
	
	/**
	 * 计算一共有多少页
	 * @param records 共有多少记录
	 * @param size	     每页大小
	 * @return
	 */
	public static Integer calcToltalPage(Integer records,Integer size) {
		if(records == null || size == null) {
			return 0;
		}
		Integer pages = records/size;
		if(records % size > 0) {
			pages ++;
		}
		return pages;
	}
	
	/**
	 * 判断字符串的obj是否为空
	 * @param obj 字符串对象
	 * @return
	 */
	public static Boolean strObjectIsEmpty(Object obj) {
        if (obj != null && !"".equals(obj) && StringUtils.isNotBlank(obj.toString())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断字符串的obj是否不为空
	 * @param obj 字符串对象
	 * @return
	 */
	public static Boolean strObjectIsNotEmpty(Object obj) {
		return !strObjectIsEmpty(obj);
	}
	

	/**
	 * 判断是否为整数
	 * @param str
	 * @return
	 */
	public static Boolean isInteger(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 判断是否为浮点数或整数
	 * @param str
	 * @return
	 */
	public static Boolean isIntOrDouble(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
        Pattern pattern = Pattern.compile("^([-\\+]?\\d+)(\\.\\d+)?$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 百分比字符串转化成数字（字符串必须是数字或者百分比）
	 * @param perceStr 百分比字符串
	 * @return
	 */
	public static Double perceStrTransDouble(String perceStr) {
		String str = perceStr.replace("%", "");
		return Double.valueOf(str)/100d;
	}
	
	/**
	 * 获取上传五大类的期次
	 * @param yearNum 年数字
	 * @param quarNum 季度数字
	 * @return
	 */
	public static String getFiveClassTaskBatch(String yearNum,String quarNum) {
		String returnStr  = "";
		if(StringUtils.isBlank(yearNum)) {
			return returnStr;
		}
		returnStr += yearNum;
		if(StringUtils.isNotBlank(quarNum)) {
			returnStr += TASK_BATCH_SUFFIX + quarNum;
		}
		return returnStr;
	}

    /**
     * 保留小数（四舍五入）
     *
     * @param scale
     *            小数位,如为空则保留整数
     * @param value
     *            格式化的值
     * @param isRound
     *            是否四舍五入
     * @return
     */
    public static String formatNumber(Integer scale, String value, Boolean isRound) {
        if (StringUtils.isBlank(value))
            return value;
        if (isRound == null)
            isRound = true;
        try {
            BigDecimal bigDecimal = new BigDecimal(value.trim());
            if (scale == null) {
                return bigDecimal.setScale(0, isRound ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN).toPlainString();
            } else {
                return bigDecimal.setScale(scale, isRound ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN)
                        .toPlainString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e);
            return value;
        }
    }

    /**
     * 得到XBRL报告截止日期
     * 
     * @param taskBatch
     *            评估计划任务期次 (YYYYQN)
     * @return
     */
    public static String getXbrlReportEndDate(String taskBatch) {
        if (taskBatch == null || "".equals(taskBatch)) {
            return "";
        }
        if (!taskBatch.contains("Q") && !taskBatch.contains("q")) {
            return taskBatch;
        }
        String year = taskBatch.substring(0, 4);
        String batch = taskBatch.substring(5);
        String endDate = null;
        if ("1".equals(batch)) {
            endDate = "0331";
        } else if ("2".equals(batch)) {
            endDate = "0630";
        } else if ("3".equals(batch)) {
            endDate = "0930";
        } else if ("4".equals(batch)) {
            endDate = "1231";
        }
        return year + endDate;
    }

}
