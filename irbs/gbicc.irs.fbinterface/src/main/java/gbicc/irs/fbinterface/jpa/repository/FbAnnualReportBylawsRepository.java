package gbicc.irs.fbinterface.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportBylawsEntity;

/**
 * 年报章程信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportBylawsRepository extends DaoRepository<FbAnnualReportBylawsEntity, String>{

    /**
     * 通过companyId删除数据
     *
     * @param companyId
     */
    void deleteByCompanyId(String companyId);

}
