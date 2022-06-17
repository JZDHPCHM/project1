package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportSocialInsurEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportSocialInsurRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportSocialInsurService;

/**
 * 年报社保信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportSocialInsurServiceImpl extends DaoServiceImpl<FbAnnualReportSocialInsurEntity, String, FbAnnualReportSocialInsurRepository> implements FbAnnualReportSocialInsurService{

}
