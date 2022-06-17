package gbicc.irs.fbinterface.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;

import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.vo.AddressInfo;
import gbicc.irs.fbinterface.jpa.vo.CompanyInfo;
import gbicc.irs.fbinterface.jpa.vo.ManagementInfor;
import gbicc.irs.fbinterface.jpa.vo.ShareholdersInformation;
import gbicc.irs.fbinterface.service.FbInterfaceService;
import gbicc.irs.fbinterface.service.registrationInformation;

/**
 * 风报接口service实现类
 * 
 * @author FC
 * @version v1.0 2019年9月11日
 */
@Service
public class registrationInformationImpl implements registrationInformation {

	

    @Autowired
    private SystemDictionaryService systemDictionaryService;
	
	@Autowired
	private ObjectMapper objectMapper;
	//数据获取
    @Override
    public String fbInterface(String url) throws Exception {
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求、
        url ="https://data.riskstorm.com"+url;
        HttpGet httpGet = new HttpGet(
        		//GET 
        		url);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength()+"url:"+url);
                String data =  EntityUtils.toString(responseEntity);
                return data;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * @根据公司全名称搜索
     */
    @Override
    public String companySearch(String companyID) throws Exception {
    	/*String message = java.net.URLEncoder.encode(keyWord, "utf-8");
    	String companyKeyWord="/v1/company/search?apikey="+getRequestUrl()+"&keyword="+message;
    	String data=fbInterface(companyKeyWord);
    	Map dataMap = objectMapper.readValue(data, Map.class);
        List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
        Map<String,Object> companyInfo = new HashMap<String,Object>();
        listData = (List<Map<String, Object>>) dataMap.get("hits");
        CompanyInfo info = new CompanyInfo();
        if(listData!=null) {
        	 String companyID = listData.get(0).get("统一社会信用代码")==null?"":listData.get(0).get("统一社会信用代码").toString();
        	// findCompany(companyID);
        	// System.out.println("工商客户信息"+ findCompany(companyID));
        	
        }*/
    	 CompanyInfo info = new CompanyInfo();
         info = findCompany(companyID);
      String  result =objectMapper.writeValueAsString(info);
        
		return result;
    }
    
    @Override
    public CompanyInfo findCompany(String companyId) throws Exception {
    	String companyKeyWord="/v1/company/"+companyId+"?apikey="+ getRequestUrl();
    	String data = fbInterface(companyKeyWord);
    	Map dataMap = objectMapper.readValue(data, Map.class);
        Map<String,Object> companyInfo = new HashMap<String,Object>();
        CompanyInfo com =new CompanyInfo();
        com.setRegistereCapital(dataMap.get("注册资本（万元）")==null?"":dataMap.get("注册资本（万元）").toString());
        com.setMainProducts(dataMap.get("经营范围")==null?"":dataMap.get("经营范围").toString());
        com.setRegistrationDate(dataMap.get("经营期限自")==null?"":dataMap.get("经营期限自").toString());
        com.setCertificateExpiryDate(dataMap.get("经营期限至")==null?"":dataMap.get("经营期限至").toString());
        com.setEconomicType(dataMap.get("类型")==null?"":dataMap.get("类型").toString());
        com.setLegalRepresentative(dataMap.get("法定代表人")==null?"":dataMap.get("法定代表人").toString());
        List<ShareholdersInformation> infoShare = findShareholders(companyId);
        com.setShareholders(infoShare);
        List<AddressInfo> address = findAddress(companyId);
        for (AddressInfo addressInfo2 : address) {
        	addressInfo2.setAddress(dataMap.get("地址")==null?"":dataMap.get("地址").toString());
		}
        com.setAddressInfo(address);
        com.setManagementInfor(findManagementInfor(companyId));
       
        
		return com;
    }
    
    
    
