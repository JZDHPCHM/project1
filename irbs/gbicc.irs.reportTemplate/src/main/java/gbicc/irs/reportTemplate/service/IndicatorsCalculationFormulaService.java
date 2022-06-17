package gbicc.irs.reportTemplate.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccount;
import gbicc.irs.reportTemplate.jpa.entity.IndicatorsCalculationFormula;
import gbicc.irs.reportTemplate.jpa.repository.IndicatorsCalculationFormulaRepository;

public interface IndicatorsCalculationFormulaService extends DaoService<IndicatorsCalculationFormula, String, IndicatorsCalculationFormulaRepository> {

	/**
	 * 验证指标计算公式是否合法
	 * @param formula 公式
	 * @param parameters 参数
	 * @throws Exception
	 */
	void vaildateFormula(String formula,Map<String,Object> parameters) throws Exception;
	
	/**
	 * 初始化验证公式参数
	 * @param financialAccounts 财报科目
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> initializeVaildateParameters(List<FinancialAccount> financialAccounts) throws Exception;
	
	/**
	 * 根据显示公式转为可执行公式
	 * @param displayFormula 显示公式
	 * @param financialAccounts 财报科目
	 * @return
	 * @throws Exception
	 */
	String getExecuteFormula(String displayFormula,List<FinancialAccount> financialAccounts) throws Exception;
	
	/**
	 * 根据可执行公式转为显示公式
	 * @param executeFormula 可执行公式
	 * @param financialAccounts 财报科目
	 * @return
	 * @throws Exception
	 */
	String getDisplayFormula(String executeFormula,List<FinancialAccount> financialAccounts) throws Exception;
}
