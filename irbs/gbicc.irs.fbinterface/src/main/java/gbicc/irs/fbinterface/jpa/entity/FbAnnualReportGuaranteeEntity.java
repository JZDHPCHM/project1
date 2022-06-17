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
 * 年报对外提供保证担保实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_GUARANTEE")
public class FbAnnualReportGuaranteeEntity extends AuditorEntity implements Serializable{
    
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
     * 主债权数额
     */
    @Column(name="PRINCIPAL_CLAIM_AMOUNT")
    private String principalClaimAmount;
    /**
     * 主债权种类
     */
    @Column(name="PRINCIPAL_CLAIM_TYPE")
    private String principalClaimType;
    /**
     * 保证的方式
     */
    @Column(name="GUARANTEE_TYPE")
    private String guaranteeType;
    /**
     * 保证的期间
     */
    @Column(name="GUARANTEE_DUR_PERIOD")
    private String guaranteeDurPeriod;
    /**
     * 债务人
     */
    @Column(name="DEBTOR")
    private String debtor;
    /**
     * 债权人
     */
    @Column(name="CREDITORS")
    private String creditors;
    /**
     * 履行债务的期限
     */
    @Column(name="PERFORMANCE_LIMIT")
    private String performanceLimit;

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

    public String getPrincipalClaimAmount() {
        return principalClaimAmount;
    }

    public void setPrincipalClaimAmount(String principalClaimAmount) {
        this.principalClaimAmount = principalClaimAmount == null ? null : principalClaimAmount.trim();
    }

    public String getPrincipalClaimType() {
        return principalClaimType;
    }

    public void setPrincipalClaimType(String principalClaimType) {
        this.principalClaimType = principalClaimType == null ? null : principalClaimType.trim();
    }

    public String getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType == null ? null : guaranteeType.trim();
    }

    public String getGuaranteeDurPeriod() {
        return guaranteeDurPeriod;
    }

    public void setGuaranteeDurPeriod(String guaranteeDurPeriod) {
        this.guaranteeDurPeriod = guaranteeDurPeriod == null ? null : guaranteeDurPeriod.trim();
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor == null ? null : debtor.trim();
    }

    public String getCreditors() {
        return creditors;
    }

    public void setCreditors(String creditors) {
        this.creditors = creditors == null ? null : creditors.trim();
    }

    public String getPerformanceLimit() {
        return performanceLimit;
    }

    public void setPerformanceLimit(String performanceLimit) {
        this.performanceLimit = performanceLimit == null ? null : performanceLimit.trim();
    }
}