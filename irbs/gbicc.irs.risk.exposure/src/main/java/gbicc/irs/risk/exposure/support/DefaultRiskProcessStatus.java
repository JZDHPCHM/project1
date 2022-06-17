package gbicc.irs.risk.exposure.support;

/**
 * 违约客户流程状态
 * @author kou
 *
 */
public enum DefaultRiskProcessStatus {
	NOT_SUBMIT,			//未提交
	IN_APPROVAL,		//审批中
	COMPLETED,			//已完成同意
	COMPLETED_STOP,     //已完成终止
	//SEND_BACK,			//退回
	//UNDONE,				//撤销
	
}
