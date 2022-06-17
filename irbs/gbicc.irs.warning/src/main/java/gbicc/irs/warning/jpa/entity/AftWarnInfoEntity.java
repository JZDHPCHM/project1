package gbicc.irs.warning.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 预警信息表
 * 
 * @author FC
 * @version v1.0 2019年9月17日
 */
@Entity
@Table(name = "T_AFT_WARN_INFO")
public class AftWarnInfoEntity implements Serializable {

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
    private String dispTime;
    
    /**
     * 业务流程（当前审批人）
     */
    @Column(name="BUSINESS_PROCESS")
    private String businessProcess;

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

    public String getDispTime() {
        return dispTime;
    }

    public void setDispTime(String dispTime) {
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
    
}