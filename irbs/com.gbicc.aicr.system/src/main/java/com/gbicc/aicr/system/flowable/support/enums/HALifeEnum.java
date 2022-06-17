package com.gbicc.aicr.system.flowable.support.enums;
/**
*	恒安人寿流程枚举
*/
public enum HALifeEnum {
	/**
	 * 采集人变量编码
	 */
	COLL_LOGIN_NAME_VAR("collUserLoginName");

	private String value;

	private HALifeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
