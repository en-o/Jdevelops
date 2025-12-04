package cn.tannn.jdevelops.annotations.jdbctemplate.xml;

import java.lang.annotation.*;

/**
 * XML Mapper 接口标记注解
 * <p>用于标记一个接口是 XML Mapper 接口，配合 XML 配置文件使用</p>
 * <p>作用在接口上，表示该接口对应一个 XML Mapper 文件</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * &#64;XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
 * public interface UserMapper {
 *     &#64;XmlSelect("findById")
 *     User findById(UserQuery query);
 * }
 * </pre>
 *
 * @author tnnn
 * @see XmlSelect
 * @see XmlInsert
 * @see XmlUpdate
 * @see XmlDelete
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface XmlMapper {

    /**
     * XML Mapper 的命名空间
     * <p>对应 XML 文件中的 namespace 属性</p>
     * <p>如果不指定，则使用接口的全限定类名作为 namespace</p>
     *
     * @return XML Mapper 命名空间
     */
    String namespace() default "";

    /**
     * XML 文件路径
     * <p>可选，用于指定对应的 XML 文件路径</p>
     * <p>如果不指定，则根据命名空间自动查找</p>
     *
     * @return XML 文件路径
     */
    String xmlPath() default "";
}
