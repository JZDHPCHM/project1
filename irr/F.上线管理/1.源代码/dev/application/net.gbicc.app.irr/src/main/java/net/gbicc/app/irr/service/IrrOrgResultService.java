package net.gbicc.app.irr.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.DaoService;

import net.gbicc.app.irr.jpa.entity.IrrIndexResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.repository.IrrOrgResultRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexResultExportDTO;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;

/**
* 机构结果
*/
public interface IrrOrgResultService extends DaoService<IrrOrgResultEntity, String, IrrOrgResultRepository> {

	/**
	 * 汇总总公司部门结果(不包含总公司渠道部门)
	 * @param id 评估计划ID
	 * @return
	 */
	public List<IrrOrgResultEntity> summHeadDeptResult(String id) throws Exception;

	/**
	 * 汇总本身的机构结果
	 * @param id 评估计划ID
	 * @param indexLevel 指标层级
	 * @return
	 */
	public List<IrrOrgResultEntity> summMyselfResult(String id,String indexLevel) throws Exception;
	
	/**
	 * 机构结果数据验证
	 * @param task 评估计划
	 * @return
	 * @throws Exception
	 */
	public List<IrrOrgResultEntity> summCalcDataVali(IrrTaskEntity task) throws Exception;

	/**
	 * 根据条件查询机构结果
	 * @param orgResult 机构结果实体
	 * @param size      一页大小
	 * @param page		第几页
	 * @param projTypeId 评估项目Id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findOrgResult(IrrOrgResultEntity orgResult, Integer size, Integer page,
			String projTypeId) throws Exception;

	/**
	 * 总公司分配到渠道所属部门为其他的采集指标相加
	 * @param taskId 评估计划ID
	 * @return
	 * @throws Exception
	 */
	public List<IndexValueDTO> sumHeadChannelDeptResult(String taskId) throws Exception;

	/**
	 * 导出汇总指标结果
	 * @param task 评估任务
	 * @return
	 * @throws Exception
	 */
	public Workbook summaryExportIndexResult(IrrTaskEntity task) throws Exception;
	/**
	 * 导出汇总单一指标结果
	 * @param orgId 机构ID
	 * @param projTypeCode 评估项目编码
	 * @param task 评估任务
	 * @return
	 */
	public Workbook summaryExportIndexResultSingle(String orgId,String projTypeCode,IrrTaskEntity task);
	/**
	 * 导出汇总全部的指标结果
	 * @param task 评估任务
	 * @param templatePath 模板路径
	 * @return
	 */
	public Workbook summaryExportIndexResultAll(IrrTaskEntity task,String templatePath);
	
	/**
     * 根据条件查询指标结果（参数为null不作为条件）
     * 
     * @param orgId
     *            机构ID
     * @param orgCode
     *            机构编码
     * @param projTypeCode
     *            评估项目编码
     * @param taskId
     *            评估计划ID
     * @param isXbrl
     *            是否上报
     * @return
     * @throws Exception
     */
    public List<IndexResultExportDTO> findExportIndexResultByCondition(String orgId, String orgCode,
            String projTypeCode, String taskId, Boolean isXbrl) throws Exception;

	/**
	 * 设置汇总单元格数据
	 * @param sheet 当前sheet
	 * @param tempRow 行索引
	 * @param colNum 列索引
	 * @param title 标题数组
	 * @param borderStyle 单元格边框
	 * @param fontStyle   单元格字体
	 * @param backColorStyle 单元格背景色
	 * @param resultList 数据源
	 * @throws Exception
	 */
	public void setSheetData(Sheet sheet,Integer tempRow,Integer colNum,String[] title,
			CellStyle borderStyle,CellStyle fontStyle,CellStyle backColorStyle,
			List<IndexResultExportDTO> resultList) throws Exception;
	
	/**
	 * 导出上报的excel（全部）
	 * @param task 评估计划
	 * @return
	 * @throws Exception
	 */
	public Map<String, Workbook> createReportAllExcel(IrrTaskEntity task) throws Exception;

	/**
	 * 编辑指标结果
	 * @param id 指标结果ID
	 * @param indexResultQ2 当期指标结果
	 * @param dataDesc 数据说明
	 * @return
	 */
	public Map<String, Object> editOrgResult(String id, String indexResultQ2,String dataDesc) throws Exception;

