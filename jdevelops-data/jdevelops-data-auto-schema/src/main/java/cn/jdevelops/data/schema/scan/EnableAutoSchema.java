package cn.jdevelops.data.schema.scan;

import cn.jdevelops.data.schema.properties.DataBaseProperties;
import cn.jdevelops.data.schema.LocalDataSourceLoader;
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
