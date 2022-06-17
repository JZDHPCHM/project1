package com.gbicc.aicr.system.geval.operator;

/**
 * The boolean not operator.
 */
public class BooleanNotOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public BooleanNotOperator() {
		super("!", 0, true);
	}

	/**
	 * Evaluate one double operand.
	 * 
	 * @param operand
	 *            The operand being evaluated.
	 */
	@Override
	public double evaluate(final double operand) {
		if (operand == 1) {
			return 0;
		}

		return 1;
	}
}