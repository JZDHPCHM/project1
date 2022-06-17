package gbicc.irs.esb.service.request;

import java.util.ArrayList;
import java.util.List;


/**
 * 客户财报信息
 * @author ljc
 *
 */
public class RatingFinancialReportRequest {
	
	public RatingFinancialReportRequest() {}
	
	public RatingFinancialReportRequest(
			String reportBussDate,
			String year,
			String reportSpecifications,
			String reportType) {}
	
	// 信贷客户编号
	private String companyCustomer;	
	
	// 财报期数
	private String reportBussDate;	

	// 财报口径
	private String reportSpecifications;
	
	// 财报币种
	private String reportCurrency;	
	
	// 审计单位(审计单位名称)
	private String auditOrganization;
	
	// 审计意见
	private String auditOpinion;	
	
	// 是否有效
	private String vaild;	
	
	// 是否审计
	private String isAudit;	
	
	// 财报周期
	private String reportCycle;	
	
	// 财报类型
	private String reportType;	
	
	// 是否空表
	private String isNullTable;	
	
	// 是否平衡
	private String isNullBalance;	
	
	// 勾稽校验通过率
	private String checkPassRate;	
	
	// 报表状态
	private String reportState;	
	
	// 财报时间
	private String year;	
	
	// 科目财报
	private List<RatingFinancialSubjectRequest> financialSubjectDatas = new ArrayList<RatingFinancialSubjectRequest>();

	public String getCompanyCustomer() {
		return companyCustomer;
	}

	public void setCompanyCustomer(String companyCustomer) {
		this.companyCustomer = companyCustomer;
	}

	public String getReportBussDate() {
		return reportBussDate;
	}

	public void setReportBussDate(String reportBussDate) {
		this.reportBussDate = reportBussDate;
	}

	public String getReportSpecifications() {
		return reportSpecifications;
	}

	public void setReportSpecifications(String reportSpecifications) {
		this.reportSpecifications = reportSpecifications;
	}

	public String getReportCurrency() {
		return reportCurrency;
	}

	public void setReportCurrency(String reportCurrency) {
		this.reportCurrency = reportCurrency;
	}

	public String getAuditOrganization() {
		return auditOrganization;
	}

	public void setAuditOrganization(String auditOrganization) {
		this.auditOrganization = auditOrganization;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getVaild() {
		return vaild;
	}

	public void setVaild(String vaild) {
		this.vaild = vaild;
	}

	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	public String getReportCycle() {
		return reportCycle;
	}

	public void setReportCycle(String reportCycle) {
		this.reportCycle = reportCycle;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getIsNullTable() {
		return isNullTable;
	}

	public void setIsNullTable(String isNullTable) {
		this.isNullTable = isNullTable;
	}

	public String getIsNullBalance() {
		return isNullBalance;
	}

	public void setIsNullBalance(String isNullBalance) {
		this.isNullBalance = isNullBalance;
	}

	public String getCheckPassRate() {
		return checkPassRate;
	}

	public void setCheckPassRate(String checkPassRate) {
		this.checkPassRate = checkPassRate;
	}

	public String getReportState() {
		return reportState;
	}

	public void setReportState(String reportState) {
		this.reportState = reportState;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<RatingFinancialSubjectRequest> getfinancialSubjectDatas() {
		return financialSubjectDatas;
	}

	public void setfinancialSubjectDatas(List<RatingFinancialSubjectRequest> financialSubjectDatas) {
		this.financialSubjectDatas = financialSubjectDatas;
	}
	
	

}
