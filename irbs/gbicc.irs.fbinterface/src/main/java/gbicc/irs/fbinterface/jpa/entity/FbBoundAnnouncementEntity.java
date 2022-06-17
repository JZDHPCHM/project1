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
 * 债券公告实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Entity
@Table(name="FB_BOUND_ANNOUNCEMENT")
public class FbBoundAnnouncementEntity extends AuditorEntity implements Serializable{

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
     * 发行人名称
     */
    @Column(name="ISSUER_NAME")
    private String issuerName;
    /**
     * URL
     */
    @Column(name="URL")
    private String url;
    /**
     * 公告标题
     */
    @Column(name="TITLE")
    private String title;
    /**
     * 公告类型
     */
    @Column(name="TYPE")
    private String type;
    /**
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 披露对象
     */
    @Column(name="DISCLOSURE")
    private String disclosure;
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
    public String getIssuerName() {
        return issuerName;
    }
    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPublicDate() {
        return publicDate;
    }
    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }
    public String getDisclosure() {
        return disclosure;
    }
    public void setDisclosure(String disclosure) {
        this.disclosure = disclosure;
    }
    
}
