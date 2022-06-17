package gbicc.irs.reportTemplate.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 基础指标实体
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_RT_BASICS_LIB")
public class FinancialBasicsLib extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 6124178784456206484L;

	public FinancialBasicsLib() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
	
	//基础指标编号
	@Column(name="FD_BASICS_CODE", length=100)
	private String basicsCode;
	
	//基础指标名称
	@Column(name="FD_BASICS_NAME", length=500)
	private String basicsName;
	
	//指标描述
	@Column(name="FD_BASICS_DESCRIBE", length=1000)
	private String basicsDescribe;
	
	
	//小企业一般准则
	@Column(name="FD_SMALLENTERPRISE", length=1000)
	private String smallenterprise;
	
	//小企业简易准则
	@Column(name="FD_SMALLENTERPRISEOLD", length=1000)
	private String smallenterpriseold;
	
	//一般企业新准则
	@Column(name="FD_GENERALENTERPRISE", length=1000)
	private String generalenterprise;

	//一般企业旧准则
	@Column(name="FD_GENERALENTERPRISEOLD", length=1000)
	private String generalenterpriseold;

	//事业单位新准则
	@Column(name="FD_INSTITUTION", length=1000)
	private String institution;
	
	//事业单位旧准则
	@Column(name="FD_INSTITUTIONOLD", length=1000)
	private String institutionold;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBasicsCode() {
		return basicsCode;
	}

	public void setBasicsCode(String basicsCode) {
		this.basicsCode = basicsCode;
	}

	public String getBasicsName() {
		return basicsName;
	}

	public void setBasicsName(String basicsName) {
		this.basicsName = basicsName;
	}

	public String getBasicsDescribe() {
		return basicsDescribe;
	}

	public void setBasicsDescribe(String basicsDescribe) {
		this.basicsDescribe = basicsDescribe;
	}

	public String getSmallenterprise() {
		return smallenterprise;
	}

	public void setSmallenterprise(String smallenterprise) {
		this.smallenterprise = smallenterprise;
	}

	public String getSmallenterpriseold() {
		return smallenterpriseold;
	}

	public void setSmallenterpriseold(String smallenterpriseold) {
		this.smallenterpriseold = smallenterpriseold;
	}

	public String getGeneralenterprise() {
		return generalenterprise;
	}

	public void setGeneralenterprise(String generalenterprise) {
		this.generalenterprise = generalenterprise;
	}

	public String getGeneralenterpriseold() {
		return generalenterpriseold;
	}

	public void setGeneralenterpriseold(String generalenterpriseold) {
		this.generalenterpriseold = generalenterpriseold;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getInstitutionold() {
		return institutionold;
	}

	public void setInstitutionold(String institutionold) {
		this.institutionold = institutionold;
	}
	
}

