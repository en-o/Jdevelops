package cn.jdevelops.file.oss.api.util;

import org.junit.Test;

import static org.junit.Assert.*;


public class AboutFileUtilTest {

    @Test
    public void getFileSuffix() {
        assertEquals(AboutFileUtil.getFileSuffix("XX.jpg"),"jpg");
    }

    @Test
    public void isSafe() {
        assertFalse(AboutFileUtil.isSafe("json"));
        assertTrue(AboutFileUtil.isSafe("png"));
        assertTrue(AboutFileUtil.isSafe("doc"));
        assertTrue(AboutFileUtil.isSafe("xlsx"));
        assertTrue(AboutFileUtil.isSafe("pdf"));
    }

    @Test
    public void isPic() {
        assertFalse(AboutFileUtil.isPic("json"));
        assertTrue(AboutFileUtil.isPic("png"));
        assertFalse(AboutFileUtil.isPic("doc"));
        assertFalse(AboutFileUtil.isPic("xlsx"));
        assertFalse(AboutFileUtil.isPic("pdf"));
    }


//    @Test
//    public void getFileSizeUnit() throws URISyntaxException {
//        // 3.93KB
//        System.out.println(AboutFileUtil
//                .getFileSizeUnit(
//                        new File("D:\\project\\java\\my\\Jdevelops\\jdevelops-file\\jdevelops-file-oss\\jdevelops-file-oss-api\\src\\main\\java\\cn\\jdevelops\\file\\oss\\api\\util\\ImageWatermarkUtil.java")));
//    }
}
