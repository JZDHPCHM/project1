package gbicc.irs.debtRating.debt.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import gbicc.irs.debtRating.debt.entity.CreditRating;
import gbicc.irs.debtRating.debt.repository.CreditRatingRepository;
import gbicc.irs.main.rating.vo.RatingInit;


public interface CreditRatingService extends DaoService<CreditRating, String, CreditRatingRepository> {

	public Map<String,String> insertModel(Map<String, String> paramValue, String type,String status) ;
	
	public Map<String,String> insertFacilityModel(Map<String, String> paramValue, String type,String status);
	public Map<String,Object> testmodel( Map<String, Object> paramValue,String type,String status);

	ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			CreditRating queryExampleEntity, Integer page, Integer rows) throws Exception;
	
	public Map<String,Object> getCreditIndexInput();
	
	public String creatCreditRating(String bpCode,String proCode);
	
	public CreditRating startModel(Map<String, Object> paramValue, String type,String status,String mainId);
}
