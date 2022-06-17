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
 * ClassName: ReportNewAnalyseKinds <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月24日 上午11:37:10 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_NEW_ANAL_KINDS")
public class ReportNewAnalyseKinds extends AuditorEntity implements Serializable{



	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -8569372325997243642L;


	public ReportNewAnalyseKinds() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//门类行业
	@Column(name="FD_NAME", length=200)
	private String name;
	
	//新增客户数目
	@Column(name="FD_CUSTOMER_NUM", length=200)
	private Double customerNum;
	
	//新增客户数目占比 
	@Column(name="FD_CUSTOMER_NUM_PERCENT", length=200)
	private Double customerNumPercent;
	
	//表内授信余额
	@Column(name="FD_GRANT_NUM", length=200)
	private Double grantNum;
	
	//违约客户数目占比 
	@Column(name="FD_GRANT_NUM_PERCENT", length=200)
	private Double grantNumPercent;
	
	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private String repTime;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCustomerNum() {
		return customerNum;
	}

	public void setCustomerNum(Double customerNum) {
		this.customerNum = customerNum;
	}

	public Double getCustomerNumPercent() {
		return customerNumPercent;
	}

	public void setCustomerNumPercent(Double customerNumPercent) {
		this.customerNumPercent = customerNumPercent;
	}

	public Double getGrantNum() {
		return grantNum;
	}

	public void setGrantNum(Double grantNum) {
		this.grantNum = grantNum;
	}

	public Double getGrantNumPercent() {
		return grantNumPercent;
	}

	public void setGrantNumPercent(Double grantNumPercent) {
		this.grantNumPercent = grantNumPercent;
	}

	public String getRepTime() {
		return repTime;
	}

	public void setRepTime(String repTime) {
		this.repTime = repTime;
	}
	
	
}

