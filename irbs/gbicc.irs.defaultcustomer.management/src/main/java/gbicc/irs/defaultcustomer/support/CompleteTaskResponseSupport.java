package gbicc.irs.defaultcustomer.support;

import org.wsp.framework.flowable.support.CompleteTaskResponse;

public class CompleteTaskResponseSupport extends CompleteTaskResponse{
   private String strCode;

public String getStrCode() {
	return strCode;
}

public void setStrCode(String strCode) {
	this.strCode = strCode;
}
}
