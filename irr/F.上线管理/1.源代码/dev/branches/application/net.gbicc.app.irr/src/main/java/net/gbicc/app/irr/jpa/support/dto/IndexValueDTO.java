package net.gbicc.app.irr.jpa.support.dto;

import java.io.Serializable;

/**
* 指标结果的DTO
*/
public class IndexValueDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 指标ID
	 */
	private String indexId;
	/**
	 * 指标编码
	 */
	private String code;
	/**
	 * 指标名称
	 */
	private String name;
	/**
	 * 指标值
	 */
	private String value;
	/**
	 * 行次
	 */
	private String line;
	
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
}
