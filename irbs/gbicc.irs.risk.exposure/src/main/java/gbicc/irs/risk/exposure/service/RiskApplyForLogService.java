package gbicc.irs.risk.exposure.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.risk.exposure.entity.RiskApplyForLogEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskApplyForLogRepository;


public interface RiskApplyForLogService extends
DaoService<RiskApplyForLogEntity, String,RiskApplyForLogRepository> {

	public Page<RiskApplyForLogEntity> findByRiskExporeId(String riskExporeId,Pageable pageable);
	
	public void deleteByRiskExposureIdAndProcessorNot(String piskExporeId,String processor);
	
}
