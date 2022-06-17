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
 * 审判流程实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Entity
@Table(name="FB_TRIAL_PROCESS")
public class FbTrialProcessEntity extends AuditorEntity implements Serializable{
    
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
     * 主审法官
     */
    @Column(name="PRESIDING_JUDGE")
    private String presidingJudge;
    /**
     * 公告人
     */
    @Column(name="NOTICE_PEOPLE")
    private String noticePeople;
    /**
     * 公告类型
     */
    @Column(name="NOTICE_TYPE")
    private String noticeType;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 合议庭成员
     */
    @Column(name="COLLEGIAL_MEMBER")
    private String collegialMember;
    /**
     * 字号
     */
    @Column(name="WORD_NUMBER")
    private String wordNumber;
    /**
     * 审判程序
     */
    @Column(name="TRIAL_PROCEDURE")
    private String trialProcedure;
    /**
     * 审判长
     */
    @Column(name="TRIAL_JUDGE")
    private String trialJudge;
    /**
     * 审判日期
     */
    @Column(name="TRIAL_DATE")
    private String trialDate;
    /**
     * 归档日期
     */
    @Column(name="FILING_DATE")
    private String filingDate;
    /**
     * 承办部门
     */
    @Column(name="UNDERTAKE_DEPART")
    private String undertakeDepart;
    /**
     * 标的金额
     */
    @Column(name="UNDERLY_AMOUNT")
    private BigDecimal underlyAmount;
    /**
     * 案件类别
     */
    @Column(name="CASE_TYPE")
    private String caseType;
    /**
     * 案件进度
     */
    @Column(name="CASE_PROGRESS")
    private String caseProgress;
    /**
     * 案号
     */
    @Column(name="CASE_NO")
    private String caseNo;
    /**
     * 案由
     */
    @Column(name="CASE_REASON")
    private String caseReason;
    /**
     * 立案日期
     */
    @Column(name="PUT_ON_RECORD_DATE")
    private String putOnRecordDate;
    /**
     * 立案时间
     */
    @Column(name="PUT_ON_RECORD_TIME")
    private String putOnRecordTime;
    /**
     * 结案方式
     */
    @Column(name="SETTLEMENT_WAY")
    private String settlementWay;
    /**
     * 结案日期
     */
    @Column(name="SETTLEMENT_DATE")
    private String settlementDate;

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

    public String getPresidingJudge() {
        return presidingJudge;
    }

    public void setPresidingJudge(String presidingJudge) {
        this.presidingJudge = presidingJudge == null ? null : presidingJudge.trim();
    }

    public String getNoticePeople() {
        return noticePeople;
    }

    public void setNoticePeople(String noticePeople) {
        this.noticePeople = noticePeople == null ? null : noticePeople.trim();
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType == null ? null : noticeType.trim();
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getCollegialMember() {
        return collegialMember;
    }

    public void setCollegialMember(String collegialMember) {
        this.collegialMember = collegialMember == null ? null : collegialMember.trim();
    }

    public String getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(String wordNumber) {
        this.wordNumber = wordNumber == null ? null : wordNumber.trim();
    }

    public String getTrialProcedure() {
        return trialProcedure;
    }

    public void setTrialProcedure(String trialProcedure) {
        this.trialProcedure = trialProcedure == null ? null : trialProcedure.trim();
    }

    public String getTrialJudge() {
        return trialJudge;
    }

    public void setTrialJudge(String trialJudge) {
        this.trialJudge = trialJudge == null ? null : trialJudge.trim();
    }

    public String getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(String trialDate) {
        this.trialDate = trialDate == null ? null : trialDate.trim();
    }

    public String getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(String filingDate) {
        this.filingDate = filingDate == null ? null : filingDate.trim();
    }

    public String getUndertakeDepart() {
        return undertakeDepart;
    }

    public void setUndertakeDepart(String undertakeDepart) {
        this.undertakeDepart = undertakeDepart == null ? null : undertakeDepart.trim();
    }

    public BigDecimal getUnderlyAmount() {
        return underlyAmount;
    }

    public void setUnderlyAmount(BigDecimal underlyAmount) {
        this.underlyAmount = underlyAmount == null ? null : underlyAmount;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType == null ? null : caseType.trim();
    }

    public String getCaseProgress() {
        return caseProgress;
    }

    public void setCaseProgress(String caseProgress) {
        this.caseProgress = caseProgress == null ? null : caseProgress.trim();
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo == null ? null : caseNo.trim();
    }

    public String getCaseReason() {
        return caseReason;
    }

    public void setCaseReason(String caseReason) {
        this.caseReason = caseReason == null ? null : caseReason.trim();
    }

    public String getPutOnRecordDate() {
        return putOnRecordDate;
    }

    public void setPutOnRecordDate(String putOnRecordDate) {
        this.putOnRecordDate = putOnRecordDate == null ? null : putOnRecordDate.trim();
    }

    public String getPutOnRecordTime() {
        return putOnRecordTime;
    }

    public void setPutOnRecordTime(String putOnRecordTime) {
        this.putOnRecordTime = putOnRecordTime == null ? null : putOnRecordTime.trim();
    }

    public String getSettlementWay() {
        return settlementWay;
    }

    public void setSettlementWay(String settlementWay) {
        this.settlementWay = settlementWay == null ? null : settlementWay.trim();
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate == null ? null : settlementDate.trim();
    }
}