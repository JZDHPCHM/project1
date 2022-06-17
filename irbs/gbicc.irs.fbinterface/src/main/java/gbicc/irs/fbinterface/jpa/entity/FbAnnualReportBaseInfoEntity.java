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
 * 年报基本信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_BASE_INFO")
public class FbAnnualReportBaseInfoEntity extends AuditorEntity implements Serializable{
    
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
     * 从业人数
     */
    @Column(name="EMPLOYEE_NUMBER")
    private String employeeNumber;
    /**
     * 企业主营业务活动
     */
    @Column(name="MAIN_ACTIVITIES")
    private String mainActivities;
    /**
     * 企业名称
     */
    @Column(name="ENTERPRISE_NAME")
    private String enterpriseName;
    /**
     * 企业控股情况
     */
    @Column(name="ENTERPRISE_HOLDING")
    private String enterpriseHolding;
    /**
     * 企业电子邮箱
     */
    @Column(name="ENTERPRISE_EMAIL")
    private String enterpriseEmail;
    /**
     * 企业经营状态
     */
    @Column(name="ENTERPRISE_OPERATE_STATUS")
    private String enterpriseOperateStatus;
    /**
     * 企业联系电话
     */
    @Column(name="ENTERPRISE_TELEPHONE")
    private String enterpriseTelephone;
    /**
     * 企业通信地址
     */
    @Column(name="ENTERPRISE_ADDRESS")
    private String enterpriseAddress;
    /**
     * 其中女性从业人数
     */
    @Column(name="EMPLOYEE_FEMIAL_NUMBER")
    private String employeeFemialNumber;
    /**
     * 是否有对外提供担保信息
     */
    @Column(name="EXTERNAL_GUARANTEE")
    private String externalGuarantee;
    /**
     * 是否有投资信息或购买其他公司股权
     */
    @Column(name="INVESTMENT")
    private String investment;
    /**
     * 是否有网站或网店
     */
    @Column(name="WEBSITE")
    private String website;
    /**
     * 有限责任公司本年度是否发生股东股权转让
     */
    @Column(name="EQUITY_TRANSFER")
    private String equityTransfer;
    /**
     * 邮政编码
     */
    @Column(name="ZIP_CODE")
    private String zipCode;

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

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber == null ? null : employeeNumber.trim();
    }

    public String getMainActivities() {
        return mainActivities;
    }

    public void setMainActivities(String mainActivities) {
        this.mainActivities = mainActivities == null ? null : mainActivities.trim();
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public String getEnterpriseHolding() {
        return enterpriseHolding;
    }

    public void setEnterpriseHolding(String enterpriseHolding) {
        this.enterpriseHolding = enterpriseHolding == null ? null : enterpriseHolding.trim();
    }

    public String getEnterpriseEmail() {
        return enterpriseEmail;
    }

    public void setEnterpriseEmail(String enterpriseEmail) {
        this.enterpriseEmail = enterpriseEmail == null ? null : enterpriseEmail.trim();
    }

    public String getEnterpriseOperateStatus() {
        return enterpriseOperateStatus;
    }

    public void setEnterpriseOperateStatus(String enterpriseOperateStatus) {
        this.enterpriseOperateStatus = enterpriseOperateStatus == null ? null : enterpriseOperateStatus.trim();
    }

    public String getEnterpriseTelephone() {
        return enterpriseTelephone;
    }

    public void setEnterpriseTelephone(String enterpriseTelephone) {
        this.enterpriseTelephone = enterpriseTelephone == null ? null : enterpriseTelephone.trim();
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress == null ? null : enterpriseAddress.trim();
    }

    public String getEmployeeFemialNumber() {
        return employeeFemialNumber;
    }

    public void setEmployeeFemialNumber(String employeeFemialNumber) {
        this.employeeFemialNumber = employeeFemialNumber == null ? null : employeeFemialNumber.trim();
    }

    public String getExternalGuarantee() {
        return externalGuarantee;
    }

    public void setExternalGuarantee(String externalGuarantee) {
        this.externalGuarantee = externalGuarantee == null ? null : externalGuarantee.trim();
    }

    public String getInvestment() {
        return investment;
    }

    public void setInvestment(String investment) {
        this.investment = investment == null ? null : investment.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getEquityTransfer() {
        return equityTransfer;
    }

    public void setEquityTransfer(String equityTransfer) {
        this.equityTransfer = equityTransfer == null ? null : equityTransfer.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }
}