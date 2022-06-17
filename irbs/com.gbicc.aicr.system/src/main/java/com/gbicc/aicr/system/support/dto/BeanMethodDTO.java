package com.gbicc.aicr.system.support.dto;

/**
 * 实体方法临时类
 * 
 * @author FC
 * @version v1.0 2019年8月8日
 */
public class BeanMethodDTO {

    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型class
     */
    private Class paramClazz;
    /**
     * 数据库字段名称
     */
    private String columnName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class getParamClazz() {
        return paramClazz;
    }

    public void setParamClazz(Class paramClazz) {
        this.paramClazz = paramClazz;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
