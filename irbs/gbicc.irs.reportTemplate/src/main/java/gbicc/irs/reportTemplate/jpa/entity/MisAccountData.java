package gbicc.irs.reportTemplate.jpa.entity;

public class MisAccountData {

	private String accoutType;
	
	private String accountNo;
	
	private Object accoutValue;

	
	public MisAccountData() {
		super();
	}


	public MisAccountData(String accoutType, String accountNo, Object accoutValue) {
		super();
		this.accoutType = accoutType;
		this.accountNo = accountNo;
		this.accoutValue = accoutValue;
	}


	public String getAccoutType() {
		return accoutType;
	}


	public void setAccoutType(String accoutType) {
		this.accoutType = accoutType;
	}


	public String getAccountNo() {
		return accountNo;
	}


	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	public Object getAccoutValue() {
		return accoutValue;
	}


	public void setAccoutValue(Object accoutValue) {
		this.accoutValue = accoutValue;
	}

}
