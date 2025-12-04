package cn.tannn.jdevelops.jdectemplate.xmlmapper.util;

import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * OGNL 表达式工具类
 * 简化版本的 OGNL 实现，支持基本的属性访问和条件判断
 *
 * @author tnnn
 */
public class OgnlUtil {

    /**
     * 获取对象属性值
     *
     * @param expression 表达式（如 "user.name" 或 "list[0]"）
     * @param root       根对象
     * @return 属性值
     */
    public static Object getValue(String expression, Object root) {
        if (!StringUtils.hasText(expression)) {
            return null;
        }

        if (root == null) {
            return null;
        }

        try {
            // 处理简单属性
            if (!expression.contains(".") && !expression.contains("[")) {
                return getSimpleProperty(root, expression);
            }

            // 处理复杂表达式
            return evaluateExpression(expression, root);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 评估布尔表达式
     *
     * @param expression 表达式
     * @param root       根对象
     * @return 布尔结果
     */
    public static boolean evaluateBoolean(String expression, Object root) {
        if (!StringUtils.hasText(expression)) {
            return false;
        }

        try {
            // 处理 null 检查
            if (expression.contains("!=")) {
                String[] parts = expression.split("!=");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    return evaluateNotEquals(left, right);
                }
            }

            if (expression.contains("==")) {
                String[] parts = expression.split("==");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    return evaluateEquals(left, right);
                }
            }

            // 处理逻辑运算
            if (expression.contains(" and ") || expression.contains(" && ")) {
                String[] parts = expression.split("(\\s+and\\s+|\\s*&&\\s*)");
                for (String part : parts) {
                    if (!evaluateBoolean(part.trim(), root)) {
                        return false;
                    }
                }
                return true;
            }

            if (expression.contains(" or ") || expression.contains(" || ")) {
                String[] parts = expression.split("(\\s+or\\s+|\\s*\\|\\|\\s*)");
                for (String part : parts) {
                    if (evaluateBoolean(part.trim(), root)) {
                        return true;
                    }
                }
                return false;
            }

            // 处理非空判断
            Object value = getValue(expression, root);
            return isNotEmpty(value);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断值是否非空
     */
    private static boolean isNotEmpty(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            return StringUtils.hasText((String) value);
        }
        if (value instanceof Collection) {
            return !((Collection<?>) value).isEmpty();
        }
        if (value instanceof Map) {
            return !((Map<?, ?>) value).isEmpty();
        }
        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        }
        return true;
    }

    /**
     * 评估不等于
     */
    private static boolean evaluateNotEquals(Object left, String right) {
        if ("null".equals(right)) {
            return left != null;
        }
        if ("''".equals(right) || "\"\"".equals(right)) {
            return left != null && !left.toString().isEmpty();
        }
        // 移除引号
        String rightValue = removeQuotes(right);
        return left != null && !left.toString().equals(rightValue);
    }

    /**
     * 评估等于
     */
    private static boolean evaluateEquals(Object left, String right) {
        if ("null".equals(right)) {
            return left == null;
        }
        if ("''".equals(right) || "\"\"".equals(right)) {
            return left == null || left.toString().isEmpty();
        }
        // 移除引号
        String rightValue = removeQuotes(right);
        return left != null && left.toString().equals(rightValue);
    }

    /**
     * 移除字符串两端的引号
     */
    private static String removeQuotes(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if ((str.startsWith("'") && str.endsWith("'")) ||
                (str.startsWith("\"") && str.endsWith("\""))) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    /**
     * 评估复杂表达式
     */
    private static Object evaluateExpression(String expression, Object root) throws Exception {
        Object current = root;

        // 处理链式访问
        String[] parts = expression.split("\\.");
        for (String part : parts) {
            if (current == null) {
                return null;
            }

            // 处理数组/列表访问
            if (part.contains("[")) {
                int bracketIndex = part.indexOf('[');
                String propertyName = part.substring(0, bracketIndex);
                String indexStr = part.substring(bracketIndex + 1, part.indexOf(']'));

                // 先获取属性
                if (!propertyName.isEmpty()) {
                    current = getSimpleProperty(current, propertyName);
                }

                // 再访问索引
                if (current != null) {
                    int index = Integer.parseInt(indexStr);
                    current = getIndexedProperty(current, index);
                }
            } else {
                current = getSimpleProperty(current, part);
            }
        }

        return current;
    }

    /**
     * 获取简单属性
     */
    private static Object getSimpleProperty(Object obj, String propertyName) throws Exception {
        if (obj == null || !StringUtils.hasText(propertyName)) {
            return null;
        }

        // 如果是 Map
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).get(propertyName);
        }

        // 尝试 getter 方法
        try {
            String getterName = "get" + capitalize(propertyName);
            Method getter = obj.getClass().getMethod(getterName);
            return getter.invoke(obj);
        } catch (NoSuchMethodException e) {
            // 尝试 is 方法（for boolean）
            try {
                String isGetterName = "is" + capitalize(propertyName);
                Method getter = obj.getClass().getMethod(isGetterName);
                return getter.invoke(obj);
            } catch (NoSuchMethodException ex) {
                // 尝试直接访问字段
                try {
                    Field field = obj.getClass().getDeclaredField(propertyName);
                    field.setAccessible(true);
                    return field.get(obj);
                } catch (NoSuchFieldException exc) {
                    return null;
                }
            }
        }
    }

    /**
     * 获取索引属性
     */
    private static Object getIndexedProperty(Object obj, int index) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof List<?> list) {
            return index >= 0 && index < list.size() ? list.get(index) : null;
        }

        if (obj.getClass().isArray()) {
            return index >= 0 && index < Array.getLength(obj) ? Array.get(obj, index) : null;
        }

        return null;
    }

    /**
     * 首字母大写
     */
    private static String capitalize(String str) {
        if (!StringUtils.hasText(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
