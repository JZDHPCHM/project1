package gbicc.irs.app.gbicc;

import java.util.UUID;

public class Temp {
	private String uuid;
	private Integer[] _cellSelection_0= {0};
	private boolean minIncluded = true;
	private String _embeddedComponents_isc_NumberRangeItem_0_numberRange = null;
	private String min;
	private String max;
	private Integer[] _cellSelection_9= {0};
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
	public String get_embeddedComponents_isc_NumberRangeItem_0_numberRange() {
		return _embeddedComponents_isc_NumberRangeItem_0_numberRange;
	}
	public void set_embeddedComponents_isc_NumberRangeItem_0_numberRange(
			String _embeddedComponents_isc_NumberRangeItem_0_numberRange) {
		this._embeddedComponents_isc_NumberRangeItem_0_numberRange = _embeddedComponents_isc_NumberRangeItem_0_numberRange;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public Integer[] get_cellSelection_9() {
		return _cellSelection_9;
	}
	public void set_cellSelection_9(Integer[] _cellSelection_9) {
		this._cellSelection_9 = _cellSelection_9;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
