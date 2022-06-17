package gbicc.irs.customer.entity;

import java.sql.Date;

import org.wsp.framework.jpa.model.org.entity.Org;
import gbicc.irs.customer.support.EnterpriseSourceType;

public class Customer{

	
	protected String creator;
	
	protected Date createDate;
	
	protected String lastModifier;
	
	protected Date lastModifyDate;
	
	//信贷客户号(主键)
	private String custNo;
	
	//统一客户号
	private String unCustNo;
	
	//客户中文名
	private String custName;
	
	//注册金额
	private String regAmount;
	
	//注册地址描述	
	private String regAddress;
	
	//国标行业
	private String gbIndustry;

	//行内行业分类
	private String intraIndustry;
	
	//营业执照号
	private String businessLicense;	
	
	//员工数量
	private Integer empCount;

	//法人代表名称
	private String legalRep;	

	//客户经理名称
	private String managerName;	
	
	//客户经理编号
	private String managerUser;
	
	//经营范围
	private String busScope;	
	
	//办公地址
	private String offcAddress;	
	
	//办公地址邮编
	private String offcAddrZipCode;	
	
	//授信金额
	private Double crdtAmt;
	
	//企业规模
	private String enterpriseScale;	
	
	//成立时间
	private Date establishmentTime;	
	
	//新建企业标识
	private String isNewMark;	
	
	//数据来源
	private EnterpriseSourceType sourceType;
	
	//客户类型
	private String ctmType;	
	
	//模型id
	private String modelCode;
	
	/**
	 * 客户核心代码
	 */
	private String ctmCoreNo;
		
	/**
	 * 证件类型
	 */
	private String certType;
	
	/**
	 * 登记证件号
	 */
	private String certidNo;
	
	/**
	 * 是否融资性担保机构
	 */
	private String isFinancingGuarantee;
	
	/**
	 * 是否政府性融资性平台
	 */
	private String govPlatform;
	
	/**
	 * 是否高新技术企业
	 */
	private String newTechnology;
	
	/**
	 * 财务报表类型
	 */
	private String financialStatementsType;
	
	/**
	 * 是否是本行股东
	 */
	private String bankShareholders;
	
	/**
	 * 首次建立信贷关系年月
	 */
	private Date creditDate;
	
	/**
	 * 是否异地
	 */
	private String isElseWhere;
	
	
	/**
	 * 登记日期
	 */
	private String inputDt;
	
	/**
	 * 更新机构
	 */
	private Org updateOrg;
	/**
	 * 更新日期
	 */
	private Date updateDt;
	
	/**
	 * 是否经营异常
	 */
	private boolean abnormalOperation;
	
	/**
	 * 人行联合惩戒名单
	 */
	private boolean blankBlackList;
	
	/**
	 * 是否重大诉讼
	 */
	private boolean isLitigation;
	
	/**
	 * 是否科技企业
	 */
	private String technology;
	
	/**
	 * 资产总额
	 */
	private String totalAssets;
	
	/**
	 * 营业收入
	 */
	private String annualIncome;
	
	/**
	 * 是否集团母公司
	 */
	private String isGroupParentCompany;
	
	/**
	 公司名
	 */
	private String enterpriseName;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getUnCustNo() {
		return unCustNo;
	}

