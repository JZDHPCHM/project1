package com.gbicc.aicr.system.support.poi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.gbicc.aicr.system.support.dto.BeanMethodDTO;
import com.gbicc.aicr.system.support.enums.PoiEnum;
import com.gbicc.aicr.system.support.poi.annotation.SheetInfo;
import com.gbicc.aicr.system.support.poi.util.PoiSupportUtil;
import com.gbicc.aicr.system.util.POIUtil;

/**
 * poi读取sheet支持类（通用）
 * 
 * @author FC
 * @version v1.0 2019年8月8日
 */
@Component
public class PoiReadSheetSupport<T> {

    /**
     * 读取excel的sheet页
     * 
     * @param clazz
     *            实体class
     * @param sheet
     *            sheet页
     * @param readFirstRowIndex
     *            读取的第一行索引
     * @param readFirstCellIndex
     *            读取第一列的索引
     * @return 返回实体集合
     */
    public List<T> readExcelSheet(Class<T> clazz, Sheet sheet, Integer readFirstRowIndex, Integer readFirstCellIndex) {
        if (clazz == null || sheet == null) {
            return null;
        }
        int readRirstRowNum = sheet.getFirstRowNum() + 1;
        if (readFirstRowIndex != null) {
            readRirstRowNum = readFirstRowIndex;
        }
        int lastRowNum = sheet.getLastRowNum();
        List<T> list = new ArrayList<T>();
        //获取映射
        Map<Integer, BeanMethodDTO> indexToMethodMap = PoiSupportUtil.indexToMethod(clazz,
                PoiEnum.SET_METHOD_PREFIX.getValue());
        for (int i = readRirstRowNum; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            //判断是否结束
            if (PoiSupportUtil.readIsOver(row, readFirstCellIndex)) {
                break;
            }
            T bean = readExcelRow(clazz, row, indexToMethodMap);
            if (bean != null) {
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 读取excel的sheet页
     * 
     * @param clazz
     *            实体class
     * @param sheet
     *            sheet页
     * @return 返回实体集合
     */
    public List<T> readExcelSheet(Class<T> clazz, Sheet sheet) {
        if (clazz == null || sheet == null) {
            return null;
        }
        int readRirstRowNum = sheet.getFirstRowNum() + 1;
        int readFirstCellIndex = 0;
        if (clazz.isAnnotationPresent(SheetInfo.class)) {
            SheetInfo sheetInfo = (SheetInfo) clazz.getAnnotation(SheetInfo.class);
            readRirstRowNum = sheetInfo.readFirstRowIndex();
            readFirstCellIndex = sheetInfo.readFirstCellIndex();
        }
        int lastRowNum = sheet.getLastRowNum();
        List<T> list = new ArrayList<T>();
        //获取映射
        Map<Integer, BeanMethodDTO> indexToMethodMap = PoiSupportUtil.indexToMethod(clazz,
                PoiEnum.SET_METHOD_PREFIX.getValue());
        for (int i = readRirstRowNum; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            //判断是否结束
            if (PoiSupportUtil.readIsOver(row, readFirstCellIndex)) {
                break;
            }
            T bean = readExcelRow(clazz, row, indexToMethodMap);
            if (bean != null) {
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 读取excel中一行
     * 
     * @param clazz
     *            实体class
     * @param row
     *            数据行
     * @param indexToMethodMap
     *            列索引和方法映射
     * @return 返回实体
     */
    public T readExcelRow(Class<T> clazz, Row row, Map<Integer, BeanMethodDTO> indexToMethodMap) {
        T bean = null;
        try {
            bean = clazz.newInstance();
            int firstCellNum = row.getFirstCellNum();
            int lastCellNum = row.getLastCellNum();
            for (int i = firstCellNum; i < lastCellNum; i++) {
                Cell cell = row.getCell(i);
                BeanMethodDTO methodDto = indexToMethodMap.get(i);
                if (methodDto == null) {
                    continue;
                }
                Method method = clazz.getMethod(methodDto.getMethodName(), methodDto.getParamClazz());
                Object obj = POIUtil.getCellValue(cell);
                if (obj == null || StringUtils.isBlank(obj.toString())
                        || obj.equals(PoiEnum.DEFAULT_BLANK_VALUE1.getValue())
                        || obj.equals(PoiEnum.DEFAULT_BLANK_VALUE2.getValue())) {
                    continue;
                }
                method.invoke(bean, PoiSupportUtil.convertType(methodDto.getParamClazz(), obj.toString()));
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
