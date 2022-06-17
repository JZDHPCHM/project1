package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportOperateScopeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportOperateScopeRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportOperateScopeService;

/**
 * 年报经营范围相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportOperateScopeServiceImpl extends DaoServiceImpl<FbAnnualReportOperateScopeEntity, String, FbAnnualReportOperateScopeRepository> implements FbAnnualReportOperateScopeService{

}
