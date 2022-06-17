package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportBaseInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportBaseInfoRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportBaseInfoService;
/**
 * 年报基本信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportBaseInfoServiceImpl extends DaoServiceImpl<FbAnnualReportBaseInfoEntity, String, FbAnnualReportBaseInfoRepository> implements FbAnnualReportBaseInfoService{

}
