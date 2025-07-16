package cn.tannn.jdevelops.utils.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON工具类
 * 提供JSON序列化、反序列化和路径解析功能
 * 支持动态配置和多种ObjectMapper实例
 *
 * @author tan
 */
public final class JsonUtils {

    // 默认ObjectMapper实例
    private static final ObjectMapper DEFAULT_MAPPER = createDefaultMapper();

    // 缓存不同配置的ObjectMapper实例
    private static final Map<String, ObjectMapper> MAPPER_CACHE = new ConcurrentHashMap<>();

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    // 数组索引匹配的正则表达式模式，用于解析如"field[0]"格式的路径
    private static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("^(.+?)\\[(\\d+)\\]$");

    // 默认时间格式
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * JSON配置类
     * 用于动态配置ObjectMapper的各种特性
     * 支持链式调用，方便灵活配置
     */
    public static class JsonConfig {
        /** 是否在遇到未知属性时抛出异常，默认false（增强容错性） */
        private boolean failOnUnknownProperties = false;

        /** 是否允许JSON中包含注释，默认true */
        private boolean allowComments = true;

        /** 是否允许JSON中的字段名不使用引号，默认true */
        private boolean allowUnquotedFieldNames = true;

        /** 是否允许JSON中使用单引号，默认true */
        private boolean allowSingleQuotes = true;

        /** 是否允许JSON中包含未转义的控制字符，默认true */
        private boolean allowUnescapedControlChars = true;

        /** 是否使用下划线命名策略（snake_case），默认true */
        private boolean useSnakeCaseNaming = true;

        /** 是否启用class字段过滤器，默认true */
        private boolean enableClassFilter = true;

        /** 日期时间格式，默认 "yyyy-MM-dd HH:mm:ss" */
        private String dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;

        /** 日期格式，默认 "yyyy-MM-dd" */
        private String dateFormat = DEFAULT_DATE_FORMAT;

        /** 时间格式，默认 "HH:mm:ss" */
        private String timeFormat = DEFAULT_TIME_FORMAT;

        /**
         * 默认构造函数
         * 使用推荐的默认配置
         */
        public JsonConfig() {}

        /**
         * 配置是否在遇到未知属性时抛出异常
         *
         * @param enable true表示抛出异常，false表示忽略未知属性（推荐）
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig failOnUnknownProperties(boolean enable) {
            this.failOnUnknownProperties = enable;
            return this;
        }

        /**
         * 配置是否允许JSON中包含注释
         *
         * @param enable true表示允许注释（推荐），false表示不允许
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig allowComments(boolean enable) {
            this.allowComments = enable;
            return this;
        }

        /**
         * 配置是否允许JSON中的字段名不使用引号
         *
         * @param enable true表示允许不带引号的字段名，false表示严格要求引号
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig allowUnquotedFieldNames(boolean enable) {
            this.allowUnquotedFieldNames = enable;
            return this;
        }

        /**
         * 配置是否允许JSON中使用单引号
         *
         * @param enable true表示允许单引号，false表示只允许双引号
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig allowSingleQuotes(boolean enable) {
            this.allowSingleQuotes = enable;
            return this;
        }

        /**
         * 配置是否允许JSON中包含未转义的控制字符
         * 注意：这是非标准特性，使用时需谨慎
         *
         * @param enable true表示允许未转义的控制字符，false表示严格要求转义
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig allowUnescapedControlChars(boolean enable) {
            this.allowUnescapedControlChars = enable;
            return this;
        }

        /**
         * 配置是否使用下划线命名策略（snake_case）
         *
         * @param enable true表示使用snake_case，false表示使用驼峰命名
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig useSnakeCaseNaming(boolean enable) {
            this.useSnakeCaseNaming = enable;
            return this;
        }

        /**
         * 配置是否启用class字段过滤器
         * 启用后会在序列化时自动过滤掉"class"属性
         *
         * @param enable true表示启用过滤器，false表示不过滤
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig enableClassFilter(boolean enable) {
            this.enableClassFilter = enable;
            return this;
        }

        /**
         * 配置日期时间格式
         *
         * @param format 日期时间格式字符串，如 "yyyy-MM-dd HH:mm:ss"
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig dateTimeFormat(String format) {
            this.dateTimeFormat = format;
            return this;
        }

        /**
         * 配置日期格式
         *
         * @param format 日期格式字符串，如 "yyyy-MM-dd"
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig dateFormat(String format) {
            this.dateFormat = format;
            return this;
        }

        /**
         * 配置时间格式
         *
         * @param format 时间格式字符串，如 "HH:mm:ss"
         * @return 当前配置实例，支持链式调用
         */
        public JsonConfig timeFormat(String format) {
            this.timeFormat = format;
            return this;
        }

