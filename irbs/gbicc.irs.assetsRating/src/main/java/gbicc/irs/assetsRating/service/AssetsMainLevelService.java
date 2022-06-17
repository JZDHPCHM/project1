package gbicc.irs.assetsRating.service;

import java.util.List;
import java.util.Map;


public interface AssetsMainLevelService{

	
	
	////////////////////////// 资产信用评级报告 ///////////////////////////////
	/**
	 * 资产信用评级报告描述综述
	 * @param id 公司 ID
	 * @return
	 */
	Map<String, Object> getAssetsRatingInfo(String id);

	/**
	 * 资产信用评级报告(跟公司所有存量资产对比)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getGroup(String id);

	/**
	 * 资产信用评级报告(跟同产品的对比)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getProduct(String id);

	/**
	 * 资产信用评级报告(跟所在部门的对比)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getAssetsDepartment(String id);

	/**
	 * 资产信用评级报告(柱状图创收性)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getBarIncome(String id);

	/**
	 * 资产信用评级报告(柱状图保值性)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getBarPreservation(String id);

	/**
	 * 资产信用评级报告(柱状图流通性)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getBarCirculation(String id);

	/**
	 * 资产信用评级报告(柱状图可控性)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getBarSteerable(String id);

	/**
	 * 资产信用评级报告(柱状图可控性重大风险提示)
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getFooter(String id);

	/**
	 *  资产评级报告仪表盘
	 * @param id 资产评级表中的 FD_ID
	 * @return
	 */
	Map<String, Object> getAssetsLevelSurface(String id);

	/**
	 *  资产评级报告得分分位数
	 * @param id 资产评级表中的 FD_ID
	 * @return
	 */
	Map<String, Object> getAssetsPosition(String id);

	/**
	 * 资产评级报告综述(页面头信息 XX公司XX项目, 项目编号)
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	Map<String, Object> getAssetsHeader(String id);

	/**
	 * 主体信用评级报告综述(提示标签)
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	List<Map<String, Object>> getAssetsTrait(String id);

	/**
	 * 资产信用评级报告描述综述(获取资产中评得分)
	 * @param id
	 * @return
	 */
	Map<String, Object> getAssetsScore(String id);

	
	
}
