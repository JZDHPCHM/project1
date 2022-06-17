package net.gbicc.app.pfm.jpa.entity;

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
 * 渠道绩效结果表
 */
@Entity
@Table(name="T_PFM_CHANNEL_RESULT")
public class PfmChannelResultEntity extends AuditorEntity implements Serializable{

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
	 * 指标绩效得分
	 */
	@Column(name="INDEX_SCORE")
    private String indexScore;

	/**
	 * 指标权重
	 */
	@Column(name="INDEX_WEIGHT")
    private BigDecimal indexWeight;

	/**
	 * 标准分值
	 */
	@Column(name="STAND_VALUE")
    private BigDecimal standValue;

	/**
	 * 渠道标识
	 */
	@Column(name="CHANNEL_FLAG")
    private String channelFlag;

	/**
	 * 任务期次
	 */
	@Column(name="TASK_BATCH")
    private String taskBatch;

	/**
	 * 任务ID
	 */
	@Column(name="TASK_ID")
    private String taskId;

	/**
	 * 主体编码
	 */
	@Column(name="DATA_BODY")
    private String dataBody;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId == null ? null : indexId.trim();
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName == null ? null : indexName.trim();
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode == null ? null : indexCode.trim();
    }

    public String getIndexScore() {
        return indexScore;
    }

    public void setIndexScore(String indexScore) {
        this.indexScore = indexScore == null ? null : indexScore.trim();
    }

    public BigDecimal getIndexWeight() {
        return indexWeight;
    }

    public void setIndexWeight(BigDecimal indexWeight) {
        this.indexWeight = indexWeight;
    }

    public BigDecimal getStandValue() {
        return standValue;
    }

    public void setStandValue(BigDecimal standValue) {
        this.standValue = standValue;
    }

    public String getChannelFlag() {
        return channelFlag;
    }

    public void setChannelFlag(String channelFlag) {
        this.channelFlag = channelFlag == null ? null : channelFlag.trim();
    }

    public String getTaskBatch() {
        return taskBatch;
    }

    public void setTaskBatch(String taskBatch) {
        this.taskBatch = taskBatch == null ? null : taskBatch.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody == null ? null : dataBody.trim();
    }
}