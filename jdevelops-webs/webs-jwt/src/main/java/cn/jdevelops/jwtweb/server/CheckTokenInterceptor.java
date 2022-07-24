package cn.jdevelops.jwtweb.server;


import cn.jdevelops.spi.SPI;

/**
 * 请各自的服务去实现该接口, 并注入到spring上下文中去
 * @author Tianms
 * @date 2020/4/19 11:18
 */
@SPI
public interface CheckTokenInterceptor {

    /**
     * token校验方法  默认不放行
     * @param token token
     * @return boolean
     */
    boolean checkToken(String token);


    /**
     * 刷新token缓存 (默认不刷新
     * @param userCode 用户唯一编码
     */
    void refreshToken(String userCode);
}