    @Override
    public List<ShareholdersInformation> findShareholders(String companyId) throws Exception {
    	String companyKeyWord="/v1/company/"+companyId+"/investor?apikey="+ getRequestUrl();
    	String data = fbInterface(companyKeyWord);
    	Map dataMap = objectMapper.readValue(data, Map.class);
    	 List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
         listData = (List<Map<String, Object>>) dataMap.get("hits");
         List<ShareholdersInformation> infoShare= new ArrayList<ShareholdersInformation>();
         for (Map<String, Object> map : listData) {
        	 ShareholdersInformation info = new ShareholdersInformation();
        	 
        	 String message = java.net.URLEncoder.encode(map.get("股东")==null?"":map.get("股东").toString(), "utf-8");
         	String companyKeyWord1="/v1/company/search?apikey="+getRequestUrl()+"&keyword="+message;
        	 //
        	 String data1 = fbInterface(companyKeyWord1);
        	 Map dataMap1 = objectMapper.readValue(data1, Map.class);
        	 List<Map<String,Object>> listData1 = new ArrayList<Map<String,Object>>();
        	 listData1 = (List<Map<String, Object>>) dataMap1.get("hits");
        	 if(listData1!=null&&listData1.size()>0) {
        		String code = listData1.get(0).get("统一社会信用代码")==null?"": listData1.get(0).get("统一社会信用代码").toString();
        		 info.setShareholderCertificateNumber(code);
        	 }
        	 info.setShareholderName(map.get("股东")==null?"":map.get("股东").toString());
        	 info.setTypeShareholders(map.get("股东类型")==null?"":map.get("股东类型").toString());
        	 
        	 if(map.get("认缴额")!=null) {
        		 info.setCapitalContribution(map.get("认缴额").toString());
        	 }
        	 if(map.get("实缴额")!=null) {
        		 info.setActualAmount(map.get("实缴额").toString());
        	 }
        	
        	 info.setStake(map.get("持股比")==null?"":map.get("持股比").toString());
        	 infoShare.add(info);
		}
         
         
		return infoShare;
    }
    
    
    
    
    @Override
    public List<ManagementInfor> findManagementInfor(String companyId) throws Exception {
    	String companyKeyWord="/v1/company/"+companyId+"/member?apikey="+ getRequestUrl();
    	String data = fbInterface(companyKeyWord);
    	Map dataMap = objectMapper.readValue(data, Map.class);
    	 List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
         listData = (List<Map<String, Object>>) dataMap.get("hits");
         List<ManagementInfor> manageInfo= new ArrayList<ManagementInfor>();
         for (Map<String, Object> map : listData) {
        	 ManagementInfor info = new ManagementInfor();
        	 info.setName(map.get("成员")==null?"":map.get("成员").toString());
        	 info.setRelationshipType(map.get("职务")==null?"":map.get("职务").toString());
        	 manageInfo.add(info);
		}
		return manageInfo;
    }
    
    
    @Override
    public List<AddressInfo> findAddress(String companyId) throws Exception {
     
    	String companyKeyWord="/v1/company/"+companyId+"/nianbao?apikey="+ getRequestUrl();
    	String data = fbInterface(companyKeyWord);
    	Map dataMap = objectMapper.readValue(data, Map.class);
    	 List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
         listData = (List<Map<String, Object>>) dataMap.get("hits");
         List<AddressInfo> addressInfo= new ArrayList<AddressInfo>();
         for (Map<String, Object> map : listData) {
        	 Map<String,Object> mapArr =(Map<String, Object>) map.get("基本信息");
        	 AddressInfo info = new AddressInfo();
        	 info.setFbDate(map.get("发布日期").toString());
        	 info.setCommunicationAddress(mapArr.get("企业通信地址")==null?"":mapArr.get("企业通信地址").toString());
        	 info.setContactPhoNumber(mapArr.get("企业联系电话")==null?"":mapArr.get("企业联系电话").toString());
        	 addressInfo.add(info);
		}
		return addressInfo;
    }
    
    private String getRequestUrl() throws Exception{
      	 Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
      	 return dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue());
    }
    
    public static void main(String[] args) throws Exception {
    	registrationInformationImpl imp =new registrationInformationImpl();
    	imp.companySearch("91231282MA1BYH8609");
	}

    
}
