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
 * 年报分支机构登记信息实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_ORG_INFO")
public class FbAnnualReportOrgInfoEntity extends AuditorEntity implements Serializable{
    
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
     * 年报ID
     */
    @Column(name="ANNUAL_REPORT_ID")
    private String annualReportId;
    /**
     * 住所
     */
    @Column(name="ADDRESS")
    private String address;
    /**
     * 分支机构名称
     */
    @Column(name="ORG_NAME")
    private String orgName;
    /**
     * 注册号
     */
    @Column(name="REGISTE_NUMBER")
    private String registeNumber;
    /**
     * 负责人
     */
    @Column(name="RESPONSE_PERSON")
    private String responsePerson;

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

    public String getAnnualReportId() {
        return annualReportId;
    }

    public void setAnnualReportId(String annualReportId) {
        this.annualReportId = annualReportId == null ? null : annualReportId.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getRegisteNumber() {
        return registeNumber;
    }

    public void setRegisteNumber(String registeNumber) {
        this.registeNumber = registeNumber == null ? null : registeNumber.trim();
    }

    public String getResponsePerson() {
        return responsePerson;
    }

    public void setResponsePerson(String responsePerson) {
        this.responsePerson = responsePerson == null ? null : responsePerson.trim();
    }
}