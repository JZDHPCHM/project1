package gbicc.irs.fbinterface.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportGuaranteeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportGuaranteeRepository;

/**
 * 年报对外提供保证担保相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
public interface FbAnnualReportGuaranteeService extends DaoService<FbAnnualReportGuaranteeEntity, String, FbAnnualReportGuaranteeRepository>{

}
