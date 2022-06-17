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
 * 授权任务日志
 * 
 * @author FC
 * @version v1.0 2019年12月5日
 */
@Entity
@Table(name = "T_IRR_AUTH_TASK_LOG")
public class IrrAuthTaskLogEntity extends AuditorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;
    
    /**
     * 任务ID
     */
    @Column(name = "TASK_ID")
    private String taskId;

    /**
     * 任务编码
     */
    @Column(name = "TASK_CODE")
    private String taskCode;

    /**
     * 任务名称
     */
    @Column(name = "TASK_NAME")
    private String taskName;

    /**
     * 授权人ID
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 授权人名称
     */
    @Column(name = "USER_NAME")
    private String userName;

    /**
     * 被授权人ID
     */
    @Column(name = "AUTH_ID")
    private String authId;

    /**
     * 被授权人名称
     */
    @Column(name = "AUTH_NAME")
    private String authName;

    /**
     * 处理结果
     */
    @Column(name = "DEAL_RESULT")
    private String dealResult;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

}
