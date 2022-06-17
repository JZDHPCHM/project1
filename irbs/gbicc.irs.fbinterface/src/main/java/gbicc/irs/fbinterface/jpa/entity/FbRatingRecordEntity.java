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
 * 评级记录实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Entity
@Table(name="FB_RATING_RECORD")
public class FbRatingRecordEntity extends AuditorEntity implements Serializable{

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
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 评级
     */
    @Column(name="RATING")
    private String rating;
    /**
     * 评级公司
     */
    @Column(name="RATING_COMPANY")
    private String ratingCompany;
    /**
     * 评级对象
     */
    @Column(name="RATING_OBJECT")
    private String ratingObject;
    /**
     * 评级方式
     */
    @Column(name="RATING_TYPE")
    private String ratingType;
    /**
     * 评级日期
     */
    @Column(name="RATING_DATE")
    private String ratingDate;
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
    public String getPublicDate() {
        return publicDate;
    }
    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getRatingCompany() {
        return ratingCompany;
    }
    public void setRatingCompany(String ratingCompany) {
        this.ratingCompany = ratingCompany;
    }
    public String getRatingObject() {
        return ratingObject;
    }
    public void setRatingObject(String ratingObject) {
        this.ratingObject = ratingObject;
    }
    public String getRatingType() {
        return ratingType;
    }
    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }
    public String getRatingDate() {
        return ratingDate;
    }
    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }
    
    
}
