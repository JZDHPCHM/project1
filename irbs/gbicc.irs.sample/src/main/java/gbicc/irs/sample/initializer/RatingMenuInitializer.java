package gbicc.irs.sample.initializer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.core.service.support.ApplicationInitializer;
import org.wsp.framework.jpa.model.role.entity.QRole;
import org.wsp.framework.jpa.model.role.entity.Role;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.mvc.service.RoleService;
import org.wsp.framework.mvc.service.UserService;

import com.querydsl.core.BooleanBuilder;

@Service
public class RatingMenuInitializer implements ApplicationInitializer{
	@Autowired private RoleService roleService;
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UserService userService;
	@Override
	public String getId() {
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return "Menu Initializer";
	}

	@Override
	public String getDescription() {
		return "Initialize menu item, and all menu item granted to the admin Role";
	}

	@Override
	public int getOrder() {
		return 2000;
	}

	@Override
	public boolean isInitialized() {
		return this.count()>0;
	}

	@Override
	@Transactional
	public void execute() throws Exception {
//		//给非管理员角色添加菜单
//		List<Role> roles = this.getOtherAdmin();
//		if(roles!=null && roles.size()>0){
//			for(Role role : roles){
//				roleService.addAllMenus(role.getId());
//			}
//		}
//		//判断是否给 系统管理管理员赋过角色
//		if(this.adminHaveRole() == 0) {
//			User adminUser =userService.getAdminUser();
//			String insertSql = "INSERT into FR_AA_USER_ROLE(FD_USER_ID,FD_ROLE_ID) values(?,?)";
//			List<Object[]> params = new ArrayList<Object[]>();
//			Object[] p1 = {adminUser.getId(),"00000000-0000-0000-3000-000000000001"};
//			Object[] p2 = {adminUser.getId(),"00000000-0000-0000-3000-000000000002"};
//			Object[] p3 = {adminUser.getId(),"00000000-0000-0000-3000-000000000003"};
//			Object[] p4 = {adminUser.getId(),"00000000-0000-0000-3000-000000000004"};
//			params.add(p1);
//			params.add(p2);
//			params.add(p3);
//			params.add(p4);
//			jdbcTemplate.batchUpdate(insertSql, params);
//		}
	}
	
	private int count() {
		String countSql = "SELECT count(1) from fr_aa_role_menu r where r.FD_ROLE_ID = '00000000-0000-0000-3000-000000000001'";
		return jdbcTemplate.queryForObject(countSql, Integer.class);
	}
	
	private int adminHaveRole() {
		String countSql = "SELECT count(1) from FR_AA_USER_ROLE r where r.FD_ROLE_ID in ('00000000-0000-0000-3000-000000000001',"
				+ "'00000000-0000-0000-3000-000000000002','00000000-0000-0000-3000-000000000003','00000000-0000-0000-3000-000000000004') "
				+ "and r.FD_USER_ID='00000000-0000-0000-0000-000000000000'";
		return jdbcTemplate.queryForObject(countSql, Integer.class);
	}
	
	//查询除了admin之外的角色
	private List<Role> getOtherAdmin() {
		BooleanBuilder builder =new BooleanBuilder();
		builder.and(QRole.role.code.notEqualsIgnoreCase(RoleService.ADMIN_ROLE_CODE));
		Iterable<Role> iterable = roleService.getRepository().findAll(builder.getValue());
		if(iterable!=null) {
			return IteratorUtils.toList(iterable.iterator());
		}
		return null;
	}
	
}


