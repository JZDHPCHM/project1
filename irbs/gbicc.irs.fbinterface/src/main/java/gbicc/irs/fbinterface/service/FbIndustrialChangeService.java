package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbIndustrialChangeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbIndustrialChangerRepository;
import net.sf.json.JSONObject;

/**
 * 工商变更相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
public interface FbIndustrialChangeService extends DaoService<FbIndustrialChangeEntity, String, FbIndustrialChangerRepository>{

    /**
     * 根据companyId获取工商变更结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getIndustrialChangeInfo(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @throws Exception
     */
    FbIndustrialChangeEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
