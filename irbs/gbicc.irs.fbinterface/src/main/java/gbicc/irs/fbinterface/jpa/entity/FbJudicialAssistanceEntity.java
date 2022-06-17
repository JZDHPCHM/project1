package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 司法协助实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Entity
@Table(name="FB_JUDICIAL_ASSISTANCE")
public class FbJudicialAssistanceEntity extends AuditorEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid2")
    @Column(name="ID",length=36)
    private String id;
    /**
     * 统一社会信用代码、注册号
     */
    @Column(name="COMPANY_ID")
    private String companyId;
    /**
     * 公示日期
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 冻结期限自
     */
    @Column(name="FREEZE_PERIOD_FROM")
    private String freezePeriodFrom;
    /**
     * 冻结期限至
     */
    @Column(name="FREEZE_PERIOD_TO")
    private String freezePeriodTo;
    /**
     * 受让人
     */
    @Column(name="RECEIVE_PARTY")
    private String receiveParty;
    /**
     * 执行事项
     */
    @Column(name="EXECUTION_MATTERS")
    private String executionMatters;
    /**
     * 执行法院
     */
    @Column(name="EXECUTION_COURT")
    private String executionCourt;
    /**
     * 执行裁定书文号
     */
    @Column(name="EXECUTION_ORDER_NUMBER")
    private String executionOrderNumber;
    /**
     * 执行通知书文号
     */
    @Column(name="ENFORCEMENT_NOTICE_NUMBER")
    private String enforcementNoticeNumber;
    /**
     * 续行冻结期限自
     */
    @Column(name="RENEW_FREEZE_PERIOD_FROM")
    private String renewFreezePeriodFrom;
    /**
     * 续行冻结期限至
     */
    @Column(name="RENEW_FREEZE_PERIOD_TO")
    private String renewFreezePeriodTo;
    /**
     * 股权所在企业名称
     */
    @Column(name="EQUITY_ENTERPRISE_NAME")
    private String equityEnterpriseName;
    /**
     * 股权数额
     */
    @Column(name="EQUITY_AMOUNT")
    private String equityAmount;
    /**
     * 被执行人
     */
    @Column(name="EXECUTE_PERSON")
    private String executePerson;
    /**
     * 被执行人证件种类
     */
    @Column(name="CERTIFICATE_TYPE")
    private String certificateType;
    /**
     * 解除冻结期限
     */
    @Column(name="LIFT_FREEZE_PERIOD")
    private String liftFreezePeriod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getFreezePeriodFrom() {
        return freezePeriodFrom;
    }

    public void setFreezePeriodFrom(String freezePeriodFrom) {
        this.freezePeriodFrom = freezePeriodFrom == null ? null : freezePeriodFrom.trim();
    }

    public String getFreezePeriodTo() {
        return freezePeriodTo;
    }

    public void setFreezePeriodTo(String freezePeriodTo) {
        this.freezePeriodTo = freezePeriodTo == null ? null : freezePeriodTo.trim();
    }

    public String getReceiveParty() {
        return receiveParty;
    }

    public void setReceiveParty(String receiveParty) {
        this.receiveParty = receiveParty == null ? null : receiveParty.trim();
    }

    public String getExecutionMatters() {
        return executionMatters;
    }

    public void setExecutionMatters(String executionMatters) {
        this.executionMatters = executionMatters == null ? null : executionMatters.trim();
    }

    public String getExecutionCourt() {
        return executionCourt;
    }

    public void setExecutionCourt(String executionCourt) {
        this.executionCourt = executionCourt == null ? null : executionCourt.trim();
    }

    public String getExecutionOrderNumber() {
        return executionOrderNumber;
    }

    public void setExecutionOrderNumber(String executionOrderNumber) {
        this.executionOrderNumber = executionOrderNumber == null ? null : executionOrderNumber.trim();
    }

    public String getEnforcementNoticeNumber() {
        return enforcementNoticeNumber;
    }

    public void setEnforcementNoticeNumber(String enforcementNoticeNumber) {
        this.enforcementNoticeNumber = enforcementNoticeNumber == null ? null : enforcementNoticeNumber.trim();
    }

    public String getRenewFreezePeriodFrom() {
        return renewFreezePeriodFrom;
    }

    public void setRenewFreezePeriodFrom(String renewFreezePeriodFrom) {
        this.renewFreezePeriodFrom = renewFreezePeriodFrom == null ? null : renewFreezePeriodFrom.trim();
    }

    public String getRenewFreezePeriodTo() {
        return renewFreezePeriodTo;
    }

    public void setRenewFreezePeriodTo(String renewFreezePeriodTo) {
        this.renewFreezePeriodTo = renewFreezePeriodTo == null ? null : renewFreezePeriodTo.trim();
    }

    public String getEquityEnterpriseName() {
        return equityEnterpriseName;
    }

    public void setEquityEnterpriseName(String equityEnterpriseName) {
        this.equityEnterpriseName = equityEnterpriseName == null ? null : equityEnterpriseName.trim();
    }

    public String getEquityAmount() {
        return equityAmount;
    }

    public void setEquityAmount(String equityAmount) {
        this.equityAmount = equityAmount == null ? null : equityAmount.trim();
    }

    public String getExecutePerson() {
        return executePerson;
    }

    public void setExecutePerson(String executePerson) {
        this.executePerson = executePerson == null ? null : executePerson.trim();
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType == null ? null : certificateType.trim();
    }

    public String getLiftFreezePeriod() {
        return liftFreezePeriod;
    }

    public void setLiftFreezePeriod(String liftFreezePeriod) {
        this.liftFreezePeriod = liftFreezePeriod == null ? null : liftFreezePeriod.trim();
    }
}