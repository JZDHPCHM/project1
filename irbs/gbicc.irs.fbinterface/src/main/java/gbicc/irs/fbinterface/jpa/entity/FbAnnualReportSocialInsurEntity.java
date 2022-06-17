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
 * 年报社保信息实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_SOCIAL_INSUR")
public class FbAnnualReportSocialInsurEntity extends AuditorEntity implements Serializable{
    
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
     * 单位参加城镇职工基本养老保险累计欠缴金额
     */
    @Column(name="ENDOWMENT_UNPAID_AMOUNT")
    private String endowmentUnpaidAmount;
    /**
     * 单位参加城镇职工基本养老保险缴费基数
     */
    @Column(name="ENDOWMENT_EXPENDS_BASE")
    private String endowmentExpendsBase;
    /**
     * 单位参加失业保险累计欠缴金额
     */
    @Column(name="UNEMPLOY_UNPAID_AMOUNT")
    private String unemployUnpaidAmount;
    /**
     * 单位参加失业保险缴费基数
     */
    @Column(name="UNEMPLOY_EXPENDS_BASE")
    private String unemployExpendsBase;
    /**
     * 单位参加工伤保险累计欠缴金额
     */
    @Column(name="INJURY_UNPAID_AMOUNT")
    private String injuryUnpaidAmount;
    /**
     * 单位参加工伤保险缴费基数
     */
    @Column(name="INJURY_EXPENDS_BASE")
    private String injuryExpendsBase;
    /**
     * 单位参加生育保险累计欠缴金额
     */
    @Column(name="BIRTH_UNPAID_AMOUNT")
    private String birthUnpaidAmount;
    /**
     * 单位参加生育保险缴费基数
     */
    @Column(name="BIRTH_EXPENDS_BASE")
    private String birthExpendsBase;
    /**
     * 单位参加职工基本医疗保险累计欠缴金额
     */
    @Column(name="MEDICAL_UNPAID_AMOUNT")
    private String medicalUnpaidAmount;
    /**
     * 单位参加职工基本医疗保险缴费基数
     */
    @Column(name="MEDICAL_EXPENDS_BASE")
    private String medicalExpendsBase;
    /**
     * 参加城镇职工基本养老保险本期实际缴费基数
     */
    @Column(name="ENDOWMENT_PAYED_BASE")
    private String endowmentPayedBase;
    /**
     * 参加失业保险本期实际缴费基数
     */
    @Column(name="UNEMPLOY_PAYED_BASE")
    private String unemployPayedBase;
    /**
     * 参加工伤保险本期实际缴费基数
     */
    @Column(name="INJURY_PAYED_BASE")
    private String injuryPayedBase;
    /**
     * 参加生育保险本期实际缴费基数
     */
    @Column(name="BIRTH_PAYED_BASE")
    private String birthPayedBase;
    /**
     * 参加职工基本医疗保险本期实际缴费基数
     */
    @Column(name="MEDICAL_PAYED_BASE")
    private String medicalPayedBase;
    /**
     * 城镇职工基本养老保险
     */
    @Column(name="ENDOWMENT_INSURANCE")
    private String endowmentInsurance;
    /**
     * 失业保险
     */
    @Column(name="UNEMPLOY_INSURANCE")
    private String unemployInsurance;
    /**
     * 工伤保险
     */
    @Column(name="INJURY_INSURANCE")
    private String injuryInsurance;
    /**
     * 生育保险
     */
    @Column(name="BIRTH_INSURANCE")
    private String birthInsurance;
    /**
     * 职工基本医疗保险
     */
    @Column(name="MEDICAL_INSURANCE")
    private String medicalInsurance;

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

    public String getEndowmentUnpaidAmount() {
        return endowmentUnpaidAmount;
    }

    public void setEndowmentUnpaidAmount(String endowmentUnpaidAmount) {
        this.endowmentUnpaidAmount = endowmentUnpaidAmount == null ? null : endowmentUnpaidAmount.trim();
    }

    public String getEndowmentExpendsBase() {
        return endowmentExpendsBase;
    }

    public void setEndowmentExpendsBase(String endowmentExpendsBase) {
        this.endowmentExpendsBase = endowmentExpendsBase == null ? null : endowmentExpendsBase.trim();
    }

    public String getUnemployUnpaidAmount() {
        return unemployUnpaidAmount;
    }

