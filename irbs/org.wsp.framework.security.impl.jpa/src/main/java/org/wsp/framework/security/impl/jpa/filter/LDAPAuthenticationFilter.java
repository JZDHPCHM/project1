package org.wsp.framework.security.impl.jpa.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;
import org.wsp.framework.security.impl.jpa.entity.AdminAuthADInfo;
import org.wsp.framework.security.util.SecurityUtil;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPSearchResults;

/**
 * 汉口银行LDAP统一认证
 * @author koupengyang
 *
 */
public class LDAPAuthenticationFilter extends GenericFilterBean{
	private static final Logger log =LoggerFactory.getLogger(TokenAuthenticationFilter.class);
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		final HttpServletResponse httpResponse = (HttpServletResponse)response;
		final String token = httpRequest.getParameter("token");
		final String loginName =httpRequest.getParameter("loginName");
		final String timestamp =httpRequest.getParameter("timestamp");
		System.out.println(SecurityUtil.getLoginName());
		if(loginName != null) {
			System.out.println("12123132");
		}
		
		AdminAuthADInfo adInfo = new AdminAuthADInfo();
		
		String dn = "";
        LDAPConnection ldapConn = new LDAPConnection();
        try {
            ldapConn.connect(adInfo.getHost(), adInfo.getPort());
            ldapConn.bind(LDAPConnection.LDAP_V3, adInfo.getPrincipal(), adInfo.getPassword().getBytes("UTF-8"));
            String[] attrs = new String[]{"*"};
            LDAPSearchResults results = ldapConn.search(adInfo.getUserBaseDN(), 2, "(&(objectclass="
                    + adInfo.getUserObjectClass() + ")(" + adInfo.getAccountNameAttr() + "=" + loginName + "))", attrs,
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
	        
//	        
//		if(SecurityContextHolder.getContext().getAuthentication()==null || !SecurityUtil.getLoginName().equals(loginName)){
//			final String ipWithTypeHeader =IpUtil.getRemoteAddressWithIpTypeHeader(httpRequest);
//			if(StringUtils.hasText(token) && StringUtils.hasText(loginName) && StringUtils.hasText(timestamp)){
//				WebApplicationContext context =WebApplicationContextUtils.getWebApplicationContext(httpRequest.getServletContext());
//				SystemParameterService systemParameterService =context.getBean(SystemParameterService.class);
//				String expirationTimeStr ="60";
//				try {
//					expirationTimeStr =systemParameterService.getParameter("urlSsoTokenExpirationTime");
//				}catch(Exception e) {
//					
//				}
//				long expirationTime =Long.parseLong(expirationTimeStr)*1000;
//				if(context!=null) {
//					if(authenticate(token,loginName,timestamp,expirationTime) && ipWithTypeHeader!=null){
//						UserDetailsService userDetailsService=context.getBean(UserDetailsService.class);
//						if(userDetailsService!=null){
//							try {
//								UserDetails userDetails =userDetailsService.loadUserByUsername(ipWithTypeHeader + loginName);
//								final UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//								SecurityContextHolder.getContext().setAuthentication(authentication);
//							} catch (UsernameNotFoundException e) {
//								httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//								return;
//							}
//						}
//					}else{
//						httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
//						return;
//					}
//				}
//			}
//		}
		chain.doFilter(request, response);
	}
}
