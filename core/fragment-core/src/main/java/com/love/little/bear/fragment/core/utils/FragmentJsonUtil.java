package com.love.little.bear.fragment.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;
import java.util.List;

/**
 * Created by hanyang1 on 2017/4/21.
 */
public class FragmentJsonUtil {

    private static SerializeConfig mapping = new SerializeConfig();

    /**
     * 默认的处理时间
     * @param jsonText
     * SerializerFeature.WriteMapNullValue null也序列化
     * @return
     */
    public static String toJSON(Object jsonText) {
        return JSON.toJSONString(jsonText,
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 自定义时间格式
     * @param jsonText
     * @return
     */
    public static String toJSON(Object jsonText, String dateFormat) {
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        return JSON.toJSONString(jsonText, mapping);
    }

    /**
     * JavaBean
     * @param jsonText
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String jsonText, Class<T> clazz){
        return JSON.parseObject(jsonText, clazz);
    }

    /**
     * List<JavaBean>
     * @param jsonText
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String jsonText, Class<T> clazz){
        return JSON.parseArray(jsonText, clazz);
    }

    /**
     * List<Map<String,Object>>
     * @param jsonText
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String jsonText, TypeReference<T> typeReference){
        return JSON.parseObject(jsonText, typeReference);
    }

}
