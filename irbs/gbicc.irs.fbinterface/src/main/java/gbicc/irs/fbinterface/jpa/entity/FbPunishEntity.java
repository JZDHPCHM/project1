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
 * 行政处罚实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Entity
@Table(name="FB_PUNISH")
public class FbPunishEntity extends AuditorEntity implements Serializable{
    
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
     * 事件时间
     */
    @Column(name="EVENT_DATE")
    private String eventDate;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 处罚决定书文号
     */
    @Column(name="PUNISH_NO")
    private String punishNo;
    /**
     * 处罚决定日期
     */
    @Column(name="PUNISH_DECISION_DATE")
    private String punishDecisionDate;
    /**
     * 来源
     */
    @Column(name="SOURCE")
    private String source;
    /**
     * 标题
     */
    @Column(name="TITLE")
    private String title;
    /**
     * 案件名称
     */
    @Column(name="CASE_NAME")
    private String caseName;
    /**
     * 正文
     */
    @Column(name="CONTENT")
    private String content;

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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate == null ? null : eventDate.trim();
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getPunishNo() {
        return punishNo;
    }

    public void setPunishNo(String punishNo) {
        this.punishNo = punishNo == null ? null : punishNo.trim();
    }

    public String getPunishDecisionDate() {
        return punishDecisionDate;
    }

    public void setPunishDecisionDate(String punishDecisionDate) {
        this.punishDecisionDate = punishDecisionDate == null ? null : punishDecisionDate.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName == null ? null : caseName.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}