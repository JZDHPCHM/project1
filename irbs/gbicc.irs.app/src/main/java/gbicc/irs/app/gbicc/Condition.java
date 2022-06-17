package gbicc.irs.app.gbicc;

import java.util.UUID;

public class Condition {
	private String uuid;
	private Integer[] _cellSelection_0= {0};
	private boolean minIncluded = true;
	private String _embeddedComponents_isc_ConditionRangeItem_0_conditionRange = null;
	private String condition;
	private String value;
	public String getUuid() {
		return uuid;
	}
	public void setUuid() {
		this.uuid = UUID.randomUUID().toString();
	}
	public Integer[] get_cellSelection_0() {
		return _cellSelection_0;
	}
	public void set_cellSelection_0(Integer[] _cellSelection_0) {
		this._cellSelection_0 = _cellSelection_0;
	}
	public boolean isMinIncluded() {
		return minIncluded;
	}
	public void setMinIncluded(boolean minIncluded) {
		this.minIncluded = minIncluded;
	}
	public String get_embeddedComponents_isc_ConditionRangeItem_0_conditionRange() {
		return _embeddedComponents_isc_ConditionRangeItem_0_conditionRange;
	}
	public void set_embeddedComponents_isc_ConditionRangeItem_0_conditionRange(
			String _embeddedComponents_isc_ConditionRangeItem_0_conditionRange) {
		this._embeddedComponents_isc_ConditionRangeItem_0_conditionRange = _embeddedComponents_isc_ConditionRangeItem_0_conditionRange;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
