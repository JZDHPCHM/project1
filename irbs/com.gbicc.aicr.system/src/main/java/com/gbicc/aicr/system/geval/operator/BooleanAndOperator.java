package com.gbicc.aicr.system.geval.operator;

/**
 * The boolean and operator.
 */
public class BooleanAndOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public BooleanAndOperator() {
		super("&&", 2);
	}

	/**
	 * Evaluates two double operands.
	 * 
	 * @param leftOperand
	 *            The left operand being evaluated.
	 * @param rightOperand
	 *            The right operand being evaluated.
	 */
	@Override
	public double evaluate(final double leftOperand, final double rightOperand) {
		if (leftOperand == 1 && rightOperand == 1) {
			return 1;
		}

		return 0;
	}
}