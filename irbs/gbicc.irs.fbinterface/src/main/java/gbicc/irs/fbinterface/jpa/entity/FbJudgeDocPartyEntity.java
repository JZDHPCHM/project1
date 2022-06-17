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
 * 裁判文书当事人详情实体
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Entity
@Table(name="FB_JUDGE_DOC_PARTY")
public class FbJudgeDocPartyEntity extends AuditorEntity implements Serializable{
   
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 统一社会信用代码、注册号
     */
    @Column(name = "COMPANY_ID")
    private String companyId;
    /**
     * 裁判文书ID
     */
    @Column(name = "JUDGE_DOC_ID")
    private String judgeDocId;
    /**
     * 公司名
     */
    @Column(name = "COMPANY_NAME")
    private String companyName;
    /**
     * 判决结果
     */
    @Column(name = "JUDGE_RESULT")
    private String judgeResult;
    /**
     * 名字
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 类型
     */
    @Column(name = "TYPE")
    private String type;
    /**
     * 组织名
     */
    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;

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

    public String getJudgeDocId() {
        return judgeDocId;
    }

    public void setJudgeDocId(String judgeDocId) {
        this.judgeDocId = judgeDocId == null ? null : judgeDocId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getJudgeResult() {
        return judgeResult;
    }

    public void setJudgeResult(String judgeResult) {
        this.judgeResult = judgeResult == null ? null : judgeResult.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
}