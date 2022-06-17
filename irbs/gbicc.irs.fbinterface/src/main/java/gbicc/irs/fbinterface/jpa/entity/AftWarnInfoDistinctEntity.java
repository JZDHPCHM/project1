package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 预警信息去重表
 * 
 * @author FC
 * @version v1.0 2019年9月17日
 */
@Entity
@Table(name = "T_AFT_WARN_INFO_DISTINCT")
public class AftWarnInfoDistinctEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID", length = 36)
    private String id;

    /**
     * 规则编码
     */
    @Column(name = "RULE_CODE")
    private String ruleCode;

    /**
     * 任务批次
     */
    @Column(name = "TASK_SEQNO")
    private Short taskSeqno;

    /**
     * 承租人ID
     */
    @Column(name = "LESSEE_ID")
    private String lesseeId;

    /**
     * 预警详情
     */
    @Column(name = "WARN_DESC")
    private String warnDesc;

    /**
     * 关联人类型
     */
    @Column(name = "ASSO_TYPE")
    private String assoType;

    /**
     * 关联人ID
     */
    @Column(name = "ASSO_ID")
    private String assoId;

    /**
     * 预警时间
     */
    @Column(name = "WARN_TIME")
    private Date warnTime;

    /**
     * 处置结果
     */
    @Column(name = "DISP_RESULT")
    private String dispResult;

    /**
     * 处置时间
     */
    @Column(name = "DISP_TIME")
    private Date dispTime;

    /**
     * 业务流程（当前审批人）
     */
    @Column(name="BUSINESS_PROCESS")
    private String businessProcess;
    
    /**
     * 推送状态
     */
    @Column(name="PUSH_STATUS")
    private String pushStatus;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode == null ? null : ruleCode.trim();
    }

    public Short getTaskSeqno() {
        return taskSeqno;
    }

    public void setTaskSeqno(Short taskSeqno) {
        this.taskSeqno = taskSeqno;
    }

    public String getLesseeId() {
        return lesseeId;
    }

    public void setLesseeId(String lesseeId) {
        this.lesseeId = lesseeId == null ? null : lesseeId.trim();
    }

    public String getWarnDesc() {
        return warnDesc;
    }

    public void setWarnDesc(String warnDesc) {
        this.warnDesc = warnDesc == null ? null : warnDesc.trim();
    }

    public String getAssoType() {
        return assoType;
    }

    public void setAssoType(String assoType) {
        this.assoType = assoType == null ? null : assoType.trim();
    }

    public Date getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
    }

    public String getDispResult() {
        return dispResult;
    }

    public void setDispResult(String dispResult) {
        this.dispResult = dispResult == null ? null : dispResult.trim();
    }

    public Date getDispTime() {
        return dispTime;
    }

    public void setDispTime(Date dispTime) {
        this.dispTime = dispTime;
    }

    public String getAssoId() {
        return assoId;
    }

    public void setAssoId(String assoId) {
        this.assoId = assoId;
    }

    public String getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        this.businessProcess = businessProcess;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assoId == null) ? 0 : assoId.hashCode());
        result = prime * result + ((assoType == null) ? 0 : assoType.hashCode());
        result = prime * result + ((lesseeId == null) ? 0 : lesseeId.hashCode());
        result = prime * result + ((ruleCode == null) ? 0 : ruleCode.hashCode());
        result = prime * result + ((warnDesc == null) ? 0 : warnDesc.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AftWarnInfoDistinctEntity other = (AftWarnInfoDistinctEntity) obj;
        if (assoId == null) {
            if (other.assoId != null)
                return false;
        } else if (!assoId.equals(other.assoId))
            return false;
        if (assoType == null) {
            if (other.assoType != null)
                return false;
        } else if (!assoType.equals(other.assoType))
            return false;
        if (lesseeId == null) {
            if (other.lesseeId != null)
                return false;
        } else if (!lesseeId.equals(other.lesseeId))
            return false;
        if (ruleCode == null) {
            if (other.ruleCode != null)
                return false;
        } else if (!ruleCode.equals(other.ruleCode))
            return false;
        if (warnDesc == null) {
            if (other.warnDesc != null)
                return false;
        } else if (!warnDesc.equals(other.warnDesc))
            return false;
        return true;
    }
}