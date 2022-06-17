package org.wsp.framework.security.impl.jpa.filter;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.TextEscapeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wsp.framework.core.util.IpUtil;
import org.wsp.framework.mvc.service.SystemParameterService;
import org.wsp.framework.security.impl.jpa.entity.AdminAuthADInfo;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPSearchResults;

/**
 * 
 * @author koupengyang
 *
 */
public class LDAPUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private static final Logger log =LoggerFactory.getLogger(LDAPUsernamePasswordAuthenticationFilter.class);
	
	/**
	 * 过滤方法
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException{
//		WebApplicationContext context =WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
//		SystemParameterService systemParameterService =context.getBean(SystemParameterService.class);
//		
//		
//		String loginName = obtainUsername(request);
//		String password = obtainPassword(request);
//		
//		AdminAuthADInfo adInfo = null;
//		try {
//			String LDAP_HOST = systemParameterService.getParameter("LDAP_HOST");
//			int LDAP_PORT = Integer.valueOf(systemParameterService.getParameter("LDAP_PORT"));
//			adInfo = new AdminAuthADInfo();
//			adInfo.setHost(LDAP_HOST);
//			adInfo.setPort(LDAP_PORT);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		//获取DNName
//		String userDNName = this.getDNByName(loginName, adInfo);
//        if(userDNName == null || "".equals(userDNName)) {
//        	log.error("统一认证获取用户名称失败！");
//        	//throw new AuthenticationServiceException("统一认证获取用户名称失败！");
//        }
//        
//        LDAPConnection ldapConn = new LDAPConnection();
//        try{
//        	ldapConn.connect(adInfo.getHost(), adInfo.getPort());
//        	ldapConn.bind(userDNName, password);
//            if(!ldapConn.isBound()){
//            	log.error("...是否绑定密码.....");
//            	//throw new AuthenticationServiceException("密码为空..");
//			}
//        }catch(Exception e){
//        	e.printStackTrace();
//        	log.error(".....绑定密码异常.......");
//        	//throw new AuthenticationServiceException(".....绑定密码异常......");
//        }finally{
//	        try {
//	        	ldapConn.disconnect();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//               //throw new AuthenticationServiceException("关闭LDAP连接异常");
//            }
//        }
//        
//        //进行统一认证登录
//        try {
//			this.loginLDAPVerify(userDNName, password, adInfo);
//		} catch (Exception e) {
//			log.error("LDAP认证失败！");
//			e.printStackTrace();
//		}
//
//		if(StringUtils.hasText(loginName)){
//			String ipWithTypeHeader =IpUtil.getRemoteAddressWithIpTypeHeader(request);
//			if(ipWithTypeHeader==null){
//				throw new AuthenticationServiceException("IP address error");
//			}
//			log.info("remote user's ip : " + ipWithTypeHeader);
//			loginName =ipWithTypeHeader + loginName;
//		}
//		
		return super.attemptAuthentication(request, response);
//		UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(loginName, password);
//		setDetails(request, token);
//		return this.getAuthenticationManager().authenticate(token);
	}
	
	
}
