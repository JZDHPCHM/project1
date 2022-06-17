package gbicc.irs.risk.exposure.Wrapper;

import java.io.Serializable;
import java.util.Date;

public class RiskWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 经办机构
	 */
	private String agency;
	/**
	 *受理人
	 */
	private String assigneeId;
	/**
	 * 余额
	 */
	private Double balance;
	/**
	 * 合同编号
	 */

	private String contractNum;
	/**
	 * 合同金额
	 */
	private String contractSum;
	/**
	 * 客户名称
	 */
	private String ctmName;
	/**
	 * 客户编号
	 */
	private String ctmNum;
	
	/**
	 * 客户类型
	 */
	private String ctmType;
	/**
	 * 币种
	 */
	private String currency;
	/**
	 * 当前任务人
	 */
	private String currentTaskTerson;
	/**
	 * 起始日期
	 */
	private String endDt;
	/**
	 * 最终判定结果
	 */
	private String finalDecision;
	/**
	 * 完成时间
	 */
	private String finshDate;
	/**
	 *行业大类
	 */
	private String gbIndustry;
	private String id;
	/**
	 *行业
	 */
	private String industry;
	/**
	 * 行业类型
	 */
	private String industryType;
	/**
	 *行业大类
	 */
	private String intraIndustry;
	/**
	 * 贷款投向
	 */
	private String loansTo;
	/**
	 * 主要担保方式
	 */
	private String mainGuaranteeMethod;
	/**
	 * 人工判定结果
	 */
	private String manualDecisionResult;
	/**
	 * 发生类型
	 */
	private String occurrenceType;
	/**
	 * 流程状态
	 */
	private String processState;
	/**
	 * 产品类型
	 */
	private String productType;
	/**
	 * 调整方式
	 */
	private String reason;
	
	/**
	 * 调整方式
	 */
	private String regulatingMethods;
	/**
	 * 企业规模
	 */
	private String scale;
	/**
	 * 发起时间
	 */
	private String startDate;
	/**
	 * 到期日期
	 */
	private String startDt;
	/**
	 * 发起人
	 */
	private String startUser;
	/**
	 * 系统判定时间
	 */
	private Date systemDecisionDate;
	/**
	 * 系统判定结果
	 */
	private String systemDecisionResult;
	/**
	 * 营业收入
	 */
	private Double taking;
	/**
	 * 任务id
	 */
	private  String taskId;
	/**
	 * 资产总额
	 */
	private Double totalAssets;
	public String getAgency() {
		return agency;
	}
	public String getAssigneeId() {
		return assigneeId;
	}
	public Double getBalance() {
		return balance;
	}
	public String getContractNum() {
		return contractNum;
	}

	public String getContractSum() {
		return contractSum;
	}
	public String getCtmName() {
		return ctmName;
	}
	public String getCtmNum() {
		return ctmNum;
	}
	public String getCtmType() {
		return ctmType;
	}
	public String getCurrency() {
		return currency;
	}
	public String getCurrentTaskTerson() {
		return currentTaskTerson;
	}
	public String getEndDt() {
		return endDt;
	}
	public String getFinalDecision() {
		return finalDecision;
	}
	
	public String getFinshDate() {
		return finshDate;
	}
	public String getGbIndustry() {
		return gbIndustry;
	}
	public String getId() {
		return id;
	}
	public String getIndustry() {
		return industry;
	}
	public String getIndustryType() {
		return industryType;
	}
	public String getIntraIndustry() {
		return intraIndustry;
	}
	public String getLoansTo() {
		return loansTo;
	}
	public String getMainGuaranteeMethod() {
		return mainGuaranteeMethod;
	}
	public String getManualDecisionResult() {
		return manualDecisionResult;
	}
	public String getOccurrenceType() {
		return occurrenceType;
	}
	public String getProcessState() {
		return processState;
	}
	public String getProductType() {
		return productType;
	}
	public String getReason() {
		return reason;
	}
	public String getRegulatingMethods() {
		return regulatingMethods;
	}
	public String getScale() {
		return scale;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getStartDt() {
		return startDt;
	}
	public String getStartUser() {
		return startUser;
	}
	public Date getSystemDecisionDate() {
		return systemDecisionDate;
	}
	public void setSystemDecisionDate(Date systemDecisionDate) {
		this.systemDecisionDate = systemDecisionDate;
	}
	public String getSystemDecisionResult() {
		return systemDecisionResult;
	}
	public Double getTaking() {
		return taking;
	}
	public String getTaskId() {
		return taskId;
	}
	public Double getTotalAssets() {
		return totalAssets;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public void setContractSum(String contractSum) {
		this.contractSum = contractSum;
	}
	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}
	public void setCtmNum(String ctmNum) {
		this.ctmNum = ctmNum;
	}
	public void setCtmType(String ctmType) {
		this.ctmType = ctmType;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setCurrentTaskTerson(String currentTaskTerson) {
		this.currentTaskTerson = currentTaskTerson;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public void setFinalDecision(String finalDecision) {
		this.finalDecision = finalDecision;
	}
	public void setFinshDate(String finshDate) {
		this.finshDate = finshDate;
	}
	public void setGbIndustry(String gbIndustry) {
		this.gbIndustry = gbIndustry;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	public void setIntraIndustry(String intraIndustry) {
		this.intraIndustry = intraIndustry;
	}
	public void setLoansTo(String loansTo) {
		this.loansTo = loansTo;
	}
	
	public void setMainGuaranteeMethod(String mainGuaranteeMethod) {
		this.mainGuaranteeMethod = mainGuaranteeMethod;
	}
	public void setManualDecisionResult(String manualDecisionResult) {
		this.manualDecisionResult = manualDecisionResult;
	}
	public void setOccurrenceType(String occurrenceType) {
		this.occurrenceType = occurrenceType;
	}
	public void setProcessState(String processState) {
		this.processState = processState;
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setRegulatingMethods(String regulatingMethods) {
		this.regulatingMethods = regulatingMethods;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public void setStartUser(String startUser) {
		this.startUser = startUser;
	}
	public void setSystemDecisionResult(String systemDecisionResult) {
		this.systemDecisionResult = systemDecisionResult;
	}
	public void setTaking(Double taking) {
		this.taking = taking;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public void setTotalAssets(Double totalAssets) {
		this.totalAssets = totalAssets;
	}

	
	private String riskFinalType;
	public String getRiskFinalType() {
		return riskFinalType;
	}
	public void setRiskFinalType(String riskFinalType) {
		this.riskFinalType = riskFinalType;
	}
	
}
