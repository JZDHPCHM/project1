package com.gbicc.aicr.system.flowable.support.enums;
/**
* flowable的枚举类
*/
public enum FlowableEnum {

	/**
	 * 业务唯一变量编码
	 */
	VAR_BUSINESS_KEY_CODE("businessKey");
	
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
