package com.gbicc.aicr.system.httpclient.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gbicc.aicr.system.httpclient.service.HttpClientService;

/**
 * httpClient业务类
 * 
 * @author FC
 * @version v1.0 2019年12月4日
 */
@Service
public class HttpClientServiceImpl implements HttpClientService {

    private static final Logger LOG = LogManager.getLogger(HttpClientServiceImpl.class);

    /**
     * 设置超时时间
     */
    private int time = 30000;
    /**
     * 成功状态
     */
    private int SUCCESS_CODE = 200;

    @Override
    public String postMethodRequestByString(String address, String xml) throws Exception {
        if (StringUtils.isBlank(address) || StringUtils.isBlank(xml)) {
            throw new RuntimeException("对接失败：地址或内容为空！");
        }
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建post请求
        HttpPost post = new HttpPost(address);
        CloseableHttpResponse httpResponse = null;
        String result = null;
        try {
            //配置参数
            RequestConfig config = RequestConfig.custom().setConnectTimeout(time).setConnectionRequestTimeout(time)
                    .setSocketTimeout(time).build();
            post.setConfig(config);
            post.setHeader("Content-Type", "text/xml;charset=UTF-8");
            //设置报文
            StringEntity entity = new StringEntity(xml, "UTF-8");
            post.setEntity(entity);
            //执行请求
            httpResponse = httpClient.execute(post);
            //取结果
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOG.info(result);
            if (SUCCESS_CODE != statusCode) {
                throw new RuntimeException("对接失败：" + result);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(httpClient != null) {
                httpClient.close();
            }
            if(httpResponse != null) {
                httpResponse.close();
            }
        }
        return result;
    }

    @Override
    public String urlConnectionRequest(String address, String xml) throws Exception {
        StringBuffer sb = new StringBuffer();
        OutputStream os = null;
        InputStream is = null;
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            byte[] b = xml.getBytes("UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(b.length));
            os = conn.getOutputStream();
            os.write(b, 0, b.length);
            is = conn.getInputStream();
            byte[] cache = new byte[1024];
            int len = 0;
            while ((len = is.read(cache)) != -1) {
                sb.append(new String(cache, 0, len));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return sb.toString();
    }

    @Override
    public String postMethodRequestByInputStream(String address, String xml) throws Exception {
        if (StringUtils.isBlank(address) || StringUtils.isBlank(xml)) {
            throw new RuntimeException("对接失败：地址或内容为空！");
        }
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建post请求
        HttpPost post = new HttpPost(address);
        CloseableHttpResponse httpResponse = null;
        InputStream is = null;
        String result = null;
        try {
            //设置参数
            RequestConfig config = RequestConfig.custom().setConnectTimeout(time).setConnectionRequestTimeout(time)
                    .setSocketTimeout(time).build();
            post.setConfig(config);
            //整理输入流
            byte[] b = xml.getBytes("UTF-8");
            is = new ByteArrayInputStream(b, 0, b.length);
            ContentType contentType = ContentType.create("text/xml", Consts.UTF_8);
            InputStreamEntity isEntity = new InputStreamEntity(is, b.length, contentType);
            post.setEntity(isEntity);
            //执行请求
            httpResponse = httpClient.execute(post);
            //获取返回值
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOG.error(result);
            if (SUCCESS_CODE != statusCode) {
                throw new RuntimeException("对接失败：" + result);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
        return result;
    }

}
