package com.gbicc.aicr.system.in2oa.service;

public interface TaskToOAService {

    /**
     * 发送OA任务提醒
     *
     * @param userAccount
     *            用户账号
     * @param issue
     *            期次
     * @throws Exception
     */
    public String sendOARemind(String userAccount, String issue, String startData, String endData) throws Exception;

    /**
     * 是否为流程采集角色
     *
     * @param loginName
     *            登录名
     * @return
     * @throws Exception
     */
    public Boolean isProcessCollUser(String loginName) throws Exception;
	
}
