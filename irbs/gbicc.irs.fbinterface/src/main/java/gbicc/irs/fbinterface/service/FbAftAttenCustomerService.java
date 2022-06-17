package gbicc.irs.fbinterface.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAftAttenCustomerEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAftAttenCustomerRepository;

/**
 * 客户关注相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年10月18日
 */
public interface FbAftAttenCustomerService extends DaoService<FbAftAttenCustomerEntity, String, FbAftAttenCustomerRepository>{

    /**
     * 根据关注状态获取客户信息
     *
     * @param isAtten
     * @return
     */
    List<String> queryAttenCutomerByIsAtten(String isAtten);

    /**
     * 更新客户关注状态
     *
     * @param companyId
     * @param isAtten
     */
    void updateIsAttenByCompanyId(String companyId, String isAtten);

    /**
     * 获取去重后所有关联人统一社会信用代码
     *
     * @return
     */
    List<String> queryAssoCompanyId();

}
