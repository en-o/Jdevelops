package cn.tannn.jdevelops.webs.interceptor.fiflter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * 自定义 HttpServletRequestWrapper 来包装输入流
 * 解决: request.getInputStream()只能读取一次的问题
 * 目标: 流可重复读
 *
 * @author tan
 */
public class JdevelopsHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 请求体 body
     */
    private byte[] body;

    public JdevelopsHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // 将body数据存储起来
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 重新包装输入流
     *
     * @throws IOException IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream bodyStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bodyStream.read();
            }

            /**
             * 下面的方法一般情况下不会被使用，
             * 如果你引入了一些需要使用ServletInputStream的外部组件，可以重点关注一下。
             */
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }


}
