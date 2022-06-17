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
 * 涉诉公告实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Entity
@Table(name="FB_LITIGATION_NOTICE")
public class FbLitigationNoticeEntity extends AuditorEntity implements Serializable{
    
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
     * 公告人
     */
    @Column(name="NOTICE_PEOPLE")
    private String noticePeople;
    /**
     * 公告时间
     */
    @Column(name="NOTICE_DATE")
    private String noticeDate;
    /**
     * 公告类型
     */
    @Column(name="NOTICE_TYPE")
    private String noticeType;
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

    public String getNoticePeople() {
        return noticePeople;
    }

    public void setNoticePeople(String noticePeople) {
        this.noticePeople = noticePeople == null ? null : noticePeople.trim();
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate == null ? null : noticeDate.trim();
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType == null ? null : noticeType.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}