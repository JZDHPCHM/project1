package gbicc.irs.main.rating.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flowable.bpmn.model.UserTask;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.flowable.service.AssigneeQueryService;
import org.wsp.framework.flowable.support.Assignee;
import org.wsp.framework.jpa.model.role.entity.Role;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.RoleService;
import org.wsp.framework.mvc.service.UserService;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import liquibase.util.StringUtils;

@Service("assigneeQueryService")
public class AssigneeQueryServiceImpl implements AssigneeQueryService {

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
