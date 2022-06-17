package gbicc.irs.debtRating.debt.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.wsp.framework.jpa.entity.AuditorEntity;

@Entity
@Table(name="NS_FACILITY_RATING ")
public class FacilityRating extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = -3456809335174136601L;

	public FacilityRating() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	//承租人编号
	@Column(name = "FD_CUST_CODE")
	private String custCode;
	//承租人名称
	@Column(name = "FD_CUST_NAME")
	private String custName;
	//项目编号
	@Column(name = "FD_PROJECT_CODE")
	private String projectCode;
	//项目名称
	@Column(name = "FD_PROJECT_NAME")
	private String projectName;
	//产品类型--模型类型
	@Column(name = "PRODUCT_TYPE")
	private String type;
	
	//产品类型--模型类型
	@Column(name = "ASSETS_RATING_ID")
	private String assetsId;
	
	//产品类型--模型类型
	@Column(name = "MAIN_RATING_ID")
	private String mainId;
	//产品类型--模型类型
	@Column(name = "CREDIT_RATING_ID")
	private String creditId;	
	
	//初评人（发起人）编码
	@Column(name = "FD_INTERN_CODE")
	private String internCode;
	
	//初评人（发起人）编码
	@Column(name = "FD_INTERN_NAME")
	private String internName;
	
	//初评（发起）日期
	@CreatedDate
	@Column(name="FD_INTERN_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date internDate;
	
	
	//评级资产得分
	@Column(name = "FD_INTERN_SCO")
	private BigDecimal internSco;
	
	//初评评级等级
	@Column(name = "FD_INTERN_LEVEL")
	private String internLevel;
	
	//资产经理编码
	@Column(name = "FD_ASSETS_CODE")
	private String assetsCode;
	
	//资产经理名称
	@Column(name = "FD_ASSETS_NAME")
	private String assetsName;
	
	//复核人(评审员)编码
	@Column(name = "FD_FINAL_CODE")
	private String finalCode;
	
	//复核人(评审员)名称
	@Column(name = "FD_FINAL_NAME")
	private String finalName;
	
	//评级日期(终评日期)
	@Column(name="FD_FINAL_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date finalDate;
	
	//评级到期日
	@Column(name="FD_DATE", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fdDate;
	
	//复评资产得分
	@Column(name = "FD_FINAL_SCO")
	private BigDecimal finalSco;
	
	//复评评级等级
	@Column(name = "FD_FINAL_LEVEL")
	private String finalLevel;
	//复核人复合意见
	@Column(name = "FD_FINAL_ADVICE")
	private String finalAdvice;
	
	//评级状态--000测算,010初评,020复评
	@Column(name = "FD_RATING_STATUS")
	private String ratingStatus;
	//评级状态（有效评级，过期评级等）
	@Column(name = "FD_RATING_TYPE")
	private String ratingType;
	
	
	
	public String getAssetsId() {
		return assetsId;
	}

	public void setAssetsId(String assetsId) {
		this.assetsId = assetsId;
	}

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	// 生效状态
	@Column(name = "FD_VAILD")
	private String vaild;
	
	//评级生效状态
	@Column(name = "FD_RATING_VAILD")
	private String ratingVaild;

	
	@Column(name = "FD_VERSION")
	private String fdVersion;
	

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


	public BigDecimal getInternSco() {
		return internSco;
	}

	public void setInternSco(BigDecimal internSco) {
		this.internSco = internSco;
	}

	public String getAssetsCode() {
		return assetsCode;
	}

	public void setAssetsCode(String assetsCode) {
		this.assetsCode = assetsCode;
	}

	public String getAssetsName() {
		return assetsName;
	}

	public void setAssetsName(String assetsName) {
		this.assetsName = assetsName;
	}

	public Date getFdDate() {
		return fdDate;
	}

	public void setFdDate(Date fdDate) {
		this.fdDate = fdDate;
	}


	public BigDecimal getFinalSco() {
		return finalSco;
	}

	public void setFinalSco(BigDecimal finalSco) {
		this.finalSco = finalSco;
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

	public String getVaild() {
		return vaild;
	}

	public void setVaild(String vaild) {
		this.vaild = vaild;
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

	@Override
	public String toString() {
		return "FacilityRating [id=" + id + ", custCode=" + custCode + ", custName=" + custName + ", projectCode="
				+ projectCode + ", projectName=" + projectName + ", type=" + type + ", assetsId=" + assetsId
				+ ", mainId=" + mainId + ", creditId=" + creditId + ", internCode=" + internCode + ", internName="
				+ internName + ", internDate=" + internDate + ", internSco=" + internSco + ", internLevel="
				+ internLevel + ", assetsCode=" + assetsCode + ", assetsName=" + assetsName + ", finalCode=" + finalCode
				+ ", finalName=" + finalName + ", finalDate=" + finalDate + ", fdDate=" + fdDate + ", finalSco="
				+ finalSco + ", finalLevel=" + finalLevel + ", finalAdvice=" + finalAdvice + ", ratingStatus="
				+ ratingStatus + ", ratingType=" + ratingType + ", vaild=" + vaild + ", ratingVaild=" + ratingVaild
				+ ", fdVersion=" + fdVersion + "]";
	}

	
}
