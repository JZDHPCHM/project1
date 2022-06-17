package gbicc.irs.risk.exposure.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;
@Entity
@Table(name="irs_risk_type_ctm")
public class RiskTypeCtmEntity extends AuditorEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	//信贷客户号(主键)
	@Id
	@GeneratedValue(generator = "system-uuid-assigned")
	@GenericGenerator(name = "system-uuid-assigned", strategy = "assigned")
	@Column(name="FD_CTM_NO", length=255)
	private String ctmNo;
	/**
	 */
	@Column(name="FD_CTM_NAME")
	private String ctmName;
	/**
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FD_RISK_TYPE_CODE")
	private RiskTypeEntity riskTypeEntity;
	
	public String getCtmNo() {
		return ctmNo;
	}
	public void setCtmNo(String ctmNo) {
		this.ctmNo = ctmNo;
	}
	public String getCtmName() {
		return ctmName;
	}
	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}
	public RiskTypeEntity getRiskTypeEntity() {
		return riskTypeEntity;
	}
	public void setRiskTypeEntity(RiskTypeEntity riskTypeEntity) {
		this.riskTypeEntity = riskTypeEntity;
	}
	
}
