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
	IRR_TEMPLATE_FILE_TITLE("评估项目名称-行次-指标编码-指标名称-指标结果类型-指标值"),
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
	IRR_INDEX_RESULT_FILE_NAME("风险综合评级"),
	/**
	 * IRR汇总指标结果分公司sheet页标题(中间用-隔开)
	 */
	SUMM_SHEET_TITLE("上期结果-上期得分-本期结果-本期得分-数据校验"),
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
	SUMMARY_FILE_BRANCH_PROJ_FIRST("分"),
	/**
	 * 模板日期类型数据格式化
	 */
	TEMPLATE_DATA_FORMAT_DATE_STR("yyyy年m月d日"),
	/**
	 * 模板文本类型数据格式化
	 */
	TEMPLATE_TEXT_FORMAT_DATE_STR("@"),
	/**
	 * 绩效补录模板sheet名称包含字
	 */
	IRR_SHEET_NAME("绩效"),
	/**
	 * 模板目录sheet页名称
	 */
    TEMPLATE_DIRECTORY_SHEETNAME("目录"),
    /**
     * 采集指标查询导出sheet名称
     */
    TEMPLATE_COLL_RESULT_SHEET_NAME("采集结果"),
    /**
     * 采集指标查询导出sheet表头
     */
    TEMPLATE_COLL_RESULT_SHEET_TITLE("任务期次-拆分指标编码-拆分指标名称-本期指标结果-本期指标得分-上期指标结果-上期指标得分-采集方式-采集机构名称-采集人名称"),
    /**
     * 采集指标查询导出无数据提示信息
     */
    TEMPLATE_COLL_RESULT_EMPTY_INFO("您查询导出的采集结果为空！");
	
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
