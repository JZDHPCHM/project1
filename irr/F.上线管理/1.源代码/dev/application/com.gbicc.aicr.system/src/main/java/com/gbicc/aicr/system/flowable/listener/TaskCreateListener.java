package com.gbicc.aicr.system.flowable.listener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.TaskService;
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
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.role.entity.Role;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.RoleService;
import org.wsp.framework.mvc.service.SystemDictionaryService;
import org.wsp.framework.mvc.service.UserService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gbicc.aicr.system.flowable.support.enums.HALifeEnum;
import com.gbicc.aicr.system.flowable.support.enums.MailEnum;
import com.gbicc.aicr.system.in2oa.service.TaskToOAService;
import com.gbicc.aicr.system.util.DateUtils;

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
			/*给新生成的任务添加IRR流程必要的其他参数*/
			TaskService taskService = applicationContext.getBean(TaskService.class);
			Map<String, Object> variables = taskService.getVariables(task.getId());
			task.setVariableLocal(HALifeEnum.COLL_LOGIN_NAME_VAR.getValue(), variables.get(HALifeEnum.COLL_LOGIN_NAME_VAR.getValue()));
            sendMailAndOARemind(task, task.getAssignee(), variables);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	/**
     * 创建任务的时候发送邮件和OA
     * 
     * @param userLoginName
     *            用户账号
     */
    public void sendMailAndOARemind(DelegateTask task, String userLoginName, Map<String, Object> variables)
            throws Exception {
        SystemDictionaryService systemDictionaryService = applicationContext.getBean(SystemDictionaryService.class);
        Map<String, String> mailMap = systemDictionaryService.getDictionaryMap(MailEnum.SYS_DICT_MAIN_INFO.getValue(),
                Locale.CHINA);
        String onOff = mailMap.get(MailEnum.SYS_DICT_MAIN_SWITCH.getValue());
        UserService userService = applicationContext.getBean(UserService.class);
        RoleService roleService = applicationContext.getBean(RoleService.class);
        //对接OA
        TaskToOAService taskToOAService = applicationContext.getBean(TaskToOAService.class);
        String planIssue = variables.get("planIssue").toString();
        //当任务处理人为空的时候，则为上报节点
        if (StringUtils.isEmpty(userLoginName)) {
            Role role = roleService.getRepository().findByCode(HALifeEnum.ROLE_REPORT_CODE.getValue());
            if (role != null) {
                List<String> roleIds = new ArrayList<String>();
                roleIds.add(role.getId());
                List<User> users = userService.listUsersByRoles(roleIds);
                if (CollectionUtils.isNotEmpty(users)) {
                    String[] toReport = { users.get(0).getEmail() };
                    Map<String, String> reportMailMap = systemDictionaryService
                            .getDictionaryMap(HALifeEnum.REPORT_MAIL_CODE.getValue(), Locale.CHINA);
                    if (MapUtils.isEmpty(reportMailMap)
                            || StringUtils.isEmpty(reportMailMap.get(HALifeEnum.REPORT_MAIL_SUBJECT_CODE.getValue()))
                            || StringUtils.isEmpty(reportMailMap.get(HALifeEnum.REPORT_MAIL_TEXT_CODE.getValue()))) {
                        sendMail(onOff, toReport, HALifeEnum.REPORT_MAIL_SUBJECT_INFO.getValue(),
                                HALifeEnum.REPORT_MAIL_TEXT_INFO.getValue());
                    } else {
                        sendMail(onOff, toReport, reportMailMap.get(HALifeEnum.REPORT_MAIL_SUBJECT_CODE.getValue()),
                                reportMailMap.get(HALifeEnum.REPORT_MAIL_TEXT_CODE.getValue()));
                    }
                    taskToOAService.sendOARemind(users.get(0).getLoginName(), planIssue,
                            DateUtils.formatDate(task.getCreateTime()), DateUtils.formatDate(task.getDueDate()));
                    log.info("发送邮件成功！收件邮箱：" + toReport[0].toString());
                }
            }
            return;
        }
        //所有子流程任务发送邮件
		User user = userService.getRepository().findByLoginName(userLoginName);
		String subject = mailMap.get(MailEnum.SYS_DICT_MAIN_SUBJECT.getValue());
		String text = mailMap.get(MailEnum.SYS_DICT_MAIN_TEXT.getValue());
		String[] to = {user.getEmail()};
        //采集人信息
        Object collUserLoginName = variables.get(HALifeEnum.COLL_LOGIN_NAME_VAR.getValue());
        if (collUserLoginName != null && !"".equals(collUserLoginName)) {
            User collUser = userService.getRepository().findByLoginName(collUserLoginName.toString());
            if (collUser != null) {
                List<Org> collUserOrg = collUser.getOrgs();
                if (CollectionUtils.isNotEmpty(collUserOrg) && collUserOrg.get(0) != null) {
                    text = text + MailEnum.TEXT_NEWLINE.getValue() + HALifeEnum.MAIL_COLL_USER_TEXT.getValue()
                            + collUser.getUserName()
                            + MailEnum.TEXT_NEWLINE.getValue() + HALifeEnum.MAIL_COLL_ORG_TEXT.getValue()
                            + collUserOrg.get(0).getName();
                }
            }
        }
        sendMail(onOff, to, subject, text);
        log.info("发送邮件成功！收件邮箱：" + String.valueOf(to[0]));
        //如果为每个子流程的采集节点，则不在此发送，在任务成功后，一并发送
        if (taskToOAService.isProcessCollUser(userLoginName)) {
            return;
        }
        taskToOAService.sendOARemind(userLoginName, planIssue, DateUtils.formatDate(task.getCreateTime()),
                DateUtils.formatDate(task.getDueDate()));
	}

    /**
     * 发送简单邮件
     * 
     * @param onOff
     *            开关
     * @param to
     *            收件人
     * @param subject
     *            标题
     * @param text
     *            文本
     */
    private void sendMail(String onOff, String[] to, String subject, String text) throws Exception {
        if (StringUtils.isEmpty(onOff) || MailEnum.SWITCH_OFF.getValue().equals(onOff)) {
            return;
        }
        Environment evn = applicationContext.getBean(Environment.class);
        String from = evn.getProperty(MailEnum.APPLICATION_MAIL_USERNAME.getValue());
        MailSenderService mailSenderService = applicationContext.getBean(MailSenderService.class);
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