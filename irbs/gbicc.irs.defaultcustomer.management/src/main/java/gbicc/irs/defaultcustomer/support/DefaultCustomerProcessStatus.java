package gbicc.irs.defaultcustomer.support;

/**
 * 违约客户流程状态
 * @author kou
 *
 */
public enum DefaultCustomerProcessStatus {
	//=========================旧流程状态======================================
	NOT_SUBMIT,			//待确认 (对于系统发起、分支行/总行管理人员发起的申请需要进行违约认定确认)
	IN_APPROVAL,		//审批中(当前客户经理已经提交给上级，正在审查审批中的违约认定申请)
	RGRD_WY,           //人工认定-违约(当前已经被人工审批通过的违约认定申请)
	RGRD_FWY,           //人工认定-非违约(在审查审批流程中被审批人否决的违约认定申请)
	XTRD_WY,            //系统认定-违约 (系统直接认定该客户为违约)
	CSRD_WY,           //超时认定-违约  (当前因超过时限被系统自动认定为违约的违约认定申请)
	COMPLETED,			//已完成-终止 (系统终止或在经过线下沟通被管理员终止的违约认定申请)
	SEND_BACK,			//退回
	UNDONE	,			//撤销
	
	//=========================违约认定流程状态======================================================
	TO_BE_CONFIRMED,   //待确认
	APPROVAL,          //审批中
	MAN_DEFAULT,       //人工认定－违约
	MAN_NO_DEFAULT,    //人工认定－非违约
	SYSTEM_DEFAULT,    //系统认定-违约	
	OVERTIME_DEFAULT,  //超时认定-违约
	COMPLETED_TERMINATION, //已完成-终止
	
	//=========================违约重生流程状态======================================================
	COMPLETED_NOREBORN,//已完成-不重生
	COMPLETED_REBORN,  //已完成-重生
	COMPLETED_STOP  //已完成-终止
}
