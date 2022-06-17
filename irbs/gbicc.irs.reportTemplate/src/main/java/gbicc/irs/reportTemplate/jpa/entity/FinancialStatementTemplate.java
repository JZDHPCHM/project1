package gbicc.irs.reportTemplate.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gbicc.irs.reportTemplate.jpa.support.FinancialStatementTemplateJsonSerializer;

/**
 * 财报模板表
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_RT_FINAN_STAT_TEMPLATE")
@JsonSerialize(using=FinancialStatementTemplateJsonSerializer.class)
public class FinancialStatementTemplate extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 2468197468726081572L;

	public FinancialStatementTemplate() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
		

	
	//系统财报类型
	@Column(name="FD_FS_TYPE", length=100)
	private String fsType;
	
	//原财报类型
	@Column(name="FD_FS_FORMERTYPE", length=100)
	private String fsFormerType;
	
	//是否有效
	@Column(name="FD_FS_ISVALID")
	@org.hibernate.annotations.Type(type="numeric_boolean")
	private Boolean fsValid;

	@OneToMany(mappedBy="finanStateTemplate",cascade=CascadeType.REMOVE)
	private List<FinancialAccountTemplateMapping> financialAccountTemplateMapping = new ArrayList<FinancialAccountTemplateMapping>();
	
	@OneToMany(mappedBy="fdNormId",cascade=CascadeType.REMOVE)
	private List<FinAccountNorm> finAccountNorm = new ArrayList<FinAccountNorm>();
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFsType() {
		return fsType;
	}

	public void setFsType(String fsType) {
		this.fsType = fsType;
	}

	public String getFsFormerType() {
		return fsFormerType;
	}

	public void setFsFormerType(String fsFormerType) {
		this.fsFormerType = fsFormerType;
	}

	public Boolean getFsValid() {
		return fsValid;
	}

	public void setFsValid(Boolean fsValid) {
		this.fsValid = fsValid;
	}

	public List<FinancialAccountTemplateMapping> getFinancialAccountTemplateMapping() {
		return financialAccountTemplateMapping;
	}

	public void setFinancialAccountTemplateMapping(List<FinancialAccountTemplateMapping> financialAccountTemplateMapping) {
		this.financialAccountTemplateMapping = financialAccountTemplateMapping;
	}

	public List<FinAccountNorm> getFinAccountNorm() {
		return finAccountNorm;
	}

	public void setFinAccountNorm(List<FinAccountNorm> finAccountNorm) {
		this.finAccountNorm = finAccountNorm;
	}

	
}

