package cn.jdevelop.spring.core.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * aop专用的util
 * @author tn
 * @date 2020-10-09 13:03
 */
@Slf4j
public class CommUtil {

    public static<T> void consoleLog(T logs){
        try {
            log.info("api接口调用信息默认输出控制台:"+ JSON.toJSONString(logs));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
