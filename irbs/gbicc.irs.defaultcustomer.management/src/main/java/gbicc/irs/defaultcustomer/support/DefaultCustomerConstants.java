/**
 * 违约模块常量类配置读取系统配置文件
 */
package gbicc.irs.defaultcustomer.support;

import org.springframework.stereotype.Component;

@Component
public class DefaultCustomerConstants {
	
	
	
	public static String WYRDFQ= "CONTRACT_CUSTOMER";					//违约认定流程名称
	public static String WYRDFQREBIRTH= "CONTRACT_CUSTOMER_REBIRTH";	//违约重生流程名称
	public static String PWY= "DEFAULT_CONFIRM";					//违约流程名称
	public static String PRB= "DEFAULT_RE_BORN";					//违约流程名称
	public static String WY_LEVEL= "D";					//违约等级
	
	public static String JOB_STAUS_DTJ= "DTJ";				//待提交
	public static String JOB_STAUS_DSP="DSP";					//待审批
	public static String JOB_STAUS_WC="WC";					//完成
	public static String JOB_STAUS_TH="TH";					//退回
	
	public static String JOB_TYPE_RG="RG";					//人工发起
	public static String JOB_TYPE_XT="XT";					//系统发起	
}
