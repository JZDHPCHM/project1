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
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyDetailEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyKeywordEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEventOrganizationDetailEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEventPersonDetailEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEventPunishDetailEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEventPunishSourceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyDetailRepository;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyKeywordRepository;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyRepository;
import gbicc.irs.fbinterface.jpa.repository.FbEventOrganizationDetailRepository;
import gbicc.irs.fbinterface.jpa.repository.FbEventPersonDetailRepository;
import gbicc.irs.fbinterface.jpa.repository.FbEventPunishDetailRepository;
import gbicc.irs.fbinterface.jpa.repository.FbEventPunishSourceRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbEventCompanyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 事件检索公司信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbEventCompanyServiceImpl extends DaoServiceImpl<FbEventCompanyEntity, String, FbEventCompanyRepository> implements FbEventCompanyService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private FbEventCompanyDetailRepository companyDetailRepository;
    @Autowired
    private FbEventCompanyKeywordRepository companyKeywordRepository;
    @Autowired
    private FbEventOrganizationDetailRepository organizationDetailRepository;
    @Autowired
    private FbEventPersonDetailRepository personDetailRepository;
    @Autowired
    private FbEventPunishDetailRepository punishDetailRepository;
    @Autowired
    private FbEventPunishSourceRepository punishSourceRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public static final List<Map<String, Object>> useMapList = new ArrayList<>();
    
    @PostConstruct
    public void initChild() {
        if(CollectionUtils.isNotEmpty(useMapList)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "主表");
        map.put("entity", FbEventCompanyEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "keywords");
        map.put("entity", FbEventCompanyKeywordEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.eventCompanyDetailEntityMap);
        map.put("type", "company");
        map.put("entity", FbEventCompanyDetailEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.eventOrganizationDetailEntityMap);
        map.put("type", "organization");
        map.put("entity", FbEventOrganizationDetailEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.eventPersonDetailEntityMap);
        map.put("type", "person");
        map.put("entity", FbEventPersonDetailEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "eventPunish");
        map.put("entity", FbEventPunishDetailEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "eventSource");
        map.put("entity", FbEventPunishSourceEntity.class);
        useMapList.add(map);
        
    }
    
    @Override
    public Map<String, Object> getEvent(String companyId) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        String requestUrl = dictionaryMap.get(FbInterfaceEnums.BASE_URL.getValue())+companyId+"/"+dictionaryMap.get(FbInterfaceEnums.EVENT.getValue())+ FbInterfaceEnums.APIKEY_SUFFIX.getValue()
            +dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue())+"&begin=1451606400&end=1569572408";
        
        //获取请求结果
        Map<String, Object> requestMap = FbHttpUtil.requestGet(requestUrl);
        
        parseJsonToEntity(dictionaryMap, requestMap, companyId, true);
        
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    @Transactional
    public Map<String, Object> getRecrusionEvent(String companyId, String url, String endTime, boolean isAll) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        System.out.println(Thread.currentThread().getName());
        String strDate = FbCommonUtil.getCurrentTimeStamp13();
        boolean flag = false;
        if(FbCommonUtil.stringIsNotValid(url)) {
            if(isAll) {
                //删除数据
                companyDetailRepository.deleteByCompanyId(companyId);
                companyKeywordRepository.deleteByCompanyId(companyId);
                organizationDetailRepository.deleteByCompanyId(companyId);
                personDetailRepository.deleteByCompanyId(companyId);
                punishDetailRepository.deleteByCompanyId(companyId);
                punishSourceRepository.deleteByCompanyId(companyId);
                //url
                url = dictionaryMap.get(FbInterfaceEnums.BASE_URL.getValue()) + companyId + FbInterfaceEnums.SEPRATOR.getValue()
                + dictionaryMap.get(FbInterfaceEnums.EVENT.getValue()) + FbInterfaceEnums.APIKEY_SUFFIX.getValue()
                + dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue()) + FbInterfaceEnums.BEGIN_SUFFIX.getValue()
                + FbCommonUtil.getMonthBeforeTimeStamp(strDate,
                        Integer.parseInt(dictionaryMap.get(FbInterfaceEnums.EVENT_INTERVAL.getValue())));
            }else {
                url = dictionaryMap.get(FbInterfaceEnums.BASE_URL.getValue()) + companyId + FbInterfaceEnums.SEPRATOR.getValue()
                + dictionaryMap.get(FbInterfaceEnums.EVENT.getValue()) + FbInterfaceEnums.APIKEY_SUFFIX.getValue()
                + dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue()) + FbInterfaceEnums.BEGIN_SUFFIX.getValue()
                + FbCommonUtil.getDayBeforeTimeStamp(strDate,1);
            }
            //用来判断是否保存公司信息
            flag = true;
        }
        String urlTemp = url;
        if(FbCommonUtil.stringIsNotValid(endTime)) {
            urlTemp = url + FbInterfaceEnums.END_SUFFIX.getValue() + FbCommonUtil.getCurrentTimeStamp(strDate);
        }else {
            urlTemp = url + FbInterfaceEnums.END_SUFFIX.getValue() + endTime;
        }
        Map<String, Object> requestMap = FbHttpUtil.requestGet(urlTemp);
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionEvent(companyId,url,endTime,isAll);
            }
            accessLogService.failed(FbEventCompanyEntity.class.getName(), AccessType.ADD, null,requestMap, null);
            return requestMap;
        }
        
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        //结果
        JSONObject object = jsonObject.getJSONObject(FbInterfaceEnums.JSON_EVENT.getValue());
        //下一页时间戳
        String newEndTime = String.valueOf(object.get("next_timestamp"));
        //封装保存
        parseJsonToEntity(dictionaryMap, requestMap, companyId,flag);
        //记录日志
        accessLogService.success(urlTemp, AccessType.ADD, null, requestMap);
        //判断是否有下一页
        if(FbCommonUtil.stringIsNotValid(newEndTime)) {
            return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
        }else {
            return getRecrusionEvent(companyId,url,newEndTime,isAll);
        }
    }
    /**
     * 封装结果入库
     *
     * @param dictionaryMap
     * @param requestMap
     * @param companyId
     * @param flag
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<String, Object> parseJsonToEntity(Map<String,String> dictionaryMap, Map<String, Object> requestMap, String companyId, boolean flag) throws Exception{

        List<FbEventCompanyEntity> eventCompanyList = new ArrayList<>();
        List<FbEventCompanyKeywordEntity> keywordsList = new ArrayList<>();
        
        Map<String, Object> newEntityResultMap = new HashMap<>();
        
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        JSONObject object = JSONObject.fromObject(jsonString);
        
        String saveCompanyId = null;
        
        if(flag) {
            if(object.get(FbInterfaceEnums.ALIAS.getValue())!=null && !object.get(FbInterfaceEnums.ALIAS.getValue()).toString().equals(FbInterfaceEnums.JSONOBJECT_NULL.getValue())) {
                JSONObject aliasObject = object.getJSONObject(FbInterfaceEnums.ALIAS.getValue());
                
                FbEventCompanyEntity companyEntity = FbBeanSetValueUtil.setValue(FbEventCompanyEntity.class, aliasObject, EntityMap.eventCompanyEntityMap);
                companyEntity.setCompanyId(companyId);
                companyEntity.setId(UUID.randomUUID().toString());
                eventCompanyList.add(companyEntity);
                
                Map<String, String> columnValueMap = new HashMap<String, String>();
                columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
                columnValueMap.put("eventCompanyId", companyEntity.getId());
                //关键词
                keywordsList = FbBeanSetValueUtil.saveDetailStringArrayEntity(aliasObject, "keywords", FbEventCompanyKeywordEntity.class, "name", columnValueMap);
                
                if(object.get(FbInterfaceEnums.JSON_ENTITY.getValue())!=null && !object.get(FbInterfaceEnums.JSON_ENTITY.getValue()).equals(FbInterfaceEnums.JSONOBJECT_NULL.getValue())) {
                    JSONObject entityObject = object.getJSONObject(FbInterfaceEnums.JSON_ENTITY.getValue());
                    
                    for(Map<String,Object> entrySet:useMapList) {
                        newEntityResultMap = FbBeanSetValueUtil.parseToMap(entityObject, entrySet.get("method").toString(), columnValueMap, entrySet.get("entityMap"), entrySet.get("type").toString(), (Class)entrySet.get("entity"), newEntityResultMap);
                    }
                }
                saveCompanyId = companyEntity.getId();
            }
        }else {
            saveCompanyId = getSaveEventCompanyIdByCompanyId(companyId);
        }
        Map<String, Object> tempEntityResultMap = saveEventHits(object, dictionaryMap, companyId, saveCompanyId);
        newEntityResultMap = FbBeanSetValueUtil.mergeMap(tempEntityResultMap, newEntityResultMap, useMapList);
        if(CollectionUtils.isNotEmpty(eventCompanyList)) {
            newEntityResultMap.put(useMapList.get(0).get("type").toString(), eventCompanyList);
        }
        if(CollectionUtils.isNotEmpty(keywordsList)) {
            newEntityResultMap.put(useMapList.get(1).get("type").toString(), keywordsList);
        }
        FbBeanSetValueUtil.saveDataToDB(newEntityResultMap,useMapList,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.SAVE_SUCCESS.getValue());
    }

    /**
     * 保存事件检索event：hits内容
     *
     * @param object
     * @param dictionaryMap
     * @param companyId
     * @param saveCompanyId
     * @return
     * @throws Exception
     */
    private Map<String, Object> saveEventHits(JSONObject object, Map<String, String> dictionaryMap, String companyId, String saveCompanyId) throws Exception{
        Map<String, Object> entityMap = new HashMap<>();
        if(object.get(FbInterfaceEnums.JSON_EVENT.getValue())!=null && !object.get(FbInterfaceEnums.JSON_EVENT.getValue()).toString().equals(FbInterfaceEnums.JSONOBJECT_NULL.getValue())) {
            JSONObject eventObject = object.getJSONObject(FbInterfaceEnums.JSON_EVENT.getValue());
            JSONArray eventArray = eventObject.getJSONArray(FbInterfaceEnums.HITS.getValue());
            if(eventArray!=null && eventArray.size()>0) {
                for(int j=0;j<eventArray.size();j++) {
                    Map<String, Object> tempEntityResultMap = parseJsonObjectToEntity(eventArray.getJSONObject(j), dictionaryMap, companyId, saveCompanyId);
                    entityMap = FbBeanSetValueUtil.mergeMap(entityMap, tempEntityResultMap, useMapList);
                }
            }
        }
        return entityMap;
    }

    @Override
    public Map<String, Object> parseJsonObjectToEntity(JSONObject detailObject, Map<String, String> dictionaryMap, String companyId, String saveCompanyId) throws Exception {
        
        Map<String, Object> entityMap = new HashMap<>();
        List<FbEventPunishDetailEntity> eventPunishList = new ArrayList<>();
        List<FbEventPunishSourceEntity> eventSourceList = new ArrayList<>();
        
        FbEventPunishDetailEntity eventPunishEntity = FbBeanSetValueUtil.setValue(FbEventPunishDetailEntity.class, detailObject, EntityMap.eventPunishDetailEntityMap);
        eventPunishEntity.setCompanyId(companyId);
        eventPunishEntity.setEventCompanyId(saveCompanyId);
        eventPunishEntity.setId(UUID.randomUUID().toString());
        eventPunishList.add(eventPunishEntity);
        
        String _id = eventPunishEntity.getSourceId();
        
        //获取封装url
        String sourceUrl = dictionaryMap.get(FbInterfaceEnums.EVENT_NEWS_URL.getValue())
                + FbInterfaceEnums.SEPRATOR.getValue() + _id.substring(0, _id.lastIndexOf("_"))
                + FbInterfaceEnums.APIKEY_SUFFIX.getValue()
                + dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue());
        //获取请求结果
        Map<String, Object> requestSourceMap = FbHttpUtil.requestGet(sourceUrl);
        
        if(!FbCommonUtil.getApputilMapFlag(requestSourceMap)) {
            accessLogService.failed(sourceUrl, AccessType.ADD, null, requestSourceMap, null);
            return requestSourceMap;
        }

        String sourceString = requestSourceMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        
        if(FbCommonUtil.stringIsValid(sourceString) && !sourceString.equals(FbInterfaceEnums.JSONOBJECT_NULL.getValue())) {
            JSONObject sourceObject = JSONObject.fromObject(sourceString);
            FbEventPunishSourceEntity sourceEntity = FbBeanSetValueUtil.setValue(FbEventPunishSourceEntity.class, sourceObject, EntityMap.eventPunishSourceEntityMap);
            sourceEntity.setCompanyId(companyId);
            sourceEntity.setEventCompanyId(saveCompanyId);
            sourceEntity.setEventDetailId(eventPunishEntity.getId());
            sourceEntity.setId(UUID.randomUUID().toString());
            eventSourceList.add(sourceEntity);
        }
        int size = useMapList.size();
        if(CollectionUtils.isNotEmpty(eventPunishList)) {
            entityMap.put(useMapList.get(size-2).get("type").toString(), eventPunishList);
        }
        if(CollectionUtils.isNotEmpty(eventSourceList)) {
            entityMap.put(useMapList.get(size-1).get("type").toString(), eventSourceList);
        }
        
        return entityMap;
    }
    
    @Override
    public String getSaveEventCompanyIdByCompanyId(String companyId) throws Exception{
        String saveCompanyId = null;
        //获取最新保存的companyId的记录id
        List<FbEventCompanyEntity> savedEntityList = repository.findByCompanyIdOrderByCreateDateDesc(companyId);
        
        if(CollectionUtils.isNotEmpty(savedEntityList)) {
            FbEventCompanyEntity savedEntity = savedEntityList.get(0);
            saveCompanyId = savedEntity.getId();
        }
        
        return saveCompanyId;
    }
    
}
