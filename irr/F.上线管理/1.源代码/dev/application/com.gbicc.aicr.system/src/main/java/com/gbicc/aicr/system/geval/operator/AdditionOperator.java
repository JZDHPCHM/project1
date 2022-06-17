package com.gbicc.aicr.system.geval.operator;

/**
 * The addition operator.
 */
public class AdditionOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public AdditionOperator() {
		super("+", 5, true);
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
		Double rtnValue = new Double(leftOperand + rightOperand);

		return rtnValue.doubleValue();
	}

	/**
	 * Evaluates two string operands.
	 * 
	 * @param leftOperand
	 *            The left operand being evaluated.
	 * @param rightOperand
	 *            The right operand being evaluated.
	 */
	@Override
	public String evaluate(final String leftOperand, final String rightOperand) {
		String rtnValue = new String(leftOperand.substring(0, leftOperand
				.length() - 1)
				+ rightOperand.substring(1, rightOperand.length()));

		return rtnValue;
	}

	/**
	 * Evaluate one double operand.
	 * 
	 * @param operand
	 *            The operand being evaluated.
	 */
	@Override
	public double evaluate(double operand) {
		return operand;
	}
}