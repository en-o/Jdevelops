package cn.tannn.jdevelops.mq.redis;


import cn.tannn.jdevelops.mq.redis.exception.ResMqException;
import com.alibaba.fastjson2.JSON;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 消息队列模板抽象
 *
 * @author tan
 */
public abstract class MqService {

    /**
     * object to json
     *
     * @param message 消息对象
     * @return json str
     */
    public String toJsonStr(Object message) {
        return JSON.toJSONString(message);
    }

    /**
     * 同步发送消息 - 主要功能
     *
     * @param topic 主题
     * @param message 消息
     * @return 发送状态
     */
    public abstract boolean syncSend_Object(String topic, Object message);

    /**
     * 同步发送延迟消息
     *
     * @param topic     主题
     * @param message   消息
     * @param delayTime 延迟时间
     * @param timeUnit  时间单位
     * @return 发送状态
     */
    public boolean syncDelaySend(String topic, Object message, int delayTime, TimeUnit timeUnit) {
        throw new ResMqException("同步发送延迟消息功能未被实现");
    }

    public abstract boolean syncSend_Map(String topic, Map<String, Object> message);
}
