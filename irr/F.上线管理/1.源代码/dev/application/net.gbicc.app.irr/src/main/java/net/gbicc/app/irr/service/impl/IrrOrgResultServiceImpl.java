package net.gbicc.app.irr.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.jpa.service.support.protocol.criteria.FetchMode;
import org.wsp.framework.jpa.service.support.protocol.criteria.TextMatchStyle;
import org.wsp.framework.mvc.service.OrgService;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexOptionEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrOrgResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
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
import net.gbicc.app.irr.service.IrrChannelResultService;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrIndexOptionService;
import net.gbicc.app.irr.service.IrrIndexResultService;
import net.gbicc.app.irr.service.IrrIndexScoreRelaService;
import net.gbicc.app.irr.service.IrrOrgResultService;
import net.gbicc.app.irr.service.IrrProjResultService;
import net.gbicc.app.irr.service.IrrTaskService;
import net.gbicc.app.irr.service.IrrUploadResultService;

/**
* 机构结果
*/
@Service
@Transactional
public class IrrOrgResultServiceImpl extends DaoServiceImpl<IrrOrgResultEntity, String, IrrOrgResultRepository> 
	implements IrrOrgResultService {
	
	private static final Logger LOG = LogManager.getLogger(IrrOrgResultServiceImpl.class);
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private IrrUploadResultService irrUploadResultService;
	@Autowired private OrgService orgService;
	@Autowired private SystemDictionaryService systemDictionaryService;
	@Autowired private IrrIndexScoreRelaService indexScoreRelaService;
	@Autowired private IrrTaskService irrTaskService;
	@Autowired private IrrIndexInfoService indexInfoService;
	@Autowired private IrrIndexOptionService indexOptionService;
	@Autowired private IrrIndexResultService indexResultService;
	@Autowired private IrrChannelResultService irrChannelResultService;
    @Autowired
    private IrrProjResultService irrProjResultService;
    /**
     * 项目work中的download目录
     */
    @Value("${dir.work.web.download}")
    private String workDownloadUrl;

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
			orgResult.setIndexResultQ1(previous.getIndexResultQ2());
			orgResult.setIndexScoreQ1(previous.getIndexScoreQ2());
			Object calcVali = irrUploadResultService.calcDataVali(previous.getIndexResultQ2(), orgResult.getIndexResultQ2());
			if(!IrrUtil.strObjectIsEmpty(calcVali)) {
				orgResult.setDataVali(new BigDecimal(calcVali.toString()));
			}
			calcValiList.add(update(orgResult.getId(), orgResult));
		}
		return calcValiList;
	}

	@Override
	public Map<String, Object> findOrgResult(IrrOrgResultEntity orgResult, Integer size, Integer page,
			String projTypeId) throws Exception {
		String sql="SELECT TOR.* FROM T_IRR_ORG_RESULT TOR INNER JOIN T_IRR_INDEX_INFO INFO ON "
				 + "TOR.INDEX_ID=INFO.ID WHERE 1=1 ";
		StringBuffer sb = new StringBuffer(sql);
		if(StringUtils.isNotBlank(projTypeId)) {
			sb.append("AND INFO.PROJ_TYPE_ID='"+projTypeId+"' ");
		}
		if(orgResult != null) {
			if(StringUtils.isNotBlank(orgResult.getTaskId())) {
				sb.append("AND TOR.TASK_ID ='"+orgResult.getTaskId()+"' ");
			}
			if(StringUtils.isNotBlank(orgResult.getIndexName())) {
				sb.append("AND TOR.INDEX_NAME LIKE '%"+orgResult.getIndexName()+"%' ");
			}
			if(StringUtils.isNotBlank(orgResult.getIndexCode())) {
				sb.append("AND TOR.INDEX_CODE LIKE '%"+orgResult.getIndexCode()+"%' ");
			}
			if(StringUtils.isNotBlank(orgResult.getOrgId())) {
				sb.append("AND TOR.ORG_ID LIKE '%"+orgResult.getOrgId()+"%' ");
			}
		}
        sb.append("ORDER BY INFO.INDEX_LINE");
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
	public Workbook summaryExportIndexResult(IrrTaskEntity task) throws Exception{
        Workbook wb = irrChannelResultService.exportChannelIndexResult(task,
                summaryExportIndexResultAll(task, IrrTemplateEnum.SUMMARY_FILE_CODE.getValue()));
        //导出权重值
        return irrProjResultService.exportProjResult(task, wb);
	}

	@Override
    public List<IndexResultExportDTO> findExportIndexResultByCondition(String orgId, String orgCode,
            String projTypeCode, String taskId, Boolean isXbrl)
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
        if (isXbrl != null) {
            if (isXbrl) {
                sb.append("AND II.IS_XBRL='" + IrrEnum.YESNO_Y.getValue() + "' ");
            } else {
                sb.append("AND II.IS_XBRL='" + IrrEnum.YESNO_N.getValue() + "' ");
            }
        }
		sb.append("ORDER BY II.INDEX_LINE ASC");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IndexResultExportDTO>(IndexResultExportDTO.class));
	}

	@Override
	public Workbook summaryExportIndexResultSingle(String orgId, String projTypeCode, IrrTaskEntity task) {
		return null;
	}

	@Override
	public Workbook summaryExportIndexResultAll(IrrTaskEntity task,String templatePath) {
		Workbook wb = null;
		InputStream is = null;
		try {
            is = new FileInputStream(new File(workDownloadUrl + File.separator + templatePath));
			wb = WorkbookFactory.create(is);
			/*查询导出的机构*/
			List<Org> orgs = orgService.getRepository().findAll();
			Collections.sort(orgs, new Comparator<Org>() {
				@Override
				public int compare(Org org1, Org org2) {
					if(Integer.parseInt(org1.getCode())>Integer.parseInt(org2.getCode())){
	                    return 1;
	                }
	                if(Integer.parseInt(org1.getCode())==Integer.parseInt(org2.getCode())){
	                    return 0;
	                }
	                return -1;
				}
				
			});
			/*获取到有多少个sheet*/
			int sheetNum = wb.getNumberOfSheets();
			/*单元格样式*/
			CellStyle cellBorderStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
			CellStyle cellBackClolrStyle = POIUtil.createBackgroundColor(wb, HSSFColorPredefined.LIGHT_ORANGE.getIndex(),FillPatternType.SOLID_FOREGROUND);
			CellStyle cellFontStyle = POIUtil.createFontCellStyle(wb, IrrTemplateEnum.IRR_TEMPLATE_TITLE_FONT_NAME.getValue(), 
					Font.COLOR_NORMAL, true);
			/*查询总公司机构*/
            Org rootOrg = orgService.getRepository().findByCode(IrrEnum.ORG_ROOT_CODE.getValue());
            if (rootOrg == null) {
                rootOrg = new Org();
			}
			/*sheet页标题*/
			String[] title = IrrTemplateEnum.SUMM_SHEET_TITLE.getValue().split("-");
			/*循环sheet进行赋值*/
			for(int i=1;i<sheetNum;i++) {
				//sheet名称格式为：评估项目编码+"-"+评估项目名称
				Sheet sheet = wb.getSheetAt(i);
				String sheetName = sheet.getSheetName();
                if (StringUtils.isBlank(sheetName) || sheetName.contains(IrrTemplateEnum.IRR_SHEET_NAME.getValue())
                        || !sheetName.contains("-")) {
                    //绩效sheet页、权重sheet页不进行设置值
				    continue;
				}
				String[] projInfo = sheetName.split("-");
				String projCode = projInfo[0];
				String projName = projInfo[1];
				//起始行、列开始写入
				Integer rowNum = 1;
				Integer colNum = 3;
				if(StringUtils.isNotBlank(projCode) && projCode.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
					//封面页列从1开始
					colNum = 1;
					//封面页行从1开始
					rowNum = 1;
				}
				//判断是总公司还是分公司评估项目
				if(StringUtils.isNotBlank(projName) && IrrTemplateEnum.SUMMARY_FILE_BRANCH_PROJ_FIRST.getValue().equals(projName.substring(0, 1))) {//分公司评估项目
					//循环机构写入值
					for(Org org : orgs) {
						//判断是否为分公司机构
						if(StringUtils.isBlank(org.getName()) || !org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {
							continue;
						}
                        List<IndexResultExportDTO> resultList = findExportIndexResultByCondition(org.getId(), null,
                                projCode, task.getId(), null);
						if(CollectionUtils.isEmpty(resultList)) {
							continue;
						}
						int tempRow = rowNum;
						if(StringUtils.isNotBlank(projCode) && projCode.contains(IrrProjTypeEnum.FM_CODE.getValue())) {//封页面sheet
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
                    List<IndexResultExportDTO> resultList = findExportIndexResultByCondition(rootOrg.getId(), null,
                            projCode, task.getId(), null);
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
            if (row == null) {
                break;
            }
			Cell q1 = row.createCell(colNum);
			q1.setCellStyle(borderStyle);
            String indexResultQ1 = dto.getIndexResultQ1();
            if (IrrUtil.isIntOrDouble(indexResultQ1)) {
                q1.setCellValue(Double.valueOf(indexResultQ1));
            }else {
                q1.setCellValue(indexResultQ1);
            }
			Cell q1Score = row.createCell(colNum+1);
			q1Score.setCellStyle(borderStyle);
            String indexScoreQ1 = dto.getIndexScoreQ1();
            if (IrrUtil.isIntOrDouble(indexScoreQ1)) {
                q1Score.setCellValue(Double.valueOf(indexScoreQ1));
            }else {
                q1Score.setCellValue(indexScoreQ1);
            }
			Cell q2 = row.createCell(colNum+2);
			q2.setCellStyle(borderStyle);
            String indexResultQ2 = dto.getIndexResultQ2();
            if (IrrUtil.isIntOrDouble(indexResultQ2)) {
                q2.setCellValue(Double.valueOf(indexResultQ2));
            } else {
                q2.setCellValue(indexResultQ2);
            }
			Cell q2Score = row.createCell(colNum+3);
			q2Score.setCellStyle(borderStyle);
            String indexScoreQ2 = dto.getIndexScoreQ2();
            if (IrrUtil.isIntOrDouble(indexScoreQ2)) {
                q2Score.setCellValue(Double.valueOf(indexScoreQ2));
            } else {
                q2Score.setCellValue(indexScoreQ2);
            }
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
                for (int i = 0; i < sheetNum; i++) {
					Sheet sheet = wb.getSheetAt(i);
					String sheetName = sheet.getSheetName();
                    if (StringUtils.isBlank(sheetName) || IrrEnum.SHEET_DIREC_NAME.getValue().equals(sheetName)) {
						continue;
					}
					String projCode = sheetName.split("-")[0];
                    List<IndexResultExportDTO> resultList = findExportIndexResultByCondition(null, str, projCode,
                            task.getId(), true);
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
                wbMap.put(orgXBRL.get(str) + IrrEnum.SEPARATOR.getValue()
                        + IrrUtil.getXbrlReportEndDate(task.getTaskBatch()), wb);
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
            exam.setOrgId(orgResult.getOrgId());
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

	@Override
	public List<IrrOrgResultEntity> calcOrgIndexScore(String orgId, IrrTaskEntity task) throws Exception {
		Org org = orgService.findById(orgId);
		/*机构结果*/
		IrrOrgResultEntity examOrgResult = new IrrOrgResultEntity();
		examOrgResult.setTaskId(task.getId());
		examOrgResult.setOrgId(orgId);
		List<IrrOrgResultEntity> orgResultList = fetch(examOrgResult, IrrQueryParameter.getIrrDefaultQP());
		if(CollectionUtils.isEmpty(orgResultList)) {
			return null;
		}
		Map<String, String> orgResultMap = new HashMap<String, String>();
		for(IrrOrgResultEntity orgResult : orgResultList) {
			if(orgResult != null) {
				orgResultMap.put(orgResult.getIndexCode(), orgResult.getIndexResultQ2());
			}
		}
		/*全部启用得分指标*/
		IrrIndexInfoEntity indexExam = new IrrIndexInfoEntity();
		indexExam.setIndexStatus(IrrEnum.INDEX_STATUS_ENABLE.getValue());
		indexExam.setIsScoreIndex(IrrEnum.YESNO_Y.getValue());
		indexExam.setIsApplicabie(IrrEnum.YESNO_Y.getValue());
		if(org != null && StringUtils.isNotBlank(org.getName()) && org.getName().contains(IrrProcessEnum.IRR_BRANCH_SUFFIX.getValue().toString())) {
			//分公司
			indexExam.setIndexLevel(IrrEnum.INDEX_LEVEL_BRANCH.getValue());
		}else {
			indexExam.setIndexLevel(IrrEnum.INDEX_LEVEL_HEAD.getValue());
		}
		List<IrrIndexInfoEntity> scoreIndexList = indexInfoService.fetch(indexExam, IrrQueryParameter.getIrrDefaultQP());
		if(CollectionUtils.isEmpty(scoreIndexList)) {
			return null;
		}
		Map<String, IrrIndexInfoEntity> scoreIndexMap = new HashMap<String,IrrIndexInfoEntity>();
		for(IrrIndexInfoEntity indexInfo : scoreIndexList) {
			if(indexInfo != null) {
				scoreIndexMap.put(indexInfo.getIndexCode(), indexInfo);
			}
		}
		List<IrrOrgResultEntity> returnList = new ArrayList<IrrOrgResultEntity>();
		/*计算有得分公式的指标得分*/
		List<IrrOrgResultEntity> directList = findOrgResultByCondition(IrrEnum.INDEX_STATUS_ENABLE.getValue(),null, 
				IrrEnum.YESNO_Y.getValue(), null, null,IrrEnum.YESNO_N.getValue(),orgId,task.getId());
		if(CollectionUtils.isNotEmpty(directList)) {
			for(IrrOrgResultEntity orgResult : directList) {
				IrrIndexInfoEntity indexInfo = scoreIndexMap.get(orgResult.getIndexCode());
				if(indexInfo != null) {
					List<String> relaCodeList = indexScoreRelaService.findRelaIndexCodeByCondition(orgResult.getIndexId());
					Object result = irrTaskService.calcFormula(indexInfo.getPfmEvalForm(), relaCodeList, orgResultMap);
					if(!IrrUtil.strObjectIsEmpty(result)) {
						orgResult.setIndexScoreQ2(result.toString());
					}
				}
				returnList.add(update(orgResult.getId(), orgResult));
			}
		}
		/*计算选项指标得分*/
		List<IrrOrgResultEntity> directNoFormulaList = findOrgResultByCondition(IrrEnum.INDEX_STATUS_ENABLE.getValue(),IrrEnum.YESNO_Y.getValue(), 
				IrrEnum.YESNO_Y.getValue(), IrrEnum.INDEX_RESULT_TYPE_OPTION.getValue(), null,IrrEnum.YESNO_Y.getValue(),orgId,task.getId());
		/*全部选项*/
		List<IrrIndexOptionEntity> optionList = indexOptionService.fetch(new IrrIndexOptionEntity(), new IrrQueryParameter(FetchMode.EMPTY_CRITERIA_ALL, TextMatchStyle.substring));
		Map<String, String> optionMap = new HashMap<String,String>();
		for(IrrIndexOptionEntity option : optionList) {
			if(option != null && option.getOptionScore() != null) {
				optionMap.put(option.getIndexCode()+option.getOptionSort(), option.getOptionScore().toString());
			}
		}
		if(CollectionUtils.isNotEmpty(directNoFormulaList)) {
			for(IrrOrgResultEntity orgResult : directNoFormulaList) {
				String result = orgResult.getIndexResultQ2();
				if(StringUtils.isNotBlank(result)) {
					String sort = result.substring(0, 1);
					String score = optionMap.get(orgResult.getIndexCode()+sort);
					if(StringUtils.isNotBlank(score)) {
						orgResult.setIndexScoreQ2(score);
					}
				}
				returnList.add(update(orgResult.getId(), orgResult));
			}
		}
		/*计算不适用指标得分*/
		List<IrrOrgResultEntity> disableList = findOrgResultByCondition(IrrEnum.INDEX_STATUS_ENABLE.getValue(),IrrEnum.YESNO_Y.getValue(), 
				IrrEnum.YESNO_N.getValue(), null, null,null,orgId,task.getId());
		if(CollectionUtils.isNotEmpty(disableList)) {
			for(IrrOrgResultEntity orgResult : disableList) {
				orgResult.setIndexScoreQ2(IrrEnum.INDEX_NOT_APPLICABIE_VALUE.getValue());
				returnList.add(update(orgResult.getId(), orgResult));
			}
		}
		return returnList;
	}

	@Override
	public List<IrrOrgResultEntity> findOrgResultByCondition(String indexStatus, String isScore, String isApplication,
			String resultType, String scoreType,String pfmEvalFormulaIsNull,String orgId,String taskId) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT ORGR.* FROM T_IRR_ORG_RESULT ORGR INNER JOIN T_IRR_INDEX_INFO II ON ORGR.INDEX_ID=II.ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(indexStatus)) {
			sb.append("AND II.INDEX_STATUS='"+indexStatus+"' ");
		}
		if(StringUtils.isNotBlank(isScore)) {
			sb.append("AND II.IS_SCORE_INDEX='"+isScore+"' ");
		}
		if(StringUtils.isNotBlank(isApplication)) {
			sb.append("AND II.IS_APPLICABIE='"+isApplication+"' ");
		}
		if(StringUtils.isNotBlank(resultType)) {
			sb.append("AND II.INDEX_RESULT_TYPE='"+resultType+"' ");
		}
		if(StringUtils.isNotBlank(scoreType)) {
			sb.append("AND II.SCOR_TYPE='"+scoreType+"' ");
		}
		if(StringUtils.isNotBlank(pfmEvalFormulaIsNull)) {
			if(IrrEnum.YESNO_Y.getValue().equals(pfmEvalFormulaIsNull)) {
				sb.append("AND II.PFM_EVAL_FORM IS NULL ");
			}else {
				sb.append("AND II.PFM_EVAL_FORM IS NOT NULL ");
			}
		}
		if(StringUtils.isNotBlank(orgId)) {
			sb.append("AND ORGR.ORG_ID='"+orgId+"' ");
		}
		if(StringUtils.isNotBlank(taskId)) {
			sb.append("AND ORGR.TASK_ID='"+taskId+"' ");
		}
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
	}

	@Override
	public List<IrrIndexResultEntity> calcBranchIndexSource(IrrTaskEntity task,IrrProjTypeEntity projType,
			Map<String,IrrIndexResultEntity> perivousIndexResult,Integer branchNum) throws Exception {
		List<IrrIndexResultEntity> returnList = new ArrayList<IrrIndexResultEntity>();
		/*数值结果类型*/
		List<Map<String, Object>> numberResultList = findSumOrgResultByCondition(IrrEnum.YESNO_Y.getValue(), projType.getId(), IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue(), task.getId(), false);
		List<Map<String, Object>> numberScoreList = findSumOrgResultByCondition(IrrEnum.YESNO_Y.getValue(), projType.getId(), IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue(), task.getId(), true);
		for(Map<String, Object> map : numberResultList) {
			if(IrrUtil.strObjectIsEmpty(map.get("INDEX_ID"))) {
				continue;
			}
			String indexId = map.get("INDEX_ID").toString();
			for(Map<String, Object> scoreMap : numberScoreList) {
				if(IrrUtil.strObjectIsEmpty(scoreMap.get("INDEX_ID")) 
						|| !indexId.equals(scoreMap.get("INDEX_ID"))) {
					continue;
				}
				map.put("SCOREQ2", scoreMap.get("SCOREQ2"));
				break;
			}
		}
		if(CollectionUtils.isNotEmpty(numberResultList)) {
			for(Map<String, Object> numMap : numberResultList) {
				IrrIndexResultEntity indexResult = new IrrIndexResultEntity();
				indexResult.setTaskId(task.getId());
				indexResult.setTaskBatch(task.getTaskBatch());
				indexResult.setIndexId(numMap.get("INDEX_ID").toString());
				indexResult.setIndexCode(numMap.get("INDEX_CODE").toString());
				indexResult.setIndexName(numMap.get("INDEX_NAME").toString());
				//计算平均值
				Object resultQ2 = numMap.get("RESULTQ2");
				if(!IrrUtil.strObjectIsEmpty(resultQ2)) {
					indexResult.setIndexResultQ2(String.valueOf(Double.valueOf(resultQ2.toString())/branchNum));
				}
				Object scoreQ2 = numMap.get("SCOREQ2");
				if(!IrrUtil.strObjectIsEmpty(scoreQ2)) {
					indexResult.setIndexScoreQ2(String.valueOf(Double.valueOf(scoreQ2.toString())/branchNum));
				}
				//上期数据
				IrrIndexResultEntity pervious = perivousIndexResult.get(indexResult.getIndexCode());
				if(pervious != null) {
					indexResult.setIndexResultQ1(pervious.getIndexResultQ2());
					indexResult.setIndexScoreQ2(pervious.getIndexScoreQ2());
				}
				//得分指标计算其他项
				if(StringUtils.isNotBlank(indexResult.getIndexScoreQ2())) {
					if(StringUtils.isNotBlank(indexResult.getIndexScoreQ1())) {
						//分数差
						indexResult.setFracDiff(new BigDecimal(Double.valueOf(indexResult.getIndexScoreQ2()) - Double.valueOf(indexResult.getIndexScoreQ1())));
					}
					Object indexValue = numMap.get("INDEX_VALUE");
					if(!IrrUtil.strObjectIsEmpty(indexValue)) {
						//扣分
						indexResult.setDeduValue(new BigDecimal(Double.valueOf(indexValue.toString()) - Double.valueOf(indexResult.getIndexScoreQ2())));
					}
					String bureauRate = projType.getBureauRate();
					if(StringUtils.isNotBlank(bureauRate) && indexResult.getDeduValue() != null) {
						//加权
						indexResult.setWeigValue(new BigDecimal(Double.valueOf(bureauRate) * indexResult.getDeduValue().doubleValue()));
					}
					if(indexResult.getWeigValue() != null) {
						String theRiskRate = projType.getTheRiskRate();
						if(StringUtils.isNotBlank(theRiskRate)) {
							//占操作风险
							indexResult.setHoldOperRisk(new BigDecimal(Double.valueOf(theRiskRate) * indexResult.getWeigValue().doubleValue()));
						}
						String pdqRiskRate = projType.getPdqRiskRate();
						if(StringUtils.isNotBlank(pdqRiskRate)) {
							//占难以量化
							indexResult.setHoldQuanDiff(new BigDecimal(Double.valueOf(pdqRiskRate) * indexResult.getWeigValue().doubleValue()));
						}
					}
				}
				returnList.add(indexResultService.add(indexResult));
			}
		}
		/*选项结果类型*/
		List<Map<String, Object>> optionIndex = findSumOrgResultByCondition(IrrEnum.YESNO_Y.getValue(), projType.getId(), IrrEnum.INDEX_RESULT_TYPE_OPTION.getValue(), task.getId(),true);
		if(CollectionUtils.isNotEmpty(optionIndex)) {
			//默认取得分最高的
			List<IrrIndexOptionEntity> maxScoreList = indexOptionService.findAllMaxPoints();
			Map<String, IrrIndexOptionEntity> optionMap = new HashMap<String, IrrIndexOptionEntity>();
			if(CollectionUtils.isNotEmpty(maxScoreList)) {
				for(IrrIndexOptionEntity io : maxScoreList) {
					optionMap.put(io.getIndexId(), io);
				}
			}
			for(Map<String, Object> option : optionIndex) {
				if(MapUtils.isEmpty(option)) {
					continue;
				}
				IrrIndexResultEntity indexResult = new IrrIndexResultEntity();
				indexResult.setTaskId(task.getId());
				indexResult.setTaskBatch(task.getTaskBatch());
				indexResult.setIndexId(option.get("INDEX_ID").toString());
				indexResult.setIndexCode(option.get("INDEX_CODE").toString());
				indexResult.setIndexName(option.get("INDEX_NAME").toString());
				if(!IrrUtil.strObjectIsEmpty(option.get("SCOREQ2"))) {
					indexResult.setIndexScoreQ2(String.valueOf(Double.valueOf(option.get("SCOREQ2").toString())/branchNum));
				}
				//上期数据
				IrrIndexResultEntity pervious = perivousIndexResult.get(indexResult.getIndexCode());
				if(pervious != null) {
					indexResult.setIndexResultQ1(pervious.getIndexResultQ2());
					indexResult.setIndexScoreQ2(pervious.getIndexScoreQ2());
				}
				//本期数据
				IrrIndexOptionEntity io = optionMap.get(option.get("INDEX_ID").toString());
				if(io != null) {
					indexResult.setIndexResultQ2(io.getOptionName());
				}
				returnList.add(indexResultService.add(indexResult));
			}
		}
		/*不适用*/
		List<Map<String, Object>> disableIndex = findSumOrgResultByCondition(IrrEnum.YESNO_N.getValue(), projType.getId(), null, task.getId(),null);
		if(!CollectionUtils.isEmpty(disableIndex)) {
			for(Map<String, Object> disable : disableIndex) {
				if(MapUtils.isEmpty(disable)) {
					continue;
				}
				IrrIndexResultEntity indexResult = new IrrIndexResultEntity();
				indexResult.setTaskId(task.getId());
				indexResult.setTaskBatch(task.getTaskBatch());
				indexResult.setIndexId(disable.get("ID").toString());
				indexResult.setIndexCode(disable.get("INDEX_CODE").toString());
				indexResult.setIndexName(disable.get("INDEX_NAME").toString());
				indexResult.setIndexResultQ2(IrrEnum.INDEX_NOT_APPLICABIE_VALUE.getValue());
				indexResult.setIndexScoreQ2(IrrEnum.INDEX_NOT_APPLICABIE_VALUE.getValue());
				//上期数据
				IrrIndexResultEntity pervious = perivousIndexResult.get(indexResult.getIndexCode());
				if(pervious != null) {
					indexResult.setIndexResultQ1(pervious.getIndexResultQ2());
					indexResult.setIndexScoreQ2(pervious.getIndexScoreQ2());
				}
				returnList.add(indexResultService.add(indexResult));
			}
		}
		return returnList;
	}

	@Override
	public List<IrrIndexResultEntity> calcHeadIndexSource(IrrTaskEntity task) throws Exception {
		List<IrrIndexResultEntity> returnList = new ArrayList<IrrIndexResultEntity>();
		/*总公司的直接从机构结果表中直接取过来(剔除封面)*/
		String sql = "SELECT * FROM T_IRR_ORG_RESULT IOR WHERE IOR.ORG_CODE='"+IrrEnum.ORG_ROOT_CODE.getValue()+"' AND IOR.TASK_ID='"+task.getId()+"' AND IOR.INDEX_CODE NOT LIKE '"+IrrProjTypeEnum.FM_CODE.getValue()+"%'";
		List<IrrOrgResultEntity> headOrgResult = jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrOrgResultEntity>(IrrOrgResultEntity.class));
		if(CollectionUtils.isNotEmpty(headOrgResult)) {
			for(IrrOrgResultEntity org : headOrgResult) {
				IrrIndexResultEntity indexResult = new IrrIndexResultEntity();
				indexResult.setTaskId(task.getId());
				indexResult.setTaskBatch(task.getTaskBatch());
				indexResult.setIndexId(org.getIndexId());
				indexResult.setIndexCode(org.getIndexCode());
				indexResult.setIndexName(org.getIndexName());
				indexResult.setIndexResultQ1(org.getIndexResultQ1());
				indexResult.setIndexResultQ2(org.getIndexResultQ2());
				indexResult.setIndexScoreQ1(org.getIndexScoreQ1());
				indexResult.setIndexScoreQ2(org.getIndexScoreQ2());
				indexResult.setDataVali(org.getDataVali());
				indexResult.setDataDesc(org.getDataDesc());
				returnList.add(indexResultService.add(indexResult));
			}
		}
		return returnList;
	}

	@Override
	public List<Map<String, Object>> findSumOrgResultByCondition(String isApplication, String projTypeId,
			String resultType,String taskId,Boolean isScore) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT ORGR.INDEX_ID,ORGR.INDEX_CODE,ORGR.INDEX_NAME,II.INDEX_VALUE ");
		if(isScore != null) {
			if(isScore) {
				sb.append(",SUM(NVL(ORGR.INDEX_SCORE_Q2,0)) SCOREQ2");
			}else {
				sb.append(",SUM(NVL(ORGR.INDEX_RESULT_Q2,0)) RESULTQ2");
			}
		}
		sb.append(" FROM T_IRR_INDEX_INFO II INNER JOIN T_IRR_ORG_RESULT ORGR ON II.ID=ORGR.INDEX_ID WHERE 1=1");
		if(StringUtils.isNotBlank(isApplication)) {
			sb.append("AND II.IS_APPLICABIE='"+isApplication+"' ");
		}
		if(StringUtils.isNotBlank(projTypeId)) {
			sb.append("AND II.PROJ_TYPE_ID='"+projTypeId+"' ");
		}
		if(StringUtils.isNotBlank(resultType)) {
			sb.append("AND II.INDEX_RESULT_TYPE='"+resultType+"' ");
		}
		if(StringUtils.isNotBlank(taskId)) {
			sb.append("AND ORGR.TASK_ID='"+taskId+"' ");
		}
		if(isScore != null) {
			if(isScore) {
				sb.append("AND LENGTH(ORGR.INDEX_SCORE_Q2)=LENGTHB(ORGR.INDEX_SCORE_Q2) ");
			}
		}
		sb.append("GROUP BY ORGR.INDEX_ID,ORGR.INDEX_CODE,ORGR.INDEX_NAME,II.INDEX_VALUE");
		return jdbcTemplate.queryForList(sb.toString());
	}

	@Override
	public List<Map<String, Object>> findOrgIndexInfoByCondition(String taskId, String isApplication, String projTypeId,
			String resultType) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT DISTINCT II.ID,II.INDEX_CODE,II.INDEX_NAME,II.IS_SCORE_INDEX FROM T_IRR_ORG_RESULT ORGR INNER JOIN T_IRR_INDEX_INFO II ON ORGR.INDEX_ID=II.ID WHERE 1=1");
		if(StringUtils.isNotBlank(taskId)) {
			sb.append("AND ORGR.TASK_ID='"+taskId+"' ");
		}
		if(StringUtils.isNotBlank(isApplication)) {
			sb.append("AND II.IS_APPLICABIE='"+isApplication+"' ");
		}
		if(StringUtils.isNotBlank(projTypeId)) {
			sb.append("AND II.PROJ_TYPE_ID='"+projTypeId+"' ");
		}
		if(StringUtils.isNotBlank(resultType)) {
			sb.append("AND II.INDEX_RESULT_TYPE='"+resultType+"' ");
		}
		return jdbcTemplate.queryForList(sb.toString());
	}

	@Override
	public Double sumOrgIndexTotalScoreResult(IrrTaskEntity task, IrrProjTypeEntity proj, Org org) throws Exception {
		if(task == null || proj == null || org == null) {
			return 0d;
		}
		String sql = "SELECT SUM(NVL(IOR.INDEX_SCORE_Q2,0)) TOTAL_SCORE FROM T_IRR_ORG_RESULT IOR INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_ID=II.ID \r\n" + 
				"WHERE II.IS_SCORE_INDEX='"+IrrEnum.YESNO_Y.getValue()+"' AND II.PROJ_TYPE_ID='"+proj.getId()+"' AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' "+
				"AND IOR.ORG_ID='"+org.getId()+"' AND IOR.TASK_ID='"+task.getId()+"'\r\n" + 
				"AND REGEXP_LIKE(IOR.INDEX_SCORE_Q2,'(^[+-]?\\d{0,}\\.?\\d{0,}$)')";
		List<Map<String, Object>> resultMapList = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(resultMapList)) {
			return 0d;
		}
		Object obj = resultMapList.get(0).get("TOTAL_SCORE");
		if(IrrUtil.strObjectIsEmpty(obj)) {
			return 0d;
		}
		return Double.valueOf(obj.toString());
	}

	@Override
	public Double calcOrgScoreInduLevelResult(IrrTaskEntity task, IrrProjTypeEntity proj, Org org) throws Exception {
		if(task == null || proj == null || org == null) {
			return 0d;
		}
		String sql = "SELECT II.INDEX_VALUE STANVALUE,IOR.INDEX_SCORE_Q2 SCORE FROM T_IRR_ORG_RESULT IOR INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_ID=II.ID\r\n" + 
				"WHERE IOR.TASK_ID='"+task.getId()+"' AND IOR.ORG_ID='"+org.getId()+"' AND II.PROJ_TYPE_ID='"+proj.getId()+"' "+
				"AND II.IS_SCORE_INDEX='"+IrrEnum.YESNO_Y.getValue()+"' AND II.SCOR_TYPE='"+IrrEnum.SCORE_TYPE_INDUSTRY.getValue()+"'";
		List<Map<String, Object>> induLevelInfoMap = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(induLevelInfoMap)) {
			return 0d;
		}
		Double maxValue = 0d;
		Double sumScore = 0d;
		for(Map<String, Object> map : induLevelInfoMap) {
			if(!IrrUtil.strObjectIsEmpty(map.get("STANVALUE")) && !IrrUtil.strObjectIsEmpty(map.get("SCORE"))) {
				maxValue += Double.valueOf(map.get("STANVALUE").toString());
				if(IrrUtil.isIntOrDouble(map.get("SCORE").toString())) {
					sumScore += Double.valueOf(map.get("SCORE").toString());
				}
			}
		}
		if(maxValue <= sumScore) {
			return maxValue;
		}
		return 0d;
	}

	@Override
	public Double sumInduIndexStanValue(IrrProjTypeEntity proj) throws Exception {
		if(proj == null) {
			return 0d;
		}
		String sql = "SELECT SUM(NVL(II.INDEX_VALUE,0)) STANSUM FROM T_IRR_INDEX_INFO II "
					+ "WHERE II.IS_SCORE_INDEX='"+IrrEnum.YESNO_Y.getValue()+"' AND II.SCOR_TYPE='"+IrrEnum.SCORE_TYPE_INDUSTRY.getValue()+"' "
					+ "AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.PROJ_TYPE_ID='"+proj.getId()+"'"; 
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isNotEmpty(mapList) && mapList.get(0) != null 
				&& !IrrUtil.strObjectIsEmpty(mapList.get(0).get("STANSUM"))
				&& IrrUtil.isIntOrDouble(mapList.get(0).get("STANSUM").toString())) {
			return Double.valueOf(mapList.get(0).get("STANSUM").toString());
		}
		return 0d;
	}

	@Override
	public Double sumInduOrgScore(IrrTaskEntity task, IrrProjTypeEntity proj, Org org) throws Exception {
		if(task == null || proj == null || org == null) {
			return 0d;
		}
		String sql = "SELECT IOR.INDEX_SCORE_Q2 SCORE FROM T_IRR_ORG_RESULT IOR INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_ID=II.ID\r\n" + 
				"WHERE IOR.TASK_ID='"+task.getId()+"' AND IOR.ORG_ID='"+org.getId()+"' AND II.PROJ_TYPE_ID='"+proj.getId()+"' \r\n" + 
				"AND II.IS_SCORE_INDEX='"+IrrEnum.YESNO_Y.getValue()+"' AND II.IS_XBRL='"+IrrEnum.YESNO_Y.getValue()+"' AND II.SCOR_TYPE='"+IrrEnum.SCORE_TYPE_INDUSTRY.getValue()+"'";
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(mapList)) {
			return 0d;
		}
		Double result = 0d;
		for(Map<String, Object> map : mapList) {
			if(!IrrUtil.strObjectIsEmpty(map.get("SCORE")) && IrrUtil.isIntOrDouble(map.get("SCORE").toString())) {
				result += Double.valueOf(map.get("SCORE").toString());
			}
		}
		return result;
	}

	@Override
	public Double sumProjScoreIndexStanValue(IrrProjTypeEntity proj) throws Exception {
		if(proj == null) {
			return 0d;
		}
		String sql = "SELECT SUM(NVL(II.INDEX_VALUE,0)) STANSUM FROM T_IRR_INDEX_INFO II WHERE II.IS_SCORE_INDEX='"+IrrEnum.YESNO_Y.getValue()+"' "+
					 "AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' AND II.PROJ_TYPE_ID='"+proj.getId()+"' AND II.SCOR_TYPE<>'"+IrrEnum.SCORE_TYPE_DEDUCTION.getValue()+"'"; 
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(mapList) || mapList.get(0) == null) {
			return 0d;
		}
		if(!IrrUtil.strObjectIsEmpty(mapList.get(0).get("STANSUM"))) {
			return Double.valueOf(mapList.get(0).get("STANSUM").toString());
		}
		return 0d;
	}

	@Override
	public List<Map<String, Object>> findOrgResultInfoByCondition(String isApplication, String projTypeId,
			String resultType, String taskId) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT IOR.INDEX_ID,IOR.INDEX_CODE,IOR.INDEX_NAME,II.INDEX_VALUE,IOR.INDEX_RESULT_Q2,IOR.INDEX_SCORE_Q2 ");
		sb.append("FROM T_IRR_ORG_RESULT IOR INNER JOIN T_IRR_INDEX_INFO II ON IOR.INDEX_ID=II.ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(projTypeId)) {
			sb.append("AND II.PROJ_TYPE_ID='"+projTypeId+"' ");
		}
		if(StringUtils.isNotBlank(isApplication)) {
			sb.append("AND II.IS_APPLICABIE='"+isApplication+"' ");
		}
		if(StringUtils.isNotBlank(resultType)) {
			sb.append("AND II.INDEX_RESULT_TYPE='"+resultType+"' ");
		}
		if(StringUtils.isNotBlank(taskId)) {
			sb.append("AND IOR.TASK_ID='"+taskId+"' ");
		}
		return jdbcTemplate.queryForList(sb.toString());
	}

}
