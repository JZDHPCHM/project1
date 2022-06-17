package net.gbicc.app.irr.jpa.support.enums;
/**
* irr流程枚举类
*
*/
public enum IrrProcessEnum {

	/**
	 * IRR流程定义的key
	 */
	IRR_PROCESS_DEFINITION_KEY("IRRCOLL"),
	/**
	 * 流程中采集节点定义的ID
	 */
	COLL_USER_TASK_ID("sid-80D79B8D-D082-4F78-AADA-97FCC94D8EE7"),
	/**
	 * 可用
	 */
	ENABLE(1),
	/**
	 * irr流程采集角色编码
	 */
	IRR_PROCESS_COLL("irrProcessColl"),
	/**
	 * irr流程审核角色编码
	 */
	IRR_PROCESS_EXAM("irrProcessExam"),
	/**
	 * irr流程复核角色编码
	 */
	IRR_PROCESS_REVIEW("irrProcessReview"),
	/**
	 * irr流程汇总角色编码
	 */
	IRR_PROCESS_SUMM("irrProcessSumm"),
	/**
	 * irr流程总经理角色编码
	 */
	IRR_PROCESS_MANAGER("irrProcessManager"),
	/**
	 * irr流程报数角色节点
	 */
	IRR_PROCESS_REPORT("irrProcessReport"),
	/**
	 * 分公司采集子流程
	 */
	IRR_SUB_PROCESS_BRANCH("BRANCH"),
	/**
	 * 分公司后缀
	 */
	IRR_BRANCH_SUFFIX("分公司"),
	/**
	 * 总公司采集子流程
	 */
	IRR_SUB_PROCESS_HEAD("HEAD"),
	/**
	 * 总公司后缀
	 */
	IRR_HEAD_SUFFIX("总部"),
	/**
	 * 任务状态-采集中
	 */
	TASK_STATUS_CJZ("CJZ"),
	/**
	 * 任务状态-汇总中
	 */
	TASK_STATUS_HZZ("HZZ"),
	/**
	 * 任务状态-总经理审核
	 */
	TASK_STATUS_ZJLSH("ZJLSH"),
	/**
	 * 任务状态-上报中
	 */
	TASK_STATUS_SBZ("SBZ"),
	/**
	 * 总公司子流程采集人集合编码
	 */
	IRR_SUB_PROCESS_HEAD_VAR_CODE("headUsers"),
	/**
	 * 总公司子流程采集人编码
	 */
	IRR_SUB_PROCESS_HEAD_COLL_USER_CODE("headCollUser"),
	/**
	 * 总公司子流程审核人集合编码
	 */
	IRR_SUB_PROCESS_HEAD_EXAM_VAR_CODE("headExamUsers"),
	/**
	 * 总公司子流程复核人集合编码
	 */
	IRR_SUB_PROCESS_HEAD_REVIEW_VAR_CODE("headReviewUsers"),
	/**
	 * 分公司子流程采集人集合编码
	 */
	IRR_SUB_PROCESS_BRANCH_VAR_CODE("branchUsers"),
	/**
	 * 分公司子流程采集人编码
	 */
	IRR_SUB_PROCESS_BRANCH_COLL_USER_CODE("branchCollUser"),
	/**
	 * 分公司子流程审核人集合编码
	 */
	IRR_SUB_PROCESS_BRANCH_EXAM_VAR_CODE("branchExamUsers"),
	/**
	 * 分公司子流程复核人集合编码
	 */
	IRR_SUB_PROCESS_BRANCH_REVIEW_VAR_CODE("branchReviewUsers"),
	/**
	 * 流程默认意见
	 */
	IRR_PROCESS_DEFAULT_COMMENT("通过"),
	/**
	 * 流程线流转变量
	 */
	IRR_PROCESS_PATH_CODE("path"),
	/**
	 * 流程线退回变量编码
	 */
	IRR_PROCESS_BACK_CODE("back"),
	/**
	 * 流程线提交变量编码
	 */
	IRR_PROCESS_SUBMIT_CODE("submit"),
	/**
	 * 子流程标识采集人的编码
	 */
	IRR_SUB_PROCESS_COLL_FLAG("collUserLoginName"),
	/**
	 * 汇总节点退回到分公司流转条件
	 */
	IRR_SUMM_PATH_BRANCH_CODE("backBranch"),
	/**
	 * 汇总节点退回到总公司流转条件
	 */
	IRR_SUMM_PATH_HEAD_CODE("backHead"),
	/**
	 * IRR审批意见提交前缀
	 */
	IRR_COMMENT_SUBMIT_PREFIX("(通过)"),
	/**
	 * IRR审批意见退回前缀
	 */
	IRR_COMMENT_BACK_PREFIX("(退回)"),
	
	/**
	 * 子流程变量编码
	 */
	IRR_SUB_PROCESS_VAR_CODE("users"),
	/**
	 * 子流程采集节点编码
	 */
	IRR_SUB_PROCESS_COLL_CODE("collUser"),
	/**
	 * 子流程审核节点编码
	 */
	IRR_SUB_PROCESS_EXAM_CODE("examUsers"),
	/**
	 * 子流程复核节点编码
	 */
	IRR_SUB_PROCESS_REVIEW_CODE("reviewUser"),
	/**
	 * 汇总-结果-渠道
	 */
	SUMM_RESULT_FLAG_CHANNEL("channel"),
	/**
	 * 汇总-结果-机构
	 */
	SUMM_RESULT_FLAG_ORG("org");
	
	private Object value;

	private IrrProcessEnum(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
}
