package gbicc.irs.report.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 *整体借款人评级分布
 * @author xiaoxianlie
 *
 */
@Entity
@Table(name="IRS_REPORT_RATING_DISTRIBUTION")
public class ReportRatingDistribution extends AuditorEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -735729094276726282L;

	public ReportRatingDistribution() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//客户评级
	@Column(name="FD_GUEST_GR", length=200)
	private String guestGr;
	
	//客户数目
	@Column(name="FD_CUSTOMER_NUM", length=200)
	private Double customerNum;
	
	//较上月增减额
	@Column(name="FD_LAST_MONTH_MINUS", length=200)
	private Double lastMonthMinus;
		
	//比年初增减额
	@Column(name="FD_BEGIN_YEAR_MINUS", length=200)
	private Double beginYearMinus;
		
	//客户数目占比 
	@Column(name="FD_CUSTOMER_NUM_PERCENT", length=200)
	private Double customerNumPercent;
	
	//占比较上月变动
	@Column(name="FD_LAST_MONTH_CHANGE", length=200)
	private Double lastMonthChange;
	
	//占比比年初变动 
	@Column(name="FD_BEGIN_YEAR_CHANGE", length=200)
	private Double beginYearChange;



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

	public Double getLastMonthMinus() {
		return lastMonthMinus;
	}

	public void setLastMonthMinus(Double lastMonthMinus) {
		this.lastMonthMinus = lastMonthMinus;
	}

	public Double getBeginYearMinus() {
		return beginYearMinus;
	}

	public void setBeginYearMinus(Double beginYearMinus) {
		this.beginYearMinus = beginYearMinus;
	}



	public Double getRepTime() {
		return repTime;
	}

	public void setRepTime(Double repTime) {
		this.repTime = repTime;
	}
	
	//类型：1、客户数目、2、授信金额、3、业务余额
	@Column(name="FD_TYPE", length=10)
	private Integer type;

	@Transient
	private String typeName;

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return type==1?"客户数目":type==2?"授信金额":"业务余额";
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Double getCustomerNumPercent() {
		return customerNumPercent;
	}

	public void setCustomerNumPercent(Double customerNumPercent) {
		this.customerNumPercent = customerNumPercent;
	}

	public Double getLastMonthChange() {
		return lastMonthChange;
	}

	public void setLastMonthChange(Double lastMonthChange) {
		this.lastMonthChange = lastMonthChange;
	}

	public Double getBeginYearChange() {
		return beginYearChange;
	}

	public void setBeginYearChange(Double beginYearChange) {
		this.beginYearChange = beginYearChange;
	}
}

