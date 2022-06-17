package gbicc.irs.fbinterface.jpa.support.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 请求接口公共方法
 * 
 * @author songxubei
 * @version v1.0 2019年9月17日
 */
public class FbHttpUtil {
    
    private static final Logger LOGGER = LogManager.getLogger(FbHttpUtil.class);
    
    /**
     * 拼接请求地址url
     * 其中事件检索获取原文地址参数后无后缀，所以urlSuffix可以为空
     * @param baseUrl
     * @param param
     * @param urlSuffix
     * @param apiKey
     * @return
     */
    public static Map<String, Object> getRequestUrl(String baseUrl,String param,String urlSuffix,String apiKey) {
        if(FbCommonUtil.stringIsNotValid(baseUrl)) {
            return AppUtil.getMap(false,"请求地址BASE_URL未设置");
        }
        if(FbCommonUtil.stringIsNotValid(param)) {
            return AppUtil.getMap(false,"请求参数不能为空");
        }
        if(FbCommonUtil.stringIsNotValid(apiKey)) {
            return AppUtil.getMap(false,"apiKey不能为空");
        }
        
        if(!baseUrl.endsWith(FbInterfaceEnums.SEPRATOR.getValue())) {
            baseUrl = baseUrl + FbInterfaceEnums.SEPRATOR.getValue();
        }
        if(FbCommonUtil.stringIsValid(urlSuffix)) {
            if(!urlSuffix.startsWith(FbInterfaceEnums.SEPRATOR.getValue())) {
                urlSuffix = FbInterfaceEnums.SEPRATOR.getValue() + urlSuffix;
            }
        }else {
            urlSuffix = "";
        }
        
        String url = baseUrl + param + urlSuffix + FbInterfaceEnums.APIKEY_SUFFIX.getValue() + apiKey
                + FbInterfaceEnums.FETCH_MODE.getValue();
        return AppUtil.getMap(true,url);
    }
    /**
     * 拼接请求地址url
     *
     * @param url
     * @param pageId
     * @return
     */
    public static Map<String, Object> getRequestUrl(String url, String pageId) {
        if(FbCommonUtil.stringIsNotValid(url)) {
            return AppUtil.getMap(false,"请求地址无效");
        }
        
        if(FbCommonUtil.stringIsNotValid(pageId)) {
            return AppUtil.getMap(true, url);
        }else {
            return AppUtil.getMap(true, url + FbInterfaceEnums.CURSOR_PAGE.getValue()+pageId);
        }
    }
    
