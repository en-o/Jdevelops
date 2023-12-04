package cn.jdevelops.sboot.authentication.jwt.util;

import cn.jdevelops.util.jwt.constant.PlatformConstant;
import com.alibaba.fastjson2.JSON;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

import static cn.jdevelops.util.jwt.constant.PlatformConstant.*;

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

    public void testEnumEq() {
        List<PlatformConstant> platformConstants = Arrays.asList(COMMON,WEB_H5);
        if (platformConstants.contains(COMMON)) {
            System.out.println("1 存在：COMMON");
        }
        if (platformConstants.contains(COMMON.name())) {
            System.out.println("2 存在：COMMON");
        }else {
            System.out.println("2 COMMON 不存在");
        }
        if (platformConstants.contains(WEB_ADMIN)) {
            System.out.println("存在：WEB_ADMIN");
        }else {
            System.out.println("WEB_ADMIN 不存在");
        }
    }
}
