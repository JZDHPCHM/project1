package gbicc.irs.fbinterface.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbAbnormalOperationEntity;
import gbicc.irs.fbinterface.jpa.entity.FbBoundAnnouncementEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEquityPledgedEntity;
import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyEntity;
import gbicc.irs.fbinterface.jpa.entity.FbFaithExecutePersonEntity;
import gbicc.irs.fbinterface.jpa.entity.FbIndustrialChangeEntity;
import gbicc.irs.fbinterface.jpa.entity.FbJudicialAssistanceEntity;
import gbicc.irs.fbinterface.jpa.entity.FbMajorTaxViolationEntity;
import gbicc.irs.fbinterface.jpa.entity.FbOverdueTaxCallEntity;
import gbicc.irs.fbinterface.jpa.entity.FbTaxIrrefularAccountEntity;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbAbnormalOperationService;
import gbicc.irs.fbinterface.service.FbBoundAnnouncementService;
import gbicc.irs.fbinterface.service.FbEquityPledgedService;
import gbicc.irs.fbinterface.service.FbEventCompanyService;
import gbicc.irs.fbinterface.service.FbExecutePersonService;
import gbicc.irs.fbinterface.service.FbFaithExecutePersonService;
import gbicc.irs.fbinterface.service.FbIndustrialChangeService;
import gbicc.irs.fbinterface.service.FbInfoDisclosureService;
import gbicc.irs.fbinterface.service.FbInterfaceService;
import gbicc.irs.fbinterface.service.FbJudgeDocService;
import gbicc.irs.fbinterface.service.FbJudicialAssistanceService;
import gbicc.irs.fbinterface.service.FbLitigationNoticeService;
import gbicc.irs.fbinterface.service.FbMajorTaxViolationService;
import gbicc.irs.fbinterface.service.FbOpeningNoticeService;
import gbicc.irs.fbinterface.service.FbOverdueTaxCallService;
import gbicc.irs.fbinterface.service.FbPunishService;
import gbicc.irs.fbinterface.service.FbRatingRecordService;
import gbicc.irs.fbinterface.service.FbShareholderInfoService;
import gbicc.irs.fbinterface.service.FbTaxIrrefularAccountService;
import gbicc.irs.fbinterface.service.FbTrialProcessService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 风报接口service实现类
 * 
 * @author songxubei
 * @version v1.0 2019年10月9日
 */
