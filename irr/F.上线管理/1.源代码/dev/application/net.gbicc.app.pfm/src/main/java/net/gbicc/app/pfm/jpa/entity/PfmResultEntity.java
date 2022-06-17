package net.gbicc.app.pfm.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 绩效结果
 * 
 * @author FC
 * @version v1.0 2019年7月4日
 */
@Entity
@Table(name = "T_PFM_RESULT")
public class PfmResultEntity extends AuditorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;
    /**
     * 绩效名称
     */
    @Column(name = "PFM_NAME")
    private String pfmName;
    /**
     * 绩效得分
     */
    @Column(name = "PFM_VALUE")
    private Double pfmValue;
    /**
     * 任务ID
     */
    @Column(name = "TASK_ID")
    private String taskId;
    /**
     * 任务期次
     */
    @Column(name = "TASK_BATCH")
    private String taskBatch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPfmName() {
        return pfmName;
    }

    public void setPfmName(String pfmName) {
        this.pfmName = pfmName;
    }

    public Double getPfmValue() {
        return pfmValue;
    }

    public void setPfmValue(Double pfmValue) {
        this.pfmValue = pfmValue;
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

}
