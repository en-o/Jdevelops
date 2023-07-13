package cn.jdevelops.util.time.core;

import junit.framework.TestCase;

import static cn.jdevelops.util.time.enums.SqlTimeFormat.MYSQL_FORMAT_DATETIME_YEAR;
import static cn.jdevelops.util.time.enums.TimeFormat.NORM_FORMAT_DATETIME_YEAR;

public class TimeFormatUtilTest extends TestCase {

    public void testGetJavaTimeFormat() throws IllegalAccessException {
        TimeFormatUtil.getJavaTimeFormat().forEach(System.out::println);
    }

    public void testGetSqlTimeFormat() throws IllegalAccessException {
        TimeFormatUtil.getSqlTimeFormat().forEach(System.out::println);
    }

    public void testGetMySqlFormat() throws IllegalAccessException {
        TimeFormatUtil.getMySqlFormat().forEach(System.out::println);
    }

    public void testMysql2Java() {
        assertEquals(TimeFormatUtil.mysql2Java(MYSQL_FORMAT_DATETIME_YEAR),
                NORM_FORMAT_DATETIME_YEAR);
    }
}
