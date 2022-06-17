package net.gbicc.app.irr.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 任务表
 */
@Entity
@Table(name="T_IRR_TASK")
public class IrrTaskEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;

	/**
	 * 任务名称
	 */
	@Column(name="TASK_NAME")
    private String taskName;

	/**
	 * 任务期次
	 */
	@Column(name="TASK_BATCH")
    private String taskBatch;

	/**
	 * 计划截止时间
	 */
	@Column(name="DEAD_PLAN_DATE")
	private Date deadPlanDate;

	/**
	 * 结束时间
	 */
	@Column(name="END_DATE")
    private Date endDate;

	/**
	 * 任务状态
	 */
	@Column(name="TASK_STATUS")
    private String taskStatus;

	/**
	 * 任务描述
	 */
	@Column(name="TASK_DESC")
    private String taskDesc;

	/**
	 * 主体编码
	 */
	@Column(name="DATA_BODY")
    private String dataBody;

    public IrrTaskEntity() {
		super();
	}
    
	public IrrTaskEntity(String taskName, String taskBatch, Date deadPlanDate, String taskStatus) {
		super();
		this.taskName = taskName;
		this.taskBatch = taskBatch;
		this.deadPlanDate = deadPlanDate;
		this.taskStatus = taskStatus;
	}



	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskBatch() {
        return taskBatch;
    }

    public void setTaskBatch(String taskBatch) {
        this.taskBatch = taskBatch;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }

	public Date getDeadPlanDate() {
		return deadPlanDate;
	}

	public void setDeadPlanDate(Date deadPlanDate) {
		this.deadPlanDate = deadPlanDate;
	}
    
}