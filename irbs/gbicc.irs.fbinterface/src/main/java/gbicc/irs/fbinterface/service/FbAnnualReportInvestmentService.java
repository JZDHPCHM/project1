package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportInvestmentEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportInvestmentRepository;

/**
 * 年报对外投资信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportInvestmentService extends DaoService<FbAnnualReportInvestmentEntity, String, FbAnnualReportInvestmentRepository>{

}
