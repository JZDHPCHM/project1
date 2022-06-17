package org.wsp.framework.security.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.wsp.framework.security.impl.config.Myconfig;
import org.wsp.framework.security.impl.util.MDC;
import org.wsp.framework.security.service.RoleSynchronousService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class RoleSynchronousServiceImpl implements RoleSynchronousService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String randValue = "";
	
	@Value("${spring.typt.prepare}")
	private String prepare;
	
	@Value("${spring.typt.role}")
	private String role;
	
	@Value("${spring.typt.complete}")
	private String complete;
	
	@Override
	public Integer prepare() throws Exception{
		Myconfig myc = new Myconfig();
		RestTemplate restTemplate = myc.restTemplate();
		System.out.println("enter sync role prepare");
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

		String url = prepare;// 统一服务平台提供

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
				Integer syncCode = syncRole();
				if(syncCode == 200){
					Integer comCode = complete();
					if(comCode == 200) {
						return comCode;
					}
				}
			} else {
				System.out.println("准备同步角色失败：失败原因:" + comments);
			}
		} else {
			System.out.println("准备同步角色接口http响应不是200");
		}
		return 1;
	}

	@Override
	public Integer syncRole() throws Exception {
		System.out.println("enter sync role");
		Myconfig myc = new Myconfig();
		RestTemplate restTemplate = myc.restTemplate();
		MediaType mediaType = MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		HttpHeaders headers = new HttpHeaders();
		//headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setContentType(mediaType);
		String clientId = "3LTcQgrA1hNPtasr";// 统一服务平台提供
		System.out.println("clientId:" + clientId);
		String clientSecret = "MXY2aoOo0kH31Fy3sLeJIFE1wdDWzICd";// 统一服务平台提供
		System.out.println("clientSecret:" + clientSecret);

		JSONObject params = new JSONObject();
		params.put("clientId", clientId);
		Instant instant = Instant.now();
		long requestTimestamp = instant.getEpochSecond();
		params.put("timestamp", requestTimestamp);
		params.put("randValue", randValue);
		params.put("algo", "SHA256");
		String sql = "select FD_ID,FD_CODE,FD_NAME from fr_aa_role";
		List<Map<String, Object>> roleList = jdbcTemplate.queryForList(sql);
		JSONArray dataArray = new JSONArray();
		if(roleList != null && roleList.size() > 0) {
			for (int i = 0; i < roleList.size(); i++) {
				JSONObject dataItem = new JSONObject();
				dataItem.put("id", roleList.get(i).get("FD_ID"));
				dataItem.put("roleCode",  roleList.get(i).get("FD_CODE"));
				dataItem.put("roleName",  roleList.get(i).get("FD_NAME"));
				dataItem.put("operate", 1);
				dataArray.add(dataItem);
			}
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

		String url = role;// 统一服务平台提供
		requestBody = new String(requestBody.getBytes("UTF-8"),"UTF-8");
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
				return statusCode;
			} else {
				System.out.println("同步角色失败：失败原因:" + comments);
			}
		} else {
			System.out.println("同步角色接口http响应不是200");
		}
		return 1;
	}

	@Override
	public Integer complete() throws Exception {
		System.out.println("enter complete");
		Myconfig myc = new Myconfig();
		RestTemplate restTemplate = myc.restTemplate();
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
		params.put("randValue", randValue);
		params.put("algo", "SHA256");
		String data = clientId + requestTimestamp + randValue;
		String mesHmac = MDC.calcHMACSAH256(data, clientSecret);
		System.out.println("mesHmac:" + mesHmac);
		params.put("mesHmac", mesHmac);

		String requestBody = JSON.toJSONString(params);
		System.out.println("requestBody:" + requestBody);

		String url = complete;// 统一服务平台提供

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
				return statusCode;
			} else {
				System.out.println("完成同步角色失败：失败原因:" + comments);
			}
		} else {
			System.out.println("完成同步角色接口http响应不是200");
		}
		return 1;
	}

}
