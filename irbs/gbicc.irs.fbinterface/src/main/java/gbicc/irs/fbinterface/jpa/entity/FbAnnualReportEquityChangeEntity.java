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
 * 年报股权变更实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_EQUITY_CHANGE")
public class FbAnnualReportEquityChangeEntity extends AuditorEntity implements Serializable{
    
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
     * 变更前股权比例
     */
    @Column(name="EQUITY_CHANGE_BEFORE")
    private String equityChangeBefore;
    /**
     * 变更后股权比例
     */
    @Column(name="EQUITY_CHANGE_AFTER")
    private String equityChangeAfter;
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
     * 股权变更日期
     */
    @Column(name="CHANGE_DATE")
    private String changeDate;

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

    public String getEquityChangeBefore() {
        return equityChangeBefore;
    }

    public void setEquityChangeBefore(String equityChangeBefore) {
        this.equityChangeBefore = equityChangeBefore == null ? null : equityChangeBefore.trim();
    }

    public String getEquityChangeAfter() {
        return equityChangeAfter;
    }

    public void setEquityChangeAfter(String equityChangeAfter) {
        this.equityChangeAfter = equityChangeAfter == null ? null : equityChangeAfter.trim();
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

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate == null ? null : changeDate.trim();
    }
}