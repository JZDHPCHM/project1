package org.wsp.framework.security.impl.controller.roleInfo;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.wsp.framework.security.impl.util.MDC;

@RestController
@RequestMapping("/demo")
public class SyncRoleInfoController {

	@Autowired
	private RestTemplate restTemplate;

	private String randValue = "";

	/**
	 * 准备同步角色接口
	 */
	@GetMapping("/prepare/role")
	public void prepare() {
		System.out.println("enter sync role prepare");
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String clientId = "3LTcQgrA1hNPtasr";// 统一服务平台提供
			System.out.println("clientId:" + clientId);
			String clientSecret = "MXY2aoOo0kH31Fy3sLeJIFE1wdDWzICd";// 统一服务平台提供
			System.out.println("clientSecret:" + clientSecret);

			JSONObject params = new JSONObject();
			params.put("clientId", clientId);
			Instant instant = Instant.now();
			long requestTimestamp = instant.getEpochSecond();
			params.put("timestamp", requestTimestamp);
			randValue = MDC.gen32Encode();
			params.put("randValue", randValue);
			params.put("algo", "SHA256");
			String data = clientId + requestTimestamp + randValue;
			String mesHmac = MDC.calcHMACSAH256(data, clientSecret);
			System.out.println("mesHmac:" + mesHmac);
			params.put("mesHmac", mesHmac);

			String requestBody = JSON.toJSONString(params);
			System.out.println("requestBody:" + requestBody);

			String url = "https://172.16.9.27/user/api/sync/prepare/role";// 统一服务平台提供

			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			HttpStatus httpStatus = response.getStatusCode();
			System.out.println("httpStatus:" + httpStatus);
			if (httpStatus.equals(HttpStatus.OK)) {
				String body = response.getBody();
				System.out.println("准备同步角色接口返回数据：" + body);
				JSONObject jsonMsg = JSONObject.parseObject(body);
				Integer statusCode = jsonMsg.getInteger("statusCode");
				System.out.println("statusCode:" + statusCode);
				String comments = jsonMsg.getString("comments");
				System.out.println("comments:" + comments);
				if (statusCode == 200) {
					System.out.println("准备同步角色成功");
				} else {
					System.out.println("准备同步角色失败：失败原因:" + comments);
				}
			} else {
				System.out.println("准备同步角色接口http响应不是200");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 同步角色接口
	 */
	@GetMapping("/sync/role")
	public void syncRole() {
		System.out.println("enter sync role");
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String clientId = "nPQe7C6Qkqed8ZTl";// 统一服务平台提供
			System.out.println("clientId:" + clientId);
			String clientSecret = "CTOYlrT1O2z22viomEjwX82JLdITUmsS";// 统一服务平台提供
			System.out.println("clientSecret:" + clientSecret);

			JSONObject params = new JSONObject();
			params.put("clientId", clientId);
			Instant instant = Instant.now();
			long requestTimestamp = instant.getEpochSecond();
			params.put("timestamp", requestTimestamp);
			params.put("randValue", randValue);
			params.put("algo", "SHA256");
			JSONArray dataArray = new JSONArray();
			for (int i = 1; i <= 3; i++) {
				JSONObject dataItem = new JSONObject();
				dataItem.put("id", "777b5cc0-ae5d-11e6-8d22-7744" + i);
				dataItem.put("roleCode", "testCode" + i);
				dataItem.put("roleName", "测试角色bb" + i);
				dataItem.put("operate", 1);
				dataArray.add(dataItem);
			}
			String dataJsonArray = dataArray.toJSONString();
			System.out.println("dataJsonArray:" + dataJsonArray);
			params.put("data", dataJsonArray);
			String data = clientId + requestTimestamp + dataJsonArray;
			String mesHmac = MDC.calcHMACSAH256(data, clientSecret);
			System.out.println("mesHmac:" + mesHmac);
			params.put("mesHmac", mesHmac);

			String requestBody = JSON.toJSONString(params);
			System.out.println("requestBody:" + requestBody);

			String url = "https://192.168.36.39/user/api/sync/role";// 统一服务平台提供

			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			HttpStatus httpStatus = response.getStatusCode();
			System.out.println("httpStatus:" + httpStatus);
			if (httpStatus.equals(HttpStatus.OK)) {
				String body = response.getBody();
				System.out.println("同步角色接口返回数据：" + body);
				JSONObject jsonMsg = JSONObject.parseObject(body);
				Integer statusCode = jsonMsg.getInteger("statusCode");
				System.out.println("statusCode:" + statusCode);
				String comments = jsonMsg.getString("comments");
				System.out.println("comments:" + comments);
				if (statusCode == 200) {
					System.out.println("同步角色成功");
				} else {
					System.out.println("同步角色失败：失败原因:" + comments);
				}
			} else {
				System.out.println("同步角色接口http响应不是200");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 准备同步角色接口
	 */
	@GetMapping("/complete/role")
	public void complete() {
		System.out.println("enter complete");
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String clientId = "nPQe7C6Qkqed8ZTl";// 统一服务平台提供
			System.out.println("clientId:" + clientId);
			String clientSecret = "CTOYlrT1O2z22viomEjwX82JLdITUmsS";// 统一服务平台提供
			System.out.println("clientSecret:" + clientSecret);

			JSONObject params = new JSONObject();
			params.put("clientId", clientId);
			Instant instant = Instant.now();
			long requestTimestamp = instant.getEpochSecond();
			params.put("timestamp", requestTimestamp);
			params.put("randValue", randValue);
			params.put("algo", "SHA256");
			String data = clientId + requestTimestamp + randValue;
			String mesHmac = MDC.calcHMACSAH256(data, clientSecret);
			System.out.println("mesHmac:" + mesHmac);
			params.put("mesHmac", mesHmac);

			String requestBody = JSON.toJSONString(params);
			System.out.println("requestBody:" + requestBody);

			String url = "https://192.168.36.39/user/api/sync/complete/role";// 统一服务平台提供

			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			HttpStatus httpStatus = response.getStatusCode();
			System.out.println("httpStatus:" + httpStatus);
			if (httpStatus.equals(HttpStatus.OK)) {
				String body = response.getBody();
				System.out.println("完成同步角色接口返回数据：" + body);
				JSONObject jsonMsg = JSONObject.parseObject(body);
				Integer statusCode = jsonMsg.getInteger("statusCode");
				System.out.println("statusCode:" + statusCode);
				String comments = jsonMsg.getString("comments");
				System.out.println("comments:" + comments);
				if (statusCode == 200) {
					System.out.println("完成同步角色成功");
				} else {
					System.out.println("完成同步角色失败：失败原因:" + comments);
				}
			} else {
				System.out.println("完成同步角色接口http响应不是200");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
