package cn.tannn.jdevelops.jdectemplate.sql.v2;

import cn.tannn.jdevelops.jdectemplate.annotations.SqlPage;
import cn.tannn.jdevelops.jdectemplate.sql.DynamicSqlBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        processPageRobust(builder,queryObj);
    }


    /**
     * 处理分页信息对象
     * @param builder builder
     * @param pageObj page
     */
    public static void processPageRobust(DynamicSqlBuilder builder, Object pageObj) {
        Class<?> clazz = pageObj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Integer pageIndex = null;
        Integer pageSize = null;
        Object pageInfo = null;

        for (Field field : fields) {
            field.setAccessible(true);
            SqlPage pageAnnotation = field.getAnnotation(SqlPage.class);

            if (pageAnnotation != null) {
                try {
                    Object value = field.get(pageObj);
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

        // 处理分页信息对象 - 更健壮的方式
        if (pageInfo != null) {
            pageIndex = extractPageIndex(pageInfo);
            pageSize = extractPageSize(pageInfo);
        }

        // 应用分页
        if (pageIndex != null && pageSize != null) {
            builder.pageZero(pageIndex, pageSize);
        }
    }


    /**
     * 提取页码，优先使用getter方法
     * 默认0
     */
    private static Integer extractPageIndex(Object pageInfo) {
        // 尝试通过getPageIndex方法获取
        try {
            Method method = pageInfo.getClass().getMethod("getPageIndex");
            return (Integer) method.invoke(pageInfo);
        } catch (Exception e) {
            // 忽略
        }

        // 回退到字段访问
        try {
            Field field = pageInfo.getClass().getDeclaredField("pageIndex");
            field.setAccessible(true);
            return (Integer) field.get(pageInfo);
        } catch (Exception e) {
            // 忽略
        }

        return 0;
    }

    /**
     * 提取页大小，优先使用getter方法(默认10)
     */
    private static Integer extractPageSize(Object pageInfo) {
        // 尝试通过getPageSize方法获取
        try {
            Method method = pageInfo.getClass().getMethod("getPageSize");
            return (Integer) method.invoke(pageInfo);
        } catch (Exception e) {
            // 忽略
        }

        // 回退到字段访问
        try {
            Field field = pageInfo.getClass().getDeclaredField("pageSize");
            field.setAccessible(true);
            return (Integer) field.get(pageInfo);
        } catch (Exception e) {
            // 忽略
        }

        return 10;
    }
}
