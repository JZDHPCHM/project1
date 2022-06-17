package gbicc.irs.customer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;



/**
 * 财报基本信息实体类
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_FIN_STAT")
//@JsonSerialize(using=FinancialStatementsJsonSerializer.class)
public class FinancialStatements implements Serializable{
	private static final long serialVersionUID = -3478994277338492269L;

	public FinancialStatements() {}

	
	//主键
	@Id
	@Column(name="FD_ID")
	private String finStatementId;
	
	//客户对象
	@Column(name="FD_CUST_NO")
	private String bpCode;
	
	
	//财报期数
	@Column(name="FD_REPORT_BUSS_DATE", length=10)
	private String fiscalTimes;
	
	@Column(name="FD_REPORT_SPECIFICATIONS", length=10)
	private String reportDetailType;
	
	//财报币种	
	@Column(name="FD_REPORT_CURRENCY", length=10)
	private String currencyCodeDesc;
	
	//审计单位
	@Column(name="FD_AUDIT_ORGANIZATION", length=255)
	private String auditOrganization;

	//审计意见
	@Column(name="FD_AUDIT_OPINION", length=2000)
	private String auditOpinion;
		
	//是否有效
	@Column(name="FD_VAILD")
	private String vaild;

	//是否审计
	@Column(name="FD_IS_AUDIT")
	private String isAudit;

	//财报周期
	@Column(name="FD_REPORT_CYCLE", length=10)
	private String reportCycle;
	
	//财报来源
	@Column(name="FD_REPORT_SOURCE", length=10)
	private String reportSource;
	
	//财报类型
	@Column(name="FD_REPORT_TYPE", length=2)
	private String reportType;
	
	/**
	 * 是否空表
	 */
	@Column(name="FD_IS_NULL_TABLE")
	private String  isNullTable;
	
	/**
	 * 是否平衡
	 */
	@Column(name="FD_IS_NULL_BALANCE")
	private String  isNullBalance;
	
	/**
	 * 勾稽校验通过率
	 */
	@Column(name="FD_CHECK_PASS_RATE")
	private String  checkPassRate;
	
	/**
	 * 报表状态
	 */
	@Column(name="FD_REPORT_STATE")
	private String  reportState;

	/**
	 * 财报时间
	 */
	@Column(name="FD_REPORT_YEAR")
	private Long  year;
	
	//财报科目数

	public String getFinStatementId() {
		return finStatementId;
	}

	public void setFinStatementId(String finStatementId) {
		this.finStatementId = finStatementId;
	}

	public String getAuditOrganization() {
		return auditOrganization;
	}

	public void setAuditOrganization(String auditOrganization) {
		this.auditOrganization = auditOrganization;
	}

	
	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getReportCycle() {
		return reportCycle;
	}

	public void setReportCycle(String reportCycle) {
		this.reportCycle = reportCycle;
	}

	public String getReportSource() {
		return reportSource;
	}

	public void setReportSource(String reportSource) {
		this.reportSource = reportSource;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	
	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
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

	public String getFiscalTimes() {
		return fiscalTimes;
	}

	public void setFiscalTimes(String fiscalTimes) {
		this.fiscalTimes = fiscalTimes;
	}

	public String getReportDetailType() {
		return reportDetailType;
	}

	public void setReportDetailType(String reportDetailType) {
		this.reportDetailType = reportDetailType;
	}

	public String getCurrencyCodeDesc() {
		return currencyCodeDesc;
	}

	public void setCurrencyCodeDesc(String currencyCodeDesc) {
		this.currencyCodeDesc = currencyCodeDesc;
	}

	public String getBpCode() {
		return bpCode;
	}

	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}

	public String getVaild() {
		return vaild;
	}

	public void setVaild(String vaild) {
		this.vaild = vaild;
	}

	

	
	
	
}

