package cn.tannn.jdevelops.jdectemplate.sql;

import org.junit.jupiter.api.Test;

import static cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.ParameterMode.NAMED;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DynamicSqlBuilder排序功能测试
 */
public class DynamicSqlBuilderOrderTest {

    @Test
    public void testBasicOrderBy() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users");
        builder.orderBy("id DESC");

        assertEquals("SELECT * FROM users ORDER BY id DESC", builder.getSql());
    }

    @Test
    public void testMultipleOrderBy() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users");
        builder.orderBy("id DESC")
              .orderBy("name ASC");

        assertEquals("SELECT * FROM users ORDER BY id DESC, name ASC", builder.getSql());
    }

    @Test
    public void testOrderByWithLimit() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users");
        builder.pageZero(0, 10)
              .orderBy("id DESC");

        assertEquals("SELECT * FROM users ORDER BY id DESC LIMIT ?, ?", builder.getSql());
    }

    @Test
    public void testOrderByAfterLimit() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users",NAMED);
        builder.pageZero(0, 10)
              .orderBy("id DESC")
              .orderBy("name ASC");

        assertEquals("SELECT * FROM users ORDER BY id DESC, name ASC LIMIT :offset, :limit", builder.getSql());
    }

    @Test
    public void testOrderByWithTableAlias() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users u");
        builder.orderBy("u.id DESC")
              .orderBy("u.name ASC");

        assertEquals("SELECT * FROM users u ORDER BY u.id DESC, u.name ASC", builder.getSql());
    }

    @Test
    public void testEmptyOrderBy() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users");
        builder.orderBy("");
        assertEquals("SELECT * FROM users", builder.getSql());
    }

    @Test
    public void testOrderByWithWhereClause() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users",NAMED);
        builder.eq("status", 1)
              .orderBy("id DESC");

        assertEquals("SELECT * FROM users WHERE status = :status1 ORDER BY id DESC", builder.getSql());
        assertEquals("SELECT * FROM users WHERE status = 1 ORDER BY id DESC", builder.getNativeSql());
    }

    @Test
    public void testComplexOrderBy() {
        DynamicSqlBuilder builder = new DynamicSqlBuilder("SELECT * FROM users u",NAMED);
        builder.eq("u.status", 1)
              .orderBy("u.id DESC")
              .pageZero(0, 10)
              .orderBy("u.name ASC");

        assertEquals(
            "SELECT * FROM users u WHERE u.status = :ustatus1 ORDER BY u.id DESC, u.name ASC LIMIT :offset, :limit",
            builder.getSql()
        );
        assertEquals(
                "SELECT * FROM users u WHERE u.status = 1 ORDER BY u.id DESC, u.name ASC LIMIT 0, 10",
                builder.getNativeSql()
        );
    }
}
