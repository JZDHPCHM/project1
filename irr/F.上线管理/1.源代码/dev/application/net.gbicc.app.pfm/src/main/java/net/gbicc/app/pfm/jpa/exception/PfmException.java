package net.gbicc.app.pfm.jpa.exception;
/**
*	绩效自定义异常
*/
public class PfmException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PfmException() {
		super();
	}

	public PfmException(String message) {
		super(message);
	}

	
}
