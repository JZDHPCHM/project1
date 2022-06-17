package com.gbicc.aicr.system.support.poi.util;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import com.gbicc.aicr.system.support.dto.BeanMethodDTO;
import com.gbicc.aicr.system.support.enums.AppEnum;
import com.gbicc.aicr.system.support.enums.PoiEnum;
import com.gbicc.aicr.system.support.poi.annotation.SheetTitleIndex;

/**
 * poi支持工具类
 * 
 * @author FC
 * @version v1.0 2019年8月19日
 */
public class PoiSupportUtil {

    /**
     * sheet页中title和实体类属性映射
     * 
     * @param clazz
     *            class类
     * @param methodPrefix
     *            方法前缀
     * @return 映射<br/>
     *         key：sheet页的title<br/>
     *         value：实体类属性
     * 
     */
    public static Map<Integer, BeanMethodDTO> indexToMethod(Class clazz, String methodPrefix) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            throw new RuntimeException("无法读取：实体无属性！");
        }
        Map<Integer, BeanMethodDTO> map = new HashMap<Integer, BeanMethodDTO>();
        for (Field field : fields) {
            //判断(类上,方法上…)是否有指定的注解
            if (!field.isAnnotationPresent(SheetTitleIndex.class)) {
                continue;
            }
            //获取(类上 ,方法上…)的注解
            SheetTitleIndex titleNameAnnotation = field.getAnnotation(SheetTitleIndex.class);
            int titleIndex = titleNameAnnotation.value();
            String variable = field.getName();
            //映射set方法
            BeanMethodDTO method = new BeanMethodDTO();
            String methodName = variable.substring(0, 1).toUpperCase() + variable.substring(1);
            if (StringUtils.isNotBlank(methodPrefix)) {
                methodName = methodPrefix + methodName;
            }
            method.setMethodName(methodName);
            method.setParamClazz(field.getType());
            //获取数据库字段注解
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                method.setColumnName(column.name());
            }
            map.put(titleIndex, method);
        }
        return map;
    }

    /**
     * 类型转换
     *
     * @param classzz
     *            参数类对象
     * @param value
     *            参数值
     * @return
     */
    public static Object convertType(Class classzz, String value) {
        if (Integer.class == classzz || int.class == classzz) {
            return Integer.valueOf(value);
        }
        if (Short.class == classzz || short.class == classzz) {
            return Short.valueOf(value);
        }
        if (Byte.class == classzz || byte.class == classzz) {
            return Byte.valueOf(value);
        }
        if (Character.class == classzz || char.class == classzz) {
            return value.charAt(0);
        }
        if (Long.class == classzz || long.class == classzz) {
            return Long.valueOf(value);
        }
        if (Float.class == classzz || float.class == classzz) {
            return Float.valueOf(value);
        }
        if (Double.class == classzz || double.class == classzz) {
            return Double.valueOf(value);
        }
        if (Boolean.class == classzz || boolean.class == classzz) {
            return Boolean.valueOf(value.toLowerCase());
        }
        if (BigDecimal.class == classzz) {
            return new BigDecimal(value);
        }
        if (Date.class == classzz) {
            SimpleDateFormat formatter = new SimpleDateFormat(PoiEnum.DEFAULT_DATE_FORMAT_PATTERN.getValue());
            ParsePosition pos = new ParsePosition(0);//用来标明解析开始位，其实也可以不明传pos的
            Date date = formatter.parse(value, pos);
            return date;
        }
        return value;
    }

    /**
     * 获取包下特定结尾的文件
     *
     * @param sheetEntityPackageName
     *            excel所有sheet页对应的实体包
     * @param fileSuffix
     *            文件后缀
     * @return
     */
    public static File[] getPackageFile(String sheetEntityPackageName, String fileSuffix) {
        if (StringUtils.isBlank(sheetEntityPackageName)) {
            throw new RuntimeException("package is null");
        }
        //获取实体包名下的所有文件
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader
                .getResource(sheetEntityPackageName.replace(AppEnum.PACKAGE_SEPATATOR.getValue(), File.separator));
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File dir = new File(filePath);
        if (!dir.exists()) {
            return null;
        }
        File[] files = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname != null && StringUtils.isNotBlank(pathname.getName())
                        && pathname.getName().endsWith(fileSuffix);
            }
        });
        return files;
    }

    /**
     * 获取包下的class
     *
     * @param sheetEntityPackageName
     *            excel所有sheet页对应的实体包
     */
    public static List<Class> getPackageClass(String sheetEntityPackageName) {
        File[] files = getPackageFile(sheetEntityPackageName, AppEnum.CLASS_FILE_SUFFIX.getValue());
        if (files == null || files.length <= 0) {
            return null;
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class> list = new ArrayList<Class>();
        for (File file : files) {
            String className = file.getName().substring(0, file.getName().length() - 6);
            try {
                list.add(classLoader
                        .loadClass(sheetEntityPackageName + AppEnum.PACKAGE_SEPATATOR.getValue() + className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 判断读取文档的时候结束
     * 
     * @param row
     *            当前列
     * @param firstCellIndex
     *            第一列索引
     * @return
     */
    public static Boolean readIsOver(Row row, Integer firstCellIndex) {
        if (row == null) {
            return true;
        }
        Cell cell = row.getCell(firstCellIndex);
        if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
            return true;
        }
        return false;
    }

}