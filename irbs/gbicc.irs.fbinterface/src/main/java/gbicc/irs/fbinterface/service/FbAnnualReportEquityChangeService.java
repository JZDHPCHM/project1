package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportEquityChangeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportEquityChangeRepository;

/**
 * 年报股权变更相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportEquityChangeService extends DaoService<FbAnnualReportEquityChangeEntity, String, FbAnnualReportEquityChangeRepository>{

}
