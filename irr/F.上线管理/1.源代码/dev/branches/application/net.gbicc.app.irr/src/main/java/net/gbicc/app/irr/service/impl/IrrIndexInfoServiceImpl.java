package net.gbicc.app.irr.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;

import net.gbicc.app.irr.jpa.entity.IrrCasemanaResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrCorpgoveResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexOptionEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexRelationEntity;
import net.gbicc.app.irr.jpa.entity.IrrInfosysResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrLiquriskResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrStrariskResultEntity;
import net.gbicc.app.irr.jpa.repository.IrrIndexInfoRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.util.JavaJsEvalUtil;
import net.gbicc.app.irr.service.IrrCasemanaResultService;
import net.gbicc.app.irr.service.IrrCorpgoveResultService;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrIndexOptionService;
import net.gbicc.app.irr.service.IrrIndexRelationService;
import net.gbicc.app.irr.service.IrrInfosysResultService;
import net.gbicc.app.irr.service.IrrLiquriskResultService;
import net.gbicc.app.irr.service.IrrStrariskResultService;

/**
* 指标信息实现类
*/
@Service
public class IrrIndexInfoServiceImpl extends DaoServiceImpl<IrrIndexInfoEntity, String, IrrIndexInfoRepository> 
	implements IrrIndexInfoService {
	
	private static final Logger LOG = LogManager.getLogger(IrrIndexInfoServiceImpl.class);
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired 
	private IrrIndexRelationService irrIndexRelationService;
	@Autowired 
	private IrrIndexOptionService irrIndexOptionService;
	@Autowired
	private IrrInfosysResultService irrInfosysResultService;
	@Autowired
	private IrrCasemanaResultService irrCasemanaResultService;
	@Autowired
	private IrrLiquriskResultService irrLiquriskResultService;
	@Autowired
	private IrrCorpgoveResultService irrCorpgoveResultService;
	@Autowired
	private IrrStrariskResultService irrStrariskResultService;
	
	private ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
	private DecimalFormat decimalFormat = new DecimalFormat("#.#");
	private Calendar now = Calendar.getInstance();
	
	
	
	@Override
	public List<Map<String,Object>> findOrgByParentId(String parentId) {
		String sql = null;
		if(StringUtils.isBlank(parentId)) {
			sql = "SELECT * FROM FR_AA_ORG WHERE FD_PARENT_ID IS NULL";
		}else {
			sql = "SELECT * FROM FR_AA_ORG WHERE FD_PARENT_ID='"+parentId+"'";
		}
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> findIndexInfoBySplitId(String splitId) {
		String sql = "SELECT DISTINCT INDE.INDEX_CODE,INDE.INDEX_LEVEL,SPLI.SPLIT_LEVEL,SPLI.ORG_CODE,SPLI.CHANNEL_FLAG FROM T_IRR_INDEX_INFO INDE INNER JOIN T_IRR_SPLIT_INDEX SPLI "
				+ "ON INDE.ID=SPLI.SOURCE_INDEX_ID WHERE SPLI.ID='"+splitId+"'";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<IrrIndexInfoEntity> findFormulaIndexByCondition(String indexStatus, String indexLevel,
			String isApplication) throws Exception {
		String sql = "SELECT * FROM T_IRR_INDEX_INFO II WHERE II.INDEX_EVAL_FORM IS NOT NULL AND II.INDEX_LEVEL='"+indexLevel+"' AND II.INDEX_STATUS='"+indexStatus+"' AND II.IS_APPLICABIE='"+isApplication+"'";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrIndexInfoEntity>(IrrIndexInfoEntity.class));
	}
	
	@Override
	public List<IndexValueDTO> summIndexNum(List<IrrIndexInfoEntity> calcIndex, List<IndexValueDTO> baseIndex)
			throws Exception {
		if(CollectionUtils.isEmpty(calcIndex) || CollectionUtils.isEmpty(baseIndex)) {
			LOG.error("无法汇总数值指标：传入的参数为空！");
			return null;
		}
		Map<String,String> baseMap = new HashMap<String,String>();
		for(IndexValueDTO dto : baseIndex) {
			baseMap.put(dto.getCode(), dto.getValue());
		}
		List<IndexValueDTO> returnList = new ArrayList<IndexValueDTO>();
		for(IrrIndexInfoEntity index : calcIndex) {//计算指标
			if(index == null) {
				continue;
			}
			IndexValueDTO value = new IndexValueDTO();
			value.setIndexId(index.getId());
			value.setCode(index.getIndexCode());
			value.setName(index.getIndexName());
			String indexEvalForm = index.getIndexEvalForm();
			if(StringUtils.isBlank(indexEvalForm)) {
				returnList.add(value);
				continue;
			}
			IrrIndexRelationEntity exampleRela = new IrrIndexRelationEntity();
			exampleRela.setIndexId(index.getId());
			List<IrrIndexRelationEntity> relaList = irrIndexRelationService.fetch(exampleRela, IrrQueryParameter.getIrrDefaultQP());
			if(CollectionUtils.isEmpty(relaList)) {
				returnList.add(value);
				continue;
			}
			for(IrrIndexRelationEntity rela : relaList) {
				if(rela == null) {
					continue;
				}
				String relaCode = rela.getRelaCode();
				if(StringUtils.isBlank(relaCode)) {
					continue;
				}
				String relaValue = baseMap.get(relaCode);
				if(StringUtils.isNotBlank(relaValue)) {
					indexEvalForm = indexEvalForm.replace(relaCode, relaValue);
				}else {
					indexEvalForm = indexEvalForm.replace(relaCode, "null");
				}
			}
			Object evalValue = JavaJsEvalUtil.evalStr(indexEvalForm);
			if(evalValue != null && !"null".equals(evalValue)) {
				value.setValue(evalValue.toString());
			}
			returnList.add(value);
		}
		return returnList;
	}

	@Override
	public List<IndexValueDTO> summIndexOption(List<IrrIndexInfoEntity> calcIndex, List<IndexValueDTO> baseIndex)
			throws Exception {
		if(CollectionUtils.isEmpty(calcIndex) || CollectionUtils.isEmpty(baseIndex)) {
			LOG.error("无法汇总选项指标：传入的参数为空！");
			return null;
		}
		List<IrrIndexOptionEntity> options = irrIndexOptionService.findAllMaxPoints();
		if(CollectionUtils.isEmpty(options)) {
			LOG.error("无法汇总选项指标：无选项信息！");
			return null;
		}
		Map<String, String> baseMap = new HashMap<String,String>();
		for(IrrIndexOptionEntity option : options) {
			baseMap.put(option.getIndexCode(), option.getIndexName());
		}
		List<IndexValueDTO> returnList = new ArrayList<IndexValueDTO>();
		for(IrrIndexInfoEntity index : calcIndex) {
			if(index == null) {
				continue;
			}
			IndexValueDTO value = new IndexValueDTO();
			value.setIndexId(index.getId());
			value.setCode(index.getIndexCode());
			value.setName(index.getIndexName());
			value.setValue(baseMap.get(index.getIndexCode()));
			returnList.add(value);
		}
		return returnList;
	}

	@Override
	public List<IndexValueDTO> summIndexNone(List<IrrIndexInfoEntity> calcIndex, List<IndexValueDTO> baseIndex)
			throws Exception {
		if(CollectionUtils.isEmpty(calcIndex) || CollectionUtils.isEmpty(baseIndex)) {
			LOG.error("无法汇总简答指标：传入的参数为空!");
			return null;
		}
		Map<String, String> baseMap = new HashMap<String, String>();
		for(IndexValueDTO dto : baseIndex) {
			baseMap.put(dto.getCode(), dto.getValue());
		}
		List<IndexValueDTO> returnList = new ArrayList<IndexValueDTO>();
		for(IrrIndexInfoEntity index : calcIndex) {
			IndexValueDTO value = new IndexValueDTO();
			value.setIndexId(index.getId());
			value.setCode(index.getIndexCode());
			value.setName(index.getIndexName());
			value.setValue(baseMap.get(index.getIndexCode()));
			returnList.add(value);
		}
		return returnList;
	}

	@Override
	public List<IndexValueDTO> summIndexDisable(List<IrrIndexInfoEntity> calcIndex)
			throws Exception {
		if(CollectionUtils.isEmpty(calcIndex)) {
			LOG.error("无法汇总不适用指标：传入的参数为空!");
			return null;
		}
		List<IndexValueDTO> returnList = new ArrayList<IndexValueDTO>();
		for(IrrIndexInfoEntity index : calcIndex) {
			IndexValueDTO indexValue = new IndexValueDTO();
			indexValue.setIndexId(index.getId());
			indexValue.setCode(index.getIndexCode());
			indexValue.setName(index.getIndexName());
			indexValue.setValue(IrrEnum.INDEX_NOT_APPLICABIE_VALUE.getValue());
			returnList.add(indexValue);
		}
		return returnList;
	}

	@Override
	public List<IndexValueDTO> sumHeadChannelIndex(String taskId) throws Exception {
		String sql = "SELECT II.ID INDEX_ID,II.INDEX_CODE CODE,II.INDEX_NAME NAME,SUM(NVL(UR.SPLIT_RESULT_Q2,0)) VALUE\r\n" + 
				"FROM T_IRR_UPLOAD_RESULT UR INNER JOIN FR_AA_ORG ORG ON UR.COLL_ORG_ID=ORG.FD_ID INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n" + 
				"WHERE UR.TASK_ID='"+taskId+"' AND SI.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' "+
				"AND II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' "+
				"AND ORG.FD_CODE IN ('"+IrrEnum.ORG_HEAD_AD_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_BK_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_FC_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_GP_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_RP_DEPT_CODE.getValue()+"') GROUP BY II.ID,II.INDEX_CODE,II.INDEX_NAME";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IndexValueDTO>(IndexValueDTO.class));
	}
	
	@Override
	public Map<String, Object> importFileInfoBytempId(HSSFWorkbook wb ,String templateId ,String projTypeId) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			if (templateId == "OR11" || templateId.equals("OR11")) {
				map = importCorpgoveResult(wb ,projTypeId);
			} else if (templateId == "OR16" || templateId.equals("OR16")) {
				map = importInfosysResult(wb ,projTypeId);
			} else if (templateId == "OR17" || templateId.equals("OR17")) {
				map = importCasemanaResult(wb ,projTypeId);
			} else if (templateId == "LR01" || templateId.equals("LR01")) {
				map = importLiquriskResult(wb ,projTypeId);
			} else if (templateId == "SR01" || templateId.equals("SR01")) {
				map = importStrariskResult(wb ,projTypeId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("mes", "导入失败");
			map.put("flag", false);
			return map;
		}
		return map;
		
		
	}
    
	/**
	 * 添加公司治理结果
	 * @param wb HSSFWorkbook
	 * @return
	 */
	private Map<String, Object> importCorpgoveResult(HSSFWorkbook wb ,String projTypeId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 获取总行数
				int totalRows = hssfSheet.getLastRowNum();
				//获取当前期次
				String taskBatch = now.get(Calendar.YEAR) + "" ;
				
				//根据当前期次删除历史数据
				String sql = "DELETE FROM T_IRR_CORPGOVE_RESULT WHERE TASK_BATCH = ?";
				jdbcTemplate.update(sql, taskBatch);
				
				// 读取Row,从第三行开始
				for (int rowNum = 2; rowNum <= totalRows; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					// 创建公司治理结果对象
					IrrCorpgoveResultEntity corpgove = new IrrCorpgoveResultEntity();
					if (hssfRow != null) {
						int cellNum = 0;

						// 初始化对象数据
						cellNum++;
						corpgove.setIndexName(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						corpgove.setIndexId(projTypeId);
						corpgove.setIndexCode(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum = cellNum + 2;
						corpgove.setTaskBatch(taskBatch);
						cellNum++;
						corpgove.setSelfEval(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						corpgove.setIndexScore(StringgetCellString(hssfRow.getCell(cellNum)).trim());

						irrCorpgoveResultService.add(corpgove);
					}
				}
			}
			
			map.put("mes", "导入成功");
			map.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("mes", "公司治理结果导入失败");
			map.put("flag", false);
			return map;
		}
		return map;
	}
	
	/**
	 * 添加信息系统结果
	 * @param wb HSSFWorkbook
	 * @return
	 */
	private Map<String, Object> importInfosysResult(HSSFWorkbook wb ,String projTypeId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			 int a = wb.getNumberOfSheets();
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < a; numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 获取总行数
				int totalRows = hssfSheet.getLastRowNum();
				//获取当前期次
				double num = ((Integer.parseInt((now.get(Calendar.MONTH) + 1) + "") / 3) + 1);
				String taskBatch = now.get(Calendar.YEAR) + "Q" + decimalFormat.format(num);
				
				//根据当前期次删除历史数据
				String sql = "DELETE FROM T_IRR_INFOSYS_RESULT WHERE TASK_BATCH = ?";
				jdbcTemplate.update(sql, taskBatch);
				
				// 读取Row,从第三行开始
				for (int rowNum = 2; rowNum <= totalRows; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					// 创建信息系统结果对象
					IrrInfosysResultEntity infosys = new IrrInfosysResultEntity();
					
					if (hssfRow != null) {
						int cellNum = 0;

						infosys.setIndexId(projTypeId);
						// 初始化对象数据
						cellNum++;
						infosys.setIndexName(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						infosys.setIndexCode(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum = cellNum + 3;
						infosys.setTaskBatch(taskBatch);
						cellNum++;
						infosys.setIndexScore(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						infosys.setIndexCentScore(jse.eval(
								StringgetCellString(hssfRow.getCell(cellNum + 1)).trim() + "*" + infosys.getIndexScore()
										+ "/" + StringgetCellString(hssfRow.getCell(cellNum - 3)).trim())
								.toString());
						
						irrInfosysResultService.add(infosys);
					}
				}
			}
			
			map.put("mes", "导入成功");
			map.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("mes", "信息系统结果导入失败");
			map.put("flag", false);
			return map;
		}
		return map;
	}
	
	/**
	 * 添加案件管理结果
	 * @param wb HSSFWorkbook
	 * @return
	 */
	private Map<String, Object> importCasemanaResult(HSSFWorkbook wb ,String projTypeId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 获取总行数
				int totalRows = hssfSheet.getLastRowNum();
				//获取当前期次
				double num = ((Integer.parseInt((now.get(Calendar.MONTH) + 1) + "") / 3) + 1);
				String taskBatch = now.get(Calendar.YEAR) + "Q" + decimalFormat.format(num);
				
				//根据当前期次删除历史数据
				String sql = "DELETE FROM T_IRR_CASEMANA_RESULT WHERE TASK_BATCH = ?";
				jdbcTemplate.update(sql, taskBatch);
				
				// 读取Row,从第三行开始
				for (int rowNum = 2; rowNum <= totalRows; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					// 创建案件管理结果对象
					IrrCasemanaResultEntity casemana = new IrrCasemanaResultEntity();
					if (hssfRow != null) {
						int cellNum = 0;

						// 初始化对象数据
						cellNum++;
						casemana.setIndexName(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						casemana.setIndexId(projTypeId);
						casemana.setIndexCode(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum = cellNum + 3;
						casemana.setTaskBatch(taskBatch);
						cellNum++;
						casemana.setIndexScore(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						
						irrCasemanaResultService.add(casemana);
					}
				}
			}

			map.put("mes", "导入成功");
			map.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("mes", "案件管理结果导入失败");
			map.put("flag", false);
			return map;
		}
		return map;
	}
	
	/**
	 * 添加流动性风险结果
	 * @param wb HSSFWorkbook
	 * @return
	 */
	private Map<String, Object> importLiquriskResult(HSSFWorkbook wb ,String projTypeId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 获取总行数
				int totalRows = hssfSheet.getLastRowNum();
				//获取当前期次
				double num = ((Integer.parseInt((now.get(Calendar.MONTH) + 1) + "") / 3) + 1);
				String taskBatch = now.get(Calendar.YEAR) + "Q" + decimalFormat.format(num);
				
				//根据当前期次删除历史数据
				String sql = "DELETE FROM T_IRR_LIQURISK_RESULT WHERE TASK_BATCH = ?";
				jdbcTemplate.update(sql, taskBatch);
				
				// 读取Row,从第三行开始
				for (int rowNum = 2; rowNum <= totalRows; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					// 创建流动性风险结果对象
					IrrLiquriskResultEntity liqurisk = new IrrLiquriskResultEntity();
					if (hssfRow != null) {
						int cellNum = 0;

						// 初始化对象数据
						liqurisk.setIndexName(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						liqurisk.setIndexId(projTypeId);
						liqurisk.setIndexCode(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum = cellNum + 2;
						liqurisk.setTaskBatch(taskBatch);
						cellNum++;
						liqurisk.setIndexResult(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						liqurisk.setIndexScore(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						
						liqurisk = irrLiquriskResultService.add(liqurisk);
					}
				}
			}

			map.put("mes", "导入成功");
			map.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("mes", "流动性风险结果导入失败");
			map.put("flag", false);
			return map;
		}
		return map;
	}
	
	/**
	 * 添加战略风险结果
	 * @param wb HSSFWorkbook
	 * @return
	 */
	private Map<String, Object> importStrariskResult(HSSFWorkbook wb ,String projTypeId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				// 获取总行数
				int totalRows = hssfSheet.getLastRowNum();
				//获取当前期次
				double num = ((Integer.parseInt((now.get(Calendar.MONTH) + 1) + "") / 3) + 1);
				String taskBatch = now.get(Calendar.YEAR) + "Q" + decimalFormat.format(num);
				
				//根据当前期次删除历史数据
				String sql = "DELETE FROM T_IRR_STRARISK_RESULT WHERE TASK_BATCH = ?";
				jdbcTemplate.update(sql, taskBatch);
				
				// 读取Row,从第三行开始
				for (int rowNum = 2; rowNum <= totalRows; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					// 创建战略风险结果对象
					IrrStrariskResultEntity strarisk = new IrrStrariskResultEntity();
					if (hssfRow != null) {
						int cellNum = 0;

						// 初始化对象数据
						cellNum++;
						strarisk.setIndexName(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum++;
						strarisk.setIndexId(projTypeId);
						strarisk.setIndexCode(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						cellNum = cellNum + 3;
						strarisk.setTaskBatch(taskBatch);
						cellNum++;
						strarisk.setIndexScore(StringgetCellString(hssfRow.getCell(cellNum)).trim());
						
						strarisk = irrStrariskResultService.add(strarisk);
					}
				}
			}

			map.put("mes", "导入成功");
			map.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			map.put("mes", "战略风险结果导入失败");
			map.put("flag", false);
			return map;
		}
		return map;
	}
	
	/**
	 * 把Excel表中 每个列的原有数据类型都转换成String类型
	 * @param cell 单元格内容
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String StringgetCellString(Cell cell) {
		if (cell == null)
			return "";
		String cellSring = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING: // 字符串
			cellSring = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字
			cellSring = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
			cellSring = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 公式
			cellSring = String.valueOf(cell.getCellFormula());
			break;
		case HSSFCell.CELL_TYPE_BLANK: // 空值
			cellSring = "";
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 故障
			cellSring = "";
			break;
		default:
			cellSring = "";
			break;
		}
		return cellSring;
	}

}
