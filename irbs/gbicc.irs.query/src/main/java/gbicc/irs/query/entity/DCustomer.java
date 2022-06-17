package gbicc.irs.query.entity;

import java.io.Serializable;
import java.util.Date;

public class DCustomer implements Serializable{

	private static final long serialVersionUID = -6522918650428060442L;
	
	private String custid;
	
	private String custno;
	
	private String custname;
	
	private String type;
	
	private String flowstatus;
	
	private String creator;
	
	private String creatorName;
	
	private String creatorg;
	
	private Date createdate;
	
	private Date completeTime;
	
	private String startType;
	
	private String rate;
	
	private String isValid;
	
	private String vaild;
	
	private String intitiationMode;
	
	private Date invalidDate;
	
	private Date effectiveDate;
	
	private String rateBefore;
	
	private Date endDate;
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getRateBefore() {
		return rateBefore;
	}

	public void setRateBefore(String rateBefore) {
		this.rateBefore = rateBefore;
	}

	public String getVaild() {
		return vaild;
	}

	public void setVaild(String vaild) {
		this.vaild = vaild;
	}

	public String getIntitiationMode() {
		return intitiationMode;
	}

	public void setIntitiationMode(String intitiationMode) {
		this.intitiationMode = intitiationMode;
	}

	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getCreatorg() {
		return creatorg;
	}

	public void setCreatorg(String creatorg) {
		this.creatorg = creatorg;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlowstatus() {
		return flowstatus;
	}

	public void setFlowstatus(String flowstatus) {
		this.flowstatus = flowstatus;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getStartType() {
		return startType;
	}

	public void setStartType(String startType) {
		this.startType = startType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	

	

}
