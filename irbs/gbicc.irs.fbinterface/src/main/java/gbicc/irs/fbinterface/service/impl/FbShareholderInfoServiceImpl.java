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

import gbicc.irs.fbinterface.jpa.entity.FbShareholderInfoEntity;
import gbicc.irs.fbinterface.jpa.entity.FbShareholderPaidDetailEntity;
import gbicc.irs.fbinterface.jpa.entity.FbShareholderSubscribedInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderInfoRepository;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderPaidDetailRepository;
import gbicc.irs.fbinterface.jpa.repository.FbShareholderSubscribedInfoRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbShareholderInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 股东信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbShareholderInfoServiceImpl extends DaoServiceImpl<FbShareholderInfoEntity, String, FbShareholderInfoRepository> implements FbShareholderInfoService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FbShareholderPaidDetailRepository paidDetailRepository;
    @Autowired
    private FbShareholderSubscribedInfoRepository subscribedInfoRepository;
    
    private static final List<Map<String, Object>> useMapList = new ArrayList<>();
    
    @PostConstruct
    public void initChild() {
        if(CollectionUtils.isNotEmpty(useMapList)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "主表");
        map.put("entity", FbShareholderInfoEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.shareholderPaidDetailEntityMap);
        map.put("type", "实缴明细");
        map.put("entity", FbShareholderPaidDetailEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.shareholderSubscribedInfoEntityMap);
        map.put("type", "认缴明细");
        map.put("entity", FbShareholderSubscribedInfoEntity.class);
        useMapList.add(map);
    }
    
    
    @Override
    public Map<String, Object> getShareholderInfo(String companyId) throws Exception {
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        Map<String, Object> requestMap = FbHttpUtil.getRequestMap(companyId,dictionaryMap,FbInterfaceEnums.INVESTOR.getValue());
        
        parseJsonToEntity(requestMap, companyId);
        
        return AppUtil.getMap(true,"获取成功");
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    @Transactional
    public Map<String, Object> getRecrusionShareholderInfo(String companyId, String url, String pageId) throws Exception {
        
        System.out.println(Thread.currentThread().getName());
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        if(FbCommonUtil.stringIsNotValid(url)) {
            
            Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId, dictionaryMap, FbInterfaceEnums.INVESTOR.getValue());
            if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
                return requestUrlMap;
            }
            url = requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
            
            repository.deleteByCompanyId(companyId);
            paidDetailRepository.deleteByCompanyId(companyId);
            subscribedInfoRepository.deleteByCompanyId(companyId);
        }
        
        Map<String, Object> requestMap = FbHttpUtil.getRecrusionRequestMap(dictionaryMap, companyId, url, pageId, FbInterfaceEnums.INVESTOR.getValue());
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionShareholderInfo(companyId,url,pageId);
            }
            accessLogService.failed(FbShareholderInfoEntity.class.getName(), AccessType.ADD, null,requestMap, null);
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
            return getRecrusionShareholderInfo(companyId,url,pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<String, Object> parseJsonToEntity(Map<String, Object> requestMap, String companyId) throws Exception{
        Map<String, Object> newEntityResultMap = new HashMap<>();
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        JSONObject object = JSONObject.fromObject(jsonString);
        JSONArray jsonArray = object.getJSONArray(FbInterfaceEnums.HITS.getValue());
        if(jsonArray.size()<=0) {
            return null;
        }
        int size = jsonArray.size();
        List<FbShareholderInfoEntity> list = new ArrayList<>();
        for(int i=0;i<size;i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //获取股东信息结果
            FbShareholderInfoEntity entity = FbBeanSetValueUtil.setValue(FbShareholderInfoEntity.class, jsonObject, EntityMap.shareholderInfoEntityMap);
            entity.setCompanyId(companyId);
            entity.setId(UUID.randomUUID().toString());
            list.add(entity);
            Map<String, String> columnValueMap = new HashMap<>();
            columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
            columnValueMap.put("shareholderInfoId", entity.getId());
            
            for(Map<String,Object> entrySet:useMapList) {
                newEntityResultMap = FbBeanSetValueUtil.parseToMap(jsonObject, entrySet.get("method").toString(), columnValueMap, entrySet.get("entityMap"), entrySet.get("type").toString(), (Class)entrySet.get("entity"), newEntityResultMap);
            }
        }
        
        if(CollectionUtils.isNotEmpty(list)) {
            newEntityResultMap.put("主表", list);
        }
        FbBeanSetValueUtil.saveDataToDB(newEntityResultMap,useMapList,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.SAVE_SUCCESS.getValue());
    }
    
}
