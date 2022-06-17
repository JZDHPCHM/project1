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
 * ClassName: ReportSingleLimit <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月6日 上午11:10:57 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_SINGLE_MANAGE")
public class ReportSingleManage extends AuditorEntity implements Serializable{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = 2213948225722867138L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	

	@Column(name="FD_KIND_NAME")
	private String fdKindName;

	/**
    * 客户数目
    */
	@Column(name="FD_CUSTOM_NUM")
    private Double fdCustomNum;

    /**
    * 授信额度
    */
	@Column(name="FD_GRANT_NUM")
    private Double fdGrantNum;

    /**
    * 单一客户限额合计
    */
	@Column(name="FD_SINGLE_LIMIT_NUM")
    private Double fdSingleLimitNum;

    /**
    * 平均限额占用率
    */
	@Column(name="FD_AVG_LIMIT_USE")
    private Double fdAvgLimitUse;

    /**
    * 平均限额占用率
    */
	@Column(name="FD_OVER_LIMIT_PER")
    private Double fdOverLimitPer;

    /**
    *报表时间
    */
	@Column(name="FD_REP_TIME")
    private String repTime;

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


	public Double getFdCustomNum() {
		return fdCustomNum;
	}

	public void setFdCustomNum(Double fdCustomNum) {
		this.fdCustomNum = fdCustomNum;
	}

	public Double getFdGrantNum() {
		return fdGrantNum;
	}

	public void setFdGrantNum(Double fdGrantNum) {
		this.fdGrantNum = fdGrantNum;
	}

	public Double getFdSingleLimitNum() {
		return fdSingleLimitNum;
	}

	public void setFdSingleLimitNum(Double fdSingleLimitNum) {
		this.fdSingleLimitNum = fdSingleLimitNum;
	}

	public Double getFdAvgLimitUse() {
		return fdAvgLimitUse;
	}

	public void setFdAvgLimitUse(Double fdAvgLimitUse) {
		this.fdAvgLimitUse = fdAvgLimitUse;
	}

	public Double getFdOverLimitPer() {
		return fdOverLimitPer;
	}

	public void setFdOverLimitPer(Double fdOverLimitPer) {
		this.fdOverLimitPer = fdOverLimitPer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFdKindName() {
		return fdKindName;
	}

	public void setFdKindName(String fdKindName) {
		this.fdKindName = fdKindName;
	}

	public String getRepTime() {
		return repTime;
	}

	public void setRepTime(String repTime) {
		this.repTime = repTime;
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


}