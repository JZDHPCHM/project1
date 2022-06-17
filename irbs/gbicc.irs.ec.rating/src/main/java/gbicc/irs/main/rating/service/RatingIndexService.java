package gbicc.irs.main.rating.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import gbicc.irs.main.rating.entity.RatingIndex;
import gbicc.irs.main.rating.repository.RatingIndexRepository;
import gbicc.irs.main.rating.support.RatingStepType;

public interface RatingIndexService extends DaoService<RatingIndex, String, RatingIndexRepository> {

	/**
	 * 根据步骤ID查询指标
	 * 
	 * @param stepId
	 * @return
	 * @throws Exception
	 */
	List<RatingIndex> getRatingIndexsByStepId(String stepId) throws Exception;

	/**
	 * 根据步骤ID查询指标和指标值
	 * 
	 * @param stepId
	 * @return
	 * @throws Exception
	 */
	Map<String, String> getRatingIndexsValueByStepId(String stepId) throws Exception;

	/**
	 * 根据指标类型查询指标
	 * 
	 * @param stepType
	 * @return
	 * @throws Exception
	 */
	List<RatingIndex> getRatingIndexsByIndexType(RatingStepType stepType) throws Exception;

	/**
	 * 主体信用评级报告仪表盘, 资产信用评级报告仪表盘
	 * 
	 * @param id 公司 ID
	 * @return
	 */
	Map<String, Object> getNsMainLevelList(String id);

	/**
	 * 主体信用评级报告综述
	 * 
	 * @param id 公司 ID
	 * @return
	 */
	Map<String, Object> getNsMainLevelInfo(String id);

	/**
	 * 主体信用评级报告综述(仪表盘下边那一块)存量维度图
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getIndicator(String id);

	/**
	 * 主体信用评级报告综述(仪表盘下边那一块)维度图部门级
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getDepartment(String id);

	/**
	 * 主体信用评级报告综述(得分分位数)
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	Map<String, Object> getTrack(String id);

	/**
	 * 主体信用评级报告综述(经营质量)
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getManage(String id);

	/**
	 * 主体信用评级报告综述(管理能力)
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getAbility(String id);

	/**
	 * 主体信用评级报告综述(产业环境)
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getEnvironment(String id);

	/**
	 * 主体信用评级报告综述(产品品质)
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getQuality(String id);

	/**
	 * 主体信用评级报告综述(市场地位)
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getPosition(String id);

	/**
	 * 主体信用评级报告综述(商业模式)
	 * 
	 * @param id 评级步骤表中的评级 ID
	 * @return
	 */
	List<Map<String, Object>> getModel(String id);

	/**
	 * 主体信用评级报告综述(重大潜在风险提示)
	 * 
	 * @param id 客户表中的客户编码
	 * @return
	 */
	List<Map<String, Object>> getTip(String id);

	/**
	 * 主体信用评级报告综述(仪表盘下边那一块)维度图同赛道(下拉框那个选项)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	List<Map<String, Object>> getGrade(String id);

	/**
	 * 主体信用评级报告综述(页面头信息 XX公司, 客户编号)
	 * 
	 * @param id 债项评级表中的 FD_ID
	 * @return
	 */
	Map<String, Object> getMainHeader(String id);

	/**
	 * 获取主体的特点(页面综述上面一条)
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getMainTrait(String id);
	
	/**
	 * 主体信用评级报告综述(主体得分)
	 * @param id
	 * @return
	 */
	Map<String, Object> getMainScore(String id);


}
