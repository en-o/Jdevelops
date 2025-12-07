package cn.tannn.jdevelops.annotations.jdbctemplate.xml;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * XML Mapper 接口标记注解
 * <p>用于标记一个接口是 XML Mapper 接口，配合 XML 配置文件使用</p>
 * <p>作用在接口上，表示该接口对应一个 XML Mapper 文件</p>
 * <p>标记为 Spring 组件，框架会自动为其创建代理对象并注册到 Spring 容器中</p>
 *
 * <h3>核心属性说明：</h3>
 * <ul>
 *   <li><b>value</b>: Spring Bean 名称（可选）
 *       <br>- 默认值：接口简单名称首字母小写（如 UserMapper → userMapper）
 *       <br>- 用途：自定义 Bean 名称，避免名称冲突
 *       <br>- 实现位置：XmlMapperScannerRegistrar#generateBeanName
 *   </li>
 *   <li><b>namespace</b>: XML Mapper 命名空间（推荐填写）
 *       <br>- 默认值：接口全限定类名（如 com.example.mapper.UserMapper）
 *       <br>- 用途：关联接口与 XML 文件，必须与 XML 文件中的 namespace 属性一致
 *       <br>- 实现位置：XmlMapperProxyFactory#getNamespace
 *   </li>
 * </ul>
 *
 * <h3>基本使用示例：</h3>
 * <pre>
 * &#64;XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
 * public interface UserMapper {
 *     &#64;XmlSelect("findById")
 *     User findById(UserQuery query);
 * }
 * // Bean 名称: userMapper (默认)
 * // Namespace: cn.tannn.jdevelops.mapper.UserMapper (显式指定)
 * </pre>
 *
 * <h3>自定义 Bean 名称示例：</h3>
 * <pre>
 * &#64;XmlMapper(value = "myUserMapper", namespace = "cn.tannn.jdevelops.mapper.UserMapper")
 * public interface UserMapper {
 *     // Bean 名称: myUserMapper (自定义)
 *     // Namespace: cn.tannn.jdevelops.mapper.UserMapper (显式指定)
 * }
 * </pre>
 *
 * <h3>默认行为示例：</h3>
 * <pre>
 * &#64;XmlMapper  // 不指定任何属性
 * public interface UserMapper {
 *     // Bean 名称: userMapper (接口名首字母小写)
 *     // Namespace: com.example.mapper.UserMapper (接口全限定类名)
 * }
 * </pre>
 *
 * <h3>XML 文件配置：</h3>
 * <p>通过全局配置的 locations 扫描 XML 文件：</p>
 * <pre>
 * spring:
 *   jdevelops:
 *     jdbctemplate:
 *       xml-mapper:
 *         enabled: true
 *         locations: classpath*:jmapper/※※/※.xml
 * </pre>
 *
 * <h3>对应的 XML 文件：</h3>
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
 *         "http://mybatis.org/dtd/mybatis-3-mapper.dtd"&gt;
 *
 * &lt;mapper namespace="cn.tannn.jdevelops.mapper.UserMapper"&gt;
 *     &lt;!-- namespace 必须与注解中的 namespace 属性一致 --&gt;
 *     &lt;select id="findById" resultType="User"&gt;
 *         SELECT * FROM users WHERE id = #{id}
 *     &lt;/select&gt;
 * &lt;/mapper&gt;
 * </pre>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>推荐显式指定 namespace，避免因类名重构导致的 XML 配置失效</li>
 *   <li>XML 文件的 namespace 必须与注解的 namespace 保持一致</li>
 *   <li>value 属性用于自定义 Spring Bean 名称，解决 Bean 名称冲突问题</li>
 *   <li>如果不指定 value，默认使用接口简单名称首字母小写</li>
 * </ul>
 *
 * @author tnnn
 * @see XmlSelect
 * @see XmlInsert
 * @see XmlUpdate
 * @see XmlDelete
 * @see XmlPageSelect
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Component
public @interface XmlMapper {

