package com.gbicc.aicr.system.flowable.entity;
import java.util.Date;
public class TaskInfo {


	// 任务ID
	private String id;

	// 任务名称
	private String name;
	
	// 申请人
	private String user;
	
	// 流程名
	private String processName;
	
	//任务描述
	private String taskNameDesc;
	
	//创建时间
	private Date createDate;
	
	//完成时间
	private Date finishDate;
	
	//所有人
	private String owner;
	
	//姓名（账号）
	private String ownerInfo;
	
	//任务提交内容
	private String taskContent;
	
	//任务审批意见
	private String taskComment;
	
	//任务审批是否通过
	private String taskCommit;
	
	//任务为角色时，统计有多少人分配到了此任务
	//private String getTaskUsers;
	//public String getGetTaskUsers() {
	//	return getTaskUsers;
	//}

	//public void setGetTaskUsers(String getTaskUsers) {
	//	this.getTaskUsers = getTaskUsers;
	//}

	public String getTaskCommit() {
		return taskCommit;
	}

	public void setTaskCommit(String taskCommit) {
		this.taskCommit = taskCommit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTaskNameDesc() {
		return taskNameDesc;
	}

	public void setTaskNameDesc(String taskNameDesc) {
		this.taskNameDesc = taskNameDesc;
	}
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

	public String getTaskComment() {
		return taskComment;
	}

	public void setTaskComment(String taskComment) {
		this.taskComment = taskComment;
	}

	public String getOwnerInfo() {
		return ownerInfo;
	}

	public void setOwnerInfo(String ownerInfo) {
		this.ownerInfo = ownerInfo;
	}
}
