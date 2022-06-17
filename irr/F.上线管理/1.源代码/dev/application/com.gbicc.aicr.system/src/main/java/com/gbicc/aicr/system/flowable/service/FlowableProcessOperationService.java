package com.gbicc.aicr.system.flowable.service;

import java.util.Map;

import org.flowable.engine.runtime.ProcessInstance;

/**
 * 任务查询服务
 * 
 * @author lingzhigang
 *
 */
public interface FlowableProcessOperationService {

	public ProcessInstance startProcessByKey(String startUser, String processDefinitionKey, String bussinessKey,
			Map<String, Object> variables, Map<String, Object> transientVariables, boolean autoCompleteFirstTask)
			throws Exception;
}
