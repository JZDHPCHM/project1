package gbicc.irs.fbinterface.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 催缴/欠税实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Entity
@Table(name="FB_OVERDUE_TAX_CALL")
public class FbOverdueTaxCallEntity extends AuditorEntity implements Serializable{
    
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
     * 发布时间
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 名称
     */
    @Column(name="NAME")
    private String name;
    /**
     * 法定代表人姓名
     */
    @Column(name="LEGAL_NAME")
    private String legalName;
    /**
     * 税务机关
     */
    @Column(name="TAX_AUTHORITY")
    private String taxAuthority;
    /**
     * 税种
     */
    @Column(name="TAX_TYPE")
    private String taxType;
    /**
     * 经营地址
     */
    @Column(name="BUSINESS_ADDRESS")
    private String businessAddress;
    /**
     * 识别号
     */
    @Column(name="IDENTIFICATION_NUMBER")
    private String identificationNumber;
    /**
     * 金额
     */
    @Column(name="AMOUNT")
    private BigDecimal amount;
    /**
     * 高级税务机关
     */
    @Column(name="HIGH_TAX_AUTHORITY")
    private String highTaxAuthority;

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

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate == null ? null : publicDate.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType == null ? null : taxType.trim();
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress == null ? null : businessAddress.trim();
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber == null ? null : identificationNumber.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount == null ? null : amount;
    }

    public String getHighTaxAuthority() {
        return highTaxAuthority;
    }

    public void setHighTaxAuthority(String highTaxAuthority) {
        this.highTaxAuthority = highTaxAuthority == null ? null : highTaxAuthority.trim();
    }
}