package com.gbicc.aicr.system.geval.function.string;

import com.gbicc.aicr.system.geval.Evaluator;
import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.function.FunctionConstants;
import com.gbicc.aicr.system.geval.function.FunctionException;
import com.gbicc.aicr.system.geval.function.FunctionHelper;
import com.gbicc.aicr.system.geval.function.FunctionResult;




/**
 * This class is a function that executes within Evaluator. The function returns
 * the source string in upper case. See the String.toUpperCase() method in the
 * JDK for a complete description of how this function works.
 */
public class ToUpperCase implements Function {
	/**
	 * Returns the name of the function - "toUpperCase".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "toUpperCase";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into one string
	 *            argument. The string argument(s) HAS to be enclosed in quotes.
	 *            White space that is not enclosed within quotes will be
	 *            trimmed. Quote characters in the first and last positions of
	 *            any string argument (after being trimmed) will be removed
	 *            also. The quote characters used must be the same as the quote
	 *            characters used by the current instance of Evaluator. If there
	 *            are multiple arguments, they must be separated by a comma
	 *            (",").
	 * 
	 * @return The source string, converted to lowercase.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "One string argument is required.";

		try {
			String stringValue = arguments;

			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					stringValue, evaluator.getQuoteCharacter());

			result = argumentOne.toUpperCase();
		} catch (FunctionException fe) {
			throw new FunctionException(fe.getMessage(), fe);
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result, 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}