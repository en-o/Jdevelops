package cn.jdevelops.spi;

import cn.jdevelops.spi.fixture.TestSPI;
import junit.framework.TestCase;

public class ExtensionLoaderTest extends TestCase {


    /**
     * 获取指定 实现
     */
    public void testExtensionTest(){
        // name 在  META-INF.jdevelops 中定义的
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI1").test();
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI2").test();
    }

    /**
     * 获取 3 看看是不是被4替换掉了
     */
    public void testExtensionTest1(){
        // name 在  META-INF.jdevelops 中定义的
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI3").test();
    }

    /**
     * 获取所有 实现
     */
    public void testExtensionTest2(){
        // name 在  META-INF.jdevelops 中定义的
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoins().forEach(TestSPI::test);
    }




}
