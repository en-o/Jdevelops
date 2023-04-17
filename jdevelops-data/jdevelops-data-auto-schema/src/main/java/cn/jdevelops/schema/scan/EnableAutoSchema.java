package cn.jdevelops.schema.scan;

import cn.jdevelops.schema.LocalDataSourceLoader;
import cn.jdevelops.schema.properties.DataBaseProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启自动建库
 * @author tnnn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DataBaseProperties.class, LocalDataSourceLoader.class})
public  @interface EnableAutoSchema {

}
