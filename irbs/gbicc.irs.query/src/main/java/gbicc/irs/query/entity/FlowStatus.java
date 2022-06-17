package gbicc.irs.query.entity;

import java.io.Serializable;

public class FlowStatus implements Serializable {
	
	private static final long serialVersionUID = 4029839662164971889L;

	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
