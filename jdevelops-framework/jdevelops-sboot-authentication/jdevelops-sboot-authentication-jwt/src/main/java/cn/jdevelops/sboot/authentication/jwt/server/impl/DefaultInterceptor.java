package cn.jdevelops.sboot.authentication.jwt.server.impl;

import cn.jdevelops.sboot.authentication.jwt.server.CheckTokenInterceptor;
import cn.jdevelops.spi.JoinSPI;
import cn.jdevelops.util.jwt.core.JwtService;

/**
 * @author tn
 * @date 2022-12-28 09:48:22
 */
@JoinSPI(cover = true)
public class DefaultInterceptor implements CheckTokenInterceptor {

    @Override
    public boolean checkToken(String token) {
        try {
            return JwtService.validateTokenByBoolean(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
