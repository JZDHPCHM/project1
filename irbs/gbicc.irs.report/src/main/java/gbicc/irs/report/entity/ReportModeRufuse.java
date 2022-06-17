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
 * ClassName: ReportModeRufuse <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年5月7日 下午5:17:21 <br/>  
 *  
 * @author xiaoxianlie
 * @version   
 * @since JDK 1.8
 */

@Entity
@Table(name="IRS_REPORT_MODE_RUFUSE")
public class ReportModeRufuse extends AuditorEntity implements Serializable{


	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @since JDK 1.8  
	 */
	private static final long serialVersionUID = -6628637110970010051L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="ID", length=36)
	private String id;

	/**
	 * 模型类别
	 */
	@Column(name="FD_GUEST_GR")
	private String fdGuestGr;

	/**
    * 客户数目
    */
	@Column(name="FD_CUSTOM_NUM")
    private Double fdCustomNum;

    /**
    * 评级认定次数
    */
	@Column(name="FD_JUDGE_TIMES")
    private Double fdJudgeTimes;

    /**
    *  评级推翻次数
    */
	@Column(name="FD_JUDGE_REFUSE_TIMES")
    private Double fdJudgeRefuseTimes;

    /**
    * 评级上调占比 一级
    */
	@Column(name="FD_UP_ONE_PER")
    private Double fdUpOnePer;

    /**
    *  评级上调占比 二级
    */
	@Column(name="FD_UP_TWO_PER")
    private Double fdUpTwoPer;
	
	
    /**
    * 评级上调占比 二级以上
    */
	@Column(name="FD_UP_GREAT_TWO_PER")
    private Double fdUpGreatTwoPer;

    /**
    *  评级上调占比 合计
    */
	@Column(name="FD_UP_TOATAL_PER")
    private Double fdUpToatalPer;

    /**
    * 评级下调占比 一级
    */
	@Column(name="FD_DOWN_ONE_PER")
    private Double fdDownOnePer;

    /**
    *  评级下调占比 二级
    */
	@Column(name="FD_DOWN_TWO_PER")
    private Double fdDownTwoPer;
	
    /**
    *  评级下调占比 三级
    */
	@Column(name="FD_DOWN_THREE_PER")
    private Double fdDownThreePer;
	
	
    /**
    *  评级下调占比 三级以上
    */
	@Column(name="FD_DOWN_LESS_THREE_PER")
    private Double fdDownLessThreePer;
	
	
    /**
    *  评级下调占比 合计
    */
	@Column(name="FD_DOWN_TOATAL_PER")
    private Double fdDownToatalPer;
	
    /**
    *  评级推翻率
    */
	@Column(name="FD_JUDGE_REFUSE_PER")
    private Double fdJudgeRefusePer;

    /**
    *报表时间
    */
	@Column(name="FD_REP_TIME")
    private String repTime;

    /**
    * 报表时间
    */
	@Column(name="FD_YEAR")
    private Integer year;

    /**
    * 季度：值为：1、2 、3 、4
    */
	@Column(name="FD_QUARTER")
    private Integer quarter;


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

	public Double getFdCustomNum() {
		return fdCustomNum;
	}

	public void setFdCustomNum(Double fdCustomNum) {
		this.fdCustomNum = fdCustomNum;
	}

	public Double getFdJudgeTimes() {
		return fdJudgeTimes;
	}

	public void setFdJudgeTimes(Double fdJudgeTimes) {
		this.fdJudgeTimes = fdJudgeTimes;
	}

	public Double getFdJudgeRefuseTimes() {
		return fdJudgeRefuseTimes;
	}

	public void setFdJudgeRefuseTimes(Double fdJudgeRefuseTimes) {
		this.fdJudgeRefuseTimes = fdJudgeRefuseTimes;
	}

	public Double getFdUpOnePer() {
		return fdUpOnePer;
	}

	public void setFdUpOnePer(Double fdUpOnePer) {
		this.fdUpOnePer = fdUpOnePer;
	}

	public Double getFdUpTwoPer() {
		return fdUpTwoPer;
	}

	public void setFdUpTwoPer(Double fdUpTwoPer) {
		this.fdUpTwoPer = fdUpTwoPer;
	}

	public Double getFdUpGreatTwoPer() {
		return fdUpGreatTwoPer;
	}

	public void setFdUpGreatTwoPer(Double fdUpGreatTwoPer) {
		this.fdUpGreatTwoPer = fdUpGreatTwoPer;
	}

	public Double getFdUpToatalPer() {
		return fdUpToatalPer;
	}

	public void setFdUpToatalPer(Double fdUpToatalPer) {
		this.fdUpToatalPer = fdUpToatalPer;
	}

	public Double getFdDownOnePer() {
		return fdDownOnePer;
	}

	public void setFdDownOnePer(Double fdDownOnePer) {
		this.fdDownOnePer = fdDownOnePer;
	}

	public Double getFdDownTwoPer() {
		return fdDownTwoPer;
	}

	public void setFdDownTwoPer(Double fdDownTwoPer) {
		this.fdDownTwoPer = fdDownTwoPer;
	}

	public Double getFdDownThreePer() {
		return fdDownThreePer;
	}

	public void setFdDownThreePer(Double fdDownThreePer) {
		this.fdDownThreePer = fdDownThreePer;
	}

	public Double getFdDownLessThreePer() {
		return fdDownLessThreePer;
	}

	public void setFdDownLessThreePer(Double fdDownLessThreePer) {
		this.fdDownLessThreePer = fdDownLessThreePer;
	}

	public Double getFdDownToatalPer() {
		return fdDownToatalPer;
	}

	public void setFdDownToatalPer(Double fdDownToatalPer) {
		this.fdDownToatalPer = fdDownToatalPer;
	}

	public Double getFdJudgeRefusePer() {
		return fdJudgeRefusePer;
	}

	public void setFdJudgeRefusePer(Double fdJudgeRefusePer) {
		this.fdJudgeRefusePer = fdJudgeRefusePer;
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