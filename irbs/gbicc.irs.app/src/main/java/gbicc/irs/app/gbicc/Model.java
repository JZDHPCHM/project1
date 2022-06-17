package gbicc.irs.app.gbicc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Model {
	private String javaClassName="org.wsp.engine.model.core.po.Model";
	private String id;
	private String name;
	private String type="MODEL";
	private boolean isTop=false;
	private String code;
	private String description=null;
	private String status="SKETCH";
	private String version=null;
	private Integer order=null;
	private String category=null;;
	private String executeMode="DOWN_TOP";
	private String effectiveDate=null;
	private String imports=null;
	private List<Parameter> parameters = new ArrayList<Parameter>();
	private List<String> testCases = new ArrayList<String>();
	private List<String> scoreCardVars = new ArrayList<String>();
	private List<Model> children = new ArrayList<Model>();
	public String getJavaClassName() {
		return javaClassName;
	}
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}
	public String getId() {
		return id;
	}
	public void setId() {
		this.id = UUID.randomUUID().toString();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getExecuteMode() {
		return executeMode;
	}
	public void setExecuteMode(String executeMode) {
		this.executeMode = executeMode;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getImports() {
		return imports;
	}
	public void setImports(String imports) {
		this.imports = imports;
	}
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	public List<String> getTestCases() {
		return testCases;
	}
	public void setTestCases(List<String> testCases) {
		this.testCases = testCases;
	}
	public List<String> getScoreCardVars() {
		return scoreCardVars;
	}
	public void setScoreCardVars(List<String> scoreCardVars) {
		this.scoreCardVars = scoreCardVars;
	}
	public List<Model> getChildren() {
		return children;
	}
	public void setChildren(List<Model> children) {
		this.children = children;
	}
	
	
}
