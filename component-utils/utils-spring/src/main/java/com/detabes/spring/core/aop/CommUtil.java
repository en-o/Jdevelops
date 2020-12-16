package com.detabes.spring.core.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tn
 * @ClassName CommUtil
 * @description aop专用的util
 * @date 2020-10-09 13:03
 */
@Slf4j
public class CommUtil {

    public static<T> void consoleLog(T logs){
        try {
            log.info("api接口调用信息默认输出控制台:"+ JSONObject.toJSONString(logs));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
