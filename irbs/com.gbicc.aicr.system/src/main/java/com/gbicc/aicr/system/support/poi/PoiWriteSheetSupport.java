package com.gbicc.aicr.system.support.poi;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gbicc.aicr.system.support.dto.BeanMethodDTO;
import com.gbicc.aicr.system.support.enums.PoiEnum;
import com.gbicc.aicr.system.support.poi.util.PoiSupportUtil;
import com.gbicc.aicr.system.util.AppUtil;

/**
 * poi写表数据支持类
 * 
 * @author FC
 * @version v1.0 2019年8月19日
 */
@Component
public class PoiWriteSheetSupport<T> {

    /**
     * 写入表格数据
     * 
     * @param clazz
     *            对应表格的class类
     * @param list
     *            数据
     * @param sheet
     *            写入的表格
     * @param wirteFirstRowIndex
     *            写的第一行索引
     * @param cellStyle
     *            单元格样式
     */
    public void writeSheet(Class clazz, List<Map<String, Object>> list, Sheet sheet, Integer wirteFirstRowIndex,
            CellStyle cellStyle) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<Integer, BeanMethodDTO> indexToMethodMap = PoiSupportUtil.indexToMethod(clazz,
                PoiEnum.GET_METHOD_PREFIX.getValue());
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.getRow(wirteFirstRowIndex++);
            if (row == null) {
                row = sheet.createRow(wirteFirstRowIndex++);
            }
            writeRow(list.get(i), row, indexToMethodMap, cellStyle);
        }
    }

    /**
     * 写入行的数据
     * 
     * @param map
     *            需写入的数据
     * @param row
     *            需写数据的表格行
     * @param indexToMethodMap
     *            列索引和方法映射
     * @param cellStyle
     *            单元格样式
     */
    public void writeRow(Map<String, Object> map, Row row, Map<Integer, BeanMethodDTO> indexToMethodMap,
            CellStyle cellStyle) {
        if (MapUtils.isEmpty(indexToMethodMap)) {
            throw new RuntimeException("no cell index");
        }
        for (Integer key : indexToMethodMap.keySet()) {
            BeanMethodDTO dto = indexToMethodMap.get(key);
            if (dto == null) {
                continue;
            }
            Cell cell = row.getCell(key);
            if (cell == null) {
                cell = row.createCell(key);
            }
            cell.setCellStyle(cellStyle);
            try {
                Object value = map.get(dto.getColumnName());
                if (AppUtil.strObjectIsEmpty(value) || PoiEnum.NO_WRITE_VALUE.getValue().equals(value)) {
                    continue;
                }
                cell.setCellValue(value.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
