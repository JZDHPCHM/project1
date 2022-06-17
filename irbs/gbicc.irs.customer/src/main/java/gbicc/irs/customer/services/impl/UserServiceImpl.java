package gbicc.irs.customer.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.model.user.entity.QUser;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.model.user.repository.UserRepository;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.security.util.SecurityUtil;

import com.ibm.db2.jcc.am.re;
import com.querydsl.core.BooleanBuilder;

import gbicc.irs.customer.service.UserService;
@Service("userService2")
public class UserServiceImpl  extends DaoServiceImpl<User, String, UserRepository> implements UserService {

	@Autowired
	private UserService userService;
	
	public Map<String, String> findByUser(String orgId){
		Map<String, String> map=new HashMap<String,String>();
		QUser quser=QUser.user;
		String roleCode=SecurityUtil.getDefaultRoleCode();
		String loginName=SecurityUtil.getLoginName();
		String loginIn=SecurityUtil.getUserId();
		BooleanBuilder builder =new BooleanBuilder();
		
		//如果等于客户经理 则下拉框只等于客户经理
		if("R_ACCOUNT_MANAGER".equals(roleCode)){
			map.put(loginIn, loginName);
			builder.and(quser.defaultOrgId.eq(SecurityUtil.getDefaultRoleId()));
			return map;
		}else if("R_ACCOUNT_MANAGER".equals(roleCode)){
			builder.and(quser.defaultOrgId.eq(SecurityUtil.getDefaultRoleId()));
		}else{
			if(StringUtils.isNoneEmpty(orgId)){
				builder.and(quser.defaultOrgId.eq(orgId));
			}
		}
		Iterable<User> list=repository.findAll(builder.getValue());
		for (User user : list) {
			map.put(user.getLoginName(), user.getUserName());
		}
		return map;
	}
	
	
	
	public String findByOrgCode(){
		List<Org> orgs = userService.getRepository().findByLoginName(SecurityUtil.getLoginName()).getOrgs();
		String orgCode ="''";
		if(orgs.size()>0) {
			orgCode = orgs.get(0).getCode();
		}
		return orgCode;
	}


}
