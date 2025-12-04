package cn.tannn.jdevelops.jdectemplate.xmlmapper.example;

import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlDelete;
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlInsert;
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlMapper;
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlSelect;
import cn.tannn.jdevelops.annotations.jdbctemplate.xml.XmlUpdate;

import java.util.List;

/**
 * 用户 Mapper 接口（示例）
 * <p>
 * 配合 XML Mapper 使用，提供类型安全的方法定义
 * <p>
 * 对应 XML 文件：resources/jmapper/UserMapper.xml
 * <p>
 * namespace: cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserMapper
 *
 * @author tnnn
 */
@XmlMapper(namespace = "cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserMapper")
public interface UserMapper {

    /**
     * 根据 ID 查询用户
     * <p>
     * 对应 XML 中的 &lt;select id="findById"&gt;
     *
     * @param query 查询参数（包含 id 字段）
     * @return 用户对象，如果不存在返回 null
     */
    @XmlSelect("findById")
    User findById(UserQuery query);

    /**
     * 动态条件查询用户列表
     * <p>
     * 对应 XML 中的 &lt;select id="findUsers"&gt;
     * <p>
     * 支持的查询条件：
     * <ul>
     *     <li>username - 用户名（模糊匹配）</li>
     *     <li>email - 邮箱（精确匹配）</li>
     *     <li>status - 状态（精确匹配）</li>
     *     <li>minAge - 最小年龄</li>
     *     <li>maxAge - 最大年龄</li>
     * </ul>
     *
     * @param query 查询参数对象
     * @return 用户列表
     */
    @XmlSelect("findUsers")
    List<User> findUsers(UserQuery query);

    /**
     * 根据 ID 列表批量查询用户
     * <p>
     * 对应 XML 中的 &lt;select id="findByIds"&gt;
     *
     * @param query 查询参数（包含 ids 列表）
     * @return 用户列表
     */
    @XmlSelect("findByIds")
    List<User> findByIds(UserQuery query);

    /**
     * 插入用户
     * <p>
     * 对应 XML 中的 &lt;insert id="insertUser"&gt;
     *
     * @param user 用户对象
     * @return 影响的行数
     */
    @XmlInsert("insertUser")
    int insertUser(User user);

    /**
     * 批量插入用户
     * <p>
     * 对应 XML 中的 &lt;insert id="batchInsert"&gt;
     *
     * @param query 查询参数（包含 users 列表）
     * @return 影响的行数
     */
    @XmlInsert("batchInsert")
    int batchInsert(UserQuery query);

    /**
     * 动态更新用户信息
     * <p>
     * 对应 XML 中的 &lt;update id="updateUser"&gt;
     * <p>
     * 只更新非 null 的字段
     *
     * @param user 用户对象（id 必须，其他字段可选）
     * @return 影响的行数
     */
    @XmlUpdate("updateUser")
    int updateUser(User user);

    /**
     * 根据 ID 删除用户
     * <p>
     * 对应 XML 中的 &lt;delete id="deleteById"&gt;
     *
     * @param user 用户对象（只需要 id）
     * @return 影响的行数
     */
    @XmlDelete("deleteById")
    int deleteById(User user);

    /**
     * 根据 ID 列表批量删除用户
     * <p>
     * 对应 XML 中的 &lt;delete id="deleteByIds"&gt;
     *
     * @param query 查询参数（包含 ids 列表）
     * @return 影响的行数
     */
    @XmlDelete("deleteByIds")
    int deleteByIds(UserQuery query);

    /**
     * 统计用户数量
     * <p>
     * 对应 XML 中的 &lt;select id="countUsers"&gt;
     *
     * @param query 查询参数（支持 status 条件）
     * @return 用户数量
     */
    @XmlSelect("countUsers")
    Integer countUsers(UserQuery query);

    /**
     * 分页查询用户
     * <p>
     * 对应 XML 中的 &lt;select id="findUsersPage"&gt;
     *
     * @param query 查询参数（包含 pageSize 和 offset）
     * @return 用户列表
     */
    @XmlSelect("findUsersPage")
    List<User> findUsersPage(UserQuery query);

    /**
     * 高级查询 - 支持关键字搜索、状态列表、日期范围、自定义排序
     * <p>
     * 对应 XML 中的 &lt;select id="findUsersAdvanced"&gt;
     *
     * @param query 查询参数
     * @return 用户列表
     */
    @XmlSelect("findUsersAdvanced")
    List<User> findUsersAdvanced(UserQuery query);
}
