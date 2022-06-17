package gbicc.irs.assetsRating.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wsp.engine.model.core.po.Model;
import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import gbicc.irs.assetsRating.entity.AssetsRating;
import gbicc.irs.assetsRating.repository.AssetsRatingRepository;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.vo.RatingInit;

public interface AssetsRatingService extends DaoService<AssetsRating, String, AssetsRatingRepository> {

	ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception;

	Model getModel(String modelCode) throws Exception;

	MainRating startTrial(String type, String mainId, Map<String, String> map, String status, String version)
			throws Exception;
	Map<String, String> subjects(MainRating rating, String status);

	AssetsRating testmodel(Map<String, String> paramValue,String type,AssetsRating mainG,String status);
	
	/**
	 * 生成一条资产评级记录
	 * @param bpCode
	 * @param tempType
	 * @param version
	 * @return
	 */
	public String createAssetsRating(String bpCode,String tempType,String version,String proCode,String ratingid) throws Exception;

	public ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			AssetsRating queryExampleEntity, Integer page, Integer rows) throws Exception;

//	public Map<String, String> parsingData(Map<String, Object> map);

	ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			AssetsRating queryExampleEntity, Integer page, Integer rows) throws Exception;
}
