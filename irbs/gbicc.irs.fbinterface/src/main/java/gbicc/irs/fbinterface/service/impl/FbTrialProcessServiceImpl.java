package gbicc.irs.fbinterface.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessEntity;
import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessOtherEntity;
import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessPartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTrialProcessRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbTrialProcessService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 审判流程相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbTrialProcessServiceImpl extends DaoServiceImpl<FbTrialProcessEntity, String, FbTrialProcessRepository> implements FbTrialProcessService{
    
    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public static final List<Map<String,Object>> useMapList = new ArrayList<>();
    
    @PostConstruct
    public void initChild() {
        if(CollectionUtils.isNotEmpty(useMapList)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "主表");
        map.put("entity", FbTrialProcessEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "主表2");
        map.put("entity", FbTrialProcessPartyEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "name");
        map.put("type", "其他角色");
        map.put("entity", FbTrialProcessOtherEntity.class);
        useMapList.add(map);
    }
    
    @Override
    public Map<String, Object> getTrialProcess(String companyId) throws Exception {
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        Map<String, Object> requestMap = FbHttpUtil.getRequestMap(companyId,dictionaryMap,FbInterfaceEnums.SPLC.getValue());
        
        parseJsonToEntity(requestMap, companyId);
        
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    public Map<String, Object> getRecrusionTrialProcess(String companyId, String url, String pageId) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        Map<String, Object> requestMap = FbHttpUtil.getRecrusionRequestMap(dictionaryMap, companyId, url, pageId, FbInterfaceEnums.SPLC.getValue());
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionTrialProcess(companyId,url,pageId);
                
            }
            accessLogService.failed(FbTrialProcessEntity.class.getName(), AccessType.ADD, null, requestMap, null);
            return requestMap;
        }
        //判断是否有下一页
        Map<String, Object> pageMap = FbBeanSetValueUtil.getRequestMapContinue(requestMap);
        //封装保存
        parseJsonToEntity(requestMap, companyId);
        //记录日志
        accessLogService.success(url+";"+pageId, AccessType.ADD, null, requestMap);
        if(!FbCommonUtil.getApputilMapFlag(pageMap)) {
            return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
        }else {
            return getRecrusionTrialProcess(companyId,url,pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
        }
    }

    private Map<String, Object> parseJsonToEntity(Map<String, Object> requestMap, String companyId) throws Exception{
        
        Map<String, Object> entityResultMap = new HashMap<>();
        
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        JSONObject object = JSONObject.fromObject(jsonString);
        JSONArray jsonArray = object.getJSONArray(FbInterfaceEnums.HITS.getValue());
        if(jsonArray.size()<=0) {
            return null;
        }
        int size = jsonArray.size();
        
        for(int i=0;i<size;i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String, Object> tempEntityMap = parseJsonObjectToEntity(jsonObject, companyId);
            entityResultMap = FbBeanSetValueUtil.mergeMap(entityResultMap,tempEntityMap,useMapList);
        }
        
        FbBeanSetValueUtil.saveDataToDB(entityResultMap, useMapList, jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.SAVE_SUCCESS.getValue());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map<String, Object> parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception {
        List<FbTrialProcessEntity> trialProcessList = new ArrayList<>();
        List<FbTrialProcessPartyEntity> trialProcessPartyList = new ArrayList<>();
        Map<String, Object> newEntityResultMap = new HashMap<>();
        
        
        //获取审判流程结果
        FbTrialProcessEntity entity = FbBeanSetValueUtil.setValue(FbTrialProcessEntity.class, detailObject, EntityMap.trialProcessEntityMap);
        entity.setCompanyId(companyId);
        entity.setId(UUID.randomUUID().toString());
        trialProcessList.add(entity);
        //获取当事人结果记录
        JSONArray partyArray = detailObject.getJSONArray(FbInterfaceEnums.PARTY.getValue());
        
        if(partyArray!=null && partyArray.size()>0) {
            for(int j=0;j<partyArray.size();j++) {
                JSONObject partyObject = partyArray.getJSONObject(j);
                FbTrialProcessPartyEntity partyEntity = FbBeanSetValueUtil.setValue(FbTrialProcessPartyEntity.class, partyObject, EntityMap.trialProcessPartyEntityMap);
                partyEntity.setCompanyId(companyId);
                partyEntity.setTrialProcessId(entity.getId());
                partyEntity.setId(UUID.randomUUID().toString());
                trialProcessPartyList.add(partyEntity);
                
                Map<String, String> columnValueMap = new HashMap<>();
                columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
                columnValueMap.put("trialProcessId", entity.getId());
                columnValueMap.put("trialProcessPartyId", partyEntity.getId());
                //其他角色
                newEntityResultMap = FbBeanSetValueUtil.parseToMap(partyObject, useMapList.get(2).get("method").toString(), columnValueMap, useMapList.get(2).get("entityMap").toString(), useMapList.get(2).get("type").toString(), (Class)useMapList.get(2).get("entity"), newEntityResultMap);
            }
        }
        if(CollectionUtils.isNotEmpty(trialProcessList)) {
            newEntityResultMap.put(useMapList.get(0).get("type").toString(), trialProcessList);
        }
        if(CollectionUtils.isNotEmpty(trialProcessPartyList)) {
            newEntityResultMap.put(useMapList.get(1).get("type").toString(), trialProcessPartyList);
        }
        
        return newEntityResultMap;
    }

}