    public void setUnemployUnpaidAmount(String unemployUnpaidAmount) {
        this.unemployUnpaidAmount = unemployUnpaidAmount == null ? null : unemployUnpaidAmount.trim();
    }

    public String getUnemployExpendsBase() {
        return unemployExpendsBase;
    }

    public void setUnemployExpendsBase(String unemployExpendsBase) {
        this.unemployExpendsBase = unemployExpendsBase == null ? null : unemployExpendsBase.trim();
    }

    public String getInjuryUnpaidAmount() {
        return injuryUnpaidAmount;
    }

    public void setInjuryUnpaidAmount(String injuryUnpaidAmount) {
        this.injuryUnpaidAmount = injuryUnpaidAmount == null ? null : injuryUnpaidAmount.trim();
    }

    public String getInjuryExpendsBase() {
        return injuryExpendsBase;
    }

    public void setInjuryExpendsBase(String injuryExpendsBase) {
        this.injuryExpendsBase = injuryExpendsBase == null ? null : injuryExpendsBase.trim();
    }

    public String getBirthUnpaidAmount() {
        return birthUnpaidAmount;
    }

    public void setBirthUnpaidAmount(String birthUnpaidAmount) {
        this.birthUnpaidAmount = birthUnpaidAmount == null ? null : birthUnpaidAmount.trim();
    }

    public String getBirthExpendsBase() {
        return birthExpendsBase;
    }

    public void setBirthExpendsBase(String birthExpendsBase) {
        this.birthExpendsBase = birthExpendsBase == null ? null : birthExpendsBase.trim();
    }

    public String getMedicalUnpaidAmount() {
        return medicalUnpaidAmount;
    }

    public void setMedicalUnpaidAmount(String medicalUnpaidAmount) {
        this.medicalUnpaidAmount = medicalUnpaidAmount == null ? null : medicalUnpaidAmount.trim();
    }

    public String getMedicalExpendsBase() {
        return medicalExpendsBase;
    }

    public void setMedicalExpendsBase(String medicalExpendsBase) {
        this.medicalExpendsBase = medicalExpendsBase == null ? null : medicalExpendsBase.trim();
    }

    public String getEndowmentPayedBase() {
        return endowmentPayedBase;
    }

    public void setEndowmentPayedBase(String endowmentPayedBase) {
        this.endowmentPayedBase = endowmentPayedBase == null ? null : endowmentPayedBase.trim();
    }

    public String getUnemployPayedBase() {
        return unemployPayedBase;
    }

    public void setUnemployPayedBase(String unemployPayedBase) {
        this.unemployPayedBase = unemployPayedBase == null ? null : unemployPayedBase.trim();
    }

    public String getInjuryPayedBase() {
        return injuryPayedBase;
    }

    public void setInjuryPayedBase(String injuryPayedBase) {
        this.injuryPayedBase = injuryPayedBase == null ? null : injuryPayedBase.trim();
    }

    public String getBirthPayedBase() {
        return birthPayedBase;
    }

    public void setBirthPayedBase(String birthPayedBase) {
        this.birthPayedBase = birthPayedBase == null ? null : birthPayedBase.trim();
    }

    public String getMedicalPayedBase() {
        return medicalPayedBase;
    }

    public void setMedicalPayedBase(String medicalPayedBase) {
        this.medicalPayedBase = medicalPayedBase == null ? null : medicalPayedBase.trim();
    }

    public String getEndowmentInsurance() {
        return endowmentInsurance;
    }

    public void setEndowmentInsurance(String endowmentInsurance) {
        this.endowmentInsurance = endowmentInsurance == null ? null : endowmentInsurance.trim();
    }

    public String getUnemployInsurance() {
        return unemployInsurance;
    }

    public void setUnemployInsurance(String unemployInsurance) {
        this.unemployInsurance = unemployInsurance == null ? null : unemployInsurance.trim();
    }

    public String getInjuryInsurance() {
        return injuryInsurance;
    }

    public void setInjuryInsurance(String injuryInsurance) {
        this.injuryInsurance = injuryInsurance == null ? null : injuryInsurance.trim();
    }

    public String getBirthInsurance() {
        return birthInsurance;
    }

    public void setBirthInsurance(String birthInsurance) {
        this.birthInsurance = birthInsurance == null ? null : birthInsurance.trim();
    }

    public String getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(String medicalInsurance) {
        this.medicalInsurance = medicalInsurance == null ? null : medicalInsurance.trim();
    }
}