package gbicc.irs.reportTemplate.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.jpa.service.support.protocol.QueryParameter;

import com.googlecode.aviator.AviatorEvaluator;

import gbicc.irs.reportTemplate.jpa.entity.FinancialAccount;
import gbicc.irs.reportTemplate.jpa.entity.IndicatorsCalculationFormula;
import gbicc.irs.reportTemplate.jpa.repository.IndicatorsCalculationFormulaRepository;
import gbicc.irs.reportTemplate.service.FinancialAccountService;
import gbicc.irs.reportTemplate.service.IndicatorsCalculationFormulaService;

@Service
public class IndicatorsCalculationFormulaServiceImpl extends DaoServiceImpl<IndicatorsCalculationFormula, String, IndicatorsCalculationFormulaRepository>
		implements IndicatorsCalculationFormulaService {

	@Autowired
	private FinancialAccountService financialAccountService;
	
	@Override
	public IndicatorsCalculationFormula update(String primaryKey, IndicatorsCalculationFormula entity)
			throws Exception {
		if(StringUtils.hasText(primaryKey)) {
			return super.update(primaryKey, entity);
		}else {
			return super.add(entity);
		}
		
	}
	
	@Override
	public void vaildateFormula(String formula, Map<String, Object> parameters) throws Exception {
		try {
			 AviatorEvaluator.execute(formula,parameters);
		} catch (Exception e) {
			throw new Exception("指标计算公式无法验证通过，请您检查指标计算公式正确性！");
		}
	}

	@Override
	public Map<String, Object> initializeVaildateParameters(List<FinancialAccount> financialAccounts) throws Exception {
		if(financialAccounts != null) {
			Map<String, Object> parameters = new HashMap<String, Object>(financialAccounts.size()*2);
			for(FinancialAccount fa : financialAccounts) {
				parameters.put(fa.getFaCode(), 1);
				parameters.put("LAST_"+fa.getFaCode(), 1);
			}
			return parameters;
		}
		return null;
	}
	@Override
	public String getExecuteFormula(String displayFormula,List<FinancialAccount> financialAccounts) throws Exception {
		if(financialAccounts != null) {
			//排序
			financialAccounts.sort(new Comparator<FinancialAccount>() {
				public int compare(FinancialAccount arg0, FinancialAccount arg1) {
					if(arg0.getFaName().length() > arg1.getFaName().length()) {
						return -1;
					}else if(arg0.getFaName().length() < arg1.getFaName().length()) {
						return 1;
					}else {
						return 0;
					}
	            }
			});
			for(FinancialAccount fa : financialAccounts) {
				displayFormula = displayFormula.replace("上期_"+fa.getFaName(), "LAST_"+fa.getFaCode());
				displayFormula = displayFormula.replace(fa.getFaName(), fa.getFaCode());
			}
		}
		return displayFormula;
	}
	
	@Override
	public String getDisplayFormula(String executeFormula,List<FinancialAccount> financialAccounts) throws Exception {
		if(financialAccounts != null) {
			//排序
			financialAccounts.sort(new Comparator<FinancialAccount>() {
				public int compare(FinancialAccount arg0, FinancialAccount arg1) {
					if(arg0.getFaName().length() > arg1.getFaName().length()) {
						return -1;
					}else if(arg0.getFaName().length() < arg1.getFaName().length()) {
						return 1;
					}else {
						return 0;
					}
	            }
			});
			for(FinancialAccount fa : financialAccounts) {
				executeFormula = executeFormula.replace( "LAST_"+fa.getFaCode(),"上期_"+fa.getFaName());
				executeFormula = executeFormula.replace( fa.getFaCode(),fa.getFaName());
			}
		}
		return executeFormula;
	}

	@Override
	public Page<IndicatorsCalculationFormula> query(IndicatorsCalculationFormula queryExampleEntity,
			QueryParameter queryParameter) throws Exception {
		Page<IndicatorsCalculationFormula> pageData = super.query(queryExampleEntity, queryParameter);
		List<IndicatorsCalculationFormula> listData = pageData.getContent();
		for(IndicatorsCalculationFormula formula : listData) {
			List<FinancialAccount> financialAccounts = financialAccountService.queryAccoutByStatementTemplate(formula.getReportTemplate());
			formula.setDisplayCalculationFormula(this.getDisplayFormula(formula.getCalculationFormula(), financialAccounts));
		}
		return super.query(queryExampleEntity, queryParameter);
	}
	
}
