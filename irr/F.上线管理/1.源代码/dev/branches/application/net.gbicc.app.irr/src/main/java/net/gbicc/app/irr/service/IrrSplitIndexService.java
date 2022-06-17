package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.repository.IrrSplitIndexRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.sf.json.JSONObject;

/**
* 拆分指标信息
*/
public interface IrrSplitIndexService extends DaoService<IrrSplitIndexEntity, String, IrrSplitIndexRepository> {

	/**
	 * 根据用户角色查询用户的MapJson格式
	 * @param userRole 用户角色
	 * @param orgId 机构ID
	 * @param roleAble 角色是否可用
	 * @return
	 */
	public JSONObject getUserMapJsonString(String userRole,String orgId,String roleAble) throws Exception;
	
	/**
	 * 根据采集人的ID查询启用的拆分指标
	 * @param collUserId 采集人Id
	 * @return
	 */
	public List<IrrSplitIndexEntity> findSplitIndexByCollUser(String collUserId) throws Exception;
	
	/**
	 * 根据采集人的ID和是否可用查询拆分指标
	 * @param collUserId 采集人Id
	 * @param isUse 是否可用
	 * @return
	 */
	public List<IrrSplitIndexEntity> findSplitIndexByCollUserAndUse(String collUserId,String isUse) throws Exception;
	
	/**
	 * 获取拆分指标的Map格式（key：编码，value：对象）
	 * @param list 拆分指标集合
	 * @return
	 */
	public Map<String, IrrSplitIndexEntity> getSplitIndexMap(List<IrrSplitIndexEntity> list);
	
	/**
	 * 根据是否启用和采集方式查询采集人(去重)
	 * @param isUse 是否启用
	 * @param collWay 采集方式
	 * @return
	 */
	public List<Map<String, Object>> findSplitCollUser(String isUse,String collWay);
	
	/**
	 * 根据采集人ID和是否启用查询审核人（去重）
	 * @param collUserLoginName 采集人账号
	 * @param isUse 是否启用
	 * @return
	 */
	public List<String> findSplitExamUserByCollUserLoginName(String collUserLoginName,String isUse); 
	
	/**
	 * 根据采集人ID和是否启用查询复核人（去重）
	 * @param collUserLoginName 采集人账号
	 * @param isUse 是否启用
	 * @return
	 */
	public List<String> findSplitReviewUserByCollUserLoginName(String collUserLoginName,String isUse);
	
	/**
	 * 根据采集人账号查找子流程编码（去重）
	 * @param collUserLoginName 采集人账号
	 * @param isUse 是否启用
	 * @return
	 */
	public List<String> findSubProcessByCollUserLoginName(String collUserLoginName,String isUse);

	/**
	 * 汇总拆分指标（全部）
	 * @param baseIndex 已有结果的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summSplitIndex(List<IndexValueDTO> baseIndex) throws Exception;

	/**
	 * 汇总拆分指标（数值型）
	 * @param calcIndex 需要计算的指标
	 * @param baseIndex 已有结果的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summSplitIndexNum(List<IrrSplitIndexEntity> calcIndex,List<IndexValueDTO> baseIndex) throws Exception;

	/**
	 * 汇总拆分指标（选项型）
	 * @param calcIndex 需要计算的指标
	 * @param baseIndex 已有结果的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summSplitIndexOption(List<IrrSplitIndexEntity> calcIndex,List<IndexValueDTO> baseIndex) throws Exception;

	/**
	 * 汇总拆分指标（无：也就是简答）
	 * @param calcIndex 需要计算的指标
	 * @param baseIndex 已有结果的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summSplitIndexNone(List<IrrSplitIndexEntity> calcIndex,List<IndexValueDTO> baseIndex) throws Exception;
	
	
	/**
	 * 汇总拆分指标（不适用型）
	 * @param calcIndex 需要计算的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summSplitIndexDisable(List<IrrSplitIndexEntity> calcIndex) throws Exception;
	
	/**
	 * 根据条件查询拆分指标（传值为null会过滤条件）
	 * @param isUse 	     是否启用
	 * @param indexStatus 源指标状态
	 * @param resultType  指标结果类型
	 * @param isScore     是否得分指标
	 * @param isApplicabie 是否适用
	 * @return
	 */
	public List<IrrSplitIndexEntity> findSplitByCondition(String isUse,String indexStatus,String resultType,String isScore,String isApplicabie);

	/**
	 * 根据条件查询审核人(传null不作为条件)
	 * @param isUse 	      拆分指标是否可用
	 * @param indexStatus 源指标状态
	 * @param indexLevel  源指标层级
	 * @param orgId		     拆分指标机构或部门
	 * @return
	 */
	public List<String> findExamUserByCondition(String isUse,String indexStatus,String indexLevel,String orgId);
	
	/**
	 * 根据条件查询拆分指标(传null不作为条件)
	 * @param isUse	拆分指标是否可用
	 * @param collWay	拆分指标采集方式
	 * @param indexStatus	源指标装填
	 * @param indexLevel	源指标层级
	 * @param orgId			拆分指标所属机构
	 * @return
	 */
	public List<IrrSplitIndexEntity> findSplitIndexByCondition(String isUse,String collWay,String indexStatus,String indexLevel,String orgId);

	/**
	 * 根据用户ID和采集方式查询拆分指标
	 * @param userId 用户ID
	 * @param collWay 拆分指标的采集方式
	 * @return
	 */
	public List<IrrSplitIndexEntity> findSplitIndexByUserIdAndCollWay(String userId,String collWay) throws Exception;
	
	/**
	 * 根据条件查询拆分指标审核人为null的
	 * @param isUse 是否启用
	 * @return
	 * @throws Exception
	 */
	public List<IrrSplitIndexEntity> findSplitExamIsNullByCondition(String isUse) throws Exception;

	/**
	 * 启用/禁用拆分指标
	 * @param ids 拆分指标id（中间用，隔开）
	 * @return
	 */
	public Map<String, Object> updateSplitIndexUse(String ids) throws Exception;
}
