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
 * ClassName: ReportProfitAll <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: 借款人评级与风险调整收益率总体情况  <br/>  
 * date: 2019年4月26日 下午4:24:10 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_PROFIT_ALL")
public class ReportProfitAll extends AuditorEntity implements Serializable{
	

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -5330704413492481952L;

	public ReportProfitAll() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//客户评级
	@Column(name="FD_GUEST_GR", length=200)
	private String guestGr;
	
	//本月
	@Column(name="FD_MONTH_RATE", length=200)
	private Double monthRate;
	
	//上月
	@Column(name="FD_LAST_MONTH_RATE", length=200)
	private Double lastMonthRate;
	
	//上年同期
	@Column(name="FD_LAST_YEAR_RATE", length=200)
	private Double lastYearRate;
	
	//较上月变动
	@Column(name="FD_COMPARE_MONTH_RATE", length=200)
	private Double compareMonthRate;
	
	//较上年同期变动
	@Column(name="FD_COMPARE_YEAR_RATE", length=200)
	private Double compareYearRate;
	
	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private String repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private Integer year;
	
	//报表时间
	@Column(name="FD_MONTH", length=200)
	private Integer month;


	public Double getMonthRate() {
		return monthRate;
	}

	public void setMonthRate(Double monthRate) {
		this.monthRate = monthRate;
	}

	public Double getLastMonthRate() {
		return lastMonthRate;
	}

	public void setLastMonthRate(Double lastMonthRate) {
		this.lastMonthRate = lastMonthRate;
	}

	public Double getLastYearRate() {
		return lastYearRate;
	}

	public void setLastYearRate(Double lastYearRate) {
		this.lastYearRate = lastYearRate;
	}

	public Double getCompareMonthRate() {
		return compareMonthRate;
	}

	public void setCompareMonthRate(Double compareMonthRate) {
		this.compareMonthRate = compareMonthRate;
	}

	public Double getCompareYearRate() {
		return compareYearRate;
	}

	public void setCompareYearRate(Double compareYearRate) {
		this.compareYearRate = compareYearRate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuestGr() {
		return guestGr;
	}

	public void setGuestGr(String guestGr) {
		this.guestGr = guestGr;
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

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	
	
}

