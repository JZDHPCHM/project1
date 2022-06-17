package com.gbicc.aicr.system.support.enums;

/**
 * 框架通用枚举类
 * 
 * @author FC
 * @version v1.0 2019年6月17日
 */
public enum AppEnum {
    /**
     * 是否-是
     */
    YESNO_Y("Y"),
    /**
     * 是否-否
     */
    YESNO_N("N"),
    /**
     * 请求返回map默认的数据的key
     */
    RETURN_DATA_DEFAULT_MAP_KEY("response"),
    /**
     * 请求返回数据默认的当前页
     */
    RETURN_DATA_DEFAULT_PAGE("1"),
    /**
     * 请求返回数据默认的每页大小
     */
    RETURN_DATA_DEFAULT_SIZE("20"),
    /**
     * 框架是否：是（需转化成数字）
     */
    FR_YESNO_Y("1"),
    /**
     * 框架是否：否（需转化成数字）
     */
    FR_YESNO_N("0"),
    /**
	 * 分公司后缀
	 */
	BRANCH_ORG_SUFFIX("分公司"),
	/**
	 * 百分比符号
	 */
    PERCE_SYMBOL("%"),
    /**
     * 默认下载excel的后缀
     */
    DEFAULT_DOWNLOAD_EXCEL_SUFFIX(".xls"),
    /**
     * 分隔符
     */
    SEPARATOR("-"),
    /**
     * 包文件夹分隔符
     */
    PACKAGE_SEPATATOR("."),
    /**
     * class文件后缀
     */
    CLASS_FILE_SUFFIX(".class"),
    /**
     * 实体命名后缀
     */
    ENTITY_SUFFIX("Entity"),
    /**
     * repository命名后缀
     */
    REPOSITORY_SUFFIX("Repository"),
    /**
     * repository保存的方法名
     */
    REPOSITORY_SAVE_METHOD_NAME("save"),
    /**
     * 无穷符号
     */
    INFI_OPER("∞"),;

    private String value;

    private AppEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
