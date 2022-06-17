package com.gbicc.aicr.system.geval.function;

import com.gbicc.aicr.system.geval.Evaluator;

/**
 * A function that can be specified in an expression.
 */
public interface Function {

	/**
	 * Returns the name of the function.
	 * 
	 * @return The name of this function class.
	 */
	public String getName();

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            The arguments that will be evaluated by the function. It is up
	 *            to the function implementation to break the string into one or
	 *            more arguments.
	 * 
	 * @return The value of the evaluated argument and its type.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(Evaluator evaluator, String arguments)
			throws FunctionException;
}