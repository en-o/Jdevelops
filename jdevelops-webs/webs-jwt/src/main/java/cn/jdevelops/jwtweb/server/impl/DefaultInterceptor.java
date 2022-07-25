package cn.jdevelops.jwtweb.server.impl;


import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
import cn.jdevelops.spi.JoinSPI;

/**
 * @author Tianms
 * @date 2020/4/19 11:25
 */
@JoinSPI(cover = true)
public class DefaultInterceptor implements CheckTokenInterceptor {

    @Override
    public boolean checkToken(String token) {
        try {
            return JwtUtil.verity(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
