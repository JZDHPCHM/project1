package com.gbicc.aicr.system.service;

public interface CommToolService {
	/**
	 * 生成流水号service
	 * 生成流水号,格式如下：[A-Za-z]{0,7}2013031500001
	 * 
	 * @param prefix
	 * @param format 格式 日期:年月日=Ymd 全年=Y 小年=y 月=m 日=d
	 * @param seqDigit 序列位数
	 */
	public String  getFlowCode(String prefix,String format,String seqDigit);
}
