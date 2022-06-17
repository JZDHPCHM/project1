package gbicc.irs.defaultcustomer.service.impl;

import java.util.List;

import org.flowable.bpmn.model.UserTask;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.wsp.framework.flowable.service.AssigneeQueryService;
import org.wsp.framework.flowable.support.Assignee;
@Service("approverQueryService")
public class ApproverQueryServiceImpl implements AssigneeQueryService{

	@Override
	public List<Assignee> query(Task preTask, Task task, UserTask taskDefinition) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Assignee> query(UserTask taskDefinition) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
