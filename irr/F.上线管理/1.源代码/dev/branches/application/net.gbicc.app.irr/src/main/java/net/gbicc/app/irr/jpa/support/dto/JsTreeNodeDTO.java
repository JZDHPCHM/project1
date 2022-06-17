package net.gbicc.app.irr.jpa.support.dto;

import net.sf.json.JSONObject;

/**
* jstree树节点实体
*
*/
public class JsTreeNodeDTO {

	/**
	 * 节点唯一标识
	 */
	private String id;
	/**
	 * 节点内容
	 */
	private String text;
	/**
	 * 是否有子节点
	 */
	private Boolean children;
	/**
	 * 父节点唯一标识
	 */
	private String parent;
	
	/**
	 * 自定义属性
	 */
	private JSONObject li_attr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getChildren() {
		return children;
	}

	public void setChildren(Boolean children) {
		this.children = children;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public JSONObject getLi_attr() {
		return li_attr;
	}

	public void setLi_attr(JSONObject li_attr) {
		this.li_attr = li_attr;
	}
}
