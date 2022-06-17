package com.gbicc.aicr.system.geval;

import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.operator.Operator;

/**
 * This class represents a function that has been parsed.
 */
public class ParsedFunction {

	// The function that has been parsed.
	// FIXME Make all class instance methods final if possible.
	private final Function function;

	// The arguments to the function.
	private final String arguments;

	// The unary operator for this object, if there is one.
	private final Operator unaryOperator;

	/**
	 * The constructor for this class.
	 * 
	 * @param function
	 *            The function that has been parsed.
	 * @param arguments
	 *            The arguments to the function.
	 * @param unaryOperator
	 *            The unary operator for this object, if there is one.
	 */
	public ParsedFunction(final Function function, final String arguments,
			final Operator unaryOperator) {

		this.function = function;
		this.arguments = arguments;
		this.unaryOperator = unaryOperator;
	}

	/**
	 * Returns the function that has been parsed.
	 * 
	 * @return The function that has been parsed.
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * Returns the arguments to the function.
	 * 
	 * @return The arguments to the function.
	 */
	public String getArguments() {
		return arguments;
	}

	/**
	 * Returns the unary operator for this object, if there is one.
	 * 
	 * @return The unary operator for this object, if there is one.
	 */
	public Operator getUnaryOperator() {
		return unaryOperator;
	}
}