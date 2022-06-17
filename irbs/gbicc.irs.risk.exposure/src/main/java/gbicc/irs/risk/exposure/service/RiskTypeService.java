package gbicc.irs.risk.exposure.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.risk.exposure.entity.RiskTypeEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskTypeRepository;


public interface RiskTypeService extends DaoService<RiskTypeEntity, String,RiskTypeRepository> {
	/**
	 * 查询可用的风险类
	 * @param isStart
	 * @return
	 */
	Map<String, String> findByIsStart(String isStart);

}
