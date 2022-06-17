package net.gbicc.app.cas.client.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.wsp.framework.core.Environment;
import org.wsp.framework.datasource.routing.DataSourceContext;
import org.wsp.framework.jpa.entity.CorporationConst;
import org.wsp.framework.jpa.model.role.entity.Role;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.model.user.repository.UserRepository;
import org.wsp.framework.mvc.service.CorporationService;
import org.wsp.framework.security.support.SecurityRole;
import org.wsp.framework.security.support.SecurityUser;

/**
 * 用于加载用户信息 实现UserDetailsService接口，或者实现AuthenticationUserDetailsService接口
 * 
 * @author fun
 * 
 */
public class CasUserDetailsService

        // 实现AuthenticationUserDetailsService，实现loadUserDetails方法
        implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken>
{

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException
    {
        System.out.println("当前的用户名是：" + token.getName());    
			String loginName =token.getName();
			String corporationCode =CorporationConst.PRIMARY;
			String corporationName =CorporationConst.PRIMARY;
			
			User u =userRepository.findByLoginName(loginName);
			DataSourceContext.removeDatasourceName();
			
			if(u!=null){
				List<Role> roles =u.getRoles();
				
				SecurityUser user =new SecurityUser(u.getId(),u.getPassword(),getAuthorities(roles));
				user.setLoginName(u.getLoginName());
				user.setAliasName(u.getUserName());
				user.setCorporationCode(corporationCode);
				user.setCorporationName(corporationName);
				user.setIp("");
				
				if(Environment.isEnableChangeRole() && u.getDefaultRoleId()!=null && !u.getDefaultRoleId().isEmpty()) {
					Role defaultRole =null;
					for(Role role : roles) {
						if(role.getId().equals(u.getDefaultRoleId())) {
							defaultRole =role;
							break;
						}
					}
					if(defaultRole!=null) {
						user.setDefaultRoleId(defaultRole.getId());
						user.setDefaultRoleCode(defaultRole.getCode());
						user.setDefaultRoleName(defaultRole.getName());
					}
				}
				return user;
			}else {
				throw new UsernameNotFoundException(loginName + " Not Found");
			}
    }

    
    @Autowired private UserRepository userRepository;
	@Autowired private CorporationService corporationService;

	
	private Collection<GrantedAuthority> getAuthorities(List<Role> roles){
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		if(roles!=null && !roles.isEmpty()){
			for(Role role : roles){
				authList.add(new SecurityRole(role.getId(),role.getCode(),role.getName()));
			}
		}
		return authList;
	}    
    
}