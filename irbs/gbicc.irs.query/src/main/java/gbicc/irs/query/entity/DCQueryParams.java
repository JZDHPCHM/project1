package gbicc.irs.query.entity;

import java.io.Serializable;

public class DCQueryParams implements Serializable{

	private static final long serialVersionUID = 42710030640412359L;
	
	private String custNo;
	
	private String custName;
	
	private String type;
	
	private String creator;
	
	private String creatorg;
	
	private String flowstatus;
	
	private String startType;

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorg() {
		return creatorg;
	}

	public void setCreatorg(String creatorg) {
		this.creatorg = creatorg;
	}

	public String getFlowstatus() {
		return flowstatus;
	}

	public void setFlowstatus(String flowstatus) {
		this.flowstatus = flowstatus;
	}

	public String getStartType() {
		return startType;
	}

	public void setStartType(String startType) {
		this.startType = startType;
	}


}
