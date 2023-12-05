package cn.jdevelops.sboot.authentication.jredis.util;

import junit.framework.TestCase;

import java.util.Arrays;

public class ListUtilTest extends TestCase {

    public void testVerifyList() {
        String[] parameterRole = new String[10];
        parameterRole[0] = "admin";
        parameterRole[1] = "tan";
        assertTrue(ListUtil.verifyList(Arrays.asList("admin","tan","ning","power"),parameterRole));
    }
}
