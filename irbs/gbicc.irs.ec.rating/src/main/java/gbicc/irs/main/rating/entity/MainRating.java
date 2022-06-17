package gbicc.irs.main.rating.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;
import org.springframework.data.annotation.CreatedDate;



@Entity
@Table(name="NS_MAIN_RATING ")
public class MainRating {

	private static final long serialVersionUID = -3456809335174136601L;

	public MainRating() {}
	 
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	@Column(name = "FD_CUST_CODE")
	private String custCode;
	@Column(name = "FD_CUST_NAME")
	private String custName;
	//评级类型--模型类型
	@Column(name = "FD_TYPE")
	private String type;
	@Column(name = "FD_PD")
	private String pd;
	/**
	 * 第一年财报
	 */
	@Column(name = "FD_FIR_REP")
	private String firRep;
	/**
	 * 第二年财报
	 */
	@Column(name = "FD_SEC_REP")
	private String secRep;
	
	/**
	 * 第三年财报
	 */
	@Column(name = "FD_THI_REP")
	private String thiRep;
	
	/**
	 * 评审经理意见
	 */
	@Column(name = "REVIEWEROPINION")
	private String reviewerOpinion;
	
	/**
	 * 评主体评级类型/赛道类型（市值型，增速型，利润型）
	 */
	@Column(name = "TRACK_TYPE")
	private String trackType;
	
	/**
	 * 定量得分
	 */
	@Column(name = "FD_QUAN_SCO")
	private String quanSco;
	/**
	 * 定性得分
	 */
	@Column(name = "FD_QUAL_SCO")
	private String qualSco;
	/**
	 * 总得分
	 */
	@Column(name = "FD_SCO")
	private String sco;
	
	@Column(name = "FD_INITSCO")
	private String initSco;
	
	//业务经理编码
	@Column(name = "FD_INTERN_CODE")
	private String internCode;
	//业务经理名称
	@Column(name = "FD_INTERN_NAME")
	private String internName;
	
