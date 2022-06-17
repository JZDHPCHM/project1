package gbicc.irs.reportTemplate.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 指标计算公式实体
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_RT_FI_CALCU_FORM")
public class IndicatorsCalculationFormula extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 6521485293765799817L;

	public IndicatorsCalculationFormula() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//指标定义对象
	@ManyToOne
	@JoinColumn(name="FD_FI_ID")
	private FinancialIndicators financialIndicators;
	
	//适用财报模板
	@Column(name="FD_FI_REPORT_TEMPLATE", length=100)
	private String reportTemplate;
		
	//计算公式
	@Column(name="FD_FI_CALCULATION_FORMULA", length=2000)
	private String calculationFormula;

	//显示指标公式
	@Transient
	private String displayCalculationFormula;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FinancialIndicators getFinancialIndicators() {
		return financialIndicators;
	}

	public void setFinancialIndicators(FinancialIndicators financialIndicators) {
		this.financialIndicators = financialIndicators;
	}

	public String getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(String reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public String getCalculationFormula() {
		return calculationFormula;
	}

	public void setCalculationFormula(String calculationFormula) {
		this.calculationFormula = calculationFormula;
	}

	public String getDisplayCalculationFormula() {
		return displayCalculationFormula;
	}

	public void setDisplayCalculationFormula(String displayCalculationFormula) {
		this.displayCalculationFormula = displayCalculationFormula;
	}
	
	
}

