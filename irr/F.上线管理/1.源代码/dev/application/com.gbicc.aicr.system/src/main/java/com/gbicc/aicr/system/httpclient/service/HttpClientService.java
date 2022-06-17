package com.gbicc.aicr.system.httpclient.service;

/**
 * httpclient 服务类
 * 
 * @author FC
 * @version v1.0 2019年12月4日
 */
public interface HttpClientService {

    /**
     * post请求字符串形式
     *
     * @param address
     *            地址
     * @param xml
     *            报文
     * @throws Exception
     */
    public String postMethodRequestByString(String address, String xml) throws Exception;

    /**
     * post请求输入流形式
     *
     * @param address
     *            地址
     * @param xml
     *            报文
     * @throws Exception
     */
    public String postMethodRequestByInputStream(String address, String xml) throws Exception;

    /**
     * urlconnection 请求webservice
     *
     * @param address
     *            地址
     * @param xml
     *            报文
     * @return
     * @throws Exception
     */
    public String urlConnectionRequest(String address, String xml) throws Exception;

}
