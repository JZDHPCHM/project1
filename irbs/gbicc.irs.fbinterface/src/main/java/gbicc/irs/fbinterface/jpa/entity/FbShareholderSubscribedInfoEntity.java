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
 * 股东信息认缴明细实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_SHAREHOLDER_SUBSCRIBED_INFO")
public class FbShareholderSubscribedInfoEntity extends AuditorEntity implements Serializable{
    
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
     * 股东信息ID
     */
    @Column(name="SHAREHOLDER_INFO_ID")
    private String shareholderInfoId;
    /**
     * 认缴出资币种
     */
    @Column(name="SUBSCRIBED_CURRENCY")
    private String subscribedCurrency;
    /**
     * 认缴出资方式
     */
    @Column(name="SUBSCRIBED_TYPE")
    private String subscribedType;
    /**
     * 认缴出资日期
     */
    @Column(name="SUBSCRIBED_DATE")
    private String subscribedDate;
    /**
     * 认缴出资额
     */
    @Column(name="SUBSCRIBED_AMOUNT")
    private String subscribedAmount;

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

    public String getShareholderInfoId() {
        return shareholderInfoId;
    }

    public void setShareholderInfoId(String shareholderInfoId) {
        this.shareholderInfoId = shareholderInfoId == null ? null : shareholderInfoId.trim();
    }

    public String getSubscribedCurrency() {
        return subscribedCurrency;
    }

    public void setSubscribedCurrency(String subscribedCurrency) {
        this.subscribedCurrency = subscribedCurrency == null ? null : subscribedCurrency.trim();
    }

    public String getSubscribedType() {
        return subscribedType;
    }

    public void setSubscribedType(String subscribedType) {
        this.subscribedType = subscribedType == null ? null : subscribedType.trim();
    }

    public String getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(String subscribedDate) {
        this.subscribedDate = subscribedDate == null ? null : subscribedDate.trim();
    }

    public String getSubscribedAmount() {
        return subscribedAmount;
    }

    public void setSubscribedAmount(String subscribedAmount) {
        this.subscribedAmount = subscribedAmount == null ? null : subscribedAmount.trim();
    }
}