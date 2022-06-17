package gbicc.irs.code.library.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;


@Entity
@Table(name="FR_SYS_FIX_CODE")
public class CodeLibraryPojo extends AuditorEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="FD_ID", length=36)
	@NotNull
	private String id;
	
	// 代码编号
	@Column(name="FD_CODE_NO", length=36)
	private String codeNo;
	
	// 中文注释
	@Column(name="FD_CODE_NAME", length=36)
	private String codeName;
	
	// 项目编号
	@Column(name="FD_ITEM_CODE", length=36)
	private String itemCode;
	
	// 项目名称
	@Column(name="FD_ITEM_NAME", length=36)
	private String itemName;
	
	// 排序
	@Column(name="FD_SORT_NO", length=36)
	private String sortNo;

	// 是否可用
	@Column(name="FD_IS_INUSE", length=18)
	private String isInuse;

	public String getCodeNo() {
		return codeNo;
	}

	public void setCodeNo(String codeNo) {
		this.codeNo = codeNo;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public String getIsInuse() {
		return isInuse;
	}

	public void setIsInuse(String isInuse) {
		this.isInuse = isInuse;
	}

	public CodeLibraryPojo() {
	}

	public CodeLibraryPojo(@NotNull String codeNo, String codeName, String itemCode, String itemName, String sortNo,
			String isInuse) {
		super();
		this.codeNo = codeNo;
		this.codeName = codeName;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.sortNo = sortNo;
		this.isInuse = isInuse;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
