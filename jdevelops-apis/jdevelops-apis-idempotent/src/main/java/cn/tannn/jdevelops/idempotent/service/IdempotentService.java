package cn.tannn.jdevelops.idempotent.service;



import cn.tannn.jdevelops.idempotent.annotation.ApiIdempotent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tnnn
 * @version V1.0
 * @date 2022-11-02 14:46
 */
public interface IdempotentService {

     /**
      * 检查接口重复调用
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @param methodAnnotation 注解里的东西可能要用
      * @return true 无重复调用
      */
     boolean checkApiRedo(HttpServletRequest request, HttpServletResponse response, ApiIdempotent methodAnnotation);
}
