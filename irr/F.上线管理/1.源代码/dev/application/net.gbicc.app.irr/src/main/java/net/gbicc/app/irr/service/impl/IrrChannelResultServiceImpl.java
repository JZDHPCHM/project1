package net.gbicc.app.irr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.jpa.model.org.entity.Org;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import net.gbicc.app.irr.jpa.entity.IrrChannelResultEntity;
import net.gbicc.app.irr.jpa.entity.IrrIndexInfoEntity;
import net.gbicc.app.irr.jpa.entity.IrrProjTypeEntity;
import net.gbicc.app.irr.jpa.entity.IrrTaskEntity;
import net.gbicc.app.irr.jpa.repository.IrrChannelResultRepository;
import net.gbicc.app.irr.jpa.support.dto.IndexValueDTO;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrChannelResultService;
import net.gbicc.app.irr.service.IrrIndexInfoService;
import net.gbicc.app.irr.service.IrrSplitIndexService;

/**
*	渠道结果
*/
@Service
public class IrrChannelResultServiceImpl extends DaoServiceImpl<IrrChannelResultEntity, String, IrrChannelResultRepository> 
	implements IrrChannelResultService {

	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private IrrIndexInfoService irrIndexInfoService;
	@Autowired private SystemDictionaryService systemDictionaryService;
	@Autowired private IrrSplitIndexService irrSplitIndexService;
	
	@Override
	public List<IrrChannelResultEntity> saveHeadChannelResult(String taskId,Org org) throws Exception {
		String sql = "SELECT DISTINCT II.ID INDEX_ID,II.INDEX_CODE,II.INDEX_NAME,SI.CHANNEL_FLAG,UR.TASK_BATCH,UR.TASK_ID,UR.SPLIT_RESULT_Q2 INDEX_RESULT,'"+org.getId()+"' ORG_ID,'"+org.getName()+"' ORG_NAME,'"+org.getCode()+"' ORG_CODE\r\n" + 
				"FROM T_IRR_UPLOAD_RESULT UR INNER JOIN T_IRR_SPLIT_INDEX SI ON UR.SPLIT_ID=SI.ID INNER JOIN T_IRR_INDEX_INFO II ON SI.SOURCE_INDEX_ID=II.ID\r\n" + 
				"WHERE UR.TASK_ID='"+taskId+"' AND SI.SPLIT_LEVEL='"+IrrEnum.SPLIT_LEVEL_CHANNEL.getValue()+"' AND SI.IS_USE='"+IrrEnum.YESNO_Y.getValue()+"' " +
				"AND II.INDEX_STATUS='"+IrrEnum.INDEX_STATUS_ENABLE.getValue()+"' AND II.IS_APPLICABIE='"+IrrEnum.YESNO_Y.getValue()+"' " +
				"AND II.INDEX_LEVEL='"+IrrEnum.INDEX_LEVEL_HEAD.getValue()+"' AND II.IS_XBRL='"+IrrEnum.YESNO_Y.getValue()+"' ";
		List<IrrChannelResultEntity> channelResult = jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrChannelResultEntity>(IrrChannelResultEntity.class));
		channelResult = add(channelResult);
		return channelResult;
	}

	@Override
	public List<IndexValueDTO> sumBranchChannelIndexResult(String taskId,String orgId) {
		String sql = "SELECT CR.INDEX_ID,CR.INDEX_CODE CODE,CR.INDEX_NAME NAME,SUM(NVL(CR.INDEX_RESULT,0)) VALUE FROM T_IRR_CHANNEL_RESULT CR \r\n" + 
				"INNER JOIN T_IRR_INDEX_INFO II ON CR.INDEX_ID=II.ID WHERE II.INDEX_RESULT_TYPE='"+IrrEnum.INDEX_RESULT_TYPE_NUMBER.getValue()+"' AND CR.TASK_ID='"+taskId+"' AND CR.ORG_ID='"+orgId+"' GROUP BY CR.INDEX_ID,CR.INDEX_CODE,CR.INDEX_NAME";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IndexValueDTO>(IndexValueDTO.class));
	}

	@Override
	public List<IrrChannelResultEntity> calcChannelScoreIndex(IrrTaskEntity task) throws Exception {
		//查询有指标拆分渠道的大类
		List<IrrProjTypeEntity> projList = irrSplitIndexService.findProjBySplitIndexCondition(IrrEnum.SPLIT_LEVEL_CHANNEL.getValue(), false);
		if(CollectionUtils.isEmpty(projList)) {
			return null;
		}
		//渠道
		Map<String, String> channelFlagMap = systemDictionaryService.getDictionaryMap(IrrEnum.CHANNEL_FLAG.getValue(), Locale.CHINA);
		if(MapUtils.isEmpty(channelFlagMap)) {
			return null;
		}
		List<IrrChannelResultEntity> returnList = new ArrayList<IrrChannelResultEntity>();
		//计算渠道指标
		for(IrrProjTypeEntity proj : projList) {
			//查询评估项目得分指标
            List<IrrIndexInfoEntity> projIndexInfoList = irrIndexInfoService.findIndexInfoHaveFactor(proj.getId(),
                    IrrEnum.INDEX_STATUS_ENABLE.getValue(), IrrEnum.YESNO_Y.getValue(), IrrEnum.YESNO_Y.getValue());
			if(CollectionUtils.isEmpty(projIndexInfoList)) {
				continue;
			}
			for(String key : channelFlagMap.keySet()) {
				//查询因子结果
				List<IrrChannelResultEntity> channelResultList = findByCondition(task.getId(),proj.getId(),key);
				List<IndexValueDTO> relaIndexResult = new ArrayList<IndexValueDTO>();
				String orgId = null;
				String orgCode = null;
				String orgName = null;
				if(CollectionUtils.isNotEmpty(channelResultList)) {
					IrrChannelResultEntity infoChannelResult = channelResultList.get(0);
					orgId = infoChannelResult.getId();
					orgCode = infoChannelResult.getOrgCode();
					orgName = infoChannelResult.getOrgName();
					for(IrrChannelResultEntity channelResult : channelResultList) {
						IndexValueDTO indexValue = new IndexValueDTO();
						indexValue.setIndexId(channelResult.getIndexId());
						indexValue.setCode(channelResult.getIndexCode());
						indexValue.setName(channelResult.getIndexName());
						indexValue.setValue(channelResult.getIndexResult());
						relaIndexResult.add(indexValue);
					}
				}
				List<IndexValueDTO> resultList = irrIndexInfoService.summIndexNum(projIndexInfoList, relaIndexResult);
				if(CollectionUtils.isNotEmpty(resultList)) {
					for(IndexValueDTO dto : resultList) {
                        IrrChannelResultEntity channelResult = new IrrChannelResultEntity();
						channelResult.setIndexId(dto.getIndexId());
						channelResult.setIndexName(dto.getName());
						channelResult.setIndexCode(dto.getCode());
						channelResult.setIndexResult(dto.getValue());
						channelResult.setChannelFlag(key);
						channelResult.setTaskId(task.getId());
						channelResult.setTaskBatch(task.getTaskBatch());
						channelResult.setOrgId(orgId);
						channelResult.setOrgCode(orgCode);
						channelResult.setOrgName(orgName);
						returnList.add(channelResult);
					}
				}
			}
		}
        return add(returnList);
	}

	@Override
	public Workbook exportChannelIndexResult(IrrTaskEntity task,Workbook wb) throws Exception {
		if(task == null || wb == null) {
			return wb;
		}
		/*查询有指标拆分渠道的大类*/
		List<IrrProjTypeEntity> projList = irrSplitIndexService.findProjBySplitIndexCondition(IrrEnum.SPLIT_LEVEL_CHANNEL.getValue(), false);
		if(CollectionUtils.isEmpty(projList)) {
			return wb;
		}
		Map<String, IrrProjTypeEntity> projMap = new HashMap<String, IrrProjTypeEntity>();
		for(IrrProjTypeEntity proj : projList) {
			projMap.put(proj.getTypeCode(), proj);
		}
		/*渠道*/
		Map<String, String> channelFlagMap = systemDictionaryService.getDictionaryMap(IrrEnum.CHANNEL_FLAG.getValue(), Locale.CHINA);
		if(MapUtils.isEmpty(channelFlagMap)) {
			return wb;
		}
		/*获取到有多少个sheet*/
		int sheetNum = wb.getNumberOfSheets();
		/*单元格样式*/
		CellStyle cellBorderStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
		CellStyle titleCellStyle = POIUtil.createBorderFontCellStyle(wb, BorderStyle.THIN,IrrTemplateEnum.IRR_TEMPLATE_TITLE_FONT_NAME.getValue(),
				Font.COLOR_NORMAL, true);
		/*循环sheet进行赋值*/
		for(int i=1;i<sheetNum;i++) {
			//sheet名称格式为：评估项目编码+"-"+评估项目名称
			Sheet sheet = wb.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			String[] projInfo = sheetName.split("-");
			String projCode = projInfo[0];
			//评估项目是否分渠道
			if(StringUtils.isBlank(projCode) || projMap.get(projCode) == null) {
				continue;
			}
			IrrProjTypeEntity proj = projMap.get(projCode);
			//查询所有评估项目指标
			List<IrrIndexInfoEntity> projIndexList = irrIndexInfoService.getRepository().findByProjTypeIdOrderByIndexLine(proj.getId());
			if(CollectionUtils.isEmpty(projIndexList)) {
				continue;
			}
			//起始行、列开始写入
			Integer rowNum = 1;
			Integer colNum = null;
			String projName = projInfo[1];
			//判断是总公司还是分公司评估项目
			if(StringUtils.isNotBlank(projName) && IrrTemplateEnum.SUMMARY_FILE_BRANCH_PROJ_FIRST.getValue().equals(projName.substring(0, 1))) {
				//分公司评估项目,还没有此种情况
			}else {//总公司评估项目
				Row titleRow = sheet.getRow(rowNum);
				if(titleRow == null) {
					continue;
				}
                colNum = IrrTemplateEnum.SUMM_SHEET_TITLE.getValue().split(IrrEnum.SEPARATOR.getValue()).length + 3;
				//循环渠道设置值
				for(String key : channelFlagMap.keySet()) {
					int tempRow = rowNum;
					String channelName = channelFlagMap.get(key);
                    Row channelTitleRow = sheet.getRow(tempRow++);
                    Cell channelTitleCell = channelTitleRow.getCell(colNum);
                    if (channelTitleCell == null) {
                        channelTitleCell = channelTitleRow.createCell(colNum);
                    }
					channelTitleCell.setCellStyle(titleCellStyle);
					channelTitleCell.setCellValue(channelName);
					List<IrrChannelResultEntity> channelResultList = findByCondition(task.getId(),proj.getId(),key);
					if(CollectionUtils.isEmpty(channelResultList)) {
						continue;
					}
					Map<String, IrrChannelResultEntity> channelResultMap = new HashMap<String, IrrChannelResultEntity>();
					for(IrrChannelResultEntity channelResult : channelResultList) {
						channelResultMap.put(channelResult.getIndexId(), channelResult);
					}
					//设置指标值
					for(IrrIndexInfoEntity index : projIndexList) {
						IrrChannelResultEntity channelResult  = channelResultMap.get(index.getId());
						if(channelResult == null) {
							tempRow ++;
							continue;
						}
                        Row channelValueRow = sheet.getRow(tempRow++);
                        Cell channelCellValue = channelValueRow.getCell(colNum);
                        if (channelCellValue == null) {
                            channelCellValue = channelValueRow.createCell(colNum);
                        }
                        channelCellValue.setCellStyle(cellBorderStyle);
                        channelCellValue.setCellValue(channelResult.getIndexResult());
					}
					//换渠道位置
					colNum ++;
				}
			}
		}
		return wb;
	}

	@Override
	public List<IrrChannelResultEntity> findByCondition(String taskId, String projId, String channelFlag)
			throws Exception {
		String sql = "SELECT CR.* FROM T_IRR_CHANNEL_RESULT CR INNER JOIN T_IRR_INDEX_INFO II ON CR.INDEX_ID=II.ID WHERE 1=1 ";
		if(StringUtils.isNotBlank(taskId)) {
			sql += "AND CR.TASK_ID='"+taskId+"' ";
		}
		if(StringUtils.isNotBlank(projId)) {
			sql += "AND II.PROJ_TYPE_ID='"+projId+"' ";		
		}
		if(StringUtils.isNotBlank(channelFlag)) {
			sql += "AND CR.CHANNEL_FLAG='"+channelFlag+"' ";
		}
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<IrrChannelResultEntity>(IrrChannelResultEntity.class));
	}
}
