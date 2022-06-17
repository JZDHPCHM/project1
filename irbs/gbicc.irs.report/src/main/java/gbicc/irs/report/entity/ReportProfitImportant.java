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
 * Reason: 借款人评级与风险调整收益率总体情况 —重点行业 <br/>  
 * date: 2019年4月26日 下午4:24:10 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_PROFIT_IMPORTANT")
public class ReportProfitImportant extends AuditorEntity implements Serializable{
	

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -5534075056909064350L;

	public ReportProfitImportant() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//客户评级
	@Column(name="FD_GUEST_GR", length=200)
	private String guestGr;
	
	//房地产业_本月
	@Column(name="fd_month_rate1", length=200)
	private Double monthRate1;
	
	//房地产业_较上月变动
	@Column(name="fd_compare_month1", length=200)
	private String compareMonth1;
	
	//房地产业_较上年同期变动
	@Column(name="fd_compare_year1", length=200)
	private Double compareYear1;
	
	//建筑业_本月
	@Column(name="fd_month_rate2", length=200)
	private Double monthRate2;
	
	//建筑业_较上月变动
	@Column(name="fd_compare_month2", length=200)
	private String compareMonth2;
	
	//建筑业_较上年同期变动
	@Column(name="fd_compare_year2", length=200)
	private Double compareYear2;
	
	//批发零售业_本月
	@Column(name="fd_month_rate3", length=200)
	private Double monthRate3;
	
	//批发零售业_较上月变动
	@Column(name="fd_compare_month3", length=200)
	private String compareMonth3;
	
	//批发零售业_较上年同期变动
	@Column(name="fd_compare_year3", length=200)
	private Double compareYear3;
	
	//制造业_本月
	@Column(name="fd_month_rate4", length=200)
	private Double monthRate4;
	
	//制造业_较上月变动
	@Column(name="fd_compare_month4", length=200)
	private String compareMonth4;
	
	//制造业_较上年同期变动
	@Column(name="fd_compare_year4", length=200)
	private Double compareYear4;
	
	//租赁、商务服务业_本月
	@Column(name="fd_month_rate5", length=200)
	private Double monthRate5;
	
	//租赁、商务服务业_较上月变动
	@Column(name="fd_compare_month5", length=200)
	private String compareMonth5;
	
	//租赁、商务服务业_较上年同期变动
	@Column(name="fd_compare_year5", length=200)
	private Double compareYear5;
	
	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private Double repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private Integer year;
	
	//报表时间
	@Column(name="FD_MONTH", length=200)
	private Integer month;

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

	public Double getMonthRate1() {
		return monthRate1;
	}

	public void setMonthRate1(Double monthRate1) {
		this.monthRate1 = monthRate1;
	}

	public String getCompareMonth1() {
		return compareMonth1;
	}

	public void setCompareMonth1(String compareMonth1) {
		this.compareMonth1 = compareMonth1;
	}

	public Double getCompareYear1() {
		return compareYear1;
	}

	public void setCompareYear1(Double compareYear1) {
		this.compareYear1 = compareYear1;
	}

	public Double getMonthRate2() {
		return monthRate2;
	}

	public void setMonthRate2(Double monthRate2) {
		this.monthRate2 = monthRate2;
	}

	public String getCompareMonth2() {
		return compareMonth2;
	}

	public void setCompareMonth2(String compareMonth2) {
		this.compareMonth2 = compareMonth2;
	}

	public Double getCompareYear2() {
		return compareYear2;
	}

	public void setCompareYear2(Double compareYear2) {
		this.compareYear2 = compareYear2;
	}

	public Double getMonthRate3() {
		return monthRate3;
	}

	public void setMonthRate3(Double monthRate3) {
		this.monthRate3 = monthRate3;
	}

	public String getCompareMonth3() {
		return compareMonth3;
	}

	public void setCompareMonth3(String compareMonth3) {
		this.compareMonth3 = compareMonth3;
	}

	public Double getCompareYear3() {
		return compareYear3;
	}

	public void setCompareYear3(Double compareYear3) {
		this.compareYear3 = compareYear3;
	}

	public Double getMonthRate4() {
		return monthRate4;
	}

	public void setMonthRate4(Double monthRate4) {
		this.monthRate4 = monthRate4;
	}

	public String getCompareMonth4() {
		return compareMonth4;
	}

	public void setCompareMonth4(String compareMonth4) {
		this.compareMonth4 = compareMonth4;
	}

	public Double getCompareYear4() {
		return compareYear4;
	}

	public void setCompareYear4(Double compareYear4) {
		this.compareYear4 = compareYear4;
	}

	public Double getMonthRate5() {
		return monthRate5;
	}

	public void setMonthRate5(Double monthRate5) {
		this.monthRate5 = monthRate5;
	}

	public String getCompareMonth5() {
		return compareMonth5;
	}

	public void setCompareMonth5(String compareMonth5) {
		this.compareMonth5 = compareMonth5;
	}

	public Double getCompareYear5() {
		return compareYear5;
	}

	public void setCompareYear5(Double compareYear5) {
		this.compareYear5 = compareYear5;
	}

	public Double getRepTime() {
		return repTime;
	}

	public void setRepTime(Double repTime) {
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

