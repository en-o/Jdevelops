package cn.tannn.jdevelops.jdectemplate.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcUtilsTest {

    @Test
    void tableName() {
        assertEquals(JdbcUtils.tableName("SELECT * FROM users WHERE age > 18"),"USERS");
        assertEquals(JdbcUtils.tableName("SELECT * FROM   users WHERE age > 18"),"USERS");
        assertEquals(JdbcUtils.tableName("SELECT * FROM   users     WHERE age > 18"),"USERS");
        assertEquals(JdbcUtils.tableName("SELECT * FROM   users"),"USERS");
        assertEquals(JdbcUtils.tableName("FROM   users"),"USERS");
        assertEquals(JdbcUtils.tableName("select * from sys_user"),"SYS_USER");
    }
}
