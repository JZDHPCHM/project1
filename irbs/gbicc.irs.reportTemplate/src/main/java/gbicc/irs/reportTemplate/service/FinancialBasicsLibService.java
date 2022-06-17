package gbicc.irs.reportTemplate.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.reportTemplate.jpa.entity.FinancialBasicsLib;
import gbicc.irs.reportTemplate.jpa.repository.FinancialBasicsLibRepository;
import gbicc.irs.reportTemplate.jpa.support.FinancialStatementsWrap;

public interface FinancialBasicsLibService extends DaoService<FinancialBasicsLib, String, FinancialBasicsLibRepository> {

	/**
	 * 根据传入财报信息计算基础指标
	 * @param finStats 用于评级的客户财报
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> calculateBaseLib(List<FinancialStatementsWrap> finStats) throws Exception;
	
	/**
	 * 根据传入财报信息和其它参数计算基础指标
	 * @param finStats	财报
	 * @param parameters 参数
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> calculateBaseLib(List<FinancialStatementsWrap> finStats,Map<String, Object> parameters) throws Exception;
	
	/**
	 * 计算指定基础指标值
	 * @param baseListCode	指标集合
	 * @param finStats	财报
	 * @param parameters 没有特殊，可以为空
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> calculateAssignBaseLib(String[] baseListCode,List<FinancialStatementsWrap> finStats,Map<String, Object> parameters) throws Exception;
	
	
	/**
	 * 根据编号 名称 检查基础指标是否存在
	 * @param basicsCode
	 * @param basicsName
	 * @return
	 * @throws Exception
	 */
	boolean checkBasicsLibExist(String basicsCode,String basicsName) throws Exception;
	
}
