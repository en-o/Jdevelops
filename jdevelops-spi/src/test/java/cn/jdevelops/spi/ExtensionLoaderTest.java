package cn.jdevelops.spi;

import cn.jdevelops.spi.fixture.TestSPI;
import junit.framework.TestCase;

public class ExtensionLoaderTest extends TestCase {

    /**
     * 获取指定 实现
     */
    public void testExtensionTest(){
        // name 在  META-INF.jdevelops 中定义的
        assertEquals(ExtensionLoader.getExtensionLoader(TestSPI.class)
                .getJoin("testSPI1")
                .test(), "test1");
        assertEquals(ExtensionLoader.getExtensionLoader(TestSPI.class)
                .getJoin("testSPI2")
                .test(), "test2");
    }

    /**
     * 获取 testSPI3
     * 同一个文件中的相同的key最后一个覆盖掉
     */
    public void testExtensionTest1(){
        // name 在  META-INF.jdevelops 中定义的
        assertEquals(ExtensionLoader.getExtensionLoader(TestSPI.class)
                .getJoin("testSPI3")
                .test(), "test4");
    }


    /**
     * 获取所有 实现
     */
    public void testExtensionTest3(){
        // name 在  META-INF.jdevelops 中定义的
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoins().forEach(it -> System.out.println(it.test()));
    }




}
