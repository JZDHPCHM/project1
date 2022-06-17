package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbTrialProcessEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTrialProcessRepository;
import net.sf.json.JSONObject;

/**
 * 审判流程相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbTrialProcessService extends DaoService<FbTrialProcessEntity, String, FbTrialProcessRepository>{

    /**
     * 根据companyId获取审判流程结果
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getTrialProcess(String companyId) throws Exception;
    /**
     * 根据companyId获取审判流程结果，递归获取所有
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionTrialProcess(String companyId, String url, String pageId) throws Exception;
    
    /**
     * 增量接口数据保存
     * 
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
