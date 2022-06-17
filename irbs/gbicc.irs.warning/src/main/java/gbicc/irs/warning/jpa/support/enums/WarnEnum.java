package gbicc.irs.warning.jpa.support.enums;

/**
 * 预警枚举
 * 
 * @author FC
 * @version v1.0 2019年9月17日
 */
public enum WarnEnum {

    /**
     * 数据字典-预警大类编码
     */
    SYSTEM_DICTIONARY_WARN_CATE("WARN_CATE"),
    /**
     * 数据字典-工商信息预警小类编码
     */
    SYSTEM_DICTIONARY_IC_INFO("R001"),
    /**
     * 数据字典-舆情预警小类编码
     */
    SYSTEM_DICTIONARY_SENT_WARN("R002"),
    /**
     * 数据字典-诉讼处罚预警小类编码
     */
    SYSTEM_DICTIONARY_LITI_PUNI("R003");

    private String value;

    public String getValue() {
        return value;
    }

    private WarnEnum(String value) {
        this.value = value;
    }

}
