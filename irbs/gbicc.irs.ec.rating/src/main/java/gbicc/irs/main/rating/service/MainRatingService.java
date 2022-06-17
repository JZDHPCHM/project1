package gbicc.irs.main.rating.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wsp.engine.model.core.po.Model;
import org.wsp.framework.jpa.service.DaoService;
import org.wsp.framework.mvc.protocol.response.ResponseWrapper;

import gbicc.irs.customer.entity.NsBpMaster;
import gbicc.irs.main.rating.entity.MainRating;
import gbicc.irs.main.rating.repository.MainRatingRepository;
import gbicc.irs.main.rating.vo.RatingInit;

public interface MainRatingService extends DaoService<MainRating, String, MainRatingRepository> {

	ResponseWrapper<RatingInit> parameterQuery(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception;

	Model getModel(String modelCode) throws Exception;

	MainRating startTrial(String type, String mainId, Map<String, String> map, String status, String version)
			throws Exception;
	Map<String, String> subjects(MainRating rating, String status,NsBpMaster master);

	/**
	 * 查询历史评级
	 *
	 * @param custCode
	 * @return
	 */
    ResponseWrapper<RatingInit> parameterQueryHistory(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception;
    public ResponseWrapper<RatingInit> parameterQueryHistory2(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception;
	boolean check(Map<String, String> quantitative, Map<String, String> qualitative);

	/**
	 * 判断台账查询是否有数据
	 *
	 * @param custName
	 * @param fdType
	 * @param industry
	 * @param highprecision
	 * @param employee
	 * @param finalName
	 * @param leaseOrganization
	 * @param method
	 * @param date1
	 * @param date2
	 * @return
	 */

	Map<String, Object> queryCount(String custName, String fdType, String industry, String highprecision,
			String employee, String finalName, String leaseOrganization, String method, String date1, String date2,
			String vailds);
    /**
     * 导出台账查询数据
     *
     * @param custName
     * @param fdType
     * @param industry
     * @param highprecision
     * @param employee
     * @param finalName
     * @param leaseOrganization
     * @param method
     * @param date1
     * @param date2
     * @param fileName 
     * @return
     */

	/*Map<String, Object> exportData(String custName, String fdType, String industry, String highprecision,
			String employee, String finalName, String leaseOrganization, String method, String date1, String date2,
			String fileName, String vailds);*/

	Map<String, Object> exportData(String loginName, String custName, String fdType, String industry,
			String highprecision, String employee, String finalName, String leaseOrganization, String method,
			String date1, String date2, String fileName, String vailds);

	public void testmodel( Map<String, String> paramValue,String type);

	ResponseWrapper<RatingInit> parameterQueryNew(HttpServletRequest request, HttpServletResponse response,
			MainRating queryExampleEntity, Integer page, Integer rows) throws Exception;



}
