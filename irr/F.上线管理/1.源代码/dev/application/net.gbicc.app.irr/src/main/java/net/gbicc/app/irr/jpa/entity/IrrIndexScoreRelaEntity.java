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
* 指标得分关联实体
*
*/
@Entity
@Table(name="T_IRR_INDEX_SCORE_RELA")
public class IrrIndexScoreRelaEntity extends AuditorEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;
	
	/**
	 * 指标ID
	 */
	@Column(name="INDEX_ID")
	private String indexId;
	
	/**
	 * 指标编码
	 */
	@Column(name="INDEX_CODE")
	private String indexCode;
	
	/**
	 * 指标名称
	 */
	@Column(name="INDEX_NAME")
	private String indexName;
	
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public String getIndexCode() {
		return indexCode;
	}

	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
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
}
