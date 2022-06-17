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
 * ClassName: ReportApplicationTrend <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: 评级模型推翻幅度报告 . <br/>  
 * date: 2019年4月26日 上午10:02:53 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_REFUSE_TREND")
public class ReportApplicationTrend extends AuditorEntity implements Serializable{


	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -2060495059521654664L;




	public ReportApplicationTrend() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	//分支机构名称
	@Column(name="FD_NAME", length=200)
	private String name;
	
	//该机构下的排序
	@Column(name="FD_RANGE_ORDER", length=200)
	private String rangeOrder;
	
	//本分支机构权限内 推翻次数
	@Column(name="FD_LOCAL_RUFUSE_TIMES", length=200)
	private Double localRufuseTimes;
	
	//本分支机构权限内 占比
	@Column(name="FD_LOCAL_RUFUSE_PER", length=200)
	private String localRufusePer;
	
	//本分支机构权限内 较上季变动 
	@Column(name="FD_LOCAL_RUFUSE_COMP_Q", length=200)
	private Double localRufuseCompQ;
	
	//本分支机构权限内 比年初变动
	@Column(name="FD_LOCAL_RUFUSE_COMP_Y", length=200)
	private Double localRufuseCompY;
	
	//总行权限内 推翻次数
	@Column(name="FD_UPPER_RUFUSE_TIMES", length=200)
	private Double upperRufuseTimes;
	
	//总行权限内 占比
	@Column(name="FD_UPPER_RUFUSE_PER", length=200)
	private Double upperRufusePer;
	
	//总行权限内 较上季变动
	@Column(name="FD_UPPER_RUFUSE_COMP_Q", length=200)
	private Double upperRufuseCompQ;
	
	//总行权限内 比年初变动
	@Column(name="FD_UPPER_RUFUSE_COMP_Y", length=200)
	private Double upperRufuseCompY;
	
	//推翻次数合计
	@Column(name="FD_SUM_REFUSE_PER", length=200)
	private Double sumRufusePer;

	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private String repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private Integer year;
	
	//季度：值为：1、2 、3 、4
	@Column(name="FD_QUARTER", length=200)
	private Integer quarter;




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

	public String getRangeOrder() {
		return rangeOrder;
	}

	public void setRangeOrder(String rangeOrder) {
		this.rangeOrder = rangeOrder;
	}

	public Double getLocalRufuseTimes() {
		return localRufuseTimes;
	}

	public void setLocalRufuseTimes(Double localRufuseTimes) {
		this.localRufuseTimes = localRufuseTimes;
	}

	public String getLocalRufusePer() {
		return localRufusePer;
	}

	public void setLocalRufusePer(String localRufusePer) {
		this.localRufusePer = localRufusePer;
	}

	public Double getLocalRufuseCompQ() {
		return localRufuseCompQ;
	}

	public void setLocalRufuseCompQ(Double localRufuseCompQ) {
		this.localRufuseCompQ = localRufuseCompQ;
	}

	public Double getLocalRufuseCompY() {
		return localRufuseCompY;
	}

	public void setLocalRufuseCompY(Double localRufuseCompY) {
		this.localRufuseCompY = localRufuseCompY;
	}

	public Double getUpperRufuseTimes() {
		return upperRufuseTimes;
	}

	public void setUpperRufuseTimes(Double upperRufuseTimes) {
		this.upperRufuseTimes = upperRufuseTimes;
	}

	public Double getUpperRufusePer() {
		return upperRufusePer;
	}

	public void setUpperRufusePer(Double upperRufusePer) {
		this.upperRufusePer = upperRufusePer;
	}

	public Double getUpperRufuseCompQ() {
		return upperRufuseCompQ;
	}

	public void setUpperRufuseCompQ(Double upperRufuseCompQ) {
		this.upperRufuseCompQ = upperRufuseCompQ;
	}

	public Double getUpperRufuseCompY() {
		return upperRufuseCompY;
	}

	public void setUpperRufuseCompY(Double upperRufuseCompY) {
		this.upperRufuseCompY = upperRufuseCompY;
	}

	public Double getSumRufusePer() {
		return sumRufusePer;
	}

	public void setSumRufusePer(Double sumRufusePer) {
		this.sumRufusePer = sumRufusePer;
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

