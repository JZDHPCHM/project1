package net.gbicc.app.xbrl.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
*	xbrl参数类型安全配置类
*/
@Component
@PropertySource("file:${dir.config}/xbrl/xbrl.properties")
@ConfigurationProperties(prefix="xbrl")
public class XBRLProperties {

	/**
	 * xbrl系统地址
	 */
	private String serverPath;
	/**
	 * xbrl查询权限
	 */
	private String queryGrant;
	/**
	 * xbrl注册请求地址
	 */
	private String registerRequest;
	/**
	 * xbrl初始化报告请求地址
	 */
	private String initReportRequest;
	/**
	 * xbrl查看报告请求地址
	 */
	private String viewReportRequest;
	/**
	 * xbrl权限请求参数
	 */
	private String grantParam;
	/**
	 * xbrl临时认证请求参数
	 */
	private String tokenParam;
	/**
	 * xbrl请求公司参数
	 */
	private String companyParam;
	/**
	 * xbrl报告类型请求参数
	 */
	private String reportTypeParam;
	/**
	 * xbrl报告截止时间请求参数
	 */
	private String reportEndDateParam;
	/**
	 * 保监会报告
	 */
	private String circReportCode;
	/**
	 * 偿付能力季度快报
	 */
	private String solvQuarBullCode;
	/**
	 * 偿付能力季度报告
	 */
	private String solvQuarRepoCode;
	/**
	 * 风险综合评级报告
	 */
	private String compRiskRateReportCode;
	/**
	 * 分支机构评级报告
	 */
	private String branchRateReportCode;
	/**
	 * 季度报告
	 */
	private String quarReportCode;
	
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	public String getQueryGrant() {
		return queryGrant;
	}
	public void setQueryGrant(String queryGrant) {
		this.queryGrant = queryGrant;
	}
	public String getRegisterRequest() {
		return registerRequest;
	}
	public void setRegisterRequest(String registerRequest) {
		this.registerRequest = registerRequest;
	}
	public String getInitReportRequest() {
		return initReportRequest;
	}
	public void setInitReportRequest(String initReportRequest) {
		this.initReportRequest = initReportRequest;
	}
	public String getViewReportRequest() {
		return viewReportRequest;
	}
	public void setViewReportRequest(String viewReportRequest) {
		this.viewReportRequest = viewReportRequest;
	}
	public String getGrantParam() {
		return grantParam;
	}
	public void setGrantParam(String grantParam) {
		this.grantParam = grantParam;
	}
	public String getTokenParam() {
		return tokenParam;
	}
	public void setTokenParam(String tokenParam) {
		this.tokenParam = tokenParam;
	}
	public String getCompanyParam() {
		return companyParam;
	}
	public void setCompanyParam(String companyParam) {
		this.companyParam = companyParam;
	}
	public String getReportTypeParam() {
		return reportTypeParam;
	}
	public void setReportTypeParam(String reportTypeParam) {
		this.reportTypeParam = reportTypeParam;
	}
	public String getReportEndDateParam() {
		return reportEndDateParam;
	}
	public void setReportEndDateParam(String reportEndDateParam) {
		this.reportEndDateParam = reportEndDateParam;
	}
	public String getCircReportCode() {
		return circReportCode;
	}
	public void setCircReportCode(String circReportCode) {
		this.circReportCode = circReportCode;
	}
	public String getSolvQuarBullCode() {
		return solvQuarBullCode;
	}
	public void setSolvQuarBullCode(String solvQuarBullCode) {
		this.solvQuarBullCode = solvQuarBullCode;
	}
	public String getSolvQuarRepoCode() {
		return solvQuarRepoCode;
	}
	public void setSolvQuarRepoCode(String solvQuarRepoCode) {
		this.solvQuarRepoCode = solvQuarRepoCode;
	}
	public String getCompRiskRateReportCode() {
		return compRiskRateReportCode;
	}
	public void setCompRiskRateReportCode(String compRiskRateReportCode) {
		this.compRiskRateReportCode = compRiskRateReportCode;
	}
	public String getBranchRateReportCode() {
		return branchRateReportCode;
	}
	public void setBranchRateReportCode(String branchRateReportCode) {
		this.branchRateReportCode = branchRateReportCode;
	}
	public String getQuarReportCode() {
		return quarReportCode;
	}
	public void setQuarReportCode(String quarReportCode) {
		this.quarReportCode = quarReportCode;
	}
}
