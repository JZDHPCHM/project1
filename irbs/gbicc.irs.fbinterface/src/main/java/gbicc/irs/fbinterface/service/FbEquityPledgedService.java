package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbEquityPledgedEntity;
import gbicc.irs.fbinterface.jpa.repository.FbEquityPledgedRepository;
import net.sf.json.JSONObject;

/**
 * 股权出质结果
 * 
 * @author songxubei
 * @version v1.0 2019年9月17日
 */
public interface FbEquityPledgedService extends DaoService<FbEquityPledgedEntity, String, FbEquityPledgedRepository>{

    /**
     * 根据companyId获取股权出质结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getEquityPledgedInfo(String companyId) throws Exception;
    /**
     * 增量接口数据保存
     *
     * @param jsonObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbEquityPledgedEntity parseJsonObjectToEntity(JSONObject jsonObject,String companyId) throws Exception;

}
