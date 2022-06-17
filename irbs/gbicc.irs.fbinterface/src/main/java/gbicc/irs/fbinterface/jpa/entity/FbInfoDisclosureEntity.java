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
 * 信息披露实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Entity
@Table(name="FB_INFO_DISCLOSURE")
public class FbInfoDisclosureEntity extends AuditorEntity implements Serializable{
    
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
     * 事件时间
     */
    @Column(name="EVENT_DATE")
    private String eventDate;
    /**
     * 代码
     */
    @Column(name="CODE")
    private String code;
    /**
     * 公告时间
     */
    @Column(name="ANNOUNCEMENT_DATE")
    private String announcementDate;
    /**
     * 原文链接
     */
    @Column(name="ORIGINAL_LINK")
    private String originalLink;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 标题
     */
    @Column(name="TITLE")
    private String title;
    /**
     * 证券简称
     */
    @Column(name="SHORT_SECURITIES")
    private String shortSecuritites;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAnnouncementDate() {
        return announcementDate;
    }
    public void setAnnouncementDate(String announcementDate) {
        this.announcementDate = announcementDate;
    }
    public String getOriginalLink() {
        return originalLink;
    }
    public void setOriginalLink(String originalLink) {
        this.originalLink = originalLink;
    }
    public String getPublicDate() {
        return publicDate;
    }
    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getShortSecuritites() {
        return shortSecuritites;
    }
    public void setShortSecuritites(String shortSecuritites) {
        this.shortSecuritites = shortSecuritites;
    }
    
}
