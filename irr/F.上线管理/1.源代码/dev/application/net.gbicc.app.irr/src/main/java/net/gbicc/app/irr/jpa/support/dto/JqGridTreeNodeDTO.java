package net.gbicc.app.irr.jpa.support.dto;
/**
*	jqGridTree的节点类
*/


public class JqGridTreeNodeDTO {

	/**
	 * 树内容实体
	 */
	private Object obj;
	/**
	 * 节点的级别，默认最高级为0
	 */
	private Integer level;
	/**
	 * 该行数据父节点的id
	 */
	private String parent;
	/**
	 * 是否为叶节点，为true时表示该节点下面没有子节点了
	 */
	private Boolean isLeaf;
	/**
	 * 是否默认展开状态(默认不展开)
	 */
	private Boolean expanded = false;
	/**
	 * 是否已经加载过子节点（为false时点击节点会自动加载子节点）
	 */
	private Boolean loaded = false;
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public Boolean getLoaded() {
		return loaded;
	}
	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}
}