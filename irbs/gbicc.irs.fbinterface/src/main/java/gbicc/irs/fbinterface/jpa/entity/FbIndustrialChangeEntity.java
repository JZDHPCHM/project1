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
 * 工商变更实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Entity
@Table(name="FB_INDUSTRIAL_CHANGE")
public class FbIndustrialChangeEntity extends AuditorEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
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
     * 变更事项
     */
    @Column(name="CHANGE_CONTENT")
    private String changeContent;
    /**
     * 变更事项详情
     */
    @Column(name="CHANGE_DETAIL")
    private String changeDetail;
    /**
     * 变更前
     */
    @Column(name="CHANGE_BEFORE")
    private String changeBefore;
    /**
     * 变更后
     */
    @Column(name="CHANGE_AFTER")
    private String changeAfter;
    /**
     * 变更日期
     */
    @Column(name="CHANGE_DATE")
    private String changeDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public String getChangeContent() {
        return changeContent;
    }
    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
    }
    public String getChangeDetail() {
        return changeDetail;
    }
    public void setChangeDetail(String changeDetail) {
        this.changeDetail = changeDetail;
    }
    public String getChangeBefore() {
        return changeBefore;
    }
    public void setChangeBefore(String changeBefore) {
        this.changeBefore = changeBefore;
    }
    public String getChangeAfter() {
        return changeAfter;
    }
    public void setChangeAfter(String changeAfter) {
        this.changeAfter = changeAfter;
    }
    public String getChangeDate() {
        return changeDate;
    }
    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }
}