package cn.jdevelops.sboot.web.context.servlet;


import cn.jdevelops.sboot.web.entity.http.JdevelopsRequest;
import cn.jdevelops.sboot.web.exception.JHttpException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tnnn
 * @version V1.0
 * @date 2023-02-11 13:09
 */
public class JdevelopsRequestForServlet  implements JdevelopsRequest {

    protected HttpServletRequest request;

    public JdevelopsRequestForServlet(HttpServletRequest request) {
        this.request = request;
    }


    @Override
    public Object getSource() {
        return this.request;
    }

    @Override
    public String getParam(String name) {
        return this.request.getParameter(name);
    }

    @Override
    public String getHeader(String name) {
        return this.request.getHeader(name);
    }

    @Override
    public String getCookieValue(String name) {
        Cookie[] cookies = this.request.getCookies();
        if (cookies != null) {
            int var4 = cookies.length;
            for(int var5 = 0; var5 < var4; ++var5) {
                Cookie cookie = cookies[var5];
                if (cookie != null && name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    @Override
    public String getRequestPath() {
        return this.request.getServletPath();
    }

    @Override
    public String getUrl() {
        return this.request.getRequestURL().toString();
    }

    @Override
    public String getMethod() {
        return this.request.getMethod();
    }

    @Override
    public Object forward(String path, HttpServletResponse response) {
        try {
            this.request.getRequestDispatcher(path).forward(this.request, response);
            return null;
        } catch (IOException | ServletException var3) {
            throw new JHttpException(var3);
        }
    }
}
