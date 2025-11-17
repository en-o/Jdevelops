package cn.tannn.jdevelops.jdectemplate.sql;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 复杂SQL的buildCountSql方法单元测试
 */
public class ComplexBuildCountSqlTest {

    @Test
    public void testSimpleQuery() {
        // 测试简单查询 - 解决问题一：多余空格
        String baseSql = "SELECT * FROM users";
        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.eq("status", 25);
        builder.eq("age", 25);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        assertEquals("SELECT COUNT(*) FROM users WHERE status = ? AND age = ?", countSql);
        assertEquals(2, countBuilder.getPositionalParams().length);
    }

    @Test
    public void testComplexQueryWithSubquery() {
        // 测试复杂查询 - 解决问题二：子查询中的FROM干扰
        String baseSql = "SELECT a.*,b.masked_phone as phone, " +
                        "(SELECT GROUP_CONCAT(c.login_platform) FROM tb_account_login_platform c WHERE c.user_id = a.id) as login_platforms " +
                        "FROM tb_account a left join tb_account_sensitive b on b.user_id=a.id";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.custom("FIND_IN_SET('ADMIN', (SELECT GROUP_CONCAT(c.login_platform) FROM tb_account_login_platform c WHERE c.user_id = a.id)) > 0")
               .custom("(user_expire_time IS NULL OR a.user_expire_time >= '2025-07-03 13:42:07')")
               .page(1, 20);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM tb_account a left join tb_account_sensitive b on b.user_id=a.id " +
                                "WHERE FIND_IN_SET('ADMIN', (SELECT GROUP_CONCAT(c.login_platform) FROM tb_account_login_platform c WHERE c.user_id = a.id)) > 0 " +
                                "AND (user_expire_time IS NULL OR a.user_expire_time >= '2025-07-03 13:42:07')";

        assertEquals(expectedCountSql, countSql);
        // 验证LIMIT参数被排除
        assertEquals(0, countBuilder.getPositionalParams().length);
    }

    @Test
    public void testNestedSubqueries() {
        // 测试嵌套子查询
        String baseSql = "SELECT u.*, " +
                        "(SELECT COUNT(*) FROM orders o WHERE o.user_id = u.id AND o.status IN " +
                        "(SELECT s.id FROM status s WHERE s.name = 'COMPLETED')) as order_count " +
                        "FROM users u";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.eq("u.active", true)
               .op("u.created_date", ">=", "2024-01-01")
               .orderBy("u.id DESC")
               .limit(10, 50);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM users u WHERE u.active = ? AND u.created_date >= ?";

        assertEquals(expectedCountSql, countSql);
        assertEquals(2, countBuilder.getPositionalParams().length);
    }

    @Test
    public void testMultipleJoinsWithSubqueries() {
        // 测试多表连接和子查询
        String baseSql = "SELECT u.*, p.name as profile_name, " +
                        "(SELECT AVG(r.rating) FROM reviews r WHERE r.user_id = u.id) as avg_rating " +
                        "FROM users u " +
                        "LEFT JOIN profiles p ON p.user_id = u.id " +
                        "INNER JOIN (SELECT user_id FROM subscriptions WHERE status = 'ACTIVE') s ON s.user_id = u.id";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.like("u.email", "test")
               .in("u.role", java.util.Arrays.asList("ADMIN", "USER"))
               .isNotNull("p.name")
               .orderBy("u.created_date DESC, u.id ASC")
               .pageZero(2, 25);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM users u " +
                                "LEFT JOIN profiles p ON p.user_id = u.id " +
                                "INNER JOIN (SELECT user_id FROM subscriptions WHERE status = 'ACTIVE') s ON s.user_id = u.id " +
                                "WHERE u.email LIKE ? AND u.role IN (?, ?) AND p.name IS NOT NULL";

        assertEquals(expectedCountSql, countSql);
        assertEquals(3, countBuilder.getPositionalParams().length);
    }

    @Test
    public void testNamedParametersWithComplexQuery() {
        // 测试命名参数模式的复杂查询
        String baseSql = "SELECT d.*, " +
                        "(SELECT COUNT(*) FROM comments c WHERE c.document_id = d.id) as comment_count, " +
                        "(SELECT u.name FROM users u WHERE u.id = d.author_id) as author_name " +
                        "FROM documents d";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql, ParameterMode.NAMED);
        builder.eq("d.status", "status", "PUBLISHED")
               .between("d.created_date", "startDate", "2024-01-01", "endDate", "2024-12-31")
               .custom("d.category_id IN (SELECT id FROM categories WHERE parent_id = :parentCategoryId)")
               .addNamedParam("parentCategoryId", 5)
               .orderBy("d.created_date DESC")
               .limit(0, 30);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM documents d " +
                                "WHERE d.status = :status AND d.created_date BETWEEN :startDate AND :endDate " +
                                "AND d.category_id IN (SELECT id FROM categories WHERE parent_id = :parentCategoryId)";

