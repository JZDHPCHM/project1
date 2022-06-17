package org.wsp.framework.security.service.impl;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wsp.framework.security.impl.config.Myconfig;
import org.wsp.framework.security.service.OuthLoginService;

import com.alibaba.fastjson.JSONObject;

@Service
public class OuthLoginServiceImpl implements OuthLoginService {
	
	@Value("${spring.typt.token}")
	private String token;
	
	@Value("${spring.typt.token.v1}")
	private String v1;
	
	@Value("${spring.typt.authorize}")
	private String authorize;
	
	//风险系统回调地址
	@Value("${spring.fxxt.fxdz}") 
	private String fxdz;
	
	@Override
	public Integer outhLogin(HttpServletRequest request, HttpServletResponse response) {
		Myconfig myc = new Myconfig();
		String clientId = "3LTcQgrA1hNPtasr";// 统一服务平台提供
		System.out.println("clientId:" + clientId);
		String clientSecret = "MXY2aoOo0kH31Fy3sLeJIFE1wdDWzICd";// 统一服务平台提供
		System.out.println("clientSecret:" + clientSecret);
		//String redirect_uri = "http://192.168.1.15:8089/demo/sso/authorize";// 要改成业务系统自己ip地址，统一服务平台能访问到,这个是demo的地址
		String redirect_uri = fxdz;
		System.out.println("redirect_uri:" + redirect_uri);
		//String oauth_token_uri = "https://192.168.36.39/auth_center/oauth/token";// 统一服务平台部署的ip和端口
		String oauth_token_uri = token;
		System.out.println("oauth_token_uri:" + oauth_token_uri);
		//String oauth_user_token_uri = "https://192.168.36.39:443/manage/user/token/v1";// 统一服务平台部署的ip和端口
		String oauth_user_token_uri = v1;
		System.out.println("oauth_user_token_uri:" + oauth_user_token_uri);
		// 取得提交参数
		String code = request.getParameter("code");
		System.out.println("code:" + code);
		String access_token = "";
		RestTemplate restTemplate = myc.restTemplate();
		try {
			// 发起用code取acccess_token
			HttpHeaders headers = new HttpHeaders();
			String authorization = "Basic "
					+ Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
			System.out.println("authorization:" + authorization);
			headers.add("Authorization", authorization);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
			parameters.add("grant_type", "authorization_code");
			parameters.add("code", code);
			parameters.add("redirect_uri", redirect_uri);

			// String url = "https://ip:port/auth_center/oauth/token";// 统一服务平台提供

			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					parameters, headers);
			ResponseEntity<String> httpResponse = restTemplate.exchange(oauth_token_uri, HttpMethod.POST, requestEntity,
					String.class);

			HttpStatus httpStatus = httpResponse.getStatusCode();
			System.out.println("httpStatus:" + httpStatus);
			if (httpStatus.equals(HttpStatus.OK)) {
				String body = httpResponse.getBody();
				System.out.println("Oauth获取Token接口返回数据：" + body);
				JSONObject jsonMsg = JSONObject.parseObject(body);
				access_token = jsonMsg.getString("access_token");
				System.out.println("access_token:" + access_token);
			} else {
				System.out.println("Oauth获取Token接口http响应不是200");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (!StringUtils.isEmpty(access_token)) {
			// 用access_token获取用户信息成功，代表用户认证成功
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String authorization = "Bearer " + access_token;
			headers.add("Authorization", authorization);
			HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
			ResponseEntity<String> httResponse = restTemplate.exchange(oauth_user_token_uri,
					HttpMethod.GET, requestEntity, String.class);

			HttpStatus httpStatus = httResponse.getStatusCode();
			System.out.println("httpStatus:" + httpStatus);
			if (httpStatus.equals(HttpStatus.OK)) {
				String body = httResponse.getBody();
				System.out.println("根据token获取用户信息接口返回数据：" + body);				
				JSONObject jsonMsg = JSONObject.parseObject(body);
				int statusCode = jsonMsg.getInteger("statusCode");
				if (statusCode == 200) {
					String comments = jsonMsg.getString("comments");
					System.out.println("comments:" + comments);
					String data = jsonMsg.getString("data");
					System.out.println("data:" + data);
					JSONObject jsonData = JSONObject.parseObject(data);
					String username = jsonData.getString("account");
					System.out.println("username:" + username);
					// 认证成功，跳转到成功界面
					final HttpServletRequest httpRequest = (HttpServletRequest)request;
					final HttpServletResponse httpResponse = (HttpServletResponse)response;
					WebApplicationContext context =WebApplicationContextUtils.getWebApplicationContext(httpRequest.getServletContext());
					UserDetailsService userDetailsService=context.getBean(UserDetailsService.class);
					UserDetails userDetails =userDetailsService.loadUserByUsername(username);
					if(userDetails != null) {
						final UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authentication);
						System.out.println("认证成功，跳转到成功界面");
						return 0;
					}else {
						return 1;
					}
				}
			} else {
				System.out.println("根据token获取用户信息失败");
			}

		}
		return 1;

		
	}
	
	@Override
	public Object authorize(HttpServletRequest request, HttpServletResponse response) {
		try {
			String clientId = "3LTcQgrA1hNPtasr";//// 统一服务平台提供
			String redirect_uri = fxdz;// 要改成业务系统自己ip地址，统一服务平台能访问到，这个是demo的地址
			String authorize_uri = authorize;// 统一服务平台部署的ip和端口
			// 判断用户是否登录
			boolean flag = true;
			if (flag) {
				response.sendRedirect(authorize_uri = authorize_uri + "?response_type=code&client_id=" + clientId
						+ "&redirect_uri=" + redirect_uri + "&state=AnVNdXltVYuY8GLD70PRao0HzqmoRMjh");
			} else {
				// request.getRequestDispatcher("/success.jsp").forward(request, response);
				System.out.println("认证成功，跳转到成功界面");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
