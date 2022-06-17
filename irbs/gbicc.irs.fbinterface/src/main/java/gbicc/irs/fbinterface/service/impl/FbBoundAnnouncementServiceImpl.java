package gbicc.irs.fbinterface.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.model.access.support.AccessType;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbBoundAnnouncementEntity;
import gbicc.irs.fbinterface.jpa.entity.FbLitigationNoticeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbBoundAnnouncementRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbBoundAnnouncementService;
import net.sf.json.JSONObject;

/**
 * 债券公告相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Service
public class FbBoundAnnouncementServiceImpl extends DaoServiceImpl<FbBoundAnnouncementEntity, String, FbBoundAnnouncementRepository> implements FbBoundAnnouncementService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getBoundAnnouncement(String companyId) throws Exception {
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        Map<String, Object> requestMap = FbHttpUtil.getRequestUrlMap(companyId,dictionaryMap,FbInterfaceEnums.ZQGG.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestMap)) {
            accessLogService.failed(FbLitigationNoticeEntity.class.getName(), AccessType.ADD, null,null, requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
            return requestMap;
        }
        
        //递归获取所有数据
        Map<String, Object> result = FbBeanSetValueUtil.getJsonResultMap(FbBoundAnnouncementEntity.class,
                new ArrayList<FbBoundAnnouncementEntity>(), companyId,
                requestMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), "",
                EntityMap.boundAnnouncementEntityMap,accessLogService);
        
        if(!FbCommonUtil.getApputilMapFlag(result)) {
            return result;
        }
        
        List<FbBoundAnnouncementEntity> list = (List<FbBoundAnnouncementEntity>) result.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue());
//        add(list);
        FbBeanSetValueUtil.saveDataToDB(list,FbBoundAnnouncementEntity.class,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
        
    }
    
    @Override
    public FbBoundAnnouncementEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception {
        FbBoundAnnouncementEntity entity = FbBeanSetValueUtil.setValue(FbBoundAnnouncementEntity.class, detailObject, EntityMap.boundAnnouncementEntityMap);
        entity.setCompanyId(companyId);
        return entity;
    }
}
