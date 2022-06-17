package gbicc.irs.fbinterface.jpa.entity;

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
 * 年报股东及出资信息实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_SHAREHOLDER")
public class FbAnnualReportShareholderEntity extends AuditorEntity implements Serializable{
    
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
     * 年报ID
     */
    @Column(name="ANNUAL_REPORT_ID")
    private String annualReportId;
    /**
     * 实缴出资方式
     */
    @Column(name="PAID_TYPE")
    private String paidType;
    /**
     * 实缴出资时间
     */
    @Column(name="PAID_TIME")
    private String paidTime;
    /**
     * 实缴出资额（万元）
     */
    @Column(name="PAID_AMOUNT")
    private BigDecimal paidAmount;
    /**
     * 序号
     */
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;
    /**
     * 股东
     */
    @Column(name="SHAREHOLDER")
    private String shareholder;
    /**
     * 认缴出资方式
     */
    @Column(name="SUBSCRIBED_TYPE")
    private String subscribedType;
    /**
     * 认缴出资时间
     */
    @Column(name="SUBSCRIBED_TIME")
    private String subscribedTime;
    /**
     * 认缴出资额（万元）
     */
    @Column(name="SUBSCRIBED_AMOUNT")
    private BigDecimal subscribedAmount;

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

    public String getAnnualReportId() {
        return annualReportId;
    }

    public void setAnnualReportId(String annualReportId) {
        this.annualReportId = annualReportId == null ? null : annualReportId.trim();
    }

    public String getPaidType() {
        return paidType;
    }

    public void setPaidType(String paidType) {
        this.paidType = paidType == null ? null : paidType.trim();
    }

    public String getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(String paidTime) {
        this.paidTime = paidTime == null ? null : paidTime.trim();
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount == null ? null : paidAmount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder == null ? null : shareholder.trim();
    }

    public String getSubscribedType() {
        return subscribedType;
    }

    public void setSubscribedType(String subscribedType) {
        this.subscribedType = subscribedType == null ? null : subscribedType.trim();
    }

    public String getSubscribedTime() {
        return subscribedTime;
    }

    public void setSubscribedTime(String subscribedTime) {
        this.subscribedTime = subscribedTime == null ? null : subscribedTime.trim();
    }

    public BigDecimal getSubscribedAmount() {
        return subscribedAmount;
    }

    public void setSubscribedAmount(BigDecimal subscribedAmount) {
        this.subscribedAmount = subscribedAmount == null ? null : subscribedAmount;
    }
}