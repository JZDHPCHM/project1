package com.gbicc.aicr.system.geval.function.math;

import com.gbicc.aicr.system.geval.Evaluator;
import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.function.FunctionConstants;
import com.gbicc.aicr.system.geval.function.FunctionException;
import com.gbicc.aicr.system.geval.function.FunctionResult;



/**
 * This class is a function that executes within Evaluator. The function returns
 * the absolute value of a double value. See the Math.abs(double) method in the
 * JDK for a complete description of how this function works.
 */
public class Abs implements Function {
	/**
	 * Returns the name of the function - "abs".
	 * 
	 * @return The name of this function class.
	 */
	private String name;
	
	public  Abs(){
		this.name=name;
	}
	public String getName() {
		return "ABS";
	}
	
	@Override
	public String toString()
	{
	   return "函数名："+name;
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
	 * @return The absolute value of the argument.
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

		result = new Double(Math.abs(number.doubleValue()));

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}
}