@Service
public class FbInterfaceServiceImpl implements FbInterfaceService {
    
    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private FbEquityPledgedService equityPledgedService;
    @Autowired
    private FbIndustrialChangeService industrialChangeService;
    @Autowired
    private FbJudicialAssistanceService judicialAssistance;
    @Autowired
    private FbOverdueTaxCallService overdueTaxCallService;
    @Autowired
    private FbTaxIrrefularAccountService taxIrrefularAccountService;
    @Autowired
    private FbAbnormalOperationService abnormalOperationService;
    @Autowired
    private FbEventCompanyService eventCompanyService;
    @Autowired
    private FbJudgeDocService judgeDocService;
    @Autowired
    private FbOpeningNoticeService openingNoticeService;
    @Autowired
    private FbFaithExecutePersonService faithExecutePersonService;
    @Autowired
    private FbExecutePersonService executePersonService;
    @Autowired
    private FbTrialProcessService trialProcessService;
    @Autowired
    private FbLitigationNoticeService litigationNoticeService;
    @Autowired
    private FbMajorTaxViolationService majorTaxViolationService;
    @Autowired
    private FbPunishService punishService;
    @Autowired
    private FbInfoDisclosureService infoDisclosureService;
    @Autowired
    private FbBoundAnnouncementService boundAnnouncementService;
    @Autowired
    private FbAnnualReportServiceImpl annualReportService;
    @Autowired
    private FbRatingRecordService ratingRecordService;
    @Autowired
    private FbShareholderInfoService shareholderInfoService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    @Async(value="fbTaskExecutor")
    public Map<String, Object> getIncrementalInterfaceData(String companyId, String beginTime,String endTime, Integer from, boolean flag) throws Exception{
        
        System.out.println(Thread.currentThread().getName()+"----"+companyId);
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        JSONObject params = getRequestParams(companyId,dictionaryMap,beginTime,endTime,flag);
        
        String url = dictionaryMap.get(FbInterfaceEnums.FOLLOWING_FEED.getValue()) + FbInterfaceEnums.APIKEY_SUFFIX.getValue()+dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue());
        if(FbCommonUtil.pageIntegerIsNotValid(from)) {
            from = 0;
        }
        url = url + FbInterfaceEnums.FROM_SUFFIX.getValue() + from;
        url = url + FbInterfaceEnums.SIZE_SUFFIX.getValue() + Integer.parseInt(FbInterfaceEnums.PAGE_SIZE.getValue());
        Map<String, Object> requestMap = FbHttpUtil.requestPost(url, params.toString());
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            //TODO 超过次数限制，应该是要暂停等待，不能停止，但是要暂停多长时间
            if(requestMap!=null && FbInterfaceEnums.STATUS_CODE_429.getValue().equals(requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString())) {
                
                System.out.println("------------------请求次数超过限制-----------------");
                Thread.sleep(5000);
                //TODO 重新请求，抓取
                return getIncrementalInterfaceData(companyId, beginTime, endTime, from, flag);
                
            }else {
                return requestMap;
            }
        }
        
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        JSONObject object = JSONObject.fromObject(jsonString);
        JSONArray jsonArray = object.getJSONArray(FbInterfaceEnums.HITS.getValue());
        if(jsonArray==null || jsonArray.size()<=0) {
            return AppUtil.getMap(false,FbCommonEnums.HITS_NULL.getValue());
        }
        Integer total = object.getInt(FbInterfaceEnums.TOTAL.getValue());
        int arraySize = jsonArray.size();
        
        List<FbEquityPledgedEntity> equityPledgedList = new ArrayList<>();
        List<FbIndustrialChangeEntity> industrialChangeList = new ArrayList<>();
        List<FbJudicialAssistanceEntity> judicialAssistanceList = new ArrayList<>();
        List<FbOverdueTaxCallEntity> overdueTaxCallList = new ArrayList<>();
        List<FbTaxIrrefularAccountEntity> taxIrrefularAccountList = new ArrayList<>();
        List<FbAbnormalOperationEntity> abnormalOperationList = new ArrayList<>();
        List<FbFaithExecutePersonEntity> faithExecutePersonList = new ArrayList<>();
        List<FbMajorTaxViolationEntity> majorTaxViolationList = new ArrayList<>();
        List<FbBoundAnnouncementEntity> boundAnnouncementList = new ArrayList<>();
        
        Map<String, Object> eventCompanyMap = new HashMap<>();
        Map<String, Object> judgeDocMap = new HashMap<>();
        Map<String, Object> openingNoticeMap = new HashMap<>();
        Map<String, Object> executePersonMap = new HashMap<>();
        Map<String, Object> trialProcessMap = new HashMap<>();
        Map<String, Object> litigationNoticeMap = new HashMap<>();
        Map<String, Object> punishMap = new HashMap<>();
        Map<String, Object> infoDisclosureMap = new HashMap<>();
        
        for(int i=0;i<arraySize;i++) {
            
            /**
             * TODO 后面试试优化
             * 
             * Map<String, Map<String,Object>>
             *   gqcz
             *   entity:FbEquityPledgedEntity
             *   type:entity/map  entity代表返回entity，保存使用list；map代表返回map
             *   service:equityPledgedService
             *   result:保存结果
             */
            
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String docType = jsonObject.getString(FbInterfaceEnums.DOC_TYPE.getValue());
            JSONObject detailObject = jsonObject.getJSONObject(FbInterfaceEnums.CONTENT.getValue());
            
            //股权出质
            if(dictionaryMap.get(FbInterfaceEnums.GQCZ.getValue()).equals(docType)) {
                equityPledgedList.add(equityPledgedService.parseJsonObjectToEntity(detailObject,companyId));
            }
            //工商变更
            if(dictionaryMap.get(FbInterfaceEnums.GSBG.getValue()).equals(docType)) {
                industrialChangeList.add(industrialChangeService.parseJsonObjectToEntity(detailObject,companyId));
            }
            //司法协助
            if(dictionaryMap.get(FbInterfaceEnums.SFXZ.getValue()).equals(docType)) {
                judicialAssistanceList.add(judicialAssistance.parseJsonObjectToEntity(detailObject,companyId));
            }
            //催缴/欠税
            if(dictionaryMap.get(FbInterfaceEnums.QSJL.getValue()).equals(docType)) {
                overdueTaxCallList.add(overdueTaxCallService.parseJsonObjectToEntity(detailObject,companyId));
            }
            //税务非正常户
            if(dictionaryMap.get(FbInterfaceEnums.SWFZCH.getValue()).equals(docType)) {
                taxIrrefularAccountList.add(taxIrrefularAccountService.parseJsonObjectToEntity(detailObject,companyId));
            }
            //经营异常
            if(dictionaryMap.get(FbInterfaceEnums.JYYC.getValue()).equals(docType)) {
                abnormalOperationList.add(abnormalOperationService.parseJsonObjectToEntity(detailObject,companyId));
            }
            //失信被执行人
            if(dictionaryMap.get(FbInterfaceEnums.SHIXIN.getValue()).equals(docType)) {
                faithExecutePersonList.add(faithExecutePersonService.parseJsonObjectToEntity(detailObject,companyId));
            }
            //重大税收违法
            if(dictionaryMap.get(FbInterfaceEnums.SSWF.getValue()).equals(docType)) {
                majorTaxViolationList.add(majorTaxViolationService.parseJsonObjectToEntity(detailObject,companyId));
            }
            //债券公告
            if(dictionaryMap.get(FbInterfaceEnums.ZQGG.getValue()).equals(docType)) {
                boundAnnouncementList.add(boundAnnouncementService.parseJsonObjectToEntity(detailObject,companyId));
            }
            
            //事件检索
            if(dictionaryMap.get(FbInterfaceEnums.EVENT.getValue()).equals(docType)) {
                String saveCompanyId = eventCompanyService.getSaveEventCompanyIdByCompanyId(companyId);
                if(FbCommonUtil.stringIsNotValid(saveCompanyId)) {
                    FbEventCompanyEntity eventCompanyEntity = new FbEventCompanyEntity();
                    eventCompanyEntity.setCompanyId(companyId);
                    eventCompanyEntity.setName(jsonObject.getString("name"));
                    FbEventCompanyEntity savedEntity = eventCompanyService.add(eventCompanyEntity);
                    saveCompanyId = savedEntity.getId();
                }
                Map<String, Object> tempMap = eventCompanyService.parseJsonObjectToEntity(detailObject, dictionaryMap, companyId, saveCompanyId);
                eventCompanyMap = FbBeanSetValueUtil.mergeMap(eventCompanyMap, tempMap, FbEventCompanyServiceImpl.useMapList);
            }
            //裁判文书
            if(dictionaryMap.get(FbInterfaceEnums.CPWS.getValue()).equals(docType)) {
                Map<String, Object> tempMap = judgeDocService.parseJsonObjectToEntity(detailObject, companyId);
                judgeDocMap = FbBeanSetValueUtil.mergeMap(judgeDocMap, tempMap, FbJudgeDocServiceImpl.useMapList);
            }
            //开庭公告
            if(dictionaryMap.get(FbInterfaceEnums.KTGG.getValue()).equals(docType)) {
                Map<String, Object> tempMap = openingNoticeService.parseJsonObjectToEntity(detailObject, companyId);
                openingNoticeMap = FbBeanSetValueUtil.mergeMap(openingNoticeMap, tempMap, FbOpeningNoticeServiceImpl.useMapList);
            }
            //被执行人
            if(dictionaryMap.get(FbInterfaceEnums.ZHIXING.getValue()).equals(docType)) {
                Map<String, Object> tempMap = executePersonService.parseJsonObjectToEntity(detailObject, companyId);
                executePersonMap = FbBeanSetValueUtil.mergeMap(executePersonMap, tempMap, FbExecutePersonServiceImpl.useMapList);
            }
            //审判流程
            if(dictionaryMap.get(FbInterfaceEnums.SPLC.getValue()).equals(docType)) {
                Map<String, Object> tempMap = trialProcessService.parseJsonObjectToEntity(detailObject, companyId);
                trialProcessMap = FbBeanSetValueUtil.mergeMap(trialProcessMap, tempMap, FbTrialProcessServiceImpl.useMapList);
            }
            //涉诉公告
            if(dictionaryMap.get(FbInterfaceEnums.SSGG.getValue()).equals(docType)) {
                Map<String, Object> tempMap = litigationNoticeService.parseJsonObjectToEntity(detailObject, companyId);
                litigationNoticeMap = FbBeanSetValueUtil.mergeMap(litigationNoticeMap, tempMap, FbLitigationNoticeServiceImpl.useMapList);
            }
            //行政处罚
            if(dictionaryMap.get(FbInterfaceEnums.XZCF2.getValue()).equals(docType)) {
                Map<String, Object> tempMap = punishService.parseJsonObjectToEntity(detailObject, companyId);
                punishMap = FbBeanSetValueUtil.mergeMap(punishMap, tempMap, FbPunishServiceImpl.useMapList);
            }
            //信息披露
            if(dictionaryMap.get(FbInterfaceEnums.XXPL.getValue()).equals(docType)) {
                Map<String, Object> tempMap = infoDisclosureService.parseJsonObjectToEntity(detailObject, companyId);
                infoDisclosureMap = FbBeanSetValueUtil.mergeMap(infoDisclosureMap, tempMap, FbInfoDisclosureServiceImpl.useMapList);
            }
        }
        
        FbBeanSetValueUtil.saveDataToDB(equityPledgedList, FbEquityPledgedEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(industrialChangeList, FbIndustrialChangeEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(judicialAssistanceList, FbJudicialAssistanceEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(overdueTaxCallList, FbOverdueTaxCallEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(taxIrrefularAccountList, FbTaxIrrefularAccountEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(abnormalOperationList, FbAbnormalOperationEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(faithExecutePersonList, FbFaithExecutePersonEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(majorTaxViolationList, FbMajorTaxViolationEntity.class, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(boundAnnouncementList, FbBoundAnnouncementEntity.class, jdbcTemplate);
        
        FbBeanSetValueUtil.saveDataToDB(eventCompanyMap, FbEventCompanyServiceImpl.useMapList, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(judgeDocMap, FbJudgeDocServiceImpl.useMapList, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(openingNoticeMap, FbOpeningNoticeServiceImpl.useMapList, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(trialProcessMap, FbTrialProcessServiceImpl.useMapList, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(litigationNoticeMap, FbLitigationNoticeServiceImpl.useMapList, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(punishMap, FbPunishServiceImpl.useMapList, jdbcTemplate);
        FbBeanSetValueUtil.saveDataToDB(infoDisclosureMap, FbInfoDisclosureServiceImpl.useMapList, jdbcTemplate);

        //from+size总数限制
        if(from+Integer.parseInt(FbInterfaceEnums.PAGE_SIZE.getValue())>10000) {
            //TODO 总数超过限制，是要停止还是继续，如果继续的话，该怎么处理：获取最后一条记录的时间重新封装参数？？
            
        }
        //循环获取数据
        if(total>from+Integer.parseInt(FbInterfaceEnums.PAGE_SIZE.getValue())) {
            return getIncrementalInterfaceData(companyId, beginTime, endTime, from+Integer.parseInt(FbInterfaceEnums.PAGE_SIZE.getValue()),flag);
        }
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }
    /**
     * 封装请求参数
     *
     * @param companyId
     * @param dictionaryMap
     * @param beginTime
     * @param endTime
     * @param flag 是否是第一次初始化
     * @return
     */
    private JSONObject getRequestParams(String companyId, Map<String, String> dictionaryMap, String beginTime,
            String endTime, boolean flag) {
        
        JSONArray crns = new JSONArray();
        crns.add(companyId);
        //接口类型
        JSONArray types = new JSONArray();
        
        //经营异常
        types.add(dictionaryMap.get(FbInterfaceEnums.JYYC.getValue()));
        //股权出质
        types.add(dictionaryMap.get(FbInterfaceEnums.GQCZ.getValue()));
        //催缴/欠税
        types.add(dictionaryMap.get(FbInterfaceEnums.QSJL.getValue()));
        //税务非正常户
        types.add(dictionaryMap.get(FbInterfaceEnums.SWFZCH.getValue()));
        //债券公告
        types.add(dictionaryMap.get(FbInterfaceEnums.ZQGG.getValue()));
        //信息披露
        types.add(dictionaryMap.get(FbInterfaceEnums.XXPL.getValue()));
        
        if(!flag) {
            //工商变更
            types.add(dictionaryMap.get(FbInterfaceEnums.GSBG.getValue()));
            //司法协助
            types.add(dictionaryMap.get(FbInterfaceEnums.SFXZ.getValue()));
            //失信被执行人
            types.add(dictionaryMap.get(FbInterfaceEnums.SHIXIN.getValue()));
            //重大税收违法
            types.add(dictionaryMap.get(FbInterfaceEnums.SSWF.getValue()));
            
            //裁判文书
            types.add(dictionaryMap.get(FbInterfaceEnums.CPWS.getValue()));
            //开庭公告
            types.add(dictionaryMap.get(FbInterfaceEnums.KTGG.getValue()));
            //被执行人
            types.add(dictionaryMap.get(FbInterfaceEnums.ZHIXING.getValue()));
            //审判流程
            types.add(dictionaryMap.get(FbInterfaceEnums.SPLC.getValue()));
            //涉诉公告
            types.add(dictionaryMap.get(FbInterfaceEnums.SSGG.getValue()));
            //行政处罚
            types.add(dictionaryMap.get(FbInterfaceEnums.XZCF2.getValue()));
        }
        //封装
        JSONObject params = new JSONObject();
        params.put("crns", crns);
        //日期范围
        params.put("date_range", beginTime+"-"+endTime);
        params.put("types", types);
        
        return params;
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    public void getAllDateFirst(String companyId) throws Exception{
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        String beginTime = FbCommonUtil.getStartTimeRequest(Integer.parseInt(dictionaryMap.get(FbInterfaceEnums.EVENT_INTERVAL.getValue())));
        String endTime = FbCommonUtil.getStartTimeOneDayRequest();
        
        getIncrementalInterfaceData(companyId, beginTime, endTime, null, true);
        industrialChangeService.getIndustrialChangeInfo(companyId);
        judicialAssistance.getJudicialAssistance(companyId);
        majorTaxViolationService.getMajorTaxViolation(companyId);
        faithExecutePersonService.getFaithExecutePerson(companyId);
        judgeDocService.getRecrusionJudgeDoc(companyId, null, null);
        openingNoticeService.getRecrusionOpeningNotice(companyId, null, null);
        executePersonService.getRecrusionExecutePerson(companyId, null, null);
        trialProcessService.getRecrusionTrialProcess(companyId, null, null);
        litigationNoticeService.getRecrusionLitigationNotice(companyId, null, null);
        punishService.getRecrusionPunish(companyId, null, null);
        eventCompanyService.getRecrusionEvent(companyId, null, null,true);
        annualReportService.getRecrusionAnnualReport(companyId, null, null);
        ratingRecordService.getRatingRecord(companyId);
        shareholderInfoService.getRecrusionShareholderInfo(companyId, null, null);
        
//        infoDisclosureService.getRecrusionInfoDisclosure(companyId, null, null);
    }
    
    @Override
    @Async(value="fbTaskExecutor")
    public void getIncrementalData(String companyId) throws Exception {
        String startTime = FbCommonUtil.getStartTimeOneDayRequest();
        getIncrementalInterfaceData(companyId, startTime, startTime, null, false);
        eventCompanyService.getRecrusionEvent(companyId, null, null,false);
        annualReportService.getRecrusionAnnualReport(companyId, null, null);
        ratingRecordService.getRatingRecord(companyId);
        shareholderInfoService.getRecrusionShareholderInfo(companyId, null, null);
    }
    
    @Override
    public List<String> getAllAttenFromFb() throws Exception{
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        String baseUrl = dictionaryMap.get(FbInterfaceEnums.FOLLOWING_FEED.getValue());
        baseUrl = baseUrl.substring(0,baseUrl.lastIndexOf(FbInterfaceEnums.SEPRATOR.getValue()));
        
        String url = baseUrl+FbInterfaceEnums.APIKEY_SUFFIX.getValue()+dictionaryMap.get(FbInterfaceEnums.APIKEY.getValue())
                    +FbInterfaceEnums.FROM_SUFFIX.getValue()+0
                    +FbInterfaceEnums.SIZE_SUFFIX.getValue()+Integer.parseInt(FbInterfaceEnums.PAGE_SIZE.getValue());

        List<String> attenCompanyIds = new ArrayList<>();
        
        Map<String, Object> requestMap = FbHttpUtil.requestGet(url);
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            return null;
        }
        String jsonString = requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString();
        JSONObject object = JSONObject.fromObject(jsonString);
        JSONArray jsonArray = object.getJSONArray(FbInterfaceEnums.HITS.getValue());
        
        if(jsonArray==null || jsonArray.size()<=0) {
            return null;
        }
        
        for(int i=0;i<jsonArray.size();i++) {
            attenCompanyIds.add(jsonArray.getString(i));
        }
        
        return attenCompanyIds;
    }
    @Override
    @Async(value="ruleTaskExecutor")
    public Future<Map<String,Object>> warnRuleRunner(String url) {
        
        Map<String, Object> requestMap = FbHttpUtil.requestGet(url);
        
        return new AsyncResult<Map<String,Object>>(requestMap);
    }
}
