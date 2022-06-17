package net.gbicc.app.irr.jpa.support.enums;
/**
* irr流程枚举类
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
	 * 分公司后缀
	 */
	IRR_BRANCH_SUFFIX("分公司"),
	/**
	 * 总公司后缀
	 */
	IRR_HEAD_SUFFIX("总部"),
	/**
	 * 任务状态-采集中
	 */
	TASK_STATUS_CJZ("CJZ"),
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
	 * IRR审批意见提交前缀
	 */
	IRR_COMMENT_SUBMIT_PREFIX("(通过)"),
	/**
	 * IRR审批意见退回前缀
	 */
	IRR_COMMENT_BACK_PREFIX("(退回)"),
	/**
	 * 一票全过审批意见
	 */
	A_TICKET_COMMENT("一票全过"),
	
	/**
	 * 子流程变量编码
	 */
	IRR_SUB_PROCESS_VAR_CODE("users"),
	/**
	 * 子流程采集节点编码
	 */
	IRR_SUB_PROCESS_COLL_CODE("collUser"),
	/**
     * irr流程节点标识-采集
     */
    IRR_PROCESS_NODE_COLL("coll"),
    /**
     * 会签子流程采集角色前缀
     */
	IRR_SIGN_PROCESS_COLL_PREFIX("irrProcessSignColl"),
	/**
	 * 会签子流程审核角色前缀
	 */
	IRR_SIGN_PROCESS_EXAM_PREFIX("irrProcessSignExam"),
	/**
	 * 会签子流程采集节点变量
	 */
	IRR_SIGN_PROCESS_COLL_VAR("collUsers"),
	/**
	 * 会签子流程审核节点变量
	 */
	IRR_SIGN_PROCESS_EXAM_VAR("examUsers"),
	/**
	 * 总公司子流程变量
	 */
	IRR_HEAD_PROCESS_VAR("headUsers"),
	/**
	 * 分公司子流程变量
	 */
	IRR_BRANCH_PROCESS_VAR("branchUsers"),
	/**
	 * 会签子流程变量
	 */
	IRR_SIGN_PROCESS_VAR("signUsers"),
	/**
	 * 会签子流程任务节点前缀
	 */
	IRR_SIGN_NAME_PREFIX("会签"),
	/**
	 * 会签子流程审核节点一票否决变量
	 */
    IRR_SIGN_EXAM_ONE_TICKET_VETO("veto"),
    /**
     * 会签流程前缀
     */
    IRR_SIGN_PROCESS("irrProcessSign"),
    /**
     * 计划期次
     */
    PLAN_ISSUE_CODE("planIssue");
	
	private Object value;

	private IrrProcessEnum(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
}
