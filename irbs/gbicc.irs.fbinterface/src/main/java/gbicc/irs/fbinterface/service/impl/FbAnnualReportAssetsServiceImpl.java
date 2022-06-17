package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportAssetsEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportAssetsRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportAssetsService;

/**
 * 年报资产状况相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportAssetsServiceImpl extends DaoServiceImpl<FbAnnualReportAssetsEntity, String, FbAnnualReportAssetsRepository> implements FbAnnualReportAssetsService{

}
