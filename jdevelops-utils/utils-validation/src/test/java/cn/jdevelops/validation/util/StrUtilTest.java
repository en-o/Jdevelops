package cn.jdevelops.validation.util;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class StrUtilTest {

    @Test
    public void hasText() {
        assertTrue(StrUtil.hasText("2131"));
        assertFalse(StrUtil.hasText(""));
        assertFalse(StrUtil.hasText(null));
    }

    @Test
    public void verifyRegular() {
        // 手机号
         Pattern mobile = Pattern.compile("^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1589]))\\d{8}");
        assertTrue(StrUtil.verifyRegular(mobile,"13320385140"));
        assertFalse(StrUtil.verifyRegular(mobile,"1332038514"));
        assertFalse(StrUtil.verifyRegular(mobile,"131"));

        // 身份证
        Pattern idCard = Pattern.compile("^\\d{6}((((((19|20)\\d{2})(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(((19|20)\\d{2})(0[13578]|1[02])31)|((19|20)\\d{2})02(0[1-9]|1\\d|2[0-8])|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))0229))\\d{3})|((((\\d{2})(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|((\\d{2})(0[13578]|1[02])31)|((\\d{2})02(0[1-9]|1\\d|2[0-8]))|(([13579][26]|[2468][048]|0[048])0229))\\d{2}))(\\d|X|x)");
        assertTrue(StrUtil.verifyRegular(idCard,"51223019931115607x"));
        assertTrue(StrUtil.verifyRegular(idCard,"51223019931115607X"));
        assertTrue(StrUtil.verifyRegular(idCard,"512230199311156070"));
        assertFalse(StrUtil.verifyRegular(idCard,"5122301993111"));
        assertFalse(StrUtil.verifyRegular(idCard,"512230199311156070123"));
    }
}
