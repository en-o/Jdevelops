package com.detabes.redis.pub.server;

import com.detabes.string.core.StringFormat;
import org.springframework.data.redis.connection.Message;

/**
 * @ClassName  ReisReciverServer
 * @description  消息订阅   ---  处理接收到的数据0
 * @author  tn
 * @date 2020-09-11 10:11
 */
public interface RedisReceiverServer<T> {
    /**
     *  消息接收
     * @param message
     * @return t
     */
    T pubMessage(Message message);
}
