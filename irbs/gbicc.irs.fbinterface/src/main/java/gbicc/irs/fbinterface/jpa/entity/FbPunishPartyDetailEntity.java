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
 * 行政处罚当事人处罚详情相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Entity
@Table(name="FB_PUNISH_PARTY_DETAIL")
public class FbPunishPartyDetailEntity extends AuditorEntity implements Serializable{
    
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
     * 行政处罚ID
     */
    @Column(name="PUNISH_ID")
    private String punishId;
    /**
     * 当事人ID
     */
    @Column(name="PUNISH_PARTY")
    private String punishParty;
    /**
     * 时间
     */
    @Column(name="PUNISH_DATE")
    private String punishDate;
    /**
     * 类别
     */
    @Column(name="TYPE")
    private String type;
    /**
     * 金额
     */
    @Column(name="AMOUNT")
    private String amount;

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

    public String getPunishId() {
        return punishId;
    }

    public void setPunishId(String punishId) {
        this.punishId = punishId == null ? null : punishId.trim();
    }

    public String getPunishParty() {
        return punishParty;
    }

    public void setPunishParty(String punishParty) {
        this.punishParty = punishParty == null ? null : punishParty.trim();
    }

    public String getPunishDate() {
        return punishDate;
    }

    public void setPunishDate(String punishDate) {
        this.punishDate = punishDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }
}