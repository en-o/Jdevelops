package cn.jdevelops.map.core.bean;

import junit.framework.TestCase;


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


    public static class Apply {
        private String age,name;
        @ColumnUtil.TableField(value = "sex")
        private String mySex;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMySex() {
            return mySex;
        }

        public void setMySex(String mySex) {
            this.mySex = mySex;
        }
    }

    public static class Help {
        private String author,title,myTest;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMyTest() {
            return myTest;
        }

        public void setMyTest(String myTest) {
            this.myTest = myTest;
        }
    }


    public static class Apply2 extends Help {
        private String age,name;
        @ColumnUtil.TableField(value = "sex")
        private String mySex;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMySex() {
            return mySex;
        }

        public void setMySex(String mySex) {
            this.mySex = mySex;
        }
    }
}
