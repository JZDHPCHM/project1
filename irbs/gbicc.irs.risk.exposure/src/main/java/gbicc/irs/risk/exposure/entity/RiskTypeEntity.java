package gbicc.irs.risk.exposure.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.wsp.framework.jpa.entity.AuditorEntity;
@Entity
@Table(name="IRS_RISK_TYPE")
public class RiskTypeEntity extends AuditorEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 风险暴露细类编码
	 */
	@Id
	@Column(name="FD_CODE", length=255)
	private String code;
	/**
	 * 风险暴露细类
	 */
	@Column(name="FD_DETAIL_TYPE")
	private String detailType;
	/**
	 * 风险暴露大类
	 */
	@Column(name="FD_TYPE", length=255)
	private String type;
	/**
	 * 是否启用
	 */
	@Column(name="FD_IS_START", length=255)
	private String isStart;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDetailType() {
		return detailType;
	}
	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsStart() {
		return isStart;
	}
	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
