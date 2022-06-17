package net.gbicc.app.irr.jpa.support.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* js引擎eval工具类
*
*/
public class JavaJsEvalUtil {

	private static final ScriptEngineManager manager = new ScriptEngineManager();
	private static final ScriptEngine scriptEngine = manager.getEngineByName("js");
    private static final Logger LOG = LogManager.getLogger(JavaJsEvalUtil.class);
	
	/**
	 * 计算公式
	 * @param formula 公式（数字的字符串）
	 * @return
	 */
	public static Object evalStr(String formula) {
		Object result = null;
		try {
            LOG.info(formula);
			result = scriptEngine.eval(formula);
		} catch (ScriptException e) {
			e.printStackTrace();
            LOG.error(e);
		}
		return result;
	}
	
}
