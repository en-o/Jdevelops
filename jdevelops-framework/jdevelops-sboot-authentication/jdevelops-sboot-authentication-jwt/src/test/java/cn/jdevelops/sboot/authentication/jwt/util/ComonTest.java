package cn.jdevelops.sboot.authentication.jwt.util;

import com.alibaba.fastjson2.JSON;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/12 13:50
 */
public class ComonTest extends TestCase {

    public void test1111() {
        // map int
        String x = "[\"tan\",\"tan\"]";
        Arrays.stream(JSON.to(String[].class,x)).forEach(System.out::println);
    }

    public void test2222() {
        String[] x2 = new String[]{"ad", "bc"};
        assertEquals(0,Arrays.binarySearch(x2, "ad"));

        x2 = new String[]{"ad", "bc","ad"};
        assertEquals(0,Arrays.binarySearch(x2, "ad"));

        x2 = new String[]{"ad", "bc","ad"};
        assertEquals(-1,Arrays.binarySearch(x2, "111"));
    }
}
