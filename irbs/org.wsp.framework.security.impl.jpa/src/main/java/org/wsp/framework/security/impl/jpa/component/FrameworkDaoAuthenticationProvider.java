package org.wsp.framework.security.impl.jpa.component;

import java.util.Date;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.wsp.framework.core.util.IpUtil;
import org.wsp.framework.jpa.model.access.entity.AccessLog;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.model.access.support.AccessStatus;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.security.support.SecurityUser;

public class FrameworkDaoAuthenticationProvider extends DaoAuthenticationProvider{
	private AccessLogService accessLogService;
	
	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,UserDetails user) {
		if(user instanceof SecurityUser) {
			SecurityUser _user =(SecurityUser)user;
			AccessLog log =new AccessLog();
			log.setDate(new Date());
			log.setStatus(AccessStatus.SUCCESS);
			log.setType(AccessType.LOGIN);
			log.setUserId(_user.getUsername());
			log.setUserLoginName(_user.getLoginName());
			log.setUserName(_user.getAliasName());
			log.setIp(_user.getIp());
			log.setUrl("/login");
			accessLogService.record(log);
		}
		
		return super.createSuccessAuthentication(principal, authentication, user);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			return super.authenticate(authentication);
		}catch(AuthenticationException e) {
			String loginNameAndIp =authentication.getPrincipal().toString();
			String ip =IpUtil.parseIp(loginNameAndIp);
			String loginName =IpUtil.parseLoginName(loginNameAndIp);
			
			AccessLog log =new AccessLog();
			log.setDate(new Date());
			log.setStatus(AccessStatus.FAILED);
			log.setType(AccessType.LOGIN);
			log.setUserLoginName(loginName);
			log.setIp(ip);
			log.setUrl("/login");
			accessLogService.record(log);
			throw e;
		}
	}



	public AccessLogService getAccessLogService() {
		return accessLogService;
	}

	public void setAccessLogService(AccessLogService accessLogService) {
		this.accessLogService = accessLogService;
	}
}
