package cn.tannn.jdevelops.jdectemplate.xmlmapper;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.example.User;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserQuery;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.registry.XmlMapperRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML Mapper 测试
 *
 * @author tnnn
 */
class XmlMapperTest {

    private XmlMapperRegistry registry;
    private JdbcTemplate jdbcTemplate;

    private static final String NAMESPACE = "cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserMapper";

    @BeforeEach
    void setUp() throws Exception {
        // 创建内存数据库
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema-test.sql")
                .addScript("classpath:data-test.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(dataSource);
        registry = new XmlMapperRegistry(jdbcTemplate);

        // 注册 XML Mapper
        registry.registerMapper("classpath:mapper/UserMapper.xml");
    }

    @Test
    void testFindById() {
        // 创建查询参数
        UserQuery query = new UserQuery();
        query.setIds(Arrays.asList(1L));

        // 执行查询
        Object result = registry.executeQuery(NAMESPACE, "findById", query, User.class);

        // 验证结果
        assertNotNull(result);
        if (result instanceof User) {
            User user = (User) result;
            assertEquals(1L, user.getId());
        }
    }

    @Test
    void testFindUsers() {
        // 创建查询参数
        UserQuery query = new UserQuery();
        query.setStatus(1);
        query.setMinAge(18);

        // 执行查询
        Object result = registry.executeQuery(NAMESPACE, "findUsers", query, User.class);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
    }

    @Test
    void testFindByIds() {
        // 创建查询参数
        UserQuery query = new UserQuery();
        query.setIds(Arrays.asList(1L, 2L, 3L));

        // 执行查询
        Object result = registry.executeQuery(NAMESPACE, "findByIds", query, User.class);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
    }

    @Test
    void testInsertUser() {
        // 创建用户对象
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setAge(25);
        user.setStatus(1);

        // 执行插入
        int rows = registry.executeUpdate(NAMESPACE, "insertUser", user);

        // 验证结果
        assertEquals(1, rows);
    }

    @Test
    void testUpdateUser() {
        // 创建更新参数
        User user = new User();
        user.setId(1L);
        user.setUsername("updated_user");
        user.setEmail("updated@example.com");

        // 执行更新
        int rows = registry.executeUpdate(NAMESPACE, "updateUser", user);

        // 验证结果
        assertTrue(rows >= 0);
    }

    @Test
    void testDeleteById() {
        // 创建删除参数
        User user = new User();
        user.setId(1L);

        // 执行删除
        int rows = registry.executeUpdate(NAMESPACE, "deleteById", user);

        // 验证结果
        assertTrue(rows >= 0);
    }

    @Test
    void testCountUsers() {
        // 创建查询参数
        UserQuery query = new UserQuery();
        query.setStatus(1);

        // 执行统计
        Object result = registry.executeQuery(NAMESPACE, "countUsers", query, Integer.class);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    void testDynamicSql() {
        // 测试动态 SQL - 只设置部分条件
        UserQuery query = new UserQuery();
        query.setUsername("test%");

        // 执行查询
        Object result = registry.executeQuery(NAMESPACE, "findUsers", query, User.class);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
    }
}
