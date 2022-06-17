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

import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocParagraphEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyLegalEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyOtherEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyRoleEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocPartyUsedEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocSentenceEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudgeDocSentenceInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudgeDocRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbJudgeDocService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 裁判文书相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
@Service
public class FbJudgeDocServiceImpl extends DaoServiceImpl<FbJudgeDocEntity, String, FbJudgeDocRepository> implements FbJudgeDocService{

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
        map.put("entity", FbJudgeDocEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "judgeDocSentence");
        map.put("entity", FbJudgeDocSentenceEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", "name");
        map.put("type", "给付方");
        map.put("entity", FbJudgeDocSentenceInfoEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", null);
        map.put("type", "judgeDocParty");
        map.put("entity", FbJudgeDocPartyEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "name");
        map.put("type", "其他角色");
        map.put("entity", FbJudgeDocPartyOtherEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "name");
        map.put("type", "曾用名");
        map.put("entity", FbJudgeDocPartyUsedEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "name");
        map.put("type", "法定代表");
        map.put("entity", FbJudgeDocPartyLegalEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "string");
        map.put("entityMap", "role");
        map.put("type", "角色");
        map.put("entity", FbJudgeDocPartyRoleEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "");
        map.put("entityMap", EntityMap.judgeDocParagraphEntityMap);
        map.put("type", "段落");
        map.put("entity", FbJudgeDocParagraphEntity.class);
        useMapList.add(map);
    }
    
    
    @Override
    public Map<String, Object> getJudgeDoc(String companyId) throws Exception {
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        Map<String, Object> requestMap = FbHttpUtil.getRequestMap(companyId,dictionaryMap,FbInterfaceEnums.CPWS.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            accessLogService.failed(FbJudgeDocEntity.class.getName(), AccessType.ADD, null,requestMap, null);
            return requestMap;
        }
        
        parseJsonToEntity(requestMap, companyId);
        
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    public Map<String, Object> getRecrusionJudgeDoc(String companyId, String url, String pageId) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        Map<String, Object> requestMap = FbHttpUtil.getRecrusionRequestMap(dictionaryMap, companyId, url, pageId, FbInterfaceEnums.CPWS.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionJudgeDoc(companyId,url,pageId);
                
            }
            accessLogService.failed(FbJudgeDocEntity.class.getName(), AccessType.ADD, null,requestMap, null);
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
            return getRecrusionJudgeDoc(companyId,url,pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
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
    public Map<String, Object> parseJsonObjectToEntity(JSONObject jsonObject,String companyId) throws Exception{
        
        List<FbJudgeDocEntity> judgeDocList = new ArrayList<>();
        List<FbJudgeDocSentenceEntity> judgeDocSentenceList = new ArrayList<>();
        List<FbJudgeDocPartyEntity> judgeDocPartyList = new ArrayList<>();
        
        Map<String, Object> newEntityResultMap = new HashMap<>();
        
        //获取裁判文书结果
        FbJudgeDocEntity entity = FbBeanSetValueUtil.setValue(FbJudgeDocEntity.class, jsonObject, EntityMap.judgeDocEntityMap);
        entity.setCompanyId(companyId);
        entity.setId(UUID.randomUUID().toString());
        judgeDocList.add(entity);
        
        //获取判决金额
        if(jsonObject.get("判决金额")!=null) {
            JSONArray sentenceArray = jsonObject.getJSONArray("判决金额");
            if(sentenceArray!=null && sentenceArray.size()>0) {
                for(int j=0;j<sentenceArray.size();j++) {
                    JSONObject sentenceObject = sentenceArray.getJSONObject(j);
                    FbJudgeDocSentenceEntity sentenceEntity = FbBeanSetValueUtil.setValue(FbJudgeDocSentenceEntity.class, sentenceObject, EntityMap.judgeDocSentenceEntityMap);
                    sentenceEntity.setCompanyId(companyId);
                    sentenceEntity.setJudgeDocId(entity.getId());
                    sentenceEntity.setId(UUID.randomUUID().toString());
                    judgeDocSentenceList.add(sentenceEntity);
                    
                    Map<String, String> judgeDocSentenceInfoColumnValueMap = new HashMap<>();
                    judgeDocSentenceInfoColumnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
                    judgeDocSentenceInfoColumnValueMap.put("judgeDocId", entity.getId());
                    judgeDocSentenceInfoColumnValueMap.put("judgeDocSentenceId", sentenceEntity.getId());
                    newEntityResultMap = FbBeanSetValueUtil.parseToMap(sentenceObject, "string", judgeDocSentenceInfoColumnValueMap, useMapList.get(2).get("entityMap"), useMapList.get(2).get("type").toString(), FbJudgeDocSentenceInfoEntity.class, newEntityResultMap);
                }
            }
        }
        
        //获取当事人
        if(jsonObject.get(FbInterfaceEnums.PARTY.getValue())!=null) {
            JSONArray partyArray = jsonObject.getJSONArray(FbInterfaceEnums.PARTY.getValue());
            if(partyArray!=null && partyArray.size()>0) {
                for(int j=0;j<partyArray.size();j++) {
                    JSONObject partyObject = partyArray.getJSONObject(j);
                    FbJudgeDocPartyEntity partyEntity = FbBeanSetValueUtil.setValue(FbJudgeDocPartyEntity.class, partyObject, EntityMap.judgeDocPartyEntityMap);
                    partyEntity.setCompanyId(companyId);
                    partyEntity.setJudgeDocId(entity.getId());
                    partyEntity.setId(UUID.randomUUID().toString());
                    judgeDocPartyList.add(partyEntity);
                    
                    //其他角色
                    Map<String, String> columnValueMap = new HashMap<>();
                    columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
                    columnValueMap.put("judgeDocId", entity.getId());
                    columnValueMap.put("judgeDocPartyId", partyEntity.getId());
                    
                    for(Map<String,Object> entrySet:useMapList) {
                        newEntityResultMap = FbBeanSetValueUtil.parseToMap(partyObject, entrySet.get("method").toString(), columnValueMap, entrySet.get("entityMap"), entrySet.get("type").toString(), (Class)entrySet.get("entity"), newEntityResultMap);
                    }
                }
            }
        }
        //获取段落
        Map<String, String> paragraphColumnValueMap = new HashMap<String, String>();
        paragraphColumnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
        paragraphColumnValueMap.put("judgeDocId", entity.getId());
        List<FbJudgeDocParagraphEntity> judgeDocParagraphList = FbBeanSetValueUtil.saveDetailArrayEntity(jsonObject, "段落", FbJudgeDocParagraphEntity.class, EntityMap.judgeDocParagraphEntityMap, paragraphColumnValueMap);

        
        if(CollectionUtils.isNotEmpty(judgeDocList)) {
            newEntityResultMap.put(useMapList.get(0).get("type").toString(), judgeDocList);
        }
        if(CollectionUtils.isNotEmpty(judgeDocSentenceList)) {
            newEntityResultMap.put(useMapList.get(1).get("type").toString(), judgeDocSentenceList);
        }
        if(CollectionUtils.isNotEmpty(judgeDocPartyList)) {
            newEntityResultMap.put(useMapList.get(3).get("type").toString(), judgeDocPartyList);
        }
        if(CollectionUtils.isNotEmpty(judgeDocParagraphList)) {
            newEntityResultMap.put(useMapList.get(useMapList.size()-1).get("type").toString(), judgeDocParagraphList);
        }
        
        return newEntityResultMap;
    }
}
