package cn.tannn.jdevelops.result.bean;

import cn.tannn.jdevelops.result.domin.column.Apply;
import cn.tannn.jdevelops.result.domin.column.Apply2;
import cn.tannn.jdevelops.result.domin.column.BoilerEntity;
import cn.tannn.jdevelops.result.domin.column.Help;
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


    /**
     * 测试获取父类的属性
     */
    @Test
    void superclassField(){
        assertEquals(ColumnUtil.getFieldName(Apply2::getAuthor),"author");
    }

    /**
     * 测试获取特殊父类的属性
     *  ps: JpaAuditFields
     */
    @Test
    void superclassFieldMulti(){
        assertEquals(ColumnUtil.getFieldName(BoilerEntity::getCreateTime),"createTime");
    }
}
