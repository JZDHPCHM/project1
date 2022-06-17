package gbicc.irs.fbinterface.service.impl;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.fbinterface.jpa.entity.FbAnnualReportGuaranteeEntity;
import gbicc.irs.fbinterface.jpa.repository.FbAnnualReportGuaranteeRepository;
import gbicc.irs.fbinterface.service.FbAnnualReportGuaranteeService;

/**
 * 年报对外提供保证担保相关操作
 * 
 * @author songxubei
 * @version v1.0 2019年9月24日
 */
@Service
public class FbAnnualReportGuaranteeServiceImpl extends DaoServiceImpl<FbAnnualReportGuaranteeEntity, String, FbAnnualReportGuaranteeRepository> implements FbAnnualReportGuaranteeService{

}
