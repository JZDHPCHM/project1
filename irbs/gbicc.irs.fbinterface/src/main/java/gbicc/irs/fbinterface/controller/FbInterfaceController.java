package gbicc.irs.fbinterface.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.jpa.vo.CompanyInfo;
import gbicc.irs.fbinterface.service.FbAftAttenCustomerService;
import gbicc.irs.fbinterface.service.FbInterfaceService;
import gbicc.irs.fbinterface.service.registrationInformation;

/**
 * 风报接口controller
 * 
 * @author songxubei
 * @version v1.0 2019年10月9日
 */
@Controller
@RequestMapping("/fbController")
public class FbInterfaceController {

    private static final Logger LOGGER = LogManager.getLogger(FbInterfaceController.class);

    @Autowired
    private FbInterfaceService fbInterfaceService;
    @Autowired
    private FbAftAttenCustomerService aftAttenCustomerService;
    @Autowired
    private SystemDictionaryService systemDictionaryService;

    @Autowired
    private registrationInformation registrationInfo;
    
    /**
     * 获取接口增量数据（除去年报、股东信息、评级记录）
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/incrementalInterfaceData.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getIncrementalInterfaceData(String companyId, String beginTime, String endTime){
        
        try {
            long startTime = System.currentTimeMillis();
            Map<String, Object> resultMap = fbInterfaceService.getIncrementalInterfaceData(companyId, beginTime, endTime, null, false);
            long endTime2 = System.currentTimeMillis();
            System.out.println(endTime2-startTime);
            return resultMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        }
        
    }
    /**
     * 增量获取数据接口数据
     *
     * @return
     */
    
    @RequestMapping(value="/getIncrementalData.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getIncrementalData(){
        System.out.println(Thread.currentThread().getName());
        
        //获取所有关注客户信息
        List<String> aftWarnAttenCustomerList = aftAttenCustomerService.queryAttenCutomerByIsAtten(FbCommonEnums.IS_Y.getValue());
        
        if(CollectionUtils.isEmpty(aftWarnAttenCustomerList)) {
            return AppUtil.getMap(true,"无关注客户");
        }
        
        for(String companyId:aftWarnAttenCustomerList) {
            try {
                companyId = companyId.trim();
                fbInterfaceService.getIncrementalData(companyId);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
                return AppUtil.getMap(false,e.getMessage());
            }
        }
        
        System.out.println("---------------------");
        return AppUtil.getMap(true,"批量跑数据");
    }
    
    /**
     * 批量获取数据接口数据测试
     *
     * @return
     */
    
    @RequestMapping(value="/getAllDateFirst.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAllDateFirst(){
        try {

            //获取所有关注客户信息
            List<String> aftWarnAttenCustomerList = aftAttenCustomerService.queryAttenCutomerByIsAtten(FbCommonEnums.IS_Y.getValue());
            if(CollectionUtils.isEmpty(aftWarnAttenCustomerList)) {
                return AppUtil.getMap(true,"无关注客户");
            }
            
            for(String companyId:aftWarnAttenCustomerList) {
                companyId = companyId.trim();
                fbInterfaceService.getAllDateFirst(companyId);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        }
        
        System.out.println("---------------------");
        
        return AppUtil.getMap(true,"批量跑数据");
    }
    
    @RequestMapping(value="/putAtten.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> putCompanyIdToAtten(){
        try {
            List<String> aftWarnAttenCustomerList = aftAttenCustomerService.queryAssoCompanyId();
            if(CollectionUtils.isEmpty(aftWarnAttenCustomerList)) {
                return AppUtil.getMap(true,"无客户信息");
            }
            for(String companyId:aftWarnAttenCustomerList) {
                String url = getRequestUrl(companyId.trim());
                boolean flag = FbHttpUtil.requestPut(url);
                if(flag) {
                    aftAttenCustomerService.updateIsAttenByCompanyId(companyId,FbCommonEnums.IS_Y.getValue());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        }
        
        return AppUtil.getMap(true,"关注成功");
    }
    
    @RequestMapping(value="/deleteAtten.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteCompanyIdFromAtten(){
        
        try {
            List<String> aftWarnAttenCustomerList = aftAttenCustomerService.queryAttenCutomerByIsAtten(FbCommonEnums.IS_Y.getValue());
            if(CollectionUtils.isEmpty(aftWarnAttenCustomerList)) {
                aftWarnAttenCustomerList = fbInterfaceService.getAllAttenFromFb();
                if(CollectionUtils.isEmpty(aftWarnAttenCustomerList)) {
                    return AppUtil.getMap(true,"无关注客户");
                }
            }
            
            for(String companyId:aftWarnAttenCustomerList) {
                String url = getRequestUrl(companyId.trim());
                boolean flag = FbHttpUtil.requestDelete(url);
                if(flag) {
                    aftAttenCustomerService.updateIsAttenByCompanyId(companyId,FbCommonEnums.IS_N.getValue());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        }
        
        return AppUtil.getMap(true,"取消关注成功");
    }
    
    /**
     * 获取客户关注/取消请求地址
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    private String getRequestUrl(String companyId) throws Exception{
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        String baseUrl = dictionaryMap.get(FbInterfaceEnums.FOLLOWING_FEED.getValue());
        baseUrl = baseUrl.substring(0,baseUrl.lastIndexOf(FbInterfaceEnums.SEPRATOR.getValue())+1);
        String url = baseUrl+companyId.trim()+FbInterfaceEnums.APIKEY_SUFFIX.getValue()+dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue());
        return url;
    }
    
    
    
    @ResponseBody
    @RequestMapping("/test.action")
    public String testInterface() {
    	String info = "";
        try {
        	info=registrationInfo.companySearch("深圳华大临床检验中心");
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e);
            return info;
        }
    }   
    
    
    
    
    
}
