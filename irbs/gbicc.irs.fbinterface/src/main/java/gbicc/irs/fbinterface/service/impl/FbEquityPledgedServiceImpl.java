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

import gbicc.irs.fbinterface.jpa.entity.FbEquityPledgedEntity;
import gbicc.irs.fbinterface.jpa.entity.FbLitigationNoticeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEquityPledgedRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbEquityPledgedService;
import net.sf.json.JSONObject;

/**
 * 股权出质结果操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月17日
 */
@Service
public class FbEquityPledgedServiceImpl extends DaoServiceImpl<FbEquityPledgedEntity, String, FbEquityPledgedRepository> implements FbEquityPledgedService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getEquityPledgedInfo(String companyId) throws Exception{
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);

        //获取封装后的url
        Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId,dictionaryMap,FbInterfaceEnums.GQCZ.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
            accessLogService.failed(FbLitigationNoticeEntity.class.getName(), AccessType.ADD, null,null, requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString());
            return requestUrlMap;
        }
        
        //递归获取所有数据
        Map<String, Object> result = FbBeanSetValueUtil.getJsonResultMap(FbEquityPledgedEntity.class,
                new ArrayList<FbEquityPledgedEntity>(), companyId,
                requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), "",
                EntityMap.equityPledgedEntityMap,accessLogService);
        
        if(!FbCommonUtil.getApputilMapFlag(result)) {
            return result;
        }
        List<FbEquityPledgedEntity> list = (List<FbEquityPledgedEntity>) result.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue());
//        add(list);
        FbBeanSetValueUtil.saveDataToDB(list,FbEquityPledgedEntity.class,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    
    }
    
    @Override
    public FbEquityPledgedEntity parseJsonObjectToEntity(JSONObject jsonObject,String companyId) throws Exception{
        
        FbEquityPledgedEntity entity = FbBeanSetValueUtil.setValue(FbEquityPledgedEntity.class, jsonObject, EntityMap.equityPledgedEntityMap);
        entity.setCompanyId(companyId);
        return entity;
        
    }
}
