package cn.tannn.jdevelops.utils.http;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpContextUtilsTest {
    HttpServletRequest request = mock(HttpServletRequest.class);

    @BeforeEach
    void setRequest() throws IOException {
        // 定义模拟参数
        Map<String, String> params = new HashMap<>();
        params.put("username", "testuser");
        params.put("id", "123");
        // 模拟 getParameterNames 方法，返回参数名称的枚举
        Enumeration<String> paramNames = Collections.enumeration(params.keySet());
        when(request.getParameterNames()).thenReturn(paramNames);
        // 模拟 getParameter 方法，根据参数名称返回参数值
        for (Map.Entry<String, String> entry : params.entrySet()) {
            when(request.getParameter(entry.getKey())).thenReturn(entry.getValue());
        }

        String requestBody = "example request body";
        when(request.getInputStream()).thenAnswer(inv -> new ServletInputStream() {
            private final ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());

            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // Do nothing
            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        });
    }

    @Test
    void getParameterMapAll() {
//        id=123
//        username=testuser
        Map<String, String> mapAll = HttpContextUtils.getParameterMapAll(request);
        assertNotNull(mapAll);
        mapAll.forEach((x,y) -> System.out.println(x+"="+y));
    }

    @Test
    void getBodyString() {
        String bodyString = HttpContextUtils.getBodyString(request);
        assertEquals("example request body", bodyString);
    }

    @Test
    void isMultipartContent() {
        assertFalse(HttpContextUtils.isMultipartContent(request));
    }

    @Test
    void getRequestParamValue() {
        assertEquals("testuser",HttpContextUtils.getRequestParamValue(request,"username"));
    }
}
