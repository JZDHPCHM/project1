package gbicc.irs.fbinterface.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbTaxIrrefularAccountEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTaxIrrefularAccountRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbTaxIrrefularAccountService;
import net.sf.json.JSONObject;

/**
 * 税务非正常户相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
@Service
public class FbTaxIrrefularAccountServiceImpl extends DaoServiceImpl<FbTaxIrrefularAccountEntity, String, FbTaxIrrefularAccountRepository> implements FbTaxIrrefularAccountService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getTaxIrrefularAccount(String companyId) throws Exception {
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        //获取封装后的url
        Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId, dictionaryMap, FbInterfaceEnums.SWFZCH.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
            return requestUrlMap;
        }
        
        //递归获取所有数据
        Map<String, Object> result = FbBeanSetValueUtil.getJsonResultMap(FbTaxIrrefularAccountEntity.class,
                new ArrayList<FbTaxIrrefularAccountEntity>(), companyId,
                requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), "",
                EntityMap.taxIrrefularAccountEntityMap,accessLogService);
        
        if(!FbCommonUtil.getApputilMapFlag(result)) {
            return result;
        }
        List<FbTaxIrrefularAccountEntity> list = (List<FbTaxIrrefularAccountEntity>) result.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue());
//        add(list);
        FbBeanSetValueUtil.saveDataToDB(list,FbTaxIrrefularAccountEntity.class,jdbcTemplate);
        return AppUtil.getMap(true,FbCommonEnums.RESULT_SUCCESS.getValue());
    }

    @Override
    public FbTaxIrrefularAccountEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception {
        FbTaxIrrefularAccountEntity entity = FbBeanSetValueUtil.setValue(FbTaxIrrefularAccountEntity.class, detailObject, EntityMap.taxIrrefularAccountEntityMap);
        entity.setCompanyId(companyId);
        return entity;
    }

}
