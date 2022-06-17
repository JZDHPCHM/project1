package gbicc.irs.debtRating.debt.service;

import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.main.rating.entity.RatingSelectValuesConfig;
import gbicc.irs.main.rating.repository.RatingSelectValuesConfigRepository;


public interface RatingSelectValuesConfigService extends DaoService<RatingSelectValuesConfig, String, RatingSelectValuesConfigRepository> {
	
	List<RatingSelectValuesConfig> getSelectValuesByDefId(String defId) throws Exception;
	
	RatingSelectValuesConfig getValueByDefIdAndValueCode(String defId,String valueCode) throws Exception;
	
}
