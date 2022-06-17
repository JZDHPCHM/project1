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
 * ClassName: ReportAnalyseKinds <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年4月23日 下午2:21:03 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_ANALYSE_KINDS")
public class ReportAnalyseKinds extends AuditorEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -735729094276726282L;

	public ReportAnalyseKinds() {}

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
	private String customerNumPercent;
	
	//表内授信余额
	@Column(name="FD_GRANT_NUM", length=200)
	private Double grantNum;
	
	//违约客户数目占比 
	@Column(name="FD_GRANT_NUM_PERCENT", length=200)
	private String grantNumPercent;
	
	
	public Double getGrantNum() {
		return grantNum;
	}

	public void setGrantNum(Double grantNum) {
		this.grantNum = grantNum;
	}

	public String getGrantNumPercent() {
		return grantNumPercent;
	}

	public void setGrantNumPercent(String grantNumPercent) {
		this.grantNumPercent = grantNumPercent;
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

