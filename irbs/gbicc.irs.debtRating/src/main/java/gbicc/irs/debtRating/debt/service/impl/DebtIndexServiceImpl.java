package gbicc.irs.debtRating.debt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import gbicc.irs.debtRating.debt.entity.RatingDebtIndex;
import gbicc.irs.debtRating.debt.repository.DebtIndexRepository;
import gbicc.irs.debtRating.debt.service.DebtIndexService;
import gbicc.irs.main.rating.entity.RatingSelectValuesConfig;
import gbicc.irs.main.rating.service.RatingSelectValuesConfigService;
import gbicc.irs.main.rating.support.RatingStepType;


@Service
public class DebtIndexServiceImpl extends DaoServiceImpl<RatingDebtIndex, String, DebtIndexRepository> implements
	DebtIndexService {

	@Autowired
	RatingSelectValuesConfigService ratingSelectValuesConfigService;
	
	@Override
	public List<RatingDebtIndex> getRatingIndexsByStepId(String stepId) throws Exception {
		List<RatingDebtIndex> indexs =  repository.findByRatingStep_Id(stepId);
		return indexs;
	}

	@Override
	public List<RatingDebtIndex> getRatingIndexsByIndexType(RatingStepType stepType) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<RatingDebtIndex> indexs =  repository.findByIndexType(stepType);
		List<RatingDebtIndex> resultIndexs =  new ArrayList<RatingDebtIndex>();;
		for(RatingDebtIndex ri : indexs) {
			Map<String,String> optionsMap = new HashMap<String,String>();
			List<RatingSelectValuesConfig> listOptions = ratingSelectValuesConfigService.getSelectValuesByDefId(ri.getIndexConfigId());
			for(RatingSelectValuesConfig va : listOptions) {
				optionsMap.put(va.getRealValue(), va.getDisplayValue());
			}
			ri.setOptions(mapper.writeValueAsString(optionsMap));
			resultIndexs.add(ri);
		}
		return resultIndexs;
	}

	@Override
	public Map<String, String> getRatingIndexsValueByStepId(String stepId) throws Exception {
		Map<String,String> indexValueMap = new HashMap<String,String>();
		List<RatingDebtIndex> indexs =  repository.findByRatingStep_Id(stepId);
		for(RatingDebtIndex ri : indexs) {
			indexValueMap.put(ri.getIndexCode(), ri.getIndexValue());
		}
		return indexValueMap;
	}


}
