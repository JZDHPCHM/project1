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
 * ClassName: ReportAnalyseTrend <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: 整体客户评级趋势分布 . <br/>  
 * date: 2019年5月5日 上午10:04:47 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
/**  
 * ClassName: ReportAnalyseTrend <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月31日 上午9:58:06 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8  
 */
@Entity
@Table(name="IRS_REPORT_ANALYSE_TREND")
public class ReportAnalyseTrend extends AuditorEntity implements Serializable{



	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -6780991320937976388L;

	public ReportAnalyseTrend() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;

	//客户评级
	@Column(name="FD_GUEST_GR", length=200)
	private String guestGr;
	
	//授信余额
	@Column(name="FD_GRANT_NUM", length=200)
	private Double grantNum;
	
	//授信余额较上季末变动度
	@Column(name="FD_COMP_QUARTER_GRANT_NUM", length=200)
	private Double compQuarterGrantNum;
	
	//授信余额较去年同季度变动度
	@Column(name="FD_COMP_YEAR_GRANT_NUM", length=200)
	private Double compYearGrantNum;
	
	//授信余额占比 
	@Column(name="FD_GRANT_PER", length=200)
	private String grantPer;
	
	//授信余额占比较上季末变动度
	@Column(name="FD_COMP_QUARTER_GRANT_PER", length=200)
	private String compQuarterGrantPer;
	
	//授信余额占比较去年同季度变动度
	@Column(name="FD_COMP_YEAR_GRANT_PER", length=200)
	private String compYearGrantPer;

	
	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private Double repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private Integer year;
	
	//报表时间
	@Column(name="FD_QUARTER", length=200)
	private String quarter;

	
	//类型：1表内授信余额, 2表外授信余额
	@Column(name="FD_TYPE", length=10)
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Double getGrantNum() {
		return grantNum;
	}

	public void setGrantNum(Double grantNum) {
		this.grantNum = grantNum;
	}


	public Double getCompQuarterGrantNum() {
		return compQuarterGrantNum;
	}

	public void setCompQuarterGrantNum(Double compQuarterGrantNum) {
		this.compQuarterGrantNum = compQuarterGrantNum;
	}

	public Double getCompYearGrantNum() {
		return compYearGrantNum;
	}

	public void setCompYearGrantNum(Double compYearGrantNum) {
		this.compYearGrantNum = compYearGrantNum;
	}

	public String getGrantPer() {
		return grantPer;
	}

	public void setGrantPer(String grantPer) {
		this.grantPer = grantPer;
	}

	public String getCompQuarterGrantPer() {
		return compQuarterGrantPer;
	}

	public void setCompQuarterGrantPer(String compQuarterGrantPer) {
		this.compQuarterGrantPer = compQuarterGrantPer;
	}

	public String getCompYearGrantPer() {
		return compYearGrantPer;
	}

	public void setCompYearGrantPer(String compYearGrantPer) {
		this.compYearGrantPer = compYearGrantPer;
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

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
}

