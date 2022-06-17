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
 * 审判流程当事人实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Entity
@Table(name="FB_TRIAL_PROCESS_PARTY")
public class FbTrialProcessPartyEntity extends AuditorEntity implements Serializable{
    
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
     * 审判流程ID
     */
    @Column(name="TRIAL_PROCESS_ID")
    private String trialProcessId;
    /**
     * 公司名
     */
    @Column(name="COMPANY_NAME")
    private String companyName;
    /**
     * 名字
     */
    @Column(name="NAME")
    private String name;
    /**
     * 法定代表人
     */
    @Column(name="LEGAL_PERSON")
    private String legalPerson;
    /**
     * 类型
     */
    @Column(name="TYPE")
    private String type;
    /**
     * 组织名
     */
    @Column(name="ORGANIZATION_NAME")
    private String organizationName;
    /**
     * 角色
     */
    @Column(name="ROLE")
    private String role;

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

    public String getTrialProcessId() {
        return trialProcessId;
    }

    public void setTrialProcessId(String trialProcessId) {
        this.trialProcessId = trialProcessId == null ? null : trialProcessId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName == null ? null : organizationName.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }
}