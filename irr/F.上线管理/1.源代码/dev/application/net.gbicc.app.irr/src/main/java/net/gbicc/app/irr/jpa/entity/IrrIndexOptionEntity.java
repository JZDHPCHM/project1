package net.gbicc.app.irr.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 指标选项表
 */
@Entity
@Table(name="T_IRR_INDEX_OPTION")
public class IrrIndexOptionEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;
    
	/**
	 * 指标ID
	 */
	@Column(name="INDEX_ID")
	private String indexId;
	
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
	 * 选项名称
	 */
	@Column(name="OPTION_NAME")
    private String optionName;

	/**
	 * 选项备注
	 */
	@Column(name="OPTION_REMARK")
    private String optionRemark;

	/**
	 * 选项顺序
	 */
	@Column(name="OPTION_SORT")
    private BigDecimal optionSort;

	/**
	 * 选项得分
	 */
	@Column(name="OPTION_SCORE")
    private BigDecimal optionScore;

	/**
	 * 选项扣分标准
	 */
	@Column(name="OPTION_POINTS")
    private BigDecimal optionPoints;

	/**
	 * 主体编码
	 */
	@Column(name="DATA_BODY")
    private String dataBody;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionRemark() {
        return optionRemark;
    }

    public void setOptionRemark(String optionRemark) {
        this.optionRemark = optionRemark;
    }

    public BigDecimal getOptionSort() {
        return optionSort;
    }

    public void setOptionSort(BigDecimal optionSort) {
        this.optionSort = optionSort;
    }

    public BigDecimal getOptionScore() {
        return optionScore;
    }

    public void setOptionScore(BigDecimal optionScore) {
        this.optionScore = optionScore;
    }

    public BigDecimal getOptionPoints() {
        return optionPoints;
    }

    public void setOptionPoints(BigDecimal optionPoints) {
        this.optionPoints = optionPoints;
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }
}