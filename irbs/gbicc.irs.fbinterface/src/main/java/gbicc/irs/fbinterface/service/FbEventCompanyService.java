package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEventCompanyRepository;
import net.sf.json.JSONObject;
/**
 * 事件检索公司信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventCompanyService extends DaoService<FbEventCompanyEntity, String, FbEventCompanyRepository>{

    /**
     * 根据companyId获取事件检索结果
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> getEvent(String companyId) throws Exception;
    /**
     * 根据companyId获取事件检索结果，递归获取所有结果
     *
     * @param companyId
     * @param url
     * @param endTime
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionEvent(String companyId, String url, String endTime, boolean isAll) throws Exception;
    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param dictionaryMap
     * @param companyId
     * @param saveCompanyId
     * @return
     * @throws Exception
     */
    Map<String, Object> parseJsonObjectToEntity(JSONObject detailObject, Map<String, String> dictionaryMap, String companyId, String saveCompanyId) throws Exception;

    /**
     * 根据companyId获取保存Id
     *
     * @param companyId
     * @return
     * @throws Exception
     */
    String getSaveEventCompanyIdByCompanyId(String companyId) throws Exception;
}
