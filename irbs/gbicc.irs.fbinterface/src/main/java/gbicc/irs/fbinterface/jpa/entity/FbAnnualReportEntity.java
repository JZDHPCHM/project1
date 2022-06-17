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
 * 年报实体类
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Entity
@Table(name="FB_ANNUAL_REPORT")
public class FbAnnualReportEntity extends AuditorEntity implements Serializable{
    
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
     * 发布日期
     */
    @Column(name="PUBLIC_DATE")
    private String publicDate;
    /**
     * 报送年度
     */
    @Column(name="REPORT_DATE")
    private String reportDate;
    /**
     * 注册号
     */
    @Column(name="REGISTE_NUMBER")
    private String registeNumber;

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

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate == null ? null : reportDate.trim();
    }

    public String getRegisteNumber() {
        return registeNumber;
    }

    public void setRegisteNumber(String registeNumber) {
        this.registeNumber = registeNumber == null ? null : registeNumber.trim();
    }
}