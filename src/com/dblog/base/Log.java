package com.dblog.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by luosv on 2017/9/4 0004.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

    /**
     * 字段名
     */
    String logField();

    /**
     * 字段类型
     */
    String fieldType();

    /**
     * 字段索引值
     */
    String index();

}
