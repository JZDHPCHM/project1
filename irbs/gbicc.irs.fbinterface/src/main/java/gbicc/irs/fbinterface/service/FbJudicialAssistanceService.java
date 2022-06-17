package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbJudicialAssistanceEntity;
import gbicc.irs.fbinterface.jpa.repository.FbJudicialAssistanceRepository;
import net.sf.json.JSONObject;

/**
 * 司法协助相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
public interface FbJudicialAssistanceService extends DaoService<FbJudicialAssistanceEntity, String, FbJudicialAssistanceRepository>{

    /**
     * 根据companyId获取司法协助结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getJudicialAssistance(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbJudicialAssistanceEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
