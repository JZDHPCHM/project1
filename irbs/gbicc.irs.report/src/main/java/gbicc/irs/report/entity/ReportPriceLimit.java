package gbicc.irs.report.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;


/**
 * 
 * ClassName: ReportPriceLimit <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月7日 上午9:59:06 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_PRICE_LIMIT")
public class ReportPriceLimit extends AuditorEntity implements Serializable{


	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -5506606976509459642L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//客户评级
	@Column(name="FD_GUEST_GR")
	private String fdGuestGr;

	/**
    * 总债项数目
    */
	@Column(name="FD_LOAN_SUM")
    private Double fdLoanSum;

    /**
    * 超定价债项数目
    */
	@Column(name="FD_OVER_LOAN_SUM")
    private Double fdOverLoanSum;

    /**
    * 超定价幅度 0%~10%（含）
    */
	@Column(name="FD_LIMIT_10")
    private Double fdLimit10;

    /**
    *  超定价幅度 10%~30%（含）
    */
	@Column(name="FD_LIMIT_30")
    private Double fdLimit30;

    /**
    * 超定价幅度 30%~50%（含）
    */
	@Column(name="FD_LIMIT_50")
    private Double fdLimit50;

    /**
    *  超定价幅度 50%以上
    */
	@Column(name="FD_LIMIT_GREAT_50")
    private Double fdLimitGreat50;
	
    /**
    *  总体超定价幅度
    */
	@Column(name="FD_OVER_LIMIT_SUM")
    private Double fdOverLimitSum;

    /**
    * 报表时间
    */
	@Column(name="FD_YEAR")
    private Integer year;

    /**
    * 季度：值为：1、2 、3 、4
    */
	@Column(name="FD_QUARTER")
    private Integer quarter;

	//类型：机构名称
	@Column(name="FD_TYPE")
    private Integer type;


	public Double getFdLoanSum() {
		return fdLoanSum;
	}

	public void setFdLoanSum(Double fdLoanSum) {
		this.fdLoanSum = fdLoanSum;
	}

	public Double getFdOverLoanSum() {
		return fdOverLoanSum;
	}

	public void setFdOverLoanSum(Double fdOverLoanSum) {
		this.fdOverLoanSum = fdOverLoanSum;
	}

	public Double getFdLimit10() {
		return fdLimit10;
	}

	public void setFdLimit10(Double fdLimit10) {
		this.fdLimit10 = fdLimit10;
	}

	public Double getFdLimit30() {
		return fdLimit30;
	}

	public void setFdLimit30(Double fdLimit30) {
		this.fdLimit30 = fdLimit30;
	}

	public Double getFdLimit50() {
		return fdLimit50;
	}

	public void setFdLimit50(Double fdLimit50) {
		this.fdLimit50 = fdLimit50;
	}

	public Double getFdLimitGreat50() {
		return fdLimitGreat50;
	}

	public void setFdLimitGreat50(Double fdLimitGreat50) {
		this.fdLimitGreat50 = fdLimitGreat50;
	}

	public Double getFdOverLimitSum() {
		return fdOverLimitSum;
	}

	public void setFdOverLimitSum(Double fdOverLimitSum) {
		this.fdOverLimitSum = fdOverLimitSum;
	}


	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getQuarter() {
		return quarter;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFdGuestGr() {
		return fdGuestGr;
	}

	public void setFdGuestGr(String fdGuestGr) {
		this.fdGuestGr = fdGuestGr;
	}
	
	
}