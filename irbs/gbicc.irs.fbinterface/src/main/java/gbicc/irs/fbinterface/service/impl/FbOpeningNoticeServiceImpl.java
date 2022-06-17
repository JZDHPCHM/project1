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

import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticeEntity;
import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticeOtherEntity;
import gbicc.irs.fbinterface.jpa.entity.FbOpeningNoticePartyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOpeningNoticeRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbOpeningNoticeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 开庭公告相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Service
public class FbOpeningNoticeServiceImpl extends DaoServiceImpl<FbOpeningNoticeEntity, String, FbOpeningNoticeRepository> implements FbOpeningNoticeService{
   
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
        map.put("entity", FbOpeningNoticeEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "主表2");
        map.put("entity", FbOpeningNoticePartyEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "name");
        map.put("type", "其他角色");
        map.put("entity", FbOpeningNoticeOtherEntity.class);
        useMapList.add(map);
    }
    
    @Override
    public Map<String, Object> getOpeningNotice(String companyId) throws Exception{
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        Map<String, Object> requestMap = FbHttpUtil.getRequestMap(companyId,dictionaryMap,FbInterfaceEnums.KTGG.getValue());
        
        parseJsonToEntity(requestMap, companyId);
        
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    public Map<String, Object> getRecrusionOpeningNotice(String companyId, String url, String pageId) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        Map<String, Object> requestMap = FbHttpUtil.getRecrusionRequestMap(dictionaryMap, companyId, url, pageId, FbInterfaceEnums.KTGG.getValue());
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionOpeningNotice(companyId,url,pageId);
                
            }
            accessLogService.failed(FbOpeningNoticeEntity.class.getName(), AccessType.ADD, null,requestMap, null);
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
            return getRecrusionOpeningNotice(companyId,url,pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
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
        List<FbOpeningNoticeEntity> openingNoticeList = new ArrayList<>();
        List<FbOpeningNoticePartyEntity> openingNoticePartyList = new ArrayList<>();
        Map<String, Object> newEntityResultMap = new HashMap<>();
        
        //获取开庭公告结果
        FbOpeningNoticeEntity entity = FbBeanSetValueUtil.setValue(FbOpeningNoticeEntity.class, detailObject, EntityMap.openingNoticeEntityMap);
        entity.setCompanyId(companyId);
        entity.setId(UUID.randomUUID().toString());
        openingNoticeList.add(entity);
        //获取当事人结果记录
        if(detailObject.get(FbInterfaceEnums.PARTY_DETAL.getValue())==null) {
            return AppUtil.getMap(true,FbCommonEnums.SAVE_SUCCESS.getValue());
        }
        JSONArray partyArray = detailObject.getJSONArray(FbInterfaceEnums.PARTY_DETAL.getValue());
        
        if(partyArray!=null && partyArray.size()>0) {
            for(int j=0;j<partyArray.size();j++) {
                JSONObject partyObject = partyArray.getJSONObject(j);
                FbOpeningNoticePartyEntity partyEntity = FbBeanSetValueUtil.setValue(FbOpeningNoticePartyEntity.class, partyObject, EntityMap.openingNoticePartyEntityMap);
                partyEntity.setCompanyId(companyId);
                partyEntity.setOpeningNoticeId(entity.getId());
                partyEntity.setId(UUID.randomUUID().toString());
                openingNoticePartyList.add(partyEntity);
                
                Map<String, String> columnValueMap = new HashMap<String, String>();
                columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
                columnValueMap.put("openingNoticeId", entity.getId());
                columnValueMap.put("openingNoticePartyId", partyEntity.getId());
                newEntityResultMap = FbBeanSetValueUtil.parseToMap(partyObject, useMapList.get(2).get("method").toString(), columnValueMap, useMapList.get(2).get("entityMap"), useMapList.get(2).get("type").toString(), (Class)useMapList.get(2).get("entity"), newEntityResultMap);
            }
        }
        
        if(CollectionUtils.isNotEmpty(openingNoticeList)) {
            newEntityResultMap.put(useMapList.get(0).get("type").toString(), openingNoticeList);
        }
        if(CollectionUtils.isNotEmpty(openingNoticePartyList)) {
            newEntityResultMap.put(useMapList.get(1).get("type").toString(), openingNoticePartyList);
        }
        return newEntityResultMap;
    }

}
