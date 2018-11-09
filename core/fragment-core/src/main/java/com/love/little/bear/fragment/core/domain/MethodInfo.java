package com.love.little.bear.fragment.core.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by hanyang1 on 2017/10/20.
 */
@Data
public class MethodInfo {

    private String methodName;

    private Class<?> returnType;

    private List<ParamInfo> paramInfoList;

    private String desc;
}
