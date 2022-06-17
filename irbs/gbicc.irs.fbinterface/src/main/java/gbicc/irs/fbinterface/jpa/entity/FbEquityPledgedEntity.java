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
 * 股权出质实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月17日
 */
@Entity
@Table(name="FB_EQUITY_PLEDGED")
public class FbEquityPledgedEntity extends AuditorEntity implements Serializable{

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
     * 出质人
     */
    @Column(name="PLEDGOR")
    private String pledgor;
    /**
     * 出质标的
     */
    @Column(name="QUALITY_TARGET")
    private String qualityTarget;
    /**
     * 出质股权数额
     */
    @Column(name="PLEDGED_EQUITY_AMOUNT")
    private String pledgedEquityAmount;
    /**
     * 发布时间
     */
    @Column(name="RELEASE_TIME")
    private String releaseTime;
    /**
     * 备注
     */
    @Column(name="REMARK")
    private String remark;
    /**
     * 注销原因
     */
    @Column(name="CANCELLATION_REASON")
    private String cancellationReason;
    /**
     * 注销日期
     */
    @Column(name="CANCELLATION_DATE")
    private String cancellationDate;
    /**
     * 状态
     */
    @Column(name="STATUS")
    private String status;
    /**
     * 登记日期
     */
    @Column(name="REGISTRATION_DATE")
    private String registrationDate;
    /**
     * 登记种类
     */
    @Column(name="REGISTRATION_TYPES")
    private String registrationTypes;
    /**
     * 登记编号
     */
    @Column(name="REGISTRATION_NUMBER")
    private String registrationNumber;
    /**
     * 质权人
     */
    @Column(name="PLEDGEE")
    private String pledgee;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public String getPledgor() {
        return pledgor;
    }
    public void setPledgor(String pledgor) {
        this.pledgor = pledgor;
    }
    public String getQualityTarget() {
        return qualityTarget;
    }
    public void setQualityTarget(String qualityTarget) {
        this.qualityTarget = qualityTarget;
    }
    public String getPledgedEquityAmount() {
        return pledgedEquityAmount;
    }
    public void setPledgedEquityAmount(String pledgedEquityAmount) {
        this.pledgedEquityAmount = pledgedEquityAmount;
    }
    public String getReleaseTime() {
        return releaseTime;
    }
    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCancellationReason() {
        return cancellationReason;
    }
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    public String getCancellationDate() {
        return cancellationDate;
    }
    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    public String getRegistrationTypes() {
        return registrationTypes;
    }
    public void setRegistrationTypes(String registrationTypes) {
        this.registrationTypes = registrationTypes;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    public String getPledgee() {
        return pledgee;
    }
    public void setPledgee(String pledgee) {
        this.pledgee = pledgee;
    }
    
}
