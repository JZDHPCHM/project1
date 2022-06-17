package com.gbicc.aicr.system.geval.function;

import java.util.List;

import com.gbicc.aicr.system.geval.Evaluator;


/**
 * A groups of functions that can loaded at one time into an instance of
 * Evaluator.
 */
public interface FunctionGroup {
	/**
	 * Returns the name of the function group.
	 * 
	 * @return The name of this function group class.
	 */
	public String getName();

	/**
	 * Returns a list of the functions that are loaded by this class.
	 * 
	 * @return A list of the functions loaded by this class.
	 */
	public List getFunctions();

	/**
	 * Loads the functions in this function group into an instance of Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator to load the functions into.
	 */
	public void load(Evaluator evaluator);

	/**
	 * Unloads the functions in this function group from an instance of
	 * Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator to unload the functions from.
	 */
	public void unload(Evaluator evaluator);
}