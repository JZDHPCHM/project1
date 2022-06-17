package com.gbicc.aicr.system.flowable.service;

import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;

import com.gbicc.aicr.system.flowable.entity.TaskInfo;

/**
 * 任务查询服务
 * @author lingzhigang
 *
 */
public interface FlowableTaskService {
	public HistoryService getHistoryService();
	public RepositoryService getRepositoryService();
	public TaskService getTaskService();
	public RuntimeService getRuntimeService();
	
	/**
	 * 查询待办任务
	 * @return 待办任务集
	 */
	public List<Map<String, Object>> getTodoTaskList() throws Exception;
	
	/**
	 * 查询已办任务
	 * @return 已办任务集
	 */
	public List<Map<String, Object>> getDoneTaskList();
	
	/**
	 * 查询待办任务
	 * @return 待办任务集
	 */
	public Map<String, Object> getTodoTaskList(String processKey,String createDate1,String createDate2,
			String dueDate1,String dueDate2,int start,int size) throws Exception;
	
	/**
	 * 查询已办任务
	 * @return 已办任务集
	 */
	public Map<String, Object> getDoneTaskList(String processKey,String createDate1,String createDate2,String finishDate1,String finishDate2,int start,int size) ;
	/**
	 * 查询已完成任务
	 * @return 已完成任务集
	 */
	public List<Map<String, Object>> getCompleteTaskList();
	
	/**
	 * 完成任务
	 * @param taskId 流程任务ID
	 * @param variables 持久化变量
	 * @param transientVariables 瞬态变量
	 * @param comment 意见
	 * @throws Exception
	 */
	public void completeTask(String taskId, Map<String, Object> variables,Map<String, Object> transientVariables,String comment) throws Exception ;
	
	/**
	 * 完成任务
	 * @param taskId 流程任务ID
	 * @param variables 持久化变量
	 * @param transientVariables 瞬态变量
	 * @param variablesLocal 本地化变量（当前需要完成的本地化变量）
	 * @param newTaskVariablesLocal 本地化变量(当前任务完成后，产生的新任务的本地化变量)
	 * @param comment 意见
	 * @throws Exception
	 */
	public void completeTask(String taskId, Map<String, Object> variables,Map<String, Object> transientVariables,
			Map<String,Object> variablesLocal,Map<String,Object> newTaskVariablesLocal,String comment) throws Exception ;
	
	public void deleteProcessInstanceByTaskId(String taskId,String comment) throws Exception;
	
	public List<Map<String, String>> getRPMultipleUser() throws Exception;
	public List<String> getNextTaskSameDeptUsersByRole(String lastTaskUserVar,String roleCode) throws Exception;
	public List<String> getDeptUserByDeptAndRole(String deptCode,String roleCode) ;
	public List<String> getUserDeptId(String userId) ;
	
	/**
	 * 根据流程实例ID查询审批意见
	 * @param proInsId 流程实例ID
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfo> myFinishedTaskDetailByProIns(String proInsId) throws Exception;
	
	/**
	 * 根据业务唯一主键查询审批意见
	 * @param bussinessKey 业务唯一主键
	 * @return
	 * @throws Exception
	 */
	public List<TaskInfo> findFinishedTaskDetailByBussinessKey(String bussinessKey) throws Exception;

	/**
	 * 根据条件查询历史办理人
	 * @param bussinessKey 业务唯一主键
	 * @param actId 执行ID
	 * @return
	 * @throws Exception
	 */
	public List<String> findAssignByCondition(String bussinessKey,String actId) throws Exception;
	
	/**
	 * 任务信息设置其他信息
	 * @param taskInfoList 任务信息
	 * @throws Exception
	 */
	public void setOtherTaskInfo(List<Map<String, Object>> taskInfoList) throws Exception;
	
    /**
     * 查找授权人的账号
     *
     * @param userId
     *            当前人ID
     * @return
     * @throws Exception
     */
    public List<String> findAuthUserAssignee(String userId) throws Exception;

    /**
     * 查询任务的办理人
     *
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    public String findTaskAssignee(String taskId) throws Exception;

    /**
     * 判断登录账号和任务办理人是否为同一个人
     *
     * @param taskId
     *            任务ID
     * @param userLoginName
     *            登录账号
     * @return
     * @throws Exception
     */
    public Boolean taskAssigneeIsSame(String taskId, String userLoginName) throws Exception;

    /**
     * 获取任务真实的办理人的用户ID
     *
     * @param taskId
     *            任务ID
     * @param userLoginName
     *            登录账号
     * @return
     * @throws Exception
     */
    public String getTaskTrueAssigneeUserId(String taskId, String userLoginName) throws Exception;

    /**
     * 获取计划的期次
     *
     * @param procInstId
     *            流程执行实例ID
     * @return
     * @throws Exception
     */
    public String findPlanIssueByProcInstId(String procInstId) throws Exception;
    /**
     * 根据流程实例ID查询自己的审批意见
     * @param proInsId 流程实例ID
     * @return
     * @throws Exception
     */
	List<TaskInfo> myFinishedTaskDetailByProInsByMyself(String proInsId,String processDefinitionId) throws Exception;

}
