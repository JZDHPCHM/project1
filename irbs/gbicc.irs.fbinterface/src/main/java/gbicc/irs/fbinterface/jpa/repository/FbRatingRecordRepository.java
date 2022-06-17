package gbicc.irs.fbinterface.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.fbinterface.jpa.entity.FbRatingRecordEntity;

/**
 * 评级记录相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月23日
 */
public interface FbRatingRecordRepository extends DaoRepository<FbRatingRecordEntity, String>{

    /**
     * 根据companyId删除数据
     *
     * @param companyId
     */
    void deleteByCompanyId(String companyId);

}
