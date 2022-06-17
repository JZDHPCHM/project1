package net.gbicc.app.xbrl.enums;
/**
*	xbrl枚举
*/
public enum XBRLEnum {

	/**
	 * xbrl服务器访问路径
	 */
	SERVER_PATH_VAR("serverPath"),
	/**
	 * 字符集
	 */
	CHARSET("UTF-8"),
	/**
	 * XBRL机构映射
	 */
	REGULATOR_CODE("regulatorCode"),
	/**
	 * XBRL具体功能请求地址key
	 */
	XBRL_URL("xbrlUrl"),
	/**
	 * XBRL服务端请求返回的编码
	 */
	XBRL_SERVER_REQUEST_VAR("code"),
	/**
	 * XBRL服务端请求返回的信息编码
	 */
	XBRL_SERVER_REQUEST_MESSAGE("message"),
	/**
	 * 恒安标准人寿总公司机构编码
	 */
	HA_STANDARD_LIFE_HEAD_ORG_CODE("86");
	
	private String value;

	private XBRLEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
