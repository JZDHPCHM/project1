package gbicc.irs.customer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

@Entity
@Table(name="ns_customer_early_warning")
public class CustomerEarlyWarning extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid-assigned")
	@GenericGenerator(name = "system-uuid-assigned", strategy = "assigned")
	@Column(name="FD_ID", length=100)
	private String id;
	
	/**
	 * 客户编号
	 */
	@Column(name="FD_CTM_NO", length=100)
	private String ctmNo;
	
	/**
	 * 预警主题
	 */
	@Column(name="FD_EARLY_WARNING", length=100)
	private String earlyWarning;
	
	/**
	 * 预警时间
	 */
	@Column(name="FD_EARLY_DT", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date earlyDt;
	
	/**
	 * 预警信号
	 */
	@Column(name="FD_EARLY_SIGNAL", length=100)
	private String earlySignal;
	
	/**
	 * 信号等级
	 */
	@Column(name="FD_SIGNAL_LEVEL", length=100)
	private String signalLevel;
	
	/**
	 * 预警标签
	 */
	@Column(name="FD_EARLY_DESC", length=100)
	private String earlyDesc;
	
	/**
	 * 预警描述
	 */
	@Column(name="FD_WARNING_DESCRIPTION", length=100)
	private String warningDescription;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCtmNo() {
		return ctmNo;
	}

	public void setCtmNo(String ctmNo) {
		this.ctmNo = ctmNo;
	}

	public String getEarlyWarning() {
		return earlyWarning;
	}

	public void setEarlyWarning(String earlyWarning) {
		this.earlyWarning = earlyWarning;
	}

	public Date getEarlyDt() {
		return earlyDt;
	}

	public void setEarlyDt(Date earlyDt) {
		this.earlyDt = earlyDt;
	}

	public String getEarlySignal() {
		return earlySignal;
	}

	public void setEarlySignal(String earlySignal) {
		this.earlySignal = earlySignal;
	}

	public String getSignalLevel() {
		return signalLevel;
	}

	public void setSignalLevel(String signalLevel) {
		this.signalLevel = signalLevel;
	}

	public String getEarlyDesc() {
		return earlyDesc;
	}

	public void setEarlyDesc(String earlyDesc) {
		this.earlyDesc = earlyDesc;
	}

	public String getWarningDescription() {
		return warningDescription;
	}

	public void setWarningDescription(String warningDescription) {
		this.warningDescription = warningDescription;
	}
	
}
