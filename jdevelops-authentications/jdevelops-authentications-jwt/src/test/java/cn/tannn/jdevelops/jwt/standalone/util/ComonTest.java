package cn.tannn.jdevelops.jwt.standalone.util;


import cn.tannn.jdevelops.annotations.web.constant.PlatformConstant;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static cn.tannn.jdevelops.annotations.web.constant.PlatformConstant.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/12 13:50
 */
public class ComonTest {

    @Test
    public void test1111() {
        // map int
        String x = "[\"tan\",\"tan\"]";
        Arrays.stream(JSON.to(String[].class, x)).forEach(System.out::println);
    }
    @Test
    public void test2222() {
        String[] x2 = new String[]{"ad", "bc"};
        assertEquals(0, Arrays.binarySearch(x2, "ad"));

        x2 = new String[]{"ad", "bc", "ad"};
        assertEquals(0, Arrays.binarySearch(x2, "ad"));

        x2 = new String[]{"ad", "bc", "ad"};
        assertEquals(-1, Arrays.binarySearch(x2, "111"));
    }

    @Test
    public void testEnumEq() {
        List<String> platformConstants = Arrays.asList(COMMON, WEB_H5);

        assertTrue(platformConstants.contains(COMMON));
        assertFalse(platformConstants.contains(WEB_ADMIN));

        String[] platform = new String[2];
        platform[0] = COMMON;
        platform[1] = WEB_ADMIN;
        assertTrue(platformConstants.contains(platform[0]));
        assertFalse(platformConstants.contains(platform[1]));
        for (String annotationPlatform : platform) {
            if (platformConstants.contains(annotationPlatform)) {
                System.out.println("annotationPlatform :" + annotationPlatform);
            }
        }

        try {
            String token = JwtService.generateToken(SignEntity.initPlatform2("tan", WEB_H5));
            assertEquals(JwtService.getSubject(token), "tan");
            JwtService.getPlatformConstantExpires(token).forEach(plat -> {
                System.out.println("jwt annotationPlatform:" + plat);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
