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
 * 重大税收违法实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Entity
@Table(name="FB_MAJOR_TAX_VIOLATION")
public class FbMajorTaxViolationEntity extends AuditorEntity implements Serializable{
    
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
     * 主要违法事实
     */
    @Column(name="ILLEGAL_FACTS")
    private String illegalFacts;
    /**
     * 事件时间
     */
    @Column(name="FACTS_DATE")
    private String factsDate;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 案件上报期
     */
    @Column(name="CASE_REPORT_PERIOD")
    private String caseReportPeriod;
    /**
     * 案件性质
     */
    @Column(name="CASE_PROPERTIES")
    private String caseProperties;
    /**
     * 相关法律依据及税务处理处罚情况
     */
    @Column(name="PUNISH_DETAIL")
    private String punishDetail;
    /**
     * 纳税人名称
     */
    @Column(name="TAXPAYER_NAME")
    private String taxpayerName;
    /**
     * 纳税人识别码
     */
    @Column(name="TAXPAYER_CODE")
    private String taxpayerCode;
    /**
     * 组织机构代码
     */
    @Column(name="ORGANIZATION_CODE")
    private String organizationCode;

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

    public String getIllegalFacts() {
        return illegalFacts;
    }

    public void setIllegalFacts(String illegalFacts) {
        this.illegalFacts = illegalFacts == null ? null : illegalFacts.trim();
    }

    public String getFactsDate() {
        return factsDate;
    }

    public void setFactsDate(String factsDate) {
        this.factsDate = factsDate == null ? null : factsDate.trim();
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getCaseReportPeriod() {
        return caseReportPeriod;
    }

    public void setCaseReportPeriod(String caseReportPeriod) {
        this.caseReportPeriod = caseReportPeriod == null ? null : caseReportPeriod.trim();
    }

    public String getCaseProperties() {
        return caseProperties;
    }

    public void setCaseProperties(String caseProperties) {
        this.caseProperties = caseProperties == null ? null : caseProperties.trim();
    }

    public String getPunishDetail() {
        return punishDetail;
    }

    public void setPunishDetail(String punishDetail) {
        this.punishDetail = punishDetail == null ? null : punishDetail.trim();
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName == null ? null : taxpayerName.trim();
    }

    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode == null ? null : taxpayerCode.trim();
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode == null ? null : organizationCode.trim();
    }
}