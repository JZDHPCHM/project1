package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbMajorTaxViolationEntity;
import gbicc.irs.fbinterface.jpa.repository.FbMajorTaxViolationRepository;
import net.sf.json.JSONObject;

/**
 * 重大税收违法相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
public interface FbMajorTaxViolationService extends DaoService<FbMajorTaxViolationEntity, String, FbMajorTaxViolationRepository>{

    /**
     * 根据companyId获取重大税收违法结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getMajorTaxViolation(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbMajorTaxViolationEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
