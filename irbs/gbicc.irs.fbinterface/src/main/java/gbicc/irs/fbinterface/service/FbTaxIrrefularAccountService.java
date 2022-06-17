package gbicc.irs.fbinterface.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbTaxIrrefularAccountEntity;
import gbicc.irs.fbinterface.jpa.repository.FbTaxIrrefularAccountRepository;
import net.sf.json.JSONObject;

/**
 * 税务非正常户相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月19日
 */
public interface FbTaxIrrefularAccountService extends DaoService<FbTaxIrrefularAccountEntity, String, FbTaxIrrefularAccountRepository>{

    /**
     * 根据companyId获取税务非正常户结果
     *
     * @param companyId
     * @return
     */
    Map<String, Object> getTaxIrrefularAccount(String companyId) throws Exception;

    /**
     * 增量接口数据保存
     *
     * @param detailObject
     * @param companyId
     * @return
     * @throws Exception
     */
    FbTaxIrrefularAccountEntity parseJsonObjectToEntity(JSONObject detailObject, String companyId) throws Exception;

}
