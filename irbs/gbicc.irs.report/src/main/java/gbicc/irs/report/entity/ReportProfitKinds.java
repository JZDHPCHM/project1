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
 * Reason: 借款人评级与风险调整收益率总体情况 —门类行业<br/>  
 * date: 2019年4月26日 下午4:24:10 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_PROFIT_KINDS")
public class ReportProfitKinds extends AuditorEntity implements Serializable{
	
	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = 5658740230482855672L;

	public ReportProfitKinds() {}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;
	
	
	@Column(name="FD_A_COMPARE_MONTH")
	private Double fdACompareMonth;

	@Column(name="FD_A_COMPARE_YEAR")
	private Double fdACompareYear;

	@Column(name="FD_A_M_COMPARE_MONTH")
	private Double fdAMCompareMonth;

	@Column(name="FD_A_M_COMPARE_YEAR")
	private Double fdAMCompareYear;

	@Column(name="FD_A_M_MONTH_RATE")
	private Double fdAMMonthRate;

	@Column(name="FD_A_MONTH_RATE")
	private Double fdAMonthRate;

	@Column(name="FD_A_P_COMPARE_MONTH")
	private Double fdAPCompareMonth;

	@Column(name="FD_A_P_COMPARE_YEAR")
	private Double fdAPCompareYear;

	@Column(name="FD_A_P_MONTH_RATE")
	private Double fdAPMonthRate;

	@Column(name="FD_AA_COMPARE_MONTH")
	private Double fdAaCompareMonth;

	@Column(name="FD_AA_COMPARE_YEAR")
	private Double fdAaCompareYear;

	@Column(name="FD_AA_M_COMPARE_MONTH")
	private Double fdAaMCompareMonth;

	@Column(name="FD_AA_M_COMPARE_YEAR")
	private Double fdAaMCompareYear;

	@Column(name="FD_AA_M_MONTH_RATE")
	private Double fdAaMMonthRate;

	@Column(name="FD_AA_MONTH_RATE")
	private Double fdAaMonthRate;

	@Column(name="FD_AA_P_COMPARE_MONTH")
	private Double fdAaPCompareMonth;

	@Column(name="FD_AA_P_COMPARE_YEAR")
	private Double fdAaPCompareYear;

	@Column(name="FD_AA_P_MONTH_RATE")
	private Double fdAaPMonthRate;

	@Column(name="FD_AAA_COMPARE_MONTH")
	private Double fdAaaCompareMonth;

	@Column(name="FD_AAA_COMPARE_YEAR")
	private Double fdAaaCompareYear;

	@Column(name="FD_AAA_MONTH_RATE")
	private Double fdAaaMonthRate;

	@Column(name="FD_B_COMPARE_MONTH")
	private Double fdBCompareMonth;

	@Column(name="FD_B_COMPARE_YEAR")
	private Double fdBCompareYear;

	@Column(name="FD_B_MONTH_RATE")
	private Double fdBMonthRate;

	@Column(name="FD_BB_COMPARE_MONTH")
	private Double fdBbCompareMonth;

	@Column(name="FD_BB_COMPARE_YEAR")
	private Double fdBbCompareYear;

	@Column(name="FD_BB_M_COMPARE_MONTH")
	private Double fdBbMCompareMonth;

	@Column(name="FD_BB_M_COMPARE_YEAR")
	private Double fdBbMCompareYear;

	@Column(name="FD_BB_M_MONTH_RATE")
	private Double fdBbMMonthRate;

	@Column(name="FD_BB_MONTH_RATE")
	private Double fdBbMonthRate;

	@Column(name="FD_BB_P_COMPARE_MONTH")
	private Double fdBbPCompareMonth;

	@Column(name="FD_BB_P_COMPARE_YEAR")
	private Double fdBbPCompareYear;

	@Column(name="FD_BB_P_MONTH_RATE")
	private Double fdBbPMonthRate;

	@Column(name="FD_BBB_COMPARE_MONTH")
	private Double fdBbbCompareMonth;

	@Column(name="FD_BBB_COMPARE_YEAR")
	private Double fdBbbCompareYear;

	@Column(name="FD_BBB_M_COMPARE_MONTH")
	private Double fdBbbMCompareMonth;

