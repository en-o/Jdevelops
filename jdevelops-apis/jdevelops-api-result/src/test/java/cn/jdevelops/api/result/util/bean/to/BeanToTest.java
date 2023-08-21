package cn.jdevelops.api.result.util.bean.to;

import junit.framework.TestCase;

public class BeanToTest extends TestCase {

    public void testCopy() {
        CopyTestVO copyTestVO = new CopyTestVO(" 张三 ", 18);
        CopyTest copyTest = copyTestVO.to(CopyTest.class);
        assertEquals("CopyTest(name=张三, age=18)", copyTest.toString());

        CopyTestVO copyTestVO2 = new CopyTestVO("张三", 18);
        CopyTest copyTest2 = copyTestVO2.to(CopyTest.class);
        assertEquals("CopyTest(name=张三, age=18)", copyTest2.toString());

        CopyTestVO copyTestVO3 = new CopyTestVO(" 张 三 ", 18);
        CopyTest copyTest3 = copyTestVO3.to(CopyTest.class);
        assertEquals("CopyTest(name=张 三, age=18)", copyTest3.toString());
    }




}
