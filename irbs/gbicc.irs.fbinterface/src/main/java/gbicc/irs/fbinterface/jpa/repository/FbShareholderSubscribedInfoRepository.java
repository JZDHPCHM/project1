package gbicc.irs.fbinterface.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.fbinterface.jpa.entity.FbShareholderSubscribedInfoEntity;

/**
 * 股东信息认缴明细相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbShareholderSubscribedInfoRepository extends DaoRepository<FbShareholderSubscribedInfoEntity, String>{

    /**
     * 根据companyId删除数据
     *
     * @param companyId
     */
    void deleteByCompanyId(String companyId);

}
