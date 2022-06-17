package gbicc.irs.commom.module.business;

import org.wsp.framework.jpa.model.org.entity.Org;

public abstract class PJFBQuery {
	public Org currOrg;
	public String get(){
		return "";
	}
	public Org getCurrOrg() {
		return currOrg;
	}
	public void setCurrOrg(Org currOrg) {
		this.currOrg = currOrg;
	}
	
	public int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
