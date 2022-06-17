package net.gbicc.app.irr.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 机构结果表
 */
@Entity
@Table(name="T_IRR_ORG_RESULT")
public class IrrOrgResultEntity extends AuditorEntity implements Serializable{
	
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
	 * 指标名称
	 */
	@Column(name="INDEX_NAME")
    private String indexName;
	
	/**
	 * 指标编码
	 */
	@Column(name="INDEX_CODE")
    private String indexCode;
	
	/**
	 * Q1指标结果
	 */
	@Column(name="INDEX_RESULT_Q1")
    private String indexResultQ1;
	
	/**
	 * Q1指标得分
	 */
	@Column(name="INDEX_SCORE_Q1")
    private String indexScoreQ1;
	
	/**
	 * Q2指标结果
	 */
	@Column(name="INDEX_RESULT_Q2")
    private String indexResultQ2;
	
	/**
	 * Q2指标得分
	 */
	@Column(name="INDEX_SCORE_Q2")
    private String indexScoreQ2;
	
	/**
	 * 数据验证
	 */
	@Column(name="DATA_VALI")
    private BigDecimal dataVali;
	
	/**
	 * 数据说明
	 */
	@Column(name="DATA_DESC")
    private String dataDesc;
	
	/**
	 * 任务ID
	 */
	@Column(name="TASK_ID")
    private String taskId;
	
	/**
	 * 任务期次
	 */
	@Column(name="TASK_BATCH")
    private String taskBatch;
	
	/**
	 * 机构ID
	 */
	@Column(name="ORG_ID")
	private String orgId;
	
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

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getIndexResultQ1() {
        return indexResultQ1;
    }

    public void setIndexResultQ1(String indexResultQ1) {
        this.indexResultQ1 = indexResultQ1;
    }

    public String getIndexScoreQ1() {
        return indexScoreQ1;
    }

    public void setIndexScoreQ1(String indexScoreQ1) {
        this.indexScoreQ1 = indexScoreQ1;
    }

    public String getIndexResultQ2() {
        return indexResultQ2;
    }

    public void setIndexResultQ2(String indexResultQ2) {
        this.indexResultQ2 = indexResultQ2;
    }

    public String getIndexScoreQ2() {
        return indexScoreQ2;
    }

    public void setIndexScoreQ2(String indexScoreQ2) {
        this.indexScoreQ2 = indexScoreQ2;
    }

    public BigDecimal getDataVali() {
        return dataVali;
    }

    public void setDataVali(BigDecimal dataVali) {
        this.dataVali = dataVali;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskBatch() {
        return taskBatch;
    }

    public void setTaskBatch(String taskBatch) {
        this.taskBatch = taskBatch;
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
}