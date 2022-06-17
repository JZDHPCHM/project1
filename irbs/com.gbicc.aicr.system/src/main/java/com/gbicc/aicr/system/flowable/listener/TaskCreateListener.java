package com.gbicc.aicr.system.flowable.listener;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.wsp.framework.communication.mail.service.MailSenderService;
import org.wsp.framework.core.util.JacksonObjectMapper;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.SystemDictionaryService;
import org.wsp.framework.mvc.service.UserService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gbicc.aicr.system.flowable.support.enums.MailEnum;
import com.gbicc.aicr.system.in2oa.service.TaskToOAService;

@Component("flowableTaskCreateListener")
public class TaskCreateListener implements TaskListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(TaskCreateListener.class);
	@Autowired
	private ApplicationContext applicationContext;

	private Expression overTime;
	private Expression appUrl;
	private Expression pcUrl;
	private Expression isMRemind;
	private Expression isSupportBack;
	private Expression othVariables;
	
	public Expression getOthVariables() {
		return othVariables;
	}

	public void setOthVariables(Expression othVariables) {
		this.othVariables = othVariables;
	}

	public void setIsSupportBack(Expression isSupportBack) {
		this.isSupportBack = isSupportBack;
	}

	public Expression getIsSupportBack() {
		return isSupportBack;
	}

	public Expression getOverTime() {
		return overTime;
	}

	public void setOverTime(Expression overTime) {
		this.overTime = overTime;
	}

	public Expression getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(Expression pcUrl) {
		this.pcUrl = pcUrl;
	}

	public Expression getAppUrl() {
		return pcUrl;
	}

	public void setAppUrl(Expression appUrl) {
		this.appUrl = appUrl;
	}

	public Expression getIsMRemind() {
		return isMRemind;
	}

	public void setIsMRemind(Expression isMRemind) {
		this.isMRemind = isMRemind;
	}

	@Override
	public void notify(DelegateTask task) {
		try {
			if (task == null) {
				return;
			}
			TaskToOAService taskToOAService = applicationContext.getBean(TaskToOAService.class);
			if (taskToOAService == null) {
				log.warn("taskToOAService bean is NOT exists");
				return;
			}
			String strOverTime = "2";
			if (overTime != null) {
				strOverTime = (String) overTime.getExpressionText();
			}
			final Calendar cal = Calendar.getInstance();
			cal.setTime(task.getCreateTime());
			int taskOverTime = 2;
			if (strOverTime != null && !strOverTime.equals("")) {
				taskOverTime = Integer.parseInt(strOverTime);
			}
			cal.add(Calendar.DATE, taskOverTime);
			task.setDueDate(cal.getTime());
			if (appUrl != null) {
				task.setVariableLocal("appUrl", appUrl.getExpressionText());
			}
			if (isSupportBack != null) {
				task.setVariableLocal("isSupportBack", isSupportBack.getExpressionText());
			}
			if (pcUrl != null) {
				task.setVariableLocal("pcUrl", pcUrl.getExpressionText());
			}
			if (isMRemind != null) {
				task.setVariableLocal("isMRemind", isMRemind.getExpressionText());
			}
			if (othVariables != null) {
				String othVariablesText = othVariables.getExpressionText();
				if (StringUtils.hasText(othVariablesText)) {
					Map<String, Object> map = JacksonObjectMapper.getDefaultObjectMapper().readValue(othVariablesText,
							new TypeReference<Map<String, Object>>() {
							});
					if (map != null && map.size() > 0) {
						for (String key : map.keySet()) {
							task.setVariableLocal(key, map.get(key));
						}
					}
				}
			}
			/*taskToOAService.sendTaskInfo("RPRL", task.getId(), "", "", task.getName(),
					(String) task.getVariableLocal("pcUrl"), (String) task.getVariableLocal("appUrl"), "0", "0", "",
					task.getCreateTime(), task.getAssignee(), task.getCreateTime());*/
            //createTaskSendMail(task.getAssignee());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 创建任务的时候发送邮件
	 * @param userLoginName 用户账号
	 */
	public void createTaskSendMail(String userLoginName) throws Exception{
		SystemDictionaryService systemDictionaryService = applicationContext.getBean(SystemDictionaryService.class);
		Map<String, String> mailMap = systemDictionaryService.getDictionaryMap(MailEnum.SYS_DICT_MAIN_INFO.getValue(), Locale.CHINA);
		if(MapUtils.isEmpty(mailMap)) {
			throw new RuntimeException("数据字典中没有配置邮件信息!");
		}
		String onOff = mailMap.get(MailEnum.SYS_DICT_MAIN_SWITCH.getValue());
		if(StringUtils.isEmpty(onOff) || MailEnum.SWITCH_OFF.getValue().equals(onOff)) {
			return ;
		}
		UserService userService = applicationContext.getBean(UserService.class);
		User user = userService.getRepository().findByLoginName(userLoginName);
		String subject = mailMap.get(MailEnum.SYS_DICT_MAIN_SUBJECT.getValue());
		String text = mailMap.get(MailEnum.SYS_DICT_MAIN_TEXT.getValue());
		Environment evn = applicationContext.getBean(Environment.class);
		String from = evn.getProperty(MailEnum.APPLICATION_MAIL_USERNAME.getValue());
		MailSenderService mailSenderService = applicationContext.getBean(MailSenderService.class);
		//String[] to = {"linxuebing2008@163.com"};
		String[] to = {user.getEmail()};
		mailSenderService.sendSimpleMessage(from, to, null, subject, text);
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