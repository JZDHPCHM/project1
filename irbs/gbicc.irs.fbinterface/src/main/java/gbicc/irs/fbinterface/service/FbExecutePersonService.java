package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbExecutePersonEntity;
import gbicc.irs.fbinterface.jpa.repository.FbExecutePersonRepository;
import net.sf.json.JSONObject;
/**
 * 被执行人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
public interface FbExecutePersonService extends DaoService<FbExecutePersonEntity, String, FbExecutePersonRepository>{

    /**
     * 根据companyId获取被执行人结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getExecutePerson(String companyId) throws Exception;
    
    /**
     * 根据companyId获取被执行人结果，递归获取所有
     *
     * @param companyId
     * @param url
     * @param pageId
     * @return
     * @throws Exception
     */
    Map<String, Object> getRecrusionExecutePerson(String companyId, String url, String pageId) throws Exception;

    /**
     * 增量接口数据获取
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    Map<String, Object> parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;


}
