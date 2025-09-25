package cn.tannn.jdevelops.jdectemplate.sql.v2;

import cn.tannn.jdevelops.jdectemplate.annotations.SqlIgnore;
import cn.tannn.jdevelops.jdectemplate.annotations.SqlReColumn;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static cn.tannn.jdevelops.jdectemplate.sql.SqlUtil.camelToSnake;

/**
 * SELECT字段构建
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/9/25 09:51
 */
public class SqlFieldBuilder {
    /**
     * 构建SELECT字段
     */
    public static String buildSelectFields(Class<?> returnClazz) {
        return buildSelectFields(returnClazz, null);
    }

    /**
     * 构建SELECT字段
     * @param returnClazz 返回对象
     * @param customSelect 自定义返回对象字符串，注意规则是以数据库字段为准
     */
    public static String buildSelectFields(Class<?> returnClazz, String customSelect) {

        if (StringUtils.hasText(customSelect)) {
            return customSelect;
        }
        if (returnClazz == null) {
            return "*"; // 默认 fallback
        }

        Field[] fields = returnClazz.getDeclaredFields();
        List<String> fieldsList = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(SqlIgnore.class)) {
                continue;
            }

            SqlReColumn reColumn = field.getAnnotation(SqlReColumn.class);

            if (reColumn != null) {
                fieldsList.add(reColumn.alias());
            } else {
                // 默认使用驼峰转下划线
                String columnName = camelToSnake(field.getName());
                fieldsList.add(columnName);
            }
        }

        return fieldsList.isEmpty() ? "*" : String.join(", ", fieldsList);
    }
}
