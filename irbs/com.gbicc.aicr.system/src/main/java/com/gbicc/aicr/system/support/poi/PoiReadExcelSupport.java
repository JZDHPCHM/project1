package com.gbicc.aicr.system.support.poi;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gbicc.aicr.system.support.enums.AppEnum;
import com.gbicc.aicr.system.support.poi.annotation.SheetInfo;
import com.gbicc.aicr.system.support.poi.util.PoiSupportUtil;

/**
 * poi读取excel支持类（通用）
 * 
 * @author FC
 * @version v1.0 2019年8月12日
 */
@Component
public class PoiReadExcelSupport {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PoiReadSheetSupport poiReadSheetSupport;

    /**
     * 读取excel并保存
     *
     * @param wb
     *            poi操作文档对象
     * @param sheetEntityPackageName
     *            excel所有sheet页对应的实体包
     */
    public void readAndSave(Workbook wb, String sheetEntityPackageName) {
        //循环出模板实体并读取
        List<Class> classList = PoiSupportUtil.getPackageClass(sheetEntityPackageName);
        if (CollectionUtils.isEmpty(classList)) {
            return;
        }
        for (Class clazz : classList) {
            String className = clazz.getSimpleName();
            if (!clazz.isAnnotationPresent(SheetInfo.class)) {
                continue;
            }
            SheetInfo sheetInfo = (SheetInfo) clazz.getAnnotation(SheetInfo.class);
            /*获取sheet页，规则：
             * 1、如果name有值，使用name获取sheet
             * 2、如果name没有值，然后再使用index获取sheet
             * */
            Sheet sheet = null;
            String sheetName = sheetInfo.name();
            if (StringUtils.isNotBlank(sheetName)) {
                sheet = wb.getSheet(sheetName);
            } else {
                sheet = wb.getSheetAt(sheetInfo.index());
            }
            //获取读取的数据
            List list = poiReadSheetSupport.readExcelSheet(clazz, sheet, sheetInfo.readFirstRowIndex(),
                    sheetInfo.readFirstCellIndex());
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            //获取具有保存方法的repository
            String repositoryName = (className.substring(0, 1).toLowerCase() + className.substring(1))
                    .replace(AppEnum.ENTITY_SUFFIX.getValue(), AppEnum.REPOSITORY_SUFFIX.getValue());
            Object obj = applicationContext.getBean(repositoryName);
            //调用save方法保存
            try {
                Method method = obj.getClass().getMethod(AppEnum.REPOSITORY_SAVE_METHOD_NAME.getValue(), Object.class);
                for (Object temp : list) {
                    method.invoke(obj, temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
