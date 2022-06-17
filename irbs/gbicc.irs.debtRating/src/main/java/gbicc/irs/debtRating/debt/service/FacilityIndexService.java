package gbicc.irs.debtRating.debt.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.debtRating.debt.entity.FacilityIndex;
import gbicc.irs.debtRating.debt.repository.FacilityIndexRepository;

public interface FacilityIndexService extends DaoService<FacilityIndex, String, FacilityIndexRepository> {

	/**
	 * 债项安全评级报告综述(仪表盘右边那一块)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	List<Map<String, Object>> getAssetsDescribe(String id);

	/**
	 * 债项安全评级报告仪表盘
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	Map<String, Object> getAssetsLevel(String id);

	/**
	 * 债项安全评级报告(主体信用评级)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	List<Map<String, Object>> getFacilityMainInfo(String id);

	/**
	 * 债项安全评级报告(资产信用评级)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	List<Map<String, Object>> getFacilityAssetsInfo(String id);

	/**
	 * 债项安全评级报告(增信措施评价)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	List<Map<String, Object>> getFacilityCredit(String id);

	/**
	 * 债项安全评级报告(风险提示)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	List<Map<String, Object>> getFacilityWarning(String id);

	/**
	 * 债项评级报告综述(页面头信息 XX公司XX项目, 项目编号)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	Map<String, Object> getFacilityHeader(String id);

	/**
	 *  债项安全评级报告(资产和主体的大小)
	 * 
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getFacilityCompare(String id);

	/**
	 * 债项安全评级报告(提示标签)
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getFacilityTrait(String id);

	/**
	 * 跳转到资产评级
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getFacilityToAssets(String id);

	/**
	 * 跳转到主体评级
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getFacilityToMain(String id);

	/**
	 * 债项对应的资产得分
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getFacilityAssetsScore(String id);

	/**
	 * 债项对应的主体得分
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getFacilityMainScore(String id);
	

}
