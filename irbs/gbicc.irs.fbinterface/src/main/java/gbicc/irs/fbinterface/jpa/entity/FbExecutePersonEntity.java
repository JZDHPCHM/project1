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
 * 被执行人结果表
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Entity
@Table(name="FB_EXECUTE_PERSON")
public class FbExecutePersonEntity extends AuditorEntity implements Serializable{
    
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
     * 执行标的
     */
    @Column(name="EXECUTION_UNDERLY")
    private String executionUnderly;
    /**
     * 执行法院
     */
    @Column(name="EXECUTION_COURT")
    private String executionCourt;
    /**
     * 案件状态
     */
    @Column(name="CASE_STATUS")
    private String caseStatus;
    /**
     * 案号
     */
    @Column(name="CASE_NUMBER")
    private String caseNumber;
    /**
     * 立案时间
     */
    @Column(name="CASE_DATE")
    private String caseDate;
    /**
     * 被执行人姓名/名称
     */
    @Column(name="EXECUTION_NAME")
    private String executionName;

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

    public String getExecutionUnderly() {
        return executionUnderly;
    }

    public void setExecutionUnderly(String executionUnderly) {
        this.executionUnderly = executionUnderly == null ? null : executionUnderly.trim();
    }

    public String getExecutionCourt() {
        return executionCourt;
    }

    public void setExecutionCourt(String executionCourt) {
        this.executionCourt = executionCourt == null ? null : executionCourt.trim();
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus == null ? null : caseStatus.trim();
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber == null ? null : caseNumber.trim();
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate == null ? null : caseDate.trim();
    }

    public String getExecutionName() {
        return executionName;
    }

    public void setExecutionName(String executionName) {
        this.executionName = executionName == null ? null : executionName.trim();
    }
}