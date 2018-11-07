package com.jd.fce.tool.jmq.retry.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RetryMsgDto implements Serializable {

    /**
     * 业务id
     */
    private String busiId;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数
     */
    private Class[] paramTypes;

    /**
     * 方法参数
     */
    private Object[] params;
}
