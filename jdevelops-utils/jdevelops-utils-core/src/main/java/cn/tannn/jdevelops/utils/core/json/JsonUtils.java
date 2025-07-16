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
 * JsonUtils.
 *
 * @author xiaoyu
 */
public final class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("classFilter", SimpleBeanPropertyFilter.serializeAllExcept("class"));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .registerModule(javaTimeModule)
                .setFilterProvider(filterProvider);

    }

    /**
     * To json string.
     *
     * @param object the object
     * @return the string
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
     * Remove class object.
     *
     * @param object the object
     * @return the object
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


    private static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("^(.+?)\\[(\\d+)\\]$");
    /**
     * JSON解析器
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
