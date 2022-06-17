package net.gbicc.app.irr.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.OrgService;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrOrgResultRepository;
import net.gbicc.app.irr.jpa.support.IrrQueryParameter;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.dto.IndexResultExportDTO;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProcessEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProjTypeEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrOrgResultService;
import net.gbicc.app.irr.service.IrrUploadResultService;

/**
* 机构结果
*/
@Service
public class IrrOrgResultServiceImpl extends DaoServiceImpl<IrrOrgResultEntity, String, IrrOrgResultRepository> 
	implements IrrOrgResultService {
	
	private static final Logger LOG = LogManager.getLogger(IrrOrgResultServiceImpl.class);
	@Autowired JdbcTemplate jdbcTemplate;
	@Autowired IrrUploadResultService irrUploadResultService;
	@Autowired OrgService orgService;
	@Autowired SystemDictionaryService systemDictionaryService;

	@Override
	public List<IrrOrgResultEntity> summHeadDeptResult(String id) throws Exception {
		String sql = "SELECT DISTINCT II.ID INDEX_ID,II.INDEX_CODE,II.INDEX_NAME,UR.TASK_BATCH,UR.TASK_ID,UR.DATA_VALI,UR.DATA_DESC,SI.ORG_ID,SI.ORG_CODE,SI.ORG_NAME,\r\n" + 
				"UR.SPLIT_RESULT_Q1 INDEX_RESULT_Q1,UR.SPLIT_RESULT_Q2 INDEX_RESULT_Q2,UR.SPLIT_SCORE_Q1 INDEX_SCORE_Q1,UR.SPLIT_SCORE_Q2 INDEX_SCORE_Q2 \r\n" + 
				"FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n" + 
				"INNER JOIN FR_AA_ORG ORG ON UR.COLL_ORG_ID=ORG.FD_ID "+
				"WHERE UR.TASK_ID='"+id+"' AND SI.SPLIT_LEVEL='"+IrrEnum.SPLIT_LEVEL_HEAD.getValue()+"' AND SI.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' "+
				"AND II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.INDEX_LEVEL='"+IrrEnum.INDEX_LEVEL_HEAD.getValue()+"' " + 
				"AND ORG.FD_CODE NOT IN ('"+IrrEnum.ORG_HEAD_AD_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_BK_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_FC_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_GP_DEPT_CODE.getValue()+"','"+IrrEnum.ORG_HEAD_RP_DEPT_CODE.getValue()+"')";
		List<IrrOrgResultEntity> orgResult = jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
		orgResult = add(orgResult);
		return orgResult;
	}

	@Override
	public List<IrrOrgResultEntity> summMyselfResult(String id,String indexLevel) throws Exception{
		String sql = "SELECT DISTINCT II.ID INDEX_ID,II.INDEX_CODE,II.INDEX_NAME,UR.TASK_BATCH,UR.TASK_ID,UR.DATA_VALI,UR.DATA_DESC,UR.COLL_ORG_ID ORG_ID,UR.COLL_ORG_NAME ORG_NAME,\r\n" + 
				"UR.SPLIT_RESULT_Q1 INDEX_RESULT_Q1,UR.SPLIT_RESULT_Q2 INDEX_RESULT_Q2,UR.SPLIT_SCORE_Q1 INDEX_SCORE_Q1,UR.SPLIT_SCORE_Q2 INDEX_SCORE_Q2 \r\n" + 
				"FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n" + 
				"WHERE UR.TASK_ID='"+id+"' AND SI.SPLIT_LEVEL='"+IrrEnum.SPLIT_LEVEL_MYSELF.getValue()+"' AND SI.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' "+
				"AND II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.INDEX_LEVEL='"+indexLevel+"'";
		List<IrrOrgResultEntity> orgResult = jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
		orgResult = add(orgResult);
		return orgResult;
	}

	@Override
	public List<IrrOrgResultEntity> summCalcDataVali(IrrTaskEntity task) throws Exception {
		if(task == null || StringUtils.isBlank(task.getTaskBatch())) {
			throw new IrrProcessException("机构结果无法验证：评估计划期次为空！");
		}
		IrrOrgResultEntity exam = new IrrOrgResultEntity();
		exam.setTaskBatch(task.getTaskBatch());
		List<IrrOrgResultEntity> theBatchResult = fetch(exam, IrrQueryParameter.getIrrDefaultQP());
		exam.setTaskBatch(IrrUtil.getPreviousPeriod(task.getTaskBatch()));
		List<IrrOrgResultEntity> previousBatchResult = fetch(exam, IrrQueryParameter.getIrrDefaultQP());
		Map<String, IrrOrgResultEntity> map = new HashMap<String, IrrOrgResultEntity>();
		for(IrrOrgResultEntity orgResult : previousBatchResult) {
			map.put(orgResult.getIndexCode()+orgResult.getOrgCode(), orgResult);
		}
		List<IrrOrgResultEntity> calcValiList = new ArrayList<IrrOrgResultEntity>();
		for(IrrOrgResultEntity orgResult : theBatchResult) {
			IrrOrgResultEntity previous = map.get(orgResult.getIndexCode()+orgResult.getOrgCode());
			if(previous == null) {
				continue;
			}
			Object calcVali = irrUploadResultService.calcDataVali(previous.getIndexResultQ2(), orgResult.getIndexResultQ2());
			if(!IrrUtil.strObjectIsEmpty(calcVali)) {
				orgResult.setDataVali(new BigDecimal(calcVali.toString()));
				calcValiList.add(update(orgResult.getId(), orgResult));
			}
		}
		return calcValiList;
	}

	@Override
	public Map<String, Object> findOrgResult(IrrOrgResultEntity orgResult, Integer size, Integer page,
			String projTypeId) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT TOR.* FROM T_IRR_ORG_RESULT TOR INNER JOIN T_IRR_INDEX_INFO II ON TOR.INDEX_ID=II.ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(projTypeId)) {
			sb.append("AND II.PROJ_TYPE_ID='"+projTypeId+"' ");
		}
		if(orgResult != null) {
			if(StringUtils.isNotBlank(orgResult.getTaskId())) {
				sb.append("AND TOR.TASK_ID='"+orgResult.getTaskId()+"' ");
			}
			if(StringUtils.isNotBlank(orgResult.getIndexName())) {
				sb.append("AND TOR.INDEX_NAME='"+orgResult.getIndexName()+"' ");
			}
			if(StringUtils.isNotBlank(orgResult.getIndexCode())) {
				sb.append("AND TOR.INDEX_CODE='"+orgResult.getIndexCode()+"' ");
			}
			if(StringUtils.isNotBlank(orgResult.getOrgId())) {
				sb.append("AND TOR.ORG_ID='"+orgResult.getOrgId()+"' ");
			}
		}
		sb.append("ORDER BY II.INDEX_LINE ASC");
		List<IrrOrgResultEntity> result = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
		if(CollectionUtils.isEmpty(result)) {
			return null;
		}
		if(size == null) {
			size = Integer.valueOf(IrrEnum.IRR_DEFAULT_SIZE.toString());
		}
		if(page == null) {
			page = Integer.valueOf(IrrEnum.IRR_DEFAULT_PAGE.toString());
		}
		ResponsePage<IrrOrgResultEntity> response = new ResponsePage<IrrOrgResultEntity>();
		response.setAllData(result);
		response.setNumber(Long.valueOf(page));
		response.setSize(size);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("response", response.getPageData());
		return map;
	}

	@Override
	public List<IndexValueDTO> sumHeadChannelDeptResult(String taskId) throws Exception {
		String sql = "SELECT II.ID INDEX_ID,II.INDEX_CODE CODE,II.INDEX_NAME NAME,SUM(NVL(UR.SPLIT_RESULT_Q2,0)) VALUE\r\n" + 
				"FROM T_IRR_UPLOAD_RESULT UR INNER JOIN FR_AA_ORG ORG ON UR.COLL_ORG_ID=ORG.FD_ID INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n" + 
				"WHERE UR.TASK_ID='"+taskId+"' AND SI.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' "+
				"AND II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' "+
				"AND II.INDEX_LEVEL='"+IrrEnum.INDEX_LEVEL_HEAD.getValue()+"' AND SI.SPLIT_LEVEL='"+IrrEnum.SPLIT_LEVEL_CHANNEL.getValue()+"' GROUP BY II.ID,II.INDEX_CODE,II.INDEX_NAME";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IndexValueDTO>(IndexValueDTO.class));
	}

	@Override
	public Workbook summaryExportIndexResult(String orgId,String projTypeCode,IrrTaskEntity task){
		return summaryExportIndexResultAll(task);
	}

	@Override
	public List<IndexResultExportDTO> findExportIndexResultByCondition(String orgId,String orgCode,String projTypeCode,String taskId)
			throws Exception {
		StringBuffer sb = new StringBuffer("SELECT II.INDEX_LINE,TOR.INDEX_NAME,TOR.INDEX_RESULT_Q1,TOR.INDEX_SCORE_Q1,TOR.INDEX_RESULT_Q2,TOR.INDEX_SCORE_Q2,TOR.DATA_VALI FROM T_IRR_ORG_RESULT TOR INNER JOIN T_IRR_INDEX_INFO II ON TOR.INDEX_ID=II.ID "+
			"INNER JOIN T_IRR_PROJ_TYPE PROJ ON II.PROJ_TYPE_ID=PROJ.ID INNER JOIN FR_AA_ORG ORG ON TOR.ORG_ID=ORG.FD_ID "+
			"WHERE II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND PROJ.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' ");
		if(StringUtils.isNotBlank(taskId)) {
			sb.append("AND TOR.TASK_ID='"+taskId+"' ");
		}
		if(StringUtils.isNotBlank(orgId)) {
			sb.append("AND TOR.ORG_ID='"+orgId+"' ");
		}
		if(StringUtils.isNotBlank(orgCode)) {
			sb.append("AND ORG.FD_CODE='"+orgCode+"' ");
		}
		if(StringUtils.isNotBlank(projTypeCode)) {
			sb.append("AND PROJ.TYPE_CODE='"+projTypeCode+"' ");
		}
		sb.append("ORDER BY II.INDEX_LINE ASC");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IndexResultExportDTO>(IndexResultExportDTO.class));
	}

	@Override
	public Workbook summaryExportIndexResultSingle(String orgId, String projTypeCode, IrrTaskEntity task) {
		return null;
	}

	@Override
	public Workbook summaryExportIndexResultAll(IrrTaskEntity task) {
		Workbook wb = null;
		InputStream is = null;
		try {
			/*获取classPath路径，第一个必须是"/"*/
			String downloadPath = "/"+IrrTemplateEnum.DOWNLOAD_TEMPLATE_URL.getValue()+File.separator+IrrTemplateEnum.SUMMARY_FILE_CODE.getValue();
			is = this.getClass().getResourceAsStream(downloadPath);
			wb = WorkbookFactory.create(is);
			/*查询导出的机构*/
			List<Org> orgs = orgService.getRepository().findAll();
			/*获取到有多少个sheet*/
			int sheetNum = wb.getNumberOfSheets();
			/*单元格样式*/
			CellStyle cellBorderStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
			CellStyle cellBackClolrStyle = POIUtil.createBackgroundColor(wb, HSSFColorPredefined.LIGHT_ORANGE.getIndex(),FillPatternType.SOLID_FOREGROUND);
			CellStyle cellFontStyle = POIUtil.createFontCellStyle(wb, IrrTemplateEnum.IRR_TEMPLATE_TITLE_FONT_NAME.getValue(), 
					Font.COLOR_NORMAL, true);
			/*查询总公司机构*/
			Org rootOrg = new Org();
			rootOrg.setCode(IrrEnum.ORG_ROOT_CODE.getValue());
			List<Org> rootOrgs = orgService.fetch(rootOrg, IrrQueryParameter.getIrrDefaultQP());
			if(CollectionUtils.isEmpty(rootOrgs)) {
				rootOrg = rootOrgs.get(0);
			}
			/*sheet页标题*/
			String[] title = IrrTemplateEnum.SUMM_SHEET_TITLE.getValue().split("-");
			/*循环sheet进行赋值*/
			for(int i=1;i<sheetNum;i++) {
				//sheet名称格式为：评估项目编码+"-"+评估项目名称
				Sheet sheet = wb.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				String[] projInfo = sheetName.split("-");
				String projCode = projInfo[0];
				String projName = projInfo[1];
				//起始行、列开始写入
				Integer rowNum = 1;
				Integer colNum = 3;
				if(StringUtils.isNotBlank(projCode) && projCode.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
					//封面页列从1开始
					colNum = 1;
					//封面页行从2开始
					rowNum = 2;
				}
				//判断是总公司还是分公司评估项目
				if(StringUtils.isNotBlank(projName) && IrrTemplateEnum.SUMMARY_FILE_BRANCH_PROJ_FIRST.getValue().equals(projName.substring(0, 1))) {//分公司评估项目
					//循环机构写入值
					for(Org org : orgs) {
						//判断是否为分公司机构
						if(StringUtils.isBlank(org.getName()) || !org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {
							continue;
						}
						List<IndexResultExportDTO> resultList = findExportIndexResultByCondition(org.getId(), null, projCode, task.getId());
						if(CollectionUtils.isEmpty(resultList)) {
							continue;
						}
						int tempRow = rowNum;
						if(StringUtils.isNotBlank(projCode) && projCode.contains(IrrProjTypeEnum.FM_CODE.getValue())) {//封页面sheet
							Row taskBatchRow = sheet.getRow(tempRow++);
							Cell taskBatchCell = taskBatchRow.createCell(colNum);
							taskBatchCell.setCellStyle(cellBorderStyle);
							taskBatchCell.setCellValue(task.getTaskBatch());
							for(IndexResultExportDTO dto : resultList) {
								Row row = sheet.getRow(tempRow++);
								Cell cell = row.createCell(colNum);
								cell.setCellStyle(cellBorderStyle);
								cell.setCellValue(dto.getIndexResultQ2());
							}
							//当前列结束后，向右移动一列
							colNum ++;
							continue;
						}
						//非封面页
						Row orgRow = sheet.getRow(tempRow++);
						Cell orgCell = orgRow.createCell(colNum);
						orgCell.setCellStyle(cellBorderStyle);
						orgCell.setCellStyle(cellBackClolrStyle);
						orgCell.setCellStyle(cellFontStyle);
						orgCell.setCellValue(org.getName());
						//非封面页数据
						setSheetData(sheet,tempRow,colNum,title,cellBorderStyle,cellFontStyle,cellBackClolrStyle,resultList);
						colNum += title.length;
					}
				}else {//总公司评估项目
					List<IndexResultExportDTO> resultList = findExportIndexResultByCondition(rootOrg.getId(), null, projCode, task.getId());
					if(CollectionUtils.isEmpty(resultList)) {
						continue;
					}
					int tempRow = rowNum;
					if(StringUtils.isNotBlank(projCode) && projCode.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
						//总公司封面页sheet
						for(IndexResultExportDTO dto : resultList) {
							Row row = sheet.getRow(tempRow++);
							Cell cell = row.createCell(colNum);
							cell.setCellStyle(cellBorderStyle);
							cell.setCellValue(dto.getIndexResultQ2());
						}
						continue;
					}
					//非封面页数据
					setSheetData(sheet,tempRow,colNum,title,cellBorderStyle,cellFontStyle,cellBackClolrStyle,resultList);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(),e);
				}
			}
		}
		return wb;
	}

	@Override
	public void setSheetData(Sheet sheet, Integer tempRow, Integer colNum, String[] title, CellStyle borderStyle,
			CellStyle fontStyle, CellStyle backColorStyle,List<IndexResultExportDTO> resultList) throws Exception {
		//非封面页标题
		Row titleRow = sheet.getRow(tempRow++);
		for(int t=0;t<title.length;t++) {
			Cell titleCell = titleRow.createCell(colNum+t);
			titleCell.setCellStyle(borderStyle);
			titleCell.setCellStyle(backColorStyle);
			titleCell.setCellStyle(fontStyle);
			titleCell.setCellValue(title[t]);
		}
		if(CollectionUtils.isEmpty(resultList)) {
			return;
		}
		//非封面页数据
		for(IndexResultExportDTO dto : resultList) {
			Row row = sheet.getRow(tempRow++);
			Cell q1 = row.createCell(colNum);
			q1.setCellStyle(borderStyle);
			q1.setCellValue(dto.getIndexResultQ1());
			Cell q1Score = row.createCell(colNum+1);
			q1Score.setCellStyle(borderStyle);
			q1Score.setCellValue(dto.getIndexScoreQ1());
			Cell q2 = row.createCell(colNum+2);
			q2.setCellStyle(borderStyle);
			q2.setCellValue(dto.getIndexResultQ2());
			Cell q2Score = row.createCell(colNum+3);
			q2Score.setCellStyle(borderStyle);
			q2Score.setCellValue(dto.getIndexScoreQ2());
			Cell dataVali = row.createCell(colNum+4);
			dataVali.setCellStyle(borderStyle);
			dataVali.setCellValue(dto.getDataVali());
		}
	}

	@Override
	public Map<String, Workbook> createReportAllExcel(IrrTaskEntity task) throws Exception {
		Map<String, Workbook> wbMap = new HashMap<String,Workbook>();
		Map<String, String> orgXBRL = systemDictionaryService.getDictionaryMap(IrrEnum.IRR_XBRL_ORG_NAME.getValue(), Locale.CHINA);
		if(MapUtils.isEmpty(orgXBRL)) {
			throw new IrrProcessException("无法导出：数据字典没有配置要上报的机构！");
		}
		/*循环生成EXCEL结果*/
		InputStream is = null;
		try {
			for(String str : orgXBRL.keySet()) { 
				if(StringUtils.isBlank(str)) {
					continue;
				}
				String downloadPath = "/"+IrrTemplateEnum.DOWNLOAD_TEMPLATE_URL.getValue()+File.separator;
				if(IrrEnum.ORG_ROOT_CODE.getValue().equals(str)) {//总公司
					is = this.getClass().getResourceAsStream(downloadPath+IrrTemplateEnum.XBRL_HEAD_TEMP_CODE.getValue());
				}else {//分公司
					is = this.getClass().getResourceAsStream(downloadPath+IrrTemplateEnum.XBRL_BRANCH_TEMP_CODE.getValue());
				}
				Workbook wb = WorkbookFactory.create(is);
				int sheetNum = wb.getNumberOfSheets();
				/*单元格样式*/
				CellStyle cellBorderStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
				for(int i=1;i<sheetNum;i++) {
					Sheet sheet = wb.getSheetAt(i);
					String sheetName = sheet.getSheetName();
					if(StringUtils.isBlank(sheetName)) {
						continue;
					}
					String projCode = sheetName.split("-")[0];
					List<IndexResultExportDTO> resultList = findExportIndexResultByCondition(null, str, projCode, task.getId());
					if(CollectionUtils.isEmpty(resultList)) {
						continue;
					}
					//开始行
					int rowNum = 3;
					//封面页
					if(StringUtils.isNotBlank(projCode) && projCode.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
						rowNum = 1;
						for(IndexResultExportDTO dto : resultList) {
							Row row = sheet.createRow(rowNum++);
							int colNum = 0;
							Cell nameCell = row.createCell(colNum++);
							nameCell.setCellStyle(cellBorderStyle);
							nameCell.setCellValue(dto.getIndexName());
							Cell valueCell = row.createCell(colNum);
							valueCell.setCellStyle(cellBorderStyle);
							valueCell.setCellValue(dto.getIndexResultQ2());
						}
						continue;
					}
					//非封面页
					for(IndexResultExportDTO dto : resultList) {
						Row row = sheet.createRow(rowNum++);
						int colNum = 0;
						Cell lineCell = row.createCell(colNum++);
						lineCell.setCellStyle(cellBorderStyle);
						lineCell.setCellValue(dto.getIndexLine());
						Cell nameCell = row.createCell(colNum++);
						nameCell.setCellStyle(cellBorderStyle);
						nameCell.setCellValue(dto.getIndexName());
						Cell valueCell = row.createCell(colNum);
						valueCell.setCellStyle(cellBorderStyle);
						valueCell.setCellValue(dto.getIndexResultQ2());
					}
				}
				wbMap.put(orgXBRL.get(str), wb);
			}
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}finally {
			if(is != null) {
				is.close();
			}
		}
		return wbMap;
	}

	@Override
	public Map<String, Object> editOrgResult(String id, String indexResultQ2,String dataDesc) throws Exception{
		if(StringUtils.isBlank(id)) {
			throw new IrrProcessException("指标结果ID为空");
		}
		IrrOrgResultEntity orgResult = findById(id);
		if(StringUtils.isNotBlank(indexResultQ2)) {
			orgResult.setIndexResultQ2(indexResultQ2);
			IrrOrgResultEntity exam = new IrrOrgResultEntity();
			exam.setIndexCode(orgResult.getIndexCode());
			exam.setTaskBatch(IrrUtil.getPreviousPeriod(orgResult.getTaskBatch()));
			List<IrrOrgResultEntity> previousList = fetch(exam, IrrQueryParameter.getIrrDefaultQP());
			if(!CollectionUtils.isEmpty(previousList) && previousList.get(0) != null) {
				Object obj = irrUploadResultService.calcDataVali(previousList.get(0).getIndexResultQ2(), indexResultQ2);
				if(obj != null && !"".equals(obj)) {
					orgResult.setDataVali(new BigDecimal(obj.toString()));
				}
			}
		}
		if(StringUtils.isNotBlank(dataDesc)) {
			orgResult.setDataDesc(dataDesc);
		}
		update(orgResult.getId(),orgResult);
		return IrrUtil.getMap(true,"编辑成功！");
	}
	
}
