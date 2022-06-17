package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportAssetsEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportAssetsRepository;

/**
 * 年报资产状况信息相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportAssetsService extends DaoService<FbAnnualReportAssetsEntity, String, FbAnnualReportAssetsRepository>{

}
