package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrSplitIndexEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.entity.IrrUploadResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrUploadResultRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.dto.IrrSysIndexValueDTO;

/**
* 上传结果
*/
public interface IrrUploadResultService extends DaoService<IrrUploadResultEntity, String, IrrUploadResultRepository> {

	/**
	 * 下载IRR采集模板
	 */
    public Workbook createIrrCollTemplate(String processTaskId) throws Exception;
	
	/**
     * 读取上传文件
     * 
     * @param wb
     * @param taskId
     *            任务ID
     * @param processTaskId
     *            流程任务ID
     */
    public void readCollUploadFile(Workbook wb, String taskId, String processTaskId) throws Exception;
	
	/**
     * 根据期次获取上传数据
     * 
     * @param taskBatch
     *            期次
     * @return
     * @throws Exception
     */
    public List<IrrUploadResultEntity> findUploadResult(String taskBatch) throws Exception;

	/**
	 * 上传结果数据放进map(key：编码,value:对象)
	 * @param list 数据
	 * @return
	 */
	public Map<String, IrrUploadResultEntity> getUploadMap(List<IrrUploadResultEntity> list);
	
	/**
	 * 根据ID编辑上传指标结果
	 * @param id
	 * @param splitResultQ2 当期指标结果
	 * @param dataDesc 数据说明
	 * @return
	 */
	public IrrUploadResultEntity editUploadResult(String id,String splitResultQ2,String dataDesc) throws Exception;

	/**
     * 加载系统数据
     * 
     * @param taskId
     *            评价计划ID
     * @param processTaskId
     *            流程任务ID
     * @return
     */
    public Map<String, Object> loadSysIndexDataByTaskId(String taskId, String processTaskId) throws Exception;
	
	/**
	 * 加载系统数据
	 * @param list 需加载系统数据的结果实例
	 */
	public List<IrrUploadResultEntity> loadSysData(List<IrrUploadResultEntity> list) throws Exception;
	
	/**
	 * 数据进行验证是否需要填写说明
	 * @param compareParam 需要对比的值
	 * @param diffParam 参照值
	 * <br/>
	 * 规则：,如果波动超过30%(此参数在数据字典中配置)，则需在页面上必填原因
	 * @return
	 * @throws Exception 
	 */
	public Boolean dataValiIsFill(String compareParam,String diffParam) throws Exception;
	
	/**
	 * 根据评估期查找系统指标值
	 * @param evalDate 评估期
	 * @return
	 */
	public List<IrrSysIndexValueDTO> findIrrSysIndexValueByEvalDate(String evalDate);

	/**
     * 采集人提交流程任务
     * 
     * @param taskId
     *            流程任务ID
     * @param id
     *            计划ID
     * @return
     */
    public Map<String, Object> collSubmit(String taskId, String id) throws Exception;
	
	/**
	 * 通过条件查询采集的结果信息
	 * @param taskId 评估计划ID
	 * @param collOrgId 采集机构ID
	 * @param collUserLoginName 采集人账号
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUploadResultByCondition(String taskId,String collOrgId,String collUserLoginName) throws Exception;

	/**
	 * 数据验证计算
	 * @param valiValue   验证值
	 * @param beValiValue 被验证值
	 * <br/>
	 * 计算公式：IF(AND(A=0,B<>0),1,IF(AND(A=0,B=0),0,B/A-1))
	 * A:上一季度值	B:本季度值
	 * @return
	 */
	public Object calcDataVali(String valiValue,String beValiValue);

	/**
     * 查询采集数据
     * 
     * @param uploadResult
     *            采集结果
     * @param sourceProjId
     *            评估项目ID
     * @param size
     *            每页多少条记录
     * @param page
     *            第几页
     * @param processTaskId
     *            流程任务ID
     * @param processNode
     *            流程节点
     * @return
     */
    public Map<String, Object> findUploadResult(IrrUploadResultEntity uploadResult, String sourceProjId,
            String sourceOrgId, Integer size, Integer page, String processTaskId, String processNode)
            throws Exception;

	/**
     * 检查采集人是否上传数据
     * 
     * @param id
     *            评估计划ID
     * @param processTaskId
     *            流程任务ID
     * @return
     * @throws Exception
     */
    public Map<String, Object> checkIsData(String id, String processTaskId) throws Exception;
	
