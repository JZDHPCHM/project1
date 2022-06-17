package com.gbicc.aicr.system.in2oa.enums;

/**
 * OA提醒枚举
 * 
 * @author FC
 * @version v1.0 2019年12月4日
 */
public enum OARemindEnum {
    /**
     * OA提醒接口参数大类
     */
    OA_REMIND_INTERFACE("oaRemindInterface"),
    /**
     * OA提醒接口地址
     */
    ADDRESS("address"),
    /**
     * OA提醒接口连接地址
     */
    URL("url"),
    /**
     * OA提醒接口主题
     */
    TITLE("title"),
    /**
     * OA提醒接口内容
     */
    CONTENT("content"),
    /**
     * OA提醒接口开关
     */
    SWITCH("switch"),
    /**
     * 开关-开
     */
    ON_OFF_ON("on"),
    /**
     * 开关-关
     */
    ON_OFF_OFF("off"),
    /**
     * 系统编码
     */
    SYS_CODE("syscode");

    private String value;

    private OARemindEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
