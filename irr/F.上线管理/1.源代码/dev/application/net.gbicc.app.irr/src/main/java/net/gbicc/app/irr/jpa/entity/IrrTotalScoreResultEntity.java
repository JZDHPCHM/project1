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
 * 总分多维度结果表
 */
@Entity
@Table(name="T_IRR_TOTALSCORE_RESULT")
public class IrrTotalScoreResultEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;
	
	/**
	 * 评估项目ID
	 */
	@Column(name="PROJ_ID")
    private String projId;

	/**
	 * 评估项目名称
	 */
	@Column(name="PROJ_NAME")
    private String projName;

	/**
	 * 评估项目编码
	 */
	@Column(name="PROJ_CODE")
    private String projCode;

	/**
	 * 评估项目结果
	 */
	@Column(name="PROJ_RESULT")
    private String projResult;

	/**
	 * 结果标识
	 */
	@Column(name="RESULT_FLAG")
    private String resultFlag;

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

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjCode() {
        return projCode;
    }

    public void setProjCode(String projCode) {
        this.projCode = projCode;
    }

    public String getProjResult() {
        return projResult;
    }

    public void setProjResult(String projResult) {
        this.projResult = projResult;
    }

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
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
}