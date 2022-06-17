package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbFaithExecutePersonEntity;
import gbicc.irs.fbinterface.jpa.repository.FbFaithExecutePersonRepository;
import net.sf.json.JSONObject;

/**
 * 失信被执行人相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月18日
 */
public interface FbFaithExecutePersonService extends DaoService<FbFaithExecutePersonEntity, String, FbFaithExecutePersonRepository>{

    /**
     * 根据companyId获取失信被执行人结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getFaithExecutePerson(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbFaithExecutePersonEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
