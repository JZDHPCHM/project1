package gbicc.irs.assetsRating.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gbicc.irs.assetsRating.support.RatingAssetsStepJsonSerializer;
import gbicc.irs.main.rating.support.RatingStepType;

/**
 * 对公客户评级步骤实体
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_ASSETS_STEP")
@JsonSerialize(using=RatingAssetsStepJsonSerializer.class)
public class RatingAssetsStep extends AuditorEntity implements Serializable {

	private static final long serialVersionUID = 5996709246396242639L;
	
	public RatingAssetsStep() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//评级对象
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="FD_RATEID")
	private AssetsRating ratingMain;
	
	//步骤序号
	@Column(name="FD_SNO")
	private Integer sno;
	
	//步骤名称
	@Column(name="FD_STEP_NAME",length=50)
	private String stepName;
	
	//步骤名称
	@Column(name="FD_STEP_RESOURCES",length=500)
	private String stepResources;
	
	//上一步骤
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FD_LAST_STEP")
	private RatingAssetsStep lastStep;
	
	//下一步骤
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FD_NEXT_STEP")
	private RatingAssetsStep nextStep;
	
	//步骤类型
	@Column(name="FD_STEP_TYPE",length=50)
	@Enumerated(EnumType.STRING)
	private RatingStepType stepType;
	
	//模型ID
	@Column(name="FD_MODEL_ID",length=36)
	private String modelId;

	@Column(name="FD_VAILD",length=36)
	private String vaild;
	
	
	//评级指标
	@OneToMany(mappedBy="ratingStep")
	private List<AssetsIndex> stepIndexs = new ArrayList<AssetsIndex>();
	
	//步骤展示Html代码
	@Transient
	private String showHtml;
	
	/*//表单项
	@Transient
	private List<WarpFormField> formfields = new ArrayList<WarpFormField>();
	*/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AssetsRating getRatingMain() {
		return ratingMain;
	}

	public void setRatingMain(AssetsRating ratingMain) {
		this.ratingMain = ratingMain;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	
	

	public String getVaild() {
		return vaild;
	}

	public void setVaild(String vaild) {
		this.vaild = vaild;
	}

	public String getStepResources() {
		return stepResources;
	}

	public void setStepResources(String stepResources) {
		this.stepResources = stepResources;
	}

	public RatingAssetsStep getLastStep() {
		return lastStep;
	}

	public void setLastStep(RatingAssetsStep lastStep) {
		this.lastStep = lastStep;
	}

	public RatingAssetsStep getNextStep() {
		return nextStep;
	}

	public void setNextStep(RatingAssetsStep nextStep) {
		this.nextStep = nextStep;
	}

	public RatingStepType getStepType() {
		return stepType;
	}

	public void setStepType(RatingStepType stepType) {
		this.stepType = stepType;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public List<AssetsIndex> getStepIndexs() {
		return stepIndexs;
	}

	public void setStepIndexs(List<AssetsIndex> stepIndexs) {
		this.stepIndexs = stepIndexs;
	}

	public String getShowHtml() {
		return showHtml;
	}

	public void setShowHtml(String showHtml) {
		this.showHtml = showHtml;
	}

/*	public List<WarpFormField> getFormfields() {
		return formfields;
	}

	public void setFormfields(List<WarpFormField> formfields) {
		this.formfields = formfields;
	}*/
	
}
