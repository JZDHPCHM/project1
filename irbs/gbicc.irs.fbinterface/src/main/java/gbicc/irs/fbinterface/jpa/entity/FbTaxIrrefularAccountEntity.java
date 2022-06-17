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
 * 税务非正常户实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Entity
@Table(name="FB_TAX_IRREFULAR_ACCOUNT")
public class FbTaxIrrefularAccountEntity extends AuditorEntity implements Serializable{

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
     * 企业名称
     */
    @Column(name="COMPANY_NAME")
    private String companyName;
    /**
     * 发布期
     */
    @Column(name="PUBLIC_PERIOD")
    private String publicPeriod;
    /**
     * 法定代表人
     */
    @Column(name="LEGAL_NAME")
    private String legalName;
    /**
     * 税务机关
     */
    @Column(name="TAX_AUTHORITY")
    private String taxAuthority;
    /**
     * 经营地址
     */
    @Column(name="BUSINESS_ADDRESS")
    private String businessAddress;
    /**
     * 认定时间
     */
    @Column(name="IDENTIFICATION_TIME")
    private String identificationTime;
    /**
     * 识别号
     */
    @Column(name="IDENTIFICATION_NUMBER")
    private String identificationNumber;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getPublicPeriod() {
        return publicPeriod;
    }

    public void setPublicPeriod(String publicPeriod) {
        this.publicPeriod = publicPeriod == null ? null : publicPeriod.trim();
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName == null ? null : legalName.trim();
    }

    public String getTaxAuthority() {
        return taxAuthority;
    }

    public void setTaxAuthority(String taxAuthority) {
        this.taxAuthority = taxAuthority == null ? null : taxAuthority.trim();
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress == null ? null : businessAddress.trim();
    }

    public String getIdentificationTime() {
        return identificationTime;
    }

    public void setIdentificationTime(String identificationTime) {
        this.identificationTime = identificationTime == null ? null : identificationTime.trim();
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber == null ? null : identificationNumber.trim();
    }
}