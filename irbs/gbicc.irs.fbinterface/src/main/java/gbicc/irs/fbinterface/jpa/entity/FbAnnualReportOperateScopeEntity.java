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
 * 年报经营范围实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT_OPERATE_SCOPE")
public class FbAnnualReportOperateScopeEntity extends AuditorEntity implements Serializable{
    
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
     * 一般经营项目
     */
    @Column(name="GENERAL_OPERATE_ITEMS")
    private String generalOperateItems;
    /**
     * 许可经营项目
     */
    @Column(name="LICENSE_OPERATE_ITEMS")
    private String licenseOperateItems;

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

    public String getGeneralOperateItems() {
        return generalOperateItems;
    }

    public void setGeneralOperateItems(String generalOperateItems) {
        this.generalOperateItems = generalOperateItems == null ? null : generalOperateItems.trim();
    }

    public String getLicenseOperateItems() {
        return licenseOperateItems;
    }

    public void setLicenseOperateItems(String licenseOperateItems) {
        this.licenseOperateItems = licenseOperateItems == null ? null : licenseOperateItems.trim();
    }
}