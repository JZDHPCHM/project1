package net.gbicc.app.irr.jpa.support.enums;
/**
*	权重枚举
*/
public enum WeightEnum {
	
	/**
	 * 分公司-北京编码
	 */
	ORG_BRANCH_BEIJING_CODE("8601"),
	/**
	 * 分公司-天津编码
	 */
	ORG_BRANCH_TIANJIN_CODE("8602"),
	/**
	 * 分公司-辽宁编码
	 */
	ORG_BRANCH_LIAONING_CODE("8606"),
	/**
	 * 分公司-大连编码
	 */
	ORG_BRANCH_DALIAN_CODE("8609"),
	/**
	 * 分公司-江苏编码
	 */
	ORG_BRANCH_JIANGSU_CODE("8605"),
	/**
	 * 分公司-山东编码
	 */
	ORG_BRANCH_SHANDONG_CODE("8604"),
	/**
	 * 分公司-青岛编码
	 */
	ORG_BRANCH_QINGDAO_CODE("8603"),
	/**
	 * 分公司-河南编码
	 */
	ORG_BRANCH_HENAN_CODE("8608"),
	/**
	 * 分公司-广东编码
	 */
	ORG_BRANCH_GUANGDONG_CODE("8610"),
	/**
	 * 分公司-四川编码
	 */
	ORG_BRANCH_SICHUAN_CODE("8607");

	private String value;

	public String getValue() {
		return value;
	}

	private WeightEnum(String value) {
		this.value = value;
	}
	
}
