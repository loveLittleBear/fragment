package com.love.little.bear.fragment.core.utils;

import com.love.little.bear.fragment.core.annotation.ExportMethod;
import com.love.little.bear.fragment.core.annotation.ExportService;
import com.love.little.bear.fragment.core.domain.ClassInfo;
import com.love.little.bear.fragment.core.domain.MethodInfo;
import com.love.little.bear.fragment.core.domain.ParamInfo;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by hanyang1 on 2017/10/20.
 */
public class ClazzUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClazzUtils.class);

    private static final Map<String, ClassInfo> classMap = new HashMap<String, ClassInfo>();

    public static ClassInfo getClassInfo(String beanName, Class<?> clazz) throws Exception {
        ClassInfo classInfo = classMap.get(beanName);
        if(classInfo != null && !CollectionUtils.isEmpty(classInfo.getMethodInfoList())) {
            return classInfo;
        } else {
            classInfo = new ClassInfo();
            classInfo.setClassName(beanName);
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            if(annotations != null){
                for(Annotation annotation : annotations){
                    if(annotation instanceof ExportService){
                        classInfo.setAppGroup(((ExportService) annotation).appGroup());
                        classInfo.setSerGroup(((ExportService) annotation).serGroup());
                        classInfo.setDesc(((ExportService) annotation).desc());
                    }
                }
            }
            List<MethodInfo> methodInfoList = getMethodInfo(clazz, ExportMethod.class);
            classInfo.setMethodInfoList(methodInfoList);
            classMap.put(beanName, classInfo);
            return classInfo;
        }
    }

    private static List<MethodInfo> getMethodInfo(Class<?> clazz, Class<? extends Annotation> annotation) throws Exception {
        Method[] methods = clazz.getDeclaredMethods();
        List<MethodInfo> methodInfoList = new ArrayList<MethodInfo>();
        if(ArrayUtils.isNotEmpty(methods)) {
            for (int i = 0; i < methods.length; i++) {
                Annotation em = methods[i].getAnnotation(annotation);
                if (em != null) {
                    Method method = methods[i];
                    List<ParamInfo> paramInfos = getParamInfo(clazz, method);
                    MethodInfo methodInfo = new MethodInfo();
                    methodInfo.setMethodName(method.getName());
                    methodInfo.setReturnType(method.getReturnType());
                    if(em instanceof ExportService){
                        ExportService es = (ExportService)em;
                        methodInfo.setDesc(es.desc());
                    }
                    methodInfo.setParamInfoList(paramInfos);
                    methodInfoList.add(methodInfo);
                }
            }
        }
        return methodInfoList;
    }

    private static List<ParamInfo> getParamInfo(Class<?> clazz, Method method) throws Exception{
        //方法参数
        Class<?>[] paramTypes = method.getParameterTypes();
        //方法的参数列表 带泛型
        Type[] types = method.getGenericParameterTypes();
        //方法参数名称
        String[] paramName = ParameterNameUtils.getMethodParamNames(clazz, method);
        if (!ArrayUtils.isEmpty(paramTypes)) {
            List<ParamInfo> paramInfos = new ArrayList<ParamInfo>();
            for (int j = 0; j < paramTypes.length; j++) {
                ParamInfo paramInfo = new ParamInfo();
                //paramInfo.setParamName("param" + (j + 1));
                paramInfo.setParamName(paramName[j]);
                Class<?> paramType = paramTypes[j];
                paramInfo.setParamType(paramType);
                //用来判断泛型
                Type type = types[j];
                if(type != null && type instanceof ParameterizedType){
                    Type[] actualType = ((ParameterizedType) type).getActualTypeArguments();
                    if(actualType != null && actualType.length > 0){
                        List<Class<?>> actualTypeList = new ArrayList<Class<?>>();
                        for(Type at : actualType){
                            String atName = at.toString();
                            atName = atName.split(" ")[1];
                            Class<?> clazz1 = ClassUtils.getClass(atName);
                            actualTypeList.add(clazz1);
                        }
                        paramInfo.setParameterizedTypes(actualTypeList);
                    }
                }
                Object obj = instantiate(paramType, paramInfo.getParameterizedTypes());
                String defaultValue = FragmentJsonUtil.toJSON(obj);
                paramInfo.setDefaultValue(defaultValue);
                paramInfo.setPrimitiveOrWrapper(ClassUtils.isPrimitiveOrWrapper(paramType));
                paramInfos.add(paramInfo);
            }
            return paramInfos;
        }
        return null;
    }

    public static Object convertObj(String value, ParamInfo paramInfo) throws Exception{
        Class<?> paramType = paramInfo.getParamType();
        if(paramInfo.isPrimitiveOrWrapper()){
            return ConvertUtils.convert(value, paramType);
        }
        if(Date.class.isAssignableFrom(paramType)){//日期类型
            return FragmentDateUtils.parseDate(value);
        }
        if(String.class.isAssignableFrom(paramType)){
            return value;
        }
        if(paramType.isArray() || Map.class.isAssignableFrom(paramType)) {
            return FragmentJsonUtil.parseObject(value, paramType);
        }
        if(Collection.class.isAssignableFrom(paramType)){
            List<Class<?>> types = paramInfo.getParameterizedTypes();
            if(CollectionUtils.isNotEmpty(types)){
                return FragmentJsonUtil.parseArray(value, types.get(0));
            }
        }
        return FragmentJsonUtil.parseObject(value, paramType);
    }

    private static Object instantiate(Class<?> type, List<Class<?>> parameterizedTypes) throws Exception {
        boolean isPrimitive = type.isPrimitive();
        if(isPrimitive){
            return 0;
        }else if(Integer.class.isAssignableFrom(type)) {
            return new Integer(0);
        } else if(Long.class.isAssignableFrom(type)) {
            return new Long(0L);
        } else if(Double.class.isAssignableFrom(type)) {
            return new Double(0.0D);
        } else if(Float.class.isAssignableFrom(type)) {
            return new Float(0.0F);
        } else if(BigDecimal.class.isAssignableFrom(type)) {
            return new BigDecimal("0");
        } else if(BigInteger.class.isAssignableFrom(type)) {
            return new BigInteger("0");
        } else if(Short.class.isAssignableFrom(type)) {
            return new Short("0");
        } else if(Byte.class.isAssignableFrom(type)) {
            return new Byte("0");
        } else if(String.class.isAssignableFrom(type)) {
            return new String("");
        } else if(Date.class.isAssignableFrom(type)) {
            return FragmentJsonUtil.toJSON(new Date(), "yyyy-MM-dd HH:mm:ss");
        } else if(type.isArray()) {
            Class<?> ComponentType = type.getComponentType();
            Object obj = Array.newInstance(ComponentType, 1);
            return obj;
        } else if(type.isEnum()) {
            return getFirstEnum(type);
        } else if(Set.class.isAssignableFrom(type)) {
            Set set = (Set) HashSet.class.newInstance();;
            if(CollectionUtils.isNotEmpty(parameterizedTypes)) {
                Class genericClazz = parameterizedTypes.get(0);
                Object instance = genericClazz.newInstance();
                set.add(instance);
            }
            return set;
        } else if(List.class.isAssignableFrom(type)){
            List list = (List)ArrayList.class.newInstance();
            if(CollectionUtils.isNotEmpty(parameterizedTypes)) {
                Class genericClazz = parameterizedTypes.get(0);
                Object instance = genericClazz.newInstance();
                list.add(instance);
            }
            return list;
        } else if(Map.class.isAssignableFrom(type)) {
            Map map = (Map)HashMap.class.newInstance();
            if(CollectionUtils.isNotEmpty(parameterizedTypes)) {
                Class<?> keyClassType = parameterizedTypes.get(0);
                Class<?> valueClassType = parameterizedTypes.get(1);
                Object key = keyClassType.newInstance();
                Object value = valueClassType.newInstance();
                if(key != null && value != null) {
                    map.put(key, value);
                }
            }
            return map;
        }  else {
            Object instance = type.newInstance();
            return instance;
        }
    }

    private static Object getFirstEnum(Class<?> clazz) throws Exception {
        Object[] objs = clazz.getEnumConstants();
        int len$ = objs.length;
        int i$ = 0;
        if(i$ < len$) {
            Object obj = objs[i$];
            Method m = obj.getClass().getDeclaredMethod("values", (Class[])null);
            Object[] result = (Object[])((Object[])m.invoke(obj, (Object[])null));
            return result[0];
        } else {
            return null;
        }
    }

    private static String getName(Class<?> type) {
        String name = type.getName();
        if(StringUtils.contains(name, "$$EnhancerByCGLIB$$") || StringUtils.contains(name, "$$EnhancerBySpringCGLIB$$")) {
            name = StringUtils.substringBefore(name, "$$EnhancerBy");
        }
        return name;
    }


}
