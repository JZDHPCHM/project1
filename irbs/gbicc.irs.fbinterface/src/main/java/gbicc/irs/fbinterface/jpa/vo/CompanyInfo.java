package gbicc.irs.fbinterface.jpa.vo;

import java.util.ArrayList;
import java.util.List;

public class CompanyInfo {

	public String registereCapital;//注册资本（万元）
	public String mainProducts;//主要产品
	public String registrationDate;//注册日期
	public String certificateExpiryDate;//证书到期日
	public String economicType;//经济类型
	public String legalRepresentative;//法定代表人
	
	
	
	

	
	List<ShareholdersInformation> shareholders = new ArrayList<ShareholdersInformation>();
	
	List<AddressInfo> addressInfo = new ArrayList<AddressInfo>();
	
	List<ManagementInfor> managementInfor = new ArrayList<ManagementInfor>();
	
	
	
	public List<ManagementInfor> getManagementInfor() {
		return managementInfor;
	}
	public void setManagementInfor(List<ManagementInfor> managementInfor) {
		this.managementInfor = managementInfor;
	}
	public List<AddressInfo> getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(List<AddressInfo> addressInfo) {
		this.addressInfo = addressInfo;
	}
	public List<ShareholdersInformation> getShareholders() {
		return shareholders;
	}
	public void setShareholders(List<ShareholdersInformation> shareholders) {
		this.shareholders = shareholders;
	}
	public String getRegistereCapital() {
		return registereCapital;
	}
	public void setRegistereCapital(String registereCapital) {
		this.registereCapital = registereCapital;
	}
	public String getMainProducts() {
		return mainProducts;
	}
	public void setMainProducts(String mainProducts) {
		this.mainProducts = mainProducts;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getCertificateExpiryDate() {
		return certificateExpiryDate;
	}
	public void setCertificateExpiryDate(String certificateExpiryDate) {
		this.certificateExpiryDate = certificateExpiryDate;
	}
	public String getEconomicType() {
		return economicType;
	}
	public void setEconomicType(String economicType) {
		this.economicType = economicType;
	}
	public String getLegalRepresentative() {
		return legalRepresentative;
	}
	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}
	
	
}
