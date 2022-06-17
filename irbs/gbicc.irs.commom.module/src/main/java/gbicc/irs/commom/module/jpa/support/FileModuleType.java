package gbicc.irs.commom.module.jpa.support;
/**
 * 汉口银行文件所属模块
 * @author koupengyang
 *
 */

public enum FileModuleType {
	COMPANY_RATING,    			//公司客户评级
	FINANCIAL_TRADE_RATING,		//金融同业评级
	ISSUANCE_ENTERPRISE_RATING,	//发债企业评级
	DEFAULT_CUSTOMER,			//违约客户认定
	REBIRTH_CUSTOMER,			//违约客户重生
	RISK_EXPOSURE 				//风险暴露
}
