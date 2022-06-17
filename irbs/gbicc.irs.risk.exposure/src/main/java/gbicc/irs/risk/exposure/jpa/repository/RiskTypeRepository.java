package gbicc.irs.risk.exposure.jpa.repository;

import java.util.List;

import org.wsp.framework.jpa.repository.DaoRepository;

import gbicc.irs.risk.exposure.entity.RiskTypeEntity;

public interface RiskTypeRepository extends DaoRepository<RiskTypeEntity, String> {

	public List<RiskTypeEntity> findByIsStartOrderByTypeAsc(String isStart);
}