	public void setUnCustNo(String unCustNo) {
		this.unCustNo = unCustNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getRegAmount() {
		return regAmount;
	}

	public void setRegAmount(String regAmount) {
		this.regAmount = regAmount;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	public String getGbIndustry() {
		return gbIndustry;
	}

	public void setGbIndustry(String gbIndustry) {
		this.gbIndustry = gbIndustry;
	}

	public String getIntraIndustry() {
		return intraIndustry;
	}

	public void setIntraIndustry(String intraIndustry) {
		this.intraIndustry = intraIndustry;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public Integer getEmpCount() {
		return empCount;
	}

	public void setEmpCount(Integer empCount) {
		this.empCount = empCount;
	}

	public String getLegalRep() {
		return legalRep;
	}

	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerUser() {
		return managerUser;
	}

	public void setManagerUser(String managerUser) {
		this.managerUser = managerUser;
	}

	public String getBusScope() {
		return busScope;
	}

	public void setBusScope(String busScope) {
		this.busScope = busScope;
	}

	public String getOffcAddress() {
		return offcAddress;
	}

	public void setOffcAddress(String offcAddress) {
		this.offcAddress = offcAddress;
	}

	public String getOffcAddrZipCode() {
		return offcAddrZipCode;
	}

	public void setOffcAddrZipCode(String offcAddrZipCode) {
		this.offcAddrZipCode = offcAddrZipCode;
	}

	public Double getCrdtAmt() {
		return crdtAmt;
	}

	public void setCrdtAmt(Double crdtAmt) {
		this.crdtAmt = crdtAmt;
	}

	public String getEnterpriseScale() {
		return enterpriseScale;
	}

	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
	}

	public Date getEstablishmentTime() {
		return establishmentTime;
	}

	public void setEstablishmentTime(Date establishmentTime) {
		this.establishmentTime = establishmentTime;
	}


	public EnterpriseSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(EnterpriseSourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getCtmType() {
		return ctmType;
	}

	public void setCtmType(String ctmType) {
		this.ctmType = ctmType;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getCtmCoreNo() {
		return ctmCoreNo;
	}

	public void setCtmCoreNo(String ctmCoreNo) {
		this.ctmCoreNo = ctmCoreNo;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertidNo() {
		return certidNo;
	}

	public void setCertidNo(String certidNo) {
		this.certidNo = certidNo;
	}

	public String getIsFinancingGuarantee() {
		return isFinancingGuarantee;
	}

	public void setIsFinancingGuarantee(String isFinancingGuarantee) {
		this.isFinancingGuarantee = isFinancingGuarantee;
	}

	public String getGovPlatform() {
		return govPlatform;
	}

	public void setGovPlatform(String govPlatform) {
		this.govPlatform = govPlatform;
	}

	public String getNewTechnology() {
		return newTechnology;
	}

	public void setNewTechnology(String newTechnology) {
		this.newTechnology = newTechnology;
	}

	public String getFinancialStatementsType() {
		return financialStatementsType;
	}

	public void setFinancialStatementsType(String financialStatementsType) {
		this.financialStatementsType = financialStatementsType;
	}

	public String getBankShareholders() {
		return bankShareholders;
	}

	public void setBankShareholders(String bankShareholders) {
		this.bankShareholders = bankShareholders;
	}

	public Date getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
	}

	public String getIsElseWhere() {
		return isElseWhere;
	}

	public void setIsElseWhere(String isElseWhere) {
		this.isElseWhere = isElseWhere;
	}

	public String getInputDt() {
		return inputDt;
	}

	public void setInputDt(String inputDt) {
		this.inputDt = inputDt;
	}

	public Org getUpdateOrg() {
		return updateOrg;
	}

	public void setUpdateOrg(Org updateOrg) {
		this.updateOrg = updateOrg;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}


	public boolean isAbnormalOperation() {
		return abnormalOperation;
	}

	public void setAbnormalOperation(boolean abnormalOperation) {
		this.abnormalOperation = abnormalOperation;
	}

	public boolean isBlankBlackList() {
		return blankBlackList;
	}

	public void setBlankBlackList(boolean blankBlackList) {
		this.blankBlackList = blankBlackList;
	}


	public void setLitigation(boolean isLitigation) {
		this.isLitigation = isLitigation;
	}

	public boolean getIsLitigation() {
		return isLitigation;
	}

	public void setIsLitigation(boolean isLitigation) {
		this.isLitigation = isLitigation;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}

	public String getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getIsGroupParentCompany() {
		return isGroupParentCompany;
	}

	public void setIsGroupParentCompany(String isGroupParentCompany) {
		this.isGroupParentCompany = isGroupParentCompany;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getIsNewMark() {
		return isNewMark;
	}

	public void setIsNewMark(String isNewMark) {
		this.isNewMark = isNewMark;
	}
	
}

