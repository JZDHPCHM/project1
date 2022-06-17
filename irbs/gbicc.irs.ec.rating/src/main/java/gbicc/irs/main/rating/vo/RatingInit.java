package gbicc.irs.main.rating.vo;

import javax.persistence.*;
   /**
    * models 实体类
    * Wed Oct 09 16:23:41 GMT+08:00 2019 WangShuaiHeng
    */ 
public class RatingInit{
	private String custname;
	private String custcode;
	private String proCode;
	private String proName;
	private String scoretemplateid;
	private String segmentindustry;
	private String highprecision;
	private String enterprisehonor;
	private String economic;
	private String orgName;
	private String id;
	private String type;
	private String internlevel;
	private String internBs;//初评保障倍数
	private String finallevel;
	private String pd;
	private String ratingstatus;
	private String finaldate;
	private String finalBs;//复评保障倍数
	private String assetReview;//资产经理
	private String finalName;
	private String internName;
	private String version;
	private String employeeId;
	private String initSco;
	private String finaSco;
	private String segmentIndustryTop;
	private String  enterpriseScale;
	private String foundenDate;
	private String fDate;
	private String treatN;
	private String description;
	private String proItemName;
	private String assetsLevl;
	private String creditLevl;
	private String mainLevl;
	
	
	
	
	
	
	
	
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getfDate() {
		return fDate;
	}
	public void setfDate(String fDate) {
		this.fDate = fDate;
	}
	public String getTreatN() {
		return treatN;
	}
	public void setTreatN(String treatN) {
		this.treatN = treatN;
	}
	public String getFoundenDate() {
		return foundenDate;
	}
	public void setFoundenDate(String foundenDate) {
		this.foundenDate = foundenDate;
	}
	public String getEnterpriseScale() {
		return enterpriseScale;
	}
	public void setEnterpriseScale(String enterpriseScale) {
		this.enterpriseScale = enterpriseScale;
	}
	public String getSegmentIndustryTop() {
		return segmentIndustryTop;
	}
	public void setSegmentIndustryTop(String segmentIndustryTop) {
		this.segmentIndustryTop = segmentIndustryTop;
	}
	public String getInitSco() {
		return initSco;
	}
	public void setInitSco(String initSco) {
		this.initSco = initSco;
	}
	public String getFinaSco() {
		return finaSco;
	}
	public void setFinaSco(String finaSco) {
		this.finaSco = finaSco;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getInternBs() {
		return internBs;
	}
	public void setInternBs(String internBs) {
		this.internBs = internBs;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFinalName() {
		return finalName;
	}
	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}
	public String getInternName() {
		return internName;
	}
	public void setInternName(String internName) {
		this.internName = internName;
	}
	public String getAssetReview() {
		return assetReview;
	}
	public void setAssetReview(String assetReview) {
		this.assetReview = assetReview;
	}
	public String getFinalBs() {
		return finalBs;
	}
	public void setFinalBs(String finalBs) {
		this.finalBs = finalBs;
	}
	public String getProCode() {
		return proCode;
	}
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	public String getCustcode() {
		return custcode;
	}
	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}
	public String getScoretemplateid() {
		return scoretemplateid;
	}
	public void setScoretemplateid(String scoretemplateid) {
		this.scoretemplateid = scoretemplateid;
	}
	public String getSegmentindustry() {
		return segmentindustry;
	}
	public void setSegmentindustry(String segmentindustry) {
		this.segmentindustry = segmentindustry;
	}
	public String getHighprecision() {
		return highprecision;
	}
	public void setHighprecision(String highprecision) {
		this.highprecision = highprecision;
	}
	public String getEnterprisehonor() {
		return enterprisehonor;
	}
	public void setEnterprisehonor(String enterprisehonor) {
		this.enterprisehonor = enterprisehonor;
	}
	public String getEconomic() {
		return economic;
	}
	public void setEconomic(String economic) {
		this.economic = economic;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInternlevel() {
		return internlevel;
	}
	public void setInternlevel(String internlevel) {
		this.internlevel = internlevel;
	}
	public String getFinallevel() {
		return finallevel;
	}
	public void setFinallevel(String finallevel) {
		this.finallevel = finallevel;
	}
	public String getPd() {
		return pd;
	}
	public void setPd(String pd) {
		this.pd = pd;
	}
	public String getRatingstatus() {
		return ratingstatus;
	}
	public void setRatingstatus(String ratingstatus) {
		this.ratingstatus = ratingstatus;
	}
	public String getFinaldate() {
		return finaldate;
	}
	public void setFinaldate(String finaldate) {
		this.finaldate = finaldate;
	}
	public String getProItemName() {
		return proItemName;
	}
	public void setProItemName(String proItemName) {
		this.proItemName = proItemName;
	}
	public String getAssetsLevl() {
		return assetsLevl;
	}
	public void setAssetsLevl(String assetsLevl) {
		this.assetsLevl = assetsLevl;
	}
	public String getCreditLevl() {
		return creditLevl;
	}
	public void setCreditLevl(String creditLevl) {
		this.creditLevl = creditLevl;
	}
	public String getMainLevl() {
		return mainLevl;
	}
	public void setMainLevl(String mainLevl) {
		this.mainLevl = mainLevl;
	}

	

}

