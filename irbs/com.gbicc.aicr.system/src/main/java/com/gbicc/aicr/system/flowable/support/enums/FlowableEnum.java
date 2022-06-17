package com.gbicc.aicr.system.flowable.support.enums;
/**
* flowable的枚举类
*/
public enum FlowableEnum {

	/**
	 * 业务唯一变量编码
	 */
    VAR_BUSINESS_KEY_CODE("businessKey"),
    /**
     * 流程线路变量
     */
    VAR_PATH("path"),
    /**
     * 提交变量值
     */
    VAR_PATH_SUBMIT("submit"),
    /**
     * 退回变量值
     */
    VAR_PATH_BACK("back");
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private FlowableEnum(String value) {
		this.value = value;
	}
}
