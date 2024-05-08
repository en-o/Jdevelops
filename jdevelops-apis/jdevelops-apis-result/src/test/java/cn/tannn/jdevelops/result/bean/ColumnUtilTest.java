package cn.tannn.jdevelops.result.bean;

import cn.tannn.jdevelops.result.domin.Apply;
import cn.tannn.jdevelops.result.domin.Help;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColumnUtilTest {

    @Test
    void getFieldName() {
        assertEquals(ColumnUtil.getFieldName(Apply::getAge),"age");
        assertEquals(ColumnUtil.getFieldName(Apply::getName),"name");
        // 测试注解改字段名
        assertEquals(ColumnUtil.getFieldName(Apply::getMySex),"sex");
        assertEquals(ColumnUtil.getFieldName(Help::getAuthor),"author");
        assertEquals(ColumnUtil.getFieldName(Help::getTitle),"title");

    }

    @Test
    void testGetFieldName() {
        // 测试注解改字段名
        assertEquals(ColumnUtil.getFieldName(Apply::getMySex,true),"sex");
        assertEquals(ColumnUtil.getFieldName(Help::getMyTest,true),"my_test");
        assertEquals(ColumnUtil.getFieldName(Help::getMyTest,false),"myTest");
    }
}