	@Column(name="FD_BBB_M_COMPARE_YEAR")
	private Double fdBbbMCompareYear;

	@Column(name="FD_BBB_M_MONTH_RATE")
	private Double fdBbbMMonthRate;

	@Column(name="FD_BBB_MONTH_RATE")
	private Double fdBbbMonthRate;

	@Column(name="FD_BBB_P_COMPARE_MONTH")
	private Double fdBbbPCompareMonth;

	@Column(name="FD_BBB_P_COMPARE_YEAR")
	private Double fdBbbPCompareYear;

	@Column(name="FD_BBB_P_MONTH_RATE")
	private Double fdBbbPMonthRate;

	@Column(name="FD_C_COMPARE_MONTH")
	private Double fdCCompareMonth;

	@Column(name="FD_C_COMPARE_YEAR")
	private Double fdCCompareYear;

	@Column(name="FD_C_MONTH_RATE")
	private Double fdCMonthRate;

	@Column(name="FD_D_COMPARE_MONTH")
	private Double fdDCompareMonth;

	@Column(name="FD_D_COMPARE_YEAR")
	private Double fdDCompareYear;

	@Column(name="FD_D_MONTH_RATE")
	private Double fdDMonthRate;

	@Column(name="FD_KIND_NAME")
	private String fdKindName;
	

	
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

	public Double getFdACompareMonth() {
		return fdACompareMonth;
	}

	public void setFdACompareMonth(Double fdACompareMonth) {
		this.fdACompareMonth = fdACompareMonth;
	}

	public Double getFdACompareYear() {
		return fdACompareYear;
	}

	public void setFdACompareYear(Double fdACompareYear) {
		this.fdACompareYear = fdACompareYear;
	}

	public Double getFdAMCompareMonth() {
		return fdAMCompareMonth;
	}

	public void setFdAMCompareMonth(Double fdAMCompareMonth) {
		this.fdAMCompareMonth = fdAMCompareMonth;
	}

	public Double getFdAMCompareYear() {
		return fdAMCompareYear;
	}

	public void setFdAMCompareYear(Double fdAMCompareYear) {
		this.fdAMCompareYear = fdAMCompareYear;
	}

	public Double getFdAMMonthRate() {
		return fdAMMonthRate;
	}

	public void setFdAMMonthRate(Double fdAMMonthRate) {
		this.fdAMMonthRate = fdAMMonthRate;
	}

	public Double getFdAMonthRate() {
		return fdAMonthRate;
	}

	public void setFdAMonthRate(Double fdAMonthRate) {
		this.fdAMonthRate = fdAMonthRate;
	}

	public Double getFdAPCompareMonth() {
		return fdAPCompareMonth;
	}

	public void setFdAPCompareMonth(Double fdAPCompareMonth) {
		this.fdAPCompareMonth = fdAPCompareMonth;
	}

	public Double getFdAPCompareYear() {
		return fdAPCompareYear;
	}

	public void setFdAPCompareYear(Double fdAPCompareYear) {
		this.fdAPCompareYear = fdAPCompareYear;
	}

	public Double getFdAPMonthRate() {
		return fdAPMonthRate;
	}

	public void setFdAPMonthRate(Double fdAPMonthRate) {
		this.fdAPMonthRate = fdAPMonthRate;
	}

	public Double getFdAaCompareMonth() {
		return fdAaCompareMonth;
	}

	public void setFdAaCompareMonth(Double fdAaCompareMonth) {
		this.fdAaCompareMonth = fdAaCompareMonth;
	}

	public Double getFdAaCompareYear() {
		return fdAaCompareYear;
	}

	public void setFdAaCompareYear(Double fdAaCompareYear) {
		this.fdAaCompareYear = fdAaCompareYear;
	}

	public Double getFdAaMCompareMonth() {
		return fdAaMCompareMonth;
	}

	public void setFdAaMCompareMonth(Double fdAaMCompareMonth) {
		this.fdAaMCompareMonth = fdAaMCompareMonth;
	}

	public Double getFdAaMCompareYear() {
		return fdAaMCompareYear;
	}

	public void setFdAaMCompareYear(Double fdAaMCompareYear) {
		this.fdAaMCompareYear = fdAaMCompareYear;
	}

