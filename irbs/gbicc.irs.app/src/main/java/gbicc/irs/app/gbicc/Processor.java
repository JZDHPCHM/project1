package gbicc.irs.app.gbicc;

import java.util.UUID;

public class Processor {
	
	private String id;
	private String description=null;
	private Integer order;
	private String type;
	private String optionParameterCode;
	private String arithmetic=null;
	private String ternaryCondition=null;
	private String ternaryTrue=null;
	private String ternaryFalse=null;
	private String when=null;
	private String then=null;
	private boolean isWhenThenShorted = false;
	private String numberRangeVar=null;
	private String numberRange=null;
	private String conditionRange=null;
	private String decisionTable2C=null;
	private String decisionTable=null;
	private String decisionTree=null;
	private String executionFlow=null;
	private String script=null;
	public String getId() {
		return id;
	}
	public void setId() {
		this.id = UUID.randomUUID().toString();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOptionParameterCode() {
		return optionParameterCode;
	}
	public void setOptionParameterCode(String optionParameterCode) {
		this.optionParameterCode = optionParameterCode;
	}
	public String getArithmetic() {
		return arithmetic;
	}
	public void setArithmetic(String arithmetic) {
		this.arithmetic = arithmetic;
	}
	public String getTernaryCondition() {
		return ternaryCondition;
	}
	public void setTernaryCondition(String ternaryCondition) {
		this.ternaryCondition = ternaryCondition;
	}
	public String getTernaryTrue() {
		return ternaryTrue;
	}
	public void setTernaryTrue(String ternaryTrue) {
		this.ternaryTrue = ternaryTrue;
	}
	public String getTernaryFalse() {
		return ternaryFalse;
	}
	public void setTernaryFalse(String ternaryFalse) {
		this.ternaryFalse = ternaryFalse;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getThen() {
		return then;
	}
	public void setThen(String then) {
		this.then = then;
	}
	public boolean getIsWhenThenShorted() {
		return isWhenThenShorted;
	}
	public void setIsWhenThenShorted(boolean isWhenThenShorted) {
		this.isWhenThenShorted = isWhenThenShorted;
	}
	public String getNumberRangeVar() {
		return numberRangeVar;
	}
	public void setNumberRangeVar(String numberRangeVar) {
		this.numberRangeVar = numberRangeVar;
	}
	public String getNumberRange() {
		return numberRange;
	}
	public void setNumberRange(String numberRange) {
		this.numberRange = numberRange;
	}
	public String getConditionRange() {
		return conditionRange;
	}
	public void setConditionRange(String conditionRange) {
		this.conditionRange = conditionRange;
	}
	public String getDecisionTable2C() {
		return decisionTable2C;
	}
	public void setDecisionTable2C(String decisionTable2C) {
		this.decisionTable2C = decisionTable2C;
	}
	public String getDecisionTable() {
		return decisionTable;
	}
	public void setDecisionTable(String decisionTable) {
		this.decisionTable = decisionTable;
	}
	public String getDecisionTree() {
		return decisionTree;
	}
	public void setDecisionTree(String decisionTree) {
		this.decisionTree = decisionTree;
	}
	public String getExecutionFlow() {
		return executionFlow;
	}
	public void setExecutionFlow(String executionFlow) {
		this.executionFlow = executionFlow;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
	
	
}
