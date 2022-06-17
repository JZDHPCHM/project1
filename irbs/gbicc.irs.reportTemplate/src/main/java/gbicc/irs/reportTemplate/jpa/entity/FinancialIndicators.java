package gbicc.irs.reportTemplate.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.wsp.framework.jpa.entity.AuditorEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gbicc.irs.reportTemplate.jpa.support.FinancialIndicatorsJsonSerializer;

/**
 * 财报指标实体
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_RT_FINANCIAL_INDICATORS")
@JsonSerialize(using=FinancialIndicatorsJsonSerializer.class)
public class FinancialIndicators extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 4764621332745528701L;

	public FinancialIndicators() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//指标编号
	@Column(name="FD_FI_CODE", length=100)
	private String indicatorsCode;
	
	//指标名称
	@Column(name="FD_FI_NAME", length=500)
	private String indicatorsName;
	
	//指标描述
	@Column(name="FD_FI_DESCRIBE", length=1000)
	private String indicatorsDescribe;
	
	//适用模型
	@Type(type="org.wsp.framework.jpa.mapping.SetJsonStringsType")
	@Column(name="FD_FI_APPLY_MODEL", length=500)
	private Set<String> applyModel;

	@OneToMany(mappedBy="financialIndicators",cascade=CascadeType.REMOVE)
	private List<IndicatorsCalculationFormula> indicatorsCalculationFormulas = new ArrayList<IndicatorsCalculationFormula>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndicatorsCode() {
		return indicatorsCode;
	}

	public void setIndicatorsCode(String indicatorsCode) {
		this.indicatorsCode = indicatorsCode;
	}

	public String getIndicatorsName() {
		return indicatorsName;
	}

	public void setIndicatorsName(String indicatorsName) {
		this.indicatorsName = indicatorsName;
	}

	public String getIndicatorsDescribe() {
		return indicatorsDescribe;
	}

	public void setIndicatorsDescribe(String indicatorsDescribe) {
		this.indicatorsDescribe = indicatorsDescribe;
	}

	public Set<String> getApplyModel() {
		return applyModel;
	}

	public void setApplyModel(Set<String> applyModel) {
		this.applyModel = applyModel;
	}

	public List<IndicatorsCalculationFormula> getIndicatorsCalculationFormulas() {
		return indicatorsCalculationFormulas;
	}

	public void setIndicatorsCalculationFormulas(List<IndicatorsCalculationFormula> indicatorsCalculationFormulas) {
		this.indicatorsCalculationFormulas = indicatorsCalculationFormulas;
	}
	
}

