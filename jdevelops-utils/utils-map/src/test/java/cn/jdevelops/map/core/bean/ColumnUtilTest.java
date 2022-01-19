package cn.jdevelops.map.core.bean;

import junit.framework.TestCase;
import lombok.Getter;
import lombok.Setter;

public class ColumnUtilTest extends TestCase {

    public void testOf() {
        assertEquals(ColumnUtil.getFieldName(Apply::getAge),"age");
        assertEquals(ColumnUtil.getFieldName(Apply::getName),"name");
        assertEquals(ColumnUtil.getFieldName(Apply::getMySex),"sex");
        assertEquals(ColumnUtil.getFieldName(Apply::getMySex,true),"sex");
        assertEquals(ColumnUtil.getFieldName(Help::getAuthor),"author");
        assertEquals(ColumnUtil.getFieldName(Help::getTitle),"title");
        assertEquals(ColumnUtil.getFieldName(Help::getMyTest,true),"my_test");
    }

    @Getter
    @Setter
    public static class Apply {
        private String age,name;
        @ColumnUtil.TableField(value = "sex")
        private String mySex;
    }
    @Getter
    @Setter
    public static class Help {
        private String author,title,myTest;
    }
}