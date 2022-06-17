package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportWebsiteEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportWebsiteRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportWebsiteService;

/**
 * 年报网站信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportWebsiteServiceImpl extends DaoServiceImpl<FbAnnualReportWebsiteEntity, String, FbAnnualReportWebsiteRepository> implements FbAnnualReportWebsiteService{

}
