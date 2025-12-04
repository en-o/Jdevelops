package cn.tannn.jdevelops.annotations.jdbctemplate.xml;

import java.lang.annotation.*;

/**
 * XML Mapper 插入注解
 * <p>用于标记 XML Mapper 接口中的插入方法</p>
 * <p>配合 {@link XmlMapper} 注解使用</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * &#64;XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
 * public interface UserMapper {
 *     &#64;XmlInsert("insertUser")
 *     int insertUser(User user);
 *
 *     &#64;XmlInsert("batchInsert")
 *     int batchInsert(UserQuery query);
 * }
 * </pre>
 *
 * @author tnnn
 * @see XmlMapper
 * @see XmlSelect
 * @see XmlUpdate
 * @see XmlDelete
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface XmlInsert {

    /**
     * SQL 语句 ID
     * <p>对应 XML 文件中 &lt;insert&gt; 标签的 id 属性</p>
     *
     * @return SQL 语句 ID
     */
    String value();
}
