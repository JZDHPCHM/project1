package net.gbicc.app.irr.jpa.support.enums;
/**
* IRR模板枚举
*
*/
public enum IrrTemplateEnum {

	/**
	 * IRR采集模板SHEET名称
	 */
	IRR_TEMPLATE_SHEET_NAME("IRR采集"),
	/**
	 * IRR采集模板文件名称
	 */
	IRR_TEMPLATE_FILE_NAME("IRR采集文件"),
	/**
	 * IRR采集模板文件类型后缀
	 */
	IRR_TEMPLATE_FILE_SUFFIX(".xls"),
	/**
	 * IRR采集模板TITLE（中间用-隔开）
	 */
	IRR_TEMPLATE_FILE_TITLE("指标编码-指标名称-指标结果类型-指标值"),
	/**
	 * IRR采集模板title字体名称
	 */
	IRR_TEMPLATE_TITLE_FONT_NAME("微软雅黑"),
	
	/**
	 * IRR采集计划审批意见SHEET名称
	 */
	IRR_PLAN_COMMENT_SHEET_NAME("审批意见"),
	/**
	 * IRR采集计划审批意见标题（中间用-隔开）
	 */
	IRR_PLAN_COMMENT_TITLE("任务名称-任务人-审批意见-创建时间-完成时间"),
	/**
	 * IRR采集计划审批意见为空提示
	 */
	IRR_PLAN_COMMENT_EMPTY("审批意见为空!"),
	/**
	 * 指标导出文件名称
	 */
	IRR_INDEX_RESULT_FILE_NAME("指标结果"),
	/**
	 * IRR汇总指标结果分公司sheet页标题(中间用-隔开)
	 */
	SUMM_SHEET_TITLE("Q1-Q1得分-Q2-Q2得分-数据校验"),
	/**
	 * 指标结果压缩文件后缀
	 */
	IRR_INDEX_RESULT_ZIP_FILE_SUFFIX(".zip"),
	/**
	 * 下载模板地址
	 */
	DOWNLOAD_TEMPLATE_URL("template"),
	/**
	 * 汇总下载模板
	 */
	SUMMARY_FILE_CODE("SUMMARY.xls"),
	/**
	 * XBRL总公司下载模板
	 */
	XBRL_HEAD_TEMP_CODE("1000.xls"),
	/**
	 * XBRL分公司下载模板
	 */
	XBRL_BRANCH_TEMP_CODE("1000000.xls"),
	/**
	 * 汇总下载模板分公司评估项目sheet页名称第一个字
	 */
	SUMMARY_FILE_BRANCH_PROJ_FIRST("分");
	
	private String value;

	private IrrTemplateEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
