package net.gbicc.app.xbrl.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
*	XBRL监管机构映射实体
*/
@Entity
@Table(name="T_XBRL_REGULATOR_MAPPING")
public class XbrlRegulatorMappingEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;
	/**
	 * 公司编码
	 */
	@Column(name="COMPANY_CODE")
	private String companyCode;
	/**
	 * 监管机构编码
	 */
	@Column(name="REGULATOR_CODE")
	private String regulatorCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getRegulatorCode() {
		return regulatorCode;
	}
	public void setRegulatorCode(String regulatorCode) {
		this.regulatorCode = regulatorCode;
	}
}
