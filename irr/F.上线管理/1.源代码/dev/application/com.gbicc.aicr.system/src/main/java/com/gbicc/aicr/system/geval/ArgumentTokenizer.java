package com.gbicc.aicr.system.geval;

import java.util.Enumeration;

/**
 * This class allow for tokenizer methods to be called on a String of arguments.
 */
public class ArgumentTokenizer implements Enumeration {

	/**
	 * The default delimitor.
	 */
	public final char defaultDelimiter = EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR;

	// The arguments to be tokenized. This is updated every time the nextToken
	// method is called.
	private String arguments = null;

	// The separator between the arguments.
	private char delimiter = defaultDelimiter;

	/**
	 * Constructor that takes a String of arguments and a delimitoer.
	 * 
	 * @param arguments
	 *            The String of srguments to be tokenized.
	 * @param delimiter
	 *            The argument tokenizer.
	 */
	public ArgumentTokenizer(final String arguments, final char delimiter) {
		this.arguments = arguments;
		this.delimiter = delimiter;
	}

	/**
	 * Indicates if there are more elements.
	 * 
	 * @return True if there are more elements and false if not.
	 */
	public boolean hasMoreElements() {
		return hasMoreTokens();
	}

	/**
	 * Indicates if there are more tokens.
	 * 
	 * @return True if there are more tokens and false if not.
	 */
	public boolean hasMoreTokens() {

		if (arguments.length() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the next element.
	 * 
	 * @return The next element.
	 */
	public Object nextElement() {
		return nextToken();
	}

	/**
	 * Returns the next token.
	 * 
	 * @return The next element.
	 */
	public String nextToken() {
		int charCtr = 0;
		int size = arguments.length();
		int parenthesesCtr = 0;
		String returnArgument = null;

		// Loop until we hit the end of the arguments String.
		while (charCtr < size) {
			if (arguments.charAt(charCtr) == '(') {
				parenthesesCtr++;
			} else if (arguments.charAt(charCtr) == ')') {
				parenthesesCtr--;
			} else if (arguments.charAt(charCtr) == delimiter
					&& parenthesesCtr == 0) {

				returnArgument = arguments.substring(0, charCtr);
				arguments = arguments.substring(charCtr + 1);
				break;
			}

			charCtr++;
		}

		if (returnArgument == null) {
			returnArgument = arguments;
			arguments = "";
		}

		return returnArgument;
	}
}
