package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexInfoRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;

/**
* 指标信息
*/
public interface IrrIndexInfoService extends DaoService<IrrIndexInfoEntity, String, IrrIndexInfoRepository> {

	/**
	 * 根据父机构ID查询机构
	 * @param parentId 父机构ID
	 * @return
	 */
	public List<Map<String,Object>> findOrgByParentId(String parentId);
	
	/**
	 * 根据拆分指标ID查找指标
	 * @param splitId 拆分指标ID
	 * @return
	 */
	public List<Map<String, Object>> findIndexInfoBySplitId(String splitId);
	
	/**
	 * 根据条件查找源指标中有公式的指标
	 * @param indexStatus 指标状态
	 * @param indexLevel  指标层级
	 * @param isApplication 是否适用
	 * @return
	 * @throws Exception
	 */
	public List<IrrIndexInfoEntity> findFormulaIndexByCondition(String indexStatus,String indexLevel,String isApplication) throws Exception;
	
	/**
	 * 汇总指标（指标型）
	 * @param calcIndex 需要计算的指标
	 * @param baseIndex 已有结果的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summIndexNum(List<IrrIndexInfoEntity> calcIndex, List<IndexValueDTO> baseIndex) throws Exception;
	
	/**
	 * 汇总指标（选项型）
	 * @param calcIndex 需要计算的指标
	 * @param baseIndex 已有结果的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summIndexOption(List<IrrIndexInfoEntity> calcIndex, List<IndexValueDTO> baseIndex) throws Exception;

	/**
	 * 汇总指标（无：也就是简答）
	 * @param calcIndex 需要计算的指标
	 * @param baseIndex 已有结果的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summIndexNone(List<IrrIndexInfoEntity> calcIndex, List<IndexValueDTO> baseIndex) throws Exception;

	/**
	 * 汇总指标（不适用型）
	 * @param calcIndex 需要计算的指标
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> summIndexDisable(List<IrrIndexInfoEntity> calcIndex) throws Exception;
	
	/**
	 * 总公司渠道部门指标加和
	 * @param taskId 评估计划ID
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> sumHeadChannelIndex(String taskId) throws Exception;
	
	/**
	 * 根据模板ID导入文件数据
	 * @param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> importFileInfoBytempId(HSSFWorkbook wb ,String templateId ,String projTypeId) throws Exception;
}
