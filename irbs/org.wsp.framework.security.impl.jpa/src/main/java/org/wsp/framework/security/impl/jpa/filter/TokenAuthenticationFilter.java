package org.wsp.framework.security.impl.jpa.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.wsp.framework.core.util.IpUtil;
import org.wsp.framework.mvc.service.SystemParameterService;
import org.wsp.framework.security.util.SecurityUtil;
import org.wsp.framework.security.util.UrlSsoTokenEncoder;

/**
 * 基于 URL 单点登录过滤器
 * @author wangshaoping
 *
 */
public class TokenAuthenticationFilter extends GenericFilterBean{
	private static final Logger log =LoggerFactory.getLogger(TokenAuthenticationFilter.class);
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		final HttpServletResponse httpResponse = (HttpServletResponse)response;
		final String token = httpRequest.getParameter("token");
		final String loginName =httpRequest.getParameter("loginName");
		final String timestamp =httpRequest.getParameter("timestamp");
		if(SecurityContextHolder.getContext().getAuthentication()==null || !SecurityUtil.getLoginName().equals(loginName)){
			final String ipWithTypeHeader =IpUtil.getRemoteAddressWithIpTypeHeader(httpRequest);
			if(StringUtils.hasText(token) && StringUtils.hasText(loginName) && StringUtils.hasText(timestamp)){
				WebApplicationContext context =WebApplicationContextUtils.getWebApplicationContext(httpRequest.getServletContext());
				SystemParameterService systemParameterService =context.getBean(SystemParameterService.class);
				String expirationTimeStr ="60";
				try {
					expirationTimeStr =systemParameterService.getParameter("urlSsoTokenExpirationTime");
				}catch(Exception e) {
					
				}
				long expirationTime =Long.parseLong(expirationTimeStr)*1000;
				if(context!=null) {
					if(authenticate(token,loginName,timestamp,expirationTime) && ipWithTypeHeader!=null){
						UserDetailsService userDetailsService=context.getBean(UserDetailsService.class);
						if(userDetailsService!=null){
							try {
								//UserDetails userDetails =userDetailsService.loadUserByUsername(ipWithTypeHeader + loginName);
								//final UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
								//SecurityContextHolder.getContext().setAuthentication(authentication);
							} catch (UsernameNotFoundException e) {
								httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
								return;
							}
						}
					}else{
						httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
						return;
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	private boolean authenticate(String token,String loginName,String timestamp,long expirationTime){
		try {
			return validate(loginName, timestamp, token, expirationTime);
		} catch (NoSuchAlgorithmException e) {
			log.error("",e);
			return false;
		}
	}
	
	/**
	 * 验证 URL token
	 * @param loginName 登录名
	 * @param timestamp 系统时间戳
	 * @param token 加密 URL token
	 * @param expirationTime 过期时间
	 * @return 是否验证通过
	 * @throws NoSuchAlgorithmException 违例
	 */
	public boolean validate(String loginName,String timestamp,String token,long expirationTime) throws NoSuchAlgorithmException{
		if(
				loginName==null || loginName.trim().length()==0
				|| timestamp==null || timestamp.trim().length()==0
				|| token==null || token.trim().length()==0
		) {
			return false;
		}
		if(Math.abs(new Date().getTime()-Long.parseLong(timestamp))>expirationTime){
			return false;
		}
		return token.equals(UrlSsoTokenEncoder.encode(loginName,timestamp));
	}
}