	public Double getFdAaMMonthRate() {
		return fdAaMMonthRate;
	}

	public void setFdAaMMonthRate(Double fdAaMMonthRate) {
		this.fdAaMMonthRate = fdAaMMonthRate;
	}

	public Double getFdAaMonthRate() {
		return fdAaMonthRate;
	}

	public void setFdAaMonthRate(Double fdAaMonthRate) {
		this.fdAaMonthRate = fdAaMonthRate;
	}

	public Double getFdAaPCompareMonth() {
		return fdAaPCompareMonth;
	}

	public void setFdAaPCompareMonth(Double fdAaPCompareMonth) {
		this.fdAaPCompareMonth = fdAaPCompareMonth;
	}

	public Double getFdAaPCompareYear() {
		return fdAaPCompareYear;
	}

	public void setFdAaPCompareYear(Double fdAaPCompareYear) {
		this.fdAaPCompareYear = fdAaPCompareYear;
	}

	public Double getFdAaPMonthRate() {
		return fdAaPMonthRate;
	}

	public void setFdAaPMonthRate(Double fdAaPMonthRate) {
		this.fdAaPMonthRate = fdAaPMonthRate;
	}

	public Double getFdAaaCompareMonth() {
		return fdAaaCompareMonth;
	}

	public void setFdAaaCompareMonth(Double fdAaaCompareMonth) {
		this.fdAaaCompareMonth = fdAaaCompareMonth;
	}

	public Double getFdAaaCompareYear() {
		return fdAaaCompareYear;
	}

	public void setFdAaaCompareYear(Double fdAaaCompareYear) {
		this.fdAaaCompareYear = fdAaaCompareYear;
	}

	public Double getFdAaaMonthRate() {
		return fdAaaMonthRate;
	}

	public void setFdAaaMonthRate(Double fdAaaMonthRate) {
		this.fdAaaMonthRate = fdAaaMonthRate;
	}

	public Double getFdBCompareMonth() {
		return fdBCompareMonth;
	}

	public void setFdBCompareMonth(Double fdBCompareMonth) {
		this.fdBCompareMonth = fdBCompareMonth;
	}

	public Double getFdBCompareYear() {
		return fdBCompareYear;
	}

	public void setFdBCompareYear(Double fdBCompareYear) {
		this.fdBCompareYear = fdBCompareYear;
	}

	public Double getFdBMonthRate() {
		return fdBMonthRate;
	}

	public void setFdBMonthRate(Double fdBMonthRate) {
		this.fdBMonthRate = fdBMonthRate;
	}

	public Double getFdBbCompareMonth() {
		return fdBbCompareMonth;
	}

	public void setFdBbCompareMonth(Double fdBbCompareMonth) {
		this.fdBbCompareMonth = fdBbCompareMonth;
	}

	public Double getFdBbCompareYear() {
		return fdBbCompareYear;
	}

	public void setFdBbCompareYear(Double fdBbCompareYear) {
		this.fdBbCompareYear = fdBbCompareYear;
	}

	public Double getFdBbMCompareMonth() {
		return fdBbMCompareMonth;
	}

	public void setFdBbMCompareMonth(Double fdBbMCompareMonth) {
		this.fdBbMCompareMonth = fdBbMCompareMonth;
	}

	public Double getFdBbMCompareYear() {
		return fdBbMCompareYear;
	}

	public void setFdBbMCompareYear(Double fdBbMCompareYear) {
		this.fdBbMCompareYear = fdBbMCompareYear;
	}

	public Double getFdBbMMonthRate() {
		return fdBbMMonthRate;
	}

	public void setFdBbMMonthRate(Double fdBbMMonthRate) {
		this.fdBbMMonthRate = fdBbMMonthRate;
	}

	public Double getFdBbMonthRate() {
		return fdBbMonthRate;
	}

	public void setFdBbMonthRate(Double fdBbMonthRate) {
		this.fdBbMonthRate = fdBbMonthRate;
	}

	public Double getFdBbPCompareMonth() {
		return fdBbPCompareMonth;
	}

	public void setFdBbPCompareMonth(Double fdBbPCompareMonth) {
		this.fdBbPCompareMonth = fdBbPCompareMonth;
	}

