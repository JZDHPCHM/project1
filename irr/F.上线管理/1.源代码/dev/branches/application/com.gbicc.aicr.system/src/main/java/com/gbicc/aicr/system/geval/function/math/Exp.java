package com.gbicc.aicr.system.geval.function.math;

import com.gbicc.aicr.system.geval.Evaluator;
import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.function.FunctionConstants;
import com.gbicc.aicr.system.geval.function.FunctionException;
import com.gbicc.aicr.system.geval.function.FunctionResult;




/**
 * This class is a function that executes within Evaluator. The function returns
 * the exponential number e (i.e., 2.718...) raised to the power of a double
 * value. See the Math.exp(double) method in the JDK for a complete description
 * of how this function works.
 */
public class Exp implements Function {
	/**
	 * Returns the name of the function - "exp".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "EXP";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted to a double value and
	 *            evaluated.
	 * 
	 * @return The the value e to the argument power, where e is the base of the
	 *         natural logarithms
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		Double result = null;
		Double number = null;

		try {
			number = new Double(arguments);
		} catch (Exception e) {
			throw new FunctionException("Invalid argument.", e);
		}

		result = new Double(Math.exp(number.doubleValue()));

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}
}