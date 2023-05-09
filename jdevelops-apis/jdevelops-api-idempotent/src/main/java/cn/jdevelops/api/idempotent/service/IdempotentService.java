package cn.jdevelops.api.idempotent.service;


import cn.jdevelops.api.idempotent.annotation.ApiIdempotent;

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
      * @param methodAnnotation 注解里的东西可能要用
      * @return true 无重复调用
      */
     boolean checkApiRedo(HttpServletRequest request, ApiIdempotent methodAnnotation);
}
