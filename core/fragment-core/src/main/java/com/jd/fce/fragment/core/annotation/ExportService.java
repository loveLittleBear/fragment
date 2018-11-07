package com.jd.fce.fragment.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hanyang1 on 2017/10/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportService {
    String name() default "未设置";

    String desc() default "无描述";

    String appGroup() default "应用未分组";

    String serGroup() default "业务未分组";
}
