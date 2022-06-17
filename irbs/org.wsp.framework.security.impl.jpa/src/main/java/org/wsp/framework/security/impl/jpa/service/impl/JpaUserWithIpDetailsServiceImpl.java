package org.wsp.framework.security.impl.jpa.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.wsp.framework.core.Environment;
import org.wsp.framework.core.util.IpUtil;
import org.wsp.framework.datasource.routing.DataSourceContext;
import org.wsp.framework.jpa.entity.CorporationConst;
import org.wsp.framework.jpa.model.corporation.entity.Corporation;
import org.wsp.framework.jpa.model.role.entity.Role;
import org.wsp.framework.jpa.model.user.entity.User;
import org.wsp.framework.jpa.model.user.repository.UserRepository;
import org.wsp.framework.mvc.service.CorporationService;
import org.wsp.framework.security.support.SecurityRole;
import org.wsp.framework.security.support.SecurityUser;

public class JpaUserWithIpDetailsServiceImpl implements UserDetailsService{
	@Autowired private UserRepository userRepository;
	@Autowired private CorporationService corporationService;

	@Override
	public UserDetails loadUserByUsername(String loginNameAndIp) throws UsernameNotFoundException {
		if(StringUtils.hasText(loginNameAndIp)){
			String ip ="";
			String loginName ="";
			if(loginNameAndIp.startsWith(IpUtil.IP_V4)){
				ip = loginNameAndIp.substring(IpUtil.IP_TYPE_HEADER_LENGTH,IpUtil.IP_TYPE_HEADER_LENGTH + IpUtil.IP_V4_LENGTH);
				loginName = IpUtil.parseLoginName(loginNameAndIp);
			}else if(loginNameAndIp.startsWith(IpUtil.IP_V6)){
				ip = loginNameAndIp.substring(IpUtil.IP_TYPE_HEADER_LENGTH,IpUtil.IP_TYPE_HEADER_LENGTH + IpUtil.IP_V6_LENGTH);
				loginName =  IpUtil.parseLoginName(loginNameAndIp);
			}
			if(StringUtils.isEmpty(ip)) {
				ip ="统一认证平台";
				loginName =loginNameAndIp;
			}
			
			String corporationCode =CorporationConst.PRIMARY;
			String corporationName =CorporationConst.PRIMARY;
			
			//在多法人模式下,尝试获取法人信息，并采用该法人的数据源进行用户认证
			if(Environment.isEnableMultiCorporation()) {
				//从主数据源中获取法人信息
				DataSourceContext.setDatasourceName(CorporationConst.PRIMARY);
				Corporation corporation =corporationService.getCorporation(loginName,ip);
				DataSourceContext.removeDatasourceName();
				
				if(corporation!=null && StringUtils.hasText(corporation.getCode())) {
					corporationCode =corporation.getCode();
					corporationName =corporation.getName();
				}
			}
			
			//切换到法人数据源进行用户验证(即不同法人的用户数据存储在不同的数据源中)
			DataSourceContext.setDatasourceName(corporationCode);
			User u =userRepository.findByLoginName(loginName);
			DataSourceContext.removeDatasourceName();
			
			if(u!=null){
				List<Role> roles =u.getRoles();
				SecurityUser user = null;
				if(("统一认证平台").equals(ip)) {
					user =new SecurityUser(u.getId(), "Ghywwi9njtyrzptjiami21133", getAuthorities(roles));
				}else {
					user =new SecurityUser(u.getId(),u.getPassword(),getAuthorities(roles));
				}
				
				user.setLoginName(u.getLoginName());
				user.setAliasName(u.getUserName());
				user.setCorporationCode(corporationCode);
				user.setCorporationName(corporationName);
				user.setIp(ip);
				
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
				return null;
			}
		}
		throw new UsernameNotFoundException("Login Name Empty!");
	}
	
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
