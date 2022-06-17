package gbicc.irs.reportTemplate.jpa.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 财报模板科目定义表
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_RT_FINANCIAL_ACCOUNT")
public class FinancialAccount extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 3834332255056548430L;

	public FinancialAccount() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
		
	//科目行号
	@Column(name="FD_FA_NUMBER")
	private Integer faNumber;
	
	//科目编号
	@Column(name="FD_FA_CODE", length=100)
	private String faCode;
	
	//科目名称
	@Column(name="FD_FA_NAME", length=500)
	private String faName;
	
	//科目分类
	@Column(name="FD_FA_CATEGORY", length=500)
	private String faCategory;
	
	//财报模板
	@Type(type="org.wsp.framework.jpa.mapping.SetJsonStringsType")
	@Column(name="FD_FA_STATEMENT_TEMPLATE",length=100)
	private Set<String> statementTemplate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getFaNumber() {
		return faNumber;
	}

	public void setFaNumber(Integer faNumber) {
		this.faNumber = faNumber;
	}

	public String getFaCode() {
		return faCode;
	}

	public void setFaCode(String faCode) {
		this.faCode = faCode;
	}

	public String getFaName() {
		return faName;
	}

	public void setFaName(String faName) {
		this.faName = faName;
	}

	public String getFaCategory() {
		return faCategory;
	}

	public void setFaCategory(String faCategory) {
		this.faCategory = faCategory;
	}

	public Set<String> getStatementTemplate() {
		return statementTemplate;
	}

	public void setStatementTemplate(Set<String> statementTemplate) {
		this.statementTemplate = statementTemplate;
	}

}

