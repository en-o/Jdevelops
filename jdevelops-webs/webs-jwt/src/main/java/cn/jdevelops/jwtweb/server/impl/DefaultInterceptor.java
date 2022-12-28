package cn.jdevelops.jwtweb.server.impl;


import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
import cn.jdevelops.spi.JoinSPI;

/**
 * @author tn
 * @date 2022-12-28 09:48:22
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
