package gbicc.irs.reportTemplate.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 财报模板科目映射表
 * @author 寇鹏阳
 *
 */
@Entity
@Table(name="NS_RT_FINAN_ACCOUNT_MAPP")
public class FinancialAccountTemplateMapping extends AuditorEntity implements Serializable{
	private static final long serialVersionUID = 7272827689254311075L;

	public FinancialAccountTemplateMapping() {}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="FD_ID", length=36)
	private String id;
		
	//财报模板
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FD_STAT_TEMPLATE_FD_ID")
	private FinancialStatementTemplate finanStateTemplate;
	
	//科目分类
	@Column(name="FD_FA_CATEGORY")
	private String faCategory;
	
	//科目行号
	@Column(name="FD_AM_NUMBER")
	private Integer amNumber;

	//模板科目编号
	@Column(name="FD_FA_CODE",length=100)
	private String templateFaCode;
	
	//模板科目名称
	@Column(name="FD_FA_NAME",length=500)
	private String templateFaName;
	
	//原科目编号
	@Column(name="FD_FA_FORMERCODE",length=100)
	private String faFormerCode;
	
	//原科目名称
	@Column(name="FD_FA_FORMERNAME",length=500)
	private String faFormerName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FinancialStatementTemplate getFinanStateTemplate() {
		return finanStateTemplate;
	}

	public void setFinanStateTemplate(FinancialStatementTemplate finanStateTemplate) {
		this.finanStateTemplate = finanStateTemplate;
	}

	public String getFaCategory() {
		return faCategory;
	}

	public void setFaCategory(String faCategory) {
		this.faCategory = faCategory;
	}

	public Integer getAmNumber() {
		return amNumber;
	}

	public void setAmNumber(Integer amNumber) {
		this.amNumber = amNumber;
	}

	public String getTemplateFaCode() {
		return templateFaCode;
	}

	public void setTemplateFaCode(String templateFaCode) {
		this.templateFaCode = templateFaCode;
	}

	public String getTemplateFaName() {
		return templateFaName;
	}

	public void setTemplateFaName(String templateFaName) {
		this.templateFaName = templateFaName;
	}

	public String getFaFormerCode() {
		return faFormerCode;
	}

	public void setFaFormerCode(String faFormerCode) {
		this.faFormerCode = faFormerCode;
	}

	public String getFaFormerName() {
		return faFormerName;
	}

	public void setFaFormerName(String faFormerName) {
		this.faFormerName = faFormerName;
	}

}

