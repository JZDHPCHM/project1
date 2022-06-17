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
 * 事件检索具体时间条目原文实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Entity
@Table(name="FB_EVENT_PUNISH_SOURCE")
public class FbEventPunishSourceEntity extends AuditorEntity implements Serializable{
    
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
     * 事件检索公司ID
     */
    @Column(name="EVENT_COMPANY_ID")
    private String eventCompanyId;
    /**
     * 具体事件条目 id
     */
    @Column(name="EVENT_DETAIL_ID")
    private String eventDetailId;
    /**
     * 原文 id
     */
    @Column(name="SOURCE_ID")
    private String sourceId;
    /**
     * 原文发布源
     */
    @Column(name="AUTHOR")
    private String author;
    /**
     * 发布时间时间戳
     */
    @Column(name="POST_TIME")
    private Long postTime;
    /**
     * 原文标题
     */
    @Column(name="TITLE")
    private String title;
    /**
     * 原文链接
     */
    @Column(name="URL")
    private String url;
    /**
     * 原文文本内容
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    public String getEventDetailId() {
        return eventDetailId;
    }

    public void setEventDetailId(String eventDetailId) {
        this.eventDetailId = eventDetailId == null ? null : eventDetailId.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Long getPostTime() {
        return postTime;
    }

    public void setPostTime(Long postTime) {
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getEventCompanyId() {
        return eventCompanyId;
    }

    public void setEventCompanyId(String eventCompanyId) {
        this.eventCompanyId = eventCompanyId;
    }
}