package com.gbicc.aicr.system.geval.operator;

/**
 * The subtraction operator.
 */
public class SubtractionOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public SubtractionOperator() {
		super("-", 5, true);
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
		Double rtnValue = new Double(leftOperand - rightOperand);

		return rtnValue.doubleValue();
	}

	/**
	 * Evaluate one double operand.
	 * 
	 * @param operand
	 *            The operand being evaluated.
	 */
	@Override
	public double evaluate(final double operand) {
		return -operand;
	}
}