package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAbnormalOperationEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAbnormalOperationRepository;
import net.sf.json.JSONObject;

/**
 * 经营异常相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
public interface FbAbnormalOperationService extends DaoService<FbAbnormalOperationEntity, String, FbAbnormalOperationRepository>{

    /**
     * 根据companyId获取经营异常结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getAbnormalOperation(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbAbnormalOperationEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
