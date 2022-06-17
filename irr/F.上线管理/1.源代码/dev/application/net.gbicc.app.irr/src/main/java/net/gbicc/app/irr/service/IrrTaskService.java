package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.repository.IrrTaskRepository;
import net.sf.json.JSONObject;

/**
* 任务
*/
public interface IrrTaskService extends DaoService<IrrTaskEntity, String, IrrTaskRepository> {

	/**
	 * 创建任务
	 * @param taskName 任务名称
	 * @param taskBatch 任务期次
	 * @param deadPlanDate 计划截止时间
	 * @return
	 */
	public Map<String, Object> startTask(String taskName,String taskBatch,String deadPlanDate) throws Exception;

	/**
	 * 根据评估计划ID删除评估计划数据
	 * @param taskId 评估计划ID
	 * @return
	 */
	public Map<String, Object> deleteTask(String taskId) throws Exception;

	/**
     * 审核人退回
     * 
     * @param taskId
     *            流程任务ID
     * @param collUser
     *            采集人账号
     * @param comment
     *            意见
     * @param id
     *            评价计划ID
     * @return
     */
    public Map<String, Object> examBack(String taskId, String collUser, String comment, String id) throws Exception;

	/**
	 * 审核人提交
	 * @param taskId 	流程任务ID
	 * @param collUser  采集人账号
	 * @param comment   意见
	 * @return
	 */
	public Map<String, Object> examSubmit(String taskId, String collUser, String comment,String id) throws Exception;

	/**
	 * 汇总节点退回
	 * @param taskId 	流程任务ID
	 * @param loginNames  	采集人账号集合(中间用,隔开)
	 * @param comment   意见
	 * @return
	 */
	public Map<String, Object> summBack(String taskId, String loginNames, String comment) throws Exception;

	/**
	 * 指标汇总
	 * @param id 评估计划ID
	 * @return
	 */
	public Map<String, Object> indexSumm(String id) throws Exception;
	
	/**
	 * 分公司指标汇总
	 * @param task 当期评估计划
	 * @return
	 * @throws Exception
	 */
	public List<IrrOrgResultEntity> branchIndexSumm(IrrTaskEntity task) throws Exception;
	
	/**
	 * 总公司指标汇总
	 * @param task 当期评估计划
	 * @return
	 * @throws Exception
	 */
	public List<IrrOrgResultEntity> headIndexSumm(IrrTaskEntity task) throws Exception;

	/**
	 * 评估计划汇总
	 * @param id 评估项目ID
	 * @param subProcess 子流程编码
	 * @return
	 */
	public Map<String, Object> projSumm(String id) throws Exception;

	/**
	 * 汇总全部
	 * @param id 评估计划ID
	 * @param subProcess 子流程编码
	 * @return
	 */
	public Map<String, Object> summAll(String id) throws Exception;
	
	/**
	 * 根据条件查询用户信息(传null不作为条件)
	 * @param roleCode 角色编码
	 * @param orgId	        机构ID
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findRoleUserAndOrgByCondition(String roleCode,String orgId) throws Exception;
	/**
	 * 根据机构ID组装子流程数据(采集人和审核人,除会签流程外)
	 * @param org 机构
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> packSubProcessData(Org org) throws Exception;
	
	/**
	 * 根据评估任务ID生成审批意见
	 * @param id 评估任务ID
	 * @return
	 * @throws Exception
	 */
	public 	Workbook createProcessCommentById(String id) throws Exception;
	
	/**
	 * 根据条件查询采集机构
	 * @param roleCode 角色编码
	 * @param enable   1：可用；0：不可用
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findCollOrgByCondition(String roleCode,String enable) throws Exception;
	
	/**
	 * 根据父级机构Id获取机构Map
	 * @param parentId 父级机构Id
	 * @return
	 */
	public Map<String, String> getOrgMap(String parentId) throws Exception;

    /**
     * 根据机构名称查询机构map（模糊查询）
     * 
     * @param orgName
     *            机构名称
     * @return
     */
    public Map<String, String> getOrgMapByName(String orgName) throws Exception;
	/**
	 * 更新任务状态
	 * @param taskId 评估计划ID
	 * @param taskStatus 任务状态
	 */
	public void updateTask(String taskId,String taskStatus);

	/**
	 * 查询汇总节点退回信息
	 * @param id 评估计划ID
	 * @return
	 */
	public Map<String, Object> findSummBackInfo(String id);
	
	/**
	 * 根据条件查询机构ID
	 * @param loginName 账号
	 * @return
	 */
	public List<String> findOrgIdByCondition(String loginName);
	
	/**
	 * 根据状态查询任务
	 * @param taskStatus 任务状态
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getTaskMap(String taskStatus) throws Exception;

	/**
	 * 查询审批记录
	 * @param id 评估计划ID
	 * @param size 每页大小
	 * @param page 第几页
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findProcessComment(String id, Integer size, Long page) throws Exception;

	/**
	 * 获取用户信息
	 * @param loginName 用户账号
	 * @return
	 * @throws Exception
	 */
	public JSONObject getUserInfo(String loginName) throws Exception;

	/**
	 * 计算指标公式
	 * @param formulaStr 公式字符串
	 * @param codeList   因子项编码
	 * @param valueMap   因子项值Map
	 * @return
	 * @throws Exception
	 */
	public Object calcFormula(String formulaStr,List<String> codeList,Map<String, String> valueMap) throws Exception;

	/**
	 * 计算源指标
	 * @param task 评估计划
	 * @return
	 */
	public List<IrrIndexResultEntity> calcIndexSource(IrrTaskEntity task) throws Exception;
	
	/**
	 * 查询任务年
	 * @return
	 */
	public Map<String, String> findTaskYear();
	
	/**
	 * 根据角色前缀查询人员
	 * @param rolePrefix 角色前缀
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findRoleUserByCondition(String rolePrefix) throws Exception;
	
	/**
	 * 组装会签子流程
	 * @param signCollRoleCode 会签子流程采集人编码
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> packSignProcess(String signCollRoleCode) throws Exception;
	
	/**
	 * 通过用户ID查询机构
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public Org findOrgByUserId(String userId) throws Exception;
	/**
	 * 通过用户ID查询机构集合
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public List<Org> findOrgListByUserId(String userId) throws Exception;
	/**
	 * 查询用户具有某种角色(模糊查询)
	 * @param roleCode 角色编码
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findRoleByCondition(String roleCode,String userId) throws Exception;

	/**
	 * 权重汇总
	 * @param taskId 评估计划ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> weightSumm(String taskId) throws Exception;

	/**
	 * 一票全过
	 * @param id 评价计划ID
	 * @param taskId 流程任务ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> aTicketCross(String id,String taskId) throws Exception;
	
	/**
	 * 查询XBRL请求需要的信息
	 * @param taskId 任务ID
	 * @param orgId  机构ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findXbrlRequestInfo(String taskId,String orgId) throws Exception;
	/**
	 * 只查询总公司和个分公司
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getOrgsMap(String prefix) throws Exception;

    /**
     * 结果保留小数
     * 
     * @param id
     *            任务期次ID
     */
    public void resultSetScale(String id) throws Exception;

    /**
     * 得分保留小数
     * 
     * @param id
     */
    public void scoreSetScale(String id) throws Exception;

    /**
     * 权重保留小数
     * 
     * @param id
     */
    public void weightSetScale(String id) throws Exception;

    /**
     * 是否为流程采集人
     *
     * @param loginName
     *            登录名
     * @return
     * @throws Exception
     */
    public Boolean isProcessCollUser(String loginName) throws Exception;
	
}
