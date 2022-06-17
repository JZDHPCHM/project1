package gbicc.irs.customer.support;

public class QueryCustomer {
	/**
	 * 客户编号
	 */
	private String custNo;
	/**
	 * 客户名称
	 */
	private String custName;
	/**
	 * 客户规模
	 */
	private String enterpriseScale;
	/**
	 * 国标行业
	 */
	private String gbIndustry;
	/**
	 * 行内行业
	 */
	private String intraIndustry;
	/**
	 * 管户机构
	 */
	private String accountOrg;
	/**
	 * 管户经理
	 */
	private String accountManager;
	/**
	 * 成立开始时间	
	 */
	private String startDt;
	/**
	 * 成立结束时间
	 */
	private String endDt;
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
	
	
	
	public String getEnterpriseScale() {
		return enterpriseScale;
	}
	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
	}
	public String getGbIndustry() {
		return gbIndustry;
	}
	public void setGbIndustry(String gbIndustry) {
		this.gbIndustry = gbIndustry;
	}

	public String getIntraIndustry() {
		return intraIndustry;
	}
	public void setIntraIndustry(String intraIndustry) {
		this.intraIndustry = intraIndustry;
	}
	public String getAccountOrg() {
		return accountOrg;
	}
	public void setAccountOrg(String accountOrg) {
		this.accountOrg = accountOrg;
	}
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	
	
	
}
