package gbicc.irs.fbinterface.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.service.AftWarnRuleService;
import gbicc.irs.fbinterface.service.FbAftAttenCustomerService;
import gbicc.irs.fbinterface.service.FbInterfaceService;

@Controller
@RequestMapping("/fbScheduled")
public class FbScheduledController {
    
    private static final Logger LOGGER = LogManager.getLogger(FbScheduledController.class);
    @Autowired
    private FbAftAttenCustomerService aftAttenCustomerService;
    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private FbInterfaceService fbInterfaceService;
    @Autowired
    private AftWarnRuleService aftWarnRuleService;

    @RequestMapping(value="/test.action")
    @ResponseBody
    public Map<String, Object> test() {
        try {
            System.out.println("调度开始");
            Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
            
            String runnerStatus = dictionaryMap.get(FbInterfaceEnums.RUNNER_STATUS.getValue());
            
            if(FbCommonUtil.stringIsNotValid(runnerStatus)||runnerStatus.equalsIgnoreCase(FbInterfaceEnums.RUNNER_STATUS_N.getValue())) {
                return AppUtil.getMap(false,"任务调度不允许");
            }
            
            System.out.println("---------------------------------");
            return AppUtil.getMap(true,"跑批成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AppUtil.getMap(false,e.getMessage());
        }
    }
    
    /**
     * 全量接口调度
     *
     * @return
     */
    @RequestMapping(value="/interfaceRunner.action")
    @ResponseBody
    @Scheduled(cron="0 10 0 * * *")
    public Map<String, Object> fbInterfaceRunner(){
        try {
            Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
            
            String runnerStatus = dictionaryMap.get(FbInterfaceEnums.RUNNER_STATUS.getValue());
            
            if(FbCommonUtil.stringIsNotValid(runnerStatus)||runnerStatus.equalsIgnoreCase(FbInterfaceEnums.RUNNER_STATUS_N.getValue())) {
                return AppUtil.getMap(false,"任务调度不允许");
            }
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
        
        return AppUtil.getMap(true,"全量接口调度成功");
    }
    
    /**
     * 增量接口调度
     *
     * @return
     */
    @RequestMapping(value="/incrementRunner.action")
    @ResponseBody
    @Scheduled(cron="0 10 0 * * *")
    public Map<String, Object> fbInterfaceIncrementRunner(){
        try {
            Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
            
            String runnerStatus = dictionaryMap.get(FbInterfaceEnums.RUNNER_STATUS_INCREMENT.getValue());
            
            if(FbCommonUtil.stringIsNotValid(runnerStatus)||runnerStatus.equalsIgnoreCase(FbInterfaceEnums.RUNNER_STATUS_N.getValue())) {
                return AppUtil.getMap(false,"任务调度不允许");
            }
            //获取所有关注客户信息
            List<String> aftWarnAttenCustomerList = aftAttenCustomerService.queryAttenCutomerByIsAtten(FbCommonEnums.IS_Y.getValue());
            if(CollectionUtils.isEmpty(aftWarnAttenCustomerList)) {
                return AppUtil.getMap(true,"无关注客户");
            }
            
            for(String companyId:aftWarnAttenCustomerList) {
                companyId = companyId.trim();
                fbInterfaceService.getIncrementalData(companyId);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        }
        
        return AppUtil.getMap(true,"增量接口调度");
    }
    
    /**
     * 预警规则调度
     *
     * @return
     */
    @RequestMapping(value="/warnRuleRunner.action")
    @ResponseBody
    @Scheduled(cron="0 0 2 * * *")
    public Map<String, Object> warnRuleRunner(){
        try {
            Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
            
            String runnerStatus = dictionaryMap.get(FbInterfaceEnums.RUNNER_STATUS_RULE.getValue());
            
            if(FbCommonUtil.stringIsNotValid(runnerStatus)||runnerStatus.equalsIgnoreCase(FbInterfaceEnums.RUNNER_STATUS_N.getValue())) {
                return AppUtil.getMap(false,"任务调度不允许");
            }
            
            List<String> aftWarnRuleList = aftWarnRuleService.getRuleCode();
                    
            if(CollectionUtils.isEmpty(aftWarnRuleList)) {
                return AppUtil.getMap(false,"无可用预警规则");
            }
            
            String taskseqno = FbCommonUtil.getTaskseqno();
            List<Future<Map<String,Object>>> futures = new ArrayList<>();
            for(String ruleCode:aftWarnRuleList) {
                //http://localhost:8080/rest/v1/warn/after/rule/{rule}/taskseqno/{taskseqno}
                String url = dictionaryMap.get(FbCommonEnums.RULE_URL.getValue()) + FbInterfaceEnums.SEPRATOR.getValue()
                        + ruleCode + FbInterfaceEnums.SEPRATOR.getValue()
                        + dictionaryMap.get(FbCommonEnums.RULE_TASKSEQNO.getValue())
                        + FbInterfaceEnums.SEPRATOR.getValue() + taskseqno;
                Future<Map<String,Object>> result = fbInterfaceService.warnRuleRunner(url);
                futures.add(result);
            }
            
            while(true) {
                if(CollectionUtils.isNotEmpty(futures)&&aftWarnRuleList.size()==futures.size()) {
                    //t_aft_warn_info去重入t_aft_warn_info_distinct
                    aftWarnRuleService.distinctInDistinct(taskseqno);
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return AppUtil.getMap(false,e.getMessage());
        }
        
        return AppUtil.getMap(true,"预警规则调度");
    }
}
