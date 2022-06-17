package gbicc.irs.esb.service.request;

/**
 * 财报科目
 * @author ljc
 *
 */
public class RatingFinancialSubjectRequest {

	// 科目分类
	private String accountCategory;

	// 行号代码
	private String lineCode;

	// 数据项代码
	private String dataitemCode;

	// 数据项名称
	private String dataitemName;

	// 期初值
	private String beginValue;

	// 期末值
	private String endValue;

	public String getAccountCategory() {
		return accountCategory;
	}

	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getDataitemCode() {
		return dataitemCode;
	}

	public void setDataitemCode(String dataitemCode) {
		this.dataitemCode = dataitemCode;
	}

	public String getDataitemName() {
		return dataitemName;
	}

	public void setDataitemName(String dataitemName) {
		this.dataitemName = dataitemName;
	}

	public String getBeginValue() {
		return beginValue;
	}

	public void setBeginValue(String beginValue) {
		this.beginValue = beginValue;
	}

	public String getEndValue() {
		return endValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}
	
	

}
