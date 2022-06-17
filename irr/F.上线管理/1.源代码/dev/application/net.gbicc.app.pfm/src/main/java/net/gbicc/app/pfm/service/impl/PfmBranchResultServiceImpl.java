package net.gbicc.app.pfm.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrInfosysResultService;
import net.gbicc.app.pfm.jpa.entity.PfmBranchResultEntity;
import net.gbicc.app.pfm.jpa.entity.PfmChannelResultEntity;
import net.gbicc.app.pfm.jpa.entity.PfmDepaResultEntity;
import net.gbicc.app.pfm.jpa.exception.PfmException;
import net.gbicc.app.pfm.jpa.repository.PfmBranchResultRepository;
import net.gbicc.app.pfm.jpa.support.dto.EadsContinueChargeRateDTO;
import net.gbicc.app.pfm.jpa.support.dto.EadsContractVisitRateDTO;
import net.gbicc.app.pfm.jpa.support.dto.EadsEmployeeLossDTO;
import net.gbicc.app.pfm.jpa.support.dto.EadsHesitateVisitRateDTO;
import net.gbicc.app.pfm.jpa.support.dto.EadsRetreatRateDTO;
import net.gbicc.app.pfm.jpa.support.dto.PfmDto;
import net.gbicc.app.pfm.jpa.support.enums.PfmEnum;
import net.gbicc.app.pfm.jpa.support.util.POIPfmUtil;
import net.gbicc.app.pfm.service.PfmBranchResultService;
import net.gbicc.app.pfm.service.PfmChannelResultService;
import net.gbicc.app.pfm.service.PfmDepaResultService;

