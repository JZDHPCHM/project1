package com.gbicc.aicr.system.support.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.gbicc.aicr.system.support.enums.PoiEnum;
import com.gbicc.aicr.system.util.AppUtil;
import com.gbicc.aicr.system.util.POIUtil;

/**
 * poi写excel支持类
 * 
 * @author FC
 * @version v1.0 2019年8月27日
 */
@Component
public class PoiWriteExcelSupport {

    private static final Logger LOG = LogManager.getLogger(PoiWriteExcelSupport.class);

    /**
     * 生成excel的workbook<br/>
     * excel只有一个表格数据
     *
     * @param data
     *            excel数据
     * @param title
     *            excel表头
     * @param indexMap
     *            数据映射索引
     * @param suffix
     *            扩展名
     * @return
     */
    public Workbook createWb(List<Map<String, Object>> data, String[] title, Map<String, Integer> indexMap,
            String ext) {
        if (title == null || title.length <= 0 || MapUtils.isEmpty(indexMap) || StringUtils.isBlank(ext)) {
            throw new RuntimeException("create wb param has null");
        }
        Workbook wb = null;
        if (ext.toUpperCase().contains(PoiEnum.XLS_SUFFIX.getValue().toUpperCase())) {//xls文档
            wb = new HSSFWorkbook();
        } else if (ext.toUpperCase().contains(PoiEnum.XLSX_SUFFIX.getValue().toUpperCase())) {//xlsx文档
            wb = new XSSFWorkbook();
        }else {
            throw new RuntimeException("don't create " + ext + " workbook");
        }
        Sheet sheet = wb.createSheet();
        try {
            if (CollectionUtils.isEmpty(data)) {
                Cell cell = sheet.createRow(0).createCell(0);
                cell.setCellValue(PoiEnum.DATA_NULL_TIPS.getValue());
                return wb;
            }
            CellStyle titleStyle = POIUtil.createBorderFontCellStyle(wb, BorderStyle.THIN,
                    PoiEnum.DEFAULT_FONT_NAME.getValue(), Font.COLOR_NORMAL, true);
            CellStyle contentStyle = POIUtil.createBorderCellStyle(wb, BorderStyle.THIN);
            int rownum = 0;
            //写入标题
            Row titleRow = sheet.createRow(rownum++);
            for (int i=0;i<title.length;i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(title[i]);
            }
            //写入数据
            for (Map<String, Object> map : data) {
                Row row = sheet.createRow(rownum++);
                for (String key : map.keySet()) {
                    Integer index = indexMap.get(key);
                    if (index == null) {
                        continue;
                    }
                    Cell cell = row.createCell(index);
                    cell.setCellStyle(contentStyle);
                    Object value = map.get(key);
                    cell.setCellValue(AppUtil.strObjectIsEmpty(value) ? null : value.toString());
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            LOG.error(e);
        }finally {
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error(e);
            }
        }
        return wb;
    }

    /**
     * 下载excel<br/>
     *
     * excel只有一个表格数据
     *
     * @param data
     *            excel数据
     * @param title
     *            excel表头
     * @param indexMap
     *            数据映射索引
     * @param suffix
     *            扩展名
     * @param excelName
     *            excel名字
     * @param response
     */
    public void downloadExcel(List<Map<String, Object>> data, String[] title, Map<String, Integer> indexMap,
            String ext, String excelName, HttpServletResponse response) {
        if(StringUtils.isBlank(excelName)) {
            throw new RuntimeException("param excelName is null");
        }
        Workbook wb = createWb(data, title, indexMap, ext);
        OutputStream os = null;
        try {
            excelName = new String(excelName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + excelName);
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e);
        }finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error(e);
            }
        }
    }
}
