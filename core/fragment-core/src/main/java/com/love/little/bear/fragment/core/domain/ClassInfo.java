package com.love.little.bear.fragment.core.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by hanyang1 on 2017/10/20.
 */
@Data
public class ClassInfo {

    private String className;

    private List<MethodInfo> methodInfoList;

    private String appGroup;

    private String serGroup;

    private String desc;
}
