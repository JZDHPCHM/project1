package net.gbicc.app.irr.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

/**
 * 拆分指标信息表
 */
@Entity
@Table(name="T_IRR_SPLIT_INDEX")
public class IrrSplitIndexEntity extends AuditorEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid2")
	@Column(name="id")
	private String id;

	/**
	 * 拆分指标编码
	 */
	@Column(name="SPLIT_CODE")
    private String splitCode;

	/**
	 * 拆分指标名称
	 */
	@Column(name="SPLIT_NAME")
    private String splitName;
	
	/**
	 * 拆分层级
	 */
	@Column(name="SPLIT_LEVEL")
	private String splitLevel;
	
	/**
	 * 源指标ID
	 */
	@Column(name="SOURCE_INDEX_ID")
    private String sourceIndexId;

	/**
	 * 源指标名称
	 */
	@Column(name="SOURCE_INDEX_NAME")
    private String sourceIndexName;

	/**
	 * 评估项目名称
	 */
	@Column(name="SOURCE_PROJ_NAME")
    private String sourceProjName;
	
	/**
	 * 评估项目ID
	 */
	@Column(name="SOURCE_PROJ_ID")
	private String sourceProjId;

	/**
	 * 拆分指标公式
	 */
	@Column(name="SPLIT_FORMULA")
    private String splitFormula;

	/**
	 * 拆分指标公式编码
	 */
	@Column(name="SPLIT_FORMULA_CODE")
    private String splitFormulaCode;

	/**
	 * 拆分指标可执行公式
	 */
	@Column(name="SPLIT_EVAL_FORM")
    private String splitEvalForm;

	/**
	 * 是否启用
	 */
	@Column(name="IS_USE")
    private String isUse;

	/**
	 * 采集方式
	 */
	@Column(name="COLL_WAY")
    private String collWay;
	
	/**
	 * 结果类型
	 */
	@Column(name="RESULT_TYPE")
	private String resultType;

	/**
	 * 子流程编码
	 */
	@Column(name="SUB_PROCESS_CODE")
	private String subProcessCode;
	
	/**
	 * 采集人ID
	 */
	@Column(name="COLL_USER_ID")
    private String collUserId;
	
	/**
	 * 采集人账号
	 */
	@Column(name="COLL_USER_LOGINNAME")
	private String collUserLoginName;

	/**
	 * 采集人名称
	 */
	@Column(name="COLL_USER_NAME")
	private String collUserName;
	
	/** 
	 * 审核人ID
	 */
	@Column(name="EXAM_USER_ID")
	private String examUserId;
	
	/**
	 * 审核人账号
	 */
	@Column(name="EXAM_USER_LOGINNAME")
	private String examUserLoginName;
	
	/**
	 * 审核人名称
	 */
	@Column(name="EXAM_USER_NAME")
	private String examUserName;
	
	/**
	 * 复核人ID
	 */
	@Column(name="REVIEW_USER_ID")
	private String reviewUserId;
	
	/**
	 * 复核人账号
	 */
	@Column(name="REVIEW_USER_LOGINNAME")
	private String reviewUserLoginName;
	
	/**
	 * 复核人名称
	 */
	@Column(name="REVIEW_USER_NAME")
	private String reviewUserName;
	
	/**
	 * 渠道标识
	 */
	@Column(name="CHANNEL_FLAG")
	private String channelFlag;
	
	/**
	 * 所属机构ID
	 */
	@Column(name="ORG_ID")
    private String orgId;

	/**
	 * 所属机构编码
	 */
	@Column(name="ORG_CODE")
    private String orgCode;

	/**
	 * 所属机构名称
	 */
	@Column(name="ORG_NAME")
    private String orgName;

	/**
	 * 主体编码
	 */
	@Column(name="DATA_BODY")
    private String dataBody;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSplitCode() {
        return splitCode;
    }

    public void setSplitCode(String splitCode) {
        this.splitCode = splitCode;
    }

    public String getSplitName() {
        return splitName;
    }

    public void setSplitName(String splitName) {
        this.splitName = splitName;
    }

	public String getSplitLevel() {
		return splitLevel;
	}

	public void setSplitLevel(String splitLevel) {
		this.splitLevel = splitLevel;
	}

	public String getSourceIndexId() {
        return sourceIndexId;
    }

    public void setSourceIndexId(String sourceIndexId) {
        this.sourceIndexId = sourceIndexId;
    }

    public String getSourceIndexName() {
        return sourceIndexName;
    }

    public void setSourceIndexName(String sourceIndexName) {
        this.sourceIndexName = sourceIndexName;
    }

    public String getSourceProjName() {
        return sourceProjName;
    }

    public void setSourceProjName(String sourceProjName) {
        this.sourceProjName = sourceProjName;
    }

    public String getSourceProjId() {
		return sourceProjId;
	}

	public void setSourceProjId(String sourceProjId) {
		this.sourceProjId = sourceProjId;
	}

	public String getSplitFormula() {
        return splitFormula;
    }

    public void setSplitFormula(String splitFormula) {
        this.splitFormula = splitFormula;
    }

    public String getSplitFormulaCode() {
        return splitFormulaCode;
    }

    public void setSplitFormulaCode(String splitFormulaCode) {
        this.splitFormulaCode = splitFormulaCode;
    }

    public String getSplitEvalForm() {
        return splitEvalForm;
    }

    public void setSplitEvalForm(String splitEvalForm) {
        this.splitEvalForm = splitEvalForm;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getCollWay() {
        return collWay;
    }

    public void setCollWay(String collWay) {
        this.collWay = collWay;
    }

    public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDataBody() {
        return dataBody;
    }

    public void setDataBody(String dataBody) {
        this.dataBody = dataBody;
    }

	public String getCollUserId() {
		return collUserId;
	}

	public void setCollUserId(String collUserId) {
		this.collUserId = collUserId;
	}

	public String getCollUserName() {
		return collUserName;
	}

	public void setCollUserName(String collUserName) {
		this.collUserName = collUserName;
	}

	public String getExamUserId() {
		return examUserId;
	}

	public void setExamUserId(String examUserId) {
		this.examUserId = examUserId;
	}

	public String getExamUserName() {
		return examUserName;
	}

	public void setExamUserName(String examUserName) {
		this.examUserName = examUserName;
	}

	public String getReviewUserId() {
		return reviewUserId;
	}

	public void setReviewUserId(String reviewUserId) {
		this.reviewUserId = reviewUserId;
	}

	public String getReviewUserName() {
		return reviewUserName;
	}

	public void setReviewUserName(String reviewUserName) {
		this.reviewUserName = reviewUserName;
	}

	public String getSubProcessCode() {
		return subProcessCode;
	}

	public void setSubProcessCode(String subProcessCode) {
		this.subProcessCode = subProcessCode;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getCollUserLoginName() {
		return collUserLoginName;
	}

	public void setCollUserLoginName(String collUserLoginName) {
		this.collUserLoginName = collUserLoginName;
	}

	public String getExamUserLoginName() {
		return examUserLoginName;
	}

	public void setExamUserLoginName(String examUserLoginName) {
		this.examUserLoginName = examUserLoginName;
	}

	public String getReviewUserLoginName() {
		return reviewUserLoginName;
	}

	public void setReviewUserLoginName(String reviewUserLoginName) {
		this.reviewUserLoginName = reviewUserLoginName;
	}

	public String getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	
}