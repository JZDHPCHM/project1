package gbicc.irs.esb.service.response;

public class Response {
	
	// 系统状态标识
	private String ret_status;
	
	// 业务状态标识
	private String ret_code;
	
	// 调用结果描述
	private String ret_msg;

	public String getRet_status() {
		return ret_status;
	}

	public void setRet_status(String ret_status) {
		this.ret_status = ret_status;
	}

	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	
	

}
