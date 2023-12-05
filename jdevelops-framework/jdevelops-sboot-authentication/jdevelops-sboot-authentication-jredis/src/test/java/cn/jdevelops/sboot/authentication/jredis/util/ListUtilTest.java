package cn.jdevelops.sboot.authentication.jredis.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;

public class ListUtilTest extends TestCase {

    public void testVerifyList() {
        String[] parameterRole = new String[10];
        parameterRole[0] = "admin";
        parameterRole[1] = "tan";
        assertTrue(ListUtil.verifyList(Arrays.asList("admin","tan","ning","power"),parameterRole));
        assertTrue(ListUtil.verifyList(Collections.singletonList("admin"),parameterRole));
        assertFalse(ListUtil.verifyList(Arrays.asList("ning","power"),parameterRole));
        assertFalse(ListUtil.verifyList(Collections.emptyList(),parameterRole));
        assertFalse(ListUtil.verifyList(Collections.emptyList(),new String[10]));
        assertFalse(ListUtil.verifyList(null,new String[10]));
        assertFalse(ListUtil.verifyList(Collections.emptyList(),null));
        assertFalse(ListUtil.verifyList(null,null));

    }
}
