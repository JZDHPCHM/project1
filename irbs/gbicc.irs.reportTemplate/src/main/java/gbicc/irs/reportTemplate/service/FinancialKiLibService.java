package gbicc.irs.reportTemplate.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinancialKiLib;
import gbicc.irs.reportTemplate.jpa.repository.FinancialKiLibRepository;
import gbicc.irs.reportTemplate.jpa.support.FinancialStatementsWrap;

public interface FinancialKiLibService extends DaoService<FinancialKiLib, String, FinancialKiLibRepository> {

	/**
	 * 计算指标值
	 * @param finStats
	 * @return
	 */
	Map<String, Object> calculateKiLib(List<FinancialStatementsWrap> finStats) throws Exception;
	
	/**
	 * 计算指标值
	 * @param finStats 财报
	 * @param parameters 计算参数
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> calculateKiLib(List<FinancialStatementsWrap> finStats,Map<String,Object> parameters) throws Exception;
	
}
