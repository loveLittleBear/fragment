package com.jd.fce.fragment.core.service;

import com.jd.fce.fragment.core.domain.ClassInfo;
import com.jd.fce.fragment.core.utils.ClazzUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by hanyang1 on 2017/10/20.
 */
public class TaskService {

    private ApplicationContext applicationContext;

    public TaskService(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation>... annotations){
        Map<String, Object> map = new HashMap<String, Object>();
        for(Class<? extends Annotation> annotation : annotations){
            Map<String, Object> retMap = applicationContext.getBeansWithAnnotation(annotation);
            if(retMap != null){
                map.putAll(retMap);
            }
        }
        return map;
    }

    public List<ClassInfo> getClassInfos(Class<? extends Annotation> annotation) throws Exception{
        Map<String, Object> beansWithAnnotationMap = this.getBeansWithAnnotation(annotation);
        List<ClassInfo> classInfos = new ArrayList<ClassInfo>();
        if(MapUtils.isNotEmpty(beansWithAnnotationMap)){
            for(Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()){
                if(entry != null){
                    ClassInfo classInfo = ClazzUtils.getClassInfo(entry.getKey(), entry.getValue().getClass());
                    classInfos.add(classInfo);
                }
            }
        }
        return classInfos;
    }
}
