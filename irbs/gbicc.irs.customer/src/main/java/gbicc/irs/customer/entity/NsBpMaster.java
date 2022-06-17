package gbicc.irs.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
   /**
    * ns_bp_master 实体类
    * Sat Oct 12 15:08:13 GMT+08:00 2019 WangShuaiHeng
    */ 
@Entity
@Table(name = "Ns_bp_master")
public class NsBpMaster implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="FD_ID")
	private String fdid;
	@Column(name = "FD_BP_NAME")
	private String bpName;
	@Column(name = "FD_BP_TYPE")
	private String bpType;
	@Column(name = "FD_BP_CATEGORY")
	private String bpCategory;
	@Column(name = "FD_BP_CODE")
	private String bpCode;
	@Column(name = "FD_REGISTERED_CAPITAL")
	private String registeredCapital;
	@Column(name = "FD_FOUNDED_DATE")
	private String foundedDate;
	@Column(name = "FD_LEGAL_PERSON")
	private String legalPerson;
	@Column(name = "FD_ENTERPRISE_SCALE")
	private String enterpriseScale;
	@Column(name = "FD_REG_NUMBER_TYPE")
	private String regNumberType;
	@Column(name = "FD_REG_NUMBER")
	private String regNumber;
	@Column(name = "FD_SEGMENT_INDUSTRY")
	private String segmentIndustry;
	@Column(name = "FD_HIGH_PRECISION")
	private String highPrecision;
	@Column(name = "FD_ECONOMIC_TYPE")
	private String economicType;
	@Column(name = "FD_ENTERPRISE_HONOR")
	private String enterpriseHonor;
	@Column(name = "FD_ORG_TYPE")
	private String orgType;
	@Column(name = "FD_ORG_SUB_TYPE")
	private String orgSubType;
	@Column(name = "FD_PARK_ADDRESS")
	private String parkAddress;
	@Column(name = "FD_MAIN_PRODUCTS")
	private String mainProducts;
	@Column(name = "FD_EMPLOYEE_ID")
	private String employeeId;
	@Column(name = "FD_LEASE_ORGANIZATION")
	private String leaseOrganization;
	@Column(name = "FD_ACTUAL_LESSEE")
	private String actualLessee;
	//承租人类型/企业类型（生产 or 服务）
	@Column(name = "FD_SCORE_TEMPLATE_ID")
	private String scoreTemplateId;
	@Column(name = "MARKET_SIZE")
	private String marketSize;
	@Column(name = "FINANCING_SCALE")
	private String financingScale;
	
	
	
	public String getMarketSize() {
		return marketSize;
	}
	public void setMarketSize(String marketSize) {
		this.marketSize = marketSize;
	}
	public String getFinancingScale() {
		return financingScale;
	}
	public void setFinancingScale(String financingScale) {
		this.financingScale = financingScale;
	}
	public String getFdid() {
		return fdid;
	}
	public void setFdid(String fdid) {
		this.fdid = fdid;
	}
	public String getBpName() {
		return bpName;
	}
	public void setBpName(String bpName) {
		this.bpName = bpName;
	}
	public String getBpType() {
		return bpType;
	}
	public void setBpType(String bpType) {
		this.bpType = bpType;
	}
	public String getBpCategory() {
		return bpCategory;
	}
	public void setBpCategory(String bpCategory) {
		this.bpCategory = bpCategory;
	}
	public String getBpCode() {
		return bpCode;
	}
	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}
	public String getRegisteredCapital() {
		return registeredCapital;
	}
	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}
	public String getFoundedDate() {
		return foundedDate;
	}
	public void setFoundedDate(String foundedDate) {
		this.foundedDate = foundedDate;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getEnterpriseScale() {
		return enterpriseScale;
	}
	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
	}
	public String getRegNumberType() {
		return regNumberType;
	}
	public void setRegNumberType(String regNumberType) {
		this.regNumberType = regNumberType;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}
	public String getSegmentIndustry() {
		return segmentIndustry;
	}
	public void setSegmentIndustry(String segmentIndustry) {
		this.segmentIndustry = segmentIndustry;
	}
	public String getHighPrecision() {
		return highPrecision;
	}
	public void setHighPrecision(String highPrecision) {
		this.highPrecision = highPrecision;
	}
	public String getEconomicType() {
		return economicType;
	}
	public void setEconomicType(String economicType) {
		this.economicType = economicType;
	}
	public String getEnterpriseHonor() {
		return enterpriseHonor;
	}
	public void setEnterpriseHonor(String enterpriseHonor) {
		this.enterpriseHonor = enterpriseHonor;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getOrgSubType() {
		return orgSubType;
	}
	public void setOrgSubType(String orgSubType) {
		this.orgSubType = orgSubType;
	}
	public String getParkAddress() {
		return parkAddress;
	}
	public void setParkAddress(String parkAddress) {
		this.parkAddress = parkAddress;
	}
	public String getMainProducts() {
		return mainProducts;
	}
	public void setMainProducts(String mainProducts) {
		this.mainProducts = mainProducts;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getLeaseOrganization() {
		return leaseOrganization;
	}
	public void setLeaseOrganization(String leaseOrganization) {
		this.leaseOrganization = leaseOrganization;
	}
	public String getActualLessee() {
		return actualLessee;
	}
	public void setActualLessee(String actualLessee) {
		this.actualLessee = actualLessee;
	}
	public String getScoreTemplateId() {
		return scoreTemplateId;
	}
	public void setScoreTemplateId(String scoreTemplateId) {
		this.scoreTemplateId = scoreTemplateId;
	}
	@Override
	public String toString() {
		return "NsBpMaster [fdid=" + fdid + ", bpName=" + bpName + ", bpType=" + bpType + ", bpCategory=" + bpCategory
				+ ", bpCode=" + bpCode + ", registeredCapital=" + registeredCapital + ", foundedDate=" + foundedDate
				+ ", legalPerson=" + legalPerson + ", enterpriseScale=" + enterpriseScale + ", regNumberType="
				+ regNumberType + ", regNumber=" + regNumber + ", segmentIndustry=" + segmentIndustry
				+ ", highPrecision=" + highPrecision + ", economicType=" + economicType + ", enterpriseHonor="
				+ enterpriseHonor + ", orgType=" + orgType + ", orgSubType=" + orgSubType + ", parkAddress="
				+ parkAddress + ", mainProducts=" + mainProducts + ", employeeId=" + employeeId + ", leaseOrganization="
				+ leaseOrganization + ", actualLessee=" + actualLessee + ", scoreTemplateId=" + scoreTemplateId
				+ ", marketSize=" + marketSize + ", financingScale=" + financingScale + "]";
	}
	
	
}

