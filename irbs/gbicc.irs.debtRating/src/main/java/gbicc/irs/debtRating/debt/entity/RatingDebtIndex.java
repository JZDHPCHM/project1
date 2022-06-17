package gbicc.irs.debtRating.debt.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import gbicc.irs.main.rating.support.RatingStepType;



@Entity
@Table(name="NS_DEBT_INDEXES")
public class RatingDebtIndex extends AuditorEntity implements Serializable {
	private static final long serialVersionUID = -6719794381826272763L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//评级步骤
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FD_STEPID")
	private RatingDebtStep ratingStep;
	
	//指标类型
	@Column(name = "FD_INDEXTYPE", length = 36)
	private String indexType;

	//指标分类
	@Column(name = "FD_INDEXCATEGORY", length = 500)
	private String indexCategory;
	
	//指标名称
	@Column(name = "FD_INDEXNAME", length = 500)
	private String indexName;
		
	//指标配置ID
	@Column(name = "FD_CONFIGID", length = 36)
	private String indexConfigId;
	
	//指标ID
	@Column(name = "FD_INDEXID", length = 36)
	private String indexId;
	
	//指标编号
	@Column(name = "FD_INDEXCODE", length = 50)
	private String indexCode;
	
	//指标值
	@Column(name = "FD_INDEXVALUE", length = 100)
	private String indexValue;

	//权重
	@Column(name = "FD_WEIGHT")
	private String indexWeight;

	//指标得分
	@Column(name = "FD_SCORE")
	private BigDecimal indexScore;

	//选项值
	@Transient
	private String options;
	
	@Column(name = "FD_QUALITATIVE_OPTIONS")
	private String quOptions;
	
	
	//层级
	@Column(name = "FD_LEVEL",length = 10)
	private String level;
	
	//父ID
	@Column(name = "FD_PARENT_ID",length = 36)
	private String parentId;
	
	//定性文本
	@Column(name = "FD_DX_TEXT",length = 2000)
	private String dxText;
	
	
	
	public String getQuOptions() {
		return quOptions;
	}

	public void setQuOptions(String quOptions) {
		this.quOptions = quOptions;
	}

	public String getDxText() {
		return dxText;
	}

	public void setDxText(String dxText) {
		this.dxText = dxText;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RatingDebtStep getRatingStep() {
		return ratingStep;
	}

	public void setRatingStep(RatingDebtStep ratingStep) {
		this.ratingStep = ratingStep;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public String getIndexCategory() {
		return indexCategory;
	}

	public void setIndexCategory(String indexCategory) {
		this.indexCategory = indexCategory;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexConfigId() {
		return indexConfigId;
	}

	public void setIndexConfigId(String indexConfigId) {
		this.indexConfigId = indexConfigId;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public String getIndexCode() {
		return indexCode;
	}

	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}

	public String getIndexValue() {
		return indexValue;
	}

	public void setIndexValue(String indexValue) {
		this.indexValue = indexValue;
	}

	public String getIndexWeight() {
		return indexWeight;
	}

	public void setIndexWeight(String indexWeight) {
		this.indexWeight = indexWeight;
	}

	public BigDecimal getIndexScore() {
		return indexScore;
	}

	public void setIndexScore(BigDecimal indexScore) {
		this.indexScore = indexScore;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

}
