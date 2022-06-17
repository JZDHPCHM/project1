package gbicc.irs.fbinterface.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.fbinterface.jpa.entity.FbEventPersonDetailEntity;
/**
 * 事件检索人名实体统计相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月26日
 */
public interface FbEventPersonDetailRepository extends DaoRepository<FbEventPersonDetailEntity, String>{

    /**
     * 根据companyId删除数据
     *
     * @param companyId
     */
    void deleteByCompanyId(String companyId);

}
