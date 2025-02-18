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
 * This class is a function that executes within Evaluator. The function returns
 * a new string with all of the occurances of the old character in the source
 * string replaced with the new character. See the String.replace(char, char)
 * method in the JDK for a complete description of how this function works.
 */
public class Replace implements Function {
	/**
	 * Returns the name of the function - "replace".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "replace";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted into one string and
	 *            two character arguments. The first argument is the source
	 *            string to replace the charactes in. The second argument is the
	 *            old character to replace in the source string. The third
	 *            argument is the new character to replace the old character
	 *            with in the source string. The string and character
	 *            argument(s) HAS to be enclosed in quotes. White space that is
	 *            not enclosed within quotes will be trimmed. Quote characters
	 *            in the first and last positions of any string argument (after
	 *            being trimmed) will be removed also. The quote characters used
	 *            must be the same as the quote characters used by the current
	 *            instance of Evaluator. If there are multiple arguments, they
	 *            must be separated by a comma (",").
	 * 
	 * @return Returns a string with every occurence of the old character
	 *         replaced with the new character.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		String result = null;
		String exceptionMessage = "One string argument and two character "
				+ "arguments are required.";

		ArrayList values = FunctionHelper.getStrings(arguments, 
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);

		if (values.size() != 3) {
			throw new FunctionException(exceptionMessage);
		}

		try {
			String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(0), evaluator.getQuoteCharacter());

			String argumentTwo = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(1), evaluator.getQuoteCharacter());

			String argumentThree = FunctionHelper.trimAndRemoveQuoteChars(
					(String) values.get(2), evaluator.getQuoteCharacter());

			char oldCharacter = ' ';
			if (argumentTwo.length() == 1) {
				oldCharacter = argumentTwo.charAt(0);
			} else {
				throw new FunctionException(exceptionMessage);
			}

			char newCharacter = ' ';
			if (argumentThree.length() == 1) {
				newCharacter = argumentThree.charAt(0);
			} else {
				throw new FunctionException(exceptionMessage);
			}

			result = argumentOne.replace(oldCharacter, newCharacter);
		} catch (FunctionException fe) {
			throw new FunctionException(fe.getMessage(), fe);
		} catch (Exception e) {
			throw new FunctionException(exceptionMessage, e);
		}

		return new FunctionResult(result, 
				FunctionConstants.FUNCTION_RESULT_TYPE_STRING);
	}
}