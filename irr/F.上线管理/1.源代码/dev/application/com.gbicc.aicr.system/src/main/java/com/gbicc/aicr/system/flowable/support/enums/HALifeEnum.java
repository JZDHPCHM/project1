package com.gbicc.aicr.system.flowable.support.enums;
/**
*	恒安人寿流程枚举
*/
public enum HALifeEnum {
	/**
	 * 采集人变量编码
	 */
    COLL_LOGIN_NAME_VAR("collUserLoginName"),
    /**
     * 邮件采集人内容
     */
    MAIL_COLL_USER_TEXT("采集人："),
    /**
     * 邮件采集机构内容
     */
    MAIL_COLL_ORG_TEXT("采集机构："),
    /**
     * 上报角色编码
     */
    ROLE_REPORT_CODE("irrProcessReport"),
    /**
     * 上报节点邮件
     */
    REPORT_MAIL_CODE("reportMail"),
    /**
     * 上报节点邮件标题编码
     */
    REPORT_MAIL_SUBJECT_CODE("subject"),
    /**
     * 上报节点邮件标题
     */
    REPORT_MAIL_SUBJECT_INFO("上报任务"),
    /**
     * 上报节点邮件内容编码
     */
    REPORT_MAIL_TEXT_CODE("text"),
    /**
     * 上报节点邮件内容
     */
    REPORT_MAIL_TEXT_INFO("您有上报任务，请登录风险综合评级数据报送系统查看！");

	private String value;

	private HALifeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
