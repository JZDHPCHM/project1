package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportBaseInfoEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportBaseInfoRepository;

/**
 * 年报基本信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportBaseInfoService extends DaoService<FbAnnualReportBaseInfoEntity, String, FbAnnualReportBaseInfoRepository>{

}
