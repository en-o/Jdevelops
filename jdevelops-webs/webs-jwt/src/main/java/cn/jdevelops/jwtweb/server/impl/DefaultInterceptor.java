package cn.jdevelops.jwtweb.server.impl;


import cn.jdevelops.util.jwt.core.JwtService;
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
            return JwtService.verity(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
