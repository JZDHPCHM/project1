package gbicc.irs.fbinterface.jpa.repository;

import gbicc.irs.fbinterface.jpa.entity.FbEventOrganizationDetailEntity;

import org.wsp.framework.jpa.repository.DaoRepository;
/**
 * 事件检索组织实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventOrganizationDetailRepository extends DaoRepository<FbEventOrganizationDetailEntity, String>{

    /**
     * 根据companyId删除数据
     *
     * @param companyId
     */
    void deleteByCompanyId(String companyId);

}
