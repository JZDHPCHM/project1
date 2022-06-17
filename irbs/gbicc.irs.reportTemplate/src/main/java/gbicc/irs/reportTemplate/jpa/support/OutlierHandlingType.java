package gbicc.irs.reportTemplate.jpa.support;

/**
 * 指标异常值处理类型
 * @author koupengyang
 *
 */
public enum OutlierHandlingType {
	
//	特殊处理1	del_nega_rec：1
//	若分母<0，则指标=“缺失”；
//	若分母=0，则指标=“分子”（分母置为1）；
	DEAL_ONE,
	
//	特殊处理2	del_nega_rec：DMT=1
//	若分母<=0，则指标=“分子” （分母置为1）；
	DEAL_TWO,
	
//	特殊处理3	del_nega_rec：=treat
//	若分子>0 且分母<=0，则指标=“分子”；
//	若分子>0 且分母>0 ，不做特殊处理；
//	若分子<=0 且分母<0，则指标=-1*公式；
//	若分子<=0且分母>=0，则指标=“分子” ；
	DEAL_THREE
	
}
