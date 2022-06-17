package com.gbicc.aicr.system.geval.operator;

/**
 * The modulus operator.
 */
public class ModulusOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public ModulusOperator() {
		super("%", 6);
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
		Double rtnValue = new Double(leftOperand % rightOperand);

		return rtnValue.doubleValue();
	}
}