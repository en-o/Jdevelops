package cn.tannn.jdevelops.autoschema.scan;

import cn.tannn.jdevelops.autoschema.LocalDataSourceLoader;
import cn.tannn.jdevelops.autoschema.properties.DataBaseProperties;
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
