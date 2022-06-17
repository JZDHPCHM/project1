package com.gbicc.aicr.system.support.enums;

/**
 * poi枚举
 * 
 * @author FC
 * @version v1.0 2019年8月8日
 */
public enum PoiEnum {

    /**
     * set方法前缀
     */
    SET_METHOD_PREFIX("set"),
    /**
     * get方法前缀
     */
    GET_METHOD_PREFIX("get"),
    /**
     * 默认的日期格式
     */
    DEFAULT_DATE_FORMAT_PATTERN("yyyy年MM月dd日"),
    /**
     * 空值的内容
     */
    DEFAULT_BLANK_VALUE1("-"),
    /**
     * 空值的内容
     */
    DEFAULT_BLANK_VALUE2("--"),
    /**
     * 不写入的值
     */
    NO_WRITE_VALUE("…"),
    /**
     * xls文档后缀
     */
    XLS_SUFFIX("xls"),
    /**
     * xlsx文档后缀
     */
    XLSX_SUFFIX("xlsx"),
    /**
     * 数据为空提示
     */
    DATA_NULL_TIPS("数据为空！"),
    /**
     * 默认字体名称
     */
    DEFAULT_FONT_NAME("微软雅黑");

    private String value;

    private PoiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
