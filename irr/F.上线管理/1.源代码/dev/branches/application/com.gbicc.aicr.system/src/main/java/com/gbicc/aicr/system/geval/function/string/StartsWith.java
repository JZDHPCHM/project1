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
 * This class is a function that executes within Evaluator. The function tests
 * if the string starts with a specified prefix beginning at a specified index.
 * See the String.startsWith(String, int) method in the JDK for a complete
 * description of how this function works.
 */
public class StartsWith implements Function {
	/**
	 * Returns the name of the function - "startsWith".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "startsWith";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into two string
	 *            arguments and one integer argument. The first argument is the
	 *            string to test, the second argument is the prefix and the
	 *            third argument is the index to start at. The string
	 *            argument(s) HAS to be enclosed in quotes. White space that is
	 *            not enclosed within quotes will be trimmed. Quote characters
	 *            in the first and last positions of any string argument (after
	 *            being trimmed) will be removed also. The quote characters used
	 *            must be the same as the quote characters used by the current
	 *            instance of Evaluator. If there are multiple arguments, they
	 *            must be separated by a comma (",").
	 * 
	 * @return Returns "1.0" (true) if the string ends with the suffix,
	 *         otherwise it returns "0.0" (false). The return value respresents
	 *         a Boolean value that is compatible with the Boolean operators
	 *         used by Evaluator.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "Two string arguments and one integer "
				+ "argument are required.";

		ArrayList values = FunctionHelper.getTwoStringsAndOneInteger(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);

		if (values.size() != 3) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(0), evaluator.getQuoteCharacter());
			String argumentTwo = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(1), evaluator.getQuoteCharacter());
			int index = ((Integer) values.get(2)).intValue();

			if (argumentOne.startsWith(argumentTwo, index)) {
				result = EvaluationConstants.BOOLEAN_STRING_TRUE;
			} else {
				result = EvaluationConstants.BOOLEAN_STRING_FALSE;
			}
		} catch (FunctionException fe) {
			throw new FunctionException(fe.getMessage(), fe);
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result, 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}
}