package cn.jdevelops.sboot.web.context.servlet;


import cn.jdevelops.sboot.web.entity.http.JdevelopsResponse;
import cn.jdevelops.sboot.web.exception.JHttpException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tnnn
 * @version V1.0
 * @date 2023-02-11 13:09
 */
public class JdevelopsResponseForServlet implements JdevelopsResponse {
    protected HttpServletResponse response;

    public JdevelopsResponseForServlet(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public Object getSource() {
        return this.response;
    }

    @Override
    public JdevelopsResponse setStatus(int status) {
        this.response.setStatus(status);
        return this;
    }

    @Override
    public JdevelopsResponse setHeader(String name, String value) {
        this.response.setHeader(name, value);
        return this;
    }

    @Override
    public JdevelopsResponse addHeader(String name, String value) {
        this.response.addHeader(name, value);
        return this;
    }

    @Override
    public Object redirect(String url) {
        try {
            this.response.sendRedirect(url);
            return null;
        } catch (IOException var3) {
            throw new JHttpException(var3);
        }
    }


}
