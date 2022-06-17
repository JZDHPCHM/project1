package com.gbicc.aicr.system.in2oa.service;

import java.util.Date;

public interface TaskToOAService {
	/**
	 * syscode 异构系统标识
	 * flowid 流程任务id
	 * requestname 标题
	 * workflowname 流程类型名称
	 * nodename 步骤名称（节点名称）
	 * pcurl PC地址
	 * appurl APP地址
	 * isremark 流程处理状态 (0：待办 2：已办 4：办结)
	 * viewtype 流程查看状态(0：未读 1：已读)
	 * creator 创建人（原值）
	 * createdatetime 创建日期时间
	 * receiver 接收人（原值）
	 * receivedatetime 接收日期时间
	 */
	public void sendTaskInfo(String syscode
			,String flowid,
			String requestname,
			String workflowname,
			String nodename,
			String pcurl,
			String appurl,
			String isremark,
			String viewtype, 
			String creator,
			Date createdatetime,
			String receiver,
			Date receivedatetime
			);
	//public void sendFinishTaskInfo();
	
}
