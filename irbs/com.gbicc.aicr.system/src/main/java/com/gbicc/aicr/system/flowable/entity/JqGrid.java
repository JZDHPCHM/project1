package com.gbicc.aicr.system.flowable.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

public class JqGrid {
		String url;
		String recordtext ="显示：{0} - {1}，总数：{2}";
		String emptyrecords ="本次查询无数据！";
		String loadtext ="读取中...";
		String pgtext ="第 {0}页， 共{1}页";
		String mtype ="get";
		String datatype ="json";
		String height ="100%";
		Integer scrollOffset =0;
		ResponseWrapper<Object> data;
		boolean autowidth =true;
		boolean shrinkToFit =true;
		List<String> colNames =new ArrayList<String>();
		List<ColModel> colModel =new ArrayList<ColModel>();
		boolean viewrecords =true;
		String caption="";
		boolean add=true;
		boolean edit=true;
		String addtext="Add";
		String edittext="Edit";
		boolean rownumbers=false;
		boolean hidegrid=false;
		JsonReader jsonReader;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getRecordtext() {
			return recordtext;
		}
		public void setRecordtext(String recordtext) {
			this.recordtext = recordtext;
		}
		public String getEmptyrecords() {
			return emptyrecords;
		}
		public void setEmptyrecords(String emptyrecords) {
			this.emptyrecords = emptyrecords;
		}
		public String getLoadtext() {
			return loadtext;
		}
		public void setLoadtext(String loadtext) {
			this.loadtext = loadtext;
		}
		public String getPgtext() {
			return pgtext;
		}
		public void setPgtext(String pgtext) {
			this.pgtext = pgtext;
		}
		public String getMtype() {
			return mtype;
		}
		public void setMtype(String mtype) {
			this.mtype = mtype;
		}
		public String getDatatype() {
			return datatype;
		}
		public void setDatatype(String datatype) {
			this.datatype = datatype;
		}
		public String getHeight() {
			return height;
		}
		public void setHeight(String height) {
			this.height = height;
		}
		public Integer getScrollOffset() {
			return scrollOffset;
		}
		public void setScrollOffset(Integer scrollOffset) {
			this.scrollOffset = scrollOffset;
		}
		public boolean isAutowidth() {
			return autowidth;
		}
		public void setAutowidth(boolean autowidth) {
			this.autowidth = autowidth;
		}
		public boolean isShrinkToFit() {
			return shrinkToFit;
		}
		public void setShrinkToFit(boolean shrinkToFit) {
			this.shrinkToFit = shrinkToFit;
		}
		public List<String> getColNames() {
			return colNames;
		}
		public void setColNames(List<String> colNames) {
			this.colNames = colNames;
		}
		public List<ColModel> getColModel() {
			return colModel;
		}
		public void setColModel(List<ColModel> colModel) {
			this.colModel = colModel;
		}
		public boolean isViewrecords() {
			return viewrecords;
		}
		public void setViewrecords(boolean viewrecords) {
			this.viewrecords = viewrecords;
		}
		public String getCaption() {
			return caption;
		}
		public void setCaption(String caption) {
			this.caption = caption;
		}
		public boolean isAdd() {
			return add;
		}
		public void setAdd(boolean add) {
			this.add = add;
		}
		public boolean isEdit() {
			return edit;
		}
		public void setEdit(boolean edit) {
			this.edit = edit;
		}
		public String getAddtext() {
			return addtext;
		}
		public void setAddtext(String addtext) {
			this.addtext = addtext;
		}
		public String getEdittext() {
			return edittext;
		}
		public void setEdittext(String edittext) {
			this.edittext = edittext;
		}
		public boolean isRownumbers() {
			return rownumbers;
		}
		public void setRownumbers(boolean rownumbers) {
			this.rownumbers = rownumbers;
		}
		public boolean isHidegrid() {
			return hidegrid;
		}
		public void setHidegrid(boolean hidegrid) {
			this.hidegrid = hidegrid;
		}
		public JsonReader getJsonReader() {
			return jsonReader;
		}
		public void setJsonReader(JsonReader jsonReader) {
			this.jsonReader = jsonReader;
		}
		public ResponseWrapper<Object> getData() {
			return data;
		}
		public void setData(ResponseWrapper<Object> data) {
			this.data = data;
		}
	
		
}
