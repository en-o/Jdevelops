package cn.tannn.jdevelops.utils.http;


import cn.tannn.jdevelops.utils.http.pojo.PingContextRule;
import cn.tannn.jdevelops.utils.http.pojo.PingVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * ping
 * 只能get请求
 * @author tan
 */

public class PingUtil {
    /**
     * logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UpstreamCheckUtils.class);

    /**
     * @param url url(get)
     * @return 运行状态;0、已停止，1、运行中
     */
    public static int ping(String url) {
        try {
            OkHttpTools.Default().get(url);
            return 1;
        } catch (Exception e) {
            LOG.error("url调用失败：{}",e.getMessage());
        }
        return 0;
    }


    /**
     * 有效状态; 1、有校，2、无效
     * 运行状态;0、已停止，1、运行中
     *
     * @param url         url(get)
     * @param contextRule 有效状态规则
     * @return 运行状态和有效状态
     */
    public static PingVO ping(String url, PingContextRule contextRule) {
        // 运行状态;0、已停止，1、运行中
        Integer runningState = 0;
        //有效状态; 1、有校，2、无效
        Integer vaildState = 2;

        try {
            String context =  OkHttpTools.Default().get(url);
            runningState = 1;
            // 词组
            if (contextRule != null && null != contextRule.getRule()) {
                List<String> rules = Arrays.asList(StringUtils.split(contextRule.getRule(), ","));
                for (String rule : rules) {
                    if (contextRule.getRuleType() == 1) {
                        if (context.contains(rule)) {
                            vaildState = 1;
                            break;
                        } else {
                            vaildState = 2;
                        }
                    } else {
                        if (context.contains(rule)) {
                            vaildState = 2;
                            break;
                        } else {
                            vaildState = 1;
                        }
                    }
                }
            }else{
                vaildState = 1;
            }
        } catch (
                Exception e) {
            LOG.error("url调用失败：{}",e.getMessage());
        }
        return new PingVO(runningState, vaildState);

    }


}
