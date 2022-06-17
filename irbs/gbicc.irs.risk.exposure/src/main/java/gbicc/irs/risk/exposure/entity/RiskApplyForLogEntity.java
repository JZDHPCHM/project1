package gbicc.irs.risk.exposure.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;


@Entity
@Table(name="IRS_RISK_APPLY_FOR_LOG")
public class RiskApplyForLogEntity extends AuditorEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public RiskApplyForLogEntity() {
		super();
	}
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	

	/**
	 * 产品类型
	 */
	@Column(name="PROCESSOR", length=255)
	private String processor;

	/**
	 * 认定时间
	 */
	@Column(name="MAINTAIN_DT")
	private Date maintainDt;
	/**
	 * 类型
	 */
	@Column(name="MAINTAIN_RESULT", length=255)
	private String maintainResult;
	/**
	 * 理由
	 */
	@Column(name="REASON", length=255)
	private String reason;
	
	/**
	 * 风险暴露id
	 */
	@Column(name="RISK_EXPOSURE_ID", length=255)
	private String riskExposureId;

	@Column(name="OPERATE", length=50)
	private String operate;
	
	@Column(name="ROLE_Name", length=100)
	private String roleName;

	@Transient
	private String org;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public Date getMaintainDt() {
		return maintainDt;
	}
	public void setMaintainDt(Date maintainDt) {
		this.maintainDt = maintainDt;
	}
	public String getMaintainResult() {
		return maintainResult;
	}
	public void setMaintainResult(String maintainResult) {
		this.maintainResult = maintainResult;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRiskExposureId() {
		return riskExposureId;
	}
	public void setRiskExposureId(String riskExposureId) {
		this.riskExposureId = riskExposureId;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
    
    
}
