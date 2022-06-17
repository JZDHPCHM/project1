package net.gbicc.app.irr.jpa.support.enums;
/**
* irr模块的枚举类
*
*/
public enum IrrEnum {
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
	COLL_EMPTY_INDEX_INFO("您没有需要采集的指标！"),
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
	 * 指标结构类型大类编码
	 */
	INDEX_RESULT_TYPE_CODE("indexResultType"),
	/**
	 * 指标数据验证参数-大类
	 */
	INDEX_DATA_VALID_PARAM_CODE("indexDataValidParam"),
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
	IRR_DEFAULT_PAGE("1");
	
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
