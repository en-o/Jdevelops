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



    /**
     * 测试获取父类的属性
     */
    public void testSuperclassField(){
        assertEquals(ColumnUtil.getFieldName(Apply2::getAuthor),"author");
    }

    /**
     * 测试获取特殊父类的属性
     *  ps: JpaAuditFields
     */
    public void testSuperclassFieldMulti(){
        assertEquals(ColumnUtil.getFieldName(BoilerEntity::getCreateTime),"createTime");
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

    @Getter
    @Setter
    public static class Apply2 extends Help {
        private String age,name;
        @ColumnUtil.TableField(value = "sex")
        private String mySex;
    }
}
