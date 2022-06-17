package com.gbicc.aicr.system.geval;

import com.gbicc.aicr.system.geval.operator.Operator;

/**
 * Represents an operand being processed in the expression.
 */
public class ExpressionOperand {

	// The value of the operand.
	private String value = null;

	// The unary operator for the operand, if one exists.
	private Operator unaryOperator = null;

	/**
	 * Create a new ExpressionOperand.
	 * 
	 * @param value
	 *            The value for the new ExpressionOperand.
	 * @param unaryOperator
	 *            The unary operator for this object.
	 */
	public ExpressionOperand(final String value, final Operator unaryOperator) {
		this.value = value;
		this.unaryOperator = unaryOperator;
	}

	/**
	 * Returns the value of this object.
	 * 
	 * @return The value of this object.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the unary operator for this object.
	 * 
	 * @return The unary operator for this object.
	 */
	public Operator getUnaryOperator() {
		return unaryOperator;
	}
}