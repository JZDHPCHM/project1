package com.gbicc.aicr.system.geval;

import com.gbicc.aicr.system.geval.operator.Operator;

/**
 * Represents an operator being processed in the expression.
 */
public class ExpressionOperator {

	// The operator that this object represents.
	private Operator operator = null;

	// The unary operator for this object, if there is one.
	private Operator unaryOperator = null;

	/**
	 * Creates a new ExpressionOperator.
	 * 
	 * @param operator
	 *            The operator this object represents.
	 * @param unaryOperator
	 *            The unary operator for this object.
	 */
	public ExpressionOperator(final Operator operator,
			final Operator unaryOperator) {
		this.operator = operator;
		this.unaryOperator = unaryOperator;
	}

	/**
	 * Returns the operator for this object.
	 * 
	 * @return The operator for this object.
	 */
	public Operator getOperator() {
		return operator;
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