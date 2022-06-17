package gbicc.irs.fbinterface.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.fbinterface.jpa.entity.FbEventCompanyKeywordEntity;
/**
 * 事件检索公司简称相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventCompanyKeywordRepository extends DaoRepository<FbEventCompanyKeywordEntity, String>{

    /**
     * 根据companyId删除数据
     *
     * @param companyId
     */
    void deleteByCompanyId(String companyId);

}
