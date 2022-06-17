package gbicc.irs.warning.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.jpa.model.menu.contribution.annotation.MenuContributionItem;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.controller.BootstrapRestfulCrudController;
import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.warning.jpa.entity.AftWarnInfoEntity;
import gbicc.irs.warning.jpa.repository.AftWarnInfoRepository;
import gbicc.irs.warning.jpa.vo.WarnResult;
import gbicc.irs.warning.service.AftWarnInfoService;

/**
 * 预警信息表
 * 
 * @author FC
 * @version v1.0 2019年9月17日
 */
@Controller
@RequestMapping("/warn")
public class AftWarnInfoController
        extends BootstrapRestfulCrudController<AftWarnInfoEntity, String, AftWarnInfoRepository, AftWarnInfoService> {

    private static final Logger LOG = LogManager.getLogger(AftWarnInfoController.class);
    
    @Autowired
    private SystemDictionaryService systemDictionaryService;
    
    /**
     * 查询预警分类
     *
     * @param parentCode
     *            预警父编码
     * @return
     */
    @ResponseBody
    @RequestMapping("/findWarnType.action")
    public Map<String, Object> findWarnCate(String parentCode) {
        try {
            return service.findWarnCate(parentCode);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e);
            return AppUtil.getMap(false);
        }
    }
    
    
    @RequestMapping("/WarningSignals")
    @MenuContributionItem("menuitem.irs.warning.warnList")
    public String WarningSignals() {
    	return "gbicc/irs/warning/view/listWarningSignals.html";
    }

    @ResponseBody
    @RequestMapping("/warningByCode")
    public boolean warningByCode(String ids) {
    	return service.warningByCode(ids);
    }
    
    @ResponseBody
    @RequestMapping("/warningList")
    public ResponseWrapper<WarnResult> warningList(String waterID,String custType,String custname,
    		String date1,String date2,String date3,String date4,String level,String status,String warnRule,
    		String warnSmalt,String warnType,String dispResult,String assoName,
    		Integer page,Integer rows, String highWarn,String medWarn) {
    	
    	return service.warningList(waterID,page,rows,custType,custname,date1,date2,date3,date4,
    			level,status,warnRule,warnSmalt,warnType,dispResult,assoName,highWarn,medWarn);
    }
    
    @ResponseBody
    @RequestMapping("/groupWarn")
    public List<Map<String, Object>> groupWarn(String waterID,Integer page, Integer rows, String custType, 
			String custname, String date1,
			String date2,String date3,String date4,String level, 
			String status, String warnRule, String warnSmalt, 
			String warnType,String dispResult,String assoName) {
    	return service.groupWarn(waterID,page,rows,custType,custname,date1,date2,date3,date4,level,status,warnRule,warnSmalt,warnType,dispResult,assoName);
    }
    
    
    @RequestMapping("/warningFocusInfo.html")
    @MenuContributionItem("menuitem.irs.warning.focusOn")
    public String warningFocusInfo() {
    	return "gbicc/irs/warning/view/warningFocusInfo.html";
    }
    @ResponseBody
    @RequestMapping("/queryAllLessee")
    public ResponseWrapper<Map<String,Object>> queryAllLessee(Integer page,Integer rows,String custName,String date1,String date2) throws Exception {
		return service.queryAllLessee(page,rows,custName,date1,date2);
	}
    
    
    @ResponseBody
    @RequestMapping("/findByAssoidY")
    public ResponseWrapper<Map<String, Object>> findByAssoidY(String lesseeId,Integer page,Integer rows) throws Exception {
		return service.findByAssoidY(lesseeId,page,rows);
	}
    
    @ResponseBody
    @RequestMapping("/findByAssoidN")
    public ResponseWrapper<Map<String, Object>> findByAssoidN(String lesseeId,Integer page,Integer rows) throws Exception {
		return service.findByAssoidN(lesseeId,page,rows);
	}
    @ResponseBody
    @RequestMapping("/changeFocusOn")
    public boolean changeFocusOn(String assoID) throws Exception {
    	return service.changeFocusOn("Y",assoID);
		
	}
    
    @ResponseBody
    @RequestMapping("/changeNoFocusOn")
    public boolean changeNoFocusOn(String assoID) throws Exception {
    	return service.changeFocusOn("N",assoID);
		
	}
    
    /**
     * 添加客户关注
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/putCustomerAtten.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public boolean putCustomerAtten(String companyId) {
        try {
            String url = getRequestUrl(companyId);
            return FbHttpUtil.requestPut(url);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 移除客户关注
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value="/deleteCustomerAtten.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public boolean deleteCustomerAtten(String companyId) {
        try {
            String url = getRequestUrl(companyId);
            return FbHttpUtil.requestDelete(url);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
    /**
     * 判断预警历史查询是否有数据
     *
     * @param custname
     * @param custType
     * @param level
     * @param warnRule
     * @param warnType
     * @param warnSmalt
     * @param employee
     * @param dispResult
     * @param assoName
     * @param date4
     * @param date3
     * @param date2
     * @param date1
     * @return
     */
    @RequestMapping(value="/queryHistoryCount.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> queryHistoryCount(String custname,String custType,String level,String warnRule,String warnType,
            String warnSmalt,String employee,String dispResult,String assoName,String date4,String date3,String date2,String date1
            , String highWarn,String medWarn){
        
        try {
            Map<String, Object> map = service.queryHistoryCount(custname,custType,level,warnRule,warnType,
            		warnSmalt,employee,dispResult,assoName,
            		date4,date3,date2,date1,highWarn,medWarn);
            return map;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return AppUtil.getMap(false);
        }
    }
    
    /**
     * 导出预警历史数据
     *
     * @param custname
     * @param custType
     * @param level
     * @param warnRule
     * @param warnType
     * @param warnSmalt
     * @param employee
     * @param dispResult
     * @param assoName
     * @param date4
     * @param date3
     * @param date2
     * @param date1
     * @param fileName
     * @return
     */
    @RequestMapping(value="/exportHistoryData.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> exportHistoryData(String custname,String custType,String level,String warnRule,String warnType,
            String warnSmalt,String employee,String dispResult,String assoName,String date4,String date3,String date2,String date1,
            String fileName , String highWarn,String medWarn){
        
        try {
            Map<String, Object> map = service.exportHistoryData(custname, custType, level, warnRule, warnType,
                    warnSmalt, employee, dispResult, assoName, date4, date3, date2, date1, fileName,highWarn,medWarn);
            return map;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return AppUtil.getMap(false);
        }
    }
    /**
     * 判断预警信号查询是否有数据
     *
     * @param waterId
     * @param custname
     * @param warnType
     * @param warnSmalt
     * @param level
     * @param pushStatus
     * @param custType
     * @param warnRule
     * @param date2
     * @param date1
     * @return
     */
    @RequestMapping(value="/queryCount.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> queryCount(String waterId,String custname,String warnType,String warnSmalt,String level,String pushStatus,
            String custType,String warnRule,String date2,String date1, String highWarn,String medWarn){
        
        try {
            Map<String, Object> map = service.queryCount(waterId,custname,warnType,warnSmalt,level,
            		pushStatus,custType,warnRule,date2,date1,highWarn,medWarn);
            return map;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return AppUtil.getMap(false);
        }
    }
    /**
     * 导出预警信号数据
     *
     * @param waterId
     * @param custname
     * @param warnType
     * @param warnSmalt
     * @param level
     * @param pushStatus
     * @param custType
     * @param warnRule
     * @param date2
     * @param date1
     * @param fileName
     * @return
     */
    @RequestMapping(value="/exportData.action",method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> exportData(HttpServletRequest request,String waterId,String custname,String warnType,String warnSmalt,String level,String pushStatus,
            String custType,String warnRule,String date2,String date1,String fileName, String highWarn,String medWarn){
        
        try {
            Map<String, Object> map = service.exportData(waterId,custname,warnType,warnSmalt,level,
            		pushStatus,custType,warnRule,date2,date1,fileName,highWarn,medWarn);
            return map;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            return AppUtil.getMap(false);
        }
    }

}
