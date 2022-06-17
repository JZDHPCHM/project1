package com.gbicc.aicr.system.flowable.entity;

public class JsonReader {
	String root = "response.data";
	String records = "response.totalRows";
	String total = "response.totalPages"; // 总共多少页
	boolean repeatitems= false;
	String id= "0";
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getRecords() {
		return records;
	}
	public void setRecords(String records) {
		this.records = records;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public boolean isRepeatitems() {
		return repeatitems;
	}
	public void setRepeatitems(boolean repeatitems) {
		this.repeatitems = repeatitems;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
