package com.gbicc.aicr.system.geval.function.math;

import com.gbicc.aicr.system.geval.Evaluator;
import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.function.FunctionConstants;
import com.gbicc.aicr.system.geval.function.FunctionException;
import com.gbicc.aicr.system.geval.function.FunctionResult;



/**
 * This class is a function that executes within Evaluator. The function returns
 * a random double value greater than or equal to 0.0 and less than 1.0. See the
 * Math.random() method in the JDK for a complete description of how this
 * function works.
 */
public class Random implements Function {
	/**
	 * Returns the name of the function - "random".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "RANDOM";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            Not used.
	 * 
	 * @return A random double value greater than or equal to 0.0 and less than
	 *         1.0.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		Double result = new Double(Math.random());

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}
}