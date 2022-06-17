package net.gbicc.app.pfm.jpa.support.enums;
/**
* 绩效枚举
*/
public enum PfmEnum {
	/**
	 * 分公司绩效
	 */
	BRANCH_PFM_FLAG("BRANCH"),
	/**
	 * 总公司绩效1
	 */
	HEAD1_PFM_FLAG("HEAD1"),
	/**
	 * 总公司绩效2
	 */
	HEAD2_PFM_FLAG("HEAD2"),
	/**
	 * 模板文件夹路径
	 */
	TEMPLATE_FOLDER_URL("template"),
	/**
	 * 绩效帮助模板编码
	 */
	TEMPLATE_PFM_HELP_CODE("PFMHELP"),
	/**
	 * 绩效模板后缀
	 */
	TEMPLATE_PFM_SUFFIX(".xls"),
	/**
	 * 绩效模板标题维度开始索引
	 */
	TEMPLATE_PFM_TITLE_DIM_INDEX_START("5"),
	/**
	 * 绩效模板标题维度标识
	 */
	TEMPLATE_PFM_TITLE_DIM_FLAG("dimTitle"),
	/**
	 * 绩效模板指标信息标识
	 */
	TEMPLATE_PFM_INDEX_INFO_FLAG("pfmIndex"),
	
	/**
	 * 绩效补录模板
	 */
    TEMPLATE_PFM_DATA("PFMDATA"),
    /**
     * 下载绩效补录模板名称
     */
    TEMPLATE_PFM_DATA_NAME("绩效补录模板"),
    /**
     * 分公司绩效sheet名称
     */
	TEMPLATE_PFM_SHEET_BRANCH("分公司绩效"),
	/**
	 * 总公司绩效-Isheet名称
	 */
	TEMPLATE_PFM_SHEET_DEPA("总公司绩效-I"),
	/**
	 * 总公司绩效-IIsheet名称
	 */
    TEMPLATE_PFM_SHEET_CHANNEL("总公司绩效-II"),
    /**
     * 信息系统总得分编码
     */
    INFO_SYS_TOTAL_SCORE("TOTALSCORE"),
    /**
     * 员工流失率
     */
    EMPL_TURN_RATE_NAME("员工流失率"),
    /**
     * 犹豫期内电话回访成功率
     */
    HESI_TELE_VISI_SUCCESS_NAME("犹豫期内电话回访成功率"),
    /**
     * 新契约回访完成率
     */
    NEW_CONT_VISI_COMP_NAME("新契约回访完成率"),
    /**
     * 续期收费率
     */
    RENE_RATE_NAME("续期收费率"),
    /**
     * 退（撤）保率
     */
    RETU_WDW_INSU_RATE_NAME("退（撤）保率"),
    /**
     * 绩效得分
     */
    PFM_RESULT_SCORE("绩效得分"),
    /**
     * 绩效得分标题
     */
    PFM_RESULT_SCORE_TITLE("名称-得分"),
    /**
     * 绩效结果为空提示
     */
    PFM_RESULT_NULL_TIP("该计划绩效得分不存在！");

	private String value;

	private PfmEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