	@CreatedDate
	@Column(name="FD_INTERN_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date internDate;
	@Column(name = "FD_INTERN_LEVEL")
	private String internLevel;
	//复合人编码
	@Column(name = "FD_FINAL_CODE")
	private String finalCode;
	//复合人名称
	@Column(name = "FD_FINAL_NAME")
	private String finalName;
	@Column(name = "FD_FINAL_LEVEL")
	private String finalLevel;
	//复核人复合意见
	@Column(name = "FD_FINAL_ADVICE")
	private String finalAdvice;
	//评级日期(终评日期)
	@Column(name="FD_FINAL_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date finalDate;
	@Column(name = "FD_RATING_STATUS")
	private String ratingStatus;
	// 评级当前步骤Id
	@Column(name = "FD_CURRENT_STEPID")
	private String currentStepId;
	// 评级是否生效
	@Column(name = "FD_VAILD")
	@org.hibernate.annotations.Type(type = "numeric_boolean")
	private Boolean vaild;
	@Column(name = "FD_CREATOR")
	private String creator;
	@Column(name = "FD_CREATE_DATE")
	private Date createDate;
	@Column(name = "FD_LAST_MODIFIER")
	private String lastModifier;
	@Column(name = "FD_LAST_MODIFYDATE")
	private Date lastModifydate;
	@OneToMany(mappedBy = "ratingMain")
	@OrderBy(clause = "sno")
	private List<RatingMainStep> steps = new ArrayList<RatingMainStep>();
	@Column(name = "FD_RATING_TYPE")
	private String ratingType;
	
	@Column(name = "FD_VERSION")
	private String fdVersion;
	
	@Column(name = "FD_RATING_VAILD")
	private String ratingVaild;
	
	
	@Column(name = "f_date")
	private String fDate;
	
	@Column(name = "treat_n")
	private String treatN;
	
	@Column(name = "ACTUAL_RATE_SUBJECT_ID")
	private String actualRateSubjectId;
	
	
	
	public String getActualRateSubjectId() {
		return actualRateSubjectId;
	}

	public void setActualRateSubjectId(String actualRateSubjectId) {
		this.actualRateSubjectId = actualRateSubjectId;
	}

	public String getThiRep() {
		return thiRep;
	}

	public void setThiRep(String thiRep) {
		this.thiRep = thiRep;
	}

	public String getReviewerOpinion() {
		return reviewerOpinion;
	}

	public void setReviewerOpinion(String reviewerOpinion) {
		this.reviewerOpinion = reviewerOpinion;
	}

	public String getTrackType() {
		return trackType;
	}

	public void setTrackType(String trackType) {
		this.trackType = trackType;
	}

	public String getfDate() {
		return fDate;
	}

	public void setfDate(String fDate) {
		this.fDate = fDate;
	}

	public String getTreatN() {
		return treatN;
	}

	public void setTreatN(String treatN) {
		this.treatN = treatN;
	}


	// 评级当前步骤
	@Transient
	private RatingMainStep currentStep;

	
	
	

	public String getInitSco() {
		return initSco;
	}

	public void setInitSco(String initSco) {
		this.initSco = initSco;
	}

	public String getRatingVaild() {
		return ratingVaild;
	}

	public void setRatingVaild(String ratingVaild) {
		this.ratingVaild = ratingVaild;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getVaild() {
		return vaild;
	}

	public void setVaild(Boolean vaild) {
		this.vaild = vaild;
	}

	public List<RatingMainStep> getSteps() {
		return steps;
	}

	public void setSteps(List<RatingMainStep> steps) {
		this.steps = steps;
	}

	public RatingMainStep getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(RatingMainStep currentStep) {
		this.currentStep = currentStep;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPd() {
		return pd;
	}

	public void setPd(String pd) {
		this.pd = pd;
	}
	

	public String getFirRep() {
		return firRep;
	}

	public void setFirRep(String firRep) {
		this.firRep = firRep;
	}

	public String getSecRep() {
		return secRep;
	}

	public void setSecRep(String secRep) {
		this.secRep = secRep;
	}

	public String getQuanSco() {
		return quanSco;
	}

	public void setQuanSco(String quanSco) {
		this.quanSco = quanSco;
	}

	public String getQualSco() {
		return qualSco;
	}

	public void setQualSco(String qualSco) {
		this.qualSco = qualSco;
	}

	public String getSco() {
		return sco;
	}

	public void setSco(String sco) {
		this.sco = sco;
	}

	public String getInternCode() {
		return internCode;
	}

	public void setInternCode(String internCode) {
		this.internCode = internCode;
	}

	public String getInternName() {
		return internName;
	}

	public void setInternName(String internName) {
		this.internName = internName;
	}

	public Date getInternDate() {
		return internDate;
	}

	public void setInternDate(Date internDate) {
		this.internDate = internDate;
	}

	public String getInternLevel() {
		return internLevel;
	}

	public void setInternLevel(String internLevel) {
		this.internLevel = internLevel;
	}

	public String getFinalCode() {
		return finalCode;
	}

	public void setFinalCode(String finalCode) {
		this.finalCode = finalCode;
	}

	public String getFinalName() {
		return finalName;
	}

	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}

	public String getFinalLevel() {
		return finalLevel;
	}

	public void setFinalLevel(String finalLevel) {
		this.finalLevel = finalLevel;
	}

	public String getFinalAdvice() {
		return finalAdvice;
	}

	public void setFinalAdvice(String finalAdvice) {
		this.finalAdvice = finalAdvice;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	public String getRatingStatus() {
		return ratingStatus;
	}

	public void setRatingStatus(String ratingStatus) {
		this.ratingStatus = ratingStatus;
	}

	public String getCurrentStepId() {
		return currentStepId;
	}

	public void setCurrentStepId(String currentStepId) {
		this.currentStepId = currentStepId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Date getLastModifydate() {
		return lastModifydate;
	}

	public void setLastModifydate(Date lastModifydate) {
		this.lastModifydate = lastModifydate;
	}

	public String getRatingType() {
		return ratingType;
	}

	public void setRatingType(String ratingType) {
		this.ratingType = ratingType;
	}

	public String getFdVersion() {
		return fdVersion;
	}

	public void setFdVersion(String fdVersion) {
		this.fdVersion = fdVersion;
	}

	
}