	/**
	 * 查询指标说明
	 * @param splitId 分配指标ID(为null不作为条件)
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> findIndexDesc(String splitId) throws Exception;
	
	/**
	 * 查询组织机构
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public Org findOrgByCondition(String userId) throws Exception;
	
	/**
     * 组装下载模板sheet
     * 
     * @param wb
     * @param sheet
     *            需组装的sheet
     * @param splitList
     *            采集指标
     * @param splitSourceIndexInfo
     *            拆分指标源指标信息
     * @param trueUserId
     *            真实用户ID
     * @throws Exception
     */
    public void packDownloadSheet(Workbook wb, Sheet sheet, List<IrrSplitIndexEntity> splitList,
            Map<String, Map<String, Object>> splitSourceIndexInfo, String trueUserId) throws Exception;

	/**
     * 组装上传sheet数据
     * 
     * @param task
     *            评估计划
     * @param splitMap
     *            拆分指标Map
     * @param sheet
     *            数据sheet
     * @param org
     *            数据所属组织机构
     * @param assignee
     *            流程任务处理人
     * @return
     * @throws Exception
     */
	public List<IrrUploadResultEntity> packUploadResultData(IrrTaskEntity task,Map<String,IrrSplitIndexEntity> splitMap,
            Sheet sheet, Org org, String assignee) throws Exception;
	
	/**
	 * 获取指标采集结果
	 * @param taskId 评估计划ID
	 * @param orgId  组织机构ID(为空的时候是总公司)
	 * @param indexInfoList 需获取的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> getIndexResult(String taskId,String orgId,List<IrrIndexInfoEntity> indexInfoList) throws Exception;
	
	/**
     * 查询当前人的指标
     * 
     * @param id
     *            评估计划ID
     * @param splitCode
     *            拆分指标编码
     * @param splitName
     *            拆分指标名称
     * @param collOrgName
     *            采集机构编码
     * @param collUserName
     *            采集人登录账号
     * @param sourceProjId
     *            评估项目ID
     * @return
     */
    public Map<String, Object> searchIndexByPresentLogin(String id, String splitCode, String splitName,
            String collOrgName, String collUserName, String isCommit, String sourceProjId, Integer size, Integer page)
            throws Exception;
	
	/**
	 * 查询所有机构
	 * @return
	 */
	public Map<String,String> findAllOrgs();
	/**
	 * 获取指定指标指定分公司的各渠道的指标值
	 */
	public void getBranchIndexValue(String taskBatch)throws Exception;

    /**
     * 查询采集指标-分页
     * 
     * @param taskId
     *            评估计划ID
     * @param splitCode
     *            拆分指标编码
     * @param splitName
     *            拆分指标名称
     * @param collectionWay
     *            采集方式
     * @param sourceProjId
     *            评估项目
     * @param sourceOrgId
     *            采集机构
     * @param collName
     *            采集人名称
     * @param size
     *            每页多少
     * @param page
     *            当前页
     * @return
     */
    public Map<String, Object> searchCollIndex(String taskId, String splitCode, String splitName, String collectionWay,
            String sourceProjId, String sourceOrgId, String collName, Integer size, Integer page) throws Exception;

    /**
     * 查询采集指标
     * 
     * @param taskId
     *            评估计划ID
     * @param splitCode
     *            拆分指标编码
     * @param splitName
     *            拆分指标名称
     * @param collectionWay
     *            采集方式
     * @param sourceProjId
     *            评估项目
     * @param sourceOrgId
     *            采集机构
     * @param collName
     *            采集人姓名
     * @return
     */
    public List<Map<String, Object>> searchCollIndex(String taskId, String splitCode, String splitName,
            String collectionWay, String sourceProjId, String sourceOrgId, String collName) throws Exception;

    /**
     * 查询用户角色信息
     *
     * @param userId
     *            用户ID
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findUserRole(String userId) throws Exception;

    /**
     * 导出采集人结果
     * 
     * @param param
     *            查询参数
     * @return
     */
    public Workbook exportCollResult(String param) throws Exception;

    /**
     * 格式化采集的数值指标结果
     * 
     * @param formatList
     *            需要格式化的集合
     */
    public List<IrrUploadResultEntity> formatCollNumberIndexResult(List<IrrUploadResultEntity> formatList)
            throws Exception;
}
