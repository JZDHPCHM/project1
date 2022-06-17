package gbicc.irs.app.gbicc;

import java.util.UUID;

public class Options {
	
	private String id;
	private String title;
	private String value;
	private String description;
	private String inputValue;
	private String config=null;
	public String getId() {
		return id;
	}
	public void setId() {
		this.id = UUID.randomUUID().toString();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getInputValue() {
		return inputValue;
	}
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	
	
	
}
