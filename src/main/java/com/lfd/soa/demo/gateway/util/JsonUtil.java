package com.lfd.soa.demo.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linfengda
 * @date 2021-01-22 17:17
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        // 不反射不存在的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * 对象转换为字符串
     * @param obj   转换对象
     * @return      json
     * @throws JsonProcessingException
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换为对象
     * @param json      json字符串
     * @param valueType 转换对象类型
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T readValue(String json, Class<T> valueType) {
        if (!StringUtils.isEmpty(json)) {
            try {
                return objectMapper.readValue(json, valueType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * json字符串转换为map
     * @param json      json字符串
     * @param kClass    key类型
     * @param vClass    value类型
     * @param <K>
     * @param <V>
     * @return
     * @throws IOException
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换为列表
     * @param json      json字符串
     * @param eClass    元素类型
     * @param <E>
     * @return
     */
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