	/**
	 * 计算机构指标得分
	 * @param orgId 机构ID
	 * @param task  评估计划
	 * @return
	 * @throws Exception
	 */
	public List<IrrOrgResultEntity> calcOrgIndexScore(String orgId,IrrTaskEntity task) throws Exception;
	
	/**
	 * 根据条件查询机构结果
	 * @param indexStatus 指标状态
	 * @param isScore	     是否得分指标
	 * @param isApplication 是否适用
	 * @param resultType    结果类型
	 * @param scoreType     得分类型
	 * @param pfmEvalFormulaIsNull 绩效指标可计算公式是否为空
	 * @param orgId       机构ID
	 * @param taskId      评估任务ID
	 * @return
	 * @throws Exception
	 */
	public List<IrrOrgResultEntity> findOrgResultByCondition(String indexStatus,String isScore,String isApplication,
			String resultType,String scoreType,String pfmEvalFormulaIsNull,String orgId,String taskId) throws Exception;
	
	/**
	 * 计算分公司源指标
	 * @param task 评估计划
	 * @param projType 评估项目
	 * @param perivousIndexResult 上期指标结果
	 * @param branchNum 上报分公司总数
	 * @return
	 * @throws Exception
	 */
	public List<IrrIndexResultEntity> calcBranchIndexSource(IrrTaskEntity task,IrrProjTypeEntity projType,
			Map<String,IrrIndexResultEntity> perivousIndexResult,Integer branchNum) throws Exception;
	/**
	 * 计算总公司源指标
	 * @param task 评估计划
	 * @return
	 * @throws Exception
	 */
	public List<IrrIndexResultEntity> calcHeadIndexSource(IrrTaskEntity task) throws Exception;
	
	/**
	 * 根据条件相加机构结果
	 * @param isApplication 是否适用
	 * @param projTypeId  	评估项目ID
	 * @param resultType	结果类型
	 * @param taskId 		评估计划ID
	 * @param isScore 		是否计算得分
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findSumOrgResultByCondition(String isApplication,String projTypeId,
			String resultType,String taskId,Boolean isScore) throws Exception;
	
	/**
	 * 查询结果信息
	 * @param isApplication 是否适用
	 * @param projTypeId	评估项目ID
	 * @param resultType	结果类型
	 * @param taskId		评价计划ID
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOrgResultInfoByCondition(String isApplication,String projTypeId,
			String resultType,String taskId) throws Exception;
	
	/**
	 * 根据条件查询指标信息
	 * @param isApplication 是否适用
	 * @param projTypeId  	评估项目ID
	 * @param resultType	结果类型
	 * @param taskId 		评估计划ID
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOrgIndexInfoByCondition(String taskId,String isApplication,
			String projTypeId,String resultType) throws Exception;
	
	/**
	 * 多维度总分合计
	 * @param task 评估计划
	 * @param proj 评估项目
	 * @param org 机构
	 * @return
	 * @throws Exception
	 */
	public Double sumOrgIndexTotalScoreResult(IrrTaskEntity task,IrrProjTypeEntity proj,Org org) throws Exception;
	
	/**
	 * 多维度行业水平确定可得计算<br/>
	 * IF(行业水平指标得分=满分,满分,0)
	 * @param task 评估计划
	 * @param proj 评估项目
	 * @param org 机构
	 * @return
	 * @throws Exception
	 */
	public Double calcOrgScoreInduLevelResult(IrrTaskEntity task,IrrProjTypeEntity proj,Org org) throws Exception;
	
	/**
	 * 合计行业得分
	 * @param task 评估计划
	 * @param proj 评估项目
	 * @param org  机构
	 * @return
	 * @throws Exception
	 */
	public Double sumInduOrgScore(IrrTaskEntity task,IrrProjTypeEntity proj,Org org) throws Exception;
	
	/**
	 * 合计行业标准分
	 * @param proj 评估项目
	 * @return
	 * @throws Exception
	 */
	public Double sumInduIndexStanValue(IrrProjTypeEntity proj) throws Exception;
	
	/**
	 * 合计评估项目得分指标标准分
	 * @param proj 评估项目
	 * @return
	 * @throws Exception
	 */
	public Double sumProjScoreIndexStanValue(IrrProjTypeEntity proj) throws Exception;
}