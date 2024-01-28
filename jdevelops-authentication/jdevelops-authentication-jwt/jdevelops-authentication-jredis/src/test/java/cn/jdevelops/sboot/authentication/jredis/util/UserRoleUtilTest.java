package cn.jdevelops.sboot.authentication.jredis.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;

public class UserRoleUtilTest extends TestCase {

    public void testVerifyRoles() {
        String[] parameterRole = new String[10];
        parameterRole[0] = "admin";
        parameterRole[1] = "tan";
        assertTrue(UserRoleUtil.verifyRoles(Arrays.asList("admin","tan","ning","power"),parameterRole));
        assertTrue(UserRoleUtil.verifyRoles(Collections.singletonList("admin"),parameterRole));
        assertFalse(UserRoleUtil.verifyRoles(Arrays.asList("ning","power"),parameterRole));
        assertFalse(UserRoleUtil.verifyRoles(Collections.emptyList(),parameterRole));
        assertFalse(UserRoleUtil.verifyRoles(Collections.emptyList(),new String[10]));
        assertFalse(UserRoleUtil.verifyRoles(null,new String[10]));
        assertTrue(UserRoleUtil.verifyRoles(Collections.emptyList(),null));
        assertTrue(UserRoleUtil.verifyRoles(null,null));
    }

    public void testVerifyPermissions() {
        assertTrue(UserRoleUtil.verifyPermissions(
                Arrays.asList("/edit/**","/tan","/user/info","/role"),
                "/edit/passwor")) ;

        // 前缀必须一样 userPermissions除非写 👇 *edit/**
        assertFalse(UserRoleUtil.verifyPermissions(
                Arrays.asList("/edit/**","/tan","/user/info","/role"),
                "edit/passwor")) ;

        assertTrue(UserRoleUtil.verifyPermissions(
                Arrays.asList("*edit/**","/tan","/user/info","/role"),
                "edit/passwor")) ;


        assertTrue(UserRoleUtil.verifyPermissions(
                Arrays.asList("*edit/**","/tan","/user/info","/role"),
                "/user/info")) ;

        assertFalse(UserRoleUtil.verifyPermissions(
                Arrays.asList("*edit/**","/tan","/user/info","/role"),
                "/xxx")) ;
    }

}
