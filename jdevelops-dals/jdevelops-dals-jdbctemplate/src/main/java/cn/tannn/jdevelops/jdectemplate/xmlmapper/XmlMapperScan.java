package cn.tannn.jdevelops.jdectemplate.xmlmapper;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.config.XmlMapperScannerRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * XML Mapper 扫描注解
 * <p>在 Spring Boot 启动类或配置类上使用此注解来扫描 XML Mapper 接口</p>
 * <p>类似于 MyBatis 的 @MapperScan 注解</p>
 * <p>如果不指定扫描包路径，默认扫描标记类所在包及其子包（类似 @SpringBootApplication 的行为）</p>
 *
 * <pre>
 * // 示例1：扫描指定包
 * &#64;SpringBootApplication
 * &#64;XmlMapperScan("com.example.mapper")
 * public class Application {
 *     public static void main(String[] args) {
 *         SpringApplication.run(Application.class, args);
 *     }
 * }
 *
 * // 示例2：默认扫描当前包及子包
 * &#64;SpringBootApplication
 * &#64;XmlMapperScan  // 将扫描 com.example 及其子包
 * public class Application {
 *     public static void main(String[] args) {
 *         SpringApplication.run(Application.class, args);
 *     }
 * }
 * </pre>
 *
 * @author tnnn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(XmlMapperScannerRegistrar.class)
public @interface XmlMapperScan {

    /**
     * 扫描的基础包路径
     * <p>可以指定多个包路径</p>
     * <p>如果为空，默认扫描标记类所在包及其子包</p>
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * 扫描的基础包路径
     * <p>可以指定多个包路径</p>
     * <p>如果为空，默认扫描标记类所在包及其子包</p>
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * 指定基础类，将扫描这些类所在的包
     */
    Class<?>[] basePackageClasses() default {};
}
