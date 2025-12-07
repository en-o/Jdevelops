package cn.tannn.jdevelops.annotations.jdbctemplate.xml;

import java.lang.annotation.*;

/**
 * XML Mapper 分页查询注解
 * <p>用于标记 XML Mapper 接口中的分页查询方法</p>
 * <p>框架会自动执行数据查询和统计查询，并返回 {@link cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult}</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * &#64;XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
 * public interface UserMapper {
 *     // 自动分页查询
 *     &#64;XmlPageSelect(dataStatement = "findUsers", countStatement = "countUsers")
 *     PageResult&lt;User&gt; findUsersPage(UserQuery query, PageRequest pageRequest);
 *
 *     // 使用默认的统计SQL（自动将dataStatement的select替换为count）
 *     &#64;XmlPageSelect(dataStatement = "findUsers")
 *     PageResult&lt;User&gt; findUsersPageAuto(UserQuery query, PageRequest pageRequest);
 * }
 * </pre>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *     <li>方法必须返回 {@link cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult} 类型</li>
 *     <li>方法参数必须包含 {@link cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest} 类型参数</li>
 *     <li>如果不指定 countStatement，框架会自动生成统计SQL</li>
 *     <li>查询参数会同时传递给数据查询和统计查询</li>
 * </ul>
 *
 * @author tnnn
 * @see XmlMapper
 * @see XmlSelect
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface XmlPageSelect {

    /**
     * 数据查询 SQL 语句 ID
     * <p>对应 XML 文件中 &lt;select&gt; 标签的 id 属性</p>
     * <p>用于查询分页数据列表</p>
     *
     * @return 数据查询 SQL 语句 ID
     */
    String dataStatement();

    /**
     * 统计查询 SQL 语句 ID（可选）
     * <p>对应 XML 文件中 &lt;select&gt; 标签的 id 属性</p>
     * <p>用于统计符合条件的总记录数</p>
     * <p>如果不指定，框架会尝试自动生成统计SQL（通过将数据查询SQL的SELECT部分替换为COUNT(*)）</p>
     *
     * @return 统计查询 SQL 语句 ID，默认为空字符串（自动生成）
     */
    String countStatement() default "";

    /**
     * 是否吞掉查询异常
     * <p>当设置为 true 时，查询失败不抛出异常，返回空的 PageResult</p>
     * <p>默认为 false，查询失败时抛出异常</p>
     *
     * @return 是否吞掉异常，默认 false
     */
    boolean tryc() default false;
}
