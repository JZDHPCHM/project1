package org.wsp.framework.security.impl.jpa.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

public class UsernamePasswordWithIpAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private static final Logger log =LoggerFactory.getLogger(UsernamePasswordWithIpAuthenticationFilter.class);
	private boolean postOnly = true;
	
	public UsernamePasswordWithIpAuthenticationFilter() {
		super();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String loginName = obtainUsername(request);
		String password = obtainPassword(request);
		
		WebApplicationContext context =WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		SystemParameterService systemParameterService =context.getBean(SystemParameterService.class);
		
		String ipWithTypeHeader =IpUtil.getRemoteAddressWithIpTypeHeader(request);
		
		try {
			//进行LDAP认证
			String LDAP_ENABLED = systemParameterService.getParameter("LDAP_ENABLED");
			if("true".equals(LDAP_ENABLED)) {  //启用LDAP认证
				String LDAP_HOST = systemParameterService.getParameter("LDAP_HOST");
				int LDAP_PORT = Integer.valueOf(systemParameterService.getParameter("LDAP_PORT"));
				String LDAP_PRINCIPAL = systemParameterService.getParameter("LDAP_PRINCIPAL");
				String LDAP_PASSWORD = systemParameterService.getParameter("LDAP_PASSWORD");
				String LDAP_USERBASEDN = systemParameterService.getParameter("LDAP_USERBASEDN");
				AdminAuthADInfo adInfo = new AdminAuthADInfo();
				adInfo.setHost(LDAP_HOST);
				adInfo.setPort(LDAP_PORT);
				adInfo.setPrincipal(LDAP_PRINCIPAL);
				adInfo.setPassword(LDAP_PASSWORD);
				adInfo.setUserBaseDN(LDAP_USERBASEDN);
				
				this.LDAPVerify(loginName, password, adInfo);
				//认证未出现异常则视为认证成功
				UserDetailsService userDetailsService=context.getBean(UserDetailsService.class);
				if(userDetailsService!=null){
					//UserDetails userDetails =userDetailsService.loadUserByUsername(ipWithTypeHeader + loginName);
					//UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					//SecurityContextHolder.getContext().setAuthentication(authentication);
					//return authentication;
				}
			}else {
				log.info("LDAP认证未开启，进行本地认证！");
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("LDAP认证失败，进行本地认证！");
		}
        
		if(StringUtils.hasText(loginName)){
			if(ipWithTypeHeader==null){
				throw new AuthenticationServiceException("IP address error");
			}
			log.info("remote user's ip : " + ipWithTypeHeader);
			loginName =ipWithTypeHeader + loginName;
		}
		UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(loginName, password);
		setDetails(request, token);
		return this.getAuthenticationManager().authenticate(token);
	}
	
	
	
	private void LDAPVerify(String userId, String password, AdminAuthADInfo adInfo) throws Exception {
        LDAPConnection con = new LDAPConnection();
        //获取userDNName
        String userDNName = this.getDNByName(userId, adInfo);
        
        if(userDNName == null || "".equals(userDNName)) {
        	log.error("统一认证获取用户名称失败！");
        	throw new AuthenticationServiceException("统一认证获取用户名称失败！");
        }
        
        LDAPConnection ldapConn = new LDAPConnection();
        try{
        	ldapConn.connect(adInfo.getHost(), adInfo.getPort());
        	ldapConn.bind(userDNName, password);
            if(!ldapConn.isBound()){
            	log.error("LDAP密码绑定失败.....");
            	throw new AuthenticationServiceException("LDAP密码绑定失败.....");
			}
        }catch(Exception e){
        	e.printStackTrace();
        	log.error(".....绑定密码异常.......");
        	throw new AuthenticationServiceException(".....绑定密码异常......");
        }finally{
	        try {
	        	ldapConn.disconnect();
            }
            catch (Exception e) {
               e.printStackTrace();
               throw new AuthenticationServiceException("关闭LDAP连接异常");
            }
        }
        
        try {
            LDAPAttribute ldapAttribute = new LDAPAttribute("userPassword", password.getBytes("UTF-16LE"));
            con.connect(adInfo.getHost(), adInfo.getPort());
            con.bind(LDAPConnection.LDAP_V3, userDNName, password);
        }catch(Exception e){
        	e.printStackTrace();
        	throw new AuthenticationServiceException("认证LDAP异常");
        }finally {
            try {
                con.disconnect();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                throw new AuthenticationServiceException("关闭LDAP连接异常");
            }
        }
    }
	
	/**
           * 根据员工编号获取DNName
     *
     * @param uid    员工编号
     * @param adInfo 统一用户登录AD认证参数Bean
     * @return DNName
     */
    private static String getDNByName(String uid, AdminAuthADInfo adInfo) {
        String dn = "";
        LDAPConnection ldapConn = new LDAPConnection();
        try {
            ldapConn.connect(adInfo.getHost(), adInfo.getPort());
            ldapConn.bind(LDAPConnection.LDAP_V3, adInfo.getPrincipal(), adInfo.getPassword().getBytes("UTF-8"));
            String[] attrs = new String[]{"*"};
            LDAPSearchResults results = ldapConn.search(adInfo.getUserBaseDN(), 2, "(&(objectclass="
                    + adInfo.getUserObjectClass() + ")(" + adInfo.getAccountNameAttr() + "=" + uid + "))", attrs,
                    false);
            if (results != null && results.hasMore()) {
                LDAPEntry entry = results.next();
                dn = entry.getDN();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                ldapConn.disconnect();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("dn="+dn);
        return dn;
    }
}
