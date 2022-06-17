package gbicc.irs.report.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 
 * ClassName: ReportRatingImportant <br/>  
 * Reason:  整体借款人评级分布—重点行业 实体类 <br/>  
 * date: 2019年4月19日 下午2:14:26 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_RATING_IMPORTANT")
public class ReportRatingImportant extends AuditorEntity implements Serializable{

	/**  
	 * serialVersionUID: uuid.  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = 6619737187109758335L;

	public ReportRatingImportant() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//客户评级
	@Column(name="FD_GUEST_GR", length=200)
	private String guestGr;
	
	//重点行业1 客户数目
	@Column(name="FD_CUSTOMER_NUM1", length=200)
	private Double customerNum1;
	
	//重点行业1 占比
	@Column(name="FD_PERCENT1", length=200)
	private String percent1;
		

	//重点行业2 客户数目
	@Column(name="FD_CUSTOMER_NUM2", length=200)
	private Double customerNum2;
	
	//重点行业2 占比
	@Column(name="FD_PERCENT2", length=200)
	private String percent2;
	
	//重点行业3 客户数目
	@Column(name="FD_CUSTOMER_NUM3", length=200)
	private Double customerNum3;
	
	//重点行业3 占比
	@Column(name="FD_PERCENT3", length=200)
	private String percent3;
	
	//重点行业4 客户数目
	@Column(name="FD_CUSTOMER_NUM4", length=200)
	private Double customerNum4;
	
	//重点行业4 占比
	@Column(name="FD_PERCENT4", length=200)
	private String percent4;
	
	//重点行业5 客户数目
	@Column(name="FD_CUSTOMER_NUM5", length=200)
	private Double customerNum5;
	
	//重点行业5 占比
	@Column(name="FD_PERCENT5", length=200)
	private String percent5;
	
	//类型：1、客户数目、2、授信金额、3、业务余额
	@Column(name="FD_TYPE", length=10)
	private Integer type;

	
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

	public Double getCustomerNum1() {
		return customerNum1;
	}

	public void setCustomerNum1(Double customerNum1) {
		this.customerNum1 = customerNum1;
	}

	public String getPercent1() {
		return percent1;
	}

	public void setPercent1(String percent1) {
		this.percent1 = percent1;
	}

	public Double getCustomerNum2() {
		return customerNum2;
	}

	public void setCustomerNum2(Double customerNum2) {
		this.customerNum2 = customerNum2;
	}

	public String getPercent2() {
		return percent2;
	}

	public void setPercent2(String percent2) {
		this.percent2 = percent2;
	}

	public Double getCustomerNum3() {
		return customerNum3;
	}

	public void setCustomerNum3(Double customerNum3) {
		this.customerNum3 = customerNum3;
	}

	public String getPercent3() {
		return percent3;
	}

	public void setPercent3(String percent3) {
		this.percent3 = percent3;
	}

	public Double getCustomerNum4() {
		return customerNum4;
	}

	public void setCustomerNum4(Double customerNum4) {
		this.customerNum4 = customerNum4;
	}

	public String getPercent4() {
		return percent4;
	}

	public void setPercent4(String percent4) {
		this.percent4 = percent4;
	}

	public Double getCustomerNum5() {
		return customerNum5;
	}

	public void setCustomerNum5(Double customerNum5) {
		this.customerNum5 = customerNum5;
	}

	public String getPercent5() {
		return percent5;
	}

	public void setPercent5(String percent5) {
		this.percent5 = percent5;
	}

	public Double getRepTime() {
		return repTime;
	}

	public void setRepTime(Double repTime) {
		this.repTime = repTime;
	}

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

	@Transient
	private String typeName;

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return type==1?"客户数目":type==2?"授信金额":"业务余额";
	}
}

