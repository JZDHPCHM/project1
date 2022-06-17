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
 * 股东信息实缴明细实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_SHAREHOLDER_PAID_DETAIL")
public class FbShareholderPaidDetailEntity extends AuditorEntity implements Serializable{
    
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
     * 实缴出资币种
     */
    @Column(name="PAID_CURRENCY")
    private String paidCurrency;
    /**
     * 实缴出资方式
     */
    @Column(name="PAID_TYPE")
    private String paidType;
    /**
     * 实缴出资日期
     */
    @Column(name="PAID_DATE")
    private String paidDate;
    /**
     * 实缴出资额
     */
    @Column(name="PAID_AMOUNT")
    private String paidAmount;

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

    public String getPaidCurrency() {
        return paidCurrency;
    }

    public void setPaidCurrency(String paidCurrency) {
        this.paidCurrency = paidCurrency == null ? null : paidCurrency.trim();
    }

    public String getPaidType() {
        return paidType;
    }

    public void setPaidType(String paidType) {
        this.paidType = paidType == null ? null : paidType.trim();
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate == null ? null : paidDate.trim();
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount == null ? null : paidAmount.trim();
    }
}