package cn.tannn.jdevelops.autoschema.scan;

import cn.tannn.jdevelops.autoschema.DatabaseInitializer;
import cn.tannn.jdevelops.autoschema.properties.DataBaseProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启数据库自动初始化
 * 使用ApplicationReadyEvent机制，避免BeanPostProcessor的循环依赖问题
 * @author tnnn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DataBaseProperties.class, DatabaseInitializer.class})
public @interface EnableAutoSchema {
}
