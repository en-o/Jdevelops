/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package cn.tannn.jdevelops.utils.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON工具类
 * 提供JSON序列化、反序列化和路径解析功能
 *
 * @author tan
 */
public final class JsonUtils {

    // ObjectMapper实例，用于JSON处理
    private static ObjectMapper mapper = new ObjectMapper();

    // 日志记录器
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    // 数组索引匹配的正则表达式模式，用于解析如"field[0]"格式的路径
    private static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("^(.+?)\\[(\\d+)\\]$");

    static {
        // 配置Java时间模块，用于处理Java 8时间API
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 配置LocalDateTime序列化格式为 "yyyy-MM-dd HH:mm:ss"
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 配置LocalDate序列化格式为 "yyyy-MM-dd"
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 配置LocalTime序列化格式为 "HH:mm:ss"
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // 配置LocalDateTime反序列化格式为 "yyyy-MM-dd HH:mm:ss"
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 配置LocalDate反序列化格式为 "yyyy-MM-dd"
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 配置LocalTime反序列化格式为 "HH:mm:ss"
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // 配置过滤器提供器，用于在序列化时过滤掉"class"属性
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.serializeAllExcept("class"));

        // 配置ObjectMapper的各种特性
        mapper
                // 禁用遇到未知属性时抛出异常，增强容错性
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                // 允许JSON中包含注释
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)

                // 允许JSON中的字段名不使用引号
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)

                // 允许JSON中使用单引号
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)

                // 允许JSON中包含未转义的控制字符
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)

                // 设置属性命名策略为下划线格式（snake_case），自动转换驼峰命名
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

                // 设置日期格式为 "yyyy-MM-dd HH:mm:ss"
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))

                // 注册Java时间模块
                .registerModule(javaTimeModule)

                // 设置过滤器提供器
                .setFilterProvider(filterProvider);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object 要转换的对象
     * @return JSON字符串，转换失败时返回"{}"
     */
    public static String toJson(final Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return "{}";
        }
    }

    /**
     * 从对象中移除class属性
     * 主要用于处理Map类型的对象，递归移除其中的class字段
     *
     * @param object 要处理的对象
     * @return 处理后的对象
     */
    public static Object removeClass(final Object object) {
        if (object instanceof Map) {
            Map map = (Map) object;
            Object result = map.get("result");
            if (result instanceof Map) {
                Map resultMap = (Map) result;
                resultMap.remove("class");
            }
            map.remove("class");
            return object;
        } else {
            return object;
        }
    }

    /**
     * JSON路径解析器
     * 支持复杂的JSON路径解析，包括嵌套对象和数组索引
     *
     * @param jsonStr JSON字符串
     * @param path 需要解析的路径，支持嵌套对象和数组索引（如：data.users[0].name）
     * @param type 解析后的类型
     * @return 解析key的值
     * @param <T> 解析后的类型
     * @throws IllegalArgumentException 当路径无效时
     * @throws RuntimeException 当解析失败时
     */
    public static <T> T parse(String jsonStr, String path, Class<T> type) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON字符串不能为空");
        }
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("路径不能为空");
        }
        if (type == null) {
            throw new IllegalArgumentException("类型不能为空");
        }

        try {
            JsonNode root = mapper.readTree(jsonStr);
            JsonNode current = navigateToNode(root, path);

            if (current == null) {
                throw new IllegalArgumentException("无效路径: " + path);
            }

            return mapper.treeToValue(current, type);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("解析JSON失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据路径导航到指定节点
     * 将路径按"."分割，逐级访问JSON节点
     *
     * @param root 根节点
     * @param path 路径
     * @return 目标节点
     */
    private static JsonNode navigateToNode(JsonNode root, String path) {
        String[] pathSegments = path.split("\\.");
        JsonNode current = root;

        for (String segment : pathSegments) {
            if (segment.trim().isEmpty()) {
                continue;
            }

            current = processSegment(current, segment);
            if (current == null) {
                return null;
            }
        }

        return current;
    }

    /**
     * 处理单个路径段
     * 支持两种格式：
     * 1. 普通字段：直接获取字段值
     * 2. 数组索引：field[index]格式，先获取数组字段，再获取指定索引的元素
     *
     * @param node 当前节点
     * @param segment 路径段
     * @return 处理后的节点
     */
    private static JsonNode processSegment(JsonNode node, String segment) {
        Matcher matcher = ARRAY_INDEX_PATTERN.matcher(segment);

        if (matcher.matches()) {
            // 处理数组索引：field[index]
            String fieldName = matcher.group(1);
            int index = Integer.parseInt(matcher.group(2));

            JsonNode arrayNode = node.get(fieldName);
            if (arrayNode == null || !arrayNode.isArray()) {
                return null;
            }

            return arrayNode.get(index);
        } else {
            // 处理普通字段
            return node.get(segment);
        }
    }
}
