package gbicc.irs.risk.exposure.service.impl;

import java.util.ArrayList;
import java.util.List;

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

import gbicc.irs.risk.exposure.support.RiskTaskException;
import liquibase.util.StringUtils;
@Service("riskApproverQueryService")
public class RiskApproverQueryServiceImpl implements AssigneeQueryService{


	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SystemParameterServiceImpl systemParameterServiceImpl;
	

	@Override
	public List<Assignee> query(Task preTask, Task task, UserTask taskDefinition) throws RiskTaskException {
		// 系统配置的所有当前角色编号
		List<String> groups = taskDefinition.getCandidateGroups();
		
		if(CollectionUtils.isNotEmpty(groups)) {
			List<User> roleUsers =null;
			List<Role> roles = roleService.getRepository().findByCodeIn(groups);
			if(CollectionUtils.isNotEmpty(roles)){
				List<String> roleIds = new ArrayList<String>();
				for(Role role : roles){
					roleIds.add(role.getId());
				}
				try {
					roleUsers =userService.listUsersByRoles(roleIds);
				} catch (Exception e) {
					throw new RiskTaskException("角色（"+roleIds+"）用户不匹配，查询不到用户");
				}
			}
			// 如果系统没有配置则报错
			if(CollectionUtils.isEmpty(roleUsers)) throw new RiskTaskException("当前角色系统未配置用户");
			List<Assignee> candidateUserAssignees = new ArrayList<Assignee>();
			List<String> orgs = null;
			// 支行初审、支行行长审批/总行大客户初审、总行大客户审批/金融小企业初审、金融小企业审批
			if("A2".equals(task.getTaskDefinitionKey()) || "A3".equals(task.getTaskDefinitionKey())) {
				for (User u : roleUsers) {
					if(StringUtils.isEmpty(u.getId())) continue;
						Assignee assignee =new Assignee();
						assignee.setId(u.getId());
						assignee.setLoginName(u.getLoginName());
						assignee.setUserName(u.getUserName());
						candidateUserAssignees.add(assignee);
				}
			}
			if(CollectionUtils.isNotEmpty(candidateUserAssignees)) return candidateUserAssignees;
		}
		throw new RiskTaskException("没有找到下一流程处理人！");
	}
	
	private List<Assignee> getCandidateUserAssignees(List<User> roleUsers,String orgSql,String currentOrgid) throws RiskTaskException  {
		List<String> previousOrIds = null;
		List<Assignee> candidateUserAssignees = new ArrayList<Assignee>();
		for (User u : roleUsers) {
			if(StringUtils.isEmpty(u.getId())) continue;
			previousOrIds = jdbcTemplate.queryForList(orgSql, String.class, u.getId());
			if(CollectionUtils.isEmpty(previousOrIds)) continue;
			if(previousOrIds.size() > 1) throw new RiskTaskException("用户："+u.getId()+",关联有多个机构");
			if(previousOrIds.get(0).equals(currentOrgid)) {
				Assignee assignee = new Assignee();
				assignee.setId(u.getId());
				assignee.setLoginName(u.getLoginName());
				assignee.setUserName(u.getUserName());
				candidateUserAssignees.add(assignee);
			}
		}
		return candidateUserAssignees;
	}
	
	
	

	@Override
	public List<Assignee> query(UserTask taskDefinition) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
