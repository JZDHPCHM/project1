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

import gbicc.irs.fbinterface.jpa.entity.FbIndustrialChangeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbIndustrialChangerRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbIndustrialChangeService;
import net.sf.json.JSONObject;

/**
 * 工商变更相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
@Service
public class FbIndustrialChangeServiceImpl extends DaoServiceImpl<FbIndustrialChangeEntity, String, FbIndustrialChangerRepository> implements FbIndustrialChangeService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @SuppressWarnings("unchecked")
    @Override
    @Async(value="fbTaskExecutor")
    public Map<String, Object> getIndustrialChangeInfo(String companyId) throws Exception{
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        //获取封装后的url
        Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId, dictionaryMap, FbInterfaceEnums.GSBG.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
            return requestUrlMap;
        }
        
        //递归获取所有数据
        Map<String, Object> result = FbBeanSetValueUtil.getJsonResultMap(FbIndustrialChangeEntity.class,
                new ArrayList<FbIndustrialChangeEntity>(), companyId,
                requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), "",
                EntityMap.industrialChangeEntityMap,accessLogService);
        
        if(!FbCommonUtil.getApputilMapFlag(result)) {
            return result;
        }
        List<FbIndustrialChangeEntity> list = (List<FbIndustrialChangeEntity>) result.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue());
//        add(list);
        FbBeanSetValueUtil.saveDataToDB(list,FbIndustrialChangeEntity.class,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }

    @Override
    public FbIndustrialChangeEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception {
        FbIndustrialChangeEntity entity = FbBeanSetValueUtil.setValue(FbIndustrialChangeEntity.class, detailObject, EntityMap.industrialChangeEntityMap);
        entity.setCompanyId(companyId);
        return entity;
    }

}
