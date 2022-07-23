package cn.jdevelops.spi;

import cn.jdevelops.spi.fixture.TestSPI;
import junit.framework.TestCase;

public class ExtensionLoaderTest extends TestCase {


    public void testExtensionTest(){
        // name 在  META-INF.jdevelops 中定义的
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI2").test();
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI2").test();
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoins().forEach(TestSPI::test);
    }


}
