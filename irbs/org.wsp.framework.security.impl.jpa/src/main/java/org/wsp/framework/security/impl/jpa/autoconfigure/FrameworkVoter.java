package org.wsp.framework.security.impl.jpa.autoconfigure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.wsp.framework.security.support.SecurityUser;

public class FrameworkVoter implements AccessDecisionVoter<Object>{
	private String loginUrl;
	private String loginErrorUrl;
	private List<AntPathRequestMatcher> matchers =new ArrayList<AntPathRequestMatcher>();
	
	public FrameworkVoter() {}
	public FrameworkVoter(Environment environment,List<String> ignored) {
		loginUrl =environment.getProperty("security.http.formLogin.loginPage", "/login");
		loginErrorUrl =environment.getProperty("security.http.formLogin.failureUrl","/login-error");
		if(ignored!=null && ignored.size()>0) {
			for(String url : ignored) {
				if(url!=null && !url.isEmpty()) {
					matchers.add(new AntPathRequestMatcher(url));
				}
			}
		}
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		FilterInvocation filterInvocation =(FilterInvocation)object;
		HttpServletRequest request =filterInvocation.getRequest();
		String url =filterInvocation.getRequestUrl();
		
		//任何人（无论是否登录）可以访问登录页面
		if(url.equalsIgnoreCase(loginUrl) || url.equalsIgnoreCase(loginErrorUrl)) {
			return ACCESS_GRANTED;
		}
		
		//对于不需要安全认证的 URL 直接允许
		for(AntPathRequestMatcher matcher : matchers) {
			if(matcher.matches(request)) {
				return ACCESS_GRANTED;
			}
		}
		
		//如果用户没有登录，则拒绝访问
		if(authentication.getName()==null || authentication.getName().isEmpty() || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
			return ACCESS_DENIED;
		}
		
		//获取用户角色
		Object principal =authentication.getPrincipal();
		if(principal instanceof SecurityUser) {
			SecurityUser user =(SecurityUser)principal;
			user.getDefaultRoleId();
		}
		return ACCESS_GRANTED;
	}
}
