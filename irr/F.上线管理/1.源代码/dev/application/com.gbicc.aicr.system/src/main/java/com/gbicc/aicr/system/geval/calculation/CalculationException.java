package com.gbicc.aicr.system.geval.calculation;

/**
 * @project IMM
 * @since version 1.0
 * @company gbicc.net
 * @time 2011-3-11 下午02:51:29
 * @author Dexter.Qin
 * 
 */

public class CalculationException extends RuntimeException {
	private static final long serialVersionUID = 1956346912989368358L;

	public CalculationException() {
		super();
	}

	public CalculationException(String message) {
		super(message);
	}

	public CalculationException(String message, Throwable cause) {
		super(message, cause);
	}

}
