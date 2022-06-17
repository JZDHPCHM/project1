package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrUploadResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrUploadResultRepository;
import net.gbicc.app.irr.jpa.support.dto.IrrSysIndexValueDTO;

/**
* 上传结果
*/
public interface IrrUploadResultService extends DaoService<IrrUploadResultEntity, String, IrrUploadResultRepository> {

	/**
	 * 下载IRR采集模板
	 */
	public Workbook createIrrCollTemplate() throws Exception;
	
	/**
	 * 读取上传文件
	 * @param wb
	 * @param taskId 任务ID
	 */
	public void readCollUploadFile(Workbook wb,String taskId) throws Exception;
	
	/**
	 * 根据期次和采集机构获取上传数据
	 * @param taskBatch 期次
	 * @param collOrgId 采集机构
	 * @return
	 * @throws Exception
	 */
	public List<IrrUploadResultEntity> findUploadResult(String taskBatch,String collOrgId) throws Exception;

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
	 * 根据评估计划加载系统数据
	 * @param taskId 评价计划ID
	 * @return
	 */
	public Map<String, Object> loadSysIndexDataByTaskId(String taskId) throws Exception;
	
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
	 * @param taskId 流程任务ID
	 * @return
	 */
	public Map<String, Object> collSubmit(String taskId) throws Exception;
	
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
	 * @param uploadResult 采集结果
	 * @param sourceProjId 评估项目ID
	 * @param examLoginName 审核人账号
	 * @param size 每页多少条记录
	 * @param page 第几页
	 * @return
	 */
	public Map<String, Object> findUploadResult(IrrUploadResultEntity uploadResult, String sourceProjId,String examLoginName,Integer size,Integer page) throws Exception;

	/**
	 * 检查采集人是否上传数据
	 * @param id 评估计划ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> checkIsData(String id) throws Exception;
}
