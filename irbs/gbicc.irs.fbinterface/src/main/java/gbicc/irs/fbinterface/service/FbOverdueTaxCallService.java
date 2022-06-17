package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbOverdueTaxCallEntity;
import gbicc.irs.fbinterface.jpa.repository.FbOverdueTaxCallReposity;
import net.sf.json.JSONObject;

/**
 * 催缴/欠税相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
public interface FbOverdueTaxCallService extends DaoService<FbOverdueTaxCallEntity, String, FbOverdueTaxCallReposity>{

    /**
     * 根据companyId获取催缴/欠税结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getOverdueTaxCall(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbOverdueTaxCallEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
