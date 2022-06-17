package gbicc.irs.risk.exposure.service;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.risk.exposure.entity.RiskTypeCtmEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskTypeCtmRepository;


public interface RiskTypeCtmService extends
DaoService<RiskTypeCtmEntity, String,RiskTypeCtmRepository> {


}
