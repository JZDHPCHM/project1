package gbicc.irs.defaultcustomer.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户列表包装类
 * @author cxj
 *
 */
public class CustomerWrapper implements Serializable{

	private static final long serialVersionUID = -536649021L;

	//客户经理
	private String accountManager;
	
	//客户经理编号
	private String accountManagerCode;
	
	//当前任务人
	private String assignee;
	
	private String	assignee_id;
	
	//营业执照号
	private String businessLicense;
	
	//经营范围
	private String businessScope;
	
	//所属城市代码
	private String cityCode;	
	
	//所属国家地区代码	
	private String countryCode;
	
	//授信金额
	private BigDecimal crdtAmt;
	
	//违约客户id
	private String cust_id;
	
	//客户英文名	
	private String custEAllName;
	
	//客户英文别名	
	private String custESimpleName;
	
	//客户中文名
	private String custName;
	
	//对公客户编号
	private String custNo;

	//客户中文别名	
	private String custSimpleName;

	//失效时间
	private Date deadDate;

	//编号
	private String defaultId;

	//注册地址描述	
	private String descriptionRegisteredAddress;

	//所属区代码
	private String districtCode;

	//到期时间
	private Date dueDate;

	//生效时间
	private Date effectiveDate;

	//员工数量
	private Integer employeesCount;
	//发起时间
	private Date endDate;
	//重生日期
	private Date csDate;
	
	//重生次数
	private String rebirthNum;

	//企业规模
	private String enterpriseScale;
	
	//成立时间
	private Date establishmentTime;
	
	//等级
	private String finalLevel;
	
	//行业代码_一级分类
	private String industryCategoryA;
	
	//行业代码_二级分类
	private String industryCategoryB;
	
	//行业代码_三级分类
	private String industryCategoryC;

	//发起方式
	private String initiationMode;
	
	private String jobType;

	//法人代表名称
	private String legalRepresentative;
	
	//新建企业标识
	private String newEnterpriseMark;
	
	//办公地址
	private String officeAddress;
	
	//办公地址邮编
	private String officeAddressZipCode;
	
	//所属省份代码
	private String provinceCode;
	
	//违约认定方式
	private String rdfs;
	
	//注册金额
	private String registrationAmount;
	
	private String currentRate;
	
	public String getCurrentRate() {
		return currentRate;
	}

	public void setCurrentRate(String currentRate) {
		this.currentRate = currentRate;
	}

	public String getRebirthNum() {
		return rebirthNum;
	}

	public void setRebirthNum(String rebirthNum) {
		this.rebirthNum = rebirthNum;
	}

	//是否违约
	private String sfwy;

	//发起时间
	private Date startDate;

	//发起人
	private String startUser;

	private String status;
	
	private String taskId;
	
	
	//统一客户号
	private String unCustNo;

	//评级是否有效
	private String valid;

	//违约日期
	private Date wyrq;

	public CustomerWrapper() {}
	
	public String getAccountManager() {
		return accountManager;
	}
	
	public String getAccountManagerCode() {
		return accountManagerCode;
	}

	public String getAssignee() {
		return assignee;
	}

	public String getAssignee_id() {
		return assignee_id;
	}	
	
	public String getBusinessLicense() {
		return businessLicense;
	}

	public String getBusinessScope() {
		return businessScope;
	}	

	public String getCityCode() {
		return cityCode;
	}	
	
	public String getCountryCode() {
		return countryCode;
	}	
	
	public BigDecimal getCrdtAmt() {
		return crdtAmt;
	}	
	
	public String getCust_id() {
		return cust_id;
	}	
	
	public String getCustEAllName() {
		return custEAllName;
	}	
	
	public String getCustESimpleName() {
		return custESimpleName;
	}	
	
	public String getCustName() {
		return custName;
	}	
	
	public String getCustNo() {
		return custNo;
	}			

	public String getCustSimpleName() {
		return custSimpleName;
	}

	public Date getDeadDate() {
		return deadDate;
	}

	public String getDefaultId() {		
		return defaultId;
	}

