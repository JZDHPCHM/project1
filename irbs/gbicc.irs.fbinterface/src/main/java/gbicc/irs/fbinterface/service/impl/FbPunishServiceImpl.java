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

import gbicc.irs.fbinterface.jpa.entity.FbPunishBasisEntity;
import gbicc.irs.fbinterface.jpa.entity.FbPunishEntity;
import gbicc.irs.fbinterface.jpa.entity.FbPunishOrganEntity;
import gbicc.irs.fbinterface.jpa.entity.FbPunishPartyDetailEntity;
import gbicc.irs.fbinterface.jpa.entity.FbPunishPartyEntity;
import gbicc.irs.fbinterface.jpa.entity.FbPunishReasonEntity;
import gbicc.irs.fbinterface.jpa.entity.FbPunishResultEntity;
import gbicc.irs.fbinterface.jpa.entity.FbPunishTableEntity;
import gbicc.irs.fbinterface.jpa.repository.FbPunishRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbPunishService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 行政处罚相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月25日
 */
@Service
public class FbPunishServiceImpl extends DaoServiceImpl<FbPunishEntity, String, FbPunishRepository> implements FbPunishService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
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
        map.put("entity", FbPunishEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "reason");
        map.put("type", "处罚事由");
        map.put("entity", FbPunishReasonEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "reason");
        map.put("type", "处罚依据");
        map.put("entity", FbPunishBasisEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "result");
        map.put("type", "处罚决定");
        map.put("entity", FbPunishResultEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "name");
        map.put("type", "处罚决定机关");
        map.put("entity", FbPunishOrganEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "object");
        map.put("entityMap", EntityMap.punishTableEntityMap);
        map.put("type", "处罚表格");
        map.put("entity", FbPunishTableEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "party");
        map.put("entity", FbPunishPartyEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", EntityMap.punishPartyDetailEntityMap);
        map.put("type", "处罚详情");
        map.put("entity", FbPunishPartyDetailEntity.class);
        useMapList.add(map);
    }
    
    @Override
    public Map<String, Object> getPunish(String companyId) throws Exception {
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        Map<String, Object> requestMap = FbHttpUtil.getRequestMap(companyId,dictionaryMap,FbInterfaceEnums.XZCF2.getValue());
        
        parseJsonToEntity(requestMap, companyId);
        
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }

    @Override
    @Async(value="fbTaskExecutor")
    public Map<String, Object> getRecrusionPunish(String companyId, String url, String pageId) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        Map<String, Object> requestMap = FbHttpUtil.getRecrusionRequestMap(dictionaryMap, companyId, url, pageId, FbInterfaceEnums.XZCF2.getValue());
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionPunish(companyId,url,pageId);
                
            }
            accessLogService.failed(FbPunishEntity.class.getName(), AccessType.ADD, null,requestMap, null);
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
            return getRecrusionPunish(companyId,url,pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
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
        List<FbPunishEntity> punishList = new ArrayList<>();
        List<FbPunishPartyEntity> punishPartyList = new ArrayList<>();
        Map<String, Object> newEntityResultMap = new HashMap<>();
        int useMapListSize = useMapList.size();
        //获取行政处罚结果
        FbPunishEntity entity = FbBeanSetValueUtil.setValue(FbPunishEntity.class, detailObject, EntityMap.punishEntityMap);
        entity.setCompanyId(companyId);
        entity.setId(UUID.randomUUID().toString());
        punishList.add(entity);
        
        Map<String, String> columnValueMap = new HashMap<String, String>();
        columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
        columnValueMap.put("punishId", entity.getId());
        
        for(Map<String,Object> entrySet:useMapList) {
            newEntityResultMap = FbBeanSetValueUtil.parseToMap(detailObject, entrySet.get("method").toString(), columnValueMap, entrySet.get("entityMap"), entrySet.get("type").toString(), (Class)entrySet.get("entity"), newEntityResultMap);
        }
        //封装、保存当事人
        if(detailObject.get(FbInterfaceEnums.PARTY.getValue())!=null) {
            //获取当事人
            JSONArray partyArray = detailObject.getJSONArray(FbInterfaceEnums.PARTY.getValue());
            if(partyArray!=null && partyArray.size()>0) {
                for(int j=0;j<partyArray.size();j++) {
                    JSONObject partyObject = partyArray.getJSONObject(j);
                    FbPunishPartyEntity partyEntity = FbBeanSetValueUtil.setValue(FbPunishPartyEntity.class, partyObject, EntityMap.punishPartyEntityMap);
                    partyEntity.setCompanyId(companyId);
                    partyEntity.setPunishId(entity.getId());
                    partyEntity.setId(UUID.randomUUID().toString());
                    punishPartyList.add(partyEntity);
                    
                    Map<String, String> partyDetailColumnValueMap = new HashMap<>();
                    partyDetailColumnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
                    partyDetailColumnValueMap.put("punishId", entity.getId());
                    partyDetailColumnValueMap.put("punishParty", partyEntity.getId());
                    
                    newEntityResultMap = FbBeanSetValueUtil.parseToMap(detailObject, "array", columnValueMap, useMapList.get(useMapListSize-1).get("entityMap"), useMapList.get(useMapListSize-1).get("type").toString(), (Class)useMapList.get(useMapListSize-1).get("entity"), newEntityResultMap);
                }
            }
        }
        
        if(CollectionUtils.isNotEmpty(punishList)) {
            newEntityResultMap.put(useMapList.get(0).get("type").toString(), punishList);
        }
        if(CollectionUtils.isNotEmpty(punishPartyList)) {
            newEntityResultMap.put(useMapList.get(useMapListSize-2).get("type").toString(), punishPartyList);
        }
        return newEntityResultMap;
    }
}
