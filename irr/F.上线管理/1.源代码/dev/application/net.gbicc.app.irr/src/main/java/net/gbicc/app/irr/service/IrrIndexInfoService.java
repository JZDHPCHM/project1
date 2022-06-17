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
	 * 计算具有公式的指标结果
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
	 * @param wb
	 * @param templateId 模板ID
	 * @param projTypeId 评估项目ID
	 * @param taskBatch  期次
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> importFileInfoBytempId(HSSFWorkbook wb ,String templateId ,String projTypeId,
			String taskBatch) throws Exception;

	/**
	 * 查询指标信息
	 * @param indexStatus 指标状态
	 * @param indexLevel  指标层级
	 * @param isApply	     是否适用
	 * @param formulaIsNull	 查询条件公式是否为空
	 * @param isScore	     是否得分指标
	 * @param isXbrl	     是否上报
	 * @param isChannel   是否拆分渠道
	 * @return
	 * @throws Exception
	 */
	public List<IrrIndexInfoEntity> findIndexInfoByCondtion(String indexStatus,String indexLevel,String isApply,
			Boolean formulaIsNull,String isScore,String isXbrl,Boolean isChannel) throws Exception;

	/**
	 * 查询指标
	 * @param projId 评估项目Id
	 * @param indexStatus 指标状态
	 * @param isApply 是否适用
	 * @param isScore 是否是得分指标
	 * @return
	 * @throws Exception
	 */
	public List<IrrIndexInfoEntity> findIndexInfoByCondition(String projId,String indexStatus,String isApply,String isScore) throws Exception;

    /**
     * 查询有因子的指标信息
     *
     * @param projId
     *            评估项目Id
     * @param indexStatus
     *            指标状态
     * @param isApply
     *            是否适用
     * @param isScore
     *            是否是得分指标
     * @return
     * @throws Exception
     */
    public List<IrrIndexInfoEntity> findIndexInfoHaveFactor(String projId, String indexStatus, String isApply,
            String isScore) throws Exception;

}
