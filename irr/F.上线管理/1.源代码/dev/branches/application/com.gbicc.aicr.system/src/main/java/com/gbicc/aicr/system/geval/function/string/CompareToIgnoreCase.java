package com.gbicc.aicr.system.geval.function.string;

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
 * compares two strings lexicographically, ignoreing case considerations. See
 * the String.compareTo(String) method in the JDK for a complete description of
 * how this function works.
 */
public class CompareToIgnoreCase implements Function {
	/**
	 * Returns the name of the function - "compareToIgnoreCase".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "compareToIgnoreCase";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into two string
	 *            arguments. The first argument is the first string to compare
	 *            and the second argument is the second argument to compare. The
	 *            string argument(s) HAS to be enclosed in quotes. White space
	 *            that is not enclosed within quotes will be trimmed. Quote
	 *            characters in the first and last positions of any string
	 *            argument (after being trimmed) will be removed also. The quote
	 *            characters used must be the same as the quote characters used
	 *            by the current instance of Evaluator. If there are multiple
	 *            arguments, they must be separated by a comma (",").
	 * 
	 * @return Returns an integer value of zero if the strings are equal, an
	 *         integer value less than zero if the first string precedes the
	 *         second string or an integer value greater than zero if the first
	 *         string follows the second string.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		Integer result = null;
		String exceptionMessage = "Two string arguments are required.";

		ArrayList strings = FunctionHelper.getStrings(arguments, 
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);

		if (strings.size() != 2) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					(String) strings.get(0), evaluator.getQuoteCharacter());
			String argumentTwo = FunctionHelper.trimAndRemoveQuoteChars(
					(String) strings.get(1), evaluator.getQuoteCharacter());
			result = new Integer(argumentOne.compareToIgnoreCase(argumentTwo));
		} catch (FunctionException fe) {
			throw new FunctionException(fe.getMessage(), fe);
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}
}