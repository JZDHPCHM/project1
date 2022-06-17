package net.gbicc.app.irr.jpa.support.enums;
/**
* irr模块的枚举类
*
*/
public enum IrrEnum {
	/**
	 * 评估项目满分
	 */
	DEFAULT_STAN_MAX("100"),
	/**
	 * 默认数字
	 */
	DEFAULT_NUM("0"),
	/**
	 * OR08最大的行业可得得分
	 */
	OR08_MAX_INDULEVEL_NUM("10"),
	/**
	 * 是否：是
	 */
	YESNO_Y("Y"),
	/**
	 * 是否：否
	 */
	YESNO_N("N"),
	/**
	 * JStree 的root编码
	 */
	JSTREE_ROOT("#"),
	/**
     * 采集方式
     */
    COLL_WAY_CODE("collWay"),
    /**
     * 手工采集
     */
	COLL_WAY_HAND("HAND"),
	/**
	 * 系统采集
	 */
	COLL_WAY_SYS("SYS"),
	/**
	 * 系统计算
	 */
	COLL_WAY_CALC("CALC"),
	/**
	 * 采集的时候当前人没有采集指标的内容
	 */
	COLL_EMPTY_INDEX_INFO("您没有需要手工采集的指标,请上传此模板获取系统采集的指标！"),
	/**
	 * 指标结果类型-数值类型
	 */
	INDEX_RESULT_TYPE_NUMBER("NUMBER"),
	/**
	 * 指标结果类型-选项
	 */
	INDEX_RESULT_TYPE_OPTION("OPTION"),
	/**
	 * 指标结果类型-无
	 */
	INDEX_RESULT_TYPE_NONE("NONE"),
	/**
	 * 指标结果类型-不适用
	 */
	INDEX_RESULT_TYPE_DISABLE("DISABLE"),
	/**
	 * 指标结果类型-日期
	 */
	INDEX_RESULT_TYPE_DATE("DATE"),
	/**
	 * 指标结果类型大类编码
	 */
	INDEX_RESULT_TYPE_CODE("indexResultType"),
	/**
     * 指标结果为无的格式化前缀
     */
    INDEX_RESULT_TYPE_NONE_FORMATE_PREFIX("<"),
    /**
     * 指标结果为无的格式化后缀
     */
    INDEX_RESULT_TYPE_NONE_FORMATE_SUFFIX(">"),
    /**
     * 指标数据验证参数-大类
     */
	INDEX_DATA_VALID_PARAM_CODE("indexDataValidParam"),
	/**
	 * 分公司不进行上报的组织机构
	 */
	BRANCH_XBRL_ORG_EXCLUDE("branchXBRLOrgExclude"),
	/**
	 * 指标数据验证参数-最大波动值
	 */
	INDEX_DATA_VALID_PARAM_MAX("MAX"),
	/**
	 * 默认的指标填写值浮动
	 */
	DEFAULT_MAX_LIMIT_PARAM("30"),
	/**
	 * 最大的指标填写值浮动单位符号
	 */
	MAX_LIMIT_PARAM_UNIT_CODE("%"),
	/**
	 * 启用
	 */
	INDEX_STATUS_ENABLE("ENABLE"),
	/**
	 * 禁用
	 */
	INDEX_STATUS_DISABLE("DISABLE"),
	/**
	 * 不适用的指标值
	 */
	INDEX_NOT_APPLICABIE_VALUE("<不适用>"),
    /**
     * 不适用的指标值内容
     */
    INDEX_NOT_APPLICABIE_VALUE_CONTENT("不适用"),
	/**
	 * 指标层级：总公司
	 */
	INDEX_LEVEL_HEAD("HEAD"),
	/**
	 * 指标层级：分公司
	 */
	INDEX_LEVEL_BRANCH("BRANCH"),
	/**
	 * 拆分层级：分公司
	 */
	SPLIT_LEVEL_BRANCH("BRANCH"),
	/**
	 * 拆分层级：总公司
	 */
	SPLIT_LEVEL_HEAD("HEAD"),
	/**
	 * 拆分层级：渠道
	 */
	SPLIT_LEVEL_CHANNEL("CHANNEL"),
	/**
	 * 本身
	 */
	SPLIT_LEVEL_MYSELF("MYSELF"),
	/**
	 * 总公司根节点编码
	 */
	ORG_ROOT_CODE("86"),
	/**
	 * 个险渠道-总部编码
	 */
	ORG_HEAD_FC_DEPT_CODE("30"),
	/**
	 * 团险渠道-总部编码
	 */
	ORG_HEAD_GP_DEPT_CODE("31"),
	/**
	 * 续期渠道-总部编码
	 */
	ORG_HEAD_RP_DEPT_CODE("1721"),
	/**
	 * 多元渠道-总部编码
	 */
	ORG_HEAD_AD_DEPT_CODE("401"),
	/**
	 * 银保渠道-总部编码
	 */
	ORG_HEAD_BK_DEPT_CODE("32"),
	/**
	 * 上报XBRL机构名称
	 */
	IRR_XBRL_ORG_NAME("ORG_XBRL"),
	/**
	 * 默认每页记录条数
	 */
	IRR_DEFAULT_SIZE("20"),
	/**
	 * 默认第几页
	 */
	IRR_DEFAULT_PAGE("1"),
	/**
	 * 评分类型：直接评分
	 */
	SCORE_TYPE_DIRECT("DIRECT"),
	/**
	 * 评分类型：行业评分
	 */
	SCORE_TYPE_INDUSTRY("INDUSTRY"),
	/**
	 * 评分类型：扣分项
	 */
	SCORE_TYPE_DEDUCTION("DEDUCTION"),
	/**
	 * 上报评估项目
	 */
	XBRL_PROJ("xbrlProj"),
	/**
	 * 其他不上报的评估项目
	 */
	OTHER_PROJ("otherProj"),
    /**
     * 声誉风险权重
     */
    RR_WEIGHT("RRWeight"),
    /**
     * 合规风险权重
     */
    ORA_WEIGHT("ORAWeight"),
	/**
	 * 默认的分公司数量
	 */
	DEFAULT_BRANCH_NUM("10"),
	/**
	 * 评估项目多维度
	 */
	PROJ_TYPE_DIM("projTypeDim"),
	/**
	 * 总分
	 */
	TOTAL_SCORE_FLAG("TOTALSCORE"),
	/**
	 * 直接确定可得
	 */
	DIRE_DETE_FLAG("DIREDETE"),
	/**
	 * 行业水平确定可得
	 */
	INDU_LEVEL_FLAG("INDULEVEL"),
	/**
	 * 行业无法确定
	 */
	INDU_CAN_NOT_FLAG("INDUCANNOT"),
	/**
	 * 监管评分
	 */
	REGU_GRAD_FLAG("REGUGRAD"),
	/**
	 * 直接扣分
	 */
	DIRECT_POINTS_FLAG("DIRECTPOINTS"),
	/**
	 * 直接扣分特殊处理评估项目
	 */
	DIRECT_POINT_SPECIAL_PROJ("directPointSpecialProj"),
	/**
	 * OR08-分公司理赔
	 */
	OR_08("OR08"),
	/**
	 * 最近4个季度公司自查发现理赔管理操作风险事件的次数
	 */
	OR08029("OR08029"),
	/**
	 * 最近4个季度公司发生业内欺诈案件的次数
	 */
	OR08031("OR08031"),
	/**
	 * OR04-分公司销售、承保、保全
	 */
	OR_04("OR04"),
	/**
	 * OR13-分公司财务管理
	 */
	OR_13("OR13"),
	/**
	 * OR12-财务管理
	 */
	OR_12("OR12"),
	/**
	 * 权重评估项目
	 */
	WEIGHT_FLAG("weight"),
	/**
	 * 权重-操作风险-小计
	 */
	WEIGHT_OR_CALC("ORCALC"),
	/**
	 * 权重-难以量化风险合计
	 */
	WEIGHT_DR("DR"),
	/**
	 * 权重-量化风险
	 */
	WEIGHT_QR("QR"),
	/**
	 * 权重-风险综合评级
	 */
	WEIGHT_CR("CR"),
	/**
	 * 合规风险
	 */
	OR_A("ORA"),
	/**
	 * 销售、承保、保全业务线
	 */
	OR_1("OR1"),
	/**
	 * 理赔业务线
	 */
	OR_2("OR2"),
	/**
	 * 财务管理
	 */
	OR_6("OR6"),
	/**
	 * 资金运用业务线
	 */
	OR_4("OR4"),
	/**
	 * 公司治理业务线
	 */
	OR_5("OR5"),
	/**
	 * 准备金、再保险管理
	 */
	OR_7("OR7"),
	/**
	 * 信息系统
	 */
	OR_8("OR8"),
	/**
	 * 案件管理
	 */
	OR_9("OR9"),
	/**
	 * 战略风险
	 */
	SR("SR"),
	/**
	 * 声誉风险
	 */
	RR("RR"),
	/**
	 * 流动性风险
	 */
	LR("LR"),
	/**
	 * 操作风险
	 */
	OR("OR"),
	/**
	 * 分隔符
	 */
	SEPARATOR("-"),
	/**
	 * 五大类采集频率-年
	 */
	FIVE_PRPJ_COLL_FREQ_YEAR("Y"),
	/**
	 * 五大类采集频率-季度
	 */
	FIVE_PRPJ_COLL_FREQ_QUARTER("Q"),
	/**
	 * 渠道标识
	 */
	CHANNEL_FLAG("channelFlag"),
	/**
	 * zip包后缀
	 */
	ZIP_FILE_SUFFIX(".zip"),
	/**
	 * ZIP输入流编码
	 */
    ZIP_INPUTSTREAM_CHARSET("GBK"),
    /**
     * irr模块业务admin
     */
    ROLE_IRR_ADMIN("irrAdmin"),
    /**
     * 流动性风险大类编码
     */
    LR01_TYPE_CODE("LR01"),
    /**
     * 流动性风险-百分制后指标编码
     */
    LR01_CENT_AFTER_CODE("LR01031"),
    /**
     * 默认结果保留小数位数
     */
    DEFAULT_NUMBER_SCALE("4"),
    /**
     * 默认分数保留小数位数
     */
    DEFAULT_SCORE_SCALE("2"),
    /**
     * 机构部门包含
     */
    ORG_DEPT_NAME("-总部"),
    /**
     * 机构包含
     */
    ORG_ORG_NAME("公司"),
    /**
     * sheet页目录名称
     */
    SHEET_DIREC_NAME("目录"),
    /**
     * 分支机构隶属保监局拆分指标
     */
    SPLIT_BRANCH_MEMB_BUREAU_CODE("FM020150000"),
    /**
     * admin角色编码
     */
    ADMIN_ROLE_CODE("admin"),
    /**
     * IT的admin角色
     */
    IR_IT_ROLE_CODE("irrIT");
	
	private String value;
	
	private IrrEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
