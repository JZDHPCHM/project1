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
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureAbstractEntity;
import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureEntity;
import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosurePartyEntity;
import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureSourceEntity;
import gbicc.irs.fbinterface.jpa.entity.FbInfoDisclosureTypeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbInfoDisclosureRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbInfoDisclosureService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 信息披露相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbInfoDisclosureServiceImpl extends DaoServiceImpl<FbInfoDisclosureEntity, String, FbInfoDisclosureRepository> implements FbInfoDisclosureService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public static final List<Map<String, Object>> useMapList = new ArrayList<Map<String,Object>>();
    
    @PostConstruct
    public void initChild() {
        if(CollectionUtils.isNotEmpty(useMapList)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "主表");
        map.put("entity", FbInfoDisclosureEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "source");
        map.put("type", "公告来源");
        map.put("entity", FbInfoDisclosureSourceEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "type");
        map.put("type", "公告类别");
        map.put("entity", FbInfoDisclosureTypeEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "name");
        map.put("type", "当事人");
        map.put("entity", FbInfoDisclosurePartyEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "digest");
        map.put("type", "摘要");
        map.put("entity", FbInfoDisclosureAbstractEntity.class);
        useMapList.add(map);
    }
    
    
    
    @Override
    public Map<String, Object> getInfoDisclosure(String companyId) throws Exception {
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        Map<String, Object> requestMap = FbHttpUtil.getRequestMap(companyId,dictionaryMap,FbInterfaceEnums.XXPL.getValue());
        
        parseJsonToEntity(requestMap, companyId);
        
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }
    
    @Override
    public Map<String, Object> getRecrusionInfoDisclosure(String companyId, String url, String pageId) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        Map<String, Object> requestMap = FbHttpUtil.getRecrusionRequestMap(dictionaryMap, companyId, url, pageId, FbInterfaceEnums.XXPL.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            
            if(requestMap!=null && FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionInfoDisclosure(companyId, url, pageId);
                
            }else {
                return requestMap;
            }
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
            return getRecrusionInfoDisclosure(companyId,url,pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
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
        
        Map<String, Object> newEntityResultMap = new HashMap<>();
        List<FbInfoDisclosureEntity> list = new ArrayList<>();
        //获取信息披露结果
        FbInfoDisclosureEntity entity = FbBeanSetValueUtil.setValue(FbInfoDisclosureEntity.class, detailObject, EntityMap.infoDisclosureEntityMap);
        entity.setCompanyId(companyId);
        entity.setId(UUID.randomUUID().toString());
        list.add(entity);
        Map<String, String> columnValueMap = new HashMap<String, String>();
        columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
        columnValueMap.put("infoDisclosureId", entity.getId());
        
        for(Map<String,Object> entrySet:useMapList) {
            newEntityResultMap = FbBeanSetValueUtil.parseToMap(detailObject, entrySet.get("method").toString(), columnValueMap, entrySet.get("entityMap"), entrySet.get("type").toString(), (Class)entrySet.get("entity"), newEntityResultMap);
        }
        
        if(CollectionUtils.isNotEmpty(list)) {
            newEntityResultMap.put(useMapList.get(0).get("type").toString(),list);
        }
        
        return newEntityResultMap;
    }

}
