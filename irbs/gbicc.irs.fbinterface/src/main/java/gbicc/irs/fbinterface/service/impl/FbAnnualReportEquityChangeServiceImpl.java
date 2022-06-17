package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportEquityChangeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportEquityChangeRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportEquityChangeService;

/**
 * 年报股权变更相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportEquityChangeServiceImpl extends DaoServiceImpl<FbAnnualReportEquityChangeEntity, String, FbAnnualReportEquityChangeRepository> implements FbAnnualReportEquityChangeService{

}