        /**
         * 生成配置的唯一标识，用于缓存ObjectMapper实例
         *
         * @return 配置的唯一标识字符串
         */
        private String getCacheKey() {
            return String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s",
                    failOnUnknownProperties, allowComments, allowUnquotedFieldNames,
                    allowSingleQuotes, allowUnescapedControlChars, useSnakeCaseNaming,
                    enableClassFilter, dateTimeFormat, dateFormat, timeFormat);
        }
    }

    /**
     * 创建默认配置的ObjectMapper
     * 使用推荐的默认配置，适合大多数使用场景
     *
     * @return 配置好的ObjectMapper实例
     */
    private static ObjectMapper createDefaultMapper() {
        return createMapper(new JsonConfig());
    }

    /**
     * 根据配置创建ObjectMapper
     * 根据JsonConfig配置创建相应的ObjectMapper实例
     *
     * @param config JSON配置对象
     * @return 配置好的ObjectMapper实例
     */
    private static ObjectMapper createMapper(JsonConfig config) {
        ObjectMapper mapper = new ObjectMapper();

        // 配置Java时间模块，用于处理Java 8时间API
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 配置LocalDateTime序列化格式
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(config.dateTimeFormat)));

        // 配置LocalDate序列化格式
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(config.dateFormat)));

        // 配置LocalTime序列化格式
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(config.timeFormat)));

        // 配置LocalDateTime反序列化格式
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(config.dateTimeFormat)));

        // 配置LocalDate反序列化格式
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(config.dateFormat)));

        // 配置LocalTime反序列化格式
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(config.timeFormat)));

        // 配置过滤器提供器，用于在序列化时过滤掉"class"属性（如果启用）
        if (config.enableClassFilter) {
            FilterProvider filterProvider = new SimpleFilterProvider()
                    .addFilter("classFilter", SimpleBeanPropertyFilter.serializeAllExcept("class"));
            mapper.setFilterProvider(filterProvider);
        }

        // 配置ObjectMapper的各种特性
        mapper
                // 配置是否在遇到未知属性时抛出异常
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, config.failOnUnknownProperties)

                // 配置是否允许JSON中包含注释
                .configure(JsonParser.Feature.ALLOW_COMMENTS, config.allowComments)

                // 配置是否允许JSON中的字段名不使用引号
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, config.allowUnquotedFieldNames)

                // 配置是否允许JSON中使用单引号
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, config.allowSingleQuotes)

                // 使用新的JsonReadFeature替代已废弃的ALLOW_UNQUOTED_CONTROL_CHARS特性
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), config.allowUnescapedControlChars)

                // 设置日期格式
                .setDateFormat(new SimpleDateFormat(config.dateTimeFormat))

                // 注册Java时间模块
                .registerModule(javaTimeModule);

        // 配置属性命名策略（如果启用下划线命名）
        if (config.useSnakeCaseNaming) {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        }

        return mapper;
    }

    /**
     * 获取或创建配置的ObjectMapper
     * 使用缓存机制，避免重复创建相同配置的ObjectMapper
     *
     * @param config JSON配置对象，如果为null则返回默认配置
     * @return 对应配置的ObjectMapper实例
     */
    private static ObjectMapper getMapper(JsonConfig config) {
        if (config == null) {
            return DEFAULT_MAPPER;
        }

        String cacheKey = config.getCacheKey();
        return MAPPER_CACHE.computeIfAbsent(cacheKey, k -> createMapper(config));
    }

    /**
     * 使用默认配置将对象转换为JSON字符串
     *
     * @param object 要转换的对象
     * @return JSON字符串，转换失败时返回"{}"
     */
    public static String toJson(final Object object) {
        return toJson(object, null);
    }

    /**
     * 使用指定配置将对象转换为JSON字符串
     *
     * @param object 要转换的对象
     * @param config JSON配置对象，如果为null则使用默认配置
     * @return JSON字符串，转换失败时返回"{}"
     */
    public static String toJson(final Object object, JsonConfig config) {
        try {
            return getMapper(config).writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return "{}";
        }
    }


    /**
     * 使用默认配置将JSON字符串转换为对象
     *
     * @param jsonStr JSON字符串
     * @param type 目标类型
     * @return 转换后的对象，转换失败时返回null
     * @param <T> 目标类型
     */
    public static <T> T fromJson(String jsonStr, Class<T> type) {
        return fromJson(jsonStr, type, null);
    }

    /**
     * 使用指定配置将JSON字符串转换为对象
     *
     * @param jsonStr JSON字符串
     * @param type 目标类型
     * @param config JSON配置对象，如果为null则使用默认配置
     * @return 转换后的对象，转换失败时返回null
     * @param <T> 目标类型
     */
    public static <T> T fromJson(String jsonStr, Class<T> type, JsonConfig config) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return null;
        }

        try {
            return getMapper(config).readValue(jsonStr, type);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonStr, e);
            return null;
        }
    }

    /**
     * 从对象中移除class属性
     * 主要用于处理Map类型的对象，递归移除其中的class字段
     */
    public static Object removeClass(final Object object) {
        if (object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) object;
            Object result = map.get("result");
            if (result instanceof Map) {
                Map<String, Object> resultMap = (Map<String, Object>) result;
                resultMap.remove("class");
            }
            map.remove("class");
            return object;
        } else {
            return object;
        }
    }


    /**
     * 使用默认配置进行JSON路径解析
     *
     * @param jsonStr JSON字符串
     * @param path 需要解析的路径，支持嵌套对象和数组索引（如：data.users[0].name）
     * @param type 解析后的类型
     * @return 解析得到的值
     * @param <T> 解析后的类型
     */
    public static <T> T parse(String jsonStr, String path, Class<T> type) {
        return parse(jsonStr, path, type, null);
    }


    /**
     * 使用指定配置进行JSON路径解析
     * 支持复杂的JSON路径解析，包括嵌套对象和数组索引
     *
     * @param jsonStr JSON字符串
     * @param path 需要解析的路径，支持嵌套对象和数组索引（如：data.users[0].name）
     * @param type 解析后的类型
     * @param config JSON配置
     * @return 解析key的值
     * @param <T> 解析后的类型
     * @throws IllegalArgumentException 当参数无效时
     * @throws RuntimeException 当解析失败时
     */
    public static <T> T parse(String jsonStr, String path, Class<T> type, JsonConfig config) {
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
            ObjectMapper mapper = getMapper(config);
            JsonNode root = mapper.readTree(jsonStr);
            JsonNode current = navigateToNode(root, path);

            if (current == null || current.isNull()) {
                return null;
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
     * @param path 路径字符串
     * @return 目标节点，如果路径无效则返回null
     */
    private static JsonNode navigateToNode(JsonNode root, String path) {
        String[] pathSegments = path.split("\\.");
        JsonNode current = root;

        // 逐级处理路径段
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
     * @return 处理后的节点，如果路径无效则返回null
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

    /**
     * 检查JSON字符串是否有效
     * 使用默认配置进行验证
     *
     * @param jsonStr 待验证的JSON字符串
     * @return true表示有效，false表示无效
     */
    public static boolean isValidJson(String jsonStr) {
        return isValidJson(jsonStr, null);
    }

    /**
     * 使用指定配置检查JSON字符串是否有效
     *
     * @param jsonStr 待验证的JSON字符串
     * @param config JSON配置对象，如果为null则使用默认配置
     * @return true表示有效，false表示无效
     */
    public static boolean isValidJson(String jsonStr, JsonConfig config) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return false;
        }

        try {
            getMapper(config).readTree(jsonStr);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 格式化JSON字符串（美化输出）
     * 使用默认配置进行格式化
     *
     * @param jsonStr 待格式化的JSON字符串
     * @return 格式化后的JSON字符串，格式化失败时返回原字符串
     */
    public static String prettyJson(String jsonStr) {
        return prettyJson(jsonStr, null);
    }


    /**
     * 使用指定配置格式化JSON字符串（美化输出）
     *
     * @param jsonStr 待格式化的JSON字符串
     * @param config JSON配置对象，如果为null则使用默认配置
     * @return 格式化后的JSON字符串，格式化失败时返回原字符串
     */
    public static String prettyJson(String jsonStr, JsonConfig config) {
        try {
            ObjectMapper mapper = getMapper(config);
            JsonNode jsonNode = mapper.readTree(jsonStr);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (IOException e) {
            logger.warn("format json string error:" + jsonStr, e);
            return jsonStr;
        }
    }

    /**
     * 清空ObjectMapper缓存
     * 清空所有缓存的ObjectMapper实例，释放内存
     */
    public static void clearCache() {
        MAPPER_CACHE.clear();
    }

    /**
     * 获取缓存大小
     *
     * @return 当前缓存中ObjectMapper实例的数量
     */
    public static int getCacheSize() {
        return MAPPER_CACHE.size();
    }
}
