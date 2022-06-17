package com.gbicc.aicr.system.support.enums;

/**
 * jstree的枚举
 * 
 * @author FC
 * @version v1.0 2019年6月20日
 */
public enum JsTreeEnum {

    /**
     * JStree的根节点ID
     */
    JSTREE_ROOT("#");
    private String value;

    private JsTreeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
