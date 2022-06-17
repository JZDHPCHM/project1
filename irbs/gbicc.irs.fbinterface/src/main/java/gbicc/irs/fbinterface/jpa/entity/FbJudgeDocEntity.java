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
 * 裁判文书实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Entity
@Table(name="FB_JUDGE_DOC")
public class FbJudgeDocEntity extends AuditorEntity implements Serializable{
    
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
     * URL
     */
    @Column(name="URL")
    private String url;
    /**
     * 不公开理由
     */
    @Column(name="NOT_OPEN_REASON")
    private String notOpenReason;
    /**
     * 判决总金额
     */
    @Column(name="SENTENCE_TOTAL_AMOUNT")
    private String sentenceTotalAmount;
    /**
     * 判决时间
     */
    @Column(name="SENTENCE_DATE")
    private String sentenceDate;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 文书类型
     */
    @Column(name="DOCUMENT_TYPE")
    private String documentType;
    /**
     * 标题
     */
    @Column(name="TITLE")
    private String title;
    /**
     * 案件类别
     */
    @Column(name="CASE_TYPE")
    private String caseType;
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
     * 法官
     */
    @Column(name="JUDGE")
    private String judge;
    /**
     * 法院
     */
    @Column(name="COURT")
    private String court;
    /**
     * 诉讼费
     */
    @Column(name="LEGAL_FEE")
    private String legalFee;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getNotOpenReason() {
        return notOpenReason;
    }

    public void setNotOpenReason(String notOpenReason) {
        this.notOpenReason = notOpenReason == null ? null : notOpenReason.trim();
    }

    public String getSentenceTotalAmount() {
        return sentenceTotalAmount;
    }

    public void setSentenceTotalAmount(String sentenceTotalAmount) {
        this.sentenceTotalAmount = sentenceTotalAmount == null ? null : sentenceTotalAmount.trim();
    }

    public String getSentenceDate() {
        return sentenceDate;
    }

    public void setSentenceDate(String sentenceDate) {
        this.sentenceDate = sentenceDate == null ? null : sentenceDate.trim();
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType == null ? null : documentType.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType == null ? null : caseType.trim();
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

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge == null ? null : judge.trim();
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court == null ? null : court.trim();
    }

    public String getLegalFee() {
        return legalFee;
    }

    public void setLegalFee(String legalFee) {
        this.legalFee = legalFee == null ? null : legalFee.trim();
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate == null ? null : settlementDate.trim();
    }
}