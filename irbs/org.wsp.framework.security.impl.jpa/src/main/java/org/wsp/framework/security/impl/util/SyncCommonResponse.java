package org.wsp.framework.security.impl.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONType;

@JSONType(orders = { "statusCode", "comments", "data" })
public class SyncCommonResponse {

	private Integer statusCode;
	private String comments;
	private JSONArray data;

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}
}
