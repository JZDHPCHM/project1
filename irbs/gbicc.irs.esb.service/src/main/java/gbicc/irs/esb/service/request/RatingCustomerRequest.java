package gbicc.irs.esb.service.request;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户基本信息
 * @author ljc
 *
 */
public class RatingCustomerRequest {
	
	// 信贷客户号
	private String custNo;

	// 统一客户号
	private String unCustNo;

	// 客户中文名
	private String custName;
	
	// 是否担保公司 
	private String isGuaranteeCorporation;

	// 注册金额
	private String regAmount;

	// 注册地址描述
	private String regAddress;

	// 国标行业
	private String gbIndustry;

	// 行内行业分类
	private String intraIndustry;

	// 营业执照号
	private String businessLicense;

	// 员工数量
	private String empCount;

	// 法人代表名称
	private String legalRep;

	// 客户经理名称
	private String managerName;

	// 客户经理编号
	private String managerCode;

	// 经营范围
	private String busScope;

	// 办公地址
	private String offcAddress;

	// 办公地址邮编
	private String offcAddrZipCode;

	// 授信金额
	private String crdtAmt;

	// 企业规模
	private String enterpriseScale;

	// 成立时间
	private String establishmentTime;

	// 新建企业标识
	private String newEnterpriseMark;

	// 数据来源
	private String sourceType;

	// 客户类型
	private String ctmType;

	// 模型id
	private String modelId;

	// 机构ID
	private String orgId;
	
	// 客户核心代码
	private String ctmCoreNo;

	// 证件类型
	private String certType;

	// 登记证件号
	private String certidNo;

	// 是否融资性担保机构
	private String isFinancingGuarantee;

	// 是否政府性融资性平台
	private String govPlatform;

	// 是否高新技术企业
	private String newTechnology;

	// 财务报表类型
	private String financialStatementsType;

	// 是否是本行股东
	private String bankShareholders;

	// 首次建立信贷关系年月
	private String creditDate;

	// 是否异地
	private String isElseWhere;

	// 登记日期
	private String inputDt;

	// 更新机构
	private String updateOrgid;

	// 更新日期
	private String updateDt;

	// 是否经营异常
	private String isAbnormalOperation;

	// 人行联合惩戒名单
	private String isBlankBlackList;

	// 是否重大诉讼
	private String litigation;

	// 是否科技企业
	private String technology;

	// 资产总额
	private String totalAssets;

	// 营业收入
	private String annualIncome;
	
	// 公司名
	private String enterpriseName;
	
	// 所属国家
	private String country;
	
	// 所属省份 
	private String province;
	
	// 所属城市
	private String city;
	
	// 所属地区
	private String area;
	
	// 客户财报数据
	private List<RatingFinancialReportRequest> financialReportDatas = new ArrayList<RatingFinancialReportRequest>();

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

	public String getEmpCount() {
		return empCount;
	}

	public void setEmpCount(String empCount) {
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

	public String getManagerCode() {
		return managerCode;
	}

	public void setManagerCode(String managerCode) {
		this.managerCode = managerCode;
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

	public String getCrdtAmt() {
		return crdtAmt;
	}

	public void setCrdtAmt(String crdtAmt) {
		this.crdtAmt = crdtAmt;
	}

	public String getEnterpriseScale() {
		return enterpriseScale;
	}

	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
	}

	public String getEstablishmentTime() {
		return establishmentTime;
	}

	public void setEstablishmentTime(String establishmentTime) {
		this.establishmentTime = establishmentTime;
	}

	public String getNewEnterpriseMark() {
		return newEnterpriseMark;
	}

	public void setNewEnterpriseMark(String newEnterpriseMark) {
		this.newEnterpriseMark = newEnterpriseMark;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getCtmType() {
		return ctmType;
	}

	public void setCtmType(String ctmType) {
		this.ctmType = ctmType;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public String getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(String creditDate) {
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

	public String getUpdateOrgid() {
		return updateOrgid;
	}

	public void setUpdateOrgid(String updateOrgid) {
		this.updateOrgid = updateOrgid;
	}

	public String getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(String updateDt) {
		this.updateDt = updateDt;
	}

	public String getIsAbnormalOperation() {
		return isAbnormalOperation;
	}

	public void setIsAbnormalOperation(String isAbnormalOperation) {
		this.isAbnormalOperation = isAbnormalOperation;
	}

	public String getIsBlankBlackList() {
		return isBlankBlackList;
	}

	public void setIsBlankBlackList(String isBlankBlackList) {
		this.isBlankBlackList = isBlankBlackList;
	}

	public String getLitigation() {
		return litigation;
	}

	public void setLitigation(String litigation) {
		this.litigation = litigation;
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

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<RatingFinancialReportRequest> getFinancialReportDatas() {
		return financialReportDatas;
	}

	public void setFinancialReportDatas(List<RatingFinancialReportRequest> financialReportDatas) {
		this.financialReportDatas = financialReportDatas;
	}

	public String getIsGuaranteeCorporation() {
		return isGuaranteeCorporation;
	}

	public void setIsGuaranteeCorporation(String isGuaranteeCorporation) {
		this.isGuaranteeCorporation = isGuaranteeCorporation;
	}
}
