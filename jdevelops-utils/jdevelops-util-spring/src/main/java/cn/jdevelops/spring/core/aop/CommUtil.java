package cn.jdevelops.spring.core.aop;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * aop专用的util
 * @author tn
 * @date 2020-10-09 13:03
 */
public class CommUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CommUtil.class);
    public static<T> void consoleLog(T logs){
        try {
            LOG.info("api接口调用信息默认输出控制台:"+ JSON.toJSONString(logs));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
