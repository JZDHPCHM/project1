package com.gbicc.aicr.system.support.poi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel的sheet页中标题的名称注解
 * 
 * @author FC
 * @version v1.0 2019年8月8日
 */
@Documented
@Target(ElementType.FIELD) //注解在成员变量上
@Retention(RetentionPolicy.RUNTIME) //生命周期
public @interface SheetTitleIndex {
    /**
     * sheet页中列的索引
     *
     * @return
     */
    int value();
}
