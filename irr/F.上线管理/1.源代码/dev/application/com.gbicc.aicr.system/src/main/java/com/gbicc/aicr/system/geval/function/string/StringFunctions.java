package com.gbicc.aicr.system.geval.function.string;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gbicc.aicr.system.geval.Evaluator;
import com.gbicc.aicr.system.geval.function.Function;
import com.gbicc.aicr.system.geval.function.FunctionGroup;
import com.gbicc.aicr.system.util.FunctionCfg;
import com.gbicc.aicr.system.util.XMLAppParse;



/**
 * A groups of functions that can loaded at one time into an instance of
 * Evaluator. This group contains all of the functions located in the
 * net.sourceforge.jeval.function.string package.
 */
public class StringFunctions implements FunctionGroup {
	/**
	 * Used to store instances of all of the functions loaded by this class.
	 */
	private List functions = new ArrayList();

	/**
	 * Default contructor for this class. The functions loaded by this class are
	 * instantiated in this constructor.
	 */
	public StringFunctions() {
		
		functions.add(new CharAt());
		functions.add(new CompareTo());
		functions.add(new CompareToIgnoreCase());
		functions.add(new Concat());
		functions.add(new EndsWith());
		functions.add(new Equals());
		functions.add(new EqualsIgnoreCase());
		functions.add(new Eval());
		functions.add(new IndexOf());
		functions.add(new LastIndexOf());
		functions.add(new Length());
		functions.add(new Replace());
		functions.add(new StartsWith());
		functions.add(new Substring());
		functions.add(new ToLowerCase());
		functions.add(new ToUpperCase());
		functions.add(new Trim());
		
		/**
		functions.add(new QuestValue1());
		functions.add(new QuestValue2());
		functions.add(new QuestValue3());
		functions.add(new QuestValue4());
		functions.add(new QuestValue5());
		functions.add(new QuestionValue());
		functions.add(new DecisionValue());
		*/
		functions.add(new Exact());
		try {
			System.out.println();
			List<FunctionCfg> list=XMLAppParse.getFuntionList();
			for (int i = 0; i <list.size(); i++) {
				FunctionCfg fcfg=list.get(i);
				if(!fcfg.getType().equals("math")){
					functions.add(XMLAppParse.getFunctionObj(fcfg.getClassname()));
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns the name of the function group - "stringFunctions".
	 * 
	 * @return The name of this function group class.
	 */
	public String getName() {
		return "stringFunctions";
	}

	/**
	 * Returns a list of the functions that are loaded by this class.
	 * 
	 * @return A list of the functions loaded by this class.
	 */
	public List getFunctions() {
		return functions;
	}

	/**
	 * Loads the functions in this function group into an instance of Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator to load the functions into.
	 */
	public void load(final Evaluator evaluator) {
		Iterator functionIterator = functions.iterator();

		while (functionIterator.hasNext()) {
			evaluator.putFunction((Function) functionIterator.next());
		}
	}

	/**
	 * Unloads the functions in this function group from an instance of
	 * Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator to unload the functions from.
	 */
	public void unload(final Evaluator evaluator) {
		Iterator functionIterator = functions.iterator();

		while (functionIterator.hasNext()) {
			evaluator.removeFunction(((Function) functionIterator.next())
					.getName());
		}
	}
}
