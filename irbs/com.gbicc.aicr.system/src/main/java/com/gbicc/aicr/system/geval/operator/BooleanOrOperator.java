package com.gbicc.aicr.system.geval.operator;

/**
 * The boolean or operator.
 */
public class BooleanOrOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public BooleanOrOperator() {
		super("||", 1);
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
		if (leftOperand == 1 || rightOperand == 1) {
			return 1;
		}

		return 0;
	}
}