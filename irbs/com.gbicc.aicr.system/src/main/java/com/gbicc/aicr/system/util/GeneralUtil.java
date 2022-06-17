package com.gbicc.aicr.system.util;
/**
*	通用工具类
*/
public class GeneralUtil {

	/**
	 * 判断字符串Object是否为空
	 * @param obj
	 * @return
	 */
	public static Boolean strObjIsEmpty(Object obj) {
		if(obj == null || "".equals(obj)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串Object是否不为空
	 * @param obj
	 * @return
	 */
	public static Boolean strObjIsNotEmpty(Object obj) {
		return ! strObjIsEmpty(obj);
	}
}
