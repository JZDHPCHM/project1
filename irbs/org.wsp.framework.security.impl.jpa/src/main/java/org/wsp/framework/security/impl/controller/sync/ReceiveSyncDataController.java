package org.wsp.framework.security.impl.controller.sync;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.wsp.framework.security.impl.util.MDC;
import org.wsp.framework.security.impl.util.SyncCommonResponse;

@RestController
@RequestMapping("/loadUser")
public class ReceiveSyncDataController {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/sync/user", method = RequestMethod.POST)
	public Object syncUser(@RequestBody String requestBody) throws SQLException {
		System.out.println("sync user requestBody:" + requestBody);
		SyncCommonResponse response = new SyncCommonResponse();
		int retCode = 1;
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		boolean ac = conn.getAutoCommit();
	    conn.setAutoCommit(false);
		try {
			JSONObject jsonMsg = JSONObject.parseObject(requestBody);
			int timestamp = 0;
			String algo = "SHA256";
			String mesHmac = "";
			String data = "";
			timestamp = jsonMsg.getInteger("timestamp");
			System.out.println("timestamp:" + timestamp);
			algo = jsonMsg.getString("algo");
			System.out.println("algo:" + algo);
			mesHmac = jsonMsg.getString("mesHmac");
			System.out.println("mesHmac:" + mesHmac);
			data = jsonMsg.getString("data");
			System.out.println("data:" + data);

			String clientId = "3LTcQgrA1hNPtasr";// 由统一服务平台提供
			System.out.println("clientId:" + clientId);
			String clientSecret = "MXY2aoOo0kH31Fy3sLeJIFE1wdDWzICd";// 由统一服务平台提供
			System.out.println("clientSecret:" + clientSecret);
			String srcData = clientId + timestamp + data;

			String serverCalcMesHmac = MDC.calcHMACSAH256(srcData, clientSecret);
			System.out.println("serverCalcMesHmac:" + serverCalcMesHmac);
			if (StringUtils.isEmpty(mesHmac) || !mesHmac.equalsIgnoreCase(serverCalcMesHmac)) {
				response.setStatusCode(100001);
				response.setComments("消息签名值为空或错误");
				response.setData(null);
				return JSONObject.toJSON(response);
			}

			JSONArray dataArray = JSON.parseArray(data);
			String arrayStr = JSONArray.toJSONString(dataArray);
			System.out.println("arrayStr:" + arrayStr);
			Iterator<Object> it = dataArray.iterator();
			while (it.hasNext()) {
				// 遍历数组
				JSONObject arrayObj = (JSONObject) it.next();// JSONArray中是很多个JSONObject对象
				Integer operate = arrayObj.getInteger("operate");
				System.out.println("operate:" + operate);
				String id = arrayObj.getString("id");
				System.out.println("id:" + id);
				String userAcc = arrayObj.getString("userAcc");
				System.out.println("userAcc:" + userAcc);
				String userName = arrayObj.getString("userName");
				System.out.println("userName:" + userName);
				String email = arrayObj.getString("email");
				System.out.println("email:" + email);
				String phone = arrayObj.getString("phone");
				System.out.println("phone:" + phone);
				String orgId = arrayObj.getString("orgId");
				System.out.println("orgId:" + orgId);
				String responsibleMan = arrayObj.getString("responsibleMan");
				System.out.println("responsibleMan:" + responsibleMan);
				String postId = arrayObj.getString("postId");
				System.out.println("postId:" + postId);
				Integer secureLevel = arrayObj.getInteger("secureLevel");// 用户的安全级别
				System.out.println("secureLevel:" + secureLevel);
				Integer orgOrderNo = arrayObj.getInteger("orgOrderNo");
				System.out.println("orgOrderNo:" + orgOrderNo);
				Integer jobOrderNo = arrayObj.getInteger("jobOrderNo");
				System.out.println("jobOrderNo:" + jobOrderNo);
				JSONArray otherJobArray = arrayObj.getJSONArray("otherJob");
				System.out.println("otherJobArray:" + otherJobArray);
				JSONArray userRole = arrayObj.getJSONArray("userRole");
				System.out.println("userRole:" + userRole);
				if (operate == 1) {
					if(!StringUtils.isEmpty(id)) {
						String sql = "select * from fr_aa_user where fd_id = '" +id+ "'";
						List<Map<String, Object>> ids = jdbcTemplate.queryForList(sql);
						if(ids != null && ids.size() > 0) {
							String updateSql = "UPDATE FR_AA_USER SET FD_LOGINNAME = '"+userAcc+"', FD_USERNAME = '"+userName+"'\r\n" + 
									"WHERE FD_ID = '"+id+"'";
							Integer a = jdbcTemplate.update(updateSql);
							conn.commit();
						}else {
							String insertSql = "insert into FR_AA_USER(\r\n" + 
									"FD_ID,\r\n" + 
									"FD_LOGINNAME,\r\n" + 
									"FD_USERNAME,\r\n" + 
									"FD_ENABLE,\r\n" + 
									"FD_IS_ACCOUNT_EXPIRED,\r\n" + 
									"FD_IS_ACCOUNT_LOCKED,\r\n" + 
									"FD_IS_CREDENTIALS_EXPIRED) \r\n" + 
									"values(\r\n" + 
									"'"+id+"','"+userAcc+"','"+userName+"',1,0,0,0) ";
							
							Integer b = jdbcTemplate.update(insertSql);
							conn.commit();
						}
						String sql1 = "select * from fr_aa_user where fd_id = '" +id+ "'";
						List<Map<String, Object>> userIds = jdbcTemplate.queryForList(sql1);
						if (null != userRole && userRole.size() > 0) {
							for (int i = 0; i < userRole.size(); i++) {
								// 遍历用户角色
								JSONObject jsonObject = userRole.getJSONObject(i);
								String role_id = jsonObject.getString("id");
								System.out.println("role_id:" + role_id);
								String roleId = jsonObject.getString("roleId");
								System.out.println("roleId:" + roleId);
								String roleCode = jsonObject.getString("roleCode");
								System.out.println("roleCode:" + roleCode);
								String roleName = jsonObject.getString("roleName");
								System.out.println("roleName:" + roleName);
								Integer user_role_operate = jsonObject.getInteger("operate");
								System.out.println("user_role_operate:" + user_role_operate);
								
								String roleSql = "select * from fr_aa_role where fd_id = '"+roleId+"'";
								List<Map<String, Object>> roleIds = jdbcTemplate.queryForList(roleSql);
								if(userIds != null && userIds.size() > 0 && roleIds != null && roleIds.size() > 0) {
										String delUserRole = "delete from fr_aa_user_role where fd_user_id = '"+id+"' and fd_role_id = '"+roleId+"'";
										Integer c = jdbcTemplate.update(delUserRole);
										conn.commit();
										if(user_role_operate != null && user_role_operate != 3) {
											String intoUserRole = "insert into fr_aa_user_role(fd_user_id,fd_role_id) values('"+id+"','"+roleId+"')";
											Integer d = jdbcTemplate.update(intoUserRole);
											conn.commit();
										}
								}
							}
						}
						response.setStatusCode(200);
						response.setComments("更新成功");
						response.setData(null);
					}
					// 根据id判断用户是否存在，如存在则更新用户，否则新增用户
				} else if (operate == 3) {
					// 删除用户
					
					if(!StringUtils.isEmpty(id)) {
						if (null != userRole && userRole.size() > 0) {
							for (int i = 0; i < userRole.size(); i++) {
								// 遍历用户角色
								JSONObject jsonObject = userRole.getJSONObject(i);
								String role_id = jsonObject.getString("id");
								System.out.println("role_id:" + role_id);
								String roleId = jsonObject.getString("roleId");
								System.out.println("roleId:" + roleId);
								String roleCode = jsonObject.getString("roleCode");
								System.out.println("roleCode:" + roleCode);
								String roleName = jsonObject.getString("roleName");
								System.out.println("roleName:" + roleName);
								Integer user_role_operate = jsonObject.getInteger("operate");
								System.out.println("user_role_operate:" + user_role_operate);
								String delUserRole1 = "delete from fr_aa_user_role where fd_user_id = '"+id+"' and fd_role_id = '"+roleId+"'";
								Integer f = jdbcTemplate.update(delUserRole1);
								conn.commit();
							}
						}
						String delUser = "delete from FR_AA_USER where fd_id = '"+id+"'";
						Integer e = jdbcTemplate.update(delUser);
						conn.commit();
						response.setStatusCode(200);
						response.setComments("删除成功");
						response.setData(null);
					}
				}
			}
		} catch (Exception ex) {
			conn.rollback();
			ex.printStackTrace();
			response.setStatusCode(retCode);
			response.setComments("同步用户异常");
			response.setData(null);
		} finally {
	        conn.setAutoCommit(ac);
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	            	
	            }
	        }
	    }
		return JSONObject.toJSON(response);
	}

	@RequestMapping(value = "/sync/org", method = RequestMethod.POST)
	public Object syncOrg(@RequestBody String requestBody) {
		System.out.println("sync org requestBody:" + requestBody);
		SyncCommonResponse response = new SyncCommonResponse();
		int retCode = 1;
		try {
			JSONObject jsonMsg = JSONObject.parseObject(requestBody);
			int timestamp = 0;
			String algo = "";
			String mesHmac = "";
			String data = "";
			timestamp = jsonMsg.getInteger("timestamp");
			System.out.println("timestamp:" + timestamp);
			algo = jsonMsg.getString("algo");
			System.out.println("algo:" + algo);
			mesHmac = jsonMsg.getString("mesHmac");
			System.out.println("mesHmac:" + mesHmac);
			data = jsonMsg.getString("data");
			System.out.println("data:" + data);
			//
			String clientId = "nPQe7C6Qkqed8ZTl";// 统一服务平台提供
			System.out.println("clientId:" + clientId);
			String clientSecret = "CTOYlrT1O2z22viomEjwX82JLdITUmsS";
			System.out.println("clientSecret:" + clientSecret);// 统一服务平台提供
			String srcData = clientId + timestamp + data;

			String serverCalcMesHmac = MDC.calcHMACSAH256(srcData, clientSecret);
			System.out.println("serverCalcMesHmac:" + serverCalcMesHmac);
			if (StringUtils.isEmpty(mesHmac) || !mesHmac.equalsIgnoreCase(serverCalcMesHmac)) {
				response.setStatusCode(100001);
				response.setComments("消息签名值为空或错误");
				response.setData(null);
				return JSONObject.toJSON(response);
			}

			JSONArray dataArray = JSON.parseArray(data);
			String arrayStr = JSONArray.toJSONString(dataArray);
			System.out.println("arrayStr:" + arrayStr);
			Iterator<Object> it = dataArray.iterator();
			while (it.hasNext()) {
				// 遍历数组
				JSONObject arrayObj = (JSONObject) it.next();// JSONArray中是很多个JSONObject对象
				String id = arrayObj.getString("id");
				System.out.println("id:" + id);
				String orgCode = arrayObj.getString("orgCode");
				System.out.println("orgCode:" + orgCode);
				String name = arrayObj.getString("name");
				System.out.println("name:" + name);
				Integer orderNo = arrayObj.getInteger("orderNo");
				System.out.println("orderNo:" + orderNo);
				String parentOrg = arrayObj.getString("parentOrg");
				System.out.println("parentOrg:" + parentOrg);
				String rootId = arrayObj.getString("rootId");
				System.out.println("rootId:" + rootId);
				String responsibleMan = arrayObj.getString("responsibleMan");
				System.out.println("responsibleMan:" + responsibleMan);
				String orgTypeName = arrayObj.getString("orgTypeName");
				System.out.println("orgTypeName:" + orgTypeName);
				Integer orgTypeLevel = arrayObj.getInteger("orgTypeLevel");
				System.out.println("orgTypeLevel:" + orgTypeLevel);
				Integer operate = arrayObj.getInteger("operate");
				System.out.println("operate:" + operate);
				if (operate == 1) {
					// 根据id判断组织是否存在，如存在则更新组织，否则新增组织
				} else if (operate == 3) {
					// 删除组织
				}
			}

			response.setStatusCode(retCode);
			response.setComments("");
			response.setData(null);
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatusCode(retCode);
			response.setComments("同步组织异常");
			response.setData(null);
		}
		return JSONObject.toJSON(response);
	}

	@RequestMapping(value = "/sync/post", method = RequestMethod.POST)
	public Object syncPost(@RequestBody String requestBody) {
		System.out.println("sync post requestBody:" + requestBody);
		SyncCommonResponse response = new SyncCommonResponse();
		int retCode = 1;
		try {
			JSONObject jsonMsg = JSONObject.parseObject(requestBody);
			int timestamp = 0;
			String algo = "";
			String mesHmac = "";
			String data = "";
			timestamp = jsonMsg.getInteger("timestamp");
			System.out.println("timestamp:" + timestamp);
			algo = jsonMsg.getString("algo");
			System.out.println("algo:" + algo);
			mesHmac = jsonMsg.getString("mesHmac");
			System.out.println("mesHmac:" + mesHmac);
			data = jsonMsg.getString("data");
			System.out.println("data:" + data);

			String clientId = "nPQe7C6Qkqed8ZTl";// 统一服务平台提供
			System.out.println("clientId:" + clientId);
			String clientSecret = "CTOYlrT1O2z22viomEjwX82JLdITUmsS";// 统一服务平台提供
			System.out.println("clientSecret:" + clientSecret);
			String srcData = clientId + timestamp + data;
			// demo只做hmac_sha256验证
			String serverCalcMesHmac = MDC.calcHMACSAH256(srcData, clientSecret);
			System.out.println("serverCalcMesHmac:" + serverCalcMesHmac);
			if (StringUtils.isEmpty(mesHmac) || !mesHmac.equalsIgnoreCase(serverCalcMesHmac)) {
				response.setStatusCode(100001);
				response.setComments("消息签名值为空或错误");
				response.setData(null);
				return JSONObject.toJSON(response);
			}

			JSONArray dataArray = JSON.parseArray(data);
			String arrayStr = JSONArray.toJSONString(dataArray);
			System.out.println("arrayStr:" + arrayStr);
			Iterator<Object> it = dataArray.iterator();
			while (it.hasNext()) {
				// 遍历数组
				JSONObject arrayObj = (JSONObject) it.next();// JSONArray中是很多个JSONObject对象
				String id = arrayObj.getString("id");
				System.out.println("id:" + id);
				String postCode = arrayObj.getString("postCode");
				System.out.println("postCode:" + postCode);
				String postName = arrayObj.getString("postName");
				System.out.println("postName:" + postName);
				Integer postOrder = arrayObj.getInteger("postOrder");
				System.out.println("postOrder:" + postOrder);
				Integer operate = arrayObj.getInteger("operate");
				System.out.println("operate:" + operate);
				if (operate == 1) {
					// 根据id判断岗位是否存在，如存在则更新岗位，否则新增岗位
				} else if (operate == 3) {
					// 删除岗位
				}
			}

			response.setStatusCode(retCode);
			response.setComments("");
			response.setData(null);
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatusCode(retCode);
			response.setComments("同步岗位异常");
			response.setData(null);
		}
		return JSONObject.toJSON(response);
	}
}
