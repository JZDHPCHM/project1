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
 * 失信被执行人实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Entity
@Table(name="FB_FAITH_EXECUTE_PERSON")
public class FbFaithExecutePersonEntity extends AuditorEntity implements Serializable{

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
     * 做出执行依据单位
     */
    @Column(name="DECISION_AUTHORITY")
    private String decisionAuthority;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 失信被执行人行为具体情形
     */
    @Column(name="EXECUTE_DETAIL")
    private String executeDetail;
    /**
     * 已履行
     */
    @Column(name="HAVE_PERFORMED")
    private String havePerformed;
    /**
     * 年龄
     */
    @Column(name="AGE")
    private Integer age;
    /**
     * 执行依据文号
     */
    @Column(name="EXECUTION_NUMBER")
    private String executionNumber;
    /**
     * 执行法院
     */
    @Column(name="EXECUTION_COURT")
    private String executionCourt;
    /**
     * 未履行
     */
    @Column(name="NO_PERFORMED")
    private String noPerformed;
    /**
     * 案号
     */
    @Column(name="CASE_NUMBER")
    private String caseNumber;
    /**
     * 法定代表人或者负责人姓名
     */
    @Column(name="CHARGE_PERSON")
    private String chargePerson;
    /**
     * 生效法律文书确定的义务
     */
    @Column(name="EFFECT_OBLIGATIONS")
    private String effectObligations;
    /**
     * 省份
     */
    @Column(name="PROVINCE")
    private String province;
    /**
     * 立案时间
     */
    @Column(name="CASE_DATE")
    private String caseDate;
    /**
     * 被执行人姓名/名称
     */
    @Column(name="EXECUTION_NAME")
    private String executionName;
    /**
     * 被执行人的履行情况
     */
    @Column(name="EXECUTION_RESULT")
    private String executionResult;

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

    public String getDecisionAuthority() {
        return decisionAuthority;
    }

    public void setDecisionAuthority(String decisionAuthority) {
        this.decisionAuthority = decisionAuthority == null ? null : decisionAuthority.trim();
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getExecuteDetail() {
        return executeDetail;
    }

    public void setExecuteDetail(String executeDetail) {
        this.executeDetail = executeDetail == null ? null : executeDetail.trim();
    }

    public String getHavePerformed() {
        return havePerformed;
    }

    public void setHavePerformed(String havePerformed) {
        this.havePerformed = havePerformed == null ? null : havePerformed.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age == null ? null : age;
    }

    public String getExecutionNumber() {
        return executionNumber;
    }

    public void setExecutionNumber(String executionNumber) {
        this.executionNumber = executionNumber == null ? null : executionNumber.trim();
    }

    public String getExecutionCourt() {
        return executionCourt;
    }

    public void setExecutionCourt(String executionCourt) {
        this.executionCourt = executionCourt == null ? null : executionCourt.trim();
    }

    public String getNoPerformed() {
        return noPerformed;
    }

    public void setNoPerformed(String noPerformed) {
        this.noPerformed = noPerformed == null ? null : noPerformed.trim();
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber == null ? null : caseNumber.trim();
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson == null ? null : chargePerson.trim();
    }

    public String getEffectObligations() {
        return effectObligations;
    }

    public void setEffectObligations(String effectObligations) {
        this.effectObligations = effectObligations == null ? null : effectObligations.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate == null ? null : caseDate.trim();
    }

    public String getExecutionName() {
        return executionName;
    }

    public void setExecutionName(String executionName) {
        this.executionName = executionName == null ? null : executionName.trim();
    }

    public String getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(String executionResult) {
        this.executionResult = executionResult == null ? null : executionResult.trim();
    }
}