package gbicc.irs.debtRating.debt.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wsp.engine.model.core.po.Model;
import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import gbicc.irs.debtRating.debt.entity.DebtRating;
import gbicc.irs.debtRating.debt.repository.DebtRatingRepository;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.vo.RatingInit;


public interface DebtRatingService extends DaoService<DebtRating, String, DebtRatingRepository> {


	Model getModel(String modelCode) throws Exception;

	DebtRating startTrial(String type, DebtRating debt, Map<String, String> map, String status) throws Exception;

	Map<String, String> subjects(DebtRating rating, String status);

	/**
	 * 查询历史评级
	 *
	 * @param custCode
	 * @return
	 */
	boolean check(Map<String, String> quantitative, Map<String, String> qualitative);

	ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception;

	ResponseWrapper<RatingInit> parameterQuery2(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception;

	Map<String, Object> queryCount(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, String fileName);

	Map<String, String> ratingResults(String modelCode, Map<String, String> paramValue);

	Map<String, Object> exportData(String loginName, HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, String fileName);
	/*Map<String, Object> exportData(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, String fileName);*/

	ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			DebtRating queryExampleEntity, Integer page, Integer rows) throws Exception;

}
