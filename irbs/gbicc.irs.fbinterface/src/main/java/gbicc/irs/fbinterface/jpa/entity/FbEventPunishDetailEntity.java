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
 * 事件检索具体时间条目实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Entity
@Table(name="FB_EVENT_PUNISH_DETAIL")
public class FbEventPunishDetailEntity extends AuditorEntity implements Serializable{
    
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
     * 事件条目 id，用于获取原文接口
     */
    @Column(name="SOURCE_ID")
    private String sourceId;
    /**
     * 事件原文发布源
     */
    @Column(name="AUTHOR")
    private String author;
    /**
     * 事件所属分类
     */
    @Column(name="CATEGORY")
    private String category;
    /**
     * 事件正文
     */
    @Column(name="CONTENT")
    private String content;
    /**
     * 事件时间时间戳
     */
    @Column(name="EVENT_TIME")
    private Long eventTime;
    /**
     * 发布时间时间戳
     */
    @Column(name="POST_TIME")
    private Long postTime;
    /**
     * 事件原文标题
     */
    @Column(name="TITLE")
    private String title;
    /**
     * 事件原文链接
     */
    @Column(name="URL")
    private String url;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
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

    public String getEventCompanyId() {
        return eventCompanyId;
    }

    public void setEventCompanyId(String eventCompanyId) {
        this.eventCompanyId = eventCompanyId;
    }
}