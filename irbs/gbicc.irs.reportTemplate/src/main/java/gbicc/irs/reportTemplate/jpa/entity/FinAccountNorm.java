package gbicc.irs.reportTemplate.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="NS_FIN_ACCOUNT_NORM")
public class FinAccountNorm {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=100)
	private String fdId;
	
	/**
	 * 准则编码
	 */
	@Column(name="FD_CODE", length=100)
	private String fdCode;
	
	
	@Column(name="FD_NAME", length=100)
	private String fdName;

	/**
	 * 准则
	 */
	@Column(name="FD_NORM")
	private String fdNorm;
	
	
	/**
	 * 准则配置
	 */
	@Column(name="FD_NORMCONFIG")
	private String fdNormConfig;
	
	/**
	 * 财报模板
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FD_NORMID")
	private FinancialStatementTemplate fdNormId;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdCode() {
		return fdCode;
	}

	public void setFdCode(String fdCode) {
		this.fdCode = fdCode;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getFdNorm() {
		return fdNorm;
	}

	public void setFdNorm(String fdNorm) {
		this.fdNorm = fdNorm;
	}

	public String getFdNormConfig() {
		return fdNormConfig;
	}

	public void setFdNormConfig(String fdNormConfig) {
		this.fdNormConfig = fdNormConfig;
	}

	public FinancialStatementTemplate getFdNormId() {
		return fdNormId;
	}

	public void setFdNormId(FinancialStatementTemplate fdNormId) {
		this.fdNormId = fdNormId;
	}


}
