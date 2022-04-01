package cn.jdevelops.spi;

import cn.jdevelops.spi.fixture.TestSPI;
import junit.framework.TestCase;

public class ExtensionLoaderTest extends TestCase {


    public void testExtensionTest(){
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI1").test();
        ExtensionLoader.getExtensionLoader(TestSPI.class).getJoin("testSPI2").test();
    }


}