package gbicc.irs.fbinterface.jpa.support.enums;

/**
 * 常用常量内容
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
public enum FbCommonEnums {
    
    /**
     * AppUtil.getMap结果标识：flag
     */
    APPUTIL_MAP_FLAG("flag"),
    /**
     * AppUtil.getMap数据标识：data
     */
    APPUTIL_MAP_DATA("data"),
    /**
     * 请求接口返回结果为空
     */
    RESULT_NULL("请求返回结果为空"),
    /**
     * 获取成功
     */
    RESULT_SUCCESS("获取成功"),
    /**
     * 保存数据成功
     */
    SAVE_SUCCESS("保存成功"),
    /**
     * 返回结果数据封装失败
     */
    RESULT_PARSE_FAIL("返回数据封装失败"),
    /**
     * 返回值如果要set进BigDecimal，需要转换，用来判断字段类型
     */
    BIGDECIMAL_TYPE("class java.math.BigDecimal"),
    /**
     * 实体类中公共字段：companyId
     */
    ENTITY_COMPANY_ID("companyId"),
    /**
     * 返回值如果要set进Long，需要转换，用来判断字段类型
     */
    LONG_TYPE("class java.lang.Long"),
    /**
     * 返回值如果要set进Long，需要转换，用来判断字段类型
     */
    INTEGER_TYPE("class java.lang.Integer"),
    /**
     * 处理返回信息hits数组为空
     */
    HITS_NULL("返回结果hits数组为空"),
    /**
     * 是否：Y:是
     */
    IS_Y("Y"),
    /**
     * 是否：N:否
     */
    IS_N("N"),
    /**
     * 预警规则调用url
     */
    RULE_URL("RULE_URL"),
    /**
     * 预警规则调用地址拼接taskseqno
     */
    RULE_TASKSEQNO("RULE_TASKSEQNO");
    
    private FbCommonEnums(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
