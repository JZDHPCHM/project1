package gbicc.irs.fbinterface.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbMajorTaxViolationEntity;
import gbicc.irs.fbinterface.jpa.repository.FbMajorTaxViolationRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbMajorTaxViolationService;
import net.sf.json.JSONObject;

/**
 * 重大税收违法相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Service
public class FbMajorTaxViolationServiceImpl extends DaoServiceImpl<FbMajorTaxViolationEntity, String, FbMajorTaxViolationRepository> implements FbMajorTaxViolationService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @SuppressWarnings("unchecked")
    @Override
    @Async(value="fbTaskExecutor")
    public Map<String, Object> getMajorTaxViolation(String companyId) throws Exception{
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        //获取封装后的url
        Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId, dictionaryMap, FbInterfaceEnums.SSWF.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
            return requestUrlMap;
        }
        
        //递归获取所有数据
        Map<String, Object> result = FbBeanSetValueUtil.getJsonResultMap(FbMajorTaxViolationEntity.class,
                new ArrayList<FbMajorTaxViolationEntity>(), companyId,
                requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), "",
                EntityMap.majorTaxViolationEntityMap,accessLogService);
        
        if(!FbCommonUtil.getApputilMapFlag(result)) {
            return result;
        }
        List<FbMajorTaxViolationEntity> list = (List<FbMajorTaxViolationEntity>) result.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue());
//        add(list);
        FbBeanSetValueUtil.saveDataToDB(list,FbMajorTaxViolationEntity.class,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }

    @Override
    public FbMajorTaxViolationEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception {
        FbMajorTaxViolationEntity entity = FbBeanSetValueUtil.setValue(FbMajorTaxViolationEntity.class, detailObject, EntityMap.majorTaxViolationEntityMap);
        entity.setCompanyId(companyId);
        return entity;
    }

}
