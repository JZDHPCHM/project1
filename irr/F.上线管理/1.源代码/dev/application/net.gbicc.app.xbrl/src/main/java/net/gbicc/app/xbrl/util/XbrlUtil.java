package net.gbicc.app.xbrl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
*	XBRL工具类
*
*/
public class XbrlUtil {

	/**
	 * 输入了转化成字符串
	 * @param stream 输入流
	 * @param charset 字符集
	 * @return
	 * @throws IOException
	 */
	public static String getStreamAsString(InputStream stream, String charset) {
		StringWriter writer = null;
		try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset), 8192);
            writer = new StringWriter();
            char[] chars = new char[8192];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
		if(writer == null) {
			return "";
		}
		return writer.toString();
    }
	
	/**
	 * 得到XBRL报告截止日期
	 * @param taskBatch 评估计划任务期次 (YYYYQN)
	 * @return
	 */
	public static String getXbrlReportEndDate(String taskBatch) {
		if(taskBatch == null || "".equals(taskBatch)) {
			return "";
		}
		if( ! taskBatch.contains("Q") && ! taskBatch.contains("q")) {
			return taskBatch;
		}
		String year = taskBatch.substring(0, 4);
		String batch = taskBatch.substring(5);
		String endDate = null;
		if("1".equals(batch)) {
			endDate = "0331";
		}else if("2".equals(batch)){
			endDate = "0630";
		}else if("3".equals(batch)){
			endDate = "0930";
		}else if("4".equals(batch)) {
			endDate = "1231";
		}
		return year + endDate;
	}
	
	/**
	 * 获取返回的map
	 * @param flag 标识
	 * @param data 数据
	 * @return
	 */
	public static Map<String, Object> getMap(Boolean flag,Object data){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		map.put("data", data);
		return map;
	}
	
	/**
	 * 获取返回标识的map
	 * @param flag 标识
	 * @return
	 */
	public static Map<String, Object> getMap(Boolean flag){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		return map;
	}
}
