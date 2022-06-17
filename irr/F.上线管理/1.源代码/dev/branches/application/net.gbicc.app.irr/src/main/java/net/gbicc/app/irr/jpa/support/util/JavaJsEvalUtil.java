package net.gbicc.app.irr.jpa.support.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
* js引擎eval工具类
*
*/
public class JavaJsEvalUtil {

	private static final ScriptEngineManager manager = new ScriptEngineManager();
	private static final ScriptEngine scriptEngine = manager.getEngineByName("js");
	
	/**
	 * 计算公式
	 * @param formula 公式（数字的字符串）
	 * @return
	 */
	public static Object evalStr(String formula) {
		Object result = null;
		try {
			result = scriptEngine.eval(formula);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
