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
 * ClassName: ReportAnalyseDistribution <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: 新增客户评级集中度分布  <br/>  
 * date: 2019年4月23日 上午9:21:29 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_ANALYSE_DIS")
public class ReportAnalyseDistribution extends AuditorEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -735729094276726282L;

	public ReportAnalyseDistribution() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//客户评级
	@Column(name="FD_GUEST_GR", length=200)
	private String guestGr;
	
	//新增客户数目
	@Column(name="FD_CUSTOMER_NUM", length=200)
	private Double customerNum;
	
	// 占比 
	@Column(name="FD_CUSTOMER_NUM_PERCENT", length=200)
	private String customerNumPercent;
	
	
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

	public Double getCustomerNum() {
		return customerNum;
	}

	public void setCustomerNum(Double customerNum) {
		this.customerNum = customerNum;
	}

	public String getCustomerNumPercent() {
		return customerNumPercent;
	}

	public void setCustomerNumPercent(String customerNumPercent) {
		this.customerNumPercent = customerNumPercent;
	}

	public Double getRepTime() {
		return repTime;
	}

	public void setRepTime(Double repTime) {
		this.repTime = repTime;
	}
	
	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private Double repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private Integer year;
	
	//报表时间
	@Column(name="FD_MONTH", length=200)
	private Integer month;

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

