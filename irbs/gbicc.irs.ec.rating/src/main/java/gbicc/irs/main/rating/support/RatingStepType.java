package gbicc.irs.main.rating.support;

public enum RatingStepType {
	
	QUANTITATIVE("QUANTITATIVE"),			//定量信息
	QUALITATIVE_EDIT("QUALITATIVE_EDIT"),	//定性指标录入	
	REPORT_INFO("REPORT_INFO"),			//评级报告展示
	INIT("INIT"),
	BS001("BS001"),
	BS002("BS002"),
	BS003("BS003"),
	BS004("BS004");
	private String text;
	
	private RatingStepType(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return this.text;
	}
}
