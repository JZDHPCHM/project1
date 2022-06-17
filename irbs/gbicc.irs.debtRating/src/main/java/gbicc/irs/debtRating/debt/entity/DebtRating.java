package gbicc.irs.debtRating.debt.entity;

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
@Table(name="NS_DEBT_RATING ")
public class DebtRating {

	private static final long serialVersionUID = -3456809335174136601L;

	public DebtRating() {}
	 
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	@Column(name = "FD_CUST_CODE")
	private String custCode;
	@Column(name = "FD_CUST_NAME")
	private String custName;
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
	 * @现金流保障被倍数
	 */
	@Column(name = "FD_XJL_SCO")
	private String xjlSco;
	/**
	 * @租赁物保障倍数
	 */
	@Column(name = "FD_ZLW_SCO")
	private String zlwSco;
	
	/**
	 * @增信措施保障倍数
	 */
	@Column(name = "FD_ZXCS_SCO")
	private String zxcsSco;
	
	/**
	 * @客户信用保障倍数
	 */
	@Column(name = "FD_KHXY_SCO")
	private String khxySco;
	
	
	@Column(name = "FD_PROJECT_CODE")
	private String projectCode;
	
	@Column(name = "FD_PROJECT_NAME")
	private String projectName;
	
	
	/**
	 * 总得分
	 */
	@Column(name = "FD_SCO")
	private String sco;
	
	@Column(name = "FD_INTERN_CODE")
	private String internCode;
	@Column(name = "FD_INTERN_NAME")
	private String internName;
	
	@CreatedDate
	@Column(name="FD_INTERN_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date internDate;
	@Column(name = "FD_INTERN_LEVEL")
	private String internLevel;
	@Column(name = "FD_INTERN_BS")
	private String internBs;
	@Column(name = "FD_FINAL_CODE")
	private String finalCode;
	@Column(name = "FD_FINAL_NAME")
	private String finalName;
	@Column(name = "FD_FINAL_LEVEL")
	private String finalLevel;
	@Column(name = "FD_FINAL_BS")
	private String finalBs;
	
	@Column(name = "FD_FINAL_ADVICE")
	private String finalAdvice;
	
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
	private List<RatingDebtStep> steps = new ArrayList<RatingDebtStep>();
	@Column(name = "FD_RATING_TYPE")
	private String ratingType;
	
	@Column(name = "FD_VERSION")
	private String fdVersion;
	
	@Column(name = "FD_RATING_VAILD")
	private String ratingVaild;
	
	@Column(name = "FD_PRO_ID")
	private String proId;
	
	@Column(name = "FD_ASSET_REVIEW")
	private String assetReview;
	
	
	// 评级当前步骤
	@Transient
	private RatingDebtStep currentStep;

	
	
	
	public String getAssetReview() {
		return assetReview;
	}

	public void setAssetReview(String assetReview) {
		this.assetReview = assetReview;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
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

	public List<RatingDebtStep> getSteps() {
		return steps;
	}

	public void setSteps(List<RatingDebtStep> steps) {
		this.steps = steps;
	}

	public RatingDebtStep getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(RatingDebtStep currentStep) {
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

	
	public String getXjlSco() {
		return xjlSco;
	}

	public void setXjlSco(String xjlSco) {
		this.xjlSco = xjlSco;
	}

	public String getZlwSco() {
		return zlwSco;
	}

	public void setZlwSco(String zlwSco) {
		this.zlwSco = zlwSco;
	}

	public String getZxcsSco() {
		return zxcsSco;
	}

	public void setZxcsSco(String zxcsSco) {
		this.zxcsSco = zxcsSco;
	}

	public String getKhxySco() {
		return khxySco;
	}

	public void setKhxySco(String khxySco) {
		this.khxySco = khxySco;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public String getInternBs() {
		return internBs;
	}

	public void setInternBs(String internBs) {
		this.internBs = internBs;
	}

	public String getFinalBs() {
		return finalBs;
	}

	public void setFinalBs(String finalBs) {
		this.finalBs = finalBs;
	}

	
}
