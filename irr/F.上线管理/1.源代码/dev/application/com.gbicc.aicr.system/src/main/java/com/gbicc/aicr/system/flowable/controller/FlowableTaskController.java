package com.gbicc.aicr.system.flowable.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wsp.framework.flowable.service.ProcessOperationService;
import org.wsp.framework.flowable.service.ProcessQueryService;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.jpa.service.support.protocol.criteria.Criteria;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.security.util.SecurityUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbicc.aicr.system.flowable.entity.TaskInfo;
import com.gbicc.aicr.system.flowable.service.FlowableTaskService;

@Controller
@RequestMapping("/system/flowable/task")
public class FlowableTaskController {
	private static final Logger LOG = LogManager.getLogger(FlowableTaskController.class);
	@Autowired
	private FlowableTaskService flowableTaskService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RepositoryService repositoryService;
	// 运行时 服务
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessOperationService processOperationService;
	@Autowired
	private ProcessQueryService processQueryService;
	private static final String PATH = "gbicc/aicr/view/flowable/";

	/**
	 * 获取当前登录者所有待办任务数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "todo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, ? extends Object> getTodoTaskList() throws Exception {
		//待办任务列表
		List<Map<String, Object>> taskList = flowableTaskService.getTodoTaskList();
		return getModelMap(taskList);

	}

	/**
	 * 查询已办任务
	 * @return 已办任务集
	 */
	@RequestMapping(value = "done", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, ? extends Object> getDoneTaskList() throws Exception {
		// 已办任务列表
		List<Map<String, Object>> taskList = flowableTaskService.getDoneTaskList();
		return getModelMap(taskList);
	}

	/**
	 * 完成任务操作
	 * @param data 信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/completeTask", method = RequestMethod.POST)
	public Map<String, Object> completeTask(@RequestBody String data){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Map<String, Object> dataMap = objectMapper.readValue(data, Map.class);
			String id = (String) dataMap.get("id");
			String path = (String) dataMap.get("path");
			String comment = (String) dataMap.get("comment");
			if (path != null && "submit".equals(path)) {
				comment = "(通过)" + comment;
			} else {
				comment = "(退回)" + comment;
			}
			flowableTaskService.completeTask(id, null, dataMap, comment);
			map.put("flag", true);
			map.put("data", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
			map.put("data", e.getMessage());
		}
		return map;
	}

	/**
	 * 获取当前登录者所有待办任务数据
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "todo_list", method = RequestMethod.POST)
	public Map<String, Object> todoTaskList(@RequestParam(name = "param", required = false) @RequestBody String data,
			@RequestParam(name = "page", required = false) Integer pageIndex,
			@RequestParam(name = "rows", required = false) Integer pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(data)) {
			try {
				map = objectMapper.readValue(data, Map.class);
			} catch (IOException e) {
				System.err.println("格式化参数异常");
				e.printStackTrace();
			}
			// 获取条件
			String processKey = (String) map.get("processKey");
			String createDate1 = (String) map.get("createDate1");
			String createDate2 = (String) map.get("createDate2");
			String dueDate1 = (String) map.get("dueDate1");
			String dueDate2 = (String) map.get("dueDate2");
			return flowableTaskService.getTodoTaskList(processKey, createDate1, createDate2, dueDate1, dueDate2,
					(pageIndex - 1) * pageSize + 1, pageSize);
		}
		return flowableTaskService.getTodoTaskList(null, null, null, null, null, (pageIndex - 1) * pageSize + 1,
				pageSize);
	}

	/**
	 * 查询已办任务
	 * @return 已办任务集
	 */
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "done_list", method = RequestMethod.POST)
	public Map<String, Object> doneTaskList(@RequestParam(name = "param", required = false) @RequestBody String data,
			@RequestParam(name = "page", required = false) Integer pageIndex,
			@RequestParam(name = "rows", required = false) Integer pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(data)) {
			try {
				map = objectMapper.readValue(data, Map.class);
			} catch (IOException e) {
				LOG.error("格式化参数异常");
				e.printStackTrace();
			}
			// 获取条件
			String processKey = (String) map.get("processKey");
			String createDate1 = (String) map.get("createDate1");
			String createDate2 = (String) map.get("createDate2");
			String finishDate1 = (String) map.get("finishDate1");
			String finishDate2 = (String) map.get("finishDate2");
			return flowableTaskService.getDoneTaskList(processKey, createDate1, createDate2, finishDate1, finishDate2,
					(pageIndex - 1) * pageSize + 1, pageSize);
		}
		return flowableTaskService.getDoneTaskList(null, null, null, null, null, (pageIndex - 1) * pageSize + 1,
				pageSize);
	}

	/**
	 * 查询已完成任务
	 * @return 已完成任务集
	 */
	@RequestMapping(value = "complete", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, ? extends Object> getCompleteTaskList() throws Exception {
		// 已完成任务列表
		List<Map<String, Object>> taskList = flowableTaskService.getCompleteTaskList();
		return getModelMap(taskList);
	}

	protected Map<String, ? extends Object> getModelMap(Object data) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("data", data);
		modelMap.put("success", true);
		return modelMap;
	}

	@RequestMapping(value = "deal_task_app.html", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, ? extends Object> dealTaskApp(@RequestParam(name = "taskId", required = false) String taskId)
			throws Exception {
		Map<String,Object> info = new HashMap<String,Object>();
		List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
		if (tasks.size() > 0) {
			Task task = (Task) tasks.get(0);
			List<TaskInfo> ehpil = flowableTaskService.myFinishedTaskDetailByProIns(task.getProcessInstanceId());
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(task.getProcessDefinitionId()).singleResult();
			info.put("finishedTaskInfos", ehpil);
			info.put("taskId", task.getId());
			info.put("taskName", task.getName());
			info.put("assignee", task.getAssignee());
			info.put("processDefinitionKey", processDefinition.getKey());
			info.put("startUser", processInstance.getStartUserId());
		}
		return info;
	}

	/**
	 * PC处理待办任务
	 * @param taskId 任务ID
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deal_task.html", method = RequestMethod.GET)
	public ModelAndView dealTask(@RequestParam(name = "taskId", required = false) String taskId, HttpServletRequest req)
			throws Exception {
		ModelAndView mv = new ModelAndView(PATH + "deal.html");
		List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
		if (tasks.size() > 0) {
			Task task = (Task) tasks.get(0);
			//List<TaskInfo> ehpil = flowableTaskService.myFinishedTaskDetailByProIns(task.getProcessInstanceId());
			List<TaskInfo> ehpil = flowableTaskService.myFinishedTaskDetailByProInsByMyself(task.getProcessInstanceId(),task.getProcessDefinitionId());

			String loginName=SecurityUtil.getLoginName();
			for(TaskInfo ti:ehpil) {
				ti.getOwner();
				System.err.println("");
			}
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(task.getProcessDefinitionId()).singleResult();
			User example = new User();
			QueryParameter queryParameter = new QueryParameter();
			List<Criteria> criteriaList = Criteria.parse("{\"fieldName\":\"enable\",\"operator\":\"equals\",\"value\":true}");
			queryParameter.setCriterias(criteriaList);
			queryParameter.setPage(0);
			queryParameter.setSize(1000);
			List<User> users = userService.fetch(example, queryParameter);
			Map<String, String> userMap = new HashMap<String, String>();
			for (User user : users) {
				userMap.put(user.getLoginName(), user.getUserName());
			}
			Map<String, Object> varsMap = taskService.getVariablesLocal(task.getId());
			mv.addObject("vars", varsMap);
			mv.addObject("users", userMap);
			mv.addObject("finishedTaskInfos", ehpil);
			mv.addObject("processInstance", processInstance);
			mv.addObject("processDefinition", processDefinition);
			mv.addObject("task", task);
		}
		return mv;
	}

	/*
	 * 查看已办任务
	 * 
	 */
	@RequestMapping(value = "view_task.html", method = RequestMethod.GET)
	public ModelAndView viewTask(@RequestParam(name = "taskId", required = false) String taskId) throws Exception {
		ModelAndView mv = new ModelAndView(PATH + "view.html");
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		if (task != null) {
			//myFinishedTaskDetailByProInsByMyself
			//List<TaskInfo> ehpil = flowableTaskService.myFinishedTaskDetailByProIns(task.getProcessInstanceId());
			String processDefinitionId = task.getProcessDefinitionId();
			List<TaskInfo> ehpil = flowableTaskService.myFinishedTaskDetailByProInsByMyself(task.getProcessInstanceId(),processDefinitionId);
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			if (processInstance == null) {
				HistoricProcessInstance hisProcessInstance = historyService.createHistoricProcessInstanceQuery()
						.processInstanceId(task.getProcessInstanceId()).singleResult();
				mv.addObject("processInstance", hisProcessInstance);
			} else {
				mv.addObject("processInstance", processInstance);
			}
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(task.getProcessDefinitionId()).singleResult();
			User example = new User();
			QueryParameter queryParameter = new QueryParameter();
			List<Criteria> criteriaList = Criteria
					.parse("{\"fieldName\":\"enable\",\"operator\":\"equals\",\"value\":true}");
			queryParameter.setCriterias(criteriaList);
			queryParameter.setPage(0);
			queryParameter.setSize(1000);
			List<User> users = userService.fetch(example, queryParameter);
			Map<String, String> map = new HashMap<String, String>();
			for (User user : users) {
				map.put(user.getLoginName(), user.getUserName());
			}
			mv.addObject("users", map);
			mv.addObject("finishedTaskInfos", ehpil);
			mv.addObject("processDefinition", processDefinition);
			mv.addObject("task", task);
			mv.addObject("isAudit", true);
		}
		return mv;
	}

	/*
	 * 已办任务列表页面
	 */
	@RequestMapping("done.html")
	public String doneTaskList_view() {
		return PATH + "done.html";
	}

	@RequestMapping("index3.html")
	public String enterTarger() {
		return PATH + "index3.html";
	}

	/*
	 * 待办任务列表页面
	 */
	@RequestMapping("todo.html")
	public String todoTaskList_view() {
		return PATH + "todo.html";
	}

	/**
	 * 根据业务唯一编码查询审批意见
	 * @param id 业务唯一编码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findProcessComment")
	public Map<String, Object> findProcessCommentByBusinessKey(String id){
		if(StringUtils.isBlank(id)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(id).singleResult();
			if(processInstance != null) {
				map.put("data", flowableTaskService.myFinishedTaskDetailByProIns(processInstance.getId()));
			}
			map.put("flag", true);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
			map.put("flag", false);
		}
		return map;
	}
}
