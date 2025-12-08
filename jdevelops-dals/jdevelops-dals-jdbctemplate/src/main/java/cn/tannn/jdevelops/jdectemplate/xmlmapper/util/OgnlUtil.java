package cn.tannn.jdevelops.jdectemplate.xmlmapper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(OgnlUtil.class);

    /**
     * 获取对象属性值
     *
     * @param expression 表达式(如 "user.name" 或 "list[0]")
     * @param root       根对象
     * @return 属性值
     */
    public static Object getValue(String expression, Object root) {
        if (!StringUtils.hasText(expression)) {
            return null;
        }

        if (root == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getValue: expression={}, root=null -> null", expression);
            }
            return null;
        }

        try {
            // 处理特殊集合名称：当参数直接是 List 时，使用 "list" 作为别名
            if ("list".equals(expression) && root instanceof List) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getValue: special 'list' alias for List parameter, value={}", root);
                }
                return root;
            }

            // 处理特殊集合名称：当参数直接是数组时，使用 "array" 作为别名
            if ("array".equals(expression) && root.getClass().isArray()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getValue: special 'array' alias for array parameter, value={}", root);
                }
                return root;
            }

            // 处理 MyBatis 风格的参数索引：当 root 是 Map 时，支持 arg0、arg1、param1、param2 等方式访问
            // 这是多参数方法的场景，XmlMapperProxyInterceptor 会将多个参数包装成 Map
            if (root instanceof Map) {
                Map<?, ?> paramMap = (Map<?, ?>) root;

                // 处理 arg0.property、arg1.property、param1.property 形式
                if ((expression.startsWith("arg") || expression.startsWith("param"))
                    && (expression.contains(".") || expression.contains("["))) {
                    int dotIndex = expression.indexOf('.');
                    int bracketIndex = expression.indexOf('[');
                    int separatorIndex = dotIndex > 0 ? (bracketIndex > 0 ? Math.min(dotIndex, bracketIndex) : dotIndex)
                                                      : bracketIndex;

                    if (separatorIndex > 0) {
                        String keyPart = expression.substring(0, separatorIndex);  // "arg0" or "param1"
                        String restPart = expression.substring(separatorIndex);    // ".property" or "[0]"

                        if (paramMap.containsKey(keyPart)) {
                            Object paramValue = paramMap.get(keyPart);
                            // 递归处理剩余部分
                            Object value = getValue(restPart.substring(1), paramValue); // 去掉开头的 '.' 或 '['
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("getValue: Map key '{}' parameter access, restPart={}, paramValue type={}, value={}",
                                          keyPart, restPart,
                                          paramValue != null ? paramValue.getClass().getSimpleName() : "null",
                                          value);
                            }
                            return value;
                        }
                    }
                }

                // 处理单独的 arg0、arg1、param1、param2（不带属性访问）
                if (expression.matches("(arg\\d+|param\\d+)")) {
                    if (paramMap.containsKey(expression)) {
                        Object value = paramMap.get(expression);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("getValue: Map key '{}' parameter direct access, value type={}",
                                      expression, value != null ? value.getClass().getSimpleName() : "null");
                        }
                        return value;
                    }
                }
            }

            // 处理 MyBatis 风格的参数索引：当 root 是 List 时，支持 arg0、arg1、param1、param2 等方式访问
            // 保留对旧版本 List 方式的兼容性支持
            if (root instanceof List<?> paramList) {
                // 处理 arg0.property、arg1.property 形式
                if (expression.startsWith("arg") && (expression.contains(".") || expression.contains("["))) {
                    int dotIndex = expression.indexOf('.');
                    int bracketIndex = expression.indexOf('[');
                    int separatorIndex = dotIndex > 0 ? (bracketIndex > 0 ? Math.min(dotIndex, bracketIndex) : dotIndex)
                                                      : bracketIndex;

                    if (separatorIndex > 0) {
                        String indexPart = expression.substring(0, separatorIndex);
                        String restPart = expression.substring(separatorIndex);

                        try {
                            int index = Integer.parseInt(indexPart.substring(3)); // "arg0" -> 0
                            if (index >= 0 && index < paramList.size()) {
                                Object paramValue = paramList.get(index);
                                // 递归处理剩余部分
                                Object value = getValue(restPart.substring(1), paramValue); // 去掉开头的 '.' 或 '['
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("getValue: arg{} parameter access, restPart={}, paramValue type={}, value={}",
                                              index, restPart, paramValue.getClass().getSimpleName(), value);
                                }
                                return value;
                            }
                        } catch (NumberFormatException e) {
                            // 不是合法的 argN 格式，继续正常处理
                        }
                    }
                }

                // 处理单独的 arg0、arg1（不带属性访问）
                if (expression.matches("arg\\d+")) {
                    try {
                        int index = Integer.parseInt(expression.substring(3));
                        if (index >= 0 && index < paramList.size()) {
                            Object value = paramList.get(index);
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("getValue: arg{} parameter direct access, value type={}",
                                          index, value.getClass().getSimpleName());
                            }
                            return value;
                        }
                    } catch (NumberFormatException e) {
                        // 不是合法的 argN 格式，继续正常处理
                    }
                }
            }

            // 处理简单属性
            if (!expression.contains(".") && !expression.contains("[")) {
                return getSimpleProperty(root, expression);
            }

            // 处理复杂表达式
            return evaluateExpression(expression, root);
        } catch (Exception e) {
            LOG.error("🔍 [OGNL DEBUG] Exception in getValue - expression: {}, root type: {}, error: {}",
                      expression, root != null ? root.getClass().getSimpleName() : "null", e.getMessage(), e);
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

        if (LOG.isDebugEnabled()) {
            LOG.debug("evaluateBoolean called - expression: {}, root type: {}",
                     expression, root != null ? root.getClass().getSimpleName() : "null");
        }

        try {
            // 处理逻辑运算（优先处理，因为可能包含其他运算符）
            if (expression.contains(" and ") || expression.contains(" && ")) {
                String[] parts = expression.split("(\\s+and\\s+|\\s*&&\\s*)");
                for (String part : parts) {
                    if (!evaluateBoolean(part.trim(), root)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("evaluateBoolean: AND expression failed at part: {}", part);
                        }
                        return false;
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("evaluateBoolean: AND expression passed: {}", expression);
                }
                return true;
            }

            if (expression.contains(" or ") || expression.contains(" || ")) {
                String[] parts = expression.split("(\\s+or\\s+|\\s*\\|\\|\\s*)");
                for (String part : parts) {
                    if (evaluateBoolean(part.trim(), root)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("evaluateBoolean: OR expression passed at part: {}", part);
                        }
                        return true;
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("evaluateBoolean: OR expression failed: {}", expression);
                }
                return false;
            }

            // 处理比较运算符（按优先级：先处理两个字符的运算符，再处理单字符的）
            // 处理 >=
            if (expression.contains(">=")) {
                String[] parts = expression.split(">=");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    boolean result = evaluateGreaterThanOrEquals(left, right);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("evaluateBoolean: >= comparison, left={}, right={}, result={}", left, right, result);
                    }
                    return result;
                }
            }

            // 处理 <=
            if (expression.contains("<=")) {
                String[] parts = expression.split("<=");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    boolean result = evaluateLessThanOrEquals(left, right);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("evaluateBoolean: <= comparison, left={}, right={}, result={}", left, right, result);
                    }
                    return result;
                }
            }

            // 处理 !=
            if (expression.contains("!=")) {
                String[] parts = expression.split("!=");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    boolean result = evaluateNotEquals(left, right);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("evaluateBoolean: != comparison, left={}, right={}, result={}", left, right, result);
                    }
                    return result;
                }
            }

            // 处理 ==
            if (expression.contains("==")) {
                String[] parts = expression.split("==");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    boolean result = evaluateEquals(left, right);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("evaluateBoolean: == comparison, left={}, right={}, result={}", left, right, result);
                    }
                    return result;
                }
            }

            // 处理 > (必须在 >= 之后检查)
            if (expression.contains(">") && !expression.contains(">=")) {
                String[] parts = expression.split(">");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    boolean result = evaluateGreaterThan(left, right);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("evaluateBoolean: > comparison, left={}, right={}, result={}", left, right, result);
                    }
                    return result;
                }
            }

            // 处理 < (必须在 <= 之后检查)
            if (expression.contains("<") && !expression.contains("<=")) {
                String[] parts = expression.split("<");
                if (parts.length == 2) {
                    Object left = getValue(parts[0].trim(), root);
                    String right = parts[1].trim();
                    boolean result = evaluateLessThan(left, right);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("evaluateBoolean: < comparison, left={}, right={}, result={}", left, right, result);
                    }
                    return result;
                }
            }

            // 处理非空判断
            Object value = getValue(expression, root);
            boolean result = isNotEmpty(value);
            if (LOG.isDebugEnabled()) {
                LOG.debug("evaluateBoolean result - expression: {}, value: {}, result: {}", expression, value, result);
            }
            return result;
        } catch (Exception e) {
            LOG.error("🔍 [OGNL DEBUG] Exception in evaluateBoolean - expression: {}, error: {}", expression, e.getMessage(), e);
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
     * 评估大于
     */
    private static boolean evaluateGreaterThan(Object left, String right) {
        if (left == null) {
            return false;
        }
        try {
            double leftNum = convertToNumber(left);
            double rightNum = Double.parseDouble(removeQuotes(right));
            return leftNum > rightNum;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("evaluateGreaterThan: failed to convert to number, left={}, right={}", left, right);
            }
            return false;
        }
    }

    /**
     * 评估小于
     */
    private static boolean evaluateLessThan(Object left, String right) {
        if (left == null) {
            return false;
        }
        try {
            double leftNum = convertToNumber(left);
            double rightNum = Double.parseDouble(removeQuotes(right));
            return leftNum < rightNum;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("evaluateLessThan: failed to convert to number, left={}, right={}", left, right);
            }
            return false;
        }
    }

    /**
     * 评估大于等于
     */
    private static boolean evaluateGreaterThanOrEquals(Object left, String right) {
        if (left == null) {
            return false;
        }
        try {
            double leftNum = convertToNumber(left);
            double rightNum = Double.parseDouble(removeQuotes(right));
            return leftNum >= rightNum;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("evaluateGreaterThanOrEquals: failed to convert to number, left={}, right={}", left, right);
            }
            return false;
        }
    }

    /**
     * 评估小于等于
     */
    private static boolean evaluateLessThanOrEquals(Object left, String right) {
        if (left == null) {
            return false;
        }
        try {
            double leftNum = convertToNumber(left);
            double rightNum = Double.parseDouble(removeQuotes(right));
            return leftNum <= rightNum;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("evaluateLessThanOrEquals: failed to convert to number, left={}, right={}", left, right);
            }
            return false;
        }
    }

    /**
     * 将对象转换为数值
     */
    private static double convertToNumber(Object obj) throws NumberFormatException {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return Double.parseDouble(obj.toString());
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

            // 处理方法调用（如 name()）
            if (part.endsWith("()")) {
                String methodName = part.substring(0, part.length() - 2);
                current = invokeMethod(current, methodName);
            }
            // 处理数组/列表访问
            else if (part.contains("[")) {
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("getSimpleProperty: obj={}, propertyName={} -> null (empty input)", obj, propertyName);
            }
            return null;
        }

        // 如果是 Map
        if (obj instanceof Map) {
            Object value = ((Map<?, ?>) obj).get(propertyName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("getSimpleProperty: Map access, propertyName={}, value={}", propertyName, value);
            }
            return value;
        }

        // 尝试 getter 方法
        try {
            String getterName = "get" + capitalize(propertyName);
            Method getter = obj.getClass().getMethod(getterName);
            Object value = getter.invoke(obj);
            if (LOG.isDebugEnabled()) {
                LOG.debug("getSimpleProperty: getter method '{}' on {}, value={}", getterName, obj.getClass().getSimpleName(), value);
            }
            return value;
        } catch (NoSuchMethodException e) {
            // 尝试 is 方法（for boolean）
            try {
                String isGetterName = "is" + capitalize(propertyName);
                Method getter = obj.getClass().getMethod(isGetterName);
                Object value = getter.invoke(obj);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getSimpleProperty: is-getter method '{}' on {}, value={}", isGetterName, obj.getClass().getSimpleName(), value);
                }
                return value;
            } catch (NoSuchMethodException ex) {
                // 尝试直接访问字段
                try {
                    Field field = obj.getClass().getDeclaredField(propertyName);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("getSimpleProperty: field access '{}' on {}, value={}", propertyName, obj.getClass().getSimpleName(), value);
                    }
                    return value;
                } catch (NoSuchFieldException exc) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("getSimpleProperty: property '{}' not found on {}", propertyName, obj.getClass().getSimpleName());
                    }
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

    /**
     * 调用无参方法
     * 支持枚举的 name()、ordinal() 等方法，以及 record 类的访问器方法
     *
     * @param obj 对象
     * @param methodName 方法名（不含括号）
     * @return 方法返回值
     * @throws Exception 调用失败时抛出异常
     */
    private static Object invokeMethod(Object obj, String methodName) throws Exception {
        if (obj == null || !StringUtils.hasText(methodName)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("invokeMethod: obj={}, methodName={} -> null (empty input)", obj, methodName);
            }
            return null;
        }

        // 优先处理常用集合方法，直接通过接口调用（避免 Java 9+ 模块访问限制）
        // java.base 模块不 opens java.util 给未命名模块，setAccessible(true) 会抛出 InaccessibleObjectException
        if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            switch (methodName) {
                case "size":
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("invokeMethod: Collection.size() on {}, value={}",
                                 obj.getClass().getSimpleName(), collection.size());
                    }
                    return collection.size();
                case "isEmpty":
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("invokeMethod: Collection.isEmpty() on {}, value={}",
                                 obj.getClass().getSimpleName(), collection.isEmpty());
                    }
                    return collection.isEmpty();
            }
        }

        // 处理 Map 的常用方法
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            switch (methodName) {
                case "size":
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("invokeMethod: Map.size() on {}, value={}",
                                 obj.getClass().getSimpleName(), map.size());
                    }
                    return map.size();
                case "isEmpty":
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("invokeMethod: Map.isEmpty() on {}, value={}",
                                 obj.getClass().getSimpleName(), map.isEmpty());
                    }
                    return map.isEmpty();
            }
        }

        // 处理数组的 length 属性（虽然不是方法调用，但为了一致性支持）
        if (obj.getClass().isArray() && "length".equals(methodName)) {
            int length = Array.getLength(obj);
            if (LOG.isDebugEnabled()) {
                LOG.debug("invokeMethod: array.length on {}, value={}",
                         obj.getClass().getSimpleName(), length);
            }
            return length;
        }

        try {
            // 对于其他方法，使用反射调用（如枚举的 name()、ordinal()，record 的访问器等）
            Method method = obj.getClass().getMethod(methodName);

            // 尝试设置可访问性，但捕获 InaccessibleObjectException
            // 对于 java.base 模块的受保护类，setAccessible 会失败
            try {
                method.setAccessible(true);
            } catch (Exception ignored) {
                // 如果 setAccessible 失败，继续尝试直接调用（public 方法可以直接调用）
                if (LOG.isDebugEnabled()) {
                    LOG.debug("invokeMethod: setAccessible failed for '{}()' on {}, trying direct invoke",
                             methodName, obj.getClass().getSimpleName());
                }
            }

            Object value = method.invoke(obj);
            if (LOG.isDebugEnabled()) {
                LOG.debug("invokeMethod: method '{}()' on {}, value={}",
                         methodName, obj.getClass().getSimpleName(), value);
            }
            return value;
        } catch (NoSuchMethodException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("invokeMethod: method '{}()' not found on {}",
                         methodName, obj.getClass().getSimpleName());
            }
            throw new Exception("Method '" + methodName + "()' not found on " + obj.getClass().getName(), e);
        }
    }
}
