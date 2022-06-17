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
 * ClassName: ReportLgdCompare <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月6日 上午10:01:49 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */
@Entity
@Table(name="IRS_REPORT_LGD_COMPARE")
public class ReportLgdCompare extends AuditorEntity implements Serializable{

	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = 1602469322055668431L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;

	@Column(name="FD_GUEST_GR")
	private String fdGuestGr;

	//LGD最小值
	@Column(name="FD_LGD_MIN")
	private Double fdLgdMin;
	
	//LGD中位数
	@Column(name="FD_LGD_MIDDLE")
	private Double fdLgdMiddle;
	
	//LGD最大值
	@Column(name="FD_LGD_MAX")
	private Double fdLgdMax;
	
	//LGD平均值
	@Column(name="FD_LGD_AVG")
	private Double fdLgdAvg;

	//LGD实际违约损失率
	@Column(name="FD_LGD_ILEGAL")
	private Double fdLgdIlegal;

	//EAD估计值平均
	@Column(name="FD_EAD_AVG_EXP")
	private Double fdEadAvgExp;

	//EAD实际值平均
	@Column(name="FD_EAD_AVG_ACT")
	private Double fdEadAvgAct;

	//值为：1、上半年、2、下半年
	@Column(name="FD_HALF")
	private String half;

	
	//报表时间
	@Column(name="FD_REP_TIME", length=200)
	private Double repTime;
	
	//报表时间
	@Column(name="FD_YEAR", length=200)
	private String year;
	
	public ReportLgdCompare() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFdGuestGr() {
		return fdGuestGr;
	}

	public void setFdGuestGr(String fdGuestGr) {
		this.fdGuestGr = fdGuestGr;
	}

	public Double getFdLgdMin() {
		return fdLgdMin;
	}

	public void setFdLgdMin(Double fdLgdMin) {
		this.fdLgdMin = fdLgdMin;
	}

	public Double getFdLgdMiddle() {
		return fdLgdMiddle;
	}

	public void setFdLgdMiddle(Double fdLgdMiddle) {
		this.fdLgdMiddle = fdLgdMiddle;
	}

	public Double getFdLgdMax() {
		return fdLgdMax;
	}

	public void setFdLgdMax(Double fdLgdMax) {
		this.fdLgdMax = fdLgdMax;
	}

	public Double getFdLgdAvg() {
		return fdLgdAvg;
	}

	public void setFdLgdAvg(Double fdLgdAvg) {
		this.fdLgdAvg = fdLgdAvg;
	}

	public Double getFdLgdIlegal() {
		return fdLgdIlegal;
	}

	public void setFdLgdIlegal(Double fdLgdIlegal) {
		this.fdLgdIlegal = fdLgdIlegal;
	}

	public Double getFdEadAvgExp() {
		return fdEadAvgExp;
	}

	public void setFdEadAvgExp(Double fdEadAvgExp) {
		this.fdEadAvgExp = fdEadAvgExp;
	}

	public Double getFdEadAvgAct() {
		return fdEadAvgAct;
	}

	public void setFdEadAvgAct(Double fdEadAvgAct) {
		this.fdEadAvgAct = fdEadAvgAct;
	}

	public Double getRepTime() {
		return repTime;
	}

	public void setRepTime(Double repTime) {
		this.repTime = repTime;
	}


	public String getHalf() {
		return half;
	}

	public void setHalf(String half) {
		this.half = half;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}