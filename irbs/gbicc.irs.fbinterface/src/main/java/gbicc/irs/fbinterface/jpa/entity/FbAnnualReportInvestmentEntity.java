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
 * 年报对外投资信息
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_INVESTMENT")
public class FbAnnualReportInvestmentEntity extends AuditorEntity implements Serializable{
    
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
     * 投资设立企业或购买股权企业名称
     */
    @Column(name="ENTERPRISE_NAME")
    private String enterpriseName;
    /**
     * 注册号/统一社会信用代码
     */
    @Column(name="ENTERPRISE_CODE")
    private String enterpriseCode;

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

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode == null ? null : enterpriseCode.trim();
    }
}