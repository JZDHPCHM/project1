package gbicc.irs.debtRating.debt.service;

import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.debtRating.debt.entity.FacilityRating;
import gbicc.irs.debtRating.debt.repository.FacilityRatingRepository;


public interface FacilityRatingService extends DaoService<FacilityRating, String, FacilityRatingRepository> {

	
	public Map<String, String> testmodel(Map<String, String> paramValue, String type,String status);
	public FacilityRating startFacilityModel(Map<String, String> paramValue, String type,String status,String facilityId);
	public String creatFacilityRating(String bpCode,String proCode,String mainId,String assetsId,String creditId);
}
