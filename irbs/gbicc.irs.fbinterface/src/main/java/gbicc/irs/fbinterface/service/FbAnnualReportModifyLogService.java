package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportModifyLogEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportModifyLogRepository;
/**
 * 年报修改记录相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportModifyLogService extends DaoService<FbAnnualReportModifyLogEntity, String, FbAnnualReportModifyLogRepository>{

}
