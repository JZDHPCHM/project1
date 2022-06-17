package gbicc.irs.assetsRating.service;



import java.util.List;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.assetsRating.entity.AssetsIndex;
import gbicc.irs.assetsRating.repository.AssetsIndexRepository;

public interface AssetsIndexService extends DaoService<AssetsIndex, String, AssetsIndexRepository> {

//	ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
//			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception;

	
	public List<AssetsIndex> getRatingIndexsByStepId(String stepId) throws Exception;
	

}
