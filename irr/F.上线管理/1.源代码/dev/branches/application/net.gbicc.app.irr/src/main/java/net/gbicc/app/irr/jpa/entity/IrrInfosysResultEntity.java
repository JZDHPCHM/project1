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
 * 信息系统结果表
 */
@Entity
@Table(name="T_IRR_INFOSYS_RESULT")
public class IrrInfosysResultEntity extends AuditorEntity implements Serializable{
	
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
	 * 指标得分
	 */
	@Column(name="INDEX_SCORE")
    private String indexScore;

	/**
	 * 百分制得分
	 */
	@Column(name="INDEX_CENT_SCORE")
    private String indexCentScore;

	/**
	 * 任务期次
	 */
	@Column(name="TASK_BATCH")
    private String taskBatch;

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

    public String getIndexScore() {
        return indexScore;
    }

    public void setIndexScore(String indexScore) {
        this.indexScore = indexScore;
    }

    public String getIndexCentScore() {
        return indexCentScore;
    }

    public void setIndexCentScore(String indexCentScore) {
        this.indexCentScore = indexCentScore;
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
}