package com.gbicc.aicr.system.support.poi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * sheet页
 * 
 * @author FC
 * @version v1.0 2019年8月12日
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SheetInfo {
    /**
     * sheet页索引<br/>
     * 默认为0
     * 
     * @return
     */
    int index() default 0;

    /**
     * sheet页名称<br/>
     * 默认为""
     * 
     * @return
     */
    String name() default "";

    /**
     * 读取第一行的索引<br/>
     * 默认为1
     * 
     * @return
     */
    int readFirstRowIndex() default 1;

    /**
     * 读取第一列的索引<br/>
     * 默认为0
     * 
     * @return
     */
    int readFirstCellIndex() default 0;
}