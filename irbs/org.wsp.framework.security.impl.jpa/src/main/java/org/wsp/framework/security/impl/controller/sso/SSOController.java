package org.wsp.framework.security.impl.controller.sso;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/demo")
public class SSOController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/sso/authorize", method = RequestMethod.GET)
	public Object authorize(HttpServletRequest request, HttpServletResponse response) {
		try {
			String clientId = "nPQe7C6Qkqed8ZTl";//// 统一服务平台提供
			String redirect_uri = "http://192.168.1.15:8089/demo/sso/authorize";// 要改成业务系统自己ip地址，统一服务平台能访问到，这个是demo的地址
			String authorize_uri = "https://192.168.36.39/auth_center/oauth/authorize";// 统一服务平台部署的ip和端口
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

	@RequestMapping(value = "/sso/login", method = RequestMethod.GET)
	public Object outhLogin(HttpServletRequest request, HttpServletResponse response) {
		String clientId = "3LTcQgrA1hNPtasr";// 统一服务平台提供
		System.out.println("clientId:" + clientId);
		String clientSecret = "MXY2aoOo0kH31Fy3sLeJIFE1wdDWzICd";// 统一服务平台提供
		System.out.println("clientSecret:" + clientSecret);
		//String redirect_uri = "http://192.168.1.15:8089/demo/sso/authorize";// 要改成业务系统自己ip地址，统一服务平台能访问到,这个是demo的地址
		String redirect_uri = "http://192.168.4.149:8080/enterIndex.action";
		System.out.println("redirect_uri:" + redirect_uri);
		//String oauth_token_uri = "https://192.168.36.39/auth_center/oauth/token";// 统一服务平台部署的ip和端口
		String oauth_token_uri = "https://172.16.9.27/auth_center/oauth/token";
		System.out.println("oauth_token_uri:" + oauth_token_uri);
		//String oauth_user_token_uri = "https://192.168.36.39:8443/manage/user/token/v1";// 统一服务平台部署的ip和端口
		String oauth_user_token_uri = "https://172.16.9.27:8443/manage/user/token/v1";
		System.out.println("oauth_user_token_uri:" + oauth_user_token_uri);
		// 取得提交参数
		String code = request.getParameter("code");
		System.out.println("code:" + code);
		String access_token = "";
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
			ResponseEntity<String> httResponse = restTemplate.exchange(oauth_user_token_uri + "?isRole=true",
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
					String username = jsonData.getString("username");
					System.out.println("username:" + username);
					String adminType = jsonData.getString("adminType");
					String roleVo = jsonData.getString("roleVo");
					System.out.println("roleVo:" + roleVo);
					JSONObject jsonData01 = JSONObject.parseObject(roleVo);
					String roleType = jsonData01.getString("roleType");
					System.out.println("roleType:" + roleType);
					JSONObject jsonData02 = JSONObject.parseObject(roleType);
					Integer ruleType = jsonData02.getInteger("code");
					System.out.println("roleType:" + roleType);
					// 认证成功，跳转到成功界面
					System.out.println("认证成功，跳转到成功界面");
				}
			} else {
				System.out.println("根据token获取用户信息失败");
			}

		}

		return null;
	}
}
