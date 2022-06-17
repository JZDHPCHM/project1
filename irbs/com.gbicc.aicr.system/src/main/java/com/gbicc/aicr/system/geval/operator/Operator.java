package com.gbicc.aicr.system.geval.operator;

import com.gbicc.aicr.system.geval.EvaluationException;

/**
 * An oerator than can specified in an expression.
 */
public interface Operator {

	/**
	 * Evaluates two double operands.
	 * 
	 * @param leftOperand
	 *            The left operand being evaluated.
	 * @param rightOperand
	 *            The right operand being evaluated.
	 */
	public abstract double evaluate(double leftOperand, double rightOperand);

	/**
	 * Evaluates two string operands.
	 * 
	 * @param leftOperand
	 *            The left operand being evaluated.
	 * @param rightOperand
	 *            The right operand being evaluated.
	 * 
	 * @return String The value of the evaluated operands.
	 * 
	 * @exception EvaluateException
	 *                Thrown when an error is found while evaluating the
	 *                expression.
	 */
	public abstract String evaluate(final String leftOperand,
			final String rightOperand) throws EvaluationException;

	/**
	 * Evaluate one double operand.
	 * 
	 * @param operand
	 *            The operand being evaluated.
	 */
	public abstract double evaluate(final double operand);

	/**
	 * Returns the character(s) that makes up the operator.
	 * 
	 * @return The operator symbol.
	 */
	public abstract String getSymbol();

	/**
	 * Returns the precedence given to this operator.
	 * 
	 * @return The precedecne given to this operator.
	 */
	public abstract int getPrecedence();

	/**
	 * Returns the length of the operator symbol.
	 * 
	 * @return The length of the operator symbol.
	 */
	public abstract int getLength();

	/**
	 * Returns an indicator of if the operator is unary or not.
	 * 
	 * @return An indicator of if the operator is unary or not.
	 */
	public abstract boolean isUnary();
}