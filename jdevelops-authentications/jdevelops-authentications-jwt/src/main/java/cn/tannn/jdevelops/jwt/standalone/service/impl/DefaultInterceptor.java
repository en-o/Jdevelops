package cn.tannn.jdevelops.jwt.standalone.service.impl;

import cn.tannn.jdevelops.jwt.standalone.service.CheckTokenInterceptor;
import cn.tannn.jdevelops.spi.JoinSPI;
import cn.tannn.jdevelops.utils.jwt.core.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tn
 * @date 2022-12-28 09:48:22
 */
@JoinSPI(cover = true)
public class DefaultInterceptor implements CheckTokenInterceptor {
    Logger logger = LoggerFactory.getLogger(DefaultInterceptor.class);

    @Override
    public boolean checkToken(String token) {
        try {
            return JwtService.validateTokenByBoolean(token);
        }catch (Exception e){
            logger.error("验证token",e);
        }
        return false;
    }

}
