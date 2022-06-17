package com.gbicc.aicr.system.geval.calculation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gbicc.aicr.system.geval.EvaluationConstants;

public class FormulaUtils {

	/**
	 *  Parse the formula to base formula
	 *  </p>
	 * @param formula
	 * @param dataMap
	 * @return the parsed formula
	 */
	public static String doParse(IrsPara formula, Map dataMap) {
		Set<String> vVar = new HashSet<String>();
		Set<String> baseVar = new HashSet<String>();
		String strFormula = formula.getExpression();
		if (formula.isUnary()) {
			return EvaluationConstants.OPEN_VARIABLE + formula.getCode()
					+ EvaluationConstants.CLOSED_VARIABLE;
		}
		boolean flag = true;
		while (flag) {
			parse(strFormula, vVar);

			for (Iterator iterator = vVar.iterator(); iterator.hasNext();) {
				String strVal = (String) iterator.next();
				strFormula = strFormula.replace(
						EvaluationConstants.OPEN_VARIABLE + strVal
								+ EvaluationConstants.CLOSED_VARIABLE,
						EvaluationConstants.OPEN_CIRCLE_BRACE + strVal
								+ EvaluationConstants.CLOSED_CIRCLE_BRACE);
			}

			flag = false;
			for (Iterator iterator = vVar.iterator(); iterator.hasNext();) {
				String strVar = (String) iterator.next();
				if (dataMap.get(strVar) != null) {
					if (((IrsPara) dataMap.get(strVar)).isUnary()) {

					} else {
						strFormula = strFormula
								.replace(strVar,
										((IrsPara) dataMap.get(strVar))
												.getExpression());
						flag = true;
					}
				}
			}
			baseVar.addAll(vVar);
			vVar.clear();
		}
		for (Iterator iterator = baseVar.iterator(); iterator.hasNext();) {
			String strVar = (String) iterator.next();
			strFormula = strFormula.replace(strVar,
					EvaluationConstants.OPEN_VARIABLE + strVar
							+ EvaluationConstants.CLOSED_VARIABLE);
		}

		baseVar.clear();
		vVar.clear();
		return strFormula;
	}

	/**
	 * 
	 * @param strFormula
	 * @param sVar
	 */
	private static void parse(String strFormula, Set sVar) {
		if (strFormula != null) {
			int index1 = strFormula.indexOf(EvaluationConstants.OPEN_VARIABLE);
			int index2 = strFormula
					.indexOf(EvaluationConstants.CLOSED_VARIABLE);
			while (index1 != -1 && index2 != -1 && index2 > index1) {
				String strVar = strFormula.substring(index1 + 2, index2);
				strFormula = strFormula.substring(index2 + 1);
				if (!sVar.contains(strVar)) {
					sVar.add(strVar);
				}
				index1 = strFormula.indexOf(EvaluationConstants.OPEN_VARIABLE);
				index2 = strFormula
						.indexOf(EvaluationConstants.CLOSED_VARIABLE);
			}
		}
	}
	
	

	/**
	 * <p>
	 * 生成问题值参数的公式
	 * </p>
	 * eg: APP4GQV1('40284a562d5ea386012d5ea410430001','#{PS001}') 计算规则：
	 * QUESTION_CHOOSE_PRODUCER + valueSno + "('" + dimensionId + "','" +
	 * optionSno + "')";
	 * 
	 * @param dimensionId
	 *            指标值
	 * @param optionParamCode
	 *            问题选项值参数的code（既选的第几个问题）
	 * @param valueSno
	 *            选择的选项的第几个值
	 * @return
	 */
	/**
	 * <p>
	 * 生成问题值参数的公式
	 * </p>
	 * eg: QUESTION('40284a562d5ea386012d5ea410430001','#{PS001}', '1') 计算规则：
	 * QUESTION_CHOOSE_PRODUCER  + "('" + dimensionId + "','" +
	 * optionSno + "','" + valueSno+"')";
	 * 
	 * @param dimensionId
	 *            指标值
	 * @param optionParamCode
	 *            问题选项值参数的code（既选的第几个问题）
	 * @param valueSno
	 *            选择的选项的第几个值
	 * @return
	 
	public static String genderQuestionParamFormula(String dimensionId,
			String optionParamCode, int valueSno) {
		QuestionValue question = new QuestionValue();
		StringBuffer sb = new StringBuffer();
		sb.append(question.getName());
		sb.append(EvaluationConstants.OPEN_CIRCLE_BRACE);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(dimensionId);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.COMMA);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.OPEN_VARIABLE);
		sb.append(optionParamCode);
		sb.append(EvaluationConstants.CLOSED_VARIABLE);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.COMMA);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(valueSno);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.CLOSED_CIRCLE_BRACE);
		return sb.toString();
	}
*/
	/**
	 * <p>
	 * 生成决策值参数的公式
	 * </p>
	 * eg: DECISION("dimsnionId", "35", "1"); 计算规则： Decision.getName() + "('" +
	 * dimensionId + "','" + optionSno + "')";
	 * 
	 * @param dimensionId
	 *            指标值
	 * @param optionParamCode
	 *            问题选项值参数的code（既选的第几个问题）
	 * @param valueSno
	 *            选择的选项的第几个值
	 * @return
	
	public static String genderJCParamFormula(Dimension dimension,
			String baseValueParamCode, int valueSno) {
		DecisionValue decision = new DecisionValue();
		StringBuffer sb = new StringBuffer();
		sb.append(decision.getName());
		sb.append(EvaluationConstants.OPEN_CIRCLE_BRACE);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(dimension.getUuid());
		sb.append(EvaluationConstants.AUGUMENT_SPLIT_CHAR);
		sb.append(dimension.getRangeType());
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.COMMA);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.OPEN_VARIABLE);
		sb.append(baseValueParamCode);
		sb.append(EvaluationConstants.CLOSED_VARIABLE);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.COMMA);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(valueSno);
		sb.append(EvaluationConstants.SINGLE_QUOTE);
		sb.append(EvaluationConstants.CLOSED_CIRCLE_BRACE);
		return sb.toString();
	}
	 */
	public static void main(String[] args) {
		String k1 = "#{a}+#{b}+#{c}";
		String c = "#{a}+#{d}";
		String d = "#{m}+#{e}";

		IrsPara irpK1 = new IrsPara("k1", false, "#{a}+#{b}+#{c}", 4);
		IrsPara irpC = new IrsPara("c", false, "#{a}+#{d}", 3);
		IrsPara irpD = new IrsPara("d", false, "#{m}+#{e}", 2);

		IrsPara irpDD = new IrsPara("dd", false, "#{k1}+#{f}", 1);

		Map dataMap = new HashMap();
		dataMap.put(irpK1.getCode(), irpK1);
		dataMap.put(irpC.getCode(), irpC);
		dataMap.put(irpD.getCode(), irpD);

		System.out.println(FormulaUtils.doParse(irpK1, dataMap));
		irpK1.setUnary(false);
		dataMap.put(irpK1.getCode(), irpK1);
		System.out.println(FormulaUtils.doParse(irpDD, dataMap));

	}

}