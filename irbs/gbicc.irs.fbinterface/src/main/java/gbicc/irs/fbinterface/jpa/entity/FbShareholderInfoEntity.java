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
 * 股东信息实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_SHAREHOLDER_INFO")
public class FbShareholderInfoEntity extends AuditorEntity implements Serializable{

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
     * 实缴额
     */
    @Column(name="PAID_AMOUNT")
    private BigDecimal paidAmount;
    /**
     * 持股比
     */
    @Column(name="SHAREHOLD_RATIO")
    private BigDecimal shareholdRatio;
    /**
     * 股东
     */
    @Column(name="SHAREHOLDER")
    private String shareholder;
    /**
     * 股东类型
     */
    @Column(name="SHAREHOLDER_TYPE")
    private String shareholderType;
    /**
     * 认缴额
     */
    @Column(name="SUBSCRIBED_AMOUNT")
    private String subscribedAmount;
    /**
     * 责任形式
     */
    @Column(name="LIABILITY_FORM")
    private String liabilityForm;

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

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount == null ? null : paidAmount;
    }

    public BigDecimal getShareholdRatio() {
        return shareholdRatio;
    }

    public void setShareholdRatio(BigDecimal shareholdRatio) {
        this.shareholdRatio = shareholdRatio == null ? null : shareholdRatio;
    }

    public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder == null ? null : shareholder.trim();
    }

    public String getShareholderType() {
        return shareholderType;
    }

    public void setShareholderType(String shareholderType) {
        this.shareholderType = shareholderType == null ? null : shareholderType.trim();
    }

    public String getSubscribedAmount() {
        return subscribedAmount;
    }

    public void setSubscribedAmount(String subscribedAmount) {
        this.subscribedAmount = subscribedAmount == null ? null : subscribedAmount.trim();
    }

    public String getLiabilityForm() {
        return liabilityForm;
    }

    public void setLiabilityForm(String liabilityForm) {
        this.liabilityForm = liabilityForm == null ? null : liabilityForm.trim();
    }
}