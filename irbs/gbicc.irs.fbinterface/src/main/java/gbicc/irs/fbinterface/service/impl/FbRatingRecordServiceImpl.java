package gbicc.irs.fbinterface.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.access.service.AccessLogService;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import com.gbicc.aicr.system.util.AppUtil;

import gbicc.irs.fbinterface.jpa.entity.FbRatingRecordEntity;
import gbicc.irs.fbinterface.jpa.repository.FbRatingRecordRepository;
import gbicc.irs.fbinterface.jpa.support.entitymap.EntityMap;
import gbicc.irs.fbinterface.jpa.support.enums.FbCommonEnums;
import gbicc.irs.fbinterface.jpa.support.enums.FbInterfaceEnums;
import gbicc.irs.fbinterface.jpa.support.util.FbBeanSetValueUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbCommonUtil;
import gbicc.irs.fbinterface.jpa.support.util.FbHttpUtil;
import gbicc.irs.fbinterface.service.FbRatingRecordService;

/**
 * 评级记录相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
@Service
public class FbRatingRecordServiceImpl extends DaoServiceImpl<FbRatingRecordEntity, String, FbRatingRecordRepository> implements FbRatingRecordService{

    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    @SuppressWarnings("unchecked")
    @Override
    @Async(value="fbTaskExecutor")
    @Transactional
    public Map<String, Object> getRatingRecord(String companyId) throws Exception {
        
        repository.deleteByCompanyId(companyId);
        
        Map<String, String> dictionaryMap = systemDictionaryService.getDictionaryMap(FbInterfaceEnums.FB_INTERFACE.getValue(), Locale.CHINA);
        
        //获取封装后的url
        Map<String, Object> requestUrlMap = FbHttpUtil.getRequestUrlMap(companyId, dictionaryMap, FbInterfaceEnums.PJJL.getValue());
        
        if(!FbCommonUtil.getApputilMapFlag(requestUrlMap)) {
            return requestUrlMap;
        }
        
        //递归获取所有数据
        Map<String, Object> result = FbBeanSetValueUtil.getJsonResultMap(FbRatingRecordEntity.class,
                new ArrayList<FbRatingRecordEntity>(), companyId,
                requestUrlMap.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue()).toString(), "",
                EntityMap.ratingRecordEntityMap,accessLogService);
        
        if(!FbCommonUtil.getApputilMapFlag(result)) {
            return result;
        }
        List<FbRatingRecordEntity> list = (List<FbRatingRecordEntity>) result.get(FbCommonEnums.APPUTIL_MAP_DATA.getValue());
//        add(list);
        FbBeanSetValueUtil.saveDataToDB(list,FbRatingRecordEntity.class,jdbcTemplate);
        return AppUtil.getMap(true,"获取成功");
    }

}
