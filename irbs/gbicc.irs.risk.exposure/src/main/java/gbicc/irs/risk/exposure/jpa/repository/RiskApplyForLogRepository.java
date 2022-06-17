package gbicc.irs.risk.exposure.jpa.repository;

import org.springframework.data.repository.query.Param;
import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.risk.exposure.entity.RiskApplyForLogEntity;

public interface RiskApplyForLogRepository extends DaoRepository<RiskApplyForLogEntity, String> {
	/*@Modifying
	@Query(value = "delete from irs_risk_apply_for_log where RISK_EXPOSURE_ID =:piskExporeId and PROCESSOR!=:processor")
	*/public void deleteByRiskExposureIdAndProcessorNot(@Param("piskExporeId")String piskExporeId,@Param("processor")String processor) ;
	  
}
