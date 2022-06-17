package com.gbicc.aicr.system.flowable.service.impl;


import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.wsp.framework.flowable.entity.Agent;
import org.wsp.framework.flowable.exception.NoAvailableAssigneeException;
import org.wsp.framework.flowable.service.AgentService;
import org.wsp.framework.flowable.service.AssigneeQueryService;
import org.wsp.framework.flowable.service.ProcessOperationService;
import org.wsp.framework.flowable.support.Assignee;
import org.wsp.framework.flowable.support.CompleteTaskException;
import org.wsp.framework.flowable.support.FrameworkVariableNames;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbicc.aicr.system.flowable.service.FlowableProcessOperationService;
import com.gbicc.aicr.system.flowable.support.enums.FlowableEnum;

@Service
public class FlowableProcessOperationServiceImpl implements FlowableProcessOperationService{
	
	@Autowired private RuntimeService runtimeService;
	@Autowired private RepositoryService repositoryService;
	@Autowired private TaskService taskService;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private AssigneeQueryService assigneeQueryService;
	@Autowired private AgentService agentService;
	@Autowired private ProcessOperationService processOperationService;
	
	@Transactional
	public ProcessInstance startProcessByKey(String startUser,String processDefinitionKey, String bussinessKey,Map<String, Object> variables, Map<String, Object> transientVariables, boolean autoCompleteFirstTask) throws Exception {
		Authentication.setAuthenticatedUserId(startUser);
		ProcessInstanceBuilder builder =runtimeService.createProcessInstanceBuilder();
		builder.processDefinitionKey(processDefinitionKey);
		builder.businessKey(bussinessKey);
		if(variables != null){
			variables.put(FlowableEnum.VAR_BUSINESS_KEY_CODE.getValue(), bussinessKey);
			builder.variables(variables);
		}else {
			variables = new HashMap<String, Object>();
			variables.put(FlowableEnum.VAR_BUSINESS_KEY_CODE.getValue(), bussinessKey);
			builder.variables(variables);
		}
		builder.transientVariables(convertTransientVariables(variables,transientVariables));
		ProcessInstance processInstance =builder.start();
		return startProcess(processInstance,variables,transientVariables,autoCompleteFirstTask);
	}
	
	private Map<String,Object> convertTransientVariables(Map<String, Object> variables,Map<String,Object> transientVariables){
		Map<String, Object> _transientVariables =new HashMap<String,Object>();
		if(transientVariables!=null){
			_transientVariables.putAll(transientVariables);
		}
		
		if(!_transientVariables.containsKey(FrameworkVariableNames.GO_BACK) && (variables==null || !variables.containsKey(FrameworkVariableNames.GO_BACK))){
			_transientVariables.put(FrameworkVariableNames.GO_BACK, Integer.MIN_VALUE);
		}
		if(!_transientVariables.containsKey(FrameworkVariableNames.ASSIGNEE) && (variables==null || !variables.containsKey(FrameworkVariableNames.ASSIGNEE))){
			_transientVariables.put(FrameworkVariableNames.ASSIGNEE, null);
		}
		
		if(_transientVariables.containsKey(FrameworkVariableNames.FIRST_TASK_ASSIGNEE)) {
			_transientVariables.put(FrameworkVariableNames.ASSIGNEE, _transientVariables.get(FrameworkVariableNames.FIRST_TASK_ASSIGNEE));
		}
		return _transientVariables;
	}
	private ProcessInstance startProcess(ProcessInstance processInstance,Map<String, Object> variables, Map<String, Object> transientVariables,boolean autoCompleteFirstTask) throws Exception{
		List<Task> tasks =taskService.createTaskQuery().processInstanceId(processInstance.getId()).includeProcessVariables().list();
		//如果未找到新任务,表示流程结束,正常返回
		if(tasks==null || tasks.size()==0){
			return processInstance;
		}
		//如果找到新任务,且存在多个新任务,暂时不处理,正常返回
		if(tasks.size()>1){
			return processInstance;
		}
		//获取到产生的新任务
		Task newTask =tasks.get(0);
		
		//如果需要,自动完成第一个任务
		if(autoCompleteFirstTask) {
			processOperationService.completeTask(newTask.getId(),variables,transientVariables);
			return processInstance;
		}
		
		//如果新任务已经成功分配到了处理人,正常返回
		if(StringUtils.hasText(newTask.getAssignee())){
			//查找代理人
			Agent agent =getAgent(newTask.getAssignee());
			if(agent!=null){
				taskService.setAssignee(newTask.getId(), agent.getAgentLoginName());
			}
			return processInstance;
		}
		
		String taskDefinitionKey =newTask.getTaskDefinitionKey();
		String procDefinitionId =newTask.getProcessDefinitionId();
		//获取到流程定义模型对象
		BpmnModel model = repositoryService.getBpmnModel(procDefinitionId);
		Collection<FlowElement> elements =model.getMainProcess().getFlowElements();
		UserTask newUserTaskDefinition =null;
		if(elements!=null && elements.size()>0){
			for(FlowElement element : elements){
				if(taskDefinitionKey.equals(element.getId()) && (element instanceof UserTask)){
					newUserTaskDefinition =(UserTask)element;
					break;
				}
			}
		}
		if(newUserTaskDefinition==null){
			return processInstance;
		}
		
		//查找候选处理人
		List<Assignee> assignees =assigneeQueryService.query(null,newTask, newUserTaskDefinition);
		if(assignees!=null && assignees.size()>0){
			if(assignees.size()>1){
				throw new CompleteTaskException(objectMapper.writeValueAsString(assignees));
			}else{
				taskService.setAssignee(newTask.getId(), assignees.get(0).getLoginName());
				return processInstance;
			}
		}else{
			throw new NoAvailableAssigneeException();
		}
	}
	private Agent getAgent(String loginName){
		List<Agent> agents =agentService.getAgents(loginName);
		if(agents!=null && agents.size()>0){
			for(Agent agent : agents){
				Date startDate =agent.getStartDate();
				Date endDate =agent.getEndDate();
				Date now =new Date();
				if(now.getTime()>=startDate.getTime() && now.getTime()<=endDate.getTime()){
					return agent;
				}
			}
		}
		return null;
	}
}
