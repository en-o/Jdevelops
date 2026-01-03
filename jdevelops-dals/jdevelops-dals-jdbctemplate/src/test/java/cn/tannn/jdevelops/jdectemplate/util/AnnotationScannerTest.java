package cn.tannn.jdevelops.jdectemplate.util;

import cn.tannn.jdevelops.annotations.jdbctemplate.proxysql.JdbcTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class AnnotationScannerTest {

    @Test
    void getClassesAnnotatedWithAny() {
        // 指定要扫描的基础包
        String basePackage = "com.example";
        // 查找所有带有任意注解的类
        Set<Class<?>> annotatedClasses = AnnotationScanner.getClassesAnnotatedWithAny(basePackage, List.of(Controller.class));
        for (Class<?> clazz : annotatedClasses) {
            System.out.println("Found class with any of the annotations: " + clazz.getName());
        }
    }

    @Test
    void getFieldsAnnotatedWithAny() {
        // 指定要扫描的基础包
        String basePackage = "com.example";
        // 查找所有带有任意注解的字段
        Set<Field> annotatedFields = AnnotationScanner.getFieldsAnnotatedWithAny(basePackage, Collections.singletonList(JdbcTemplate.class));
        for (Field field : annotatedFields) {
            System.out.println("Found field with any of the annotations: " + field.getDeclaringClass().getName() + "." + field.getName());
        }
    }
}
