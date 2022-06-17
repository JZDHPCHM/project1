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
 * 经营异常实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Entity
@Table(name="FB_ABNORMAL_OPERATION")
public class FbAbnormalOperationEntity extends AuditorEntity implements Serializable{
    
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
     * 作出决定机关
     */
    @Column(name="DECISION_AUTHORITY")
    private String decisionAuthority;
    /**
     * 作出决定机关(列入)
     */
    @Column(name="DECISION_AUTHORITY_ABNORMAL")
    private String decisionAuthorityAbnormal;
    /**
     * 作出决定机关(移出)
     */
    @Column(name="DECISION_AUTHORITY_REMOVE")
    private String decisionAuthorityRemove;
    /**
     * 列入日期
     */
    @Column(name="ABNORMAL_DATE")
    private String abnormalDate;
    /**
     * 异常原因
     */
    @Column(name="ABNORMAL_REASON")
    private String abnormalReason;
    /**
     * 移出原因
     */
    @Column(name="REMOVE_REASON")
    private String removeReason;
    /**
     * 移出日期
     */
    @Column(name="REMOVE_DATE")
    private String removeDate;

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

    public String getDecisionAuthority() {
        return decisionAuthority;
    }

    public void setDecisionAuthority(String decisionAuthority) {
        this.decisionAuthority = decisionAuthority == null ? null : decisionAuthority.trim();
    }

    public String getDecisionAuthorityAbnormal() {
        return decisionAuthorityAbnormal;
    }

    public void setDecisionAuthorityAbnormal(String decisionAuthorityAbnormal) {
        this.decisionAuthorityAbnormal = decisionAuthorityAbnormal == null ? null : decisionAuthorityAbnormal.trim();
    }

    public String getDecisionAuthorityRemove() {
        return decisionAuthorityRemove;
    }

    public void setDecisionAuthorityRemove(String decisionAuthorityRemove) {
        this.decisionAuthorityRemove = decisionAuthorityRemove == null ? null : decisionAuthorityRemove.trim();
    }

    public String getAbnormalDate() {
        return abnormalDate;
    }

    public void setAbnormalDate(String abnormalDate) {
        this.abnormalDate = abnormalDate == null ? null : abnormalDate.trim();
    }

    public String getAbnormalReason() {
        return abnormalReason;
    }

    public void setAbnormalReason(String abnormalReason) {
        this.abnormalReason = abnormalReason == null ? null : abnormalReason.trim();
    }

    public String getRemoveReason() {
        return removeReason;
    }

    public void setRemoveReason(String removeReason) {
        this.removeReason = removeReason == null ? null : removeReason.trim();
    }

    public String getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(String removeDate) {
        this.removeDate = removeDate == null ? null : removeDate.trim();
    }
}