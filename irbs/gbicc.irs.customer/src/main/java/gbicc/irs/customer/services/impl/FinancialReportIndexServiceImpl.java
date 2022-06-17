package gbicc.irs.customer.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.customer.entity.FinancialReportIndex;
import gbicc.irs.customer.entity.FinancialStatements;
import gbicc.irs.customer.repository.FinancialReportIndexRepository;
import gbicc.irs.customer.service.FinancialReportIndexService;
import gbicc.irs.customer.support.BenchmarkingCompany;
import gbicc.irs.customer.support.BindTableData;



@Service("financialReportIndexService")
public class FinancialReportIndexServiceImpl
		extends
		DaoServiceImpl<FinancialReportIndex, String, FinancialReportIndexRepository>
		implements FinancialReportIndexService {
	
	@Autowired 
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public Map<String, Object> queryReportDtAndReportType(String reportDt, String reportType,BenchmarkingCompany benchmarkingCompany,String ctmNo) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		List<FinancialReportIndex> targets = repository.findByCustomerNoAndReportSpecifAndReportDate(ctmNo, reportType, reportDt);
		if(targets==null){
			map.put("result", false);
			return map;
		}
		map.put("targets", targets);
		Map<String, Map<String, Map<String, List<BindTableData>>>> targetMap=new HashMap<String, Map<String, Map<String, List<BindTableData>>>>();
		Map<String, List<BindTableData>> mapData1=null;
		for (FinancialReportIndex financialReportTarget : targets) {
			mapData1=new HashMap<String, List<BindTableData>>();
			//指标值
			mapData1.put("actualValue", this.wrapIndexData(ctmNo, reportDt.toString(), reportType, financialReportTarget.getIndexCode(), "1"));
			//指标值行业均值
			mapData1.put("IndustryValue", this.wrapIndexData(ctmNo, reportDt.toString(), reportType, financialReportTarget.getIndexCode(), "2"));
			//mapData1.put("actualValue", creatMap(financialStatements, financialReportTarget, 1,reportType, ctmNo));
			//mapData1.put("IndustryValue", creatMap(financialStatements, financialReportTarget, 2,reportType, ctmNo));
			Map<String, Map<String, List<BindTableData>>>  brokenLinetableData=new HashMap<String, Map<String, List<BindTableData>>>();
			brokenLinetableData.put("brokenLinetableData", mapData1);
			mapData1=new HashMap<String, List<BindTableData>>();
			if(BenchmarkingCompany.TOP10==benchmarkingCompany){
				mapData1.put("ranking", this.wrapTop10IndexData(ctmNo, reportDt.toString(), reportType, financialReportTarget.getIndexCode()));
				//mapData1.put("ranking", creatTop100Map(financialStatements, financialReportTarget,companyCustomer.getModelCode()));
			}else if(BenchmarkingCompany.ADJACENT10==benchmarkingCompany){
				mapData1.put("ranking", this.wrapAdjacent10IndexData(ctmNo, reportDt.toString(), reportType, financialReportTarget.getIndexCode()));
				//mapData1.put("ranking", creatDysmMap(financialStatements, financialReportTarget, ctmNo,companyCustomer.getModelCode()));
			}
			brokenLinetableData.put("ranking", mapData1);
			targetMap.put(financialReportTarget.getIndexCode(),brokenLinetableData );
		}
		map.put("datas", targetMap);
		map.put("result", true);
		return map;
	}

	/**
	 * 封装指标值(5期)
	 * @param custNo
	 * @param reportDate
	 * @param specifications
	 * @param indexCode
	 * @param indexCategory 分类(1:指标值2 指标行业均值)
	 * @return
	 */
	private List<BindTableData> wrapIndexData(String custNo,String reportDate,String specifications,
			String indexCode,String indexCategory) {
		String indexValue = "a.fd_index_value";
		if(StringUtils.hasText(indexCategory)&&indexCategory.equals("2")) {
			indexValue = "a.fd_industry_average";
		}
		String sql = 
				"select T.* from\n" +
						"(\n" + 
						"select\n" + 
						 indexValue+" as targetValue,\n" + 
						"a.fd_financial_report_date as financialReportDt,\n" + 
						"1 as dataType\n" + //实际值
						"from NS_FINANCIAL_REPORT_INDEX a\n" + 
						"where a.fd_customer_no='"+custNo+"' and a.fd_specifications='"+specifications+"'\n" + 
						"and a.fd_index_code='"+indexCode+"'\n" + 
						"and substr(a.fd_financial_report_date,1,4)>=substr("+reportDate+",1,4)-4\n" + 
						"and substr(a.fd_financial_report_date,1,4)<=substr("+reportDate+",1,4)\n" + 
						"union all\n" + 
						"select\n" + 
						"avg("+indexValue+") as targetValue,\n" + 
						"substr(max(a.fd_financial_report_date),1,4)+1||'12' as financialReportDt,\n" + 
						"2 as dataType\n" +  //预测值
						"from NS_FINANCIAL_REPORT_INDEX a\n" + 
						"where a.fd_customer_no='"+custNo+"' and a.fd_specifications='"+specifications+"'\n" + 
						"and a.fd_index_code='"+indexCode+"'\n" + 
						"and substr(a.fd_financial_report_date,1,4)>=substr("+reportDate+",1,4)-4\n" + 
						"and substr(a.fd_financial_report_date,1,4)<=substr("+reportDate+",1,4)\n" + 
						") T order by T.financialReportDt asc";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<BindTableData>(BindTableData.class));
	}
	
	/**
	 * 包装top10指标
	 * @param custNo
	 * @param reportDate
	 * @param specifications
	 * @param indexCode
	 * @return
	 */
	private List<BindTableData> wrapTop10IndexData(String custNo,String reportDate,String specifications,String indexCode) {
		String sql =	
				"select i.* from\n" +
						"(\n" +  
						"select T.custName,T.targetValue from (\n" +
						"select c.fd_custname as custName,a.fd_index_value as targetValue ,rownum as rnum\n" + 
						"from NS_FINANCIAL_REPORT_INDEX a,Ns_Customer c\n" + 
						"where a.fd_customer_no=c.fd_custno\n" + 
						"and a.fd_index_code ='"+indexCode+"'\n" + 
						"and a.fd_financial_report_date='"+reportDate+"'\n" + 
						"and a.fd_specifications='"+specifications+"'\n" + 
						"order by a.fd_corporate_rankings) T where T.rnum<=10\n" + 
						"union all\n" + 
						"select c.fd_custname as custName,a.fd_index_value as targetValue\n" + 
						"from NS_FINANCIAL_REPORT_INDEX a,Ns_Customer c\n" + 
						"where a.fd_customer_no=c.fd_custno\n" + 
						"and a.fd_index_code ='"+indexCode+"'\n" + 
						"and a.fd_financial_report_date='"+reportDate+"'\n" + 
						"and a.fd_specifications='"+specifications+"'\n" + 
						"and a.fd_customer_no='"+custNo+"'\n" +
						") i order by i.targetValue asc";
		
		List<BindTableData>  list = new ArrayList<BindTableData>();
		try {
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<BindTableData>(BindTableData.class));
			return list;
		} catch (Exception e) {
			return list;
		}
		
	}
	
	/**
	 * 包装相邻10指标
	 * @param custNo
	 * @param reportDate
	 * @param specifications
	 * @param indexCode
	 * @return
	 */
	private List<BindTableData> wrapAdjacent10IndexData(String custNo,String reportDate,String specifications,String indexCode) {
		String sql = "select c.fd_custname as custName,i.targetValue from (\n" +
						"select t.fd_customer_no as custNo,t.fd_index_value as targetValue from NS_FINANCIAL_REPORT_INDEX t\n" + 
						"where t.fd_corporate_rankings>=(\n" + 
						"select a.fd_corporate_rankings-5 from NS_FINANCIAL_REPORT_INDEX a\n" + 
						"where a.fd_index_code ='"+indexCode+"'\n" + 
						"and a.fd_financial_report_date='"+reportDate+"'\n" + 
						"and a.fd_specifications='"+specifications+"'\n" + 
						"and a.fd_customer_no='"+custNo+"'\n" + 
						")\n" + 
						"and t.fd_corporate_rankings<=\n" + 
						"(\n" + 
						"select a.fd_corporate_rankings+5 from NS_FINANCIAL_REPORT_INDEX a\n" + 
						"where a.fd_index_code ='"+indexCode+"'\n" + 
						"and a.fd_financial_report_date='"+reportDate+"'\n" + 
						"and a.fd_specifications='"+specifications+"'\n" + 
						"and a.fd_customer_no='"+custNo+"'\n" + 
						") \n" + 
						"and t.fd_financial_report_date='"+reportDate+"'\n" + 
						"and t.fd_index_code='"+indexCode+"'\n" + 
						"and t.fd_specifications='"+specifications+"') i,\n" + 
						"ns_customer c where i.custNo=c.fd_custno\n" + 
						"and c.fd_model_code =\n" + 
						"(\n" + 
						"select cc.fd_model_code from ns_customer cc where cc.fd_custno='"+custNo+"'\n" + 
						") order by i.targetValue asc";

		List<BindTableData>  list = new ArrayList<BindTableData>();
		try {
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<BindTableData>(BindTableData.class));
			return list;
		} catch (Exception e) {
			return list;
		}
	}

	@Override
	public List<String> queryCustomerIndexReportDate(String customerNo) {
		String sql = "select t.fd_financial_report_date from NS_FINANCIAL_REPORT_INDEX t\n" +
						"where t.fd_customer_no='"+customerNo+"'\n" + 
						"group by t.fd_financial_report_date\n" + 
						"order by t.fd_financial_report_date desc";
		List<String> years = jdbcTemplate.queryForList(sql, String.class);
		return years;
	}
	
}
