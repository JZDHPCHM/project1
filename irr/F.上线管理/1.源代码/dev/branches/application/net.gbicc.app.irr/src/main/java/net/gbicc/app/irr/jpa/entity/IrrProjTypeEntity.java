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
 * 评估项目表
 */
@Entity
@Table(name="T_IRR_PROJ_TYPE")
public class IrrProjTypeEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;

	/**
	 * 类型编码
	 */
	@Column(name="TYPE_CODE")
    private String typeCode;

	/**
	 * 类型名称
	 */
	@Column(name="TYPE_NAME")
    private String typeName;

	/**
	 * 父code
	 */
	@Column(name="PCODE")
    private String pCode;
	
	/**
	 * 父ID
	 */
	@Column(name="PID")
	private String pId;

	/**
	 * 评分标准
	 */
	@Column(name="STAN_SCORE")
    private BigDecimal stanScore;

	/**
	 * A类评分标准
	 */
	@Column(name="A_STAN_SCORE")
    private BigDecimal aStanScore;

	/**
	 * 是否叶子节点
	 */
	@Column(name="IS_LEAF")
    private String isLeaf;

	/**
	 * 是否启用0否1是
	 */
	@Column(name="IS_USE")
    private String isUse;

	/**
	 * 保监会权重
	 */
	@Column(name="CIRC_RATE")
    private String circRate;

	/**
	 * 保监局权重
	 */
	@Column(name="BUREAU_RATE")
    private String bureauRate;

	/**
	 * 占本类风险的比重
	 */
	@Column(name="THE_RISK_RATE")
    private String theRiskRate;

	/**
	 * 占难以量化风险的比重
	 */
	@Column(name="PDQ_RISK_RATE")
    private String pdqRiskRate;

	/**
	 * 占总风险的比重
	 */
	@Column(name="TOTAL_RISK_RATE")
    private String totalRiskRate;

	/**
	 * 加权后评分标准
	 */
	@Column(name="WEIG_SCOR_STAND")
    private String weigScorStand;

	/**
	 * 主体
	 */
	@Column(name="DATA_BODY")
    private String dataBody;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode == null ? null : typeCode.trim();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public BigDecimal getStanScore() {
        return stanScore;
    }

    public void setStanScore(BigDecimal stanScore) {
        this.stanScore = stanScore;
    }

    public BigDecimal getaStanScore() {
        return aStanScore;
    }

    public void setaStanScore(BigDecimal aStanScore) {
        this.aStanScore = aStanScore;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf == null ? null : isLeaf.trim();
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse == null ? null : isUse.trim();
    }

    public String getCircRate() {
        return circRate;
    }

    public void setCircRate(String circRate) {
        this.circRate = circRate == null ? null : circRate.trim();
    }

    public String getBureauRate() {
        return bureauRate;
    }

    public void setBureauRate(String bureauRate) {
        this.bureauRate = bureauRate == null ? null : bureauRate.trim();
    }

    public String getTheRiskRate() {
        return theRiskRate;
    }

    public void setTheRiskRate(String theRiskRate) {
        this.theRiskRate = theRiskRate == null ? null : theRiskRate.trim();
    }

    public String getPdqRiskRate() {
        return pdqRiskRate;
    }

    public void setPdqRiskRate(String pdqRiskRate) {
        this.pdqRiskRate = pdqRiskRate == null ? null : pdqRiskRate.trim();
    }

    public String getTotalRiskRate() {
        return totalRiskRate;
    }

    public void setTotalRiskRate(String totalRiskRate) {
        this.totalRiskRate = totalRiskRate == null ? null : totalRiskRate.trim();
    }

    public String getWeigScorStand() {
        return weigScorStand;
    }

    public void setWeigScorStand(String weigScorStand) {
        this.weigScorStand = weigScorStand == null ? null : weigScorStand.trim();
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody == null ? null : dataBody.trim();
    }

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}
    
}