/**
* 分公司绩效结果
*/
@Service
@Transactional
public class PfmBranchResultServiceImpl extends DaoServiceImpl<PfmBranchResultEntity, String, PfmBranchResultRepository> 
	implements PfmBranchResultService {
	
	private static final Logger LOG = LogManager.getLogger(PfmBranchResultServiceImpl.class);
	
	@Autowired
    private PfmDepaResultService pfmDepaResultService;
	@Autowired
    private PfmChannelResultService pfmChannelResultService;
	@Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SystemDictionaryService systemDictionaryService;
    @Autowired
    private IrrInfosysResultService irrInfosysResultService;

	@Override
	public Map<String, Map<String, Object>> readPrmHelpInfo() {
		Map<String, Map<String,Object>> returnMap = new HashMap<String,Map<String,Object>>();
		InputStream is = null;
		try {
			String path = "/" + PfmEnum.TEMPLATE_FOLDER_URL.getValue() + File.separator + PfmEnum.TEMPLATE_PFM_HELP_CODE.getValue() + PfmEnum.TEMPLATE_PFM_SUFFIX.getValue();
			is = getClass().getResourceAsStream(path);
			Workbook wb = WorkbookFactory.create(is);
			int sheetNum = wb.getNumberOfSheets();
			for(int i=0;i<sheetNum;i++) {
				Sheet sheet = wb.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				String pfmFlag = StringUtils.isNotBlank(sheetName) ? sheetName : judgeSheetFlag(i);
				Map<String, Object> pfmMap = new HashMap<String, Object>();
				Row titleRow = sheet.getRow(0);
				String[] dimTitle = getPfmSheetDimTitle(titleRow);
				pfmMap.put(PfmEnum.TEMPLATE_PFM_TITLE_DIM_FLAG.getValue(), dimTitle);
				int rows = sheet.getLastRowNum();
				List<PfmDto> pfmIndexInfoList = new ArrayList<PfmDto>();
				Object obj = null;
				for(int j=1;j<=rows;j++) {
					Row row = sheet.getRow(j);
					if(row.getLastCellNum() < 0) {
						throw new PfmException("绩效模板第"+j+1+"行不存在指标信息!");
					}
					int index = 1;
					PfmDto dto = new PfmDto();
					dto.setSortNum(j);
					obj = POIPfmUtil.getCellValue(row.getCell(index++));
					dto.setScoreIndexCode(obj == null ? null : obj.toString());
					obj = POIPfmUtil.getCellValue(row.getCell(index++));
					dto.setIndexName(obj == null ? null : obj.toString());
					obj = POIPfmUtil.getCellValue(row.getCell(index++));
					dto.setWeight(obj == null ? null : obj.toString());
					obj = POIPfmUtil.getCellValue(row.getCell(index));
					dto.setStanValue(obj == null ? null : obj.toString());
					pfmIndexInfoList.add(dto);
				}
				pfmMap.put(PfmEnum.TEMPLATE_PFM_INDEX_INFO_FLAG.getValue(), pfmIndexInfoList);
				returnMap.put(pfmFlag, pfmMap);
			}
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
				}
			}
		}
		return returnMap;
	}

	/**
	 * 判断绩效类型
	 * @param index sheet索引
	 * @return
	 */
	private String judgeSheetFlag(Integer index) {
		if(index == 0) {
			return PfmEnum.BRANCH_PFM_FLAG.getValue();
		}else if(index == 1) {
			return PfmEnum.HEAD1_PFM_FLAG.getValue();
		}else if(index == 2) {
			return PfmEnum.HEAD2_PFM_FLAG.getValue();
		}
		return null;
	}

	@Override
	public String[] getPfmSheetDimTitle(Row titleRow) throws Exception {
		if(titleRow == null) {
			return null;
		}
		int cells = titleRow.getLastCellNum();
		Integer startIndex = Integer.valueOf(PfmEnum.TEMPLATE_PFM_TITLE_DIM_INDEX_START.getValue());
		String[] returnStrArry = new String[cells-startIndex];
		int index = 0;
		for(int i=startIndex;i<cells;i++) {
			Object value = POIPfmUtil.getCellValue(titleRow.getCell(i));
			returnStrArry[index++] = value == null ? "" : value.toString();
		}
		return returnStrArry;
	}

	@Override
	public Workbook createPfmDataSuppWb(Workbook wb) {
		try {
			int sheetNum=wb.getNumberOfSheets();
			for(int i=0;i<sheetNum;i++) {
				String sheetName=wb.getSheetName(i);
				if(sheetName.contains(IrrTemplateEnum.IRR_SHEET_NAME.getValue())) {
					wb.getSheetAt(i).setForceFormulaRecalculation(true);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
		return wb;
	}

	@Override
	public void readPfmDataSupp(Workbook wb, String taskId,String taskBatch) throws Exception {
        //删除同期次绩效数据
        jdbcTemplate.execute("DELETE FROM T_PFM_BRANCH_RESULT WHERE TASK_ID='" + taskId + "'");
        jdbcTemplate.execute("DELETE FROM T_PFM_CHANNEL_RESULT WHERE TASK_ID='" + taskId + "'");
        jdbcTemplate.execute("DELETE FROM T_PFM_DEPA_RESULT WHERE TASK_ID='" + taskId + "'");
		int sheetNum=wb.getNumberOfSheets();
		for(int i=0;i<sheetNum;i++) {
			int rowNum;
			String sheetName=wb.getSheetName(i);
			Sheet sheet=wb.getSheetAt(i);
			if(sheetName.contains(IrrTemplateEnum.IRR_SHEET_NAME.getValue())) {
				if(sheetName.equals(PfmEnum.TEMPLATE_PFM_SHEET_BRANCH.getValue())) {//分公司绩效
					 rowNum=sheet.getLastRowNum();
					//第二行解析Excel
                    for (int j = 1; j <= rowNum; j++) {
						Row row=sheet.getRow(j);
						int cellNum=row.getLastCellNum();
						for(int k=4;k<cellNum;k++) {
							PfmBranchResultEntity entity=new PfmBranchResultEntity();
							Object obj=new Object();
							//获取第一行
							Row firstRow=sheet.getRow(0);
							obj=POIPfmUtil.getCellValue(firstRow.getCell(k));
							entity.setOrgName(obj==null?null:obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(k));
							entity.setIndexScore(obj==null?null:obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(1));
							entity.setIndexName(obj==null?null:obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(2));
							entity.setIndexWeight(obj==null?null:new BigDecimal(obj.toString()));
							obj=POIPfmUtil.getCellValue(row.getCell(3));
							entity.setStandValue(obj==null?null:new BigDecimal(obj.toString()));
							entity.setTaskId(taskId);
							entity.setTaskBatch(taskBatch);
                            entity.setSortNum(j);
							repository.save(entity);
						}			
					}
				}if(sheetName.equals(PfmEnum.TEMPLATE_PFM_SHEET_DEPA.getValue())) {//总公司绩效-I
					rowNum=sheet.getLastRowNum();
					//第二行解析
                    for (int j = 1; j <= rowNum; j++) {
						Row row=sheet.getRow(j);
						int cellNum=row.getLastCellNum();
						for(int k=4;k<cellNum;k++) {
							Object obj=new Object();
							PfmDepaResultEntity entity=new PfmDepaResultEntity();
							//获取第一行
							Row firstRow=sheet.getRow(0);
							obj=POIPfmUtil.getCellValue(firstRow.getCell(k));
							entity.setOrgName(obj==null?null:obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(k));
                            entity.setIndexScore(obj == null ? null : obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(1));
							entity.setIndexName(obj==null?null:obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(2));
                            entity.setIndexWeight(
                                    IrrUtil.strObjectIsEmpty(obj) ? null : new BigDecimal(obj.toString()));
							obj=POIPfmUtil.getCellValue(row.getCell(3));
                            entity.setStandValue(IrrUtil.strObjectIsEmpty(obj) ? null : new BigDecimal(obj.toString()));
							entity.setTaskId(taskId);
							entity.setTaskBatch(taskBatch);
                            entity.setSortNum(j);
							pfmDepaResultService.getRepository().save(entity);
						}
					}
				}if(sheetName.equals(PfmEnum.TEMPLATE_PFM_SHEET_CHANNEL.getValue())) {//总公司绩效-II
					rowNum=sheet.getLastRowNum();
					//第二行解析
                    for (int j = 1; j <= rowNum; j++) {
						Row row=sheet.getRow(j);
						int cellNum=row.getLastCellNum();						
                        for (int k = 4; k < cellNum; k++) {
							PfmChannelResultEntity entity=new PfmChannelResultEntity();
							Object obj=new Object();
							//第一行
							Row firstRow=sheet.getRow(0);
							obj=POIPfmUtil.getCellValue(firstRow.getCell(k));
							entity.setChannelName(obj==null?null:obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(1));
							entity.setIndexName(obj==null?null:obj.toString());
                            obj = POIPfmUtil.getCellValue(row.getCell(k));
                            entity.setIndexScore(obj == null ? null : obj.toString());
							obj=POIPfmUtil.getCellValue(row.getCell(2));
                            entity.setIndexWeight(
                                    IrrUtil.strObjectIsEmpty(obj) ? null : new BigDecimal(obj.toString()));
							obj=POIPfmUtil.getCellValue(row.getCell(3));
                            entity.setStandValue(IrrUtil.strObjectIsEmpty(obj) ? null : new BigDecimal(obj.toString()));
							entity.setTaskId(taskId);
							entity.setTaskBatch(taskBatch);
                            entity.setSortNum(j);
							pfmChannelResultService.getRepository().save(entity);
						}
					}					
				}
            }
		}
	
	}

	@Override
    public Map<String, Object> queryBranchResult(String taskId, String indexCode, String indexName, Long page,
            Integer size) {
        String sql = "SELECT PBR.SORT_NUM,PBR.TASK_BATCH,PBR.INDEX_NAME AS indexName,"
                + "MAX((CASE PBR.ORG_NAME WHEN '北京' THEN PBR.INDEX_SCORE END)) AS BJ,"
                + "MAX((CASE PBR.ORG_NAME WHEN '天津' THEN PBR.INDEX_SCORE END)) AS TJ,"
                + "MAX((CASE PBR.ORG_NAME WHEN '辽宁' THEN PBR.INDEX_SCORE END)) AS LN,"
                + "MAX((CASE PBR.ORG_NAME WHEN '大连' THEN PBR.INDEX_SCORE END)) AS DL,"
                + "MAX((CASE PBR.ORG_NAME WHEN '江苏' THEN PBR.INDEX_SCORE END)) AS JS,"
                + "MAX((CASE PBR.ORG_NAME WHEN '山东' THEN PBR.INDEX_SCORE END)) AS SD,"
                + "MAX((CASE PBR.ORG_NAME WHEN '青岛' THEN PBR.INDEX_SCORE END)) AS QD,"
                + "MAX((CASE PBR.ORG_NAME WHEN '河南' THEN PBR.INDEX_SCORE END)) AS HN,"
                + "MAX((CASE PBR.ORG_NAME WHEN '广东' THEN PBR.INDEX_SCORE END)) AS GD,"
                + "MAX((CASE PBR.ORG_NAME WHEN '四川' THEN PBR.INDEX_SCORE END)) AS SC "
                + "FROM T_PFM_BRANCH_RESULT PBR WHERE PBR.INDEX_NAME IS NOT NULL ";
		if(StringUtils.isNotBlank(taskId)) {
            sql += "AND PBR.TASK_ID = '" + taskId + "' ";
		}
		if(StringUtils.isNotBlank(indexCode)) {
            sql += "AND PBR.INDEX_CODE LIKE '%" + indexCode + "%' ";
		}
		if(StringUtils.isNotBlank(indexName)) {
            sql += "AND PBR.INDEX_NAME LIKE '%" + indexName + "%' ";
		}
        sql += "GROUP BY PBR.INDEX_NAME,PBR.SORT_NUM,PBR.TASK_BATCH ORDER BY PBR.SORT_NUM";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		ResponsePage<Map<String, Object>> response=new ResponsePage<Map<String,Object>>();
        response.setSize(size);
        response.setNumber(page);
		response.setAllData(list);
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("response", response.getPageData());
		return map;
	}

	@Override
    public String updateIndexInfo(String indexName, String channelName, Map<String, String> excludeBranchMap)
            throws Exception {
	    if(StringUtils.isBlank(indexName)) {
            return "";
	    }
		String sql="";
		String rate="";
        //不进行上报的分公司在计算的时候要过滤掉
        if (PfmEnum.EMPL_TURN_RATE_NAME.getValue().equals(indexName)) {
            sql = "SELECT * FROM E_ADS_EMPLOYEE_LOSS WHERE CHANNEL='" + channelName + "' AND SCORE IS NOT NULL";
			List<EadsEmployeeLossDTO> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<EadsEmployeeLossDTO>(EadsEmployeeLossDTO.class));
            if (CollectionUtils.isEmpty(list)) {
                return rate;
            }
            Double totalScore = 0d;
            int totalNum = list.size();
            for (EadsEmployeeLossDTO dto : list) {
                if (dto == null || StringUtils.isNotBlank(excludeBranchMap.get(dto.getOrgName()))) {
                    totalNum--;
                    continue;
                }
                totalScore += dto.getScore();
            }
            rate = String.valueOf(totalScore / totalNum);
		}
        if (PfmEnum.HESI_TELE_VISI_SUCCESS_NAME.getValue().equals(indexName)) {
            sql = "SELECT * FROM E_ADS_HESITATE_VISIT_RATE WHERE CHANNEL='" + channelName + "' AND SCORE IS NOT NULL";
        	List<EadsHesitateVisitRateDTO> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<EadsHesitateVisitRateDTO>(EadsHesitateVisitRateDTO.class));
            if (CollectionUtils.isEmpty(list)) {
                return rate;
			}
            Double totalScore = 0d;
            int totalNum = list.size();
            for (EadsHesitateVisitRateDTO dto : list) {
                if (dto == null || StringUtils.isNotBlank(excludeBranchMap.get(dto.getOrgName()))) {
                    totalNum--;
                    continue;
                }
                totalScore += dto.getScore();
            }
            rate = String.valueOf(totalScore / totalNum);
		}
        if (PfmEnum.NEW_CONT_VISI_COMP_NAME.getValue().equals(indexName)) {
            sql = "SELECT * FROM E_ADS_CONTRACT_VISIT_RATE WHERE CHANNEL='" + channelName + "' AND SCORE IS NOT NULL";
        	List<EadsContractVisitRateDTO> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<EadsContractVisitRateDTO>(EadsContractVisitRateDTO.class));
            if (CollectionUtils.isEmpty(list)) {
                return rate;
            }
            Double totalScore = 0d;
            int totalNum = list.size();
            for (EadsContractVisitRateDTO dto : list) {
                if (dto == null || StringUtils.isNotBlank(excludeBranchMap.get(dto.getOrgName()))) {
                    totalNum--;
                    continue;
                }
                totalScore += dto.getScore();
            }
            rate = String.valueOf(totalScore / totalNum);
        }
        if (PfmEnum.RENE_RATE_NAME.getValue().equals(indexName)) {
            sql = "SELECT * FROM E_ADS_CONTINUE_CHARGE_RATE WHERE CHANNEL='" + channelName + "' AND SCORE IS NOT NULL";
        	List<EadsContinueChargeRateDTO> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<EadsContinueChargeRateDTO>(EadsContinueChargeRateDTO.class));
            if (CollectionUtils.isEmpty(list)) {
                return rate;
            }
            Double totalScore = 0d;
            int totalNum = list.size();
            for (EadsContinueChargeRateDTO dto : list) {
                if (dto == null || StringUtils.isNotBlank(excludeBranchMap.get(dto.getSecOrg()))) {
                    totalNum--;
                    continue;
                }
                totalScore += dto.getScore();
            }
            rate = String.valueOf(totalScore / totalNum);
        }
        if (PfmEnum.RETU_WDW_INSU_RATE_NAME.getValue().equals(indexName)) {
            sql = "SELECT * FROM E_ADS_RETREAT_RATE WHERE CHANNEL='" + channelName + "' AND SCORE IS NOT NULL";
        	List<EadsRetreatRateDTO> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<EadsRetreatRateDTO>(EadsRetreatRateDTO.class));
            if (CollectionUtils.isEmpty(list)) {
                return rate;
            }
            Double totalScore = 0d;
            int totalNum = list.size();
            for (EadsRetreatRateDTO dto : list) {
                if (dto == null || StringUtils.isNotBlank(excludeBranchMap.get(dto.getBranchCom()))) {
                    totalNum--;
                    continue;
                }
                totalScore += dto.getScore();
            }
            rate = String.valueOf(totalScore / totalNum);
        }
        return rate;
	}

    @Override
    public Map<String, Object> findPfmResult(String id, Long page, Integer size) throws Exception {
        List<Map<String, Object>> datalist = findPfmResult(id);
        if (CollectionUtils.isEmpty(datalist)) {
            return null;
        }
        Map<String, Integer> sortMap = new HashMap<String, Integer>();
        sortMap.put("投资部", 0);
        sortMap.put("精算部", 1);
        sortMap.put("财务管理部", 2);
        sortMap.put("会计运营部", 3);
        sortMap.put("IT", 4);
        sortMap.put("客服", 5);
        sortMap.put("个险", 6);
        sortMap.put("团险", 7);
        sortMap.put("银保", 8);
        sortMap.put("多元", 9);
        sortMap.put("收展", 10);
        sortMap.put("北京", 11);
        sortMap.put("天津", 12);
        sortMap.put("青岛", 13);
        sortMap.put("山东", 14);
        sortMap.put("江苏", 15);
        sortMap.put("辽宁", 16);
        sortMap.put("四川", 17);
        sortMap.put("河南", 18);
        sortMap.put("大连", 19);
        sortMap.put("广东", 20);
        List<Map<String, Object>> list = Arrays.asList(new Map[datalist.size()]);
        for (Map<String, Object> temp : datalist) {
            Object orgName = temp.get("NAME");
            if (orgName == null || "".equals(orgName.toString().trim())) {
                continue;
            }
            list.set(sortMap.get(orgName), temp);
        }
        ResponsePage<Map<String, Object>> response = new ResponsePage<Map<String, Object>>();
        response.setAllData(list);
        response.setNumber(page);
        response.setSize(size);
        Map<String, Object> map = IrrUtil.getMap(true);
        map.put("response", response.getPageData());
        return map;
    }

    @Override
    public Workbook setOtherData(IrrTaskEntity task, Workbook workbook) throws Exception {
        if (task == null || workbook == null) {
            return null;
        }
        //设置信息系统
        String infoSysTotalScore = irrInfosysResultService.findIndexValueByCondition(task.getTaskBatch(),
                PfmEnum.INFO_SYS_TOTAL_SCORE.getValue());
        if (StringUtils.isNotBlank(infoSysTotalScore)) {
            Sheet head1PfmSheet = workbook.getSheet(PfmEnum.TEMPLATE_PFM_SHEET_DEPA.getValue());
            if (head1PfmSheet != null) {
                Row infoSysRow = head1PfmSheet.getRow(124);
                if (infoSysRow != null) {
                    Cell infoSysCell = infoSysRow.getCell(8);
                    if (infoSysCell != null) {
                        infoSysCell.setCellValue(Double.valueOf(infoSysTotalScore));
                    }
                }
            }
        }
        //设置五大指标
        Sheet head2PfmSheet = workbook.getSheet(PfmEnum.TEMPLATE_PFM_SHEET_CHANNEL.getValue());
        if (head2PfmSheet == null) {
            throw new PfmException("无法补录：没有找到总公司绩效-II的表格！");
        }
        //获取渠道映射
        Map<String, String> channelMap = systemDictionaryService.getDictionaryMap(IrrEnum.CHANNEL_FLAG.getValue(),
                Locale.CHINA);
        if (MapUtils.isEmpty(channelMap)) {
            throw new PfmException("无法补录绩效：数据字典中没有配置渠道！");
        }
        Map<String, String> channelNameMap = new HashMap<String, String>();
        for (String key : channelMap.keySet()) {
            channelNameMap.put(channelMap.get(key), key);
        }
        //获取不上报的分公司
        Map<String, String> excludeBranchMap = systemDictionaryService
                .getDictionaryMap(IrrEnum.BRANCH_XBRL_ORG_EXCLUDE.getValue(), Locale.CHINA);
        if (MapUtils.isEmpty(excludeBranchMap)) {
            throw new PfmException("无法补录绩效：数据字典中没有配置不上报的分公司！");
        }
        Row head2FirstRow = head2PfmSheet.getRow(0);
        //员工流失率
        setFiveIndex(head2PfmSheet, head2FirstRow, 16, PfmEnum.EMPL_TURN_RATE_NAME.getValue(), channelNameMap,
                excludeBranchMap);
        //犹豫期内电话回访成功率
        setFiveIndex(head2PfmSheet, head2FirstRow, 26, PfmEnum.HESI_TELE_VISI_SUCCESS_NAME.getValue(), channelNameMap,
                excludeBranchMap);
        //新契约回访完成率
        setFiveIndex(head2PfmSheet, head2FirstRow, 27, PfmEnum.NEW_CONT_VISI_COMP_NAME.getValue(), channelNameMap,
                excludeBranchMap);
        //续期收费率
        setFiveIndex(head2PfmSheet, head2FirstRow, 29, PfmEnum.RENE_RATE_NAME.getValue(), channelNameMap,
                excludeBranchMap);
        //退（撤）保率
        setFiveIndex(head2PfmSheet, head2FirstRow, 31, PfmEnum.RETU_WDW_INSU_RATE_NAME.getValue(), channelNameMap,
                excludeBranchMap);
        return workbook;
    }

    /**
     * 设置五大指标
     *
     * @param head2PfmSheet
     *            表格
     * @param head2FirstRow
     *            标题行
     * @param rownum
     *            指标行索引
     * @param indexName
     *            指标名称
     * @param channelMap
     *            渠道映射
     * @param excluedBranchMap
     *            不计算的分公司map
     */
    private void setFiveIndex(Sheet head2PfmSheet, Row head2FirstRow, int rownum, String indexName,
            Map<String, String> channelMap, Map<String, String> excluedBranchMap) throws Exception {
        if (head2PfmSheet == null || head2FirstRow == null) {
            return;
        }
        Row row = head2PfmSheet.getRow(rownum);
        if (row != null) {
            for (int i = 4; i < row.getLastCellNum(); i++) {
                Object channelObj = POIPfmUtil.getCellValue(head2FirstRow.getCell(i));
                if (IrrUtil.strObjectIsEmpty(channelObj) || StringUtils.isBlank(channelMap.get(channelObj))) {
                    continue;
                }
                Cell channelCell = row.getCell(i);
                if (channelCell == null) {
                    continue;
                }
                String value = updateIndexInfo(indexName, channelMap.get(channelObj), excluedBranchMap);
                if (StringUtils.isNotBlank(value)) {
                    channelCell.setCellValue(Double.valueOf(value));
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> findPfmResult(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        String sql = "SELECT ORG_NAME NAME,ROUND(INDEX_SCORE,2) SCORE FROM T_PFM_BRANCH_RESULT WHERE INDEX_NAME='绩效得分' AND TASK_ID='"
                + id + "'\r\n" + "UNION\r\n"
                + "SELECT CHANNEL_NAME NAME,ROUND(INDEX_SCORE,2) SCORE FROM T_PFM_CHANNEL_RESULT WHERE INDEX_NAME='绩效得分' AND TASK_ID='"
                + id + "'\r\n" + "UNION\r\n"
                + "SELECT ORG_NAME NAME,ROUND(INDEX_SCORE,2) SCORE FROM T_PFM_DEPA_RESULT WHERE INDEX_NAME='绩效得分' AND TASK_ID='"
                + id + "'";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public void downloadPfmScore(String id,HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        OutputStream os = null;
        try {
            List<Map<String, Object>> list = findPfmResult(id);
            Sheet sheet = wb.createSheet(PfmEnum.PFM_RESULT_SCORE.getValue());
            os = response.getOutputStream();
            String fileName = PfmEnum.PFM_RESULT_SCORE.getValue() + PfmEnum.TEMPLATE_PFM_SUFFIX.getValue();
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            if (CollectionUtils.isEmpty(list)) {
                Row tipRow = sheet.createRow(0);
                Cell tipCell = tipRow.createCell(0);
                tipCell.setCellValue(PfmEnum.PFM_RESULT_NULL_TIP.getValue());
                wb.write(os);
                return;
            }
            CellStyle titleCellStyle = POIUtil.createBorderFontCellStyle(wb, BorderStyle.THIN, "微软雅黑",
                    Font.COLOR_NORMAL, true);
            CellStyle contentCellStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
            //设置标题
            String[] titleArr = PfmEnum.PFM_RESULT_SCORE_TITLE.getValue().split(IrrEnum.SEPARATOR.getValue());
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < titleArr.length; i++) {
                Cell titleCell = titleRow.createCell(i);
                titleCell.setCellStyle(titleCellStyle);
                titleCell.setCellValue(titleArr[i]);
            }
            //设置内容
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                if (MapUtils.isEmpty(map)) {
                    continue;
                }
                Object nameObject = map.get("NAME");
                Object scoreObject = map.get("SCORE");
                if (IrrUtil.strObjectIsEmpty(nameObject) || IrrUtil.strObjectIsEmpty(scoreObject)) {
                    continue;
                }
                Row row = sheet.createRow(i + 1);
                int cellnum = 0;
                Cell cell = row.createCell(cellnum++);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(nameObject.toString());
                cell = row.createCell(cellnum);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(Double.valueOf(scoreObject.toString()));
            }
            wb.write(os);
        } catch (Exception e) {
            throw e;
        } finally {
            if (wb != null) {
                wb.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
}