    /**
     * 获取请求地址结果
     *
     * @param companyId
     * @param dictionaryMap
     * @param value
     * @return
     */
    public static Map<String, Object> getRequestUrlMap(String companyId, Map<String, String> dictionaryMap, String interfaceKey) {
      //获取封装url
      return getRequestUrl(
                dictionaryMap.get(FbInterfaceEnums.BASE_URL.getValue()), companyId,
                dictionaryMap.get(interfaceKey),
                dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue()));
    }
    
    /**
     * 获取请求结果
     *
     * @param companyId
     * @param dictionaryMap
     * @param value
     * @return
     */
    public static Map<String, Object> getRequestMap(String companyId, Map<String, String> dictionaryMap, String interfaceKey) {
      //获取封装url
        Map<String, Object> requestUrlMap = getRequestUrlMap(companyId, dictionaryMap, interfaceKey);
        
        if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
            return requestUrlMap;
        }
        //获取请求结果
        Map<String, Object> requestMap = requestGet(requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
        
        return requestMap;
    }
    /**
     * 获取递归请求url
     *
     * @param dictionaryMap
     * @param companyId
     * @param url
     * @param pageId
     * @param type
     * @return
     */
    public static Map<String, Object> getRecrusionRequestUrlMap(Map<String, String> dictionaryMap, String companyId,
            String url, String pageId, String type) {
        if(FbCommonUtil.stringIsNotValid(url)) {
            //获取封装后的url
            Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId,dictionaryMap,type);
            if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
                return requestUrlMap;
            }
            url = requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        }
        //封装下页请求url
        Map<String, Object> requestUrlMap = getRequestUrl(url,pageId);
        return requestUrlMap;
    }
    /**
     * 获取递归结果
     *
     * @param dictionaryMap
     * @param companyId
     * @param url
     * @param pageId
     * @return
     */
    public static Map<String, Object> getRecrusionRequestMap(Map<String, String> dictionaryMap,String companyId, String url, String pageId, String type){
        Map<String, Object> requestUrlMap = getRecrusionRequestUrlMap(dictionaryMap, companyId, url, pageId, type);
        //获取请求结果
        return requestGet(requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
    }
    
    /**
     * 请求接口地址，返回结果
     *
     * @param url
     * @return
     */
    public static Map<String, Object> requestGet(String url){
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            System.out.println(JSONObject.fromObject(response).toString());
            //请求返回结果判断
            Map<String, Object> requestResultMap = resultLimit(response);
            
            if(!FbCommonUtil.getApputilMapFlag(requestResultMap)) {
                return requestResultMap;
            }
            
            // 从响应模型中获取响应实体
            
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                String jsonString = EntityUtils.toString(responseEntity);
                System.out.println(jsonString);
                return AppUtil.getMap(true, jsonString);
            }
            return AppUtil.getMap(false,"无法获取返回内容，请求返回状态码："+response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            }
        }
    }
    
    /**
     * 请求接口地址，返回结果
     *
     * @param url
     * @param params jsonObjectString
     * @return
     */
    public static Map<String, Object> requestPost(String url, String params){
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建post请求
        HttpPost httpPost = new HttpPost(url);
        //设置请求头 发送的是json数据格式
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");  //设置编码格式
        // 发送Json格式的数据请求
        entity.setContentType("application/json");
        //把请求消息实体塞进去
        httpPost.setEntity(entity);
        
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)post请求
            response = httpClient.execute(httpPost);
            //请求返回结果判断
            Map<String, Object> requestResultMap = resultLimit(response);
            
            if(!FbCommonUtil.getApputilMapFlag(requestResultMap)) {
                return requestResultMap;
            }
            
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            
            if (responseEntity != null) {
                String jsonString = EntityUtils.toString(responseEntity);
                System.out.println(jsonString);
                return AppUtil.getMap(true, jsonString);
            }else {
                return AppUtil.getMap(false,"返回内容responseEntity为空");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            }
        }
    }
    
    /**
     * 请求接口地址，返回结果
     *
     * @param url
     * @param params jsonObjectString
     * @return
     */
    public static boolean requestPut(String url){
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建post请求
        HttpPut httpPut = new HttpPut(url);
        
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)post请求
            response = httpClient.execute(httpPut);
            System.out.println(JSONObject.fromObject(response).toString());
            //请求返回结果判断
            Map<String, Object> requestResultMap = resultLimit(response);
            
            String statusCode = String.valueOf(requestResultMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()));
            
            if(FbInterfaceEnums.STATUS_CODE_201.getValue().equals(statusCode)) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return false;
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            }
        }
    }
    public static void main(String[] args) {
        String url = "https://data.riskstorm.com/v1/following/91110112666906802U?apikey=4N2anQp7tFzw6mFo7FjDMA";
//        System.out.println(requestPut(url));
        System.out.println(requestDelete(url));
    }
    /**
     * 请求接口地址，返回结果
     *
     * @param url
     * @param params jsonObjectString
     * @return
     */
    public static boolean requestDelete(String url){
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建post请求
        HttpDelete httpDelete = new HttpDelete(url);
        
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)post请求
            response = httpClient.execute(httpDelete);
            System.out.println(JSONObject.fromObject(response).toString());
            //请求返回结果判断
            Map<String, Object> requestResultMap = resultLimit(response);
            
            String statusCode = String.valueOf(requestResultMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()));
            
            if(FbInterfaceEnums.STATUS_CODE_204.getValue().equals(statusCode)) {
                return true;
            }else {
                return false;
            }
            
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return false;
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            }
        }
    }
    
    /**
     * 请求结果判断及限制次数进行判断
     *
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> resultLimit(CloseableHttpResponse response){
        if(response==null) {
            return AppUtil.getMap(false,FbCommonEnums.RESULT_NULL.getValue());
        }
        
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode!=200) {
            return AppUtil.getMap(false,statusCode);
        }
        
        JSONObject jsonObject = JSONObject.fromObject(response);
        
        JSONArray jsonArray = jsonObject.getJSONArray("allHeaders");
        //每秒限制次数
        //int limitSecond = Integer.MAX_VALUE;
        //每秒剩余次数
        int remainingSecond = Integer.MAX_VALUE;
        //每分钟限制次数
        //int limitMinute = Integer.MAX_VALUE;
        //每分钟剩余次数
        int remainingMinute = Integer.MAX_VALUE;
        
        if(jsonArray!=null && jsonArray.size()>0) {
            Iterator<JSONObject> iterator = jsonArray.iterator();
            while(iterator.hasNext()) {
                JSONObject object = iterator.next();
                String name = object.getString("name");
                String value = object.getString("value");
//                if("X-RateLimit-Limit-second".equals(name)) {
//                    limitSecond = value==null?0:Integer.parseInt(value);
//                }
                if("X-RateLimit-Remaining-second".equals(name)) {
                    remainingSecond = value==null?0:Integer.parseInt(value);
                }
//                if("X-RateLimit-Limit-minute".equals(name)) {
//                    limitMinute = value==null?0:Integer.parseInt(value);
//                }
                if("X-RateLimit-Remaining-minute".equals(name)) {
                    remainingMinute = value==null?0:Integer.parseInt(value);
                }
            }
            
            if(remainingSecond<=0 || remainingMinute<=0) {
                return AppUtil.getMap(false,FbInterfaceEnums.STATUS_CODE_429.getValue());
            }
        }
        return AppUtil.getMap(true,statusCode);
    }
}
