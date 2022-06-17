package com.gbicc.aicr.system.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统工具类
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
public class AppUtil {

    private static final Format format = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 集合转换成sql的in条件<br/>
     * 数量不超过1000,条件的类型为字符串
     * 
     * @param inParam
     *            数据
     * @return
     */
    public static String listToInString(List<String> inParam) {
        if (CollectionUtils.isEmpty(inParam) || inParam.size() > 1000) {
            throw new RuntimeException("in条件不存在或数量超过了1000");
        }
        String in = "";
        for (int i = 0; i < inParam.size(); i++) {
            String param = inParam.get(i);
            if (StringUtils.isBlank(param)) {
                continue;
            }
            if (i == 0) {
                in += "'" + param + "'";
            } else {
                in += ",'" + param + "'";
            }
        }
        return in;
    }

    /**
     * 生成授权日志任务编码
     *
     * @param issue
     *            期次
     * @return
     */
    public static String createAuthTaskLogTaskCode(String issue) {
        if (StringUtils.isBlank(issue)) {
            throw new RuntimeException("传入的期次为空，无法生成！");
        }
        return issue + format.format(new Date());
    }
}