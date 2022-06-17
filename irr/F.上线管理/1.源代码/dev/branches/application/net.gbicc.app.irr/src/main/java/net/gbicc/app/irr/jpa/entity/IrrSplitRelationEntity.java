package net.gbicc.app.irr.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
* 拆分指标关联
*
*/
@Entity
@Table(name="T_IRR_SPLIT_RELATION")
public class IrrSplitRelationEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;
	
	/**
	 * 拆分指标ID
	 */
	@Column(name="SPLIT_ID")
	private String splitId;
	
	/**
	 * 拆分指标Code
	 */
	@Column(name="SPLIT_CODE")
	private String splitCode;
	
	/**
	 * 拆分指标名称
	 */
	@Column(name="SPLIT_NAME")
	private String splitName;

	/**
	 * 关联指标ID
	 */
	@Column(name="RELA_ID")
	private String relaId;
	
	/**
	 * 关联指标编码
	 */
	@Column(name="RELA_CODE")
	private String relaCode;
	
	/**
	 * 关联指标名称
	 */
	@Column(name="RELA_NAME")
	private String relaName;
	
	/**
	 * 主体编码
	 */
	@Column(name="DATA_BODY")
	private String dataBody;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSplitId() {
		return splitId;
	}

	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}

	public String getSplitCode() {
		return splitCode;
	}

	public void setSplitCode(String splitCode) {
		this.splitCode = splitCode;
	}

	public String getSplitName() {
		return splitName;
	}

	public void setSplitName(String splitName) {
		this.splitName = splitName;
	}

	public String getRelaId() {
		return relaId;
	}

	public void setRelaId(String relaId) {
		this.relaId = relaId;
	}

	public String getRelaCode() {
		return relaCode;
	}

	public void setRelaCode(String relaCode) {
		this.relaCode = relaCode;
	}

	public String getRelaName() {
		return relaName;
	}

	public void setRelaName(String relaName) {
		this.relaName = relaName;
	}

	public String getDataBody() {
		return dataBody;
	}

	public void setDataBody(String dataBody) {
		this.dataBody = dataBody;
	}
	
}
