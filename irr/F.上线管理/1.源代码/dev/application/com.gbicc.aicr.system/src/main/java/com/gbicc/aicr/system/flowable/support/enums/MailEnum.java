package com.gbicc.aicr.system.flowable.support.enums;
/**
*	邮件枚举
*/
public enum MailEnum {
	
	/**
	 * applicaiton配置文件中发送人
	 */
	APPLICATION_MAIL_USERNAME("spring.mail.username"),
	/**
	 * 系统数据字典-邮箱信息大类编码
	 */
	SYS_DICT_MAIN_INFO("mailInfo"),
	/**
	 * 系统数据字典-邮箱信息主题内容
	 */
	SYS_DICT_MAIN_SUBJECT("subject"),
	/**
	 * 系统数据字典-邮箱信息内容
	 */
	SYS_DICT_MAIN_TEXT("text"),
	/**
	 * 系统数据字典-邮箱开关(on开，off关)
	 */
	SYS_DICT_MAIN_SWITCH("switch"),
	/**
	 * 邮箱开关-开
	 */
	SWITCH_ON("on"),
	/**
	 * 邮箱开关-关
	 */
    SWITCH_OFF("off"),
    /**
     * 邮件内容换行符号
     */
    TEXT_NEWLINE(";");
	
	private String value;

	private MailEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
