package cn.jdevelops.api.idempotent.service;


import javax.servlet.http.HttpServletRequest;

/**
 * @author tnnn
 * @version V1.0
 * @date 2022-11-02 14:46
 */
public interface IdempotentService {

     /**
      * 检查接口重复调用
      * @param request request
      * @return true 无重复调用
      */
     boolean checkApiRedo(HttpServletRequest request);
}
