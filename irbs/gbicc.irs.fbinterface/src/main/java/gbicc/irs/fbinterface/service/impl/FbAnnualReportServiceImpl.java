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

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportAssetsEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportBaseInfoEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportBylawsEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportEquityChangeEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportGuaranteeEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportInvestmentEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportModifyLogEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportOperateScopeEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportOrgInfoEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportShareholderEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportSocialInsurEntity;
import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportWebsiteEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportAssetsRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportBaseInfoRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportBylawsRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportEquityChangeRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportGuaranteeRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportInvestmentRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportModifyLogRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportOperateScopeRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportOrgInfoRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportShareholderRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportSocialInsurRepository;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportWebsiteRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbAnnualReportService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 年报相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportServiceImpl extends DaoServiceImpl<FbAnnualReportEntity, String, FbAnnualReportRepository> implements FbAnnualReportService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FbAnnualReportAssetsRepository assetsRepository;
    @Autowired
    private FbAnnualReportBaseInfoRepository baseInfoRepository;
    @Autowired
    private FbAnnualReportBylawsRepository bylawsReporsitory;
    @Autowired
    private FbAnnualReportEquityChangeRepository equityChangeRepository;
    @Autowired
    private FbAnnualReportGuaranteeRepository guaranteeRepository;
    @Autowired
    private FbAnnualReportInvestmentRepository investmentRepository;
    @Autowired
    private FbAnnualReportModifyLogRepository modifyLogRepository;
    @Autowired
    private FbAnnualReportOperateScopeRepository operateScopeRepository;
    @Autowired
    private FbAnnualReportOrgInfoRepository orgInfoRepository;
    @Autowired
    private FbAnnualReportShareholderRepository shareholderRepository;
    @Autowired
    private FbAnnualReportSocialInsurRepository socialInsurRepository;
    @Autowired
    private FbAnnualReportWebsiteRepository websiteRepository;
    
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
        map.put("entity", FbAnnualReportEntity.class);
        useMapList.add(map);
        map = new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.annualReportModifyLogEntityMap);
        map.put("type", "修改记录");
        map.put("entity", FbAnnualReportModifyLogEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.annualReportOrgInfoEntityMap);
        map.put("type", "分支机构登记信息");
        map.put("entity", FbAnnualReportOrgInfoEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "object");
        map.put("entityMap", EntityMap.annualReportBaseInfoEntityMap);
        map.put("type", "基本信息");
        map.put("entity", FbAnnualReportBaseInfoEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.annualReportInvestmentEntityMap);
        map.put("type", "对外投资信息");
        map.put("entity", FbAnnualReportInvestmentEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.annualReportGuaranteeEntityMap);
        map.put("type", "对外提供保证担保");
        map.put("entity", FbAnnualReportGuaranteeEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "object");
        map.put("entityMap", EntityMap.annualReportSocialInsurEntityMap);
        map.put("type", "社保信息");
        map.put("entity", FbAnnualReportSocialInsurEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "object");
        map.put("entityMap", EntityMap.annualReportBylawsEntityMap);
        map.put("type", "章程信息");
        map.put("entity", FbAnnualReportBylawsEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "object");
        map.put("entityMap", EntityMap.annualReportOperateScopeEntityMap);
        map.put("type", "经营范围");
        map.put("entity", FbAnnualReportOperateScopeEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.annualReportWebsiteEntityMap);
        map.put("type", "网站信息");
        map.put("entity", FbAnnualReportWebsiteEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.annualReportShareholderEntityMap);
        map.put("type", "股东及出资信息");
        map.put("entity", FbAnnualReportShareholderEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "array");
        map.put("entityMap", EntityMap.annualReportEquityChangeEntityMap);
        map.put("type", "股权变更");
        map.put("entity", FbAnnualReportEquityChangeEntity.class);
        useMapList.add(map);
        map=new HashMap<>();
        map.put("method", "object");
        map.put("entityMap", EntityMap.annualReportAssetsEntityMap);
        map.put("type", "资产状况信息");
        map.put("entity", FbAnnualReportAssetsEntity.class);
        useMapList.add(map);
    }
    
    @Override
    public Map<String, Object> getAnnualReport(String companyId) throws Exception {
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        //获取请求结果
        Map<String, Object> requestMap = FbHttpUtil.getRequestMap(companyId,dictionaryMap,FbInterfaceEnums.NIANBAO.getValue());

        parseJsonToEntity(requestMap, companyId);
        
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    @Transactional
    public Map<String, Object> getRecrusionAnnualReport(String companyId,String url, String pageId) throws Exception{
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        if(FbCommonUtil.stringIsNotValid(url)) {
            
            Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId, dictionaryMap, FbInterfaceEnums.NIANBAO.getValue());
            if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
                return requestUrlMap;
            }
            url = requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
            
            repository.deleteByCompanyId(companyId);
            assetsRepository.deleteByCompanyId(companyId);
            baseInfoRepository.deleteByCompanyId(companyId);
            bylawsReporsitory.deleteByCompanyId(companyId);
            equityChangeRepository.deleteByCompanyId(companyId);
            guaranteeRepository.deleteByCompanyId(companyId);
            investmentRepository.deleteByCompanyId(companyId);
            modifyLogRepository.deleteByCompanyId(companyId);
            operateScopeRepository.deleteByCompanyId(companyId);
            orgInfoRepository.deleteByCompanyId(companyId);
            shareholderRepository.deleteByCompanyId(companyId);
            socialInsurRepository.deleteByCompanyId(companyId);
            websiteRepository.deleteByCompanyId(companyId);
            
        }
        
        Map<String, Object> requestMap = FbHttpUtil.getRecrusionRequestMap(dictionaryMap, companyId, url, pageId, FbInterfaceEnums.NIANBAO.getValue());
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            if(FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getRecrusionAnnualReport(companyId,url,pageId);
            }
            accessLogService.failed(FbAnnualReportEntity.class.getName(), AccessType.ADD, null,requestMap, null);
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
            return getRecrusionAnnualReport(companyId,url,pageMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
        }
    }

    /**
     * 将请求结果进行封装入库
     *
     * @param requestMap
     * @param companyId
     * @param entityResultMap 
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<String, Object> parseJsonToEntity(Map<String, Object> requestMap,String companyId) throws Exception{
        
        Map<String, Object> newEntityResultMap = new HashMap<>();
        
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        JSONObject object = JSONObject.fromObject(jsonString);
        JSONArray jsonArray = object.getJSONArray(FbInterfaceEnums.HITS.getValue());
        if(jsonArray.size()<=0) {
            return AppUtil.getMap(false,FbCommonEnums.HITS_NULL.getValue());
        }
        
        int size = jsonArray.size();
        List<FbAnnualReportEntity> list = new ArrayList<>();
        for(int i = 0; i < size; i ++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            
            FbAnnualReportEntity entity = FbBeanSetValueUtil.setValue(FbAnnualReportEntity.class, jsonObject, EntityMap.annualReportEntityMap);
            entity.setCompanyId(companyId);
            entity.setId(UUID.randomUUID().toString());
            list.add(entity);
            Map<String, String> columnValueMap = new HashMap<String, String>();
            columnValueMap.put(FbCommonEnums.ENTITY_COMPANY_ID.getValue(), companyId);
            columnValueMap.put("annualReportId", entity.getId());
            
            for(Map<String,Object> entrySet:useMapList) {
                newEntityResultMap = FbBeanSetValueUtil.parseToMap(jsonObject, entrySet.get("method").toString(), columnValueMap, entrySet.get("entityMap"), entrySet.get("type").toString(), (Class)entrySet.get("entity"), newEntityResultMap);
            }
        }
        
        if(CollectionUtils.isNotEmpty(list)) {
            newEntityResultMap.put(useMapList.get(0).get("type").toString(), list);
        }
        FbBeanSetValueUtil.saveDataToDB(newEntityResultMap,useMapList,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.SAVE_SUCCESS.getValue());
    }
    
}
