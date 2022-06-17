package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportShareholderEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportShareholderRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportShareholderService;

/**
 * 年报股东及出资信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportShareholderServiceImpl extends DaoServiceImpl<FbAnnualReportShareholderEntity, String, FbAnnualReportShareholderRepository> implements FbAnnualReportShareholderService{

}