	public String getDescriptionRegisteredAddress() {
		return descriptionRegisteredAddress;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public Integer getEmployeesCount() {
		return employeesCount;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getEnterpriseScale() {
		return enterpriseScale;
	}

	public Date getEstablishmentTime() {
		return establishmentTime;
	}

	public String getFinalLevel() {
		return finalLevel;
	}

	public String getIndustryCategoryA() {
		return industryCategoryA;
	}

	public String getIndustryCategoryB() {
		return industryCategoryB;
	}

	public String getIndustryCategoryC() {
		return industryCategoryC;
	}

	public String getInitiationMode() {
		return initiationMode;
	}

	public String getJobType() {
		return jobType;
	}

	public String getLegalRepresentative() {
		return legalRepresentative;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public String getOfficeAddressZipCode() {
		return officeAddressZipCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public String getRdfs() {
		return rdfs;
	}

	public String getRegistrationAmount() {
		return registrationAmount;
	}

	public String getSfwy() {
		return sfwy;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getStartUser() {
		return startUser;
	}

	public String getStatus() {
		return status;
	}

	public String getTaskId() {
		return taskId;
	}

	public String getUnCustNo() {
		return unCustNo;
	}

	public String getValid() {
		return valid;
	}

	public Date getWyrq() {
		return wyrq;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public void setAccountManagerCode(String accountManagerCode) {
		this.accountManagerCode = accountManagerCode;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public void setAssignee_id(String assignee_id) {
		this.assignee_id = assignee_id;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setCrdtAmt(BigDecimal crdtAmt) {
		this.crdtAmt = crdtAmt;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public void setCustEAllName(String custEAllName) {
		this.custEAllName = custEAllName;
	}

	public void setCustESimpleName(String custESimpleName) {
		this.custESimpleName = custESimpleName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public void setCustSimpleName(String custSimpleName) {
		this.custSimpleName = custSimpleName;
	}

	public void setDeadDate(Date deadDate) {
		this.deadDate = deadDate;
	}

	public void setDefaultId(String defaultId) {
		this.defaultId = defaultId;
	}
	
	public void setDescriptionRegisteredAddress(String descriptionRegisteredAddress) {
		this.descriptionRegisteredAddress = descriptionRegisteredAddress;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public void setEmployeesCount(Integer employeesCount) {
		this.employeesCount = employeesCount;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
	}

	public void setEstablishmentTime(Date establishmentTime) {
		this.establishmentTime = establishmentTime;
	}


	public void setFinalLevel(String finalLevel) {
		this.finalLevel = finalLevel;
	}

	public void setIndustryCategoryA(String industryCategoryA) {
		this.industryCategoryA = industryCategoryA;
	}

	public void setIndustryCategoryB(String industryCategoryB) {
		this.industryCategoryB = industryCategoryB;
	}

	public void setIndustryCategoryC(String industryCategoryC) {
		this.industryCategoryC = industryCategoryC;
	}

	public void setInitiationMode(String initiationMode) {
		this.initiationMode = initiationMode;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	public String getNewEnterpriseMark() {
		return newEnterpriseMark;
	}

	public void setNewEnterpriseMark(String newEnterpriseMark) {
		this.newEnterpriseMark = newEnterpriseMark;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public void setOfficeAddressZipCode(String officeAddressZipCode) {
		this.officeAddressZipCode = officeAddressZipCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public void setRdfs(String rdfs) {
		this.rdfs = rdfs;
	}

	public void setRegistrationAmount(String registrationAmount) {
		this.registrationAmount = registrationAmount;
	}

	public void setSfwy(String sfwy) {
		this.sfwy = sfwy;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setStartUser(String startUser) {
		this.startUser = startUser;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setUnCustNo(String unCustNo) {
		this.unCustNo = unCustNo;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public void setWyrq(Date wyrq) {
		this.wyrq = wyrq;
	}
	public Date getCsDate() {
		return csDate;
	}

	public void setCsDate(Date csDate) {
		this.csDate = csDate;
	}

}
