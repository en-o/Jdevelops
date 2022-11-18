package cn.jdevelops.idempotent.service;


import javax.servlet.http.HttpServletRequest;

/**
 * @author tnnn
 * @version V1.0
 * @date 2022-11-02 14:46
 */
public interface IdempotentService {

     /**
      * 检查接口是否重复调用
      * @param request request
      */
     boolean checkApiRedo(HttpServletRequest request);
}
