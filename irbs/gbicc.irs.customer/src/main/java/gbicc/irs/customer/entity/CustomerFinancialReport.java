package gbicc.irs.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gbicc.irs.customer.support.CustomerFinancialReportJsonSerializer;

@Entity
@JsonSerialize(using=CustomerFinancialReportJsonSerializer.class)
@Table(name="ns_customer_financial_report")
public class CustomerFinancialReport  extends AuditorEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid-assigned")
	@GenericGenerator(name = "system-uuid-assigned", strategy = "assigned")
	@Column(name="FD_ID", length=100)
	private String id;
	
	/**
	 * 客户编号
	 */
	@Column(name="FD_CTM_NO")
	private String ctmNo;
	/**
	 * 财报类型
	 */
	@Column(name="FD_FINANCIAL_REPORT_TYPE")
	private String financialFeportType;
	
	/**
	 * 财报名称
	 */
	@Column(name="FD_FINANCIAL_REPORT")
	private String financialReport;
	
	
	/**
	 * 财报时间
	 */
	@Column(name="FD_FINANCIAL_REPORT_DT")
	private Long  financialReportDt;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCtmNo() {
		return ctmNo;
	}


	public void setCtmNo(String ctmNo) {
		this.ctmNo = ctmNo;
	}



	public String getFinancialFeportType() {
		return financialFeportType;
	}


	public void setFinancialFeportType(String financialFeportType) {
		this.financialFeportType = financialFeportType;
	}


	public String getFinancialReport() {
		return financialReport;
	}


	public void setFinancialReport(String financialReport) {
		this.financialReport = financialReport;
	}


	public Long getFinancialReportDt() {
		return financialReportDt;
	}


	public void setFinancialReportDt(Long financialReportDt) {
		this.financialReportDt = financialReportDt;
	}

	
	
}