        assertEquals(expectedCountSql, countSql);

        MapSqlParameterSource params = countBuilder.getNamedParams();
        assertEquals("PUBLISHED", params.getValue("status"));
        assertEquals("2024-01-01", params.getValue("startDate"));
        assertEquals("2024-12-31", params.getValue("endDate"));
        assertEquals(5, params.getValue("parentCategoryId"));

        // 验证LIMIT参数被排除
        assertFalse(params.hasValue("limit"));
        assertFalse(params.hasValue("offset"));
    }

    @Test
    public void testUnionQuery() {
        // 测试UNION查询
        String baseSql = "SELECT id, name, 'USER' as type FROM users WHERE active = 1 " +
                        "UNION ALL " +
                        "SELECT id, name, 'ADMIN' as type FROM admins WHERE status = 'ACTIVE'";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.orderBy("name ASC")
               .limit(0, 100);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM users WHERE active = 1 " +
                                "UNION ALL " +
                                "SELECT id, name, 'ADMIN' as type FROM admins WHERE status = 'ACTIVE'";

        assertEquals(expectedCountSql, countSql);
        assertEquals(0, countBuilder.getPositionalParams().length);
    }

//    @Test
//    public void testQueryWithCTE() {
//        // 测试CTE（公共表表达式）
//        String baseSql = "WITH recent_orders AS (" +
//                        "SELECT user_id, COUNT(*) as order_count " +
//                        "FROM orders " +
//                        "WHERE created_date >= '2024-01-01' " +
//                        "GROUP BY user_id" +
//                        ") " +
//                        "SELECT u.*, ro.order_count " +
//                        "FROM users u " +
//                        "LEFT JOIN recent_orders ro ON ro.user_id = u.id";
//
//        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
//        builder.op("u.registration_date", ">=", "2023-01-01")
//               .isNotNull("ro.order_count")
//               .orderBy("ro.order_count DESC")
//               .page(1, 20);
//
//        DynamicSqlBuilder countBuilder = builder.buildCountSql();
//        String countSql = countBuilder.getSql();
//
//        String expectedCountSql = "SELECT COUNT(*) FROM recent_orders AS (" +
//                                "SELECT user_id, COUNT(*) as order_count " +
//                                "FROM orders " +
//                                "WHERE created_date >= '2024-01-01' " +
//                                "GROUP BY user_id" +
//                                ") " +
//                                "SELECT u.*, ro.order_count " +
//                                "FROM users u " +
//                                "LEFT JOIN recent_orders ro ON ro.user_id = u.id " +
//                                "WHERE u.registration_date >= ? AND ro.order_count IS NOT NULL";
//
//        assertEquals(expectedCountSql, countSql);
//        assertEquals(1, countBuilder.getPositionalParams().length);
//    }

    @Test
    public void testComplexExistsQuery() {
        // 测试复杂的EXISTS子查询
        String baseSql = "SELECT u.*, " +
                        "(SELECT COUNT(*) FROM posts p WHERE p.author_id = u.id) as post_count " +
                        "FROM users u";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.custom("EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'AUTHOR')")
               .custom("NOT EXISTS (SELECT 1 FROM user_bans ub WHERE ub.user_id = u.id AND ub.active = 1)")
               .op("u.created_date", ">=", "2024-01-01")
               .orderBy("u.last_login DESC")
               .limit(50, 100);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM users u " +
                                "WHERE EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'AUTHOR') " +
                                "AND NOT EXISTS (SELECT 1 FROM user_bans ub WHERE ub.user_id = u.id AND ub.active = 1) " +
                                "AND u.created_date >= ?";

        assertEquals(expectedCountSql, countSql);
        assertEquals(1, countBuilder.getPositionalParams().length);
    }

    @Test
    public void testQueryWithWindowFunction() {
        // 测试窗口函数查询
        String baseSql = "SELECT u.*, " +
                        "ROW_NUMBER() OVER (PARTITION BY u.department_id ORDER BY u.salary DESC) as rank_in_dept, " +
                        "(SELECT AVG(salary) FROM users WHERE department_id = u.department_id) as avg_dept_salary " +
                        "FROM users u";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.op("u.salary", ">=", 50000)
               .isNotNull("u.department_id")
               .orderBy("u.department_id, u.salary DESC")
               .pageZero(0, 50);

//        System.out.println(builder.getSql());
        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM users u WHERE u.salary >= ? AND u.department_id IS NOT NULL";

        assertEquals(expectedCountSql, countSql);
        assertEquals(1, countBuilder.getPositionalParams().length);
    }

    @Test
    public void testEdgeCaseEmptySubquery() {
        // 测试边界情况：空的子查询括号
        String baseSql = "SELECT * FROM users";

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.eq("status", "ACTIVE")
                .in("id",null)
                .or(r -> r.like("name","test"))
               .limit(0, 10);

        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        String expectedCountSql = "SELECT COUNT(*) FROM users WHERE status = ? AND (name LIKE ?)";

        assertEquals(expectedCountSql, countSql);
        assertEquals(2, countBuilder.getPositionalParams().length);
    }

    @Test
    public void testInvalidSqlThrowsException() {
        // 测试无效SQL抛出异常
        String baseSql = "SELECT * WHERE name = 'test'"; // 缺少FROM

        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql);
        builder.eq("id", 1);

        assertThrows(IllegalStateException.class, builder::buildCountSql);
    }

    @Test
    public void testBuildCountSqlWithComplexQuery() {
        // 构建复杂的查询SQL（包含GROUP BY、ORDER BY、LIMIT）
        String originalSql = """
            SELECT
              '加工人员' AS user_classify,
              ANY_VALUE (b.NAME) AS username,
              user_id,
              role_code AS user_groups,
              SUM(aa.task_count) AS task_count,
              SUM(aa.finish_task_count) AS finish_task_count,
              SUM(aa.finish_page_count) AS finish_pages,
              SUM(aa.inspect_pass_page_count) AS quality,
              ROUND(AVG(aa.pass_rate) * 100, 2) AS finish_pass_ratex
             FROM 
              user_task_statistic aa
              LEFT JOIN user_manage b ON aa.user_id COLLATE utf8mb4_general_ci = b.id COLLATE utf8mb4_general_ci
            """;

        DynamicSqlBuilder builder = new DynamicSqlBuilder(originalSql, ParameterMode.NAMED);

        // 添加WHERE条件
        builder.in("aa.role_code", java.util.Arrays.asList("pic_processing", "enter_processing"))
                .addGroupBy("user_id, role_code")
                .orderBy("aa.user_id DESC")
                .limit(0, 20);

        // 验证原始SQL包含所有子句
        String fullSql = builder.getSql();
        assertTrue(fullSql.contains("WHERE"));
        assertTrue(fullSql.contains("GROUP BY"));
        assertTrue(fullSql.contains("ORDER BY"));
        assertTrue(fullSql.contains("LIMIT"));

        // 构建COUNT查询
        DynamicSqlBuilder countBuilder = builder.buildCountSql();
        String countSql = countBuilder.getSql();

        // 验证COUNT SQL的正确性
        System.out.println("Original SQL:");
        System.out.println(fullSql);
        System.out.println("\nCount SQL:");
        System.out.println(countSql);

        // 断言COUNT SQL的结构
        assertTrue(countSql.startsWith("SELECT COUNT(*)"));
        assertTrue(countSql.contains("user_task_statistic aa"));
        assertTrue(countSql.contains("LEFT JOIN user_manage b"));
        assertTrue(countSql.contains("WHERE aa.role_code IN"));

        // 确保移除了GROUP BY、ORDER BY和LIMIT
        assertFalse(countSql.contains("GROUP BY"));
        assertFalse(countSql.contains("ORDER BY"));
        assertFalse(countSql.contains("LIMIT"));

        // 验证参数正确复制（排除了LIMIT相关参数）
        MapSqlParameterSource originalParams = builder.getNamedParams();
        MapSqlParameterSource countParams = countBuilder.getNamedParams();

        // 不应该包含LIMIT相关参数
        assertFalse(countParams.hasValue("limit"));
        assertFalse(countParams.hasValue("offset"));

        System.out.println("\nOriginal parameters: " + java.util.Arrays.toString(originalParams.getParameterNames()));
        System.out.println("Count parameters: " + java.util.Arrays.toString(countParams.getParameterNames()));
    }
}
