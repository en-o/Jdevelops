package cn.tannn.jdevelops.jdectemplate.xmlmapper.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OgnlUtil 测试类
 * 测试 OGNL 表达式工具的各种功能，包括枚举和 record 类的方法调用
 *
 * @author tnnn
 */
class OgnlUtilTest {

    /**
     * 测试用枚举 - 登录平台
     */
    enum LoginPlatform {
        NONE,
        WEB,
        MOBILE,
        DESKTOP
    }

    /**
     * 测试用 record - 用户信息
     */
    record UserInfo(Long id, String loginName, String name, LoginPlatform platform) {
    }

    /**
     * 测试用类 - 查询参数
     */
    static class QueryParam {
        private String loginName;
        private LoginPlatform loginPlatform;
        private UserInfo userInfo;

        public QueryParam(String loginName, LoginPlatform loginPlatform) {
            this.loginName = loginName;
            this.loginPlatform = loginPlatform;
        }

        public String getLoginName() {
            return loginName;
        }

        public LoginPlatform getLoginPlatform() {
            return loginPlatform;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }
    }

    /**
     * 测试枚举的 name() 方法调用
     */
    @Test
    void testEnumNameMethod() {
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);

        // 测试枚举的 name() 方法调用
        Object value = OgnlUtil.getValue("loginPlatform.name()", param);
        assertNotNull(value, "枚举的 name() 方法应该返回值");
        assertEquals("WEB", value, "枚举的 name() 方法应该返回 'WEB'");
    }

    /**
     * 测试枚举的 ordinal() 方法调用
     */
    @Test
    void testEnumOrdinalMethod() {
        QueryParam param = new QueryParam("testUser", LoginPlatform.MOBILE);

        // 测试枚举的 ordinal() 方法调用
        Object value = OgnlUtil.getValue("loginPlatform.ordinal()", param);
        assertNotNull(value, "枚举的 ordinal() 方法应该返回值");
        assertEquals(2, value, "MOBILE 的 ordinal 应该是 2");
    }

    /**
     * 测试 record 类的访问器方法调用
     */
    @Test
    void testRecordAccessorMethod() {
        UserInfo userInfo = new UserInfo(1001L, "john.doe", "John Doe", LoginPlatform.DESKTOP);
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);
        param.setUserInfo(userInfo);

        // 测试 record 的访问器方法调用
        Object id = OgnlUtil.getValue("userInfo.id()", param);
        assertEquals(1001L, id, "record 的 id() 方法应该返回 1001");

        Object loginName = OgnlUtil.getValue("userInfo.loginName()", param);
        assertEquals("john.doe", loginName, "record 的 loginName() 方法应该返回 'john.doe'");

        Object name = OgnlUtil.getValue("userInfo.name()", param);
        assertEquals("John Doe", name, "record 的 name() 方法应该返回 'John Doe'");
    }

    /**
     * 测试 record 嵌套访问枚举的 name() 方法
     */
    @Test
    void testRecordNestedEnumNameMethod() {
        UserInfo userInfo = new UserInfo(1001L, "john.doe", "John Doe", LoginPlatform.DESKTOP);
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);
        param.setUserInfo(userInfo);

        // 测试嵌套访问：record 的 platform() 方法返回枚举，再调用枚举的 name() 方法
        Object value = OgnlUtil.getValue("userInfo.platform().name()", param);
        assertNotNull(value, "嵌套方法调用应该返回值");
        assertEquals("DESKTOP", value, "应该返回 'DESKTOP'");
    }

    /**
     * 测试 List 参数中的枚举 name() 方法（模拟 MyBatis 的 arg0.property 形式）
     */
    @Test
    void testEnumNameMethodWithListParameter() {
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);
        List<Object> paramList = Arrays.asList(param, "otherParam");

        // 测试 arg0.loginPlatform.name() 形式
        Object value = OgnlUtil.getValue("arg0.loginPlatform.name()", paramList);
        assertNotNull(value, "arg0 形式的枚举 name() 调用应该返回值");
        assertEquals("WEB", value, "应该返回 'WEB'");
    }

    /**
     * 测试布尔表达式中的枚举 name() 方法
     */
    @Test
    void testEvaluateBooleanWithEnumName() {
        QueryParam param1 = new QueryParam("testUser", LoginPlatform.NONE);
        QueryParam param2 = new QueryParam("testUser", LoginPlatform.WEB);

        // 测试 != 'NONE'
        boolean result1 = OgnlUtil.evaluateBoolean("loginPlatform.name() != 'NONE'", param1);
        assertFalse(result1, "NONE 不应该不等于 'NONE'");

        boolean result2 = OgnlUtil.evaluateBoolean("loginPlatform.name() != 'NONE'", param2);
        assertTrue(result2, "WEB 应该不等于 'NONE'");

        // 测试 == 'WEB'
        boolean result3 = OgnlUtil.evaluateBoolean("loginPlatform.name() == 'WEB'", param2);
        assertTrue(result3, "WEB 应该等于 'WEB'");
    }

    /**
     * 测试 List 参数中的布尔表达式（模拟 XML mapper 的实际使用场景）
     */
    @Test
    void testEvaluateBooleanWithListParameterAndEnumName() {
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);
        List<Object> paramList = Arrays.asList(param, "otherParam");

        // 测试 arg0.loginPlatform.name() != 'NONE'
        boolean result = OgnlUtil.evaluateBoolean("arg0.loginPlatform.name() != 'NONE'", paramList);
        assertTrue(result, "arg0.loginPlatform.name() != 'NONE' 应该为 true");
    }

    /**
     * 测试简单属性访问（非方法调用）
     */
    @Test
    void testSimplePropertyAccess() {
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);

        Object value = OgnlUtil.getValue("loginName", param);
        assertEquals("testUser", value, "简单属性访问应该正常工作");
    }

    /**
     * 测试 Map 参数访问
     */
    @Test
    void testMapPropertyAccess() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginPlatform", LoginPlatform.MOBILE);

        Object value = OgnlUtil.getValue("loginPlatform.name()", map);
        assertEquals("MOBILE", value, "Map 中的枚举 name() 调用应该正常工作");
    }

    /**
     * 测试 null 值处理
     */
    @Test
    void testNullValueHandling() {
        QueryParam param = new QueryParam("testUser", null);

        Object value = OgnlUtil.getValue("loginPlatform.name()", param);
        assertNull(value, "null 对象的方法调用应该返回 null");
    }

    /**
     * 测试不存在的方法
     */
    @Test
    void testNonExistentMethod() {
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);

        Object value = OgnlUtil.getValue("loginPlatform.nonExistentMethod()", param);
        assertNull(value, "不存在的方法应该返回 null");
    }

    /**
     * 测试 toString() 方法调用
     */
    @Test
    void testToStringMethod() {
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);

        Object value = OgnlUtil.getValue("loginPlatform.toString()", param);
        assertEquals("WEB", value, "toString() 方法应该返回枚举名称");
    }

    /**
     * 测试复杂的链式方法调用
     */
    @Test
    void testComplexChainedMethodCalls() {
        UserInfo userInfo = new UserInfo(1001L, "john.doe", "John Doe", LoginPlatform.DESKTOP);
        QueryParam param = new QueryParam("testUser", LoginPlatform.WEB);
        param.setUserInfo(userInfo);

        // 测试链式调用：userInfo.loginName().toUpperCase()
        Object value = OgnlUtil.getValue("userInfo.loginName().toUpperCase()", param);
        assertEquals("JOHN.DOE", value, "链式方法调用应该正常工作");
    }
}
