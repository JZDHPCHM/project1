package gbicc.irs.reportTemplate.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import gbicc.irs.reportTemplate.jpa.support.OutlierHandlingType;

/**
 * 指标实体
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_RT_KI_LIB")
public class FinancialKiLib extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 2430151537269316432L;

	public FinancialKiLib() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//指标编号
	@Column(name="FD_KI_CODE", length=100)
	private String kiCode;
	
	//指标名称
	@Column(name="FD_KI_NAME", length=500)
	private String kiName;
	
	//公式分子
	@Column(name="FD_KI_NUMERATOR", length=1000)
	private String kiNumerator;
	
	
	//公式分母
	@Column(name="FD_KI_DENOMINATOR", length=1000)
	private String kiDenominator;
	
	
	//异常值处理
	@Column(name="FD_OUTLIER_HANDLING", length=1000)
	@Enumerated(EnumType.STRING)
	private OutlierHandlingType outlierHandling;
		

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getKiCode() {
		return kiCode;
	}

	public void setKiCode(String kiCode) {
		this.kiCode = kiCode;
	}

	public String getKiName() {
		return kiName;
	}

	public void setKiName(String kiName) {
		this.kiName = kiName;
	}


	public String getKiNumerator() {
		return kiNumerator;
	}


	public void setKiNumerator(String kiNumerator) {
		this.kiNumerator = kiNumerator;
	}


	public String getKiDenominator() {
		return kiDenominator;
	}


	public void setKiDenominator(String kiDenominator) {
		this.kiDenominator = kiDenominator;
	}


	public OutlierHandlingType getOutlierHandling() {
		return outlierHandling;
	}

	public void setOutlierHandling(OutlierHandlingType outlierHandling) {
		this.outlierHandling = outlierHandling;
	}

}

