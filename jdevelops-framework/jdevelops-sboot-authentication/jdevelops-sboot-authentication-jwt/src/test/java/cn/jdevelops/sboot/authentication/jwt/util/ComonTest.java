package cn.jdevelops.sboot.authentication.jwt.util;

import cn.jdevelops.util.jwt.constant.PlatformConstant;
import cn.jdevelops.util.jwt.core.JwtService;
import cn.jdevelops.util.jwt.entity.SignEntity;
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
        Arrays.stream(JSON.to(String[].class, x)).forEach(System.out::println);
    }

    public void test2222() {
        String[] x2 = new String[]{"ad", "bc"};
        assertEquals(0, Arrays.binarySearch(x2, "ad"));

        x2 = new String[]{"ad", "bc", "ad"};
        assertEquals(0, Arrays.binarySearch(x2, "ad"));

        x2 = new String[]{"ad", "bc", "ad"};
        assertEquals(-1, Arrays.binarySearch(x2, "111"));
    }

    public void testEnumEq() {
        List<PlatformConstant> platformConstants = Arrays.asList(COMMON, WEB_H5);

        assertTrue(platformConstants.contains(COMMON));
        assertFalse(platformConstants.contains(COMMON.name()));
        assertFalse(platformConstants.contains(WEB_ADMIN));

        PlatformConstant[] platform = new PlatformConstant[2];
        platform[0] = COMMON;
        platform[1] = WEB_ADMIN;
        assertTrue(platformConstants.contains(platform[0]));
        assertFalse(platformConstants.contains(platform[1]));
        for (PlatformConstant annotationPlatform : platform) {
            if (annotationPlatform.contains(platformConstants)) {
                System.out.println("annotationPlatform :" + annotationPlatform.name());
            }
        }

        try {
            String token = JwtService.generateToken(new SignEntity<>("tan", WEB_H5));
            assertEquals(JwtService.getSubject(token), "tan");
            JwtService.getPlatformConstantExpires(token).forEach(plat -> {
                System.out.println("jwt annotationPlatform:" + plat.name());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
