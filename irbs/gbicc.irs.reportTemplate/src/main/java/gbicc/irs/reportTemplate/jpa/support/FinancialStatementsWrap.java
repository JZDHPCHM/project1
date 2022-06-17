package gbicc.irs.reportTemplate.jpa.support;

import java.util.Map;

/*
 * 财报包装类
 */
public class FinancialStatementsWrap {
	
	private String reportType;
	
	private String reportBussDate;
	
	private Map<String,Object> accountData;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportBussDate() {
		return reportBussDate;
	}

	public void setReportBussDate(String reportBussDate) {
		this.reportBussDate = reportBussDate;
	}

	public Map<String, Object> getAccountData() {
		return accountData;
	}

	public void setAccountData(Map<String, Object> accountData) {
		this.accountData = accountData;
	}
	
}
