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
 * 裁判文书判决金额给付方
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Entity
@Table(name="FB_JUDGE_DOC_SENTENCE_INFO")
public class FbJudgeDocSentenceInfoEntity extends AuditorEntity implements Serializable{
    
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
     * 裁判文书ID
     */
    @Column(name="JUDGE_DOC_ID")
    private String judgeDocId;
    /**
     * 判决金额ID
     */
    @Column(name="JUDGE_DOC_SENTENCE_ID")
    private String judgeDocSentenceId;
    /**
     * 名称
     */
    @Column(name="NAME")
    private String name;

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

    public String getJudgeDocSentenceId() {
        return judgeDocSentenceId;
    }

    public void setJudgeDocSentenceId(String judgeDocSentenceId) {
        this.judgeDocSentenceId = judgeDocSentenceId == null ? null : judgeDocSentenceId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}