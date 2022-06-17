package net.gbicc.app.irr.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 指标信息表
 */
@Entity
@Table(name="T_IRR_INDEX_INFO")
public class IrrIndexInfoEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;
	
	/**
	 * 行次
	 */
	@Column(name="INDEX_LINE")
	private String indexLine;

	/**
	 * 指标编码
	 */
	@Column(name="INDEX_CODE")
    private String indexCode;

	/**
	 * 指标名称
	 */
	@Column(name="INDEX_NAME")
    private String indexName;

	/**
	 * 评估项目ID
	 */
	@Column(name="PROJ_TYPE_ID")
    private String projTypeId;

	/**
	 * 评估项目名称
	 */
	@Column(name="PROJ_TYPE_NAME")
    private String projTypeName;

	/**
	 * 指标结果类型
	 */
	@Column(name="INDEX_RESULT_TYPE")
    private String indexResultType;

	/**
	 * 指标分值
	 */
	@Column(name="INDEX_VALUE")
    private BigDecimal indexValue;

	/**
	 * 指标说明
	 */
	@Column(name="INDEX_DESC")
    private String indexDesc;

	/**
	 * 评价标准
	 */
	@Column(name="EVAL_CRIT")
    private String evalCrit;

	/**
	 * 评分类型
	 */
	@Column(name="SCOR_TYPE")
    private String scorType;

	/**
	 * XBRL指标名称
	 */
	@Column(name="XBRL_INDEX_NAME")
    private String xbrlIndexName;

	/**
	 * XBRL指标元素
	 */
	@Column(name="XBRL_INDEX_ELEMENT")
    private String xbrlIndexElement;

	/**
	 * 采集方式
	 */
	@Column(name="COLLECTION_WAY")
    private String collectionWay;

	/**
	 * 指标单位
	 */
	@Column(name="INDEX_UNIT")
    private String indexUnit;

	/**
	 * 指标公式
	 */
	@Column(name="INDEX_FORMULA")
    private String indexFormula;

	/**
	 * 指标公式编码
	 */
	@Column(name="INDEX_FORMULA_CODE")
    private String indexFormulaCode;

	/**
	 * 指标可计算公式
	 */
	@Column(name="INDEX_EVAL_FORM")
    private String indexEvalForm;

	/**
	 * 指标层级
	 */
	@Column(name="INDEX_LEVEL")
    private String indexLevel;
	
	/**
	 * 绩效层级
	 */
	@Column(name="PFM_LEVEL")
	private String pfmLevel;

	/**
	 * 指标状态
	 */
	@Column(name="INDEX_STATUS")
    private String indexStatus;

	/**
	 * 小数位数
	 */
	@Column(name="DECIMAL_PLACE")
    private BigDecimal decimalPlace;

	/**
	 * 是否得分指标
	 */
	@Column(name="IS_SCORE_INDEX")
    private String isScoreIndex;

	/**
	 * 是否上报Y/N
	 */
	@Column(name="IS_XBRL")
    private String isXbrl;

	/**
	 * 是否适用
	 */
	@Column(name="IS_APPLICABIE")
    private String isApplicabie;
	
	/**
	 * 是否绩效指标
	 */
	@Column(name="IS_PFM")
	private String isPfm;

	/**
	 * 绩效描述
	 */
	@Column(name="PFM_DESC")
    private String pfmDesc;

	/**
	 * 绩效公式
	 */
	@Column(name="PFM_FORMULA")
    private String pfmFormula;

	/**
	 * 绩效公式编码
	 */
	@Column(name="PFM_FORMULA_CODE")
    private String pfmFormulaCode;

	/**
	 * 绩效可计算公式
	 */
	@Column(name="PFM_EVAL_FORM")
    private String pfmEvalForm;

	/**
	 * 主体编码
	 */
	@Column(name="DATA_BODY")
    private String dataBody;
	
	/**
	 * 拆分个数
	 */
	@Transient
	private Double splitNum;

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexLine() {
		return indexLine;
	}

	public void setIndexLine(String indexLine) {
		this.indexLine = indexLine;
	}

	public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getProjTypeId() {
        return projTypeId;
    }

    public void setProjTypeId(String projTypeId) {
        this.projTypeId = projTypeId;
    }

    public String getProjTypeName() {
        return projTypeName;
    }

    public void setProjTypeName(String projTypeName) {
        this.projTypeName = projTypeName;
    }

    public String getIndexResultType() {
        return indexResultType;
    }

    public void setIndexResultType(String indexResultType) {
        this.indexResultType = indexResultType;
    }

    public BigDecimal getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(BigDecimal indexValue) {
        this.indexValue = indexValue;
    }

    public String getIndexDesc() {
        return indexDesc;
    }

    public void setIndexDesc(String indexDesc) {
        this.indexDesc = indexDesc;
    }

    public String getEvalCrit() {
        return evalCrit;
    }

    public void setEvalCrit(String evalCrit) {
        this.evalCrit = evalCrit;
    }

    public String getScorType() {
        return scorType;
    }

    public void setScorType(String scorType) {
        this.scorType = scorType;
    }

    public String getXbrlIndexName() {
        return xbrlIndexName;
    }

    public void setXbrlIndexName(String xbrlIndexName) {
        this.xbrlIndexName = xbrlIndexName;
    }

    public String getXbrlIndexElement() {
        return xbrlIndexElement;
    }

    public void setXbrlIndexElement(String xbrlIndexElement) {
        this.xbrlIndexElement = xbrlIndexElement;
    }

    public String getCollectionWay() {
        return collectionWay;
    }

    public void setCollectionWay(String collectionWay) {
        this.collectionWay = collectionWay;
    }

    public String getIndexUnit() {
        return indexUnit;
    }

    public void setIndexUnit(String indexUnit) {
        this.indexUnit = indexUnit;
    }

    public String getIndexFormula() {
        return indexFormula;
    }

    public void setIndexFormula(String indexFormula) {
        this.indexFormula = indexFormula;
    }

    public String getIndexFormulaCode() {
        return indexFormulaCode;
    }

    public void setIndexFormulaCode(String indexFormulaCode) {
        this.indexFormulaCode = indexFormulaCode;
    }

    public String getIndexEvalForm() {
        return indexEvalForm;
    }

    public void setIndexEvalForm(String indexEvalForm) {
        this.indexEvalForm = indexEvalForm;
    }

    public String getIndexLevel() {
        return indexLevel;
    }

    public void setIndexLevel(String indexLevel) {
        this.indexLevel = indexLevel;
    }

    public String getIndexStatus() {
        return indexStatus;
    }

    public void setIndexStatus(String indexStatus) {
        this.indexStatus = indexStatus;
    }

    public BigDecimal getDecimalPlace() {
        return decimalPlace;
    }

    public void setDecimalPlace(BigDecimal decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public String getIsScoreIndex() {
        return isScoreIndex;
    }

    public void setIsScoreIndex(String isScoreIndex) {
        this.isScoreIndex = isScoreIndex;
    }

    public String getIsXbrl() {
        return isXbrl;
    }

    public void setIsXbrl(String isXbrl) {
        this.isXbrl = isXbrl;
    }

    public String getIsApplicabie() {
        return isApplicabie;
    }

    public void setIsApplicabie(String isApplicabie) {
        this.isApplicabie = isApplicabie;
    }

    public String getPfmDesc() {
        return pfmDesc;
    }

    public void setPfmDesc(String pfmDesc) {
        this.pfmDesc = pfmDesc;
    }

    public String getPfmFormula() {
        return pfmFormula;
    }

    public void setPfmFormula(String pfmFormula) {
        this.pfmFormula = pfmFormula;
    }

    public String getPfmFormulaCode() {
        return pfmFormulaCode;
    }

    public void setPfmFormulaCode(String pfmFormulaCode) {
        this.pfmFormulaCode = pfmFormulaCode;
    }

    public String getPfmEvalForm() {
        return pfmEvalForm;
    }

    public void setPfmEvalForm(String pfmEvalForm) {
        this.pfmEvalForm = pfmEvalForm;
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }

	public Double getSplitNum() {
		return splitNum;
	}

	public void setSplitNum(Double splitNum) {
		this.splitNum = splitNum;
	}

	public String getPfmLevel() {
		return pfmLevel;
	}

	public void setPfmLevel(String pfmLevel) {
		this.pfmLevel = pfmLevel;
	}

	public String getIsPfm() {
		return isPfm;
	}

	public void setIsPfm(String isPfm) {
		this.isPfm = isPfm;
	}
    
}