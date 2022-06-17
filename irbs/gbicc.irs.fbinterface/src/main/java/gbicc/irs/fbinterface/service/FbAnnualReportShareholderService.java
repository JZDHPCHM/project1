package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportShareholderEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportShareholderRepository;
/**
 * 年报股东及出资信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportShareholderService extends DaoService<FbAnnualReportShareholderEntity, String, FbAnnualReportShareholderRepository>{

}
