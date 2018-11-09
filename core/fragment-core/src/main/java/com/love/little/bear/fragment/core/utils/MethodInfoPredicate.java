package com.love.little.bear.fragment.core.utils;

import com.love.little.bear.fragment.core.domain.MethodInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by hanyang1 on 2017/10/23.
 */
public class MethodInfoPredicate implements Predicate {

    private String methodName;

    public MethodInfoPredicate(String methodName) {
        this.methodName = methodName;
    }

    public boolean evaluate(Object o) {
        MethodInfo method = (MethodInfo) o;
        return StringUtils.equals(this.methodName, method.getMethodName());
    }

    public MethodInfo select(List<MethodInfo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            Collection<MethodInfo> select = CollectionUtils.select(list, this);
            return CollectionUtils.isEmpty(select) ? null : (MethodInfo) select.iterator().next();
        }
    }
}
