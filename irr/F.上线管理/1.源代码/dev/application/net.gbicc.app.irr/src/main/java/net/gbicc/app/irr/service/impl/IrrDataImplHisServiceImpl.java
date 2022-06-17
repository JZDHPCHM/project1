package net.gbicc.app.irr.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wsp.framework.jpa.service.impl.DaoServiceImpl;
import org.wsp.framework.mvc.service.SystemDictionaryService;

import net.gbicc.app.irr.jpa.entity.IrrDataImplHisEntity;
import net.gbicc.app.irr.jpa.exception.IrrProcessException;
import net.gbicc.app.irr.jpa.repository.IrrDataImplHisRepository;
import net.gbicc.app.irr.jpa.support.ResponsePage;
import net.gbicc.app.irr.jpa.support.enums.IrrEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrProjTypeEnum;
import net.gbicc.app.irr.jpa.support.enums.IrrTemplateEnum;
import net.gbicc.app.irr.jpa.support.util.IrrUtil;
import net.gbicc.app.irr.jpa.support.util.POIUtil;
import net.gbicc.app.irr.service.IrrDataImplHisService;
import net.sf.json.JSONObject;

/**
*	irr历史数据导入
*/
@Service
public class IrrDataImplHisServiceImpl extends DaoServiceImpl<IrrDataImplHisEntity, String, IrrDataImplHisRepository>
		implements IrrDataImplHisService {
	
	private static final Logger LOG = LogManager.getLogger(IrrDataImplHisServiceImpl.class);
	@Autowired private SystemDictionaryService systemDictionaryService;
	@Autowired private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> readIrrHisDataImpl(MultipartFile file,String year,String quarter) throws Exception{
		if(file == null || file.isEmpty() || StringUtils.isBlank(year) || StringUtils.isBlank(quarter)) {
			return IrrUtil.getMap(false, "上传的文件为空或者没有选择年季!");
		}
		String taskBatch = IrrUtil.getFiveClassTaskBatch(year, quarter);
		jdbcTemplate.execute("DELETE FROM T_IRR_DATAIMPL_HIS WHERE TASK_BATCH='"+taskBatch+"'");
		BufferedInputStream bis = null;
		ZipInputStream zis = null;
		Workbook wb = null;
		try {
			zis = new ZipInputStream(file.getInputStream(),Charset.forName(IrrEnum.ZIP_INPUTSTREAM_CHARSET.getValue()));
			bis = new BufferedInputStream(zis);
			ZipEntry ze = null;
			List<IrrDataImplHisEntity> saveList = new ArrayList<IrrDataImplHisEntity>();
			while((ze = zis.getNextEntry()) != null) {
				if(ze.isDirectory()) {
					continue;
				}
				int size = (int)ze.getSize();
				byte[] cache = new byte[size];
				bis.read(cache, 0, size);
				wb = WorkbookFactory.create(new ByteArrayInputStream(cache));
				List<IrrDataImplHisEntity> temp = poiReadExcel(ze.getName(),taskBatch,wb);
				wb.close();
				if(CollectionUtils.isNotEmpty(temp)) {
					saveList.addAll(temp);
				}
			}
			add(saveList);
		}catch(Exception e) {
			throw e;
		}finally {
			if(bis != null) {
				bis.close();
			}
			if(zis != null) {
				zis.close();
			}
			if(wb != null) {
				wb.close();
			}
		}
		return IrrUtil.getMap(true,"上传成功!");
	}

	@Override
	public List<IrrDataImplHisEntity> poiReadExcel(String fileName,String taskBatch,Workbook wb) throws Exception {
		if(StringUtils.isBlank(fileName) || wb == null) {
			return null;
		}
		String[] info = fileName.split(IrrEnum.SEPARATOR.getValue());
		if(info == null || info.length < 2) {
			return null;
		}
		int sheetNum = wb.getNumberOfSheets();
		if(sheetNum <= 0) {
			return null;
		}
		List<IrrDataImplHisEntity> returnList = new ArrayList<IrrDataImplHisEntity>();
		for(int i=0;i<sheetNum;i++) {
			Sheet sheet = wb.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			if(StringUtils.isBlank(sheetName) || sheetName.contains(IrrTemplateEnum.TEMPLATE_DIRECTORY_SHEETNAME.getValue())) {
				continue;
			}
			String[] typeInfo = sheetName.split(IrrEnum.SEPARATOR.getValue());
			if(typeInfo == null || typeInfo.length < 2) {
				continue;
			}
			int lastRowNum = sheet.getLastRowNum();
			if(lastRowNum <= 0) {
				continue;
			}
			int rowNum = 3;
			if(sheetName.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
				//封面页读取的时候从1开始
				rowNum = 1;
			}
			for(;rowNum <= lastRowNum;rowNum ++) {
				Row row = sheet.getRow(rowNum);
				if(POIUtil.readIsOver(row)) {
					break;
				}
				IrrDataImplHisEntity dataImplHis = new IrrDataImplHisEntity();
				dataImplHis.setTaskBatch(taskBatch);
				dataImplHis.setProjTypeCode(typeInfo[0]);
				dataImplHis.setProjTypeName(typeInfo[1]);
				dataImplHis.setXbrlCode(info[0]);
				dataImplHis.setXbrlName(info[1]);
				Object obj = null;
				int cellnum = 0;
				if(sheetName.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
					dataImplHis.setIndexLine(Double.valueOf(rowNum));
				}else {
					obj=POIUtil.getCellValue(row.getCell(cellnum++));
					System.out.println(sheetName + (IrrUtil.strObjectIsEmpty(obj) ? null : obj.toString()));
					dataImplHis.setIndexLine(Double.valueOf(IrrUtil.strObjectIsEmpty(obj) ? null : obj.toString()));
				}
				dataImplHis.setIndexName(IrrUtil.strObjectIsEmpty(obj=POIUtil.getCellValue(row.getCell(cellnum++))) ? null : obj.toString());
				dataImplHis.setIndexResult(IrrUtil.strObjectIsEmpty(obj=POIUtil.getCellValue(row.getCell(cellnum++))) ? null : obj.toString());
				returnList.add(dataImplHis);
			}
		}
		return returnList;
	}

	@Override
	public Map<String, Object> getHisDataOrg() throws Exception {
        String sql = "SELECT DISTINCT XBRL_CODE,XBRL_NAME FROM T_IRR_DATAIMPL_HIS ORDER BY XBRL_CODE";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
        Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		for(Map<String, Object> map : list) {
			returnMap.put(map.get("XBRL_CODE").toString(), map.get("XBRL_NAME"));
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> getHisDataTaskBatch() throws Exception {
		String sql = "SELECT DISTINCT TASK_BATCH FROM T_IRR_DATAIMPL_HIS ORDER BY TASK_BATCH DESC ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
        Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		for(Map<String, Object> map : list) {
			returnMap.put(map.get("TASK_BATCH").toString(), map.get("TASK_BATCH"));
		}
		return returnMap;
	}

	@Override
	public Map<String, Workbook> createReportAllExcel(String taskBatch) throws Exception {
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
				String xbrlOrgInfo = orgXBRL.get(str);
				if(StringUtils.isBlank(xbrlOrgInfo)) {
					continue;
				}
				String[] xbrlInfoArr = xbrlOrgInfo.split(IrrEnum.SEPARATOR.getValue());
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
					List<IrrDataImplHisEntity> resultList = findHisDataByCondhition(taskBatch, xbrlInfoArr[0], projCode);
					if(CollectionUtils.isEmpty(resultList)) {
						continue;
					}
					//开始行
					int rowNum = 3;
					//封面页
					if(StringUtils.isNotBlank(projCode) && projCode.contains(IrrProjTypeEnum.FM_CODE.getValue())) {
						rowNum = 1;
						for(IrrDataImplHisEntity dto : resultList) {
							Row row = sheet.createRow(rowNum++);
							int colNum = 0;
							Cell nameCell = row.createCell(colNum++);
							nameCell.setCellStyle(cellBorderStyle);
							nameCell.setCellValue(dto.getIndexName());
							Cell valueCell = row.createCell(colNum);
							valueCell.setCellStyle(cellBorderStyle);
							valueCell.setCellValue(dto.getIndexResult());
						}
						continue;
					}
					//非封面页
					for(IrrDataImplHisEntity dto : resultList) {
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
						valueCell.setCellValue(dto.getIndexResult());
					}
				}
				wbMap.put(xbrlOrgInfo, wb);
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
	public List<IrrDataImplHisEntity> findHisDataByCondhition(String taskBatch, String xbrlCode, String projTypeCode)
			throws Exception {
		StringBuffer sb = new StringBuffer("SELECT HIS.* FROM T_IRR_DATAIMPL_HIS HIS WHERE 1=1 ");
		if(StringUtils.isNotBlank(taskBatch)) {
			sb.append("AND HIS.TASK_BATCH='"+taskBatch+"' ");
		}
		if(StringUtils.isNotBlank(xbrlCode)) {
			sb.append("AND HIS.XBRL_CODE='"+xbrlCode+"' ");
		}
		if(StringUtils.isNotBlank(projTypeCode)) {
			sb.append("AND HIS.PROJ_TYPE_CODE='"+projTypeCode+"' ");
		}
		sb.append("ORDER BY HIS.INDEX_LINE ");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<IrrDataImplHisEntity>(IrrDataImplHisEntity.class));
	}

    @Override
    public Map<String, Object> findHisData(String data, Long page, Integer size) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM T_IRR_DATAIMPL_HIS HIS WHERE 1=1\r\n");
        if (StringUtils.isNotBlank(data)) {
            JSONObject json = JSONObject.fromObject(data);
            if (json.has("xbrlCode") && StringUtils.isNotBlank(json.getString("xbrlCode"))) {
                sb.append("AND HIS.XBRL_CODE='" + json.getString("xbrlCode") + "'\r\n");
            }
            if (json.has("taskBatch") && StringUtils.isNotBlank(json.getString("taskBatch"))) {
                sb.append("AND HIS.TASK_BATCH='" + json.getString("taskBatch") + "'\r\n");
            }
            if (json.has("indexName") && StringUtils.isNotBlank(json.getString("indexName"))) {
                sb.append("AND HIS.INDEX_NAME LIKE '%" + json.getString("indexName") + "%'\r\n");
            }
            if (json.has("projTypeCode") && StringUtils.isNotBlank(json.getString("projTypeCode"))) {
                sb.append("AND HIS.PROJ_TYPE_CODE='" + json.getString("projTypeCode") + "'\r\n");
            }
            sb.append("ORDER BY HIS.INDEX_LINE");
        } else {
            sb.append("ORDER BY HIS.PROJ_TYPE_CODE,HIS.INDEX_LINE,HIS.XBRL_CODE");
        }
        List<IrrDataImplHisEntity> list = jdbcTemplate.query(sb.toString(),
                new BeanPropertyRowMapper<IrrDataImplHisEntity>(IrrDataImplHisEntity.class));
        ResponsePage<IrrDataImplHisEntity> response = new ResponsePage<IrrDataImplHisEntity>();
        response.setAllData(list);
        response.setNumber(page);
        response.setSize(size);
        Map<String, Object> map = IrrUtil.getMap(true);
        map.put("response", response.getPageData());
        return map;
    }
	
}
