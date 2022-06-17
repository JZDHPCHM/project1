package com.gbicc.aicr.system.geval.function.math;

import java.util.ArrayList;

import com.gbicc.aicr.system.geval.EvaluationConstants;
import com.gbicc.aicr.system.geval.Evaluator;
import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.function.FunctionConstants;
import com.gbicc.aicr.system.geval.function.FunctionException;
import com.gbicc.aicr.system.geval.function.FunctionHelper;
import com.gbicc.aicr.system.geval.function.FunctionResult;




/**
 * This class is a function that executes within Evaluator. The function
 * converts rectangular coordinates to polar. See the Math.atan2(double, double)
 * method in the JDK for a complete description of how this function works.
 */
public class Atan2 implements Function {
	/**
	 * Returns the name of the function - "atan2".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "ATAN2";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into two double
	 *            values and evaluated.
	 * 
	 * @return The arc tangent2 value of the argument.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		Double result = null;

		ArrayList numbers = FunctionHelper.getDoubles(arguments, 
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);

		if (numbers.size() != 2) {
			throw new FunctionException("Two numeric arguments are required.");
		}

		try {
			double argumentOne = ((Double) numbers.get(0)).doubleValue();
			double argumentTwo = ((Double) numbers.get(1)).doubleValue();
			result = new Double(Math.atan2(argumentOne, argumentTwo));
		} catch (Exception e) {
			throw new FunctionException("Two numeric arguments are required.", e);
		}

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}
}