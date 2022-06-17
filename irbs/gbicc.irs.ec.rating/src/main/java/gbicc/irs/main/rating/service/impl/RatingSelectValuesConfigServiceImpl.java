package gbicc.irs.main.rating.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import gbicc.irs.main.rating.entity.RatingSelectValuesConfig;
import gbicc.irs.main.rating.repository.RatingSelectValuesConfigRepository;
import gbicc.irs.main.rating.service.RatingSelectValuesConfigService;


@Service
public class RatingSelectValuesConfigServiceImpl extends DaoServiceImpl<RatingSelectValuesConfig, String, RatingSelectValuesConfigRepository> implements RatingSelectValuesConfigService {

	@Override
	public List<RatingSelectValuesConfig> getSelectValuesByDefId(String defId) throws Exception {
		return repository.findByDefId(defId);
	}

	@Override
	public RatingSelectValuesConfig getValueByDefIdAndValueCode(String defId, String valueCode) throws Exception {
		return repository.findByDefIdAndRealValue(defId, valueCode);
	}
	
}
