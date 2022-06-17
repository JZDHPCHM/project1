package com.gbicc.aicr.system.flowable.listener;

import java.lang.reflect.Method;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.gbicc.aicr.system.flowable.support.enums.FlowableEnum;

@Component("flowableTaskCompleteListener")
public class TaskCompleteListener implements TaskListener {
	
	private static final long serialVersionUID = 8908421623467300939L;
	private static final Logger log = LoggerFactory.getLogger(TaskCompleteListener.class);
	@Autowired
	private ApplicationContext applicationContext;
	
	private Expression submitBusiStatus;
	private Expression backBusiStatus;
	private Expression completeBusiMethod;
	private Expression nextStepNeedSameDeptRole;
	
	public void setNextStepNeedSameDeptRole(Expression nextStepNeedSameDeptRole) {
		this.nextStepNeedSameDeptRole = nextStepNeedSameDeptRole;
	}
	public Expression getNextStepNeedSameDeptRole() {
		return nextStepNeedSameDeptRole;
	}
	public void setSubmitBusiStatus(Expression submitBusiStatus) {
		this.submitBusiStatus = submitBusiStatus;
	}
	public Expression getSubmitBusiStatus() {
		return submitBusiStatus;
	}
	public Expression getCompleteBusiMethod() {
		return completeBusiMethod;
	}
	public void setCompleteBusiMethod(Expression completeBusiMethod) {
		this.completeBusiMethod = completeBusiMethod;
	}
	public Expression getBackBusiStatus() {
		return backBusiStatus;
	}
	public void setBackBusiStatus(Expression backBusiStatus) {
		this.backBusiStatus = backBusiStatus;
	}
	@Override
	public void notify(DelegateTask task) {
		try {
			if (task == null) {
				return;
			}
			if (completeBusiMethod!=null) {
				String[] ObjInfo=completeBusiMethod.getExpressionText().split("#");
				Object target=applicationContext.getBean(Class.forName(ObjInfo[0]));
				Method method=Class.forName(ObjInfo[0]).getMethod(ObjInfo[1],new Class[]{String.class,String.class});
				Object[] param = new Object[2];
		        param[0]= task.getVariable(FlowableEnum.VAR_BUSINESS_KEY_CODE.getValue()).toString();
		        String path=task.getTransientVariable("path").toString(); 
		        if (path==null) {
		        		param[1]=submitBusiStatus.getExpressionText();
		        }else if (path.equals("submit")){
		        		param[1]=submitBusiStatus.getExpressionText();
		        }else {
		        		param[1]=backBusiStatus.getExpressionText();
		        }
				ReflectionUtils.invokeMethod(method, target, param);
			}	
			/*FlowableTaskService flowableTaskService =applicationContext.getBean(FlowableTaskService.class);
			String ass = task.getAssignee();
			if (nextStepNeedSameDeptRole != null) {
				List<String> users=flowableTaskService.getNextTaskSameDeptUsersByRole(ass, nextStepNeedSameDeptRole.getExpressionText());
				if (users!=null) {
					task.setTransientVariable("assignee", users.get(0));
				}
			}
			TaskToOAService taskToOAService =applicationContext.getBean(TaskToOAService.class);
			if(taskToOAService==null) {
				log.warn("taskToOAService bean is NOT exists");
				return;
			}
			taskToOAService.sendTaskInfo("RPRL", task.getId(),"", "", task.getName(), (String)task.getVariableLocal("pcUrl"), (String)task.getVariableLocal("appUrl"), "2", "0", "", task.getCreateTime(), task.getAssignee(),task.getCreateTime());*/
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 任务创建时提醒
	 */
	// public void createTaskRemind(DelegateTask task, String strIsMRemind) {
	// Remind remind = new Remind();
	// // 提醒时间 创建 job 需要一定代码执行时间 所以提醒时间 向后推迟 1分钟
	// final Calendar cal = Calendar.getInstance();
	// cal.add(Calendar.MINUTE, 2);
	// Timestamp alertDate = new Timestamp(cal.getTimeInMillis());
	// remind.setAlertDate(alertDate.toString());
	// remind.setMessageType("1");
	// if (strIsMRemind != null && strIsMRemind.equals("1")) {
	// remind.setRemindWay("123");
	// } else {
	// remind.setRemindWay("12");
	// }
	// JSONObject jo = JSONObject.fromObject(task.getDescription());
	// String taskorgId = "";
	// String bussLine = "";
	// String taskNameDesc = "";// 描述
	//
	// if (jo.has("taskNameDesc")) {
	// taskNameDesc = jo.getString("taskNameDesc");
	// }
	// // ptask.setTaskNameDesc(taskNameDesc);
	// remind.setContent(task.getName() + "/" + taskNameDesc);
	// //
	// remind.setContent("您有新任务("+task.getName()+"/"+taskNameDesc+")提醒！请登录安邦偿付能力风险管理信息系统，进入任务中心办理。<br><p
	// // align=\"right\">安邦偿付能力风险管理信息系统</p>");
	// remind.setContent("新任务(" + task.getName() + "/" + taskNameDesc + ")");
	// remind.setMessageTitle("新任务(" + task.getName() + ")提醒");
	// remind.setCreateDate(DateUtil.getCurrentTimestamp());
	// remind.setAlertType("0");
	// remind.setState("0");
	// remind.setApId(task.getId());
	// try {
	// remindService.createRemind(remind, task.getAssignee());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 任务规定时间过半未处理时提醒
	 * 
	 * @param task
	 *            任务
	 * 
	 */
	// public void createTaskPerRemind(DelegateTask task, String strIsMRemind,
	// String strOverTime) {
	// Remind remind = new Remind();
	// // 提醒时间 创建 job 需要一定代码执行时间 所以提醒时间 向后推迟 1分钟
	// final Calendar cal = Calendar.getInstance();
	// cal.setTime(task.getCreateTime());
	//
	// String per = remindService.getTaskTimeRemindPer();
	// int taskOverTime = 2;
	// if (strOverTime != null && !strOverTime.equals("")) {
	// Integer.parseInt(strOverTime);
	// }
	// cal.add(Calendar.DATE, taskOverTime * Integer.parseInt(per) / 100);
	// Timestamp alertDate = new Timestamp(cal.getTimeInMillis());
	// remind.setAlertDate(alertDate.toString());
	// remind.setMessageType("1");
	// if (strIsMRemind != null && strIsMRemind.equals("1")) {
	// remind.setRemindWay("123");
	// } else {
	// remind.setRemindWay("12");
	// }
	// JSONObject jo = JSONObject.fromObject(task.getDescription());
	// String taskorgId = "";
	// String bussLine = "";
	// String taskNameDesc = "";// 描述
	//
	// if (jo.has("taskNameDesc")) {
	// taskNameDesc = jo.getString("taskNameDesc");
	// }
	// remind.setContent("催办任务(" + task.getName() + "/" + taskNameDesc + ")");
	// remind.setMessageTitle("催办任务(" + task.getName() + ")提醒");
	//
	// // remind.setContent("你有催办任务提醒！");
	// // remind.setMessageTitle(task.getName());
	// remind.setCreateDate(DateUtil.getCurrentTimestamp());
	// remind.setAlertType("0");
	// remind.setState("0");
	// remind.setApId(task.getId());
	// try {
	// remindService.createRemind(remind, task.getAssignee());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 任务超时时提醒
	 * 
	 * @param task
	 *            任务
	 */
	// public void createTaskOverTimeRemind(DelegateTask task, String strIsMRemind,
	// String strOverTime) {
	// Remind remind = new Remind();
	// // 提醒时间 创建 job 需要一定代码执行时间 所以提醒时间 向后推迟 1分钟
	// final Calendar cal = Calendar.getInstance();
	// cal.setTime(task.getCreateTime());
	// int taskOverTime = 2;
	// if (strOverTime != null && !strOverTime.equals("")) {
	// Integer.parseInt(strOverTime);
	// }
	// cal.add(Calendar.DATE, taskOverTime);
	// Timestamp alertDate = new Timestamp(cal.getTimeInMillis());
	// remind.setAlertDate(alertDate.toString());
	// remind.setMessageType("1");
	// if (strIsMRemind != null && strIsMRemind.equals("1")) {
	// remind.setRemindWay("123");
	// } else {
	// remind.setRemindWay("12");
	// }
	// JSONObject jo = JSONObject.fromObject(task.getDescription());
	// String taskorgId = "";
	// String bussLine = "";
	// String taskNameDesc = "";// 描述
	//
	// if (jo.has("taskNameDesc")) {
	// taskNameDesc = jo.getString("taskNameDesc");
	// }
	// remind.setContent("超时任务(" + task.getName() + "/" + taskNameDesc + ")");
	// remind.setMessageTitle("超时任务(" + task.getName() + ")提醒");
	//
	// // remind.setContent("你有超时任务提醒！");
	// // remind.setMessageTitle(task.getName());
	// remind.setCreateDate(DateUtil.getCurrentTimestamp());
	// remind.setAlertType("0");
	// remind.setState("0");
	// remind.setApId(task.getId());
	// try {
	// remindService.createRemind(remind, task.getAssignee());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}