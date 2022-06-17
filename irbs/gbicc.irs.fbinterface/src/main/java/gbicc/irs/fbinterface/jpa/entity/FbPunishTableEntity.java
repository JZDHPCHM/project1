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
 * 行政处罚处罚表格实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Entity
@Table(name="FB_PUNISH_TABLE")
public class FbPunishTableEntity extends AuditorEntity implements Serializable{
    
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
     * 行政处罚ID
     */
    @Column(name="PUNISH_ID")
    private String punishId;
    /**
     * 修改日期
     */
    @Column(name="MODIFY_DATE")
    private String modifyDate;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 地区代码
     */
    @Column(name="AREA_CODE")
    private String areaCode;
    /**
     * 处罚事由
     */
    @Column(name="PUNISH_REASON")
    private String punishReason;
    /**
     * 处罚依据
     */
    @Column(name="PUNISH_BASIS")
    private String punishBasis;
    /**
     * 处罚内容
     */
    @Column(name="PUNISH_CONTENT")
    private String punishContent;
    /**
     * 处罚决定日期
     */
    @Column(name="PUNISH_DECISION_DATE")
    private String punishDecisionDate;
    /**
     * 处罚名称
     */
    @Column(name="PUNISH_NAME")
    private String punishName;
    /**
     * 处罚地方编码
     */
    @Column(name="PUNISH_AREA_CODE")
    private String punishAreaCode;
    /**
     * 处罚状态
     */
    @Column(name="PUNISH_STATUS")
    private String punishStatus;
    /**
     * 处罚类别
     */
    @Column(name="PUNISH_TYPE_ONE")
    private String punishTypeOne;
    /**
     * 处罚类别2
     */
    @Column(name="PUNISH_TYPE_TWO")
    private String punishTypeTwo;
    /**
     * 处罚部门
     */
    @Column(name="PUNISH_DEPART")
    private String punishDepart;
    /**
     * 当事人
     */
    @Column(name="PARTY")
    private String party;
    /**
     * 法定代表人
     */
    @Column(name="LEGAL_PERSON")
    private String legalPerson;
    /**
     * 相对人社会信用码
     */
    @Column(name="COUNTER_PART_CODE")
    private String counterPartCode;
    /**
     * 行政处罚决定书文号
     */
    @Column(name="PUNISH_NO")
    private String punishNo;

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

    public String getPunishId() {
        return punishId;
    }

    public void setPunishId(String punishId) {
        this.punishId = punishId == null ? null : punishId.trim();
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate == null ? null : modifyDate.trim();
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getPunishReason() {
        return punishReason;
    }

    public void setPunishReason(String punishReason) {
        this.punishReason = punishReason == null ? null : punishReason.trim();
    }

    public String getPunishBasis() {
        return punishBasis;
    }

    public void setPunishBasis(String punishBasis) {
        this.punishBasis = punishBasis == null ? null : punishBasis.trim();
    }

    public String getPunishContent() {
        return punishContent;
    }

    public void setPunishContent(String punishContent) {
        this.punishContent = punishContent == null ? null : punishContent.trim();
    }

    public String getPunishDecisionDate() {
        return punishDecisionDate;
    }

    public void setPunishDecisionDate(String punishDecisionDate) {
        this.punishDecisionDate = punishDecisionDate == null ? null : punishDecisionDate.trim();
    }

    public String getPunishName() {
        return punishName;
    }

    public void setPunishName(String punishName) {
        this.punishName = punishName == null ? null : punishName.trim();
    }

    public String getPunishAreaCode() {
        return punishAreaCode;
    }

    public void setPunishAreaCode(String punishAreaCode) {
        this.punishAreaCode = punishAreaCode == null ? null : punishAreaCode.trim();
    }

    public String getPunishStatus() {
        return punishStatus;
    }

    public void setPunishStatus(String punishStatus) {
        this.punishStatus = punishStatus == null ? null : punishStatus.trim();
    }

    public String getPunishTypeOne() {
        return punishTypeOne;
    }

    public void setPunishTypeOne(String punishTypeOne) {
        this.punishTypeOne = punishTypeOne == null ? null : punishTypeOne.trim();
    }

    public String getPunishTypeTwo() {
        return punishTypeTwo;
    }

    public void setPunishTypeTwo(String punishTypeTwo) {
        this.punishTypeTwo = punishTypeTwo == null ? null : punishTypeTwo.trim();
    }

    public String getPunishDepart() {
        return punishDepart;
    }

    public void setPunishDepart(String punishDepart) {
        this.punishDepart = punishDepart == null ? null : punishDepart.trim();
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party == null ? null : party.trim();
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    public String getCounterPartCode() {
        return counterPartCode;
    }

    public void setCounterPartCode(String counterPartCode) {
        this.counterPartCode = counterPartCode == null ? null : counterPartCode.trim();
    }

    public String getPunishNo() {
        return punishNo;
    }

    public void setPunishNo(String punishNo) {
        this.punishNo = punishNo == null ? null : punishNo.trim();
    }
}