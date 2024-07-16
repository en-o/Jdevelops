package cn.tannn.jdevelops.utils.http;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CookieUtilTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    Cookie[] cookies ;
    @BeforeEach
    void setRequest() throws IOException {
        // 创建模拟的 Cookie 对象
        Cookie cookie1 = new Cookie("token", "abc123");
        Cookie cookie2 = new Cookie("username", "johndoe");
        cookies = new Cookie[]{cookie1, cookie2};
        when(request.getCookies()).thenReturn(cookies);
    }


    @Test
    void cookies() {
        Cookie[] getCookie = CookieUtil.cookies(request);
        for (Cookie cookie : getCookie) {
            // abc123
            // johndoe
            System.out.println(cookie.getValue());
            // token
            // username
            System.out.println(cookie.getName());
        }
        assertNotNull(getCookie);
    }

    @Test
    void findCookie() {
        assertEquals("username" , CookieUtil.findCookie("username", request).getName());

        assertNull(CookieUtil.findCookie("username1", request));

        assertEquals("username" , CookieUtil.findCookie("username", cookies).getName());

        assertNull(CookieUtil.findCookie("username1", cookies));
    }


}
