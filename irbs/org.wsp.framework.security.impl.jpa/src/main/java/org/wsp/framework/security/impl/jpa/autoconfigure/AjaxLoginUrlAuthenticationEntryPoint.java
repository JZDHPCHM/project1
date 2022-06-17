package org.wsp.framework.security.impl.jpa.autoconfigure;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.wsp.framework.mvc.util.MediaTypeUtil;

/**
 * 默认基于表单的认证重定向入口点类
 * 该类的功能： 当 Spring Security 检测到某个请求需要验证但还没有通过验证时，如果配置了采用表单认证默认，则会将用户的请求重定向到表单认证页面。
 * 问题说明：对于普通的页面访问而言，以上功能没有问题，但对于 ajax 请求来说，如果重定向到表单认证页面，那么 ajax 客户端将不容易处理，所以需要区分“页面请求”和“ajax请求”
 * 解决办法：
 * 		通过获取 "Accept" 请求头来判断是否是 ajax 请求，
 * 		如果 Accept=application/json 或者 application/json;charset=UTF-8，则表示是ajax请求，否则表示是普通的页面请求
 * @author wangshaoping
 *
 */
public class AjaxLoginUrlAuthenticationEntryPoint extends org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint{

	public AjaxLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) throws IOException, ServletException {
		List<MediaType> mediaTypes =MediaTypeUtil.resolveMediaTypes(request);
		if(mediaTypes!=null && mediaTypes.size()>0) {
			for(MediaType mediaType : mediaTypes) {
				//json请求
				if(MediaType.APPLICATION_JSON.equals(mediaType) || MediaType.APPLICATION_JSON_UTF8.equals(mediaType)){
					response.sendError(302, "302");
					return;
				}
			}
		}
		String redirectUrl =buildRedirectUrlToLoginPage(request, response, authException);
		response.sendRedirect(redirectUrl);
	}
}