    /**
     * Spring Bean 名称（可选）
     * <p>用于自定义注册到 Spring 容器中的 Bean 名称</p>
     * <p>通过 @AliasFor 映射到 @Component 的 value 属性</p>
     *
     * <h4>默认值：</h4>
     * <p>如果不指定，默认使用接口简单名称首字母小写作为 Bean 名称</p>
     * <ul>
     *   <li>UserMapper → userMapper</li>
     *   <li>OrderMapper → orderMapper</li>
     * </ul>
     *
     * <h4>实现位置：</h4>
     * <p>XmlMapperScannerRegistrar#generateBeanName</p>
     * <pre>
     * private String generateBeanName(Class&lt;?&gt; mapperInterface) {
     *     XmlMapper annotation = mapperInterface.getAnnotation(XmlMapper.class);
     *     if (annotation != null &amp;&amp; !annotation.value().isEmpty()) {
     *         return annotation.value();  // 使用自定义名称
     *     }
     *     // 默认：接口简单名称首字母小写
     *     String simpleName = mapperInterface.getSimpleName();
     *     return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
     * }
     * </pre>
     *
     * <h4>使用场景：</h4>
     * <ul>
     *   <li>避免 Bean 名称冲突（多个模块有同名接口）</li>
     *   <li>统一命名规范（如添加前缀/后缀）</li>
     *   <li>明确 Bean 用途（如 userQueryMapper、userCommandMapper）</li>
     * </ul>
     *
     * <h4>使用示例：</h4>
     * <pre>
     * // 示例1: 自定义 Bean 名称
     * &#64;XmlMapper(value = "customUserMapper", namespace = "...")
     * public interface UserMapper {
     *     // Bean 名称: customUserMapper
     * }
     *
     * // 示例2: 默认 Bean 名称
     * &#64;XmlMapper(namespace = "...")
     * public interface UserMapper {
     *     // Bean 名称: userMapper（接口名首字母小写）
     * }
     *
     * // 示例3: 避免名称冲突
     * &#64;XmlMapper(value = "moduleAUserMapper", namespace = "...")
     * public interface UserMapper {  // 模块A
     *     // Bean 名称: moduleAUserMapper
     * }
     *
     * &#64;XmlMapper(value = "moduleBUserMapper", namespace = "...")
     * public interface UserMapper {  // 模块B
     *     // Bean 名称: moduleBUserMapper
     * }
     * </pre>
     *
     * @return Spring Bean 名称
     */
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

    /**
     * XML Mapper 命名空间（推荐填写）
     * <p>对应 XML 文件中的 &lt;mapper namespace="..."&gt; 属性</p>
     * <p>用于关联 Mapper 接口与 XML 配置文件</p>
     *
     * <h4>默认值：</h4>
     * <p>如果不指定，使用接口的全限定类名作为 namespace</p>
     * <ul>
     *   <li>com.example.mapper.UserMapper → com.example.mapper.UserMapper</li>
     *   <li>cn.tannn.jdevelops.mapper.OrderMapper → cn.tannn.jdevelops.mapper.OrderMapper</li>
     * </ul>
     *
     * <h4>实现位置：</h4>
     * <p>XmlMapperProxyFactory#getNamespace</p>
     * <pre>
     * private static String getNamespace(Class&lt;?&gt; mapperInterface) {
     *     XmlMapper annotation = mapperInterface.getAnnotation(XmlMapper.class);
     *     if (annotation != null &amp;&amp; !annotation.namespace().isEmpty()) {
     *         return annotation.namespace();  // 使用注解指定的 namespace
     *     }
     *     return mapperInterface.getName();  // 使用接口全限定类名
     * }
     * </pre>
     *
     * <h4>作用：</h4>
     * <ul>
     *   <li>关联 Mapper 接口与 XML 文件</li>
     *   <li>XML 文件中的 namespace 必须与此属性保持一致</li>
     *   <li>用于在 XmlMapperRegistry 中查找 SQL 语句</li>
     * </ul>
     *
     * <h4>推荐做法：</h4>
     * <ul>
     *   <li>显式指定 namespace，避免因类名重构导致 XML 配置失效</li>
     *   <li>使用接口的全限定类名，保持与 MyBatis 一致</li>
     *   <li>确保 XML 文件中的 namespace 与此属性完全一致（区分大小写）</li>
     * </ul>
     *
     * <h4>使用示例：</h4>
     * <pre>
     * // 示例1: 显式指定 namespace（推荐）
     * &#64;XmlMapper(namespace = "cn.tannn.jdevelops.mapper.UserMapper")
     * public interface UserMapper {
     *     // namespace: cn.tannn.jdevelops.mapper.UserMapper
     * }
     *
     * // 对应的 XML 文件
     * &lt;mapper namespace="cn.tannn.jdevelops.mapper.UserMapper"&gt;
     *     &lt;select id="findById" resultType="User"&gt;
     *         SELECT * FROM users WHERE id = #{id}
     *     &lt;/select&gt;
     * &lt;/mapper&gt;
     *
     * // 示例2: 不指定 namespace（使用默认值）
     * package com.example.mapper;
     *
     * &#64;XmlMapper  // namespace 默认为: com.example.mapper.UserMapper
     * public interface UserMapper {
     *     // namespace: com.example.mapper.UserMapper（接口全限定类名）
     * }
     * </pre>
     *
     * <h4>注意事项：</h4>
     * <ul>
     *   <li>namespace 必须与 XML 文件中的 namespace 完全一致</li>
     *   <li>namespace 区分大小写</li>
     *   <li>如果重构类名或包名，记得同步更新 XML 文件的 namespace</li>
     *   <li>推荐显式指定，避免默认行为导致的配置失效</li>
     * </ul>
     *
     * @return XML Mapper 命名空间
     */
    String namespace() default "";
}
