package gbicc.irs.fbinterface.jpa.repository;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportInvestmentEntity;

/**
 * 年报对外投资信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportInvestmentRepository extends DaoRepository<FbAnnualReportInvestmentEntity, String>{

    /**
     * 通过companyId删除数据
     *
     * @param companyId
     */
    void deleteByCompanyId(String companyId);

}
