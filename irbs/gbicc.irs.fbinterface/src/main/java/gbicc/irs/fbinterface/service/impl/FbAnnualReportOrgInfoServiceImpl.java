package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportOrgInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportOrgInfoRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportOrgInfoService;
/**
 * 年报分支机构登记信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportOrgInfoServiceImpl extends DaoServiceImpl<FbAnnualReportOrgInfoEntity, String, FbAnnualReportOrgInfoRepository> implements FbAnnualReportOrgInfoService{

}
