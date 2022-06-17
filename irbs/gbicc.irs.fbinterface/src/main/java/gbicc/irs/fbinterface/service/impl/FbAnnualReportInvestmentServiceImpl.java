package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportInvestmentEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportInvestmentRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportInvestmentService;
/**
 * 年报对外投资信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportInvestmentServiceImpl extends DaoServiceImpl<FbAnnualReportInvestmentEntity, String, FbAnnualReportInvestmentRepository> implements FbAnnualReportInvestmentService{

}
