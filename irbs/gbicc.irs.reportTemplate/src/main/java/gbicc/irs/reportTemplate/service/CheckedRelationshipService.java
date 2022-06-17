package gbicc.irs.reportTemplate.service;

import java.util.List;
import java.util.Map;

import gbicc.irs.customer.entity.FinancialStatements;

public interface CheckedRelationshipService {
	
	/**
	 * 
	 * @param results 母项编码既子项编码结果集
	 * @param formula 子项编码运算
	 * @return 返回结果集。
	 * @1.数据格式 A001=1,A002=2,A003=3 --> Map<A001,1>
	 * @2.返回数据格式 A001=true,A002=false --> Map<A001,true>
	 * @3.每期传入的模板是否会重复
	 */
	Map<String,Map<String,String>> CheckedRelationship(List<FinancialStatements> finan);
	
	/**
	 * 勾稽关系校验
	 * @param parameter   所有科目
	 * @param reportType  财报类型
	 * @param reportType  财报时间
	 * @return
	 */
	List<String> CheckedRelationship(Map<String,Object> param,String reportType,String reportDate);

}
