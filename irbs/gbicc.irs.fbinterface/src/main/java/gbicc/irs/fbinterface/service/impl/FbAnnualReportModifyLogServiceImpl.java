package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportModifyLogEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportModifyLogRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportModifyLogService;
/**
 * 年报修改记录相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportModifyLogServiceImpl extends DaoServiceImpl<FbAnnualReportModifyLogEntity, String, FbAnnualReportModifyLogRepository> implements FbAnnualReportModifyLogService{

}