	public Double getFdBbPCompareYear() {
		return fdBbPCompareYear;
	}

	public void setFdBbPCompareYear(Double fdBbPCompareYear) {
		this.fdBbPCompareYear = fdBbPCompareYear;
	}

	public Double getFdBbPMonthRate() {
		return fdBbPMonthRate;
	}

	public void setFdBbPMonthRate(Double fdBbPMonthRate) {
		this.fdBbPMonthRate = fdBbPMonthRate;
	}

	public Double getFdBbbCompareMonth() {
		return fdBbbCompareMonth;
	}

	public void setFdBbbCompareMonth(Double fdBbbCompareMonth) {
		this.fdBbbCompareMonth = fdBbbCompareMonth;
	}

	public Double getFdBbbCompareYear() {
		return fdBbbCompareYear;
	}

	public void setFdBbbCompareYear(Double fdBbbCompareYear) {
		this.fdBbbCompareYear = fdBbbCompareYear;
	}

	public Double getFdBbbMCompareMonth() {
		return fdBbbMCompareMonth;
	}

	public void setFdBbbMCompareMonth(Double fdBbbMCompareMonth) {
		this.fdBbbMCompareMonth = fdBbbMCompareMonth;
	}

	public Double getFdBbbMCompareYear() {
		return fdBbbMCompareYear;
	}

	public void setFdBbbMCompareYear(Double fdBbbMCompareYear) {
		this.fdBbbMCompareYear = fdBbbMCompareYear;
	}

	public Double getFdBbbMMonthRate() {
		return fdBbbMMonthRate;
	}

	public void setFdBbbMMonthRate(Double fdBbbMMonthRate) {
		this.fdBbbMMonthRate = fdBbbMMonthRate;
	}

	public Double getFdBbbMonthRate() {
		return fdBbbMonthRate;
	}

	public void setFdBbbMonthRate(Double fdBbbMonthRate) {
		this.fdBbbMonthRate = fdBbbMonthRate;
	}

	public Double getFdBbbPCompareMonth() {
		return fdBbbPCompareMonth;
	}

	public void setFdBbbPCompareMonth(Double fdBbbPCompareMonth) {
		this.fdBbbPCompareMonth = fdBbbPCompareMonth;
	}

	public Double getFdBbbPCompareYear() {
		return fdBbbPCompareYear;
	}

	public void setFdBbbPCompareYear(Double fdBbbPCompareYear) {
		this.fdBbbPCompareYear = fdBbbPCompareYear;
	}

	public Double getFdBbbPMonthRate() {
		return fdBbbPMonthRate;
	}

	public void setFdBbbPMonthRate(Double fdBbbPMonthRate) {
		this.fdBbbPMonthRate = fdBbbPMonthRate;
	}

	public Double getFdCCompareMonth() {
		return fdCCompareMonth;
	}

	public void setFdCCompareMonth(Double fdCCompareMonth) {
		this.fdCCompareMonth = fdCCompareMonth;
	}

	public Double getFdCCompareYear() {
		return fdCCompareYear;
	}

	public void setFdCCompareYear(Double fdCCompareYear) {
		this.fdCCompareYear = fdCCompareYear;
	}

	public Double getFdCMonthRate() {
		return fdCMonthRate;
	}

	public void setFdCMonthRate(Double fdCMonthRate) {
		this.fdCMonthRate = fdCMonthRate;
	}

	public Double getFdDCompareMonth() {
		return fdDCompareMonth;
	}

	public void setFdDCompareMonth(Double fdDCompareMonth) {
		this.fdDCompareMonth = fdDCompareMonth;
	}

	public Double getFdDCompareYear() {
		return fdDCompareYear;
	}

	public void setFdDCompareYear(Double fdDCompareYear) {
		this.fdDCompareYear = fdDCompareYear;
	}

	public Double getFdDMonthRate() {
		return fdDMonthRate;
	}

	public void setFdDMonthRate(Double fdDMonthRate) {
		this.fdDMonthRate = fdDMonthRate;
	}

	public String getFdKindName() {
		return fdKindName;
	}

	public void setFdKindName(String fdKindName) {
		this.fdKindName = fdKindName;
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

