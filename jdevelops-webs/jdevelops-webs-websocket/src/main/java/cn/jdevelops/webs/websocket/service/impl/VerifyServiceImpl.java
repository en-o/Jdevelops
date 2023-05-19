package cn.jdevelops.webs.websocket.service.impl;

import cn.jdevelops.webs.websocket.CommonConstant;
import cn.jdevelops.webs.websocket.service.VerifyService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * websocket连接认证
 * @author tan
 */
@ConditionalOnMissingBean(VerifyService.class)
@AutoConfiguration
public class VerifyServiceImpl implements VerifyService{

    /**
     * 默认的验证密钥
     */
    static String DEF_TOKEN = "123456";

    @Override
    public boolean verifyLogin(HttpServletRequest request) {

        String servletPath = request.getServletPath();
        if(servletPath.contains(CommonConstant.VERIFY_PATH_NO)){
            //  不用登录直接连接
            return true;
        }
        String token = request.getParameter("token");
        if(Objects.isNull(token)){
            token = request.getHeader("token");
        }
        return DEF_TOKEN.equals(token);
    }


}
