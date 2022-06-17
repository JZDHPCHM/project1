package com.gbicc.aicr.system.in2oa.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.wsp.framework.communication.mail.service.MailSenderService;

import com.gbicc.aicr.system.in2oa.service.TaskToOAService;
@Service("flowableTaskToOAService")
public class TaskToOAServiceImpl implements TaskToOAService{
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
			) {
		
	}
}
