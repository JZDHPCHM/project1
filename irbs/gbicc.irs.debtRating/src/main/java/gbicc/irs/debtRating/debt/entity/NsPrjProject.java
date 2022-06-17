package gbicc.irs.debtRating.debt.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
/**
 * @author hzw
 *	NS_PRJ_PROJECT 客户评级指标表
 */
@Entity
@Table(name = "NS_PRJ_PROJECT")
public class NsPrjProject implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="PROJECT_NUMBER")
	private String projectNumber;//项目编号
	@Column(name="BP_ID_TENANT")
	private String bpIdTenant;//承租人名称(id)
	@Column(name="RISK_MANAGER_NAME")
	private String riskManagerName;//评审经理
	@Column(name="DOCUMENT_TYPE")
	private String documentType;//业务类型
	@Column(name="LEASE_CHANNEL")
	private String leaseChannel;//业务类别
	@Column(name="PROJECT_NAME")
	private String projectName;//项目名称
	@Column(name="LEASE_ITEM_SHORT_NAME")
	private String leaseItemShortName;//租赁物名称（租赁标的物）
	@Column(name="EMPLOYEE_ID")
	private String employeeId;//业务经理	
	@Column(name="LEASE_ORGANIZATION")
	private String leaseOrganization;//所属部门
	@Column(name="ENTERPRISE_SCALE")
	private String enterpriseCcale;//客户规模
	@Column(name="ASSISTING_PM_A")
	private String assistingPmA;//协办经理
	@Column(name="LEASE_START_DATE")
	private String leaseStartDate;//项目投放时间
	@Column(name="FINANCE_AMOUNT")
	private String financeAmount;//租赁本金
	@Column(name="LEASE_TERM")
	private String leaseTerm;//租赁期限(年)
	@Column(name="MARGIN")
	private String margin;//保证金
	@Column(name="PRODUCT_TYPE")
	private String productType;//产品类型（厂商租赁、服务租赁等）
	@Column(name="ISCORELEASE")
	private String iscorelease;//核心租赁物（是/否）
	@Column(name="CORELEASE_PROPORTION")
	private String coreleaseProportion;//核心租赁物占比
	@Column(name="ORIGINAL_VALUE")
	private String originalValue;//租赁物原值（合计）
	@Column(name="NET_VALUE")
	private String netValue;//租赁物净值（合计）
	@Column(name="ASSESSED_VALUE")
	private String assessedValue;//评估价值
	@Column(name="ASSESSMENT_METHODS")
	private String assessmentMethods;//评估方式（收益法、市场法、成本法）顿号拼起来传
	@Column(name="CREDIT_TYPE")
	private String creditType;//增信措施类型（不动产、股权、土地权等）顿号拼起来传
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getBpIdTenant() {
		return bpIdTenant;
	}
	public void setBpIdTenant(String bpIdTenant) {
		this.bpIdTenant = bpIdTenant;
	}
	public String getRiskManagerName() {
		return riskManagerName;
	}
	public void setRiskManagerName(String riskManagerName) {
		this.riskManagerName = riskManagerName;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getLeaseChannel() {
		return leaseChannel;
	}
	public void setLeaseChannel(String leaseChannel) {
		this.leaseChannel = leaseChannel;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getLeaseItemShortName() {
		return leaseItemShortName;
	}
	public void setLeaseItemShortName(String leaseItemShortName) {
		this.leaseItemShortName = leaseItemShortName;
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
	public String getEnterpriseCcale() {
		return enterpriseCcale;
	}
	public void setEnterpriseCcale(String enterpriseCcale) {
		this.enterpriseCcale = enterpriseCcale;
	}
	public String getAssistingPmA() {
		return assistingPmA;
	}
	public void setAssistingPmA(String assistingPmA) {
		this.assistingPmA = assistingPmA;
	}
	public String getLeaseStartDate() {
		return leaseStartDate;
	}
	public void setLeaseStartDate(String leaseStartDate) {
		this.leaseStartDate = leaseStartDate;
	}
	public String getFinanceAmount() {
		return financeAmount;
	}
	public void setFinanceAmount(String financeAmount) {
		this.financeAmount = financeAmount;
	}
	public String getLeaseTerm() {
		return leaseTerm;
	}
	public void setLeaseTerm(String leaseTerm) {
		this.leaseTerm = leaseTerm;
	}
	public String getMargin() {
		return margin;
	}
	public void setMargin(String margin) {
		this.margin = margin;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getIscorelease() {
		return iscorelease;
	}
	public void setIscorelease(String iscorelease) {
		this.iscorelease = iscorelease;
	}
	public String getCoreleaseProportion() {
		return coreleaseProportion;
	}
	public void setCoreleaseProportion(String coreleaseProportion) {
		this.coreleaseProportion = coreleaseProportion;
	}
	public String getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}
	public String getNetValue() {
		return netValue;
	}
	public void setNetValue(String netValue) {
		this.netValue = netValue;
	}
	public String getAssessedValue() {
		return assessedValue;
	}
	public void setAssessedValue(String assessedValue) {
		this.assessedValue = assessedValue;
	}
	public String getAssessmentMethods() {
		return assessmentMethods;
	}
	public void setAssessmentMethods(String assessmentMethods) {
		this.assessmentMethods = assessmentMethods;
	}
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	
	@Override
	public String toString() {
		return "NsPrjProject [projectNumber=" + projectNumber + ", bpIdTenant=" + bpIdTenant + ", riskManagerName="
				+ riskManagerName + ", documentType=" + documentType + ", leaseChannel=" + leaseChannel
				+ ", projectName=" + projectName + ", leaseItemShortName=" + leaseItemShortName + ", employeeId="
				+ employeeId + ", leaseOrganization=" + leaseOrganization + ", enterpriseCcale=" + enterpriseCcale
				+ ", assistingPmA=" + assistingPmA + ", leaseStartDate=" + leaseStartDate + ", financeAmount="
				+ financeAmount + ", leaseTerm=" + leaseTerm + ", margin=" + margin + ", productType=" + productType
				+ ", iscorelease=" + iscorelease + ", coreleaseProportion=" + coreleaseProportion + ", originalValue="
				+ originalValue + ", netValue=" + netValue + ", assessedValue=" + assessedValue + ", assessmentMethods="
				+ assessmentMethods + ", creditType=" + creditType + "]";
	}

	

}

