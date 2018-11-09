package com.love.little.bear.fragment.core.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by hanyang1 on 2017/10/20.
 */
@Data
public class ParamInfo {

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数类型
     */
    private Class<?> paramType;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 是否基本数据类型或者封装类
     */
    private boolean isPrimitiveOrWrapper;

    /**
     * 参数类型-泛型
     */
    private List<Class<?>> parameterizedTypes;

    /**
     * 默认值
     */
    private String defaultValue;

}
