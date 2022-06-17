package gbicc.irs.risk.exposure.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.risk.exposure.entity.RiskTypeEntity;
import gbicc.irs.risk.exposure.jpa.repository.RiskTypeRepository;
import gbicc.irs.risk.exposure.service.RiskTypeService;
@Service("RiskTypeService")
public class RiskTypeServiceImpl  extends DaoServiceImpl<RiskTypeEntity, String,RiskTypeRepository> implements RiskTypeService {

	@Override
	public Map<String, String> findByIsStart(String isStart) {
		List<RiskTypeEntity> list=repository.findByIsStartOrderByTypeAsc(isStart);
		Map<String, String> map=new LinkedHashMap<String, String>(list.size());
		for (RiskTypeEntity riskTypeEntity : list) {
			map.put(riskTypeEntity.getCode(), riskTypeEntity.getType()+"-"+riskTypeEntity.getDetailType());
		}
		return map;
	}

	
	

	
}
