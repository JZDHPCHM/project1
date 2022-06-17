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
*	irr历史数据导入实体
*/
@Entity
@Table(name="T_IRR_DATAIMPL_HIS")
public class IrrDataImplHisEntity extends AuditorEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;
	
	/**
	 * 评估项目编码
	 */
	@Column(name="PROJ_TYPE_CODE")
	private String projTypeCode;
	/**
	 * 评估项目名称
	 */
	@Column(name="PROJ_TYPE_NAME")
	private String projTypeName;
	/**
	 * 指标名称
	 */
	@Column(name="INDEX_NAME")
	private String indexName;
	/**
	 * 行次
	 */
	@Column(name="INDEX_LINE")
	private Double indexLine;
	/**
	 * 指标结果
	 */
	@Column(name="INDEX_RESULT")
	private String indexResult;
	/**
	 * 机构编码
	 */
	@Column(name="ORG_CODE")
	private String orgCode;
	/**
	 * 机构名称
	 */
	@Column(name="ORG_NAME")
	private String orgName;
	/**
	 * xbrl机构编码
	 */
	@Column(name="XBRL_CODE")
	private String xbrlCode;
	/**
	 * xbrl机构名称
	 */
	@Column(name="XBRL_NAME")
	private String xbrlName;
	/**
	 * 期次
	 */
	@Column(name="TASK_BATCH")
	private String taskBatch;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjTypeCode() {
		return projTypeCode;
	}
	public void setProjTypeCode(String projTypeCode) {
		this.projTypeCode = projTypeCode;
	}
	public String getProjTypeName() {
		return projTypeName;
	}
	public void setProjTypeName(String projTypeName) {
		this.projTypeName = projTypeName;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public Double getIndexLine() {
		return indexLine;
	}
	public void setIndexLine(Double indexLine) {
		this.indexLine = indexLine;
	}
	public String getIndexResult() {
		return indexResult;
	}
	public void setIndexResult(String indexResult) {
		this.indexResult = indexResult;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getTaskBatch() {
		return taskBatch;
	}
	public void setTaskBatch(String taskBatch) {
		this.taskBatch = taskBatch;
	}
	public String getXbrlCode() {
		return xbrlCode;
	}
	public void setXbrlCode(String xbrlCode) {
		this.xbrlCode = xbrlCode;
	}
	public String getXbrlName() {
		return xbrlName;
	}
	public void setXbrlName(String xbrlName) {
		this.xbrlName = xbrlName;
	}
}
