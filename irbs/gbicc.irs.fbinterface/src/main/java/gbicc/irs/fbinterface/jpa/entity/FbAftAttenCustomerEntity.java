package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * 关注客户信息表
 * 
 * @author songxubei
 * @version v1.0 2019年10月18日
 */
@Entity
@Table(name="T_AFT_ATTEN_CUSTOMER")
public class FbAftAttenCustomerEntity implements Serializable{

    
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @Id
    @GenericGenerator(name="system-uuid",strategy="uuid2")
    @GeneratedValue(generator="system-uuid")
    @Column(name="ID",length=36)
    private String id;
    /**
     * 承租人Id
     */
    @Column(name="LESSEE_ID")
    private String lesseeId;
    /**
     * 承租人名称
     */
    @Column(name="LESSEE_NAME")
    private String lesseeName;
    /**
     * 承租人统一社会信用代码、注册码
     */
    @Column(name="LESSEE_COMPANY_ID")
    private String lesseeCompanyId;
    /**
     * 关联人ID
     */
    @Column(name="ASSO_ID")
    private String assoId;
    /**
     * 关联人名称
     */
    @Column(name="ASSO_NAME")
    private String assoName;
    /**
     * 关联人统一社会信用代码、注册码
     */
    @Column(name="ASSO_COMPANY_ID")
    private String assoCompanyId;
    /**
     * 关联人类型
     */
    @Column(name="ASSO_TYPE")
    private String assoType;
    /**
     * 关联人类型名称
     */
    @Column(name="ASSO_TYPE_NAME")
    private String assoTypeName;
    /**
     * 商业伙伴类别
     */
    @Column(name="PARTNER_CATEGORY")
    private String partnerCategory;
    /**
     * 是否关注
     */
    @Column(name="IS_ATTEN")
    private String isAtten;
    /**
     * 数据时间
     */
    @Column(name="DATA_DT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDt;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLesseeId() {
        return lesseeId;
    }
    public void setLesseeId(String lesseeId) {
        this.lesseeId = lesseeId;
    }
    public String getLesseeCompanyId() {
        return lesseeCompanyId;
    }
    public void setLesseeCompanyId(String lesseeCompanyId) {
        this.lesseeCompanyId = lesseeCompanyId;
    }
    public String getAssoId() {
        return assoId;
    }
    public void setAssoId(String assoId) {
        this.assoId = assoId;
    }
    public String getAssoName() {
        return assoName;
    }
    public void setAssoName(String assoName) {
        this.assoName = assoName;
    }
    public String getAssoCompanyId() {
        return assoCompanyId;
    }
    public void setAssoCompanyId(String assoCompanyId) {
        this.assoCompanyId = assoCompanyId;
    }
    public String getAssoType() {
        return assoType;
    }
    public void setAssoType(String assoType) {
        this.assoType = assoType;
    }
    public String getAssoTypeName() {
        return assoTypeName;
    }
    public void setAssoTypeName(String assoTypeName) {
        this.assoTypeName = assoTypeName;
    }
    public String getPartnerCategory() {
        return partnerCategory;
    }
    public void setPartnerCategory(String partnerCategory) {
        this.partnerCategory = partnerCategory;
    }
    public String getIsAtten() {
        return isAtten;
    }
    public void setIsAtten(String isAtten) {
        this.isAtten = isAtten;
    }
    public Date getDataDt() {
        return dataDt;
    }
    public void setDataDt(Date dataDt) {
        this.dataDt = dataDt;
    }
    @Override
    public String toString() {
        return "FbAftWarnAttenCustomerEntity [id=" + id + ", lesseeId=" + lesseeId + ", lesseeCompanyId="
                + lesseeCompanyId + ", assoId=" + assoId + ", assoName=" + assoName + ", assoCompanyId=" + assoCompanyId
                + ", assoType=" + assoType + ", assoTypeName=" + assoTypeName + ", partnerCategory=" + partnerCategory
                + ", isAtten=" + isAtten + ", dataDt=" + dataDt + "]";
    }
    
}
