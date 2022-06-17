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
 * 上传结果表
 */
@Entity
@Table(name="T_IRR_UPLOAD_RESULT")
public class IrrUploadResultEntity extends AuditorEntity implements Serializable{

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
	 * 拆分指标名称
	 */
	@Column(name="SPLIT_NAME")
    private String splitName;

	/**
	 * 拆分指标编码
	 */
	@Column(name="SPLIT_CODE")
    private String splitCode;
	
	/**
	 * 采集方式
	 */
	@Column(name="COLL_WAY")
	private String collWay;
	
	/**
	 * 采集机构/部门ID
	 */
	@Column(name="COLL_ORG_ID")
	private String collOrgId;
	
	/**
	 * 采集机构/部门名称
	 */
	@Column(name="COLL_ORG_NAME")
	private String collOrgName;
	
	/**
	 * 是否人工改动
	 */
	@Column(name="IS_HAND_CHANGE")
	private String isHandChange;
	
	/**
	 * 是否填写原因
	 */
	@Column(name="IS_FILL_REASON")
	private String isFillReason;
	
	/**
	 * 结果类型
	 */
	@Column(name="RESULT_TYPE_NAME")
	private String resultTypeName;

	/**
	 * Q1指标结果
	 */
	@Column(name="SPLIT_RESULT_Q1")
    private String splitResultQ1;
	
	/**
	 * Q1指标得分
	 */
	@Column(name="SPLIT_SCORE_Q1")
    private String splitScoreQ1;
	
	/**
	 * Q2指标结果
	 */
	@Column(name="SPLIT_RESULT_Q2")
    private String splitResultQ2;
	
	/**
	 * Q2指标得分
	 */
	@Column(name="SPLIT_SCORE_Q2")
    private String splitScoreQ2;
	
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
	
	/**
	 * 采集人名称
	 */
	@Column(name="COLL_USER_NAME")
	private String collUserName;

    public String getCollUserName() {
		return collUserName;
	}

	public void setCollUserName(String collUserName) {
		this.collUserName = collUserName;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId == null ? null : splitId.trim();
    }

    public String getSplitName() {
        return splitName;
    }

    public void setSplitName(String splitName) {
        this.splitName = splitName == null ? null : splitName.trim();
    }

    public String getSplitCode() {
        return splitCode;
    }

    public void setSplitCode(String splitCode) {
        this.splitCode = splitCode == null ? null : splitCode.trim();
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

	public String getSplitResultQ1() {
		return splitResultQ1;
	}

	public void setSplitResultQ1(String splitResultQ1) {
		this.splitResultQ1 = splitResultQ1;
	}

	public String getSplitScoreQ1() {
		return splitScoreQ1;
	}

	public void setSplitScoreQ1(String splitScoreQ1) {
		this.splitScoreQ1 = splitScoreQ1;
	}

	public String getSplitResultQ2() {
		return splitResultQ2;
	}

	public void setSplitResultQ2(String splitResultQ2) {
		this.splitResultQ2 = splitResultQ2;
	}

	public String getSplitScoreQ2() {
		return splitScoreQ2;
	}

	public void setSplitScoreQ2(String splitScoreQ2) {
		this.splitScoreQ2 = splitScoreQ2;
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

	public String getCollWay() {
		return collWay;
	}

	public void setCollWay(String collWay) {
		this.collWay = collWay;
	}

	public String getCollOrgId() {
		return collOrgId;
	}

	public void setCollOrgId(String collOrgId) {
		this.collOrgId = collOrgId;
	}

	public String getCollOrgName() {
		return collOrgName;
	}

	public void setCollOrgName(String collOrgName) {
		this.collOrgName = collOrgName;
	}

	public String getIsHandChange() {
		return isHandChange;
	}

	public void setIsHandChange(String isHandChange) {
		this.isHandChange = isHandChange;
	}

	public String getIsFillReason() {
		return isFillReason;
	}

	public void setIsFillReason(String isFillReason) {
		this.isFillReason = isFillReason;
	}

	public String getResultTypeName() {
		return resultTypeName;
	}

	public void setResultTypeName(String resultTypeName) {
		this.resultTypeName = resultTypeName;
	}

}