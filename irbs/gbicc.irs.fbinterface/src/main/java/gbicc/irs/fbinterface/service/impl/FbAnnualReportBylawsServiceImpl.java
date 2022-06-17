package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportBylawsEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportBylawsRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportBylawsService;

/**
 * 年报章程信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportBylawsServiceImpl extends DaoServiceImpl<FbAnnualReportBylawsEntity, String, FbAnnualReportBylawsRepository> implements FbAnnualReportBylawsService{

}
