package cn.tannn.jdevelops.jdectemplate.sql.v2;

import cn.tannn.jdevelops.jdectemplate.annotations.SqlPage;
import cn.tannn.jdevelops.jdectemplate.sql.DynamicSqlBuilder;

import java.lang.reflect.Field;

/**
 * 分页处理
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/9/25 09:57
 */
public class SqlPageProcessor {

    /**
     * 处理分页
     */
    public static void processPage(DynamicSqlBuilder builder, Object queryObj) {
        Class<?> clazz = queryObj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Integer pageIndex = null;
        Integer pageSize = null;
        Object pageInfo = null;

        for (Field field : fields) {
            field.setAccessible(true);
            SqlPage pageAnnotation = field.getAnnotation(SqlPage.class);

            if (pageAnnotation != null) {
                try {
                    Object value = field.get(queryObj);
                    switch (pageAnnotation.type()) {
                        case PAGE_INDEX:
                            pageIndex = (Integer) value;
                            break;
                        case PAGE_SIZE:
                            pageSize = (Integer) value;
                            break;
                        case PAGE_INFO:
                            pageInfo = value;
                            break;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing page field: " + field.getName(), e);
                }
            }
        }

        // 处理分页信息对象
        if (pageInfo != null) {
            try {
                Field pageIndexField = pageInfo.getClass().getDeclaredField("pageIndex");
                Field pageSizeField = pageInfo.getClass().getDeclaredField("pageSize");

                pageIndexField.setAccessible(true);
                pageSizeField.setAccessible(true);

                pageIndex = (Integer) pageIndexField.get(pageInfo);
                pageSize = (Integer) pageSizeField.get(pageInfo);
            } catch (Exception e) {
                // 忽略错误，可能字段名不匹配
            }
        }

        // 应用分页
        if (pageIndex != null && pageSize != null) {
            builder.pageZero(pageIndex, pageSize);
        }
    }
}
