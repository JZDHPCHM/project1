package gbicc.irs.fbinterface.jpa.vo;

public class ShareholdersInformation {
	public String shareholderName;//股东名称
	public String typeShareholders;//股东类型
	public String shareholderCertificateNumber;//股东证件号码
	public String capitalContribution;//出资金额（万元）
	public String actualAmount;//实到金额（万元）
	public String stake;//持股比例
	public String getShareholderName() {
		return shareholderName;
	}
	public void setShareholderName(String shareholderName) {
		this.shareholderName = shareholderName;
	}

	
	
	public String getTypeShareholders() {
		return typeShareholders;
	}
	public void setTypeShareholders(String typeShareholders) {
		this.typeShareholders = typeShareholders;
	}
	public String getShareholderCertificateNumber() {
		return shareholderCertificateNumber;
	}
	public void setShareholderCertificateNumber(String shareholderCertificateNumber) {
		this.shareholderCertificateNumber = shareholderCertificateNumber;
	}
	public String getCapitalContribution() {
		return capitalContribution;
	}
	public void setCapitalContribution(String capitalContribution) {
		this.capitalContribution = capitalContribution;
	}
	public String getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getStake() {
		return stake;
	}
	public void setStake(String stake) {
		this.stake = stake;
	}
	

}
