package gbicc.irs.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

@Entity
@Table(name="NS_FINANCIAL_REPORT_INDEX")
public class FinancialReportIndex  extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 5646322394055388002L;


	@Id
	@GeneratedValue(generator = "system-uuid-assigned")
	@GenericGenerator(name = "system-uuid-assigned", strategy = "assigned")
	@Column(name="FD_ID", length=100)
	private String id;
	
	/**
	 * 客户编号
	 */
	@Column(name="FD_CUSTOMER_NO")
	private String customerNo;
	
	/**
	 * 报表口径
	 */
	@Column(name="FD_SPECIFICATIONS")
	private String reportSpecif;
	
	/**
	 * 报表期次
	 */
	@Column(name="FD_FINANCIAL_REPORT_DATE")
	private String reportDate;
	
	/**
	 * 行业均值 
	 */
	@Column(name="FD_INDUSTRY_AVERAGE")
	private Double industryAverage;
	
	/**
	 * 指标风险分类	
	 * 1盈利能力 2偿债能力 3运营效率 4规模性成长	 
	 */
	@Column(name="FD_INDEX_TYPE")
	private String indexType;
	
	/**
	  * 指标编码 
	 */
	@Column(name="FD_INDEX_CODE")
	private String indexCode;
	
	/**
	  * 指标名称 
	 */
	@Column(name="FD_INDEX_NAME")
	private String indexName;
	
	/**
	  * 指标值
	 */
	@Column(name="FD_INDEX_VALUE")
	private Double indexValue;

	/**
	  * 企业排名
	 */
	@Column(name="FD_CORPORATE_RANKINGS")
	private int corporateRankings;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getReportSpecif() {
		return reportSpecif;
	}

	public void setReportSpecif(String reportSpecif) {
		this.reportSpecif = reportSpecif;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public String getIndexCode() {
		return indexCode;
	}

	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public Double getIndexValue() {
		return indexValue;
	}

	public void setIndexValue(Double indexValue) {
		this.indexValue = indexValue;
	}

	public Double getIndustryAverage() {
		return industryAverage;
	}

	public void setIndustryAverage(Double industryAverage) {
		this.industryAverage = industryAverage;
	}

	public int getCorporateRankings() {
		return corporateRankings;
	}

	public void setCorporateRankings(int corporateRankings) {
		this.corporateRankings = corporateRankings;
	}
	
	
}
