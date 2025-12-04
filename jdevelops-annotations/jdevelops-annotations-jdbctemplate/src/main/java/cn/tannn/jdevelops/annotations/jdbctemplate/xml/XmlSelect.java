package cn.tannn.jdevelops.annotations.jdbctemplate.xml;

import java.lang.annotation.*;

/**
 * XML Mapper 查询注解
 * <p>用于标记 XML Mapper 接口中的查询方法</p>
 * <p>配合 {@link XmlMapper} 注解使用</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * &#64;XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
 * public interface UserMapper {
 *     &#64;XmlSelect("findById")
 *     User findById(UserQuery query);
 *
 *     &#64;XmlSelect(value = "findByIdTryc", tryc = true)
 *     User findByIdWithTryc(UserQuery query);
 * }
 * </pre>
 *
 * @author tnnn
 * @see XmlMapper
 * @see XmlInsert
 * @see XmlUpdate
 * @see XmlDelete
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface XmlSelect {

    /**
     * SQL 语句 ID
     * <p>对应 XML 文件中 &lt;select&gt; 标签的 id 属性</p>
     *
     * @return SQL 语句 ID
     */
    String value();

    /**
     * 是否吞掉查询异常
     * <p>当设置为 true 时，查询失败不抛出异常，返回 null 或空集合</p>
     * <p>默认为 false，查询失败时抛出异常</p>
     *
     * @return 是否吞掉异常，默认 false
     */
    boolean tryc() default false;
}
