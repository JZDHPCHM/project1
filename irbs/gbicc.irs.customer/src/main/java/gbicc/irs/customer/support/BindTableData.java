package gbicc.irs.customer.support;

public class BindTableData {

	private String targetValue;   //指标值
	
	private Long financialReportDt; //指标期次
	
	private Integer dataType;   // 1 真实值  2 预测值
		
	private String custName;	//客户名称
	

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public Long getFinancialReportDt() {
		return financialReportDt;
	}

	public void setFinancialReportDt(Long financialReportDt) {
		this.financialReportDt = financialReportDt;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}
	
	
}
