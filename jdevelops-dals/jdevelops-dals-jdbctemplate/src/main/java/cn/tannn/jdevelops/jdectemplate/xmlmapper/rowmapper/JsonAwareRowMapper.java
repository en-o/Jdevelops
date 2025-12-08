package cn.tannn.jdevelops.jdectemplate.xmlmapper.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 支持JSON字段自动转换的RowMapper
 * <p>
 * 功能：
 * 1. 支持将数据库JSON字段自动转换为Java对象（List、Map、自定义对象等）
 * 2. 支持Record类型
 * 3. 支持普通JavaBean
 * 4. 支持枚举类型
 * 5. 支持时间类型（LocalDateTime、LocalDate等）
 *
 * @param <T> 目标类型
 * @author tnnn
 */
public class JsonAwareRowMapper<T> implements RowMapper<T> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonAwareRowMapper.class);

    private final Class<T> mappedClass;
    private final ObjectMapper objectMapper;

    public JsonAwareRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.objectMapper = new ObjectMapper();
        // 配置ObjectMapper以支持Java 8时间类型
        this.objectMapper.findAndRegisterModules();
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            // 检查是否是Record类型
            if (mappedClass.isRecord()) {
                return mapRecord(rs);
            } else {
                return mapBean(rs);
            }
        } catch (Exception e) {
            LOG.error("Failed to map row to {}: {}", mappedClass.getSimpleName(), e.getMessage(), e);
            throw new SQLException("Failed to map row to " + mappedClass.getSimpleName(), e);
        }
    }

    /**
     * 映射Record类型
     */
    private T mapRecord(ResultSet rs) throws Exception {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        // 获取Record的规范构造函数
        Constructor<T> constructor = getCanonicalConstructor();
        Parameter[] parameters = constructor.getParameters();

        // 准备构造函数参数值
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramName = parameter.getName();
            Class<?> paramType = parameter.getType();

            // 查找对应的列（支持驼峰转下划线）
            Object value = findColumnValue(rs, rsmd, columnCount, paramName, paramType, parameter);
            args[i] = value;
        }

        return constructor.newInstance(args);
    }

    /**
     * 映射普通JavaBean
     */
    private T mapBean(ResultSet rs) throws Exception {
        T instance = mappedClass.getDeclaredConstructor().newInstance();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        // 获取所有字段
        Field[] fields = mappedClass.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();

            // 查找对应的列值
            Object value = findColumnValue(rs, rsmd, columnCount, fieldName, fieldType, field);

            if (value != null) {
                field.setAccessible(true);
                field.set(instance, value);
            }
        }

        return instance;
    }

    /**
     * 查找列值并进行类型转换
     */
    private Object findColumnValue(ResultSet rs, ResultSetMetaData rsmd, int columnCount,
                                     String fieldName, Class<?> fieldType, AnnotatedElement element) throws SQLException {
        // 尝试多种列名匹配策略
        String[] candidateNames = {
                fieldName,                              // 原始名称
                camelToUnderscore(fieldName),           // 驼峰转下划线
                fieldName.toLowerCase(),                 // 全小写
                fieldName.toUpperCase()                  // 全大写
        };

        for (String columnName : candidateNames) {
            for (int i = 1; i <= columnCount; i++) {
                String actualColumnName = rsmd.getColumnLabel(i);
                if (actualColumnName.equalsIgnoreCase(columnName)) {
                    return getColumnValue(rs, i, fieldType, element);
                }
            }
        }

        // 未找到对应列，返回null（允许部分字段缺失）
        LOG.debug("Column not found for field: {}, tried names: {}", fieldName, Arrays.toString(candidateNames));
        return null;
    }

    /**
     * 获取列值并进行类型转换
     */
    private Object getColumnValue(ResultSet rs, int columnIndex, Class<?> targetType, AnnotatedElement element) throws SQLException {
        // 先获取原始值
        Object value = JdbcUtils.getResultSetValue(rs, columnIndex, targetType);

        if (value == null) {
            return null;
        }

        // 如果是String类型，不做转换，直接返回
        if (targetType == String.class) {
            return value;
        }

        // 如果值是字符串，尝试JSON解析
        if (value instanceof String) {
            String strValue = (String) value;

            // 检查是否是JSON格式
            if (isJsonFormat(strValue)) {
                return parseJson(strValue, targetType, element);
            }

            // 不是JSON，尝试普通类型转换
            return convertSimpleType(strValue, targetType);
        }

        // 其他类型直接返回
        return value;
    }

    /**
     * 判断字符串是否是JSON格式
     */
    private boolean isJsonFormat(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        String trimmed = value.trim();
        return (trimmed.startsWith("{") && trimmed.endsWith("}")) ||
                (trimmed.startsWith("[") && trimmed.endsWith("]"));
    }

    /**
     * 解析JSON字符串为目标类型
     */
    private Object parseJson(String json, Class<?> targetType, AnnotatedElement element) {
        try {
            // 处理泛型类型（List<T>、Map<K,V>）
            Type genericType = null;
            if (element instanceof Field) {
                genericType = ((Field) element).getGenericType();
            } else if (element instanceof Parameter) {
                genericType = ((Parameter) element).getParameterizedType();
            }

            if (genericType != null && genericType instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) genericType;
                return objectMapper.readValue(json, objectMapper.getTypeFactory()
                        .constructType(paramType));
            }

            // 非泛型类型直接解析
            return objectMapper.readValue(json, targetType);
        } catch (Exception e) {
            LOG.warn("Failed to parse JSON '{}' to type {}: {}", json, targetType.getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * 简单类型转换
     */
    private Object convertSimpleType(String value, Class<?> targetType) {
        try {
            if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(value);
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(value);
            } else if (targetType == Double.class || targetType == double.class) {
                return Double.parseDouble(value);
            } else if (targetType == Float.class || targetType == float.class) {
                return Float.parseFloat(value);
            } else if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (targetType.isEnum()) {
                return Enum.valueOf((Class<Enum>) targetType, value);
            } else if (targetType == LocalDateTime.class) {
                return LocalDateTime.parse(value);
            } else if (targetType == LocalDate.class) {
                return LocalDate.parse(value);
            }
        } catch (Exception e) {
            LOG.warn("Failed to convert '{}' to type {}: {}", value, targetType.getSimpleName(), e.getMessage());
        }
        return value;
    }

    /**
     * 驼峰转下划线
     */
    private String camelToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * 获取Record的规范构造函数
     */
    private Constructor<T> getCanonicalConstructor() {
        Constructor<T>[] constructors = (Constructor<T>[]) mappedClass.getDeclaredConstructors();
        // Record只有一个规范构造函数
        if (constructors.length == 0) {
            throw new IllegalStateException("No constructor found for Record: " + mappedClass.getName());
        }
        return constructors[0];
    }
}
