package com.gbicc.aicr.system.geval.function.string;

import com.gbicc.aicr.system.geval.EvaluationException;
import com.gbicc.aicr.system.geval.Evaluator;
import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.function.FunctionConstants;
import com.gbicc.aicr.system.geval.function.FunctionException;
import com.gbicc.aicr.system.geval.function.FunctionResult;



/**
 * This class is a function that executes within Evaluator. The function returns
 * the result of a Evaluator compatible expression. See the
 * Evaluator.evaluate(String) method for a complete description of how this
 * function works.
 */
public class Eval implements Function {
	/**
	 * Returns the name of the function - "eval".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "eval";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of evaluator.
	 * @param arguments
	 *            A string expression that is compatible with Evaluator. *** THE
	 *            STRING ARGUMENT SHOULD NOT BE ENCLOSED IN QUOTES OR THE
	 *            EXPRESSION MAY NOT BE EVALUATED CORRECTLY.*** *** FUNCTION
	 *            CALLS ARE VALID WITHIN THE EVAL FUNCTION. ***
	 * 
	 * @return The evaluated result fot the input expression.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator,
			final String arguments) throws FunctionException {
		String result = null;

		try {
			result = evaluator.evaluate(arguments, false, true);
		} catch (EvaluationException ee) {
			throw new FunctionException(ee.getMessage(), ee);
		}

		int resultType = FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC;
		try {
			Double.parseDouble(result);
		} catch (NumberFormatException nfe) {
			resultType = FunctionConstants.FUNCTION_RESULT_TYPE_STRING;
		}

		return new FunctionResult(result, resultType);
	}
}