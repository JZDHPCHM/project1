package net.gbicc.app.irr.jpa.support.enums;
/**
* IRR评估项目类型枚举
*/
public enum IrrProjTypeEnum {
	TYPE_NAME("名称："),
	STAN_SCORE("评分标准："),
	A_STAN_SCORE("A类评分标准："),
	IS_USE("是否启用："),
	CIRC_RATE("保监会权重："),
	BUREAU_RATE("保监局权重："),
	THE_RISK_RATE("占本类风险的比重："),
	PDQ_RISK_RATE("占难以量化风险的比重："),
	TOTAL_RISK_RATE("占总风险的比重："),
	WEIG_SCOR_STAND("加权后评分标准："),
	ATTR_DIVI("|"),
	IS_USE_Y("是"),
	IS_USE_N("否"),
	/**
	 * 封面页评估项目编码
	 */
	FM_CODE("FM");
	
	private String value;

	private IrrProjTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
