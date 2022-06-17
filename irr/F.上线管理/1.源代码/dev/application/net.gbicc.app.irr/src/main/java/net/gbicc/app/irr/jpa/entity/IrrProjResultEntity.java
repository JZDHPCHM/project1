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
 * 评估项目得分表
 */
@Entity
@Table(name="T_IRR_PROJ_RESULT")
public class IrrProjResultEntity extends AuditorEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;

	/**
	 * 类型编码
	 */
	@Column(name="TYPE_CODE")
    private String typeCode;

	/**
	 * 类型名称
	 */
	@Column(name="TYPE_NAME")
    private String typeName;

	/**
	 * 类型ID
	 */
	@Column(name="TYPE_ID")
    private String typeId;

	/**
	 * 分公司的得分
	 */
	@Column(name="BRANCH_SCORE")
    private BigDecimal branchScore;

	/**
	 * 总公司的得分
	 */
	@Column(name="HEAD_SCORE")
    private BigDecimal headScore;

	/**
	 * 得分
	 */
	@Column(name="TOTAL_SCORE")
    private BigDecimal totalScore;

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
	 * 主体编码
	 */
	@Column(name="DATA_BODY")
    private String dataBody;
	
	/**
	 * 结果标识
	 */
	@Column(name="RESULT_FLAG")
	private String resultFlag;
	
	/**
	 * 序号
	 */
	@Column(name="SORT_NUM")
	private Integer sortNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getBranchScore() {
        return branchScore;
    }

    public void setBranchScore(BigDecimal branchScore) {
        this.branchScore = branchScore;
    }

    public BigDecimal getHeadScore() {
        return headScore;
    }

    public void setHeadScore(BigDecimal headScore) {
        this.headScore = headScore;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
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

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	
}