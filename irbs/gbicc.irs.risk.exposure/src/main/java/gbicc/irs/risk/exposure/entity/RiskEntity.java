package gbicc.irs.risk.exposure.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.wsp.framework.jpa.entity.AuditorEntity;

import gbicc.irs.risk.exposure.support.DefaultRiskProcessStatus;


@Entity
@Table(name="IRS_RISK_EXPOSURE")
public class RiskEntity extends AuditorEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public RiskEntity() {
		super();
	}
	/**
	 * 合同编号
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name="CONTRACT_NUM", length=255)
	private String contractNum;

	/**
	 * 产品类型
	 */
	@Column(name="PRODUCT_TYPE", length=255)
	private String productType;

	/**
	 * 客户编号
	 */
	@Column(name="CTM_NUM", length=255)
	private String ctmNum;
	/**
	 * 主要担保方式
	 */
	@Column(name="MAIN_GUARANTEE_METHOD", length=255)
	private String mainGuaranteeMethod;
	
	/**
	 * 客户名称
	 */
	@Column(name="CTM_NAME", length=255)
	private String ctmName;

	/**
	 * 系统判定结果
	 */
	@Column(name="SYSTEM_DECISION_RESULT", length=255)
	private String systemDecisionResult;
	/**
	 * 人工判定结果
	 */
	@Column(name="MANUAL_DECISION_RESULT", length=255)
	private String manualDecisionResult;
	/**
	 * 最终判定结果
	 */
	@Column(name="FINAL_DECISION", length=255)
	private String finalDecision;
	/**
	 * 系统判定时间
	 */
	@Column(name="SYSTEM_DECISION_DATE")
	private Date SystemDecisionDate;
	/**
	 * 发起时间
	 */
	@Column(name="START_DATE")
	private Date startDate;
	/**
	 * 发起人
	 */
	@Column(name="START_USER")
	private String startUser;
	/**
	 * 流程状态
	 */
	@Column(name="PROCESS_STATE", length=255)
	@Enumerated(EnumType.STRING)
	private DefaultRiskProcessStatus processState;
	/**
	 * 当前任务人
	 */
	@Column(name="CURRENT_TASK_TERSON", length=255)
	private String currentTaskTerson;
	
	/**
	 * 完成时间
	 */
	@Column(name="FINSH_DATE")
	private Date finshDate;
	/**
	 * 调整方式
	 */
	@Column(name="REGULATING_METHODS")
	private String regulatingMethods;
	
	@Column(name="REASON")
	private String reason;

	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCtmNum() {
		return ctmNum;
	}
	public void setCtmNum(String ctmNum) {
		this.ctmNum = ctmNum;
	}
	public String getMainGuaranteeMethod() {
		return mainGuaranteeMethod;
	}
	public void setMainGuaranteeMethod(String mainGuaranteeMethod) {
		this.mainGuaranteeMethod = mainGuaranteeMethod;
	}
	public String getCtmName() {
		return ctmName;
	}
	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}
	public String getSystemDecisionResult() {
		return systemDecisionResult;
	}
	public void setSystemDecisionResult(String systemDecisionResult) {
		this.systemDecisionResult = systemDecisionResult;
	}
	public String getManualDecisionResult() {
		return manualDecisionResult;
	}
	public void setManualDecisionResult(String manualDecisionResult) {
		this.manualDecisionResult = manualDecisionResult;
	}
	public String getFinalDecision() {
		return finalDecision;
	}
	public void setFinalDecision(String finalDecision) {
		this.finalDecision = finalDecision;
	}
	public Date getSystemDecisionDate() {
		return SystemDecisionDate;
	}
	public void setSystemDecisionDate(Date systemDecisionDate) {
		SystemDecisionDate = systemDecisionDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public DefaultRiskProcessStatus getProcessState() {
		return processState;
	}
	public void setProcessState(DefaultRiskProcessStatus processState) {
		this.processState = processState;
	}
	public String getCurrentTaskTerson() {
		return currentTaskTerson;
	}
	public void setCurrentTaskTerson(String currentTaskTerson) {
		this.currentTaskTerson = currentTaskTerson;
	}
	public Date getFinshDate() {
		return finshDate;
	}
	public void setFinshDate(Date finshDate) {
		this.finshDate = finshDate;
	}
	public String getRegulatingMethods() {
		return regulatingMethods;
	}
	public void setRegulatingMethods(String regulatingMethods) {
		this.regulatingMethods = regulatingMethods;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStartUser() {
		return startUser;
	}
	public void setStartUser(String startUser) {
		this.startUser = startUser;
	}
	
	
